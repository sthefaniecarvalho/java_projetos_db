
package jsqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Utils {

	static Scanner sc = new Scanner(System.in);

	public static Connection conectar() {
		String URL_SERVIDOR = "jdbc:sqlite:C:/Users/Usu·rio/Documents/Banco de Dados/SQLite/jsqlite.db";
		
		try {
			Connection conn = DriverManager.getConnection(URL_SERVIDOR);
			
			String TABLE = "CREATE TABLE IF NOT EXISTS produtos("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "nome TEXT NOT NULL,"
					+ "preco REAL NOT NULL,"
					+ "estoque INTEGER NOT NULL);";
			
			Statement stmt = conn.createStatement();
			stmt.execute(TABLE);
			
			return conn;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("N„o foi possÌvel conectar ao SQLite: " + e);
			return null;
		}
	}

	public static void desconectar(Connection conn){
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void listar() {
		String BUSCAR_TODOS = "SELECT * FROM produtos";
		
		try {
			Connection conn = conectar();
			PreparedStatement produtos = conn.prepareStatement(BUSCAR_TODOS);
			ResultSet res = produtos.executeQuery();
			
			while (res.next()) {
				System.out.println("Listando produtos...");
				System.out.println("--------------------");
				System.out.println("ID: " + res.getInt(1));
				System.out.println("Nome: " + res.getString(2));
				System.out.println("PreÁo: " + res.getFloat(3));
				System.out.println("Estoque: " + res.getInt(4));
				System.out.println("--------------------");
			}
			produtos.close();
			desconectar(conn);	
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao listar produtos" + e);
			System.exit(-42);
		}
	}
	
	public static void inserir() {
		System.out.print("Informe o nome do produto: ");
		String nome = sc.next();
		System.out.print("Informe o preÁo do produto: ");
		float preco = sc.nextFloat();
		System.out.print("Informe o estoque do produto: ");
		int estoque = sc.nextInt();
		
		String INSERIR = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
		
		try {
			Connection conn = conectar();
			PreparedStatement salvar = conn.prepareStatement(INSERIR);
			
			salvar.setString(1, nome);
			salvar.setFloat(2, preco);
			salvar.setInt(3, estoque);
			
			int res = salvar.executeUpdate(); // devolve a quantidade de linhas
			
			if (res > 0) {
				System.out.println("O produto " + nome + " foi inserido com sucesso.");
			}else {
				System.out.println("N„o foi possivel inserir o produto.");
			}
			salvar.close();
			desconectar(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao salvar produto" + e);
		}
	}

	public static void atualizar() {
		System.out.println("Informe o id do produto: ");
		int id = sc.nextInt();
		
		try {
			Connection conn = conectar();
			System.out.println("Informe o novo nome do produto: ");
			String nome = sc.next();
			System.out.println("Informe o novo preÁo do produto: ");
			float preco = sc.nextFloat();
			System.out.println("Informe o novo estoque do produto: ");
			int estoque = sc.nextInt();
			
			String ATUALIZAR = "UPDATE produtos SET nome=?, preco=?, estoque=? WHERE id=?";
			PreparedStatement upd = conn.prepareStatement(ATUALIZAR);
			
			upd.setString(1, nome);
			upd.setFloat(2, preco);
			upd.setInt(3, estoque);
			upd.setInt(4, id);
			
			int res = upd.executeUpdate();
			
			if (res > 0) {
				System.out.println("O produto foi atualizado com sucesso.");
			} else {
				System.out.println("N„o foi possivel atualizar o produto com id = " + id);
			}
			
			upd.close();
			desconectar(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao atualizar o produto" + e);
		}
	}

	public static void deletar() {
		System.out.println("Informe o id do produto: ");
		int id = sc.nextInt();
		
		try {
			Connection conn = conectar();
			
			String DELETAR= "DELETE FROM produtos WHERE id=?";
			PreparedStatement del = conn.prepareStatement(DELETAR);
			
			del.setInt(1, id);
			
			int res = del.executeUpdate();
			
			if (res > 0) {
				System.out.println("O produto foi deletado com sucesso.");
			} else {
				System.out.println("N„o foi possivel deletar o produto com id = " + id);
			}
			
			del.close();
			desconectar(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao deletar o produto" + e);
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