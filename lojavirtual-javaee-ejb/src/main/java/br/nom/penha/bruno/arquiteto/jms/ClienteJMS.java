package br.nom.penha.bruno.arquiteto.jms;

import java.io.Serializable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.nom.penha.bruno.arquiteto.exception.LojaVirtualException;

public class ClienteJMS {
    
    private static ClienteJMS instance = new ClienteJMS();
    private Context ctx;
    
    /** Creates a new instance of ClienteJMS */
    private ClienteJMS() {
        try {
            initContext();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }
    
    public static ClienteJMS getInstance() {
        return instance;
    }
    
    public void enviarMensagem(Serializable objeto, String destino) throws LojaVirtualException {
        try {
            if(ctx==null) {
                initContext();
            }
            ConnectionFactory factory = (ConnectionFactory) ctx.lookup("jms/__defaultConnectionFactory");
            Destination dest = (Destination) ctx.lookup(destino);
            Connection conn = factory.createConnection();
            Session session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(dest);
            ObjectMessage mensagem = session.createObjectMessage(objeto);
            producer.send(mensagem);
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new LojaVirtualException(e.getMessage(),e);
        }
    }
   
    private void initContext() throws NamingException {
        ctx = new InitialContext();
    }
}
