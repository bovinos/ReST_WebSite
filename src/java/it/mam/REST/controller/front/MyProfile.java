package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
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
public class MyProfile extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // Attiva il template del palinsesto
    private void action_activate_ProfileUserBroadcastProgramming(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        String username = SecurityLayer.addSlashes((String) request.getSession().getAttribute("username"));
        request.setAttribute("sessionUsername", username);
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", user);
        request.setAttribute("userProfileContent_tpl", "userBroadcastProgramming.ftl.html");
        result.activate("userProfile/userProfileOutline.ftl.html", request, response);
    }

    //Attiva il template delle cerchie
    private void action_activate_ProfileUserCircles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        String username = SecurityLayer.addSlashes((String) request.getSession().getAttribute("username"));
        request.setAttribute("sessionUsername", username);
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", user);
        request.setAttribute("userProfileContent_tpl", "userCircles.ftl.html");
        result.activate("userProfile/userProfileOutline.ftl.html", request, response);
    }

    // attiva il template "le mie serie"
    private void action_activate_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        String username = SecurityLayer.addSlashes((String) request.getSession().getAttribute("username"));
        request.setAttribute("sessionUsername", username);
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", user);
        request.setAttribute("userProfileContent_tpl", "userSeries.ftl.html");
        result.activate("userProfile/userProfileOutline.ftl.html", request, response);
    }

    private void action_rating_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getDataLayer().getUser((int) request.getSession().getAttribute("userid"));
        Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("s")));
        UserSeries us = getDataLayer().getUserSeries(user, series);
        if (!(request.getParameter("r").equals(us.getRating()))) {
            int rating = SecurityLayer.checkNumeric(request.getParameter("r"));
            switch (rating) {
                case 1:
                    us.setRating(UserSeries.ONE);
                    break;
                case 2:
                    us.setRating(UserSeries.TWO);
                    break;
                case 3:
                    us.setRating(UserSeries.THREE);
                    break;
                case 4:
                    us.setRating(UserSeries.FOUR);
                    break;
                case 5:
                    us.setRating(UserSeries.FIVE);
                    break;
                default:
                    action_error(request, response, "Internal Error");
            }
            getDataLayer().storeUserSeries(RESTSecurityLayer.addSlashes(us));
            // con questa sendRedirect il caricamento della nuova pagina andrà a finire sempre sulla serie in cui l'utente ha
            // appena modificato la valutazione, in questo modo ritroverà la pagina esattamente dov'era prima xD
            response.sendRedirect("ProfiloPersonale?sezione=1#s" + series.getID());

        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        int section = SecurityLayer.checkNumeric(request.getParameter("sezione"));
        switch (section) {
            case 1:
                if (request.getParameter("s") != null && request.getParameter("r") != null) {
                    try {
                        action_rating_ProfileUserSeries(request, response);
                    } catch (IOException ex) {
                        action_error(request, response, ex.getMessage());
                    }
                } else {
                    try {
                        action_activate_ProfileUserSeries(request, response);
                    } catch (IOException ex) {
                        action_error(request, response, ex.getMessage());
                    }
                }
                break;
            case 2:
                try {
                    action_activate_ProfileUserCircles(request, response);
                } catch (IOException ex) {
                    action_error(request, response, ex.getMessage());
                }
                break;
            case 3:
                try {
                    action_activate_ProfileUserBroadcastProgramming(request, response);
                } catch (IOException ex) {
                    action_error(request, response, ex.getMessage());
                }
                break;
            default:
                action_error(request, response, "The requested resource is not available");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
