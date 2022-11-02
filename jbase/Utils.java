
package jbase;

import java.sql.Connection;
import java.util.Scanner;

public class Utils {

	static Scanner sc = new Scanner(System.in);

	public static void conectar() {
		System.out.println("Conectando");
	}

	public static void desconectar(Connection conn){
		System.out.println("Desconectando");
	}

	public static void listar() {
		System.out.println("Listando..");
	
	}
	
	public static void inserir() {
		System.out.println("Inserindo...");
	}

	public static void atualizar() {
		System.out.println("Atualizando..");
	}

	public static void deletar() {
		System.out.println("Deletando");
		
	}

	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		
		int opcao = Integer.parseInt(sc.nextLine());
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		}else {
			System.out.println("Opção inválida.");
		}
	}
}