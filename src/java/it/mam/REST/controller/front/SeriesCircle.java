package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
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
    private void action_series_messages(HttpServletRequest request, HttpServletResponse response, int id_series) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("series", getDataLayer().getSeries(id_series));
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

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        int id_series;
        try {
            id_series = SecurityLayer.checkNumeric(request.getParameter("id"));
            action_series_messages(request, response, id_series);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
