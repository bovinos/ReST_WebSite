package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.Utility;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SalvaUtente extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutte le news e le passa al template lista_news.ftl.html
    private void action_user_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confermaPassword = request.getParameter("confermaPassword");
        String email = request.getParameter("email");
        if (username != null && username.length() > 0 && password != null && password.length() > 0
                && confermaPassword != null && confermaPassword.length() > 0 && email != null && email.length() > 0) {

            // controlliamo che le password inserite corrispondano
            if (!password.equals(confermaPassword)) {
                // errore, le due password non corrispondono
            }
            username = SecurityLayer.addSlashes(username);
            password = Utility.stringToMD5(Utility.stringToMD5(password));
            email = SecurityLayer.addSlashes(email);
            user = getDataLayer().createUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setMail(email);
            getDataLayer().storeUser(user);
            // complimenti ti sei loggato
        } else {
            // errore uno dei campi è vuoto
        }
        response.sendRedirect("ListaNews");
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
    
}
