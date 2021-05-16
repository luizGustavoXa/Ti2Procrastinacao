package app;

import static spark.Spark.*;

import app.Render;
import service.AgendaService;
import service.QuizService;
import service.UsuarioService;
import spark.Request;
import spark.Response;
public class Aplicacao {

    private static UsuarioService usuarioService = new UsuarioService();
    private static AgendaService agendaService = new AgendaService();
    private static QuizService quizService = new QuizService();
    private static Render render = new Render();


    public static void main(String[] args) {
    	port(getHerokuAssignedPort());
    	
    	staticFiles.location("/public");

    	
    	before((req, res) -> {
		      String path = req.pathInfo();
		      if (path.endsWith("/"))
		        res.redirect(path.substring(0, path.length() - 1));
	    });
		
        

        get("/", (req,res) -> Aplicacao.index(req, res)); 

        post("/usuario", (request, response) -> usuarioService.add(request, response));
        
        post("/agenda", (request, response) -> agendaService.add(request, response));
        
        post("/quiz", (request, response) -> quizService.add(request, response));

        get("/usuario/:id", (request, response) -> usuarioService.get(request, response));

        get("/usuario/update/:id", (request, response) -> usuarioService.update(request, response));

        get("/usuario/delete/:id", (request, response) -> usuarioService.remove(request, response));

        get("/usuario", (request, response) -> usuarioService.getAll(request, response));
        
       



    }
    
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    
    
    public static String index(Request req, Response res) {
		res.type("text/html");
		try{
			return render.renderContent("index.html");
		}
		catch(Exception e) {
			return e.getMessage();
		}
    }
}