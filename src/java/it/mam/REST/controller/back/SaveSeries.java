
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Series;
import it.univaq.f4i.iw.framework.result.FailureResult;
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
 * @author Mirko
 */
public class SaveSeries extends RESTBaseController{
 // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutte le serie e le passa al template seriesList.ftl.html
    private void action_series_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        Series series = getDataLayer().createSeries();
        //Controllo che i campi siano validi
        if (checkSeriesInputData(request, response)){
            //Tolgo gli slash
        series.setName(SecurityLayer.addSlashes(request.getParameter("name")));
        try{
        series.setYear(SecurityLayer.checkNumeric(request.getParameter("year")));
        } catch (NumberFormatException e) {
            action_error(request, response, "Field Error");
        }
        series.setDescription(SecurityLayer.addSlashes(request.getParameter("description")));
        series.setImageURL(SecurityLayer.addSlashes(request.getParameter("imageURL")));
        series.setState(SecurityLayer.addSlashes(request.getParameter("state")));
        } else action_error(request, response, "Inserire i campi obbligatori!");
        
        //Mi prendo l'array dei generi dalla richiesta e lo trasformo in una lista
        String[] genres = request.getParameterValues("genres");
            List<Genre> genresList = new ArrayList();
                for (String s: genres){
                    try{
                    genresList.add(getDataLayer().getGenre(SecurityLayer.checkNumeric(s)));
                    } catch (NumberFormatException e) {
                        action_error(request, response, "Internal Error");
                    }
                series.setGenres(genresList);
            }
        getDataLayer().storeSeries(series);
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

    private boolean checkSeriesInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("name") != null && request.getParameter("year") != null && request.getParameter("description") != null 
                && request.getParameter("image_URL") != null && request.getParameter("state") != null && request.getParameter("genres") != null
                && (request.getParameter("name").length()) > 0 && (request.getParameter("year").length() > 0) && (request.getParameter("description").length() > 0)
                && (request.getParameter("imageURL").length() > 0) && (request.getParameter("state").length() > 0);
    }
}
