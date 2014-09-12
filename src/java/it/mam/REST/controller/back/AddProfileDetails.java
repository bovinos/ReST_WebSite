
package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.User;
import it.mam.REST.utility.Utility;
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
public class AddProfileDetails extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // prende tutti i dati necessari a far registrare un utente e se è tutto corretto, lo salva sul DB
    private void action_profileDetails_save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
       if(checkUserInputData(request, response)){
            // controlliamo che le password inserite corrispondano
            user = getDataLayer().getUser((int)request.getSession().getAttribute("userid"));
            user.setName(request.getParameter("name"));
            user.setSurname(request.getParameter("surname"));
            try{
            user.setAge(SecurityLayer.checkNumeric(request.getParameter("age")));
            } catch (NumberFormatException e) {
                action_error(request, response, "Field Error");
            }
              //Mi prendo l'array dei generi dalla richiesta e lo trasformo in una lista
            String[] genres = request.getParameterValues("genres");
            List<Genre> genresList = new ArrayList();
                for (String s: genres){
                    try{
                    //prendo il genere dal DB e NON ci metto gli slash perché nel DB ce li ha già e non serve di toglierli perché non devo usarlo
                    genresList.add(RESTSecurityLayer.addSlashes(getDataLayer().getGenre(SecurityLayer.checkNumeric(s))));
                    } catch (NumberFormatException e) {
                        action_error(request, response, "Internal Error");
                    }
                user.setGenres(genresList);
            }
            
            getDataLayer().storeUser(RESTSecurityLayer.addSlashes(user));
        response.sendRedirect("MyProfileActivateUserSignUpData");
    }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_profileDetails_save(request, response);
        } catch (IOException ex) {
            action_error(request, response, null);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    private boolean checkUserInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("surname") != null && request.getParameter("surname").length() > 0
                && request.getParameter("age") != null && request.getParameter("age").length() > 0
                && request.getParameter("gender") != null && request.getParameter("gender").length() > 0
                && request.getParameter("imageURL") != null && request.getParameter("imageURL").length() > 0
                && request.getParameterValues("genres") != null && request.getParameterValues("genres").length > 0;
    }
}
