package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.RESTUtility;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogIn extends RESTBaseController {

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // Activates the LogIn template
    private void action_activate_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (SecurityLayer.checkSession(request) != null) {
            action_error(request, response, "Sei già loggato!");
            return;
        }
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("logIn.ftl.html", request, response);
    }

    // Receives all the necessary data to log a user in
    private void action_submit_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        if (checkLoginInputData(request, response)) {
            String username = SecurityLayer.addSlashes(request.getParameter("username"));
            String password = RESTUtility.stringToMD5(RESTUtility.stringToMD5(request.getParameter("password")));
            user = getDataLayer().getUser(username, password);
            if (user == null) {
                request.setAttribute("error", "Errore: hai inserito dei dati non validi!");
                action_activate_login(request, response);
                return;
            }
            SecurityLayer.createSession(request, username, user.getID());

        } else {
            request.setAttribute("error", "Errore: uno dei campi è vuoto!");
            action_activate_login(request, response);
            return;
        }
        response.sendRedirect("ListaNews");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("logIn") != null) {
                action_submit_login(request, response);
            } else {

                action_activate_login(request, response);
            }
        } catch (IOException ex) {
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore nella Process Request di LogIn.java: IOException");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet activates the LogIn template, to allow anyone to lof into this website.";
    }

    // Checks if all the input fields have been filled
    private boolean checkLoginInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("username") != null && request.getParameter("username").length() > 0
                && request.getParameter("password") != null && request.getParameter("password").length() > 0;
    }
}
