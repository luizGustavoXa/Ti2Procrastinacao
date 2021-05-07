package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Usuario implements Serializable {
	private int id;
	private String email;
	private String nome;
	private String senha;
	
	public Usuario() {
		id = -1;
		email = email;
		nome = nome; 
		senha = senha;
	}

	public Usuario(int id, String email, String nome, String senha) {
		setId(id);
		setEmail(email);
		setNome(nome);
		setSenha(senha);
	}		
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Email: " + email + "   Nome: " + nome + "   Senha: " + senha;
    }    
	
	@Override
	public boolean equals(Object obj) {
		return (this.getId() == ((Usuario) obj).getId());
	}	
}