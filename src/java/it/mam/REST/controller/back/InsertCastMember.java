
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Series;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class InsertCastMember extends RESTBaseController {

   // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // passa la lista delle serie al template "insert_news.ftl"
    private void action_insert_castmember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        List<Series> series;
        series = getDataLayer().getSeries();
        for (Series s: series){
            s.setName(SecurityLayer.stripSlashes(s.getName()));
            s.setDescription(SecurityLayer.stripSlashes(s.getDescription()));
            s.setImageURL(SecurityLayer.stripSlashes(s.getImageURL()));
            s.setState(SecurityLayer.stripSlashes(s.getState()));
        }
        request.setAttribute("series", series);
        // decommentare se nel momento dell'inserimento abbiamo inserito slash per evitare SQL injection
        //request.setAttribute("stripSlashes", new SplitSlashesFmkExt());
        result.activate("insert_castmember.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_insert_castmember(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}