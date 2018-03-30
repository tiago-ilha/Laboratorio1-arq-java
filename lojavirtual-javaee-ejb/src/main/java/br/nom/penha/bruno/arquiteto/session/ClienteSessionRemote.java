package br.nom.penha.bruno.arquiteto.session;

import java.util.List;

import br.nom.penha.bruno.arquiteto.exception.LojaVirtualException;
import br.nom.penha.bruno.arquiteto.model.Cliente;


public interface ClienteSessionRemote {
    public List getAllClientes() throws LojaVirtualException;

    public List getByNome(String nome) throws LojaVirtualException;

    public Cliente getByEmail(String email) throws LojaVirtualException;

    public Cliente getCliente(Integer id) throws LojaVirtualException;

    public Cliente salvarCliente(Cliente cliente) throws LojaVirtualException;
    
    public void excluirCliente(Cliente cliente) throws LojaVirtualException;
    
}
