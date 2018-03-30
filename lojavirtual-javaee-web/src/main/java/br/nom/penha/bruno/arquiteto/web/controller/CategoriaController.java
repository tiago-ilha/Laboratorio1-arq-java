package br.nom.penha.bruno.arquiteto.web.controller;

import br.nom.penha.bruno.arquiteto.model.Categoria;
import br.nom.penha.bruno.arquiteto.session.ProdutoSessionRemote;
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
public class CategoriaController {

    private final static Logger logger = Logger.getLogger(CategoriaController.class);
    private Categoria categoria = new Categoria();
    private ListDataModel categorias;
    
    @EJB
    private ProdutoSessionRemote ejb;

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    
    /**
     * Retorna a última categoria selecionado. Caso o atributo
     * categoria seja null ele é inicializado para uma nova entidade
     * 
     * 
     * @return uma categoria
     */
    public Categoria getCategoria() {
        if (categoria == null) {
            categoria = new Categoria();
        }
        return categoria;
    }

    /**
     * inicializa o atributo categoria para uma nova entidade
     * 
     * @return "editarCategoria"
     */
    public String setup() {
        categoria = new Categoria();
        return "editarCategoria";
    }

    /** 
     * método vinculado ao botão salvar do formulário de criação/edição de
     * categoria. Aciona o dao correspondente a categoria
     * 
     * @return null ou "listarCategorias"
     */
    public String salvar() {
        try {
            ejb.salvarCategoria(categoria);
            reset();
            JSFHelper.addSuccessMessage("categoria foi salva com sucesso");
        } catch (Exception e) {
            String msg = "Erro ao salvar a categoria";
            logger.error(msg, e);
            JSFHelper.addErrorMessage(msg);
            return null;
        }
        return "listarCategorias";
    }

    /**
     * Método vinculado ao link de edição de uma categoria. Atualiza
     * o atributo categoria com a categoria selecionado e navega para a página
     * de edição.
     * 
     * @return editarCategoria
     */
    public String verDetalhes() {
        categoria = (Categoria) categorias.getRowData();
        return "editarCategoria";
    }

    /**
     * Método vinculado ao link de remoção de uma categoria. Aciona 
     * o DAO correspondente a categoria
     * 
     * @return null ou "listarCategorias"
     */
    public String remover() {
        try {
            categoria = (Categoria) categorias.getRowData();
            ejb.excluirCategoria(categoria);
            reset();
            JSFHelper.addSuccessMessage("A categoria foi removida com sucesso");
        } catch (Exception e) {
            String msg = "Erro ao remover a categoria";
            logger.error(msg, e);
            JSFHelper.addErrorMessage(msg);
            return null;
        }
        return "listarCategorias";
    }

    /**
     * joga os valores dos atributos para null
     */
    private void reset() {
        categorias = null;
        categoria = null;
    }

    /**
     * retorna todos os categorias. Aciona o DAO correspondente a categoria
     * 
     * @return ListDataModel com todas os protótipos
     */
    public ListDataModel getCategorias() {
        try {
            System.out.println("================ solicitando getCategorias");
            if (categorias == null) {
                List<Categoria> lista = (List<Categoria>) ejb.getAllCategorias();
                System.out.println(String.format("=============== Tamanho da lista %d",lista.size()));
                categorias = new ListDataModel(lista);
                System.out.println("=============== Categorias " + categorias);
            }
        } catch (Exception e) {
            String msg = "Erro ao carregar categorias";
            logger.error(msg, e);
            JSFHelper.addErrorMessage(msg);
            return new ListDataModel(new ArrayList());
        }
        return categorias;
    }
}
