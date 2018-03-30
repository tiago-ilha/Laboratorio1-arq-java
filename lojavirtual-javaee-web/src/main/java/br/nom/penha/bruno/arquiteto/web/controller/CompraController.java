/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.nom.penha.bruno.arquiteto.web.controller;

import br.nom.penha.bruno.arquiteto.dao.collection.BancoCollection;
import br.nom.penha.bruno.arquiteto.dao.collection.BandeiraCartaoCreditoCollection;
import br.nom.penha.bruno.arquiteto.model.Banco;
import br.nom.penha.bruno.arquiteto.model.BandeiraCartaoCredito;
import br.nom.penha.bruno.arquiteto.model.CarrinhoCompras;
import br.nom.penha.bruno.arquiteto.model.Cliente;
import br.nom.penha.bruno.arquiteto.model.ItemCompra;
import br.nom.penha.bruno.arquiteto.model.Pagamento;
import br.nom.penha.bruno.arquiteto.model.PagamentoBoleto;
import br.nom.penha.bruno.arquiteto.model.PagamentoCartaoCredito;
import br.nom.penha.bruno.arquiteto.model.Pedido;
import br.nom.penha.bruno.arquiteto.model.Produto;
import br.nom.penha.bruno.arquiteto.session.PedidoSessionRemote;
import br.nom.penha.bruno.arquiteto.session.ProdutoSessionRemote;
import br.nom.penha.bruno.arquiteto.web.util.JSFHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Arquiteto
 */
public class CompraController {

    private final static Logger logger = Logger.getLogger(CompraController.class);
    public static String CARTAO_CREDITO = "cartao";
    public static String BOLETO = "boleto";
    private ListDataModel produtos;
    private CarrinhoCompras carrinho;
    private String formaPagamentoSelecionada = "cartao";
    private Cliente cliente;
    private boolean boleto = false;
    private List formasPagamento;
    private Pagamento pagamento = new PagamentoCartaoCredito();
    
    @EJB
    ProdutoSessionRemote produtoEJB;
    
    @EJB
    PedidoSessionRemote pedidoEJB;

    public CompraController() {
        pagamento = new PagamentoCartaoCredito();
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        cliente = (Cliente) session.getAttribute("usuario");
        if(cliente == null) {
            cliente = new Cliente();
        }
    }

    public void setProdutos(ListDataModel produtos) {
        this.produtos = produtos;
    }

    public ListDataModel getProdutos() {
        try {
            if (produtos == null) {
                List<Produto> lista = (List<Produto>) produtoEJB.getAllProdutos();
                produtos = new ListDataModel(lista);
            }
        } catch (Exception e) {
            String msg = "Erro ao carregar produtos";
            logger.error(msg, e);
            JSFHelper.addErrorMessage(msg);
            return new ListDataModel(new ArrayList());
        }
        return produtos;
    }

    public void setCarrinho(CarrinhoCompras carrinho) {
        this.carrinho = carrinho;
    }

    public CarrinhoCompras getCarrinho() {
        return carrinho;
    }

    public void setFormaPagamentoSelecionada(String formaPagamento) {
        this.formaPagamentoSelecionada = formaPagamento;
        if (BOLETO.equals(formaPagamento)) {
            boleto = true;
        } else {
            boleto = false;
        }
    }

    public List getBancos() {
        List listBancos = null;
        try {
            listBancos = BancoCollection.getInstance().getAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List bancos = new ArrayList();
        if (listBancos != null && listBancos.size() > 0) {
            for (Iterator it = listBancos.iterator(); it.hasNext();) {
                Banco banco = (Banco) it.next();
                bancos.add(new SelectItem(banco, banco.getNome()));
            }
        }
        return bancos;
    }

    public void setBoleto(boolean boleto) {
        this.boleto = boleto;
    }

    public boolean isBoleto() {
        return boleto;
    }

    public void setFormasPagamento(List formasPagamento) {
        this.formasPagamento = formasPagamento;
    }

    public List getFormasPagamento() {
        formasPagamento = new ArrayList();
        formasPagamento.add(new SelectItem(BOLETO, BOLETO));
        formasPagamento.add(new SelectItem(CARTAO_CREDITO, CARTAO_CREDITO));
        return formasPagamento;
    }

    public String getFormaPagamentoSelecionada() {
        return formaPagamentoSelecionada;
    }

    public List getBandeiras() {
        List listBandeiras = null;
        try {
            listBandeiras = BandeiraCartaoCreditoCollection.getInstance().getAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List bandeiras = new ArrayList();
        if (listBandeiras != null && listBandeiras.size() > 0) {
            for (Iterator it = listBandeiras.iterator(); it.hasNext();) {
                BandeiraCartaoCredito bandeira = (BandeiraCartaoCredito) it.next();
                bandeiras.add(new SelectItem(bandeira, bandeira.getNome()));
            }
        }
        return bandeiras;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void atualizarFormaPagamento(ValueChangeEvent valueChangeEvent) {
        logger.info("[ValueChangeListener] - pagamento=" + pagamento);
        if (valueChangeEvent.getNewValue().equals("boleto")) {
            this.setBoleto(true);
            pagamento = new PagamentoBoleto(pagamento);
        } else {
            this.setBoleto(false);
            pagamento = new PagamentoCartaoCredito(pagamento);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public String adicionarCarrinho() {
        logger.info("Adicionar produto no carrinho");
        Produto produtoSelecionado = (Produto) this.produtos.getRowData();
        logger.info("Produto selecionado pelo cliente " + produtoSelecionado);
        ItemCompra item = new ItemCompra(produtoSelecionado, 1, 0);
        //caso não exista um carrinho, cria-se um e adiciona-se o item desejado
        if (carrinho == null) {
            carrinho = new CarrinhoCompras(item);
        } else {
            carrinho.addItem(item);
        }
        return "carrinhoCompras";
    }

    public String enviarPedido() {
        try {
            pagamento.setDataPagamento(new Date());
            pagamento.setValor(carrinho.getPrecoTotal());
            if (pagamento instanceof PagamentoCartaoCredito) {
                ((PagamentoCartaoCredito) pagamento).setBandeiraCartaoCredito(new BandeiraCartaoCredito(new Integer(1), "Visa"));
            } else if (pagamento instanceof PagamentoBoleto) {
                ((PagamentoBoleto) pagamento).setBanco(new Banco(new Integer(1), "Banco do Brasil"));
            }

            //Criacao do pedido
            String clienteBrowser = "";
            String clienteIP = "";
            String status = "";

            Pedido pedido =
                    new Pedido(cliente, clienteBrowser, clienteIP, new Date(),
                    carrinho.getItens(), pagamento, status);
            logger.info("[CatalogoProdutosManagedBean ] Enviando pedido para o banco de dados " + pedido);
            pedidoEJB.enviarPedido(pedido);
            JSFHelper.addSuccessMessage("Pedido enviado com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JSFHelper.addErrorMessage("Erro ao salvar os dados do pedido no envio do pedido");
        }
        return "pedidoEnviado";
    }
}
