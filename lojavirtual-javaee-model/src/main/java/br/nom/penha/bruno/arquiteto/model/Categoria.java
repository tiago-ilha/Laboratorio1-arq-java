package br.nom.penha.bruno.arquiteto.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="categorias")
public class Categoria implements Serializable {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String descricao;
	private boolean ativo = true;

	public Categoria(){
	}

	public Categoria(Integer id, String descricao, boolean ativo) {
		this.id = id;
		this.descricao = descricao;
		this.ativo = ativo;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getDescricao(){
		return descricao;
	}

	public void setDescricao(String descricao){
		this.descricao = descricao;
	}

    public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String toString() {
        return getDescricao() + " - " + getId();
    }

    public boolean equals(Object o) {
    	if (o == null) return false;
      	if (this == o) return true;
        boolean result = false;
        if (o instanceof Categoria) {
            Categoria outro = (Categoria) o;
            result = this.getDescricao() != null && 
                     this.getDescricao().equals(outro.getDescricao());
        }
        return result;
    }

    public int hashCode() {
        int result = getDescricao() != null ? getDescricao().hashCode() : 41;
        return result;
    }

}