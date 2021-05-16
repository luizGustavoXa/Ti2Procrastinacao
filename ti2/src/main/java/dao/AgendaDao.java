package dao;

import model.Agenda;
import model.Quiz;
import model.Usuario;

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

public class AgendaDao {
	private List<Agenda> agenda;
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

	public AgendaDao() throws IOException {
		conexao = null;
		maxId = 0;

	}
	

	public void add(Agenda agenda) {
        try {
            this.maxId = (agenda.getId() > this.maxId) ? agenda.getId() : this.maxId;
            Statement st = conexao.createStatement();
            st.executeUpdate("INSERT INTO agenda (id, nome, data_inicio, data_conclusao) "
                           + "VALUES ("+agenda.getId()+ ", '" 
                           + agenda.getNome() + "', '"
                           + agenda.getData_inicio() + "', '"
                           + agenda.getData_conclusao() + "');");
            st.close();
        } catch (SQLException u) {
            System.out.println("ERRO ao inserir quiz na base de dados!");
            throw new RuntimeException(u);
        }
    }

	public Agenda get(int id) {
		Agenda agenda = new Agenda();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM agenda WHERE id = " + id);		
			agenda.setId(rs.getInt("id"));
			agenda.setNome(rs.getString("nome"));
			agenda.setData_inicio(rs.getString("data_inicio"));
			agenda.setData_conclusao(rs.getString("data_conclusao"));
			
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return agenda;
	}

	public void update(Agenda agenda) {
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE usuario SET nome_usuario = '"
						 + agenda.getNome() + "', email = '"  
						 + " WHERE id = " + agenda.getId();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException u) {
			System.out.println("ERRO ao atualizar usuario na base de dados!");
			throw new RuntimeException(u);
		}
	}

	public void remove(Agenda agenda) {
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM usuario WHERE id_usuario = " + agenda.getId());
			st.close();
		} catch (SQLException u) {
			System.out.println("ERRO ao excluir usuario da base de dados!");
			throw new RuntimeException(u);
		}
	}

	public Agenda[] getAll() {
		Agenda[] agenda = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM agenda");		
	         if(rs.next()){
	             rs.last();
	             agenda = new Agenda[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                agenda[i] = new Agenda(rs.getInt("id"), 
	                								  rs.getString("nome"), 
	                								  rs.getString("data_inicio"), 
	                								  rs.getString("data_conclusao"));
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return agenda;
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