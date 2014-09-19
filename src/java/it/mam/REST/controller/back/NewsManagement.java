package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class NewsManagement extends RESTBaseController {

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // Activates the insert news template
    private void action_insert_news(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(SecurityLayer.checkSession(request) != null){ 
        if(user.getGroup().getID()!= Group.ADMIN) { result.activate("newsList.ftl.html", request, response);}
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("user", user);
        //Qui creo la lista delle serie che passo al template, in modo che si possa scegliere (opzionalmente)
        //la serie o le serie a cui la news si riferisce. Non passo la lista dei generi perché non ce n'è bisogno lì.
        request.setAttribute("series", getDataLayer().getSeries());
        request.setAttribute("where", "back");
        request.setAttribute("backContent_tpl", "insertNews.ftl.html");
        result.activate("../back/backOutline.ftl.html", request, response);
           } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            response.sendRedirect("LogIn");
        }
        } catch (NumberFormatException ex){
            SecurityLayer.disposeSession(request);
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            response.sendRedirect("LogIn");
        }
    }

     // Receives all the necessary data to insert a News and, if everything's ok, saves it in the Database
    private void action_save_news(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(SecurityLayer.checkSession(request) != null){ 
        if(user.getGroup().getID()!= Group.ADMIN) { result.activate("newsList.ftl.html", request, response);}
        request.setAttribute("user", user);
        News news = getDataLayer().createNews();
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkNewsInputData(request, response)){
            news.setTitle(request.getParameter("newsTitle"));
            news.setText(request.getParameter("newsText"));
            if(request.getParameter("newsImageURL") != null && request.getParameter("newsImageURL").length() > 0){
                news.setImageURL(request.getParameter("newsImageURL"));
            }
            //Ricavo tutte le serie che l'utente ha scelto per la sua News, le trasformo in lista e le setto nella news
            String[] series = request.getParameterValues("series");
            List<Series> seriesList = new ArrayList();
            if (series != null) {
                for (String s: series){
                    //prendo la serie dal DB e NON ci metto gli slash perché nel DB ce li ha già e non serve di toglierli perché non devo usarla
                    seriesList.add(getDataLayer().getSeries(SecurityLayer.checkNumeric(s)));
                }
                news.setSeries(seriesList);
            }
            } else {
            request.setAttribute("error", "Errore: uno dei campi è vuoto!");
            response.sendRedirect("GestioneNews");
        }
                news.setUser(user);
        //Salvo il commento
            System.err.println(news);
        getDataLayer().storeNews(RESTSecurityLayer.addSlashes(news));
       } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            response.sendRedirect("LogIn");
        }
        } catch (NumberFormatException ex){
            SecurityLayer.disposeSession(request);
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            response.sendRedirect("LogIn");
        }
        response.sendRedirect("GestioneNews");
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
     if(request.getParameter("in")!= null){
        try {
            action_save_news(request, response);
        } catch (IOException ex) {
            action_error(request, response, "Field Error");
        }
     } else {
         try {
            action_insert_news(request, response);
        } catch (IOException ex) {
            action_error(request, response, "Field Error");
        }
     }
    }

    private boolean checkNewsInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("newsTitle") != null && request.getParameter("newsTitle").length() > 0
                && request.getParameter("newsText") != null && request.getParameter("newsText").length() > 0
                && request.getParameterValues("series") != null && request.getParameterValues("series").length > 0;
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
