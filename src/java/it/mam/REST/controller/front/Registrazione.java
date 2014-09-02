package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class Registrazione extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    private void action_registrazione(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("registrazione_utente.ftl.html", request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_registrazione(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
