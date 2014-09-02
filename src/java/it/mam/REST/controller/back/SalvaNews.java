package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.News;
import it.univaq.f4i.iw.framework.result.FailureResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SalvaNews extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutte le news e le passa al template lista_news.ftl.html
    private void action_news_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        News news = getDataLayer().createNews();
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection ecc
        if (request.getParameter("title") != null && request.getParameter("title").length() > 0
                && request.getParameter("text") != null && request.getParameter("text").length() > 0) {
            news.setTitle(request.getParameter("title"));
            news.setText(request.getParameter("text"));
            if (request.getParameterValues("series") != null) {
                /* e qua? xD */
            }
            // settare l'autore prendendolo dalla sessione
        } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
