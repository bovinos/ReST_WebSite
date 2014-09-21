package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Season;
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
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SeriesCard extends RESTBaseController {

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    //Activates the series card template
    private void action_series_info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "series");
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        try {
            Series s = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("id")));
            request.setAttribute("series", s);
            request.setAttribute("seriesRating", RESTSortLayer.getMediumRating(s));
            //Creates a list of "season" objects which contain the season number and a list of the episode that belongs to that season
            List<Season> seasonList = new ArrayList();
            List<Episode> episodeList = s.getEpisodes();
            Season sn = null;
            for (Episode e : episodeList) {
                if (sn == null || sn.getNumber() != e.getSeason()) {
                    sn = new Season(e.getSeason(), new ArrayList());
                    seasonList.add(sn);
                }
                sn.getEpisodes().add(e);
            }
            request.setAttribute("seasons", seasonList);
            
            //Page management
            int page; //page number 
            if(request.getParameter("page") != null) {
            page = SecurityLayer.checkNumeric(request.getParameter("page"));
            } else {
            page = 1;
            }
            List<Comment> commentsList = s.getComments();
            Collections.reverse(commentsList);
            request.setAttribute("currentPage", page);
            int commentsPerPage = 10; // number of comments per page
            int numberOfPages = (int) Math.ceil((double)commentsList.size()/commentsPerPage); // total number of pages
            request.setAttribute("totalPages", numberOfPages);
             if(page == numberOfPages) {
            request.setAttribute("comments", commentsList);
            request.setAttribute("previousLastCommentIndex", (page-1)*commentsPerPage);
            } else if (page > numberOfPages || page < 1) {
            action_error(request, response, "Riprova di nuovo!");
            } else {
            request.setAttribute("comments", commentsList.subList(0, (page *commentsPerPage)));
            request.setAttribute("previousLastCommentIndex", (page-1)*commentsPerPage);
            }
            
            // User session checking
            if (SecurityLayer.checkSession(request) != null) {
                try {
                    User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                    request.setAttribute("user", user);

                    //Checking if the series is already in the user's favourites
                    boolean favourite;
                    UserSeries us = getDataLayer().getUserSeries(user, s);
                    favourite = (us != null);
                    request.setAttribute("favourite", favourite);
                    RESTSortLayer.checkNotifications(user, request, response);      
                } catch (NumberFormatException ex) {
                    //User id is not a number
                }
            }
            
        } catch (NumberFormatException ex) {
            //Series id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
        result.activate("seriesCard.ftl.html", request, response);
    }

    //Adds a series in the user's favourites
    private void action_addSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            //User session checking
            if (SecurityLayer.checkSession(request) != null) {

                User user = getDataLayer().getUser((SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString())));
                Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("a")));
                UserSeries us = getDataLayer().createUserSeries();
                us.setUser(user);
                us.setSeries(series);
                getDataLayer().storeUserSeries(RESTSecurityLayer.addSlashes(us));
                series.setAddCount((series.getAddCount()) + 1);
                getDataLayer().storeSeries(series);
                response.sendRedirect("SchedaSerie?id=" + series.getID());
            } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id or series id (a) is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    //Removes a series from the user's favourites
    private void action_removeSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            //User session checking
             if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser((int) request.getSession().getAttribute("userid"));
            Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("d")));
            getDataLayer().removeUserSeries(getDataLayer().getUserSeries(user, series));
            series.setAddCount((series.getAddCount())-1);
            getDataLayer().storeSeries(series);
            response.sendRedirect("SchedaSerie?id=" + series.getID());
             } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id or series id (a) is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    //Shows the list of all news related to a series
    private void action_go_to_series_news(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // This action is the same thing that happens if a user applies to the news list the filter by series
            response.sendRedirect("ListaNews?s=1&fs=" + SecurityLayer.checkNumeric(request.getParameter("n")));
        } catch (NumberFormatException ex) {
            //News id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    //Shows the list of all series that are similar to another one
    private void action_find_similar_series(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Series s = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("g")));
            String location = "";
            //Series are similar if they have all genres in common
            for (Genre g : s.getGenres()) {
                location += "fg=" + g.getID() + "&";
            }
            // This action is the same thing that happens if a user applies to the series list the filter by genres, selecting all the series' genre
            response.sendRedirect("ListaSerie?" + location + "s=1");
        } catch (NumberFormatException ex) {
            //Series id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Receives all the necessary data to comment a series and, if everything's ok, saves it in the Database
    private void action_comment_series (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        //User session checking
        if(SecurityLayer.checkSession(request) != null){
        if(checkSeriesCommentInputData(request, response)){
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("sid")));
        Calendar c = Calendar.getInstance();
        String title = request.getParameter("commentTitle");
        String text = request.getParameter("commentText");
        Comment comment = getDataLayer().createComment();
        comment.setTitle(title);
        comment.setText(text);
        comment.setUser(user);
        comment.setDate(c.getTime());
        comment.setSeries(series);
        getDataLayer().storeComment(RESTSecurityLayer.addSlashes(comment));
        response.sendRedirect("SchedaSerie?id=" + series.getID());
        } else {
            request.setAttribute("error", "Errore: uno dei campi è vuoto!");
            action_series_info(request, response);
        }
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        }catch (NumberFormatException ex){
            //User id or series id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Increases the number of comment's likes
    private void action_like_comment_series (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         try{
             TemplateResult result = new TemplateResult((getServletContext()));
         //User session checking
         if(SecurityLayer.checkSession(request) != null){ 
         Comment comment = getDataLayer().getComment(SecurityLayer.checkNumeric(request.getParameter("lc")));
         comment.setLikes(comment.getLikes() + 1);
         getDataLayer().storeComment(comment);
         response.sendRedirect("SchedaSerie?id=" + SecurityLayer.checkNumeric(request.getParameter("s")));   
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //Comment id or series id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Increases the number of comment's dislikes
    private void action_dislike_comment_series (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         try{
         TemplateResult result = new TemplateResult(getServletContext());
         //User session checking
         if(SecurityLayer.checkSession(request) != null){
         Comment comment = getDataLayer().getComment(SecurityLayer.checkNumeric((request.getParameter("dc"))));
         comment.setDislikes(comment.getDislikes() + 1);
         getDataLayer().storeComment(comment);
         response.sendRedirect("SchedaSerie?id=" + SecurityLayer.checkNumeric((request.getParameter("s"))));  
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //Comment id or series id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
     }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("a") != null) {

                action_addSeries(request, response);

            } else if (request.getParameter("d") != null) {

                action_removeSeries(request, response);

            } else if (request.getParameter("n") != null) {

                action_go_to_series_news(request, response);

            } else if (request.getParameter("g") != null) {

                action_find_similar_series(request, response);

            } else if (request.getParameter("scs") != null) {

                action_comment_series(request, response);

            } else if (request.getParameter("lc") != null) {

                action_like_comment_series(request, response);

            } else if (request.getParameter("dc") != null) {

                action_dislike_comment_series(request, response);
            } else {

                action_series_info(request, response);
            }
        } catch (IOException ex) {
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Checks if all the input fields have been filled
    private boolean checkSeriesCommentInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("commentTitle") != null && request.getParameter("commentTitle").length() > 0
                && request.getParameter("commentText") != null && request.getParameter("commentText").length() > 0;
    }

    @Override
    public String getServletInfo() {
        return "This servlet controls most of the actions related to series. It shows the series card, adds or removes it from the user's favourites"
                + "finds similar series, shows the list of all news related to that series and allows to comment or like and dislike a comment";
    }

}
