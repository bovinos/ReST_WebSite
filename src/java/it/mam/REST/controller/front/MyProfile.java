package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Episode;
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
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            request.setAttribute("where", "profile");
            if (SecurityLayer.checkSession(request) != null) {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                request.setAttribute("user", user);
                RESTSortLayer.checkNotifications(user, request, response);
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
                                && episodeCalendar.get(Calendar.YEAR) == iterationCalendar.get(Calendar.YEAR)
                                && (episodeCalendar.get(Calendar.HOUR) * RESTSortLayer.HOUR_IN_MILLISECONDS
                                + episodeCalendar.get(Calendar.MINUTE) * RESTSortLayer.MINUTE_IN_MILLISECONDS
                                + episodeCalendar.get(Calendar.SECOND) * RESTSortLayer.SECOND_IN_MILLISECONDS)
                                > (iterationCalendar.get(Calendar.HOUR) * RESTSortLayer.HOUR_IN_MILLISECONDS
                                + iterationCalendar.get(Calendar.MINUTE) * RESTSortLayer.MINUTE_IN_MILLISECONDS
                                + iterationCalendar.get(Calendar.SECOND) * RESTSortLayer.SECOND_IN_MILLISECONDS)
                                && (getDataLayer().getUserSeries(user, ce.getEpisode().getSeries()).getSeason()) == ce.getEpisode().getSeason()
                                && (getDataLayer().getUserSeries(user, ce.getEpisode().getSeries()).getEpisode() + 1) == ce.getEpisode().getNumber()) {
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
            System.err.println("Errore in MyProfile.java, nel metodo action_activate_ProfileUserBroadcastProgramming: NumberFormatException");
        }
    }

    // Activates "My Series" template
    private void action_activate_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {

                User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                request.setAttribute("user", user);
                request.setAttribute("seriesHint", getDataLayer().getHintSeries(user));
                request.setAttribute("userProfileContent_tpl", "userSeries.ftl.html");
                request.setAttribute("where", "profile");

                //Series Notification checking (Different from RESTSortLayer.checkNotifications(user, request, response); because here we need the list of series!)
                if (user.getNotificationStatus()) {
                    boolean trovato;
                    List<Series> SeriesToNotify = new ArrayList();
                    Date now = new Date();
                    for (UserSeries us : user.getUserSeries()) {
                        if (us.getAnticipationNotification() == null) {
                            continue;
                        }
                        Series s = us.getSeries();
                        trovato = false;
                        for (Episode e : s.getEpisodes()) {
                            if (trovato) {
                                break;
                            }
                            for (ChannelEpisode ce : e.getChannelEpisode()) {
                                System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                                System.err.println(now);
                                System.err.println(ce.getDate());
                                System.err.println(us.getAnticipationNotification());
                                if (us.getEpisode() + 1 == e.getNumber() && us.getSeason() == e.getSeason()
                                        && (now.getTime() >= ce.getDate().getTime() - us.getAnticipationNotification().getTime())
                                        && now.before(ce.getDate())) {
                                    SeriesToNotify.add(s);
                                    trovato = true;
                                }
                            }
                        }
                    }
                    System.err.println(SeriesToNotify.size());
                    request.setAttribute("notifySeries", SeriesToNotify);
                }
                result.activate("userProfile/userProfileOutline.ftl.html", request, response);
            } else {
                //User session is no longer valid
                request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
                result.activate("logIn.ftl.html", request, response);
            }
        } catch (NumberFormatException ex) {
            //User id is not a number
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in MyProfile.java, nel metodo action_activate_ProfileUserSeries: NumberFormatException");
        }
    }

    // Allows to rate a series 
    private void action_rating_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString()));
                if (!(checkRatingInputData(request, response))) {
                    action_error(request, response, "Riprova di nuovo!");
                    System.err.println("Errore in MyProfile.java, nel metodo action_rating_ProfileUserSeries: l'ID della serie non è stato inviato");
                    return;
                }
                Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("s")));
                if (series == null) {
                    action_error(request, response, "Riprova di nuovo!");
                    System.err.println("Errore in MyProfile.java, nel metodo action_rating_ProfileUserSeries: l'ID della serie ricevuto non corrisponde a nessuna serie nel Database");
                    return;
                }
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
                            action_error(request, response, "Riprova di nuovo!");
                            System.err.println("Errore in MyProfile.java, nel metodo action_rating_ProfileUserSeries: il valore del rating ricevuto non è compreso fra 1 e 5");
                            return;
                    }
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
            System.err.println("Errore in MyProfile.java, nel metodo action_rating_ProfileUserSeries: NumberFormatException");
        }
    }

    // Allows to set the time before notifications and the last episode seen
    private void action_setNotification_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString()));
                if (!(checkSetNotificationInputData(request, response))) {
                    action_error(request, response, "Riprova di nuovo!");
                    System.err.println("Errore in MyProfile.java, nel metodo action_rating_ProfileUserSeries: l'ID della serie non è stato inviato");
                    return;
                }
                //Series checking
                Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("series")));
                if (series == null) {
                    action_error(request, response, "Riprova di nuovo!");
                    System.err.println("Errore in MyProfile.java, nel metodo action_setNotification_ProfileUserSeries: l'ID della serie ricevuto non corrisponde a nessuna serie nel Database");
                    return;
                }
                //UserSeries checking
                UserSeries us = getDataLayer().getUserSeries(user, series);
                if (us == null) {
                    action_error(request, response, "Riprova di nuovo!");
                    System.err.println("Errore in MyProfile.java, nel metodo action_setNotification_ProfileUserSeries: l'ID della serie e dello user ricevuti non sono associati nel Database");
                    return;
                }
                if (request.getParameter("a") != null) {
                    if (request.getParameter("an") != null && request.getParameter("an").length() > 0) {
                        us.setAnticipationNotification(new Date(SecurityLayer.checkTime(request.getParameter("an"))));
                    } else {
                        request.setAttribute("error", "Non hai inserito l'anticipo della notifica!");
                        action_activate_ProfileUserSeries(request, response);
                        return;
                    }
                } else {
                    us.setAnticipationNotification(null);
                }
                if (SecurityLayer.checkNumeric(request.getParameter("lastEpisodeSeen")) == 0) {
                    us.setEpisode(0);
                    us.setSeason(1);
                } else {
                    Episode e = getDataLayer().getEpisode(SecurityLayer.checkNumeric(request.getParameter("lastEpisodeSeen")));
                    if (e == null) {
                        action_error(request, response, "Riprova di nuovo!");
                        System.err.println("Errore in MyProfile.java, nel metodo action_setNotification_ProfileUserSeries: l'ID dell'episodio ricevuto non corrisponde a nessun episodio nel Database");
                        return;
                    }
                    us.setSeason(e.getSeason());
                    us.setEpisode(e.getNumber());
                }
                getDataLayer().storeUserSeries(RESTSecurityLayer.addSlashes(us));
                // con questa sendRedirect il caricamento della nuova pagina andrà a finire sempre sulla serie in cui l'utente ha
                // appena modificato la valutazione, in questo modo ritroverà la pagina esattamente dov'era prima xD
                response.sendRedirect("ProfiloPersonale?sezione=1#s" + series.getID());

            } else {
                //User session is no longer valid
                request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
                result.activate("logIn.ftl.html", request, response);
            }
        } catch (NumberFormatException ex) {
            //User id or series id or episode is not a number
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in MyProfile.java, nel metodo action_rating_ProfileUserSeries: NumberFormatException");
        }
    }

    // Allow to delete a series from favourites
    private void action_delete_ProfileUserSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString()));
                Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("d")));
                if (series == null) {
                    action_error(request, response, "Riprova di nuovo!");
                    System.err.println("Errore in MyProfile.java, nel metodo action_delete_ProfileUserSeries: l'ID della serie ricevuto non corrisponde a nessuna serie nel Database");
                    return;
                }
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
            System.err.println("Errore in MyProfile.java, nel metodo action_delete_ProfileUserSeries: NumberFormatException");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (request.getParameter("sezione") == null) {
                action_error(request, response, "Riprova di nuovo!");
                System.err.println("Errore nella Process Request di MyProfile.java: il parametro sezione è nullo");
                return;
            }
            int section = SecurityLayer.checkNumeric(request.getParameter("sezione"));
            switch (section) {
                case 1:
                    request.setAttribute("currentSection", section);
                    if (request.getParameter("r") != null && request.getParameter("s") != null) {

                        action_rating_ProfileUserSeries(request, response);
                    } else if (request.getParameter("d") != null) {

                        action_delete_ProfileUserSeries(request, response);
                    } else if (request.getParameter("ems") != null) {

                        action_setNotification_ProfileUserSeries(request, response);
                    } else {
                        action_activate_ProfileUserSeries(request, response);
                    }
                    break;
                case 2:
                    request.setAttribute("currentSection", section);
                    action_activate_ProfileUserBroadcastProgramming(request, response);
                    break;
                default:
                    action_error(request, response, "Riprova di nuovo!");
                    System.err.println("Errore nella Process Request di MyProfile.java: il parametro sezione non vale nè 1 nè 2");
            }
        } catch (IOException | NumberFormatException ex) {
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore nella Process Request di MyProfile.java: IOException o NumberFormatException");
        }
    }

    private boolean checkRatingInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("s") != null && request.getParameter("s").length() > 0
                && request.getParameter("r") != null && request.getParameter("r").length() > 0;
    }

    private boolean checkSetNotificationInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("series") != null && request.getParameter("series").length() > 0
                && request.getParameter("lastEpisodeSeen") != null && request.getParameter("lastEpisodeSeen").length() > 0;
    }

    @Override
    public String getServletInfo() {
        return "This servlet contains some things related to user profile. It shows user series, broadcast programming and allows to rate or delete a favourite series";
    }

}
