
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Series;
import it.univaq.f4i.iw.framework.result.FailureResult;
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
public class SaveEpisode extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // controlla l'inserimento corretto di tutti i dati di una news e la salva sul DB
    private void action_episode_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Episode episode = getDataLayer().createEpisode();
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkEpisodeInputData(request, response)){
            
            episode.setTitle(request.getParameter("title"));
            episode.setDescription((request.getParameter("description")));
            episode.setNumber(SecurityLayer.checkNumeric(request.getParameter("number")));
            episode.setSeason(SecurityLayer.checkNumeric(request.getParameter("season")));
            episode.setSeries(getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("idseries"))));
            //Ricavo tutti i canali che l'utente ha scelto per l'episodio, li trasformo in lista e li setto nell'episodio
            String [] channels = request.getParameterValues("channels");
            List<Channel> channelList = new ArrayList();
            for(String c: channels) {
                try{
                   channelList.add(getDataLayer().getChannel(SecurityLayer.checkNumeric(c)));
                    } catch (NumberFormatException e) {
                        action_error(request, response, "Field Error");
                    }

            }
            episode.setChannels(channelList);
          } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
     
        getDataLayer().storeEpisode(RESTSecurityLayer.addSlashesEpisode(episode));
            
 
        //Aggiungo la data alla relazione
        ChannelEpisode ce = getDataLayer().createChannelEpisode();
     if (request.getParameter("date") != null && request.getParameter("date").length() > 0){
         ce.setDate((SecurityLayer.checkDate(request.getParameter("date")).getTime()));
     }
        
        ce.setEpisodeID(episode.getID());
        
        for(Channel channel: episode.getChannels()){
            ce.setChannelID(channel.getID());
            getDataLayer().storeChannelEpisode(ce);
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
try {
            action_episode_save(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
   private boolean checkEpisodeInputData(HttpServletRequest request, HttpServletResponse response){
        return (request.getParameter("title") != null && request.getParameter("title").length() > 0
                && request.getParameter("description") != null && request.getParameter("description").length() > 0
                && request.getParameter("number") != null && request.getParameter("number").length() > 0
                && request.getParameter("season") != null && request.getParameter("season").length() > 0
                && request.getParameter("idseries") != null && request.getParameter("idseries").length() > 0
                && request.getParameterValues("channels") != null && request.getParameterValues("channels").length > 0);
    }
} 
