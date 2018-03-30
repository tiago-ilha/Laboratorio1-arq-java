package br.nom.penha.bruno.arquiteto.dao.collection;

import java.util.ArrayList;
import java.util.List;

import br.nom.penha.bruno.arquiteto.dao.BandeiraCartaoCreditoDAO;
import br.nom.penha.bruno.arquiteto.dao.DAOException;
import br.nom.penha.bruno.arquiteto.model.BandeiraCartaoCredito;


public class BandeiraCartaoCreditoCollection implements BandeiraCartaoCreditoDAO {
    
    private List bandeiras = new ArrayList();
    
    private static BandeiraCartaoCreditoCollection instance = new BandeiraCartaoCreditoCollection();
    
    protected BandeiraCartaoCreditoCollection(){
        bandeiras.add(new BandeiraCartaoCredito(new Integer(1),"Visa"));
        bandeiras.add(new BandeiraCartaoCredito(new Integer(2),"Mastercard"));
        bandeiras.add(new BandeiraCartaoCredito(new Integer(3),"American Express"));
        bandeiras.add(new BandeiraCartaoCredito(new Integer(4),"Diners"));
    }
    
    public static BandeiraCartaoCreditoCollection getInstance() {
        return instance;
    }
    
    protected List getManyByCriteria(String sql) throws DAOException {
        return null;
    }
    
    public List getAll(){
        return bandeiras;
    }
    
    public BandeiraCartaoCredito getByPrimaryKey(Integer id){
        BandeiraCartaoCredito bandeira = null;
        for(int i=0; i<bandeiras.size(); i++){
            BandeiraCartaoCredito bandeiraDaLista = (BandeiraCartaoCredito) bandeiras.get(i);
            if(bandeiraDaLista.getId().equals(id))
                bandeira = bandeiraDaLista;
        }
        return bandeira;
    }
    
    public void delete(BandeiraCartaoCredito bandeira){
    }
    
    public void save(BandeiraCartaoCredito bandeira){
    }
    
}