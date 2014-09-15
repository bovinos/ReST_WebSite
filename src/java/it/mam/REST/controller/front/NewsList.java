package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.News;
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
public class NewsList extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutte le news e le passa al template newsList.ftl.html
    private void action_news_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("news", getDataLayer().getNews());
        request.setAttribute("series", getDataLayer().getSeries()); // per i filtri
        //Controllo che la sessione attuale sia ancora valida
        if (SecurityLayer.checkSession(request) != null) {
            String username = SecurityLayer.addSlashes((String) request.getSession().getAttribute("username"));
            request.setAttribute("sessionUsername", username);
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
        }
        result.activate("newsList.ftl.html", request, response);
    }

    private void action_FilterAndOrder_newslist(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("series", getDataLayer().getSeries()); // per i filtri
        //Controllo che la sessione attuale sia ancora valida
        User user = null;
        if (SecurityLayer.checkSession(request) != null) {
            String username = SecurityLayer.addSlashes((String) request.getSession().getAttribute("username"));
            request.setAttribute("sessionUsername", username);
            user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
        }

        List<News> newsList = getDataLayer().getNews();
        //Filtro News per Nome
        if (request.getParameter("fn") != null && !request.getParameter("fn").trim().isEmpty()) {
            List<News> filteredNews = new ArrayList();
            String name = ((request.getParameter("fn")).trim()).toLowerCase();
            for (News n : newsList) {
                if (((n.getTitle().toLowerCase()).contains(name))) {
                    filteredNews.add(n);
                }
            }
            newsList = filteredNews;
        }

        //Filtro per serie
        if (request.getParameter("fs") != null && SecurityLayer.checkNumeric(request.getParameter("fs")) != 0) {
            List<News> filteredNews = new ArrayList();
            Series selectedSeries = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("fs")));
            for (News n : newsList) {
                if (n.getSeries().contains(selectedSeries)) {
                    filteredNews.add(n);
                }
            }
            newsList = filteredNews;
        }

        //Filtro tra "Le Mie Serie"
        // se la sessione non è attiva, cioè non c'è un utente loggato, è impossibile filtrare per le serie preferite
        if (user != null && SecurityLayer.checkNumeric(request.getParameter("fmys")) == 1) {
            List<News> filteredNews = new ArrayList();
            List<Series> usersSeries = user.getSeries();
            for (News n : newsList) {
                for (Series s : usersSeries) {
                    if (n.getSeries().contains(s)) {
                        filteredNews.add(n);
                    }
                }
            }
            newsList = filteredNews;
        }

        //ordinamenti
        if (request.getParameter("o") != null) {
            int ordertype = SecurityLayer.checkNumeric(request.getParameter("o"));
            switch (ordertype) {
                case 1:
                    RESTSortLayer.sortNewsByNumberOfComments(newsList);
                    break;
                case 2:
                    RESTSortLayer.sortNewsByNumberOfLike(newsList);
                    break;
                default:
                    action_error(request, response, "Internal Error");
            }
        }

        request.setAttribute("news", newsList);
        result.activate("newsList.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (request.getParameter("s") != null) {
            try {
                action_FilterAndOrder_newslist(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        } else {
            try {
                action_news_list(request, response);
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
