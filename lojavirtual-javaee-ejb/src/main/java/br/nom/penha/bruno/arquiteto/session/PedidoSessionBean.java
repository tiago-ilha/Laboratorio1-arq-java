/*
 * PedidoSessionBean.java
 *
 */

package br.nom.penha.bruno.arquiteto.session;

import br.nom.penha.bruno.arquiteto.dao.collection.BancoCollection;
import br.nom.penha.bruno.arquiteto.dao.collection.BandeiraCartaoCreditoCollection;
import br.nom.penha.bruno.arquiteto.exception.LojaVirtualException;
import br.nom.penha.bruno.arquiteto.jms.ClienteJMS;
import br.nom.penha.bruno.arquiteto.model.Banco;
import br.nom.penha.bruno.arquiteto.model.BandeiraCartaoCredito;
import br.nom.penha.bruno.arquiteto.model.Cliente;
import br.nom.penha.bruno.arquiteto.model.Pagamento;
import br.nom.penha.bruno.arquiteto.model.PagamentoCartaoCredito;
import br.nom.penha.bruno.arquiteto.model.Pedido;
import br.nom.penha.bruno.arquiteto.util.FormatHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@Local(value=PedidoSessionLocal.class)
@Remote(value=PedidoSessionRemote.class)
@Stateless(mappedName="ejb/PedidoSession")
public class PedidoSessionBean implements PedidoSessionRemote, PedidoSessionLocal {
  
  @PersistenceContext(type=PersistenceContextType.TRANSACTION) 
  EntityManager entityManager;  
  
  private static final String ALL_PEDIDOS = "select p from Pedido p ";
  private static final String PEDIDO_BY_NUMERO = ALL_PEDIDOS + "join fetch p.itensPedido where p.numero = :numero";
  private static final String PEDIDO_BY_CLIENTE = ALL_PEDIDOS + " where p.cliente = :cliente ";
  
  public List getAllPedidos() {
    Query q = entityManager.createQuery(ALL_PEDIDOS);
    q.setMaxResults(200);
    return q.getResultList();
  }

  public Pedido getPedido(Long numero) {
    Query q = entityManager.createQuery(PEDIDO_BY_NUMERO);
    q.setParameter("numero", new Long(numero));
    return (Pedido) q.getSingleResult();
  }

  public void salvarPedido(Pedido pedido) {
    if (pedido.getNumero() == null) {
      entityManager.persist(pedido);
    } else {
      entityManager.merge(pedido);
    }
  }

  public List getPedidoByCliente(Cliente cliente) {
    Query q = entityManager.createQuery(PEDIDO_BY_CLIENTE);
    q.setParameter("cliente", cliente);
    q.setMaxResults(200);
    return q.getResultList();
  }

  public List getPedidoByPeriodo(String inicio, String fim) throws LojaVirtualException{

    boolean inicioOk = (inicio != null && inicio.trim().length() > 0);
    boolean fimOk = (fim != null && fim.trim().length() > 0);
    
    
    if (! (inicioOk || fimOk)) {
      throw new LojaVirtualException("Periodo invalido");
    }
    
    Date dataInicio = null, dataFim = null;
    String jpql = ALL_PEDIDOS;
    String connector = " where ";
    
    try {
      if (inicioOk) {
        dataInicio = FormatHelper.getInstance().parseSimpleDate(inicio + " 00:00");
        jpql += connector + "p.data >= :dataInicio";
        connector = " and ";
      }
      if (fimOk) {
        dataFim = FormatHelper.getInstance().parseSimpleDate(fim + " 23:59");
        jpql += connector + "p.data <= :dataFim";
      }
    } catch (ParseException ex) {
      throw new LojaVirtualException("Periodo invalido", ex);
    }
    
    Query q = entityManager.createQuery(jpql);
    if (dataInicio != null) q.setParameter("dataInicio", dataInicio);
    if (dataFim != null) q.setParameter("dataFim", dataFim);
    q.setMaxResults(200);
    return q.getResultList();
    
  }

  public Pagamento getPagamento(Integer id) {
    return entityManager.find(Pagamento.class, id);
  }

  public Banco getBanco(Integer id) {
    return BancoCollection.getInstance().getByPrimaryKey(id);
  }

  public BandeiraCartaoCredito getBandeiraCartaoCredito(Integer id){
    return BandeiraCartaoCreditoCollection.getInstance().getByPrimaryKey(id);
  }

  public Pagamento salvarPagamento(Pagamento pagamento){
    if (pagamento.getId() == null) {
      entityManager.persist(pagamento);
    } else {
      entityManager.merge(pagamento);
    }
    entityManager.flush();
    return pagamento;
  }

    public Pedido enviarPedido(Pedido pedido) throws LojaVirtualException {
       try {
           pedido.setStatus("Enviado");
           salvarPedido(pedido);
           if(pedido.getPagamento() instanceof PagamentoCartaoCredito) {
               ClienteJMS.getInstance().enviarMensagem(pedido,"java:app/queue/ProcessarPedido");
           }
        } catch(Exception e) {
            throw new LojaVirtualException("Erro ao enviar pedido",e);
        }
        return pedido;
    }
  
}
