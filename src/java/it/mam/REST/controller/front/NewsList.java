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
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class NewsList extends RESTBaseController {

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    //Activates the newsList template
    private void action_news_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("where", "news");
        request.setAttribute("news", getDataLayer().getNews());
        request.setAttribute("series", getDataLayer().getSeries()); // for filters
        List<News> newsList =getDataLayer().getNews();
        //Page management
        int page; //page number 
        if(request.getParameter("page") != null) {
        page = SecurityLayer.checkNumeric(request.getParameter("page"));
        } else {
            page = 1;
        }
        request.setAttribute("currentPage", page);
        int newsPerPage = 10; // number of news per page
        int numberOfPages = Math.round(newsList.size()/newsPerPage) + 1; // total number of pages
        request.setAttribute("totalPages", numberOfPages);
        if(page == numberOfPages) {
            request.setAttribute("news", newsList.subList((page*newsPerPage)-newsPerPage, newsList.size()));
       } else if(newsList.isEmpty()){
             request.setAttribute("series", newsList);
        } else if (page > numberOfPages || page < 1) {
            action_error(request, response, "Riprova di nuovo!");
        } else {
            request.setAttribute("news", newsList.subList((page *newsPerPage)-newsPerPage, (page *newsPerPage)));
        }
        
        
        //User session checking
        if (SecurityLayer.checkSession(request) != null) {
            try {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                request.setAttribute("user", user);
                //Series Notification checking
                RESTSortLayer.checkNotifications(user, request, response);
                
            } catch (NumberFormatException ex) {
                //User id is not a number
            }
        }
        //Generates and inserts into request the 5 trendiest series
        request.setAttribute("trendiestSeries", RESTSortLayer.trendify(getDataLayer().getSeries()).subList(0, 5));
        result.activate("newsList.ftl.html", request, response);
    }

    private void action_FilterAndOrder_newslist(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("series", getDataLayer().getSeries()); // for filters

        //Generates and inserts into request the 5 trendiest series
        request.setAttribute("trendiestSeries", RESTSortLayer.trendify(getDataLayer().getSeries()).subList(0, 5));
        User user = null;
        //User session checking
        if (SecurityLayer.checkSession(request) != null) {
            try {
                user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                request.setAttribute("user", user);
            } catch (NumberFormatException ex) {
                //User id is not a number
            }
        }

        List<News> newsList = getDataLayer().getNews();
        //Filters News by Name
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

        //Filters News by Series
        if (request.getParameter("fs") != null && SecurityLayer.checkNumeric(request.getParameter("fs")) != 0) {
            List<News> filteredNews = new ArrayList();
            try {
                Series selectedSeries = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("fs")));
                for (News n : newsList) {
                    if (n.getSeries().contains(selectedSeries)) {
                        filteredNews.add(n);
                    }
                }
                newsList = filteredNews;
            } catch (NumberFormatException ex) {
                action_error(request, response, "Riprova di nuovo!");
                return;
            }
        }

        //Filters News for the ones related only to My Series (if user session is not active/valid it's impossible to apply this filter)
        try {
            if (user != null && request.getParameter("fmys") != null && SecurityLayer.checkNumeric(request.getParameter("fmys")) == 1) {
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
        } catch (NumberFormatException ex) {
            action_error(request, response, "Riprova di nuovo!");
            return;
        }

        // Filters News by Date
        if (request.getParameter("fd") != null && !request.getParameter("fd").trim().isEmpty()) {
            List<News> filteredNews = new ArrayList();
            try {
                Calendar c = SecurityLayer.checkDate(request.getParameter("fd").trim());
                for (News n : newsList) {
                    Calendar currentNewsDate = Calendar.getInstance();
                    currentNewsDate.setTime(n.getDate());
                    if (currentNewsDate.get(Calendar.YEAR) == c.get(Calendar.YEAR)
                            && currentNewsDate.get(Calendar.MONTH) == c.get(Calendar.MONTH)
                            && currentNewsDate.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
                        filteredNews.add(n);
                    }
                }
            } catch (NumberFormatException ex) {
                action_error(request, response, "Field Error");
                return;
            }
            newsList = filteredNews;
        }

        //Sortings
        if (request.getParameter("o") != null) {
            int ordertype = SecurityLayer.checkNumeric(request.getParameter("o"));
            switch (ordertype) {
                case 1:
                    RESTSortLayer.sortNewsByNumberOfComments(newsList);
                    break;
                case 2:
                    RESTSortLayer.sortNewsByNumberOfLike(newsList);
                    break;
                case 3:
                    RESTSortLayer.sortNewsByDate(newsList);
                    break;
                default:
                    action_error(request, response, "Riprova di nuovo!");
                    return;
            }
        }

        request.setAttribute("news", newsList);
        result.activate("newsList.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        List<Series> sl = getDataLayer().getHintSeries(getDataLayer().getUser(6));
        System.out.println("================================================================================");
        System.out.println(sl.size());
        System.out.println("================================================================================");
        for (Series s : sl) {
            System.out.println(s.getID());
            System.out.println("================================================================================");
        }
     try {
        if (request.getParameter("s") != null) {

                action_FilterAndOrder_newslist(request, response);
        } else {

                action_news_list(request, response);
        }
         } catch (IOException ex) {
                action_error(request, response, "Riprova di nuovo!");
            }
    }

    @Override
    public String getServletInfo() {
        return "This servlet activates the news list template to show the entire list of news. It also orders the list according the filters and the"
                + "sorting method the user chose";
    }

}
