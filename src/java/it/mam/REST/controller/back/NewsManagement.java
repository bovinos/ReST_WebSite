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

    /* ============================== INSERT - SAVE =========================================*/
    
    // Activates the insert news template
    private void action_activate_insert_news(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        if(SecurityLayer.checkSession(request) != null){ 
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(user.getGroup().getID()!= Group.ADMIN) { 
            action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
        }
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("user", user);
        request.setAttribute("series", getDataLayer().getSeries());
        request.setAttribute("where", "back");
        request.setAttribute("backContent_tpl", "insertNews.ftl.html");
        result.activate("../back/backOutline.ftl.html", request, response);
           } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

     // Receives all the necessary data to insert a News and, if everything's ok, saves it in the Database
    private void action_save_news(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try{
        TemplateResult result = new TemplateResult(getServletContext());
        if(SecurityLayer.checkSession(request) != null){ 
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(user.getGroup().getID()!= Group.ADMIN) { 
            action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
        }
        request.setAttribute("user", user);
        News news = getDataLayer().createNews();
        if (checkNewsInputData(request, response)){
            news.setTitle(request.getParameter("newsTitle"));
            news.setText(request.getParameter("newsText"));
            if(request.getParameter("newsImageURL") != null && request.getParameter("newsImageURL").length() > 0){
                news.setImageURL(request.getParameter("newsImageURL"));
            }

            String[] series = request.getParameterValues("series");
            List<Series> seriesList = new ArrayList();
            if (series != null) {
                for (String s: series){
                    seriesList.add(getDataLayer().getSeries(SecurityLayer.checkNumeric(s)));
                }
                news.setSeries(seriesList);
            }
            } else {
            //Error: field empty
            request.setAttribute("error", "Uno dei campi obbligatori è vuoto!");
            action_activate_insert_news(request, response);
            return;
        }
                news.setUser(user);
            System.err.println(news);
        getDataLayer().storeNews(RESTSecurityLayer.addSlashes(news));
       } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
            return;
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
              return;
    }
        request.setAttribute("success", "News inserita correttamente!");
        action_activate_insert_news(request, response);
    }
    
    /* ============================== REMOVE - DELETE ===============================*/
    // Activates the remove news template
    private void action_activate_remove_news(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        if(SecurityLayer.checkSession(request) != null){ 
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(user.getGroup().getID()!= Group.ADMIN) { 
            action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
        }
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("user", user);
        request.setAttribute("news", getDataLayer().getNews());
        request.setAttribute("where", "back");
        request.setAttribute("backContent_tpl", "removeNews.ftl.html");
        result.activate("../back/backOutline.ftl.html", request, response);
           } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }
    
// Receives all the necessary data to delete news
    private void action_delete_news(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try{
        TemplateResult result = new TemplateResult(getServletContext());
        if(SecurityLayer.checkSession(request) != null){ 
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(user.getGroup().getID()!= Group.ADMIN) { 
            action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
        }
        request.setAttribute("user", user);
        if (request.getParameterValues("news") == null|| request.getParameterValues("news").length <= 0){
            action_error(request, response, "Riprova di nuovo!");
            return;
        }
        String[] news = request.getParameterValues("news");
        for(String n: news) {
            getDataLayer().removeNews(getDataLayer().getNews(SecurityLayer.checkNumeric(n)));
        }
       } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
            return;
        }
          } catch (NumberFormatException ex) {
              //User id or news id is not a number
              action_error(request, response, "Riprova di nuovo!");
              return;
    }
         request.setAttribute("success", "Rimozione news completata!");
        action_activate_remove_news(request, response);
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
         int sezione = SecurityLayer.checkNumeric(request.getParameter("sezione"));
         switch(sezione){
             case 1:
                 request.setAttribute("currentSection", sezione);
                    if(request.getParameter("in")!= null){
                    action_save_news(request, response);     
                    } else {
                    action_activate_insert_news(request, response);
                    }
             break;
             case 2:
                 request.setAttribute("currentSection", sezione);
                    if(request.getParameter("rn")!= null){
                    action_delete_news(request, response);     
                    } else {
                    action_activate_remove_news(request, response);
                    }
            break;
            default: action_error(request, response, "Riprova di nuovo!");
         }
          } catch (IOException | NumberFormatException ex) {
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Checks if all the input fields have been filled
    private boolean checkNewsInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("newsTitle") != null && request.getParameter("newsTitle").length() > 0
                && request.getParameter("newsText") != null && request.getParameter("newsText").length() > 0
                && request.getParameterValues("series") != null && request.getParameterValues("series").length > 0;
    }
    
    @Override
    public String getServletInfo() {
        return "This servlet creates the news insert template and the news remove template and allow to save or delete a news";
    }

}
