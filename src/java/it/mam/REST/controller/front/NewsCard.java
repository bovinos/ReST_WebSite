
package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.News;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class NewsCard extends RESTBaseController{

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            processRequest(request, response);
        } catch (ServletException ex) {
            action_error(request, response, "Errore caricamento dati");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        action_error(request, response, "Errore caricamento dati");
    }

    // prende tutte le news e le passa al template lista_news.ftl.html
    private void action_series_info(HttpServletRequest request, HttpServletResponse response, int id) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        News n = getDataLayer().getNews(id);
        request.setAttribute("news", n);
        // decommentare se nel momento dell'inserimento abbiamo inserito slash per evitare SQL injection
        //request.setAttribute("stripSlashes", new SplitSlashesFmkExt());
        result.activate("newsCard.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        try {
            int id = SecurityLayer.checkNumeric(request.getParameter("id"));
            action_series_info(request, response, id);
        } catch (ServletException | IOException | NumberFormatException ex) {
            action_error(request, response, "Errore caricamento dati");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}