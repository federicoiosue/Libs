package it.autostrade.feio.utils;

public class ErrorBean implements java.io.Serializable {



	private String nome;

	private boolean deceduto;



	// Costruttore senza argomenti
	public ErrorBean() {}



	// Proprietà "nome" (da notare l'uso della maiuscola) lettura / scrittura
	public String getNome() {
		return this.nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	// Diversa sintassi per gli attributo boolean ( 'is' al posto di 'get' )
	public boolean isDeceduto() {
		return this.deceduto;
	}



	public void setDeceduto(boolean deceduto) {
		this.deceduto = deceduto;
	}
}
