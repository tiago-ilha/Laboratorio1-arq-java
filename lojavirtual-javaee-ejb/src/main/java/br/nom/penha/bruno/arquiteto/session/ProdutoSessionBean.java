/*
 * ProdutoSessionBean.java
 *
 */

package br.nom.penha.bruno.arquiteto.session;

import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import br.nom.penha.bruno.arquiteto.exception.LojaVirtualException;
import br.nom.penha.bruno.arquiteto.model.Categoria;
import br.nom.penha.bruno.arquiteto.model.Produto;

@Remote(value=ProdutoSessionRemote.class)
@Stateless(mappedName="ejb/ProdutoSession")
public class ProdutoSessionBean implements ProdutoSessionRemote {
  
  @PersistenceContext(type=PersistenceContextType.TRANSACTION) 
  EntityManager entityManager;
    
  
  private static final String ALL_PRODUTOS = "select p from Produto p ";
  private static final String ALL_CATEGORIAS = "select c from Categoria c";
          
  
  public void salvarProduto(Produto produto) throws LojaVirtualException {
    if (produto.getId() == null) {
      entityManager.persist(produto);
    } else {
      entityManager.merge(produto);
    }
  }

  public Produto getProduto(Integer id) throws LojaVirtualException {
    return entityManager.find(Produto.class, id);
  }

  public Categoria getCategoria(Integer id) throws LojaVirtualException {
    return entityManager.find(Categoria.class, id);
  }

  public void salvarCategoria(Categoria categoria) throws LojaVirtualException {
    if (categoria.getId() == null) {
      entityManager.persist(categoria);
    } else {
      entityManager.merge(categoria);
    }
  }

  public List getAllCategorias() throws LojaVirtualException {
    Query q = entityManager.createQuery(ALL_CATEGORIAS);
    return q.getResultList();
  }

  public List getAllProdutos() throws LojaVirtualException {
    Query q = entityManager.createQuery(ALL_PRODUTOS);
    return q.getResultList();    
  }

  public List getProdutos(String nome, Integer idCategoria, String marca) throws LojaVirtualException {
    
    if (nome == null && idCategoria == null && marca == null) {
      return getAllProdutos();
    }
    
    String connector = " where ";
    String jpql = ALL_PRODUTOS;
    
    if (nome != null) {
      jpql += connector + "p.nome like :nome";
      connector = " and ";
    }
    
    if (idCategoria != null) {
      jpql += connector + "p.categoria.id = :idCategoria";
      connector = " and ";
    }
    
    if (marca != null) {
      jpql += connector + "p.marca like :marca";
    }
    
    Query q = entityManager.createQuery(jpql);
    if (nome != null) q.setParameter("nome", "%" + nome + "%");
    if (idCategoria != null) q.setParameter("idCategoria", idCategoria);
    if (marca != null) q.setParameter("marca", "%" + marca + "%");
    return q.getResultList();
    
  }

  public void excluirProduto(Produto produto) throws LojaVirtualException {
    produto = entityManager.merge(produto);
    entityManager.remove(produto);
  }

    public void excluirCategoria(Categoria categoria) throws LojaVirtualException {
    categoria = entityManager.merge(categoria);
    entityManager.remove(categoria);
    }
  

}
