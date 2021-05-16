package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Agenda {
	
	
	
		private int id;
		private String nome;
		private String data_inicio;
		private String data_conclusao;
		
		
	
	

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getData_inicio() {
			return data_inicio;
		}

		public void setData_inicio(String data_inicio) {
			this.data_inicio = data_inicio;
		}

		public String getData_conclusao() {
			return data_conclusao;
		}

		public void setData_conclusao(String data_conclusao) {
			this.data_conclusao = data_conclusao;
		}

		public Agenda() {
			id = -1;
			nome = nome;
			data_inicio = data_inicio;
			data_conclusao = data_conclusao;
			
		}

		public Agenda(int id, String nome,String data_inicio, String data_conclusao) {
			setId(id);
			setNome(nome);
			setData_conclusao(data_conclusao);
			setData_inicio(data_inicio);
			
		}	
		
		

		@Override
		public String toString() {
			return "NomeQuiz: " + nome + "DataInicio" + data_inicio + "DataConclusao" + data_conclusao;
	    }    
		
		
		@Override
		public boolean equals(Object obj) {
			return (this.getId() == ((Agenda) obj).getId());
		}	
	

}
