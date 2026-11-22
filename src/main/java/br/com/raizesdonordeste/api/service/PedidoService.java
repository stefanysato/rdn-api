package br.com.raizesdonordeste.api.service;

import br.com.raizesdonordeste.api.dto.ItemPedidoResponse;
import br.com.raizesdonordeste.api.dto.PedidoRequest;
import br.com.raizesdonordeste.api.dto.PedidoResponse;
import br.com.raizesdonordeste.api.model.*;
import br.com.raizesdonordeste.api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UnidadeRepository unidadeRepository;
    private final EstoqueUnidadeRepository estoqueUnidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContaFidelidadeRepository  contaFidelidadeRepository;
    private final AuditoriaService auditoriaService;

    public PedidoService(
            PedidoRepository pedidoRepository,
            UnidadeRepository unidadeRepository,
            EstoqueUnidadeRepository estoqueUnidadeRepository,
            UsuarioRepository usuarioRepository,
            ContaFidelidadeRepository contaFidelidadeRepository,
            AuditoriaService auditoriaService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.unidadeRepository = unidadeRepository;
        this.estoqueUnidadeRepository = estoqueUnidadeRepository;
        this.usuarioRepository =  usuarioRepository;
        this.contaFidelidadeRepository = contaFidelidadeRepository;
        this.auditoriaService = auditoriaService;
    }


    @Transactional
    public PedidoResponse registrarPedido(
            PedidoRequest request,
            Long usuarioId
    ){
        Unidade unidade = unidadeRepository.findById(request.unidadeId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Unidade não encontrada"
                ));

        Usuario registradoPor = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado."
                ));

        ContaFidelidade contaFidelidade = null;

        if (registradoPor.getRole() == Role.CLIENTE) {
            contaFidelidade = contaFidelidadeRepository
                    .findByTitular_Id(registradoPor.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Conta de fidelidade do cliente não encontrada"
                    ));
        }

        // agrupa os produtos repetidos
        Map<Long, Integer> quantidadesPorProduto = new HashMap<>();

        for (var item : request.itens()) {
            quantidadesPorProduto.merge(
                    item.produtoId(),
                    item.quantidade(),
                    Integer::sum
            );
        }

        // consulta e valida os estoques
        Map<Long, EstoqueUnidade> estoques = new HashMap<>();

        for (var entrada : quantidadesPorProduto.entrySet()) {
            Long produtoId = entrada.getKey();
            Integer quantidadeSolicitada = entrada.getValue();

            EstoqueUnidade estoque = estoqueUnidadeRepository
                    .findByProduto_IdAndUnidade_Id(produtoId, unidade.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Produto não encontrado no estoque desta unidade."
                    ));

            if (estoque.getQuantidade() < quantidadeSolicitada) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para o produto ID " + produtoId + "."
                );
            }

            estoques.put(produtoId, estoque);
        }

        // criação do pedido
        Pedido pedido = new Pedido(
                unidade,
                registradoPor,
                contaFidelidade,
                request.canal()
        );

        // adiciona os itens com os preços do estoque
        for (var itemRequest : request.itens()) {
            EstoqueUnidade estoque = estoques.get(itemRequest.produtoId());

            pedido.adicionarItem(
                    estoque.getProduto(),
                    itemRequest.quantidade(),
                    estoque.getPreco()
            );
        }

        // baixa do estoque
        for (var entrada : quantidadesPorProduto.entrySet()) {
            EstoqueUnidade estoque = estoques.get(entrada.getKey());
            estoque.retirarEstoque(entrada.getValue());
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        auditoriaService.registrar(
                registradoPor,
                AcaoAuditoria.CRIACAO_PEDIDO,
                "PEDIDO",
                pedidoSalvo.getId(),
                "Pedido criado. Canal: " + pedidoSalvo.getCanal()
        );

        return converterParaResponse(pedidoSalvo);
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id, Long usuarioId) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pedido não encontrado."
                ));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado."
                ));

        if (usuario.getRole() == Role.CLIENTE) {

            if (!pedido.getRegistradoPor().getId().equals(usuarioId)) {
                throw new AccessDeniedException(
                        "Você não possui permissão para consultar este pedido."
                );
            }

        } else {
            validarAcessoUnidade(usuario, pedido.getUnidade().getId());
        }

        return converterParaResponse(pedido);
    }


    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado."
                ));

        return buscarPedidosPermitidos(usuario, null)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorCanal(CanalPedido canal, Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado."
                ));

        return buscarPedidosPermitidos(usuario, canal)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }


    @Transactional
    public PedidoResponse atualizarStatus(Long id, StatusPedido novoStatus, Long usuarioId) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pedido não encontrado."
                ));

        Usuario usuarioResponsavel = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário responsável não encontrado."
                ));

        validarAcessoUnidade(
                usuarioResponsavel,
                pedido.getUnidade().getId()
        );

        StatusPedido statusAnterior = pedido.getStatus();

        pedido.atualizarStatus(novoStatus);

        auditoriaService.registrar(
                usuarioResponsavel,
                AcaoAuditoria.ALTERACAO_STATUS_PEDIDO,
                "PEDIDO",
                pedido.getId(),
                "Status alterado de " + statusAnterior + " para " + novoStatus
        );

        return converterParaResponse(pedido);
    }

    @Transactional
    public PedidoResponse cancelarPedido(Long id, Long usuarioId) {
        return atualizarStatus(id, StatusPedido.CANCELADO, usuarioId);
    }

    private void validarAcessoUnidade(Usuario usuario, Long unidadeId) {

        if (usuario.getRole() == Role.ADMINISTRADOR) {
            return;
        }

        if (usuario.getUnidade() == null ||
                !usuario.getUnidade().getId().equals(unidadeId)) {

            throw new AccessDeniedException(
                    "Você não possui permissão para acessar esta unidade."
            );
        }
    }


    private List<Pedido> buscarPedidosPermitidos(Usuario usuario, CanalPedido canal) {

        if (usuario.getRole() == Role.ADMINISTRADOR) {
            return canal == null
                    ? pedidoRepository.findAll()
                    : pedidoRepository.findAllByCanal(canal);
        }

        if (usuario.getRole() == Role.CLIENTE) {
            return canal == null
                    ? pedidoRepository.findAllByRegistradoPor_Id(usuario.getId())
                    : pedidoRepository.findAllByCanalAndRegistradoPor_Id(
                    canal, usuario.getId()
            );
        }

        if (usuario.getUnidade() == null) {
            throw new AccessDeniedException(
                    "Usuário não está vinculado a uma unidade."
            );
        }

        Long unidadeId = usuario.getUnidade().getId();

        return canal == null
                ? pedidoRepository.findAllByUnidade_Id(unidadeId)
                : pedidoRepository.findAllByCanalAndUnidade_Id(canal, unidadeId);
    }


    private PedidoResponse converterParaResponse(Pedido pedido) {

        List<ItemPedidoResponse> itens = pedido.getItens()
                .stream()
                .map(item -> new ItemPedidoResponse(
                        item.getProduto().getId(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.calcularSubtotal()
                ))
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getUnidade().getId(),
                pedido.getRegistradoPor().getId(),
                pedido.getCanal(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getCriadoEm(),
                itens
        );
    }

}
