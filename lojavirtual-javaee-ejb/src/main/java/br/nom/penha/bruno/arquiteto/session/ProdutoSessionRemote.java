package br.nom.penha.bruno.arquiteto.session;

import java.util.List;

import br.nom.penha.bruno.arquiteto.exception.LojaVirtualException;
import br.nom.penha.bruno.arquiteto.model.Categoria;
import br.nom.penha.bruno.arquiteto.model.Produto;


public interface ProdutoSessionRemote {
    public void salvarProduto(Produto produto) throws  LojaVirtualException;

    public Produto getProduto(Integer id) throws LojaVirtualException;

    public Categoria getCategoria(Integer id) throws LojaVirtualException;

    public void salvarCategoria(Categoria categoria) throws LojaVirtualException;

    public List getAllCategorias() throws LojaVirtualException;

    public List getAllProdutos() throws LojaVirtualException;

    public List getProdutos(String nome, Integer idCategoria, String marca) throws LojaVirtualException;

    public void excluirProduto(Produto produto) throws LojaVirtualException;
    
    public void excluirCategoria(Categoria categoria) throws LojaVirtualException;
    
}
