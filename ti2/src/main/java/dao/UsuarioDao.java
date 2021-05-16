package dao;

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

public class UsuarioDao {
	private List<Usuario> usuarios;
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

	public UsuarioDao() throws IOException {
		conexao = null;
		maxId = 0;

	}
	

	public void add(Usuario usuario) {
        try {
            this.maxId = (usuario.getId() > this.maxId) ? usuario.getId() : this.maxId;
            Statement st = conexao.createStatement();
            st.executeUpdate("INSERT INTO usuario (id, nome, email, senha) "
                           + "VALUES ("+usuario.getId()+ ", '" 
                           + usuario.getNome() + "', '"
                           + usuario.getEmail() + "', '"
                           + usuario.getSenha() + "');");
            st.close();
        } catch (SQLException u) {
            System.out.println("ERRO ao inserir funcionario na base de dados!");
            throw new RuntimeException(u);
        }
    }
	
	public Usuario get(String email) {
        Usuario user = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = (
                            "SELECT * "
                            + "FROM usuario "
                            + "WHERE email = '" + email + "';"
                         );
            ResultSet rs = st.executeQuery(sql);

            if(rs.next()) {
                rs.beforeFirst();
                rs.next();
                user = new Usuario(
                                    rs.getInt("id"),
                                    rs.getString("nome"), 
                                   rs.getString("email"),
                                   rs.getString("senha"));

                System.out.println("Sucess! --- " + user.toString());
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return user;
    }


	public Usuario get(int id) {
		Usuario usuario = new Usuario();
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE id = " + id);		
			usuario.setId(rs.getInt("id_usuario"));
			usuario.setNome(rs.getString("nome_usuario"));
			usuario.setEmail(rs.getString("email"));
			usuario.setSenha(rs.getString("senha"));
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return usuario;
	}

	public void update(Usuario usuario) {
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE usuario SET nome_usuario = '"
						 + usuario.getNome() + "', email = '"  
						 + usuario.getEmail() + "', senha = '" 
						 + usuario.getSenha()
						 + " WHERE id_usuario = " + usuario.getId();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException u) {
			System.out.println("ERRO ao atualizar usuario na base de dados!");
			throw new RuntimeException(u);
		}
	}

	public void remove(Usuario usuario) {
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM usuario WHERE id_usuario = " + usuario.getId());
			st.close();
		} catch (SQLException u) {
			System.out.println("ERRO ao excluir usuario da base de dados!");
			throw new RuntimeException(u);
		}
	}

	public Usuario[] getAll() {
		Usuario[] usuario = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM usuario");		
	         if(rs.next()){
	             rs.last();
	             usuario = new Usuario[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                usuario[i] = new Usuario(rs.getInt("id"), 
	                								  rs.getString("nome_usuario"), 
	                								  rs.getString("email"), 
	                								  rs.getString("senha"));
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return usuario;
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