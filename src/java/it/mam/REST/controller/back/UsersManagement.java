package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.Series;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mirko
 */
public class UsersManagement extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // passa la lista delle serie al template "insert_news.ftl"
    private void action_insert_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        result.activate("insert_group.ftl.html", request, response);
    }

    // controlla l'inserimento corretto di tutti i dati di una news e la salva sul DB
    private void action_save_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Group group = getDataLayer().createGroup(); 
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkGroupInputData(request, response)){
            group.setName(request.getParameter("name"));
            group.setDescription(request.getParameter("description"));
          
            //Ricavo tutte le serie che l'utente ha scelto per la sua News, le trasformo in lista e le setto nella news
            String[] series = request.getParameterValues("series");
            List<Series> seriesList = new ArrayList();
            if (series != null) {
                for (String s: series){
                    try{
                    //prendo la serie dal DB e NON ci metto gli slash perché nel DB ce li ha già e non serve di toglierli perché non devo usarla
                    seriesList.add(getDataLayer().getSeries(SecurityLayer.checkNumeric(s)));
                    } catch (NumberFormatException e) {
                        action_error(request, response, "Field Error");
                    }
                }
                //news.setSeries(seriesList);
            }
            } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
            //Mi prendo la sessione dell'utente che ha fatto la richiesta e se esiste, mi prendo l'utente, 
            //(senza mettere o togliere gli slash perché già ci sono dal DB) altrimenti errore.
            HttpSession session = SecurityLayer.checkSession(request);
            if(session != null){
                //news.setUser(getDataLayer().getUser((int)session.getAttribute("userid")));
            } else {
                action_error(request, response, "Invalid Session - Please login!");
                response.sendRedirect("Login");
            }
        //Aggiungo la data corrente
        Calendar c = Calendar.getInstance();
        //news.setDate(c.getTime());
        //Salvo il commento
        //getDataLayer().storeNews(RESTSecurityLayer.addSlashes(news));
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
     if(request.getParameter("in")!= null){
        try {
            action_save_group(request, response);
        } catch (IOException ex) {
            action_error(request, response, "Field Error");
        }
     } else {
         try {
            action_insert_group(request, response);
        } catch (IOException ex) {
            action_error(request, response, "Field Error");
        }
     }
    }

    private boolean checkGroupInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("description") != null && request.getParameter("description").length() > 0
                && request.getParameterValues("services") != null && request.getParameterValues("services").length > 0;
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
