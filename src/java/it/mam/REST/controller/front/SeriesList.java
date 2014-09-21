package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.RESTSortLayer;
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
        List<Series> seriesList =getDataLayer().getSeries();
        //Page management
        int page; //page number 
        if(request.getParameter("page") != null) {
        page = SecurityLayer.checkNumeric(request.getParameter("page"));
        } else {
            page = 1;
        }
        request.setAttribute("currentPage", page);
        int seriesPerPage = 10; // number of series per page
        int numberOfPages = Math.round(seriesList.size()/seriesPerPage) + 1; // total number of pages
        request.setAttribute("totalPages", numberOfPages);
        if(page == numberOfPages) {
            request.setAttribute("series", seriesList.subList((page*seriesPerPage)-seriesPerPage, seriesList.size()));
        } else if (page > numberOfPages || page < 1) {
            action_error(request, response, "Riprova di nuovo!");
        } else {
            request.setAttribute("series", seriesList.subList((page *seriesPerPage)-seriesPerPage, (page *seriesPerPage)));
        }
        // User session checking
        try{
        if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            
        }
         } catch (NumberFormatException ex) {
             //User id is not a number
        }
        request.setAttribute("genres", getDataLayer().getGenres());
        request.setAttribute("channels", getDataLayer().getChannels());
        //genero e inserisco nella request le 5 serie più trendy
        request.setAttribute("trendiestSeries", RESTSortLayer.trendify(getDataLayer().getSeries()).subList(0, 5));
        result.activate("seriesList.ftl.html", request, response);
    }
    
    // Activates the seriesList template with the recommended series
    private void action_hint_series(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("where", "series");
        // User session checking
        try{
        if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("series", getDataLayer().getHintSeries(user));
        } //else nothing, this list can be seen without beeing logged in
         } catch (NumberFormatException ex) {
             //User id is not a number
        }
        request.setAttribute("genres", getDataLayer().getGenres());
        request.setAttribute("channels", getDataLayer().getChannels());
        //genero e inserisco nella request le 5 serie più trendy
        request.setAttribute("trendiestSeries", RESTSortLayer.trendify(getDataLayer().getSeries()).subList(0, 5));
        result.activate("seriesList.ftl.html", request, response);
    }

    //Filters and orders the series list according to user's requests
    private void action_FilterAndOrder_series_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        List<Series> seriesList = getDataLayer().getSeries();
        if(SecurityLayer.checkSession(request) != null){
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString()));
            request.setAttribute("user", user);
        }

        //Filters series by Channel. This MUST be the first filter, because the others needs the list returned by this one.
        try {
            if (request.getParameter("fc") != null && SecurityLayer.checkNumeric(request.getParameter("fc")) != 0) {
                List<Series> filteredSeries = new ArrayList();
                Calendar calendar = Calendar.getInstance();
                Channel c = getDataLayer().getChannel(SecurityLayer.checkNumeric(request.getParameter("fc")));
                for (ChannelEpisode ce : getDataLayer().getChannelEpisode()) {
                    if (ce.getDate().after(calendar.getTime()) && ce.getChannel().equals(c) && !(filteredSeries.contains(ce.getEpisode().getSeries()))) {
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
            for (String g : request.getParameterValues("fg")) {

                    genresList.add(getDataLayer().getGenre(SecurityLayer.checkNumeric(g)));
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
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
        if (request.getParameter("s") != null) {
            action_FilterAndOrder_series_list(request, response);
        } else {
            if(request.getParameter("sh") != null){
            action_hint_series(request, response);
            } else{
             action_series_list(request, response);
            }

        }
            } catch (IOException ex) {
                action_error(request, response, "Riprova di nuovo!");
            }
    }

    @Override
    public String getServletInfo() {
        return "This servlet activates the series list template to show the entire list of series. It also orders the list according the filters and the"
                + "sorting method the user chose";
    }

}
