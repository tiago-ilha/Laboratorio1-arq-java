/*
 * ClienteSessionBean.java
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
import br.nom.penha.bruno.arquiteto.model.Cliente;

/**
 *
 */
@Remote(value = ClienteSessionRemote.class)
@Stateless(mappedName = "ejb/ClienteSession")
public class ClienteSessionBean implements ClienteSessionRemote {

    @PersistenceContext(unitName = "lojavirtualPU", type = PersistenceContextType.TRANSACTION)
    EntityManager entityManager;
    private static final String ALL = "select c from Cliente c";
    private static final String BY_NOME = ALL + " where c.nome like :nome";
    private static final String BY_EMAIL = ALL + " where c.email = :email";

    public List getAllClientes() throws LojaVirtualException {
        Query q = entityManager.createQuery(ALL);
        return q.getResultList();
    }

    public List getByNome(String nome) throws LojaVirtualException {
        Query q = entityManager.createQuery(BY_NOME);
        q.setParameter("nome", "%" + nome + "%");
        return q.getResultList();
    }

    public Cliente getByEmail(String email) throws LojaVirtualException {
        Query q = entityManager.createQuery(BY_EMAIL);
        q.setParameter("email", email);
        return (Cliente) q.getSingleResult();
    }

    public Cliente getCliente(Integer id) throws LojaVirtualException {
        return entityManager.find(Cliente.class, id);
    }

    public Cliente salvarCliente(Cliente cliente) throws LojaVirtualException {
        if (cliente.getId() == null) {
            entityManager.persist(cliente);
        } else {
            cliente = entityManager.merge(cliente);
        }
        entityManager.flush();
        return cliente;
    }

    public void excluirCliente(Cliente cliente) throws LojaVirtualException {
        cliente = entityManager.merge(cliente);
        entityManager.remove(cliente);
    }
}
