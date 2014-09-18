package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class ManageNews extends RESTBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    private void action_goToManageNews(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        /**
         * < controllare anche se è effettivamente un admin xD >
         */
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        try {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("backContent_tpl", "insertNews.ftl.html");
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "boh xD");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_goToManageNews(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
