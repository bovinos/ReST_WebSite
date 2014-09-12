package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.Utility;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SaveUser extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutti i dati necessari a far registrare un utente e se è tutto corretto, lo salva sul DB
    private void action_user_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getDataLayer().createUser();
       if(checkUserInputData(request, response)){
            // controlliamo che le password inserite corrispondano
            if (!(request.getParameter("password").equals(request.getParameter("confirmpassword")))) {
                action_error(request, response, "Le due password inserite non corrispondono!");
            }
            user.setUsername(request.getParameter("username"));
            user.setPassword(Utility.stringToMD5(Utility.stringToMD5(request.getParameter("password"))));
            user.setMail(request.getParameter("mail"));
            
            getDataLayer().storeUser(RESTSecurityLayer.addSlashes(user));
            // complimenti ti sei loggato
        } else {
            // errore uno dei campi è vuoto
        }
        response.sendRedirect("NewsList");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_user_save(request, response);
        } catch (IOException ex) {
            action_error(request, response, null);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    private boolean checkUserInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("username") != null && request.getParameter("username").length() > 0
                && request.getParameter("password") != null && request.getParameter("password").length() > 0
                && request.getParameter("confirmpassword") != null && request.getParameter("confirmpassword").length() > 0
                && request.getParameter("mail") != null && request.getParameter("mail").length() > 0;
    }
}
