package br.nom.penha.bruno.arquiteto.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.nom.penha.bruno.arquiteto.model.Categoria;

public class ConversorCategoria  implements Converter {

    public ConversorCategoria() {
    }

    public Object getAsObject(FacesContext facesContext,
                              UIComponent uIComponent, String novoValor) {        
        StringBuffer texto = new StringBuffer(novoValor);
        int posDescricao = texto.indexOf("descricaoCategoria=");
        int posAtivo = texto.indexOf("ativoCategoria=");
        int posId = texto.indexOf("idCategoria=");
        String descricao = texto.substring(posDescricao + 19, posAtivo-1);
        boolean ativo = new Boolean(texto.substring(posAtivo + 15, posId-1));
        Integer id = new Integer(texto.substring(posId + 12, texto.length()));
        return new Categoria(id, descricao, ativo);
    }

    public String getAsString(FacesContext facesContext,
                              UIComponent uIComponent, Object object) {                              
        Categoria categoria = (Categoria)object;
        return "descricaoCategoria="+categoria.getDescricao()+
            "&ativoCategoria="+categoria.isAtivo()+
            "&idCategoria="+categoria.getId();

    }
}
