
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class InsertEpisode extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // passa la lista delle serie al template "insert_news.ftl"
    private void action_episode_insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
             
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        //Qui creo la lista delle serie che passo al template, in modo che si possa scegliere (opzionalmente)
        //la serie a cui appartiene l'episodio.
        request.setAttribute("series", getDataLayer().getSeries());      
        //Qui creo la lista dei canali che passo al template, in modo che si possa scegliere (opzionalmente)
        //il canale o i canali su cui verrà trasmesso l'episodio.
        request.setAttribute("channels", getDataLayer().getChannels());
        result.activate("insert_episode.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_episode_insert(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
