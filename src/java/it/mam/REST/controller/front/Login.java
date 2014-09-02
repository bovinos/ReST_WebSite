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
public class Login extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    private void action_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username != null && username.length() > 0 && password != null && password.length() > 0) {
            username = SecurityLayer.addSlashes(username);
            password = Utility.stringToMD5(Utility.stringToMD5(password));
            user = getDataLayer().getUser(username, password);
            if (user == null) {
                // errore inserimento username o password
            }

            SecurityLayer.createSession(request, username, user.getID());
        } else {
            // errore uno dei campi è vuoto
        }
        // in realtà dovrei ridirigere alla pagina in cui ha fatto il login
        response.sendRedirect("ListaNews");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_login(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
