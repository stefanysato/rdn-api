package br.com.raizesdonordeste.api.repository;

import br.com.raizesdonordeste.api.model.CanalPedido;
import br.com.raizesdonordeste.api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByCanal(CanalPedido canal);

    List<Pedido> findAllByUnidade_Id(Long unidadeId);

    List<Pedido> findAllByCanalAndUnidade_Id(CanalPedido canal, Long unidadeId);

    List<Pedido> findAllByRegistradoPor_Id(Long usuarioId);

    List<Pedido> findAllByCanalAndRegistradoPor_Id(CanalPedido canal, Long usuarioId);
}
