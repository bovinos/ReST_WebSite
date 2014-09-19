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

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
            fail.activate(message, request, response);
        }

        // Activates the signUp template 
    private void action_activate_signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("signUp.ftl.html", request, response);
    }
    // Receives all the necessary data to register a user and, if everything's ok, saves him in the Database
    private void action_user_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getDataLayer().createUser();
        if (checkUserInputData(request, response)) {
            // Check if password and confirmpassword match
            if (!(request.getParameter("password").equals(request.getParameter("confirmPassword")))) {
            request.setAttribute("error", "Le due password inserite non corrispondono!");
                action_activate_signUp(request, response);
                return;
            }
            // Set all user's fields
            user.setUsername(request.getParameter("username"));
            user.setPassword(Utility.stringToMD5(Utility.stringToMD5(request.getParameter("password"))));
            user.setMail(request.getParameter("email"));

            getDataLayer().storeUser(RESTSecurityLayer.addSlashes(user));
            // Congratulations! You are logged in!
            request.setAttribute("success", "Congratulazioni! L'iscrizione è avvenuta con successo!");
                action_activate_signUp(request, response);
        } else {
            // Error: field empty
            request.setAttribute("error", "Uno dei campi è vuoto!");
            action_activate_signUp(request, response);
        }
    }
   

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
         try {
        if (request.getParameter("signUp") != null) {
            // User send the sign up data
                action_user_save(request, response);
        } else {
            // Show the sign up template
                action_activate_signUp(request, response);
        }
        } catch (IOException ex) {
                action_error(request, response, "Riprova di nuovo!");
            }
    }

    @Override
    public String getServletInfo() {
        return "This servlet activates the SignUp template, to allow anyone to sign up in this website. It is also used to save the user sign up data.";
    }

    // Checks if all the input fields have been filled
    private boolean checkUserInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("username") != null && request.getParameter("username").length() > 0
                && request.getParameter("password") != null && request.getParameter("password").length() > 0
                && request.getParameter("confirmPassword") != null && request.getParameter("confirmPassword").length() > 0
                && request.getParameter("email") != null && request.getParameter("email").length() > 0;
    }
}
