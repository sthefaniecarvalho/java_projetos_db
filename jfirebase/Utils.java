
package jfirebase;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Connection;
import java.util.Scanner;

import org.json.JSONObject;


public class Utils {

	static Scanner sc = new Scanner(System.in);

	public static HttpClient conectar() {
		HttpClient conn = HttpClient.newBuilder().build();
		
		return conn;
	}

	public static void desconectar(Connection conn){
		System.out.println("Desconectando");
	}
	
	public static boolean idExiste(String id) {
		HttpClient conn = conectar();
		
		String link = "https://jfire-15-default-rtdb.firebaseio.com/produtos.json";
		
		HttpRequest requisicao = HttpRequest.newBuilder(URI.create(link)).build();
		
		boolean res = false;
		try {
			
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			if(resposta.body().equals("null")) {
				System.out.println("N„o h· produtos cadastrados");
			}else {
				String obj = new JSONObject(resposta.body()).names().toString();
				
				if(obj.contains(id)) {
					 res = true;
				}
			}
			
		}catch(IOException e) {
			System.out.println("Houve um erro de conex„o.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro de conex„o.");
			e.printStackTrace();
		}
		
		return res;
		
	}

	public static void listar() {
		HttpClient conn = conectar();
		
		String link = "https://jfire-15-default-rtdb.firebaseio.com/produtos.json";
		
		HttpRequest requisicao = HttpRequest.newBuilder(URI.create(link)).build();
		
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			if(resposta.body().equals("null")) {
				System.out.println("N„o h· produtos cadastrados");
			}else {
				JSONObject obj = new JSONObject(resposta.body());
				
				System.out.println("Listando produtos...");
				System.out.println("--------------------");
				for(int i = 0; i < obj.length(); i++) {
					JSONObject prod = (JSONObject)obj.get(obj.names().getString(i));
					
					System.out.println("ID: " + obj.names().getString(i));
					System.out.println("Produto: " + prod.get("nome"));
					System.out.println("PreÁo: " + prod.get("preco"));
					System.out.println("Estoque: " + prod.get("estoque"));
					System.out.println("---------------------");			
				}
			}
		
		}catch(IOException e) {
			System.out.println("Houve um erro de conex„o.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro de conex„o.");
			e.printStackTrace();
		}
	}
	
	public static void inserir() {
		HttpClient conn = conectar();
		
		String link = "https://jfire-15-default-rtdb.firebaseio.com/produtos.json";
		
		System.out.println("Informe o nome do produto: ");
		String nome = sc.next();
		
		System.out.println("Informe o preÁo do produto: ");
		float preco = sc.nextFloat();
		
		System.out.println("Informe o estoque do produto: ");
		int estoque = sc.nextInt();
		
		JSONObject nproduto = new JSONObject();
		nproduto.put("nome", nome);
		nproduto.put("preco", preco);
		nproduto.put("estoque", estoque);
		
		HttpRequest requisicao = HttpRequest.newBuilder()
				.uri(URI.create(link))
				.POST(BodyPublishers.ofString(nproduto.toString()))
				.header("Content-Type", "application/json")
				.build();
		
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			JSONObject obj = new JSONObject(resposta.body());
			
			if (resposta.statusCode() == 200) {
				System.out.println("O produto foi inserido com sucesso.");
			}else {
				System.out.println(obj);
				System.out.println("Status: " + resposta.statusCode());
			}
			
		}catch(IOException e) {
			System.out.println("Houve um erro de conex„o.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro de conex„o.");
			e.printStackTrace();	
		}
	}

	public static void atualizar() {
		HttpClient conn = conectar();
		
		System.out.println("Informe o id do produto: ");
		String id = sc.next();
		
		if(idExiste(id)) {
			System.out.println("Informe o nome do produto: ");
			String nome = sc.next();
			
			System.out.println("Informe o preÁo do produto: ");
			float preco = sc.nextFloat();
			
			System.out.println("Informe o estoque do produto: ");
			int estoque = sc.nextInt();
			
			String link = "https://jfire-15-default-rtdb.firebaseio.com/produtos/" + id + ".json";
			
			JSONObject nproduto = new JSONObject();
			nproduto.put("nome", nome);
			nproduto.put("preco", preco);
			nproduto.put("estoque", estoque);
			
			HttpRequest requisicao = HttpRequest.newBuilder()
					.uri(URI.create(link))
					.PUT(BodyPublishers.ofString(nproduto.toString()))
					.header("Content-Type", "application/json")
					.build();
			
			try {
				HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
				
				JSONObject obj = new JSONObject(resposta.body());
				
				
				if (resposta.statusCode() == 200) {
					System.out.println("O produto foi atualizado com sucesso.");
					System.out.println(resposta.body());
				}else {
					System.out.println(obj);
					System.out.println("Status: " + resposta.statusCode());
				}
				
			}catch(IOException e) {
				System.out.println("Houve um erro de conex„o.");
				e.printStackTrace();
			}catch(InterruptedException e) {
				System.out.println("Houve um erro de conex„o.");
				e.printStackTrace();	
			}
		}else {
			System.out.println("ID n„o encontrado.");
		}
		}
		

	public static void deletar() {
		HttpClient conn = conectar();
		
		System.out.println("Informe o id do produto: ");
		String id = sc.nextLine();
		
		String link = "https://jfire-15-default-rtdb.firebaseio.com/produtos/" + id + ".json";
		
		HttpRequest requisicao = HttpRequest.newBuilder()
				.uri(URI.create(link))
				.DELETE()
				.header("Content-Type", "application/json")
				.build();
		
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			
			if (resposta.statusCode() == 200 && !resposta.body().equals("null")) {
				System.out.println("O produto foi deletado com sucesso.");
				System.out.println(resposta.body());
			}else {
				System.out.println("N„o existe produto com o ID informado");
			}
			
		}catch(IOException e) {
			System.out.println("Houve um erro de conex„o.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro de conex„o.");
			e.printStackTrace();	
		}		
	}

	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma op√ß√£o: ");
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
			System.out.println("Op√ß√£o inv√°lida.");
		}
	}
}