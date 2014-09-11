
package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
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
    
    // prende tutte le informazioni su una news e le passa al template newsCard.ftl.html
    private void action_news_info(HttpServletRequest request, HttpServletResponse response, int id) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("news", RESTSecurityLayer.stripSlashesNews(getDataLayer().getNews(id)));
        //Controllo che la sessione attuale sia ancora valida
        if (SecurityLayer.checkSession(request) != null){
        String username = SecurityLayer.addSlashes((String)request.getSession().getAttribute("username"));
        request.setAttribute("sessionUsername", username);
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", RESTSecurityLayer.stripSlashesUser(user));
        }
        result.activate("newsCard.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            int id = SecurityLayer.checkNumeric(request.getParameter("id"));
            action_news_info(request, response, id);
        } catch (ServletException | IOException | NumberFormatException ex) {
            action_error(request, response, "Errore caricamento dati");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}