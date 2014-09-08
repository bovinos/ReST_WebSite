package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author alex
 */
public class SaveNews extends RESTBaseController {

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
            news.setTitle(SecurityLayer.addSlashes(request.getParameter("title")));
            news.setText(SecurityLayer.addSlashes(request.getParameter("text")));
            //Da qui in poi ricavo tutte le serie che l'utente ha scelto per la sua News, le trasformo in lista e le passo
            String[] series = request.getParameterValues("series");
            List<Series> seriesList = new ArrayList();
            if (series != null) {
                for (String s: series){
                    try{
                    seriesList.add(getDataLayer().getSeries(SecurityLayer.checkNumeric(s)));
                    } catch (NumberFormatException e) {
                        action_error(request, response, "Internal Error");
                    }
                }
                news.setSeries(seriesList);
            }
            //Fine settaggio della lista serie
            //Mi prendo la sessione dell'utente che ha fatto la richiesta e se esiste, mi prendo l'utente, altrimenti errore.
            HttpSession session = request.getSession(false);
            if(session != null){
                news.setAuthor((User)session.getAttribute("Author"));
            } else {
                action_error(request, response, "Invalid Session - Please login!");
                response.sendRedirect("Login");
            }
        } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
        getDataLayer().storeNews(news);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
