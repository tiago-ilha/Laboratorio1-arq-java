package br.nom.penha.bruno.arquiteto.web.controller;

import br.nom.penha.bruno.arquiteto.comparator.ComparatorPedidoClienteNumero;
import br.nom.penha.bruno.arquiteto.model.Pagamento;
import br.nom.penha.bruno.arquiteto.model.PagamentoBoleto;
import br.nom.penha.bruno.arquiteto.model.Pedido;
import br.nom.penha.bruno.arquiteto.session.PedidoSessionRemote;

import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Arquiteto
 */
public class PedidoController {

    @EJB
    PedidoSessionRemote ejb;
    private ListDataModel pedidos;
    private Pedido pedido;

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public boolean isBoleto() {
        Pagamento pagamento = pedido.getPagamento();
        if (pagamento == null) {
            return false;
        } else {
            return pagamento instanceof PagamentoBoleto;
        }
    }

    public void setPedidos(ListDataModel pedidos) {
        this.pedidos = pedidos;
    }

    public ListDataModel getPedidos() {
        try {
            List pedidosList = ejb.getAllPedidos();
            if (pedidosList != null && pedidosList.size() > 0) {
                Collections.sort(pedidosList, new ComparatorPedidoClienteNumero());
            }
            pedidos = new ListDataModel(pedidosList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public String verDetalhes() {
        pedido = (Pedido) pedidos.getRowData();
        return "verPedido";
    }
}
