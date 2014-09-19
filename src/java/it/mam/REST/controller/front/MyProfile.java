package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
import it.mam.REST.utility.RESTSortLayer;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class MyProfile extends RESTBaseController {

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // Activates the user broadcast programming template
    private void action_activate_ProfileUserBroadcastProgramming(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "profile");
        if (SecurityLayer.checkSession(request) != null) {
            result.activate("logIn.ftl.html", request, response);
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("userProfileContent_tpl", "userBroadcastProgramming.ftl.html");
            Calendar iterationCalendar = Calendar.getInstance();
            Calendar episodeCalendar = Calendar.getInstance();
            List<List<ChannelEpisode>> schedule = new ArrayList();
            List<Date> days = new ArrayList();
            List<ChannelEpisode> ceList = getDataLayer().getChannelEpisode();
            for (int i = 0; i < 7; i++) {
                List<ChannelEpisode> ceResult = new ArrayList();
                iterationCalendar.setTimeInMillis(new Date().getTime() + (i * RESTSortLayer.DAY_IN_MILLISECONDS));
                days.add(iterationCalendar.getTime());
                for (ChannelEpisode ce : ceList) {
                    episodeCalendar.setTimeInMillis(ce.getDate().getTime());
                    if (episodeCalendar.get(Calendar.DAY_OF_MONTH) == iterationCalendar.get(Calendar.DAY_OF_MONTH)
                            && episodeCalendar.get(Calendar.MONTH) == iterationCalendar.get(Calendar.MONTH)
                            && episodeCalendar.get(Calendar.YEAR) == iterationCalendar.get(Calendar.YEAR)) {
                        ceResult.add(ce);
                    }
                }
                schedule.add(ceResult);
            }
            request.setAttribute("schedule", schedule);
            request.setAttribute("days", days);
            result.activate("userProfile/userProfileOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Activates "My Series" template
    private void action_activate_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) != null) {
            result.activate("logIn.ftl.html", request, response);

            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("userProfileContent_tpl", "userSeries.ftl.html");
            request.setAttribute("where", "profile");
            result.activate("userProfile/userProfileOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Allow to rate a series 
    private void action_rating_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString()));
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
                System.err.println("PRIMA DI STORE USER SERIES");
                getDataLayer().storeUserSeries(RESTSecurityLayer.addSlashes(us));
                // con questa sendRedirect il caricamento della nuova pagina andrà a finire sempre sulla serie in cui l'utente ha
                // appena modificato la valutazione, in questo modo ritroverà la pagina esattamente dov'era prima xD
                response.sendRedirect("ProfiloPersonale?sezione=1#s" + series.getID());

            }
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id or series id or rating is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Allow to delete a series from favourites
    private void action_delete_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString()));
            Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("d")));
            getDataLayer().removeUserSeries(getDataLayer().getUserSeries(user, series));
            response.sendRedirect("ProfiloPersonale?sezione=1");
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id or series id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            int section = SecurityLayer.checkNumeric(request.getParameter("sezione"));
            switch (section) {
                case 1:
                    if (request.getParameter("r") != null && request.getParameter("s") != null) {

                            action_rating_ProfileUserSeries(request, response);
                    } else if (request.getParameter("d") != null) {

                            action_delete_ProfileUserSeries(request, response);
                    } else {
                            action_activate_ProfileUserSeries(request, response);
                    }
                    break;
                case 2:
                        action_activate_ProfileUserBroadcastProgramming(request, response);
                    break;
                default:
                    action_error(request, response, "The requested resource is not available");
            }
        } catch (IOException ex) {
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet contains some things related to user profile. It shows user series, broadcast programming and allows to rate or delete a favourite series";
    }

}
