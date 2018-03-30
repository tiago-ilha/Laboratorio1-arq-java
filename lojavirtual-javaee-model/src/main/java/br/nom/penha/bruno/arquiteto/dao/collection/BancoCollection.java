package br.nom.penha.bruno.arquiteto.dao.collection;

import java.util.ArrayList;
import java.util.List;

import br.nom.penha.bruno.arquiteto.dao.BancoDAO;
import br.nom.penha.bruno.arquiteto.dao.DAOException;
import br.nom.penha.bruno.arquiteto.model.Banco;


public class BancoCollection implements BancoDAO {
    
    private List bancos = new ArrayList();
    
    private static BancoCollection instance = new BancoCollection();
    
    protected BancoCollection(){
        bancos.add(new Banco(new Integer(1),"Banco do Brasil"));
        bancos.add(new Banco(new Integer(2),"Bradesco"));
        bancos.add(new Banco(new Integer(3),"Itau"));
    }
    
    public static BancoCollection getInstance() {
        return instance;
    }
    
    protected List getManyByCriteria(String sql) throws DAOException {
        return null;
    }
    
    public List getAll(){
        return bancos;
    }
    
    public Banco getByPrimaryKey(Integer id){
        Banco banco = null;
        for(int i=0; i<bancos.size(); i++){
            Banco bancoDaLista = (Banco) bancos.get(i);
            if(bancoDaLista.getId().equals(id))
                banco = bancoDaLista;
        }
        return banco;
    }
    
    public void delete(Banco banco){
    }
    
    public void save(Banco banco){
    }
    
}