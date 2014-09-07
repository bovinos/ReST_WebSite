package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SeriesCircle extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutte le news e le passa al template lista_news.ftl.html
    private void action_series_messages(HttpServletRequest request, HttpServletResponse response, int id_series) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("messages", getDataLayer().getMessages(getDataLayer().getSeries(id_series)));
        // decommentare se nel momento dell'inserimento abbiamo inserito slash per evitare SQL injection
        //request.setAttribute("stripSlashes", new SplitSlashesFmkExt());
        result.activate("cerchia_serie.ftl", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        int id_series;
        try {
            id_series = SecurityLayer.checkNumeric(request.getParameter("id_series"));
            action_series_messages(request, response, id_series);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
