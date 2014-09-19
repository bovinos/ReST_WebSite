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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class SeriesCard extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutte le serie, le stagioni e l'utente e passa al template seriesCard.ftl.html
    private void action_series_info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "series");
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        try {
            Series s = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("id")));
            request.setAttribute("series", s);
            request.setAttribute("seriesRating", RESTSortLayer.getMediumRating(s));
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

            //Controllo la sessione e creo l'utente
            if (SecurityLayer.checkSession(request) != null) {
                User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
                request.setAttribute("user", user);

                //Vedo se la serie è già fra i preferiti dell'utente attuale
                boolean favourite;
                UserSeries us = getDataLayer().getUserSeries(user, s);
                favourite = (us != null);
                request.setAttribute("favourite", favourite);
            }
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
        result.activate("seriesCard.ftl.html", request, response);

    }

    private void action_addSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        try {
            User user = getDataLayer().getUser((SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString())));
            Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("a")));
            UserSeries us = getDataLayer().createUserSeries();
            us.setUser(user);
            us.setSeries(series);
            getDataLayer().storeUserSeries(RESTSecurityLayer.addSlashes(us));
            series.setAddCount((series.getAddCount())+1);
            getDataLayer().storeSeries(series);
            response.sendRedirect("SchedaSerie?id=" + series.getID());
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_removeSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getDataLayer().getUser((int) request.getSession().getAttribute("userid"));
            Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("d")));
            getDataLayer().removeUserSeries(getDataLayer().getUserSeries(user, series));
            series.setAddCount((series.getAddCount())-1);
            getDataLayer().storeSeries(series);
            response.sendRedirect("SchedaSerie?id=" + series.getID());
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_go_to_series_news(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // è come se nella schermata della lista delle news avessero deciso di filtrare per la serie x => fs=ID di x
            // e poi avessero cliccato il bottone per filtrare => s=1
            response.sendRedirect("ListaNews?s=1&fs=" + SecurityLayer.checkNumeric(request.getParameter("n")));
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_find_similar_series(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Series s = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("g")));
            String location = "";
            // delle serie sono simili se hanno in comune tutti i generi
            for (Genre g : s.getGenres()) {
                location += "fg=" + g.getID() + "&";
            }
            // è come se nella schermata della lista delle serie l'utente ha cliccato su tutti i generi di questa serie fg=genreID&fg=genreID...
            // e poi ha cliccato sul bottone per filtrare s=1
            response.sendRedirect("ListaSerie?" + location + "s=1");
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }
    
    private void action_comment_series (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); }
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
            action_error(request, response, "Inserisci i campi obbligatori!");
        }
        }catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
    }

    private void action_like_comment_series (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
          try{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); } 
         Comment comment = getDataLayer().getComment(SecurityLayer.checkNumeric(request.getParameter("lc")));
         comment.setLikes(comment.getLikes() + 1);
         getDataLayer().storeComment(comment);
         response.sendRedirect("SchedaSerie?id=" + SecurityLayer.checkNumeric(request.getParameter("s")));   
          } catch (NumberFormatException ex) {
              action_error(request, response, "Field Error");
          }
     }
    
    private void action_dislike_comment_series (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
         try{
         TemplateResult result = new TemplateResult(getServletContext());
         if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response); }
         Comment comment = getDataLayer().getComment(SecurityLayer.checkNumeric((request.getParameter("dc"))));
         comment.setDislikes(comment.getDislikes() + 1);
         getDataLayer().storeComment(comment);
         response.sendRedirect("SchedaSerie?id=" + SecurityLayer.checkNumeric((request.getParameter("s"))));  
        } catch (NumberFormatException ex) {
              action_error(request, response, "Field Error");
          }
     }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        if (request.getParameter("a") != null) {
            try {
                action_addSeries(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        } else if (request.getParameter("d") != null) {
            try {
                action_removeSeries(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        } else if (request.getParameter("n") != null) {
            try {
                action_go_to_series_news(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        } else if (request.getParameter("g") != null) {
            try {
                action_find_similar_series(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        } else if (request.getParameter("scs") != null) {
            try {
                action_comment_series(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
            } else if (request.getParameter("lc") != null) {
        try {
            action_like_comment_series(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
        } else if (request.getParameter("dc") != null) {
        try {
            action_dislike_comment_series(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
        } else {
            try {
                action_series_info(request, response);
            } catch (IOException ex) {
                action_error(request, response, ex.getMessage());
            }
        }
    }
private boolean checkSeriesCommentInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("commentTitle") != null && request.getParameter("commentTitle").length() > 0
                && request.getParameter("commentText") != null && request.getParameter("commentText").length() > 0;
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
