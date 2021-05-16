package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Quiz {
	
	
	
		private int id;
		private String nomeQuiz;
		private Usuario usuario;
		
		
	
		
		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNomeQuiz() {
			return nomeQuiz;
		}

		public void setNomeQuiz(String nomeQuiz) {
			this.nomeQuiz = nomeQuiz;
		}

		public Quiz() {
			id = -1;
			nomeQuiz = nomeQuiz;
			
		}

		public Quiz(int id, String nomeQuiz) {
			setId(id);
			setNomeQuiz(nomeQuiz);
			
		}	
		
		

		@Override
		public String toString() {
			return "NomeQuiz: " + nomeQuiz;
	    }    
		
		
		@Override
		public boolean equals(Object obj) {
			return (this.getId() == ((Usuario) obj).getId());
		}	
	

}
