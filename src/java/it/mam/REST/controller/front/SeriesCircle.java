package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Message;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SeriesCircle extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutti i messaggi di una determinata serie e li passa al template seriesCircle.ftl
    private void action_activate_circle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("series", getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("id"))));
        //Controllo che la sessione attuale sia ancora valida
        if (SecurityLayer.checkSession(request) != null){
        try {
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", user);
        }   catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
        }
        result.activate("seriesCircle.ftl.html", request, response);
    }

    private void action_message_circle (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); }
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("sid")));
        Calendar c = Calendar.getInstance();
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
        }catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if(request.getParameter("scms") != null){
       try {
            action_message_circle(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
    }  
        } else {
        try {
            action_activate_circle(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
    }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
