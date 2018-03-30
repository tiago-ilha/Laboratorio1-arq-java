/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.nom.penha.bruno.arquiteto.web.controller;

import br.nom.penha.bruno.arquiteto.model.Cliente;
import br.nom.penha.bruno.arquiteto.session.ClienteSessionRemote;
import br.nom.penha.bruno.arquiteto.web.util.JSFHelper;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.ListDataModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Arquiteto
 */
public class ClienteController {

    private final static Logger logger = Logger.getLogger(ClienteController.class);
    private Cliente cliente = new Cliente();
    private ListDataModel clientes;

    @EJB
    ClienteSessionRemote ejb;
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    
    /**
     * Retorna o último cliente selecionado. Caso o atributo
     * cliente seja null ele é inicializado para uma nova entidade
     * 
     * 
     * @return um cliente
     */
    public Cliente getCliente() {
        if (cliente == null) {
            cliente = new Cliente();
        }
        return cliente;
    }

    /**
     * inicializa o atributo cliente para uma nova entidade
     * 
     * @return "editarCliente"
     */
    public String setup() {
        cliente = new Cliente();
        return "editarCliente";
    }

    /** 
     * método vinculado ao botão salvar do formulário de criação/edição de
     * cliente. Aciona o dao correspondente a cliente
     * 
     * @return null ou "listarClientes"
     */
    public String salvar() {
        try {
            ejb.salvarCliente(cliente);
            reset();
            JSFHelper.addSuccessMessage("cliente foi salvo com sucesso");
        } catch (Exception e) {
            String msg = "Erro ao salvar o cliente";
            logger.error(msg, e);
            JSFHelper.addErrorMessage(msg);
            return null;
        }
        return "listarClientes";
    }

    /**
     * Método vinculado ao link de edição de um cliente. Atualiza
     * o atributo cliente com o cliente selecionado e navega para a página
     * de edição.
     * 
     * @return editarCliente
     */
    public String verDetalhes() {
        cliente = (Cliente) clientes.getRowData();
        return "editarCliente";
    }

    /**
     * Método vinculado ao link de remoção de um cliente. Aciona 
     * o DAO correspondente a cliente
     * 
     * @return null ou "listarClientes"
     */
    public String remover() {
        try {
            cliente = (Cliente) clientes.getRowData();
            ejb.excluirCliente(cliente);
            reset();
            JSFHelper.addSuccessMessage("O cliente foi removida com sucesso");
        } catch (Exception e) {
            String msg = "Erro ao remover o cliente";
            logger.error(msg, e);
            JSFHelper.addErrorMessage(msg);
            return null;
        }
        return "listarClientes";
    }

    /**
     * joga os valores dos atributos para null
     */
    private void reset() {
        clientes = null;
        cliente = null;
    }

    /**
     * retorna todos os clientes. Aciona o DAO correspondente a cliente
     * 
     * @return ListDataModel com todas os protótipos
     */
    public ListDataModel getClientes() {
        try {
            if (clientes == null) {
                List<Cliente> lista = (List<Cliente>) ejb.getAllClientes();
                clientes = new ListDataModel(lista);
            }
        } catch (Exception e) {
            String msg = "Erro ao carregar clientes";
            logger.error(msg, e);
            JSFHelper.addErrorMessage(msg);
            return new ListDataModel(new ArrayList());
        }
        return clientes;
    }
}
