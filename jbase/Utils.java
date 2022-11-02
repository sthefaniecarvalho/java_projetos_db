
package jbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Utils {

	static Scanner sc = new Scanner(System.in);

	public static Connection conectar() {
		String CLASSE_DRIVER = "com.mysql.cj.jdbc.Driver";
		String USUARIO = "geek";
		String SENHA = "university";
		String URL_SERVIDOR = "jdbc:mysql://localhost:3306/jmysql?useSSL=false";
		
		try {
			Class.forName(CLASSE_DRIVER);
			return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);
		} catch(Exception e) {
			if (e instanceof ClassNotFoundException) {
				System.out.println("Verifique o driver de conex„o");
			}else {
				System.out.println("Verifique se o servidor est· ativo");
				e.printStackTrace();
			}
			System.exit(-42);
			return null;
		}
		
	}

	public static void desconectar(Connection conn){
		if (conn != null) {
			try {
				conn.close();
			}catch (SQLException e) {
				System.out.println("N„o foi possivel fechar a conex„o");
				e.printStackTrace();
			}
			
		}
	}

	public static void listar() {
		String BUSCAR_TODOS = "SELECT * FROM produtos";
		try {
			Connection conn = conectar();
			PreparedStatement produtos = conn.prepareStatement(BUSCAR_TODOS, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);  // PERMITE QUE O CURSOR VAI APARA FRENTE E PRA TRAS
												  // ASSIM SENDO ATUALIZADO E ROLAVEL
			ResultSet res = produtos.executeQuery();
			
			res.last();
			int quant = res.getRow();  //CONTAGEM DE LINHAS
			res.beforeFirst();
			
			if (quant > 0) {
				System.out.println("Listando produtos...");
				System.out.println("--------------------");
				
				while (res.next()) {
					System.out.println("ID: " + res.getInt(1));
					System.out.println("Produto: " + res.getString(2));
					System.out.println("PreÁo: " + res.getFloat(3));
					System.out.println("Estoque: " + res.getInt(4));
					System.out.println("--------------------");
				}
			}else {
				System.out.println("N„o h· produtos cadastrados");
			}
			produtos.close();
			desconectar(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro listando produtos");
			System.err.println(-42);
		}
		
	}

	public static void inserir() {
		try {
			System.out.println("Informe o nome do produto: ");
			String nome = sc.nextLine();
			
			System.out.println("Informe o preco do produto: ");
			float preco = sc.nextFloat();
			
			System.out.println("Informe o estoque do produto: ");
			int estoque = sc.nextInt();
		
			String INSERIR = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
			//SQL INSJETION
			
			try {
				Connection conn = conectar();
				PreparedStatement salvar = conn.prepareStatement(INSERIR);
				
				salvar.setString(1, nome);
				salvar.setFloat(2, preco);
				salvar.setInt(3, estoque);
				
				salvar.executeUpdate();
				salvar.close();
				
				desconectar(conn);
				System.out.println("Produto salvo com sucesso.");
				
			}catch (Exception e) {
				e.printStackTrace();
				System.err.println("Erro salvando produtos");
				System.err.println(-42);
			}
		}catch (InputMismatchException e) {
			System.out.println("Valor inv·lido: " + e);
		}
		
		
	}

	public static void atualizar() {
		System.out.println("Informe o codigo do produto: ");
		int id = sc.nextInt();
		
		String BUSCAR_ID = "SELECT * FROM produtos WHERE id=?";
		
		try {
			Connection conn = conectar();
			PreparedStatement produto = conn.prepareStatement(BUSCAR_ID, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			produto.setInt(1, id);
			ResultSet res = produto.executeQuery();
			
			res.last();
			int quant = res.getRow();
			res.beforeFirst();
			
			if (quant > 0) {
	
				System.out.println("Informe o novo nome do produto: ");
				String nome = sc.next();
				System.out.println("Informe o novo preco do produto: ");
				float preco = sc.nextFloat();
				System.out.println("Informe o estoque do produto: ");
				int estoque = sc.nextInt();
				
				String ATUALIZAR = "UPDATE produtos SET nome=?, preco=?, estoque=? WHERE id = ?";
				PreparedStatement upd = conn.prepareStatement(ATUALIZAR);
				
				upd.setString(1, nome);
				upd.setFloat(2, preco);
				upd.setInt(3, estoque);
				upd.setInt(4,  id);
				
				upd.executeUpdate();
				upd.close();
				
				desconectar(conn);
				System.out.println("Produto atualizado com sucesso!");
		  }else {
				System.out.println("ID n„o encontrado.");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro atualizar o produto");
			System.err.println(-42);
		}
	}

	public static void deletar() {
		String BUSCAR_ID = "SELECT * FROM produtos WHERE id=?";
		String DELETAR = "DELETE FROM produtos WHERE id=?";
		
		try {
			System.out.println("Informe o codigo do produto: ");
			int id = sc.nextInt();
			
			try {
				Connection conn = conectar();
				
				PreparedStatement produto = conn.prepareStatement(BUSCAR_ID, 
						ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                    ResultSet.CONCUR_UPDATABLE);
				produto.setInt(1, id);
				ResultSet res = produto.executeQuery();
				
				res.last();
				int quant = res.getRow();
				res.beforeFirst();
				
				if (quant > 0) {
					PreparedStatement deletar = conn.prepareStatement(DELETAR);
					
					deletar.setInt(1, id);
					deletar.executeUpdate();
					deletar.close();
					desconectar(conn);
					
					System.out.println("Produto deletado com sucesso!");
					
				} else {
					System.out.println("ID n„o encontrado.");
				}
			}catch (Exception e) {
				e.printStackTrace();
				System.err.println("Erro ao deletar o produto");
				System.err.println(-42);
			}
		}catch (InputMismatchException e) {
			System.out.println("Valor inv·lido: " + e);
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