package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dao.AgendaDao;
import dao.QuizDao;
import dao.UsuarioDao;
import model.Agenda;
import model.Quiz;
import model.Usuario;
import spark.Request;
import spark.Response;


public class AgendaService {

	private AgendaDao agendadao;

	public AgendaService(){
		try {
			agendadao = new AgendaDao();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		agendadao.conectar();
	}

	public Object add(Request request, Response response) {
		String nome = request.queryParams("nome");
		String data_inicio = request.queryParams("data_inicio");
		String data_conclusao = request.queryParams("data_conclusao");
		

		int id = agendadao.getMaxId() + 1;

		Agenda agenda = new Agenda(id, nome,data_inicio,data_conclusao);

		agendadao.add(agenda);
		response.redirect("/AgendaPage.html");

		response.status(201); // 201 Created
		return id;
	}

	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));
		
		Agenda agenda = (Agenda) agendadao.get(id);
		
		if (agenda != null) {
    	    response.header("Content-Type", "application/xml");
    	    response.header("Content-Encoding", "UTF-8");

            return "<Agenda>\n" + 
            		"\t<id>" + agenda.getId() + "</id>\n" +
            		"\t<nome>" + agenda.getNome() + "</nome>\n" +
            		"\t<DataInicio>" + agenda.getData_inicio() + "</nome>\n" +
            		"\t<DataConclusao>" + agenda.getData_conclusao() + "</nome>\n" +
            		"</Agenda>\n";
        } else {
            response.status(404); // 404 Not found
            return "Produto " + id + " nÃƒÂ£o encontrado.";
        }

	}

	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        
		Agenda agenda = (Agenda) agendadao.get(id);

        if (agenda != null) {
        	agenda.setNome(request.queryParams("nome"));
           

        	agendadao.update(agenda);
        	
            return id;
        } else {
            response.status(404); // 404 Not found
            return "usuario nao encontrado.";
        }

	}

	public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));

        Agenda agenda = (Agenda) agendadao.get(id);

        if (agenda != null) {

            agendadao.remove(agenda);

            response.status(200); // success
        	return id;
        } else {
            response.status(404); // 404 Not found
            return "usuario nÃƒÂ£o encontrado.";
        }
	}

	public Object getAll(Request request, Response response) {
		StringBuffer returnValue = new StringBuffer("<usuarios type=\"array\">");
		for (Agenda agenda : agendadao.getAll()) {
			returnValue.append("<Agenda>\n" + 
            		"\t<id>" + agenda.getId() + "</id>\n" +
            		"\t<nome>" + agenda.getNome() + "</nome>\n" +
            		"\t<DataInicio>" + agenda.getData_inicio() + "</DataConclusao>\n" +
            		"\t<DataConclusao>" + agenda.getData_conclusao() + "</DataConclusao>\n" +
            		"</Agenda>\n");
		}
		returnValue.append("</Agenda>");
	    response.header("Content-Type", "application/xml");
	    response.header("Content-Encoding", "UTF-8");
		return returnValue.toString();
	}
}