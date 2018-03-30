package br.nom.penha.bruno.arquiteto.session;

import java.util.List;

import br.nom.penha.bruno.arquiteto.exception.LojaVirtualException;
import br.nom.penha.bruno.arquiteto.model.Banco;
import br.nom.penha.bruno.arquiteto.model.BandeiraCartaoCredito;
import br.nom.penha.bruno.arquiteto.model.Cliente;
import br.nom.penha.bruno.arquiteto.model.Pagamento;
import br.nom.penha.bruno.arquiteto.model.Pedido;


public interface PedidoSessionRemote {
    public List getAllPedidos() throws LojaVirtualException;

    public Pedido getPedido(Long id) throws LojaVirtualException;

    public void salvarPedido(Pedido pedido) throws LojaVirtualException;

    public List getPedidoByCliente(Cliente cliente) throws LojaVirtualException;

    public List getPedidoByPeriodo(String inicio, String fim) throws LojaVirtualException;

    public Banco getBanco(Integer id) throws LojaVirtualException;

    public BandeiraCartaoCredito getBandeiraCartaoCredito(Integer id) throws LojaVirtualException;

    public Pagamento salvarPagamento(Pagamento pagamento) throws LojaVirtualException;

    public Pedido enviarPedido(Pedido pedido) throws LojaVirtualException;
    
}
