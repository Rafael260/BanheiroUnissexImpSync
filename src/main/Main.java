package main;

import java.util.Random;


import banheiro.Banheiro;
import banheiro.Pessoa;

public class Main {

	public static void main(String[] args) {
		Banheiro banheiro = new Banheiro(4,3);
//		Semaphore semaforo = new Semaphore(banheiro.getNumeroVagas());
		int numeroPessoasParaTeste = 14;
		Random random = new Random();
		boolean homem;
		for(int i = 0; i < numeroPessoasParaTeste; i++){
			homem = random.nextInt(2) == 0;
			new Pessoa("Pessoa"+i,homem, banheiro).start();
		}
	}

}
