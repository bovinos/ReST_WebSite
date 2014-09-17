package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.Utility;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SignUp extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutti i dati necessari a far registrare un utente e se è tutto corretto, lo salva sul DB
    private void action_user_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getDataLayer().createUser();
        if (checkUserInputData(request, response)) {
            // controlliamo che le password inserite corrispondano
            if (!(request.getParameter("password").equals(request.getParameter("confirmPassword")))) {
                action_error(request, response, "Le due password inserite non corrispondono!");
                return;
            }
            user.setUsername(request.getParameter("username"));
            user.setPassword(Utility.stringToMD5(Utility.stringToMD5(request.getParameter("password"))));
            user.setMail(request.getParameter("email"));

            getDataLayer().storeUser(RESTSecurityLayer.addSlashes(user));
            // complimenti ti sei loggato
            response.sendRedirect("ListaNews");
        } else {
            // errore uno dei campi è vuoto
            action_error(request, response, "Uno dei campi è vuoto");
        }
    }

    private void action_activate_signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("signUp.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (request.getParameter("signUp") != null) {
            // l'utente ha cliccato su "invia" nella form della registrazione
            try {
                action_user_save(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        } else {
            // devo ridirigere alla pagina di registrazione
            try {
                action_activate_signUp(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private boolean checkUserInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("username") != null && request.getParameter("username").length() > 0
                && request.getParameter("password") != null && request.getParameter("password").length() > 0
                && request.getParameter("confirmPassword") != null && request.getParameter("confirmPassword").length() > 0
                && request.getParameter("email") != null && request.getParameter("email").length() > 0;
    }
}
