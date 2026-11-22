package br.com.raizesdonordeste.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    void configurar() {
        Unidade unidade = mock(Unidade.class);
        Usuario usuario = mock(Usuario.class);

        pedido = new Pedido(
                unidade,
                usuario,
                null,
                CanalPedido.BALCAO
        );
    }


    @Test
    void adicionarItemAoPedido() {
        Produto produto = mock(Produto.class);

        pedido.adicionarItem(
                produto,
                2,
                new BigDecimal("15.00")
        );

        assertEquals(1, pedido.getItens().size());

        assertEquals(
                pedido,
                pedido.getItens().get(0).getPedido()
        );

        assertEquals(
                new BigDecimal("30.00"),
                pedido.getValorTotal()
        );
    }

    @Test
    void removerItemEAtualizarTotal() {
        Produto produto = mock(Produto.class);

        pedido.adicionarItem(
                produto,
                2,
                new BigDecimal("15.00")
        );

        ItemPedido item = pedido.getItens().get(0);

        pedido.removerItem(item);

        assertTrue(pedido.getItens().isEmpty());

        assertEquals(
                BigDecimal.ZERO,
                pedido.getValorTotal()
        );

        assertNull(item.getPedido());
    }

}
