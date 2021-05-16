package dao;

import model.Quiz;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class QuizDao {
	private List<Quiz> quiz;
	private int maxId = 0;
	private Connection conexao;
	private File file;
	private FileOutputStream fos;
	private ObjectOutputStream outputFile;
	
	
	public boolean conectar() {
		String driverName = "org.postgresql.Driver";                    
		String serverName = "ti2procrastinacao.postgres.database.azure.com";
		String mydatabase = "procrastinacao";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
		String username = "adm@ti2procrastinacao";
		String password = "ti2@procrastinacao";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("ConexÃ£o efetuada com o postgres!");
		} catch (ClassNotFoundException e) { 
			System.err.println("ConexÃ£o NÃƒO efetuada com o postgres -- Driver nÃ£o encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("ConexÃ£o NÃƒO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}
	
	public int getMaxId() {
		return maxId;
	}

	public QuizDao() throws IOException {
		conexao = null;
		maxId = 0;

	}
	

	public void add(Quiz quiz) {
        try {
            this.maxId = (quiz.getId() > this.maxId) ? quiz.getId() : this.maxId;
            Statement st = conexao.createStatement();
            st.executeUpdate("INSERT INTO quiz (id, nome, usuario) "
                           + "VALUES ("+quiz.getId()+ ", '" 
                           + quiz.getNomeQuiz() + "', '"
                           + quiz.getUsuario() + "');");
            st.close();
        } catch (SQLException u) {
            System.out.println("ERRO ao inserir quiz na base de dados!");
            throw new RuntimeException(u);
        }
    }

	public Quiz get(int id) {
		Quiz quiz = new Quiz();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM quiz WHERE usuario = " + id);		
			quiz.setId(rs.getInt("id"));
			quiz.setNomeQuiz(rs.getString("nome_quiz"));
			//quiz.setUsuario(user.getz(rs.getInt("id")));
			
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return quiz;
	}

	public void update(Quiz quiz) {
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE usuario SET nome_usuario = '"
						 + quiz.getNomeQuiz() + "', email = '"  
						 + " WHERE id = " + quiz.getId();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException u) {
			System.out.println("ERRO ao atualizar usuario na base de dados!");
			throw new RuntimeException(u);
		}
	}

	public void remove(Quiz quiz) {
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM usuario WHERE id_usuario = " + quiz.getId());
			st.close();
		} catch (SQLException u) {
			System.out.println("ERRO ao excluir usuario da base de dados!");
			throw new RuntimeException(u);
		}
	}

	public Quiz[] getAll() {
		Quiz[] quiz = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM quiz");		
	         if(rs.next()){
	             rs.last();
	             quiz = new Quiz[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                quiz[i] = new Quiz(rs.getInt("id"), 
	                								  rs.getString("nome"));
	                								 
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return quiz;
	}



	public boolean close() {
		boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
}