package br.nom.penha.bruno.arquiteto.dao;

public class DAOException extends Exception{
    
    /** Creates a new instance of DAOException */
    public DAOException() {
    }
    
    public DAOException(String message){
        super(message);
    }
    
    public DAOException(String message, Throwable exception) {
        super(message,exception);
    }
    
}
