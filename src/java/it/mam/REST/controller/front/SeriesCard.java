package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Season;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
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

    // prende tutte le news e le passa al template lista_news.ftl.html
    private void action_series_info(HttpServletRequest request, HttpServletResponse response, int id) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        Series s = RESTSecurityLayer.stripSlashesSeries(getDataLayer().getSeries(id));
        request.setAttribute("series", s);
        List<Season> seasonList = new ArrayList();
        List<Episode> episodeList = s.getEpisodes();
        Season sn = null;
        for (Episode e : episodeList) {
            if (sn == null || sn.getNumber() != e.getSeason()) {
                sn = new Season(e.getSeason(), new ArrayList());
                seasonList.add(sn);
            }
            sn.getEpisodes().add(RESTSecurityLayer.stripSlashesEpisode(e));
        }
        request.setAttribute("seasons", seasonList);
        //Controllo la sessione e creo l'utente
        if (SecurityLayer.checkSession(request) != null){
        String username = SecurityLayer.addSlashes((String)request.getSession().getAttribute("username"));
        request.setAttribute("sessionUsername", username);
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", RESTSecurityLayer.stripSlashesUser(user));
        }
        result.activate("seriesCard.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        int id;
        try {
            id = SecurityLayer.checkNumeric(request.getParameter("id"));
            action_series_info(request, response, id);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
