
package jcouch;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Connection;
import java.util.Scanner;


import org.json.JSONArray;
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

	public static void listar() {
		HttpClient conn = conectar();
		
		String link = "http://localhost:5984/jcouch/_all_docs?include_docs=true";
		
		HttpRequest requisicao = HttpRequest.newBuilder().uri(URI.create(link)).build();
		
		try {
			HttpResponse<String> resposta = conn.send(requisicao,BodyHandlers.ofString());
			
			JSONObject obj = new JSONObject(resposta.body());
			
			if ((int)obj.get("total_rows") > 0) {
				JSONArray produtos = (JSONArray)obj.get("rows");
				
				System.out.println("Listando produtos...");
				System.out.println("--------------------");
				for(Object produto : produtos) {
					
					JSONObject doc = (JSONObject) produto;
					JSONObject prod = (JSONObject)doc.get("doc");
					
					System.out.println("ID: " + prod.get("_id"));
					System.out.println("Rev: " + prod.get("_rev"));
					System.out.println("Produto: " + prod.get("nome"));
					System.out.println("Pre�o: " + prod.get("preco"));
					System.out.println("Estoque: " + prod.get("estoque"));
					System.out.println("-----------------------");	
				}	
			}else {
				System.out.println("N�o existem produtos cadastrados.");
			}	
		}catch(IOException e) {
			System.out.println("Houve um erro durante a conex�o.");
			e.printStackTrace();
		}catch (InterruptedException e) {
			System.out.println("Houve um erro durante a conex�o.");
			e.printStackTrace();
		}
	
	}
	
	public static void inserir() {
		HttpClient conn = conectar();
		
		String link = "http://localhost:5984/jcouch";
		
		System.out.println("Informe o nome do produto: ");
		String nome = sc.next();
		
		System.out.println("Informe o pre�o do produto: ");
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
				.header("Content-type", "application/json")
				.build();
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			JSONObject obj = new JSONObject(resposta.body());
			
			if(resposta.statusCode() == 201) {
				System.out.println("O produto " + nome + " foi cadastrado com sucesso.");
			}else {
				System.out.println(obj);
				System.out.println("Status: " + resposta.statusCode());
			}
		}catch(IOException e) {
			System.out.println("Houve um erro durante a conex�o.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro durante a conex�o.");
			e.printStackTrace();
		}
	}

	public static void atualizar() {
		HttpClient conn = conectar();
		
		System.out.println("Informe o ID do produto: ");
		String id = sc.next();
		
		System.out.println("Informe a Rev do produto: ");
		String rev = sc.next();
		
		System.out.println("Informe o nome do produto: ");
		String nome = sc.next();
		
		System.out.println("Informe o pre�o do produto: ");
		float preco = sc.nextFloat();
		
		System.out.println("Informe o estoque do produto: ");
		int estoque = sc.nextInt();
		
		String link = "http://localhost:5984/jcouch/" + id + "/" + "?rev=" + rev;
		
		JSONObject nproduto = new JSONObject();
		nproduto.put("nome", nome);
		nproduto.put("preco", preco);
		nproduto.put("estoque", estoque);
		
		HttpRequest requisicao = HttpRequest.newBuilder()
				.uri(URI.create(link))
				.PUT(BodyPublishers.ofString(nproduto.toString()))
				.header("Content-type", "application/json")
				.build();
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			JSONObject obj = new JSONObject(resposta.body());
			
			if(resposta.statusCode() == 201) {
				System.out.println("O produto " + nome + " foi atualizado com sucesso.");
			}else {
				System.out.println(obj);
				System.out.println("Status: " + resposta.statusCode());
			}
		}catch(IOException e) {
			System.out.println("Houve um erro durante a conex�o.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro durante a conex�o.");
			e.printStackTrace();
		}
	}

	public static void deletar() {
		HttpClient conn = conectar();
		
		System.out.println("Informe o ID do produto: ");
		String id = sc.next();
		
		System.out.println("Informe a Rev do produto: ");
		String rev = sc.next();
		
		String link = "http://localhost:5984/jcouch/" + id + "/" + "?rev=" + rev;
		
		HttpRequest requisicao = HttpRequest.newBuilder()
				.uri(URI.create(link))
				.DELETE()
				.build();
		try {
			HttpResponse<String> resposta = conn.send(requisicao, BodyHandlers.ofString());
			
			
			if(resposta.statusCode() == 200) {
				System.out.println("O produto foi deletado com sucesso.");
			}else {
				System.out.println(resposta.body());
				System.out.println("Status: " + resposta.statusCode());
			}
		}catch(IOException e) {
			System.out.println("Houve um erro durante a conex�o.");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.out.println("Houve um erro durante a conex�o.");
			e.printStackTrace();
		}	
	}

	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma op��o: ");
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
			System.out.println("Op��o inv�lida.");
		}
	}
}