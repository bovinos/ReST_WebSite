
package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
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
    private void action_news_info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "news");
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("news", getDataLayer().getNews(SecurityLayer.checkNumeric(request.getParameter("id"))));
        //Controllo che la sessione attuale sia ancora valida
        if (SecurityLayer.checkSession(request) != null){
            try{
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", user);
         } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
        }
        result.activate("newsCard.ftl.html", request, response);
    }

    private void action_comment_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); }
        if(checkNewsCommentInputData(request, response)){
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        News news = getDataLayer().getNews(SecurityLayer.checkNumeric(request.getParameter("nid")));
        Calendar c = Calendar.getInstance();
        String title = request.getParameter("commentTitle");
        String text = request.getParameter("commentText");
        Comment comment = getDataLayer().createComment();
        comment.setTitle(title);
        comment.setText(text);
        comment.setUser(user);
        comment.setDate(c.getTime());
        comment.setNews(news);
        getDataLayer().storeComment(RESTSecurityLayer.addSlashes(comment));
        response.sendRedirect("SchedaNews?id=" + news.getID());
        } else action_error(request, response, "Inserisci i campi obbligatori!");
        }catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
    }
    
     private void action_like_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); } 
         News news = getDataLayer().getNews(SecurityLayer.checkNumeric((request.getParameter("ln"))));
         news.setLikes(news.getLikes() + 1);
         getDataLayer().storeNews(news);
         response.sendRedirect("SchedaNews?id=" + news.getID());   
     }
    
     private void action_dislike_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); }
         News news = getDataLayer().getNews(SecurityLayer.checkNumeric((request.getParameter("dn"))));
         news.setDislikes(news.getDislikes() + 1);
         getDataLayer().storeNews(news);
         response.sendRedirect("SchedaNews?id=" + news.getID());
     }
     
      private void action_like_comment_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
          try{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); } 
         Comment comment = getDataLayer().getComment(SecurityLayer.checkNumeric(request.getParameter("lc")));
         comment.setLikes(comment.getLikes() + 1);
         getDataLayer().storeComment(comment);
         response.sendRedirect("SchedaNews?id=" + SecurityLayer.checkNumeric(request.getParameter("n")));   
          } catch (NumberFormatException ex) {
              action_error(request, response, "Field Error");
          }
     }
    
     private void action_dislike_comment_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         try{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); }
         Comment comment = getDataLayer().getComment(SecurityLayer.checkNumeric((request.getParameter("dc"))));
         comment.setDislikes(comment.getDislikes() + 1);
         getDataLayer().storeComment(comment);
         response.sendRedirect("SchedaNews?id=" + SecurityLayer.checkNumeric((request.getParameter("n"))));  
        } catch (NumberFormatException ex) {
              action_error(request, response, "Field Error");
          }
     }
     
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if(request.getParameter("ncs") != null){
        try {
            action_comment_news(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
        } else if (request.getParameter("ln") != null) {
        try {
            action_like_news(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
        } else if (request.getParameter("dn") != null) {
        try {
            action_dislike_news(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
        } else if (request.getParameter("lc") != null) {
        try {
            action_like_comment_news(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
        } else if (request.getParameter("dc") != null) {
        try {
            action_dislike_comment_news(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
        }else {
            try {
            action_news_info(request, response);
        } catch (IOException | NumberFormatException ex) {
            action_error(request, response, ex.getMessage());
        }
        }
    }
    
    private boolean checkNewsCommentInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("commentTitle") != null && request.getParameter("commentTitle").length() > 0
                && request.getParameter("commentText") != null && request.getParameter("commentText").length() > 0;
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}