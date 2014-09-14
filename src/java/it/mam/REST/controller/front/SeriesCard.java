package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Season;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
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
public class SeriesCard extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutte le serie, le stagioni e l'utente e passa al template seriesCard.ftl.html
    private void action_series_info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        Series s = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("id")));
        request.setAttribute("series", s);
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
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        String username = SecurityLayer.addSlashes((String)request.getSession().getAttribute("username"));
        request.setAttribute("sessionUsername", username);
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", user);
        
        //Vedo se la serie è già fra i preferiti dell'utente attuale
        boolean favourite;
        UserSeries us = getDataLayer().getUserSeries(user, s);
        favourite = (us != null);
        request.setAttribute("favourite", favourite);
        
       result.activate("seriesCard.ftl.html", request, response); 
        
    }

    private void action_addSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getDataLayer().getUser((int) request.getSession().getAttribute("userid"));
        Series series = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("a")));
        UserSeries us = getDataLayer().createUserSeries();
        us.setUser(user);
        us.setSeries(series);
        getDataLayer().storeUserSeries(RESTSecurityLayer.addSlashes(us));
        response.sendRedirect("ProfiloPersonale?sezione=1#s" + series.getID());

    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        if(request.getParameter("a") != null){
            try {
            action_addSeries(request, response);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
