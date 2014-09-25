package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.RESTSortLayer;
import it.mam.REST.utility.RESTUtility;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SeriesList extends RESTBaseController {

    public static final int SERIES_PER_PAGE = 24;

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // Activates the seriesList template
    private void action_series_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("where", "series");
        List<Series> seriesList = getDataLayer().getSeries();
        //Start Page Management ===========================================================================
        RESTSortLayer.trendify(seriesList);
        int page; //page number 
        if (request.getParameter("page") != null) {
            page = SecurityLayer.checkNumeric(request.getParameter("page"));
        } else {
            page = 1;
        }
        request.setAttribute("currentPage", page);
        int numberOfPages = (int) Math.ceil((double) seriesList.size() / SeriesList.SERIES_PER_PAGE); // total number of pages
        request.setAttribute("totalPages", numberOfPages);
        if (page == numberOfPages) {
            request.setAttribute("series", seriesList.subList((page * SeriesList.SERIES_PER_PAGE) - SeriesList.SERIES_PER_PAGE, seriesList.size()));
        } else if (seriesList.isEmpty()) {
            request.setAttribute("series", seriesList);
        } else if (page > numberOfPages || page < 1) {
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in action_series_list in SeriesList.java: il numero di pagina corrente è superiore al numero totale di pagine o è minore di 1");
            return;
        } else {
            request.setAttribute("series", seriesList.subList((page * SeriesList.SERIES_PER_PAGE) - SeriesList.SERIES_PER_PAGE, (page * SeriesList.SERIES_PER_PAGE)));
        }
        // End Page Management =======================================================================

        // User session checking
        try {
            if (SecurityLayer.checkSession(request) != null) {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                request.setAttribute("user", user);
                RESTUtility.checkNotifications(user, request, response);
            }
        } catch (NumberFormatException ex) {
            //User id is not a number
        }
        request.setAttribute("genres", getDataLayer().getGenres());
        request.setAttribute("channels", getDataLayer().getChannels());
        //Generate and insert into request the 5 trendiest series
        request.setAttribute("trendiestSeries", RESTSortLayer.trendify(getDataLayer().getSeries()).subList(0, 5));
        result.activate("seriesList.ftl.html", request, response);
    }

    // Activates the seriesList template with the recommended series
    private void action_hint_series(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("where", "series");
        // User session checking
        try {
            if (SecurityLayer.checkSession(request) != null) {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                request.setAttribute("user", user);
                request.setAttribute("series", getDataLayer().getHintSeries(user));
                RESTUtility.checkNotifications(user, request, response);
            } //else nothing, this list can be seen without being logged in
        } catch (NumberFormatException ex) {
            //User id is not a number
        }
        request.setAttribute("genres", getDataLayer().getGenres());
        request.setAttribute("channels", getDataLayer().getChannels());
        //Generate and insert into request the 5 trendiest series
        request.setAttribute("trendiestSeries", RESTSortLayer.trendify(getDataLayer().getSeries()).subList(0, 5));
        result.activate("seriesList.ftl.html", request, response);
    }

    //Filters and orders the series list according to user's requests
    private void action_FilterAndOrder_series_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        List<Series> seriesList = getDataLayer().getSeries();
        if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString()));
            request.setAttribute("user", user);
        }

        //Filters series by Channel. This MUST be the first filter, because the others needs the list returned by this one.
        try {
            if (request.getParameter("fc") != null && SecurityLayer.checkNumeric(request.getParameter("fc")) != 0) {
                List<Series> filteredSeries = new ArrayList();
                Calendar calendar = Calendar.getInstance();
                Channel c = getDataLayer().getChannel(SecurityLayer.checkNumeric(request.getParameter("fc")));
                if (c == null) {
                    action_error(request, response, "Riprova di nuovo!");
                    System.err.println("Errore in SeriesList.java, nel metodo action_FilterAndOrder_series_list: l'ID del canale passato non corrisponde a nessun canale del Database");
                    return;
                }
                for (ChannelEpisode ce : c.getChannelEpisode()) {
                    if (ce.getDate().getTime() >= calendar.getTimeInMillis() && !(filteredSeries.contains(ce.getEpisode().getSeries()))) {
                        filteredSeries.add(ce.getEpisode().getSeries());
                    }
                }
                seriesList = filteredSeries;
            }

            //Filters series by Name
            if (request.getParameter("fn") != null && request.getParameter("fn").length() > 0) {
                List<Series> filteredSeries = new ArrayList();
                String name = ((request.getParameter("fn")).trim()).toLowerCase();
                for (Series s : seriesList) {
                    if (((s.getName().toLowerCase()).contains(name))) {
                        filteredSeries.add(s);
                    }
                }
                seriesList = filteredSeries;
            }
            //Filters series by Genre
            if (request.getParameterValues("fg") != null && request.getParameterValues("fg").length > 0) {
                List<Series> filteredSeries = new ArrayList();
                List<Genre> genresList = new ArrayList();
                Genre gr;
                for (String g : request.getParameterValues("fg")) {
                    gr = getDataLayer().getGenre(SecurityLayer.checkNumeric(g));
                    if (gr == null) {
                        action_error(request, response, "Riprova di nuovo!");
                        System.err.println("Errore in SeriesList.java, nel metodo action_FilterAndOrder_series_list: gli ID dei generi passati non corrispondono a nessun genere sul Database");
                        return;
                    }
                    genresList.add(gr);
                }
                for (Series s : seriesList) {
                    if (s.getGenres().containsAll(genresList)) {
                        filteredSeries.add(s);
                    }
                }
                seriesList = filteredSeries;
            }

            //Filters series by status
            if (request.getParameter("fs") != null && request.getParameter("fs").length() > 0) {
                List<Series> filteredSeries = new ArrayList();
                int status = SecurityLayer.checkNumeric(request.getParameter("fs"));
                switch (status) {
                    case 1:
                        for (Series s : seriesList) {
                            if (s.getState().equals(Series.ONGOING)) {
                                filteredSeries.add(s);
                            }
                        }
                        break;
                    case 2:
                        for (Series s : seriesList) {
                            if (s.getState().equals(Series.COMPLETE)) {
                                filteredSeries.add(s);
                            }
                        }
                        break;
                    default:
                        //The status parameter is not 1 (ongoing) nor 2 (complete)
                        action_error(request, response, "Riprova di nuovo!");
                        System.err.println("Errore nel filtro per status di SeriesList.java, nel metodo action_FilterAndOrder_series_list: il valore di status per cui filtrare non valeva nè 1 (in corso) nè 2 (completa)");
                        return;
                }
                seriesList = filteredSeries;
            }

            //Sortings
            if (request.getParameter("o") != null && request.getParameter("o").length() > 0) {
                int ordertype = SecurityLayer.checkNumeric(request.getParameter("o"));
                switch (ordertype) {
                    case 1:
                        RESTSortLayer.sortSeriesByPopularity(seriesList);
                        break;
                    case 2:
                        RESTSortLayer.sortSeriesByRating(seriesList);
                        break;
                    case 3:
                        RESTSortLayer.sortSeriesByYear(seriesList);
                        break;
                    case 4:
                        RESTSortLayer.sortSeriesByName(seriesList);
                        break;
                    default:
                        //The sorting-type parameter is not 1,2,3 or 4, so no sorting type has been chosen
                        action_error(request, response, "Riprova di nuovo!");
                        System.err.println("Errore nell'ordinamento di SeriesList.java, nel metodo action_FilterAndOrder_series_list: il valore che stabilisce il tipo di ordinamento non era 1,2,3 o 4");
                        return;
                }
            }
            request.setAttribute("series", seriesList);
            request.setAttribute("genres", getDataLayer().getGenres());
            request.setAttribute("channels", getDataLayer().getChannels());

            //Generates and inserts in the request the 5 trendiest series
            request.setAttribute("trendiestSeries", RESTSortLayer.trendify(getDataLayer().getSeries()).subList(0, 5));
            result.activate("seriesList.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore in SeriesList.java, nel metodo action_FilterAndOrder_series_list: NumberFormatException");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("s") != null) {
                action_FilterAndOrder_series_list(request, response);
            } else {
                if (request.getParameter("sh") != null) {
                    action_hint_series(request, response);
                } else {
                    action_series_list(request, response);
                }

            }
        } catch (IOException ex) {
            action_error(request, response, "Riprova di nuovo!");
            System.err.println("Errore nella Process Request di SeriesList.java: IOException");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet activates the series list template to show the entire list of series. It also orders the list according the filters and the"
                + "sorting method the user chose";
    }

}
