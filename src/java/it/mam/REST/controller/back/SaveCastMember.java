
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.CastMemberSeries;
import it.mam.REST.data.model.Series;
import it.univaq.f4i.iw.framework.result.FailureResult;
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
 * @author Mirko
 */
public class SaveCastMember extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // passa la lista delle serie al template "insert_news.ftl"
    private void action_news_insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CastMember castMember = getDataLayer().createCastMember();
        CastMemberSeries cms = getDataLayer().createCastMemberSeries();
        if (checkCastMemberInputData(request, response)){
        castMember.setName(request.getParameter("name"));
        castMember.setSurname(request.getParameter("surname"));
        try{
        castMember.setBirthDate((SecurityLayer.checkDate(request.getParameter("birthdate"))).getTime());
        } catch (NumberFormatException e){
            action_error(request, response, "Invalid Datetime");
        }
        try {
        int gender = SecurityLayer.checkNumeric(request.getParameter("gender"));
       
        switch (gender) {
            case 1: castMember.setGender(CastMember.MALE);
            break;
            case 2: castMember.setGender(CastMember.FEMALE);
            break;
            default: action_error(request, response, "Invalid gender");
        }
         } catch (NumberFormatException e) {
                        action_error(request, response, "Internal Error");
                    }
        castMember.setCountry(request.getParameter("country"));
        castMember.setImageURL(request.getParameter("imageURL"));
        
        String[] series = request.getParameterValues("series");
            List<Series> seriesList = new ArrayList();
            if (series != null) {
                for (String s: series){
                    try{
                    seriesList.add(RESTSecurityLayer.addSlashes(getDataLayer().getSeries(SecurityLayer.checkNumeric(s))));
                    } catch (NumberFormatException e) {
                        action_error(request, response, "Field Error");
                    }
                }
                castMember.setSeries(seriesList);
            }
        getDataLayer().storeCastMember(RESTSecurityLayer.addSlashes(castMember));
        
     //Salvo i dati della relazione
    
    String[] roles = request.getParameterValues("roles");
    cms.setCastMemberID(castMember.getID());
    for (Series s: castMember.getSeries()){
        for(String r: roles){
            cms.setSeriesID(s.getID());
            cms.setRole(r);
            getDataLayer().storeCastMemberSeries(RESTSecurityLayer.addSlashes(cms));
        }   
    }
  }
}
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_news_insert(request, response);
        } catch (IOException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
private boolean checkCastMemberInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("surname") != null && request.getParameter("surname").length() > 0 
                && request.getParameter("birthdate") != null && request.getParameter("birthdate").length() > 0
                && request.getParameter("gender") != null && request.getParameter("gender").length() > 0
                && request.getParameter("country") != null && request.getParameter("country").length() > 0
                && request.getParameter("imageURL") != null && request.getParameter("imageURL").length() > 0
                && request.getParameterValues("series") != null && request.getParameterValues("series").length > 0
                && request.getParameterValues("roles") != null && request.getParameterValues("roles").length > 0;
                 
    }
}
