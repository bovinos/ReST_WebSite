package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Episode;
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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SeriesList extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutte le serie e le passa al template seriesList.ftl.html
    private void action_series_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("series", getDataLayer().getSeries());
        //Controllo la sessione e creo l'utente
        if (SecurityLayer.checkSession(request) != null) {
            String username = SecurityLayer.addSlashes((String) request.getSession().getAttribute("username"));
            request.setAttribute("sessionUsername", username);
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
        }
        request.setAttribute("genres", getDataLayer().getGenres());
        request.setAttribute("channels", getDataLayer().getChannels());
        result.activate("seriesList.ftl.html", request, response);
    }

    private void action_FilterAndOrder_series_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        List<Series> seriesList = getDataLayer().getSeries();

        //Filtro Serie per Nome
        if (request.getParameter("fn") != null && request.getParameter("fn").length() > 0) {
            List<Series> filteredSeries = new ArrayList();
            for (Series s : seriesList) {
                if (s.getName().equals(request.getParameter("fn"))) {
                    filteredSeries.add(s);
                }
            }
            seriesList = filteredSeries;
        }
        //Filtro Serie per Genere
        if (request.getParameterValues("fg") != null && request.getParameterValues("fg").length > 0) {
            List<Series> filteredSeries = new ArrayList();
            List<Genre> genresList = new ArrayList();
            for (String g : request.getParameterValues("fg")) {
                genresList.add(getDataLayer().getGenre(SecurityLayer.checkNumeric(g)));
            }
            for (Series s : seriesList) {
                for (Genre g : genresList) {
                    if (s.getGenres().contains(g)) {
                        filteredSeries.add(s);
                    }
                }
            }
            seriesList = filteredSeries;
        }

        //Filtro serie per stato
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
                    action_error(request, response, "Internal Error");
            }
            seriesList = filteredSeries;
        }
        //Filtro serie per canale
        if (request.getParameterValues("fc") != null && request.getParameterValues("fc").length > 0) {
            List<Series> filteredSeries = new ArrayList();
            List<Channel> channelList = new ArrayList();
            for (String c : request.getParameterValues("fc")) {
                channelList.add(getDataLayer().getChannel(SecurityLayer.checkNumeric(c)));
            }
            for (Series s : seriesList) {
                List<Episode> epList = s.getEpisodes();
                boolean[] ok = new boolean[epList.size()];
                for (int i = 0; i < epList.size(); i++) {
                    List<ChannelEpisode> ce = epList.get(i).getChannelEpisode();
                    for (Channel c : channelList) {
                        if (epList.get(i).getChannels().contains(c)) {
                            ok[i] = true;
                            break;
                        }
                    }
                }
                for (boolean b : ok) {
                    if (b == false) {
                        break;
                    }
                    filteredSeries.add(s);
                }
            }
            seriesList = filteredSeries;
        }
        //Ordinamento
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
                    action_error(request, response, "Internal Error");
            }
        }
        request.setAttribute("series", seriesList);
        request.setAttribute("genres", getDataLayer().getGenres());
        request.setAttribute("channels", getDataLayer().getChannels());
        result.activate("seriesList.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (request.getParameter("s") != null) {
            try {
                action_FilterAndOrder_series_list(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        } else {
            try {
                action_series_list(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
