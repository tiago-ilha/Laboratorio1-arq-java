package br.nom.penha.bruno.arquiteto.exception;

/**
 */

public class LojaVirtualException extends Exception {
  /**
	 * 
	 */
	private static final long serialVersionUID = -4245059331835050931L;
public LojaVirtualException() {
  }

  public LojaVirtualException(String msg) {
    super(msg);
  }
  public LojaVirtualException(String msg, Throwable t) {
    super(msg, t);
  }
}