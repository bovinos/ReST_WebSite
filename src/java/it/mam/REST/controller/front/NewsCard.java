
package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.RESTSortLayer;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class NewsCard extends RESTBaseController{

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }
    
    // Activates the News Card template
    private void action_news_info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        News news = getDataLayer().getNews(SecurityLayer.checkNumeric(request.getParameter("id")));
        if(news == null) {
                action_error(request, response, "Riprova di nuovo!");
                System.err.println("Errore in NewsCard.java, nel metodo action_news_info: l'ID della news ricevuto non corrisponde a nessuna news nel Database");
                return;
            }
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "news");
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("news", news);
        //Controllo che la sessione attuale sia ancora valida
        if (SecurityLayer.checkSession(request) != null){   
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", user);
        RESTSortLayer.checkNotifications(user, request, response);
        }
            //Start Page Management ====================================================================
            int page; //page number 
            if(request.getParameter("page") != null) {
            page = SecurityLayer.checkNumeric(request.getParameter("page"));
            } else {
            page = 1;
            }
            List<Comment> commentsList = news.getComments();
            Collections.reverse(commentsList);
            request.setAttribute("currentPage", page);
            int commentsPerPage = 10; // number of comments per page
            int numberOfPages = (int) Math.ceil((double)commentsList.size()/commentsPerPage); // total number of pages
            request.setAttribute("totalPages", numberOfPages);
            if(page == numberOfPages || commentsList.isEmpty()){
            request.setAttribute("comments", commentsList);
            request.setAttribute("previousLastCommentIndex", (page-1)*commentsPerPage);
            } else if (page > numberOfPages || page < 1) {
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in SeriesCard.java, nel metodo action_series_info: la pagina corrente è maggiore del numero totale di pagine o è minore di 1");
            return;
            } else {
            request.setAttribute("comments", commentsList.subList(0, (page *commentsPerPage)));
            request.setAttribute("previousLastCommentIndex", (page-1)*commentsPerPage);
            }
            // End Page Management ====================================================================
             result.activate("newsCard.ftl.html", request, response);
            } catch (NumberFormatException ex) {
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in NewsCard.java, nel metodo action_news_info: NumberFormatException");
    }
    }

    // Receives all the necessary data to insert a comment to a news
    private void action_comment_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        if(SecurityLayer.checkSession(request) != null){ 
        if(checkNewsCommentInputData(request, response)){
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        News news = getDataLayer().getNews(SecurityLayer.checkNumeric(request.getParameter("nid")));
                if(news == null) {
                action_error(request, response, "Riprova di nuovo!");
                System.err.println("Errore in NewsCard.java, nel metodo action_comment_news: l'ID della news ricevuto non corrisponde a nessuna news nel Database");
                return;
                }
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
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
        } else {
            request.setAttribute("error", "Uno dei campi è vuoto!");
            action_news_info(request, response);
        }
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id or news id is not a number
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in NewsCard.java, nel metodo action_comment_news: NumberFormatException");
        }
    }
    
    // Allows a user to like a news
     private void action_like_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         try{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) != null){ 
         News news = getDataLayer().getNews(SecurityLayer.checkNumeric((request.getParameter("ln"))));
                if(news == null) {
                action_error(request, response, "Riprova di nuovo!");
                System.err.println("Errore in NewsCard.java, nel metodo action_like_news: l'ID della news ricevuto non corrisponde a nessuna news nel Database");
                return;
                }
         news.setLikes(news.getLikes() + 1);
         getDataLayer().storeNews(news);
         response.sendRedirect("SchedaNews?id=" + news.getID());   
          } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //News id is not a number
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in NewsCard.java, nel metodo action_like_news: NumberFormatException");
        }
     }
    
     //Allows a user to dislike a news
     private void action_dislike_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         try{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) != null){ 
         News news = getDataLayer().getNews(SecurityLayer.checkNumeric((request.getParameter("dn"))));
                if(news == null) {
                action_error(request, response, "Riprova di nuovo!");
                System.err.println("Errore in NewsCard.java, nel metodo action_dislike_news: l'ID della news ricevuto non corrisponde a nessuna news nel Database");
                return;
                }
         news.setDislikes(news.getDislikes() + 1);
         getDataLayer().storeNews(news);
         response.sendRedirect("SchedaNews?id=" + news.getID());
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //News id is not a number
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in NewsCard.java, nel metodo action_dislike_news: NumberFormatException");
        }
     }
     
     //Allows a user to like a news comment
      private void action_like_comment_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
          try{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) != null){ 
         Comment comment = getDataLayer().getComment(SecurityLayer.checkNumeric(request.getParameter("lc")));
                 if(comment == null) {
                action_error(request, response, "Riprova di nuovo!");
                System.err.println("Errore in NewsCard.java, nel metodo action_like_comment_news: l'ID del commento ricevuto non corrisponde a nessun commento nel Database");
                return;
            }
         comment.setLikes(comment.getLikes() + 1);
         getDataLayer().storeComment(comment);
                if((getDataLayer().getNews(SecurityLayer.checkNumeric(request.getParameter("n")))) == null) {
                action_error(request, response, "Riprova di nuovo!");
                System.err.println("Errore in NewsCard.java, nel metodo action_like_comment_news: l'ID della news ricevuto non corrisponde a nessuna news nel Database");
                return;
            }
         response.sendRedirect("SchedaNews?id=" + SecurityLayer.checkNumeric(request.getParameter("n")));   
          } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //Comment id or news id is not a number
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in NewsCard.java, nel metodo action_like_comment_news: NumberFormatException");
        }
     }
    
    //Allows a user to dislike a news comment
     private void action_dislike_comment_news (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         try{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) != null){ 
         Comment comment = getDataLayer().getComment(SecurityLayer.checkNumeric((request.getParameter("dc"))));
                if(comment == null) {
                action_error(request, response, "Riprova di nuovo!");
                System.err.println("Errore in NewsCard.java, nel metodo action_dislike_comment_news: l'ID del commento ricevuto non corrisponde a nessun commento nel Database");
                return;
            }
         comment.setDislikes(comment.getDislikes() + 1);
         getDataLayer().storeComment(comment);
         response.sendRedirect("SchedaNews?id=" + SecurityLayer.checkNumeric((request.getParameter("n"))));  
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //Comment id or User id is not a number
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in NewsCard.java, nel metodo action_dislike_comment_news: NumberFormatException");
        }
     }
     
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if(request.getParameter("ncs") != null){
        
            action_comment_news(request, response);

        } else if (request.getParameter("ln") != null) {
            
            action_like_news(request, response);

        } else if (request.getParameter("dn") != null) {

            action_dislike_news(request, response);

        } else if (request.getParameter("lc") != null) {

            action_like_comment_news(request, response);

        } else if (request.getParameter("dc") != null) {

            action_dislike_comment_news(request, response);
            
        }else {

            action_news_info(request, response);

        }
        } catch (IOException ex) {
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore nella Process Request di NewsCard.java: IOException");
        }
    }
    
    // Checks if all the input fields have been filled
    private boolean checkNewsCommentInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("commentTitle") != null && request.getParameter("commentTitle").length() > 0
                && request.getParameter("commentText") != null && request.getParameter("commentText").length() > 0;
    }
    @Override
    public String getServletInfo() {
        return "This servlet controls most of the actions related to news. It shows the news card, allow to like or dislike the news,"
                + " to comment or like and dislike a comment";
    }
}