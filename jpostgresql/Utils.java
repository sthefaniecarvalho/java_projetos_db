
package jpostgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Utils {

	static Scanner sc = new Scanner(System.in);

	public static Connection conectar() {
		Properties props = new Properties();
		props.setProperty("user", "geek");
		props.setProperty("password", "university");
		props.setProperty("ssl", "false");
		String URL_SERVIDOR = "jdbc:postgresql://localhost:5432/jpostgresql";
		
		try {
			return DriverManager.getConnection(URL_SERVIDOR, props);
			
		}catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ClassNotFoundException) {
				System.err.println("Verifique a conex„o.");
			}else {
				System.err.println("Verifique se o conector est· ativo.");
			}
			System.out.println(-42);
			return null;	
		}
	}
	

	public static void desconectar(Connection conn){
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
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
                    ResultSet.CONCUR_UPDATABLE);
			
			ResultSet res = produtos.executeQuery();
			
			res.last();
			int quant = res.getRow();
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
				System.out.println("N„o h· produtos cadastrados.");
			}
                 
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro listando produtos");
			System.err.println(-42);
		}
	
	}
	
	public static void inserir() {
		System.out.print("Informe o nome do produto: ");
		String nome = sc.next();
		System.out.print("Informe o preÁo do produto: ");
		float preco = sc.nextFloat();
		System.out.print("Informe o estoque do produto: ");
		int estoque = sc.nextInt();
		
		String INSERIR = "INSERT INTO produtos (nome, preco, estoque) "
				+ "VALUES (?, ?, ?)";
		
		try {
			Connection conn = conectar();
			PreparedStatement salvar = conn.prepareStatement(INSERIR);
			
			salvar.setString(1, nome);
			salvar.setFloat(2, preco);
			salvar.setInt(3, estoque);
			
			salvar.executeUpdate();
			salvar.close();
			
			desconectar(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro inserindo produto");
			System.err.println(-42);
		}
	}

	public static void atualizar() {
		String BUSCAR_ID = "SELECT * FROM produtos WHERE id=?";
		
		System.out.println("Informe o id: ");
		int id = sc.nextInt();
		
		try {
			Connection conn = conectar();
			PreparedStatement buscar = conn.prepareStatement(BUSCAR_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			buscar.setInt(1, id);
			ResultSet res = buscar.executeQuery();
			
			res.last();
			int quant = res.getRow();
			res.beforeFirst();
			
			if (quant > 0) {
				
				System.out.print("Informe o novo nome do produto: ");
				String nome = sc.next();
				System.out.print("Informe o novo preÁo do produto: ");
				float preco = sc.nextFloat();
				System.out.print("Informe o novo estoque do produto: ");
				int estoque = sc.nextInt();
				 
				String ATUALIZAR = "UPDATE produtos SET nome=?, preco=?, estoque=? WHERE id=?";
				PreparedStatement atualizar = conn.prepareStatement(ATUALIZAR);
				
				atualizar.setString(1, nome);
				atualizar.setFloat(2, preco);
				atualizar.setInt(3, estoque);
				atualizar.setInt(4, id);
				
				atualizar.executeUpdate();
				atualizar.close();
				
				System.out.println("Produto atualido com sucesso.");
		
			}else {
				System.out.println("ID "+ id + " n„o encontrado.");
			}
			desconectar(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao atualiar o produto");
			System.err.println(-42);
			
		}
	}

	public static void deletar() {
		System.out.println("Informe o id do produto: ");
		int id = sc.nextInt();
		
		String BUSCAR_ID = "SELECT * FROM produtos WHERE id=?";
		
		try {
			Connection conn = conectar();
			PreparedStatement buscar = conn.prepareStatement(BUSCAR_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			buscar.setInt(1, id);;
			ResultSet res = buscar.executeQuery();
			
			res.last();
			int quant = res.getRow();
			res.beforeFirst();
			
			if (quant > 0) {
				String DELETAR = "DELETE FROM produtos WHERE id=?";
				PreparedStatement del = conn.prepareStatement(DELETAR);
				
				del.setInt(1, id);
				del.executeUpdate();
				del.close();
				
				System.out.println("Produto deletado com sucesso.");
				
			}else {
				System.out.println("Id " + id + " n„o encontrado.");
			}
			desconectar(conn);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao deletar o produto");
			System.err.println(-42);
		}
		
		
	}

	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma opÁ„o: ");
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