
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Comment;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mirko
 */
public class SaveCommentNews extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // controlla l'inserimento di un commento e lo salva sul DB
    private void action_newsComment_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Comment comment = getDataLayer().createComment();
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkCommentInputData(request, response)){
            //aggiungo gli slash prima di salvare su DB per evitare valori pericolosi
            comment.setTitle(SecurityLayer.addSlashes(request.getParameter("title")));
            comment.setText(SecurityLayer.addSlashes(request.getParameter("text")));
          } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
          //setto la news del commento senza mettere o togliere slash perché già ci sono dal DB (l'id mi arriva con la request)
        comment.setNews(getDataLayer().getNews(SecurityLayer.checkNumeric(request.getParameter("news"))));
            
            
            //Mi prendo la sessione dell'utente che ha fatto la richiesta e se esiste, mi prendo l'utente, 
            //(senza mettere o togliere gli slash perché già ci sono dal DB) altrimenti errore.
            HttpSession session = SecurityLayer.checkSession(request);
            if(session != null){
                comment.setUser(getDataLayer().getUser((int)session.getAttribute("userid")));
            } else {
                action_error(request, response, "Invalid Session - Please login!");
                response.sendRedirect("Login");
            }

        getDataLayer().storeComment(RESTSecurityLayer.addSlashesComment(comment));
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
try {
            action_newsComment_save(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
    private boolean checkCommentInputData(HttpServletRequest request, HttpServletResponse response){
        return (request.getParameter("title") != null && request.getParameter("title").length() > 0
                && request.getParameter("text") != null && request.getParameter("text").length() > 0);
    }
}
