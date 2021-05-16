package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dao.QuizDao;
import dao.UsuarioDao;
import model.Quiz;
import model.Usuario;
import spark.Request;
import spark.Response;


public class QuizService {

	private QuizDao quizdao;

	public QuizService(){
		try {
			quizdao = new QuizDao();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		quizdao.conectar();
	}

	public Object add(Request request, Response response) {
		String nome = request.queryParams("nome");
		
		int id = quizdao.getMaxId() + 1;

		Quiz quiz = new Quiz(id, nome);

		quizdao.add(quiz);

		response.status(201); // 201 Created
		return id;
	}

	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));
		
		Quiz quiz = (Quiz) quizdao.get(id);
		
		if (quiz != null) {
    	    response.header("Content-Type", "application/xml");
    	    response.header("Content-Encoding", "UTF-8");

            return "<usuario>\n" + 
            		"\t<id>" + quiz.getId() + "</id>\n" +
            		"\t<nome>" + quiz.getNomeQuiz() + "</nome>\n" +
            		"</usuario>\n";
        } else {
            response.status(404); // 404 Not found
            return "Produto " + id + " nÃƒÂ£o encontrado.";
        }

	}

	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        
		Quiz quiz = (Quiz) quizdao.get(id);

        if (quiz != null) {
        	quiz.setNomeQuiz(request.queryParams("nome"));
           

        	quizdao.update(quiz);
        	
            return id;
        } else {
            response.status(404); // 404 Not found
            return "usuario nao encontrado.";
        }

	}

	public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));

        Quiz quiz = (Quiz) quizdao.get(id);

        if (quiz != null) {

            quizdao.remove(quiz);

            response.status(200); // success
        	return id;
        } else {
            response.status(404); // 404 Not found
            return "usuario nÃƒÂ£o encontrado.";
        }
	}

	public Object getAll(Request request, Response response) {
		StringBuffer returnValue = new StringBuffer("<usuarios type=\"array\">");
		for (Quiz quiz : quizdao.getAll()) {
			returnValue.append("<usuario>\n" + 
            		"\t<id>" + quiz.getId() + "</id>\n" +
            		"\t<email>" + quiz.getNomeQuiz() + "</email>\n" +
            		"</usuario>\n");
		}
		returnValue.append("</usuarios>");
	    response.header("Content-Type", "application/xml");
	    response.header("Content-Encoding", "UTF-8");
		return returnValue.toString();
	}
}