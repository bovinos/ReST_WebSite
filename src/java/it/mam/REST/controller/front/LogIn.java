package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.Utility;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogIn extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    private void action_submit_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        if (checkLoginInputData(request, response)) {
            String username = SecurityLayer.addSlashes(request.getParameter("username"));
            String password = Utility.stringToMD5(Utility.stringToMD5(request.getParameter("password")));
            user = getDataLayer().getUser(username, password);
            if (user == null) {
                action_error(request, response, "Hai inserito dei dati non validi");
                return;
            }
            SecurityLayer.createSession(request, username, user.getID());

        } else {
            action_error(request, response, "Non hai riempito tutti i campi necessari!");
        }
        // in realtà dovrei ridirigere alla pagina in cui ha fatto il login
        response.sendRedirect("ListaNews");
    }

    private void action_activate_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("logIn.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (request.getParameter("logIn") != null) {

            try {
                action_submit_login(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        } else {
            try {
                action_activate_login(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private boolean checkLoginInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("username") != null && request.getParameter("username").length() > 0
                && request.getParameter("password") != null && request.getParameter("password").length() > 0;
    }
}
