package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
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
        if (SecurityLayer.checkSession(request) != null){
        String username = SecurityLayer.addSlashes((String)request.getSession().getAttribute("username"));
        request.setAttribute("sessionUsername", username);
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        request.setAttribute("user", user);
        }
        request.setAttribute("genres", getDataLayer().getGenres());
        request.setAttribute("channels", getDataLayer().getChannels());
        result.activate("seriesList.ftl.html", request, response);
    }

    private void action_order_series_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        String[] series = request.getParameterValues("series");
        List<Series> seriesList = new ArrayList();
        for(String s: series){
          seriesList.add(getDataLayer().getSeries(SecurityLayer.checkNumeric(s)));
        }
        int ordertype = SecurityLayer.checkNumeric(request.getParameter("o"));
        switch(ordertype) {
            case 1: RESTSortLayer.sortSeriesByName(seriesList);
            break;
            case 2: RESTSortLayer.sortSeriesByPopularity(seriesList);
            break;
            case 3: RESTSortLayer.sortSeriesByRating(seriesList);
        }
    }
    
    private void action_filter_series_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        List<Series> seriesList = getDataLayer().getSeries();
        
        //Filtro Serie per Nome
        if(request.getParameter("filtername") != null){
            for(Series s: seriesList){
                if(!(s.getName().equals(request.getParameter("filtername")))){
                    seriesList.remove(s);
                }
            }
        }
        //Filtro Serie per genere
        if(request.getParameterValues("filtergenre") != null){
            List<Genre> genresList = new ArrayList();
            for(String g: request.getParameterValues("filtergenre")){
                genresList.add(getDataLayer().getGenre(SecurityLayer.checkNumeric(g)));
            }
            for(Series s: seriesList){
                for(Genre g: genresList){
                if(!(s.getGenres().contains(g))){
                    seriesList.remove(s);
                }
                }
            }
        }
        
                //Filtro Serie per Nome
        if(request.getParameter("filtermyseries") != null){
            TemplateResult result = new TemplateResult(getServletContext());
            if(SecurityLayer.checkSession(request) == null) result.activate("logIn.ftl.html", request, response);
            User user =getDataLayer().getUser(SecurityLayer.checkNumeric((String)request.getSession().getAttribute("userid")));
            List<UserSeries> usList = getDataLayer().getUserSeries(user);
            for(Series s: seriesList){
                for(UserSeries us: usList){
              //  if(!()
              //          {
               //     seriesList.remove(s);
             //   }
                }
            }
        }
        
          if(request.getParameterValues("filterchannel") != null){
            List<Channel> channelList = new ArrayList();
            for(String c: request.getParameterValues("filterchannel")){
                channelList.add(getDataLayer().getChannel(SecurityLayer.checkNumeric(c)));
            }
            for(Series s: seriesList){
                for(Channel c: channelList){
             //   if(!(s.get?????????().contains(c))){ ///////////////////////////// Che get?? non ci sta getChannel per la serie!!!
                //    seriesList.remove(s);
               // }
                }
            }
        }
    }
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_series_list(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
