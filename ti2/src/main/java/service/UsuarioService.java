package service;

import java.io.IOException;


import java.time.LocalDate;
import java.time.LocalDateTime;

import dao.UsuarioDao;
import model.Usuario;
import spark.Request;
import spark.Response;


public class UsuarioService {

	private UsuarioDao usuarioDao;

	public UsuarioService(){
		try {
			usuarioDao = new UsuarioDao();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		usuarioDao.conectar();
	}

	public Object add(Request request, Response response) {
		String email = request.queryParams("email");
		String nome = request.queryParams("nome_usuario");
		String senha = request.queryParams("senha");

		int id = usuarioDao.getMaxId() + 1;

		Usuario usuario = new Usuario(id, email, nome, senha);

		usuarioDao.add(usuario);

		response.status(201); 
		response.redirect("/login.html");// 201 Created
		return id;
	}

	public Object get(Request request, Response response) {
		usuarioDao.conectar();

        String email = request.params(":email");

        Usuario usuario = (Usuario) usuarioDao.get(email);
        usuarioDao.close();

        response.header("Content-Type", "application/json");
        response.header("Content-Encoding", "UTF-8");

       return usuario;
	}

	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        
		Usuario usuario = (Usuario) usuarioDao.get(id);

        if (usuario != null) {
        	usuario.setEmail(request.queryParams("email"));
            usuario.setNome(request.queryParams("nome"));
            usuario.setSenha(request.queryParams("senha"));

        	usuarioDao.update(usuario);
        	
            return id;
        } else {
            response.status(404); // 404 Not found
            return "usuario nao encontrado.";
        }

	}

	public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));

        Usuario usuario = (Usuario) usuarioDao.get(id);

        if (usuario != null) {

            usuarioDao.remove(usuario);

            response.status(200); // success
        	return id;
        } else {
            response.status(404); // 404 Not found
            return "usuario nÃƒÂ£o encontrado.";
        }
	}

	public Object getAll(Request request, Response response) {
		StringBuffer returnValue = new StringBuffer("<usuarios type=\"array\">");
		for (Usuario usuario : usuarioDao.getAll()) {
			returnValue.append("<usuario>\n" + 
            		"\t<id>" + usuario.getId() + "</id>\n" +
            		"\t<email>" + usuario.getEmail() + "</email>\n" +
            		"\t<nome>" + usuario.getNome() + "</nome>\n" +
            		"\t<senha>" + usuario.getSenha() + "</senha>\n" +
            		"</usuario>\n");
		}
		returnValue.append("</usuarios>");
	    response.header("Content-Type", "application/xml");
	    response.header("Content-Encoding", "UTF-8");
		return returnValue.toString();
	}
}