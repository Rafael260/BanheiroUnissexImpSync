package banheiro;

import java.util.ArrayList;
import java.util.List;

public class Banheiro {
	private int numeroVagas;
	// Controle de quais pessoas estao entrando
	private String sexo;
	// Tempo maximo que uma pessoa passa no banheiro
	Object listaLock;
	private int tempoMaximo;
	private List<Pessoa> pessoasUsando;

	// Variaveis para sincronizacao
	Object sexoAtual;
	Object banheiroLotado;

	public Banheiro(int vagas, int tempoMaximo) {
		this.numeroVagas = vagas;
		this.sexo = "vazio";
		this.tempoMaximo = tempoMaximo;
		this.pessoasUsando = new ArrayList<>();
		this.listaLock = new Object();
		this.sexoAtual = new Object();
		this.banheiroLotado = new Object();
	}

	// <getters and setters>

	public int getNumeroVagas() {
		return numeroVagas;
	}

	public void setNumeroVagas(int vagas) {
		this.numeroVagas = vagas;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public int getTempoMaximo() {
		return tempoMaximo;
	}

	public void setTempoMaximo(int tempoMaximo) {
		this.tempoMaximo = tempoMaximo;
	}

	// </getters and setters>

	public int getNumeroPessoas() {
		return this.pessoasUsando.size();
	}

	public boolean banheiroVazio() {
		return this.pessoasUsando.isEmpty();
	}

	public boolean banheiroCheio() {
		return this.pessoasUsando.size() == numeroVagas;
	}

	public boolean possuiVagas() {
		return this.pessoasUsando.size() < this.numeroVagas;
	}

	public void adicionarPessoa(Pessoa pessoa) {
		this.pessoasUsando.add(pessoa);
	}

	public void removerPessoa(Pessoa pessoa) {
		this.pessoasUsando.remove(pessoa);
	}

	public synchronized void entrar(Pessoa pessoa) throws InterruptedException {
		// Enquanto tiverem pessoas do outro sexo, eh preciso aguardar
		while (!sexo.equals("vazio") && !sexo.equals(pessoa.getSexo())) {
			// Marco essa opção para que outras pessoas do mesmo sexo das que
			// estao no banheiro nao passem na frente, garantindo justica
			this.setSexo("barrado");
			wait();
		}

		// Enquanto o banheiro tiver lotado, espera tambem (nessa situacao, ja
		// estará esperando por pessoas do mesmo sexo sairem)
		while (!possuiVagas()) {
			wait();
		}

		// adiciono a pessoa na lista de pessoas que estao usando o banheiro
		this.adicionarPessoa(pessoa);
		// Se for a primeira pessoa, deve indicar qual sexo dela para que as
		// outras pessoas saibam
		if (this.pessoasUsando.size() == 1) {
			this.setSexo(pessoa.getSexo());
		}
		
		 System.out.println("Numero de pessoas no banheiro:" + getNumeroPessoas());
	}

	public synchronized void sair(Pessoa pessoa) {
		// Remove a pessoa da lista das pessoas que estao usando
		removerPessoa(pessoa);
		// Se o banheiro ta vazio, esta livre para que qualquer pessoa (homem ou
		// mulher) possa entrar
		if (this.banheiroVazio()) {
			this.setSexo("vazio");
			notifyAll();
		}
		else{
			notify();
		}

	}
}
