package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Message;
import it.mam.REST.data.model.Series;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SeriesCircle extends RESTBaseController {

    // Creates the default template error and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }
   
   // Activates the series circle template
    private void action_activate_circle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "series");
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("id")));
        request.setAttribute("series", series);
        //User session checking
        if (SecurityLayer.checkSession(request) != null) {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                request.setAttribute("user", user);
                if(user.getSeries().contains(series)){
                    result.activate("seriesCircle.ftl.html", request, response);
                    return;
                } else{
                    //This series is not contained in this user's favourites
                    action_error(request, response, "Per entrare in questa cerchia, aggiungi questa serie alle preferite!");
                }
                
                //Page management
            int page; //page number 
            if(request.getParameter("page") != null) {
            page = SecurityLayer.checkNumeric(request.getParameter("page"));
            } else {
            page = 1;
            }
            List<Message> messagesList = series.getMessages();
            Collections.reverse(messagesList);
            request.setAttribute("currentPage", page);
            int messagesPerPage = 10; // number of messages per page
            int numberOfPages = (int) Math.ceil((double)messagesList.size()/messagesPerPage); // total number of pages
            request.setAttribute("totalPages", numberOfPages);
             if(page == numberOfPages || messagesList.isEmpty()) {
            request.setAttribute("messages", messagesList);
            request.setAttribute("previousLastCommentIndex", (page-1)*messagesPerPage);
            } else if (page > numberOfPages || page < 1) {
            action_error(request, response, "Riprova di nuovo!");
            } else {
            request.setAttribute("messages", messagesList.subList(0, (page *messagesPerPage)));
            request.setAttribute("previousLastCommentIndex", (page-1)*messagesPerPage);
            }
             
             RESTSortLayer.checkNotifications(user, request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per visualizzare questa pagina!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
                //series id or user id is not a number
                action_error(request, response, "Riprova di nuovo!");
            }
    }

   // Receives all the necessary data to send a message in the circle and, if everything's ok, saves it in the Database
    private void action_message_circle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            //User session checking
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("sid")));
            Calendar c = Calendar.getInstance();
            if(checkMessageInputData(request, response)){
            String title = request.getParameter("messageTitle");
            String text = request.getParameter("messageText");
            Message message = getDataLayer().createMessage();
            message.setTitle(title);
            message.setText(text);
            message.setUser(user);
            message.setDate(c.getTime());
            message.setSeries(series);
            getDataLayer().storeMessage(RESTSecurityLayer.addSlashes(message));
            response.sendRedirect("CerchiaSerie?id=" + series.getID());
            } else {
                //Error: field empty
                request.setAttribute("error", "Errore: uno dei campi è vuoto!");
                action_activate_circle(request, response);
            }
           } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per mandare messaggi in una cerchia!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //user id or series id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
        if (request.getParameter("scms") != null) {
            
                action_message_circle(request, response);
            
        } else {
                action_activate_circle(request, response);
        }
        } catch (IOException ex) {
                action_error(request, response, "Riprova di nuovo!");
            }
    }

        // Checks if all the input fields have been filled
    private boolean checkMessageInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("messageTitle") != null && request.getParameter("messageTitle").length() > 0
                && request.getParameter("messageText") != null && request.getParameter("messageText").length() > 0;
    }
    
    @Override
    public String getServletInfo() {
        return "This servlet activates the series circle template to show all the messages sent by users. It also allow a user to send"
                + "a message in the circle ad saves it in the database";
    }
}
