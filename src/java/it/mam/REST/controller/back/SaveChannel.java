
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Channel;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class SaveChannel extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // controlla l'inserimento corretto di tutti i dati di una news e la salva sul DB
    private void action_channel_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Channel channel = getDataLayer().createChannel();
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkChannelInputData(request, response)){
            //aggiungo gli slash prima di salvare su DB per evitare valori pericolosi
            channel.setName(request.getParameter("name"));
            channel.setType(request.getParameter("type"));
            try {
            channel.setNumber(SecurityLayer.checkNumeric(request.getParameter("number")));
             } catch (NumberFormatException e) {
            action_error(request, response, "Field Error");
        }
          } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
            
        getDataLayer().storeChannel(RESTSecurityLayer.addSlashesChannel(channel));
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
try {
            action_channel_save(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
   private boolean checkChannelInputData(HttpServletRequest request, HttpServletResponse response){
        return (request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("type") != null && request.getParameter("type").length() > 0)
                && request.getParameter("number") != null && request.getParameter("number").length() > 0;
    }
}
