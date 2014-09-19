
package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class LogOut extends RESTBaseController {

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }
    // Logs a user out
    private void action_logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       SecurityLayer.disposeSession(request);
        // in realtà dovrei ridirigere alla pagina in cui ha fatto il logout
        response.sendRedirect("ListaNews");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_logout(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet logs a user out";
    }
}
