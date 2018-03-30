package br.nom.penha.bruno.arquiteto.mdb;

import br.nom.penha.bruno.arquiteto.cartaocredito.Driver;
import br.nom.penha.bruno.arquiteto.cartaocredito.DriverFactory;
import br.nom.penha.bruno.arquiteto.jms.ClienteJMS;
import br.nom.penha.bruno.arquiteto.model.PagamentoCartaoCredito;
import br.nom.penha.bruno.arquiteto.model.Pedido;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.apache.log4j.Logger;

@MessageDriven(mappedName="java:app/queue/ProcessarPedido",
activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ProcessadorCartaoBean implements MessageListener {

    private static final Logger logger = Logger.getLogger(ProcessadorCartaoBean.class);

    public void onMessage(Message aMessage) {
        try {
            logger.info("Processando pedido recebido");
            if (!(aMessage instanceof ObjectMessage)) {
                logger.error("Tipo de mensagem invalida recebida");
                return;
            }
            ObjectMessage mensagem = (ObjectMessage) aMessage;
            Object objeto = mensagem.getObject();

            if (!(objeto instanceof Pedido)) {
                logger.error("Objeto invalido recebido na mensagem");
                return;
            }
            Pedido pedido = (Pedido) objeto;
            PagamentoCartaoCredito pagamento = (PagamentoCartaoCredito) pedido.getPagamento();
            Driver driver = DriverFactory.getInstance().getDriver(pagamento.getBandeiraCartaoCredito());
            boolean aprovado = driver.aprovaPagamento(pagamento);
            if (aprovado) {
                pedido.setStatus("Aprovado");
            } else {
                pedido.setStatus("Reprovado");
            }
            ClienteJMS.getInstance().enviarMensagem(pedido, "java:app/queue/Resultado");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
