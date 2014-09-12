
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.Series;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
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
public class InsertEpisode extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // passa la lista delle serie al template "insert_news.ftl"
    private void action_episode_insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        //Qui creo la lista delle serie che passo al template, in modo che si possa scegliere (opzionalmente)
        //la serie a cui appartiene l'episodio.
        List<Series> series;
        series = getDataLayer().getSeries();
        for (Series s: series){
           s = RESTSecurityLayer.stripSlashes(s);
        }
        request.setAttribute("series", series);
        
        //Qui creo la lista dei canali che passo al template, in modo che si possa scegliere (opzionalmente)
        //il canale o i canali su cui verrà trasmesso l'episodio.
        List<Channel> channels;
        channels = getDataLayer().getChannels();
        for (Channel c: channels){
           c = RESTSecurityLayer.stripSlashes(c);
        }
        request.setAttribute("channels", channels);
        
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
