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
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mirko
 */
public class NewsManagement extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // passa la lista delle serie al template "insert_news.ftl"
    private void action_insert_news(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response);}
        if(user.getGroup().getID()!= Group.ADMIN) { result.activate("newsList.ftl.html", request, response);}
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        //Qui creo la lista delle serie che passo al template, in modo che si possa scegliere (opzionalmente)
        //la serie o le serie a cui la news si riferisce. Non passo la lista dei generi perché non ce n'è bisogno lì.
        request.setAttribute("series", getDataLayer().getSeries());
        request.setAttribute("backContent_tpl", "insertNews.ftl.html");
        result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
    }

    // controlla l'inserimento corretto di tutti i dati di una news e la salva sul DB
    private void action_save_news(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response);}
        if(user.getGroup().getID()!= Group.ADMIN) { result.activate("newsList.ftl.html", request, response);}
        News news = getDataLayer().createNews();
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkNewsInputData(request, response)){
            news.setTitle(request.getParameter("title"));
            news.setText(request.getParameter("text"));
          
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
            action_error(request, response, "Inserire i campi obbligatori");
        }
                news.setUser(user);
        //Aggiungo la data corrente
        Calendar c = Calendar.getInstance();
        news.setDate(c.getTime());
        //Salvo il commento
        getDataLayer().storeNews(RESTSecurityLayer.addSlashes(news));
      } catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
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
        return request.getParameter("title") != null && request.getParameter("title").length() > 0
                && request.getParameter("text") != null && request.getParameter("text").length() > 0
                && request.getParameterValues("series") != null && request.getParameterValues("series").length > 0;
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
