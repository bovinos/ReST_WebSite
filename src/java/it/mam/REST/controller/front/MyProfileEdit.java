package it.mam.REST.controller.front;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
import it.mam.REST.utility.RESTSortLayer;
import it.mam.REST.utility.Utility;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class MyProfileEdit extends RESTBaseController {

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // attiva il template dei dati personali dell'utente
    private void action_activate_ProfileUserSignUpData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        try {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("userProfileContent_tpl", "userSignUpData.ftl.html");
            result.activate("userProfile/userProfileOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_submit_ProfileUserSignUpData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric(request.getSession().getAttribute("userid").toString()));
            if ((request.getParameter("username") != null && request.getParameter("username").length() > 0)
                    && !(request.getParameter("username").equals(user.getUsername()))) {
                user.setUsername(request.getParameter("username"));
            }
            if ((request.getParameter("mail") != null && request.getParameter("mail").length() > 0)
                    && !(request.getParameter("mail").equals(user.getMail()))) {
                user.setMail(request.getParameter("mail"));
            }

            if (request.getParameter("oldpassword") != null && request.getParameter("oldpassword").length() > 0
                    && (Utility.stringToMD5(Utility.stringToMD5(request.getParameter("oldpassword")))).equals(user.getPassword())
                    && (request.getParameter("newpassword") != null && request.getParameter("newpassword").length() > 0)
                    && (request.getParameter("confirmpassword") != null && request.getParameter("confirmpassword").length() > 0)
                    && (request.getParameter("newpassword").equals(request.getParameter("confirmpassword")))) {
                user.setPassword(Utility.stringToMD5(Utility.stringToMD5(request.getParameter("newpassword"))));
            }
            getDataLayer().storeUser(RESTSecurityLayer.addSlashes(user));
            response.sendRedirect("ModificaProfiloPersonale?sezione=1");
        } catch (NumberFormatException e) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_activate_ProfileUserOptionalData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        try {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("genres", getDataLayer().getGenres());
            request.setAttribute("userProfileContent_tpl", "userOptionalData.ftl.html");
            result.activate("userProfile/userProfileOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_submit_ProfileUserOptionalData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (request.getParameter("name") != null && request.getParameter("name").length() > 0
                    && !(request.getParameter("name").equals(user.getName()))) {
                user.setName(request.getParameter("name"));
            }
            if (request.getParameter("surname") != null && request.getParameter("surname").length() > 0
                    && !(request.getParameter("surname").equals(user.getSurname()))) {
                user.setSurname(request.getParameter("surname"));
            }
            if (request.getParameter("age") != null && request.getParameter("age").length() > 0
                    && !(request.getParameter("age").equals(String.valueOf(user.getAge())))) {
                try {
                    user.setAge(SecurityLayer.checkNumeric(request.getParameter("age")));
                } catch (NumberFormatException e) {
                    action_error(request, response, "Field Error");
                }
            }
            if (request.getParameter("imageURL") != null && request.getParameter("imageURL").length() > 0
                    && !(request.getParameter("imageURL").equals(user.getImageURL()))) {
                user.setImageURL(request.getParameter("imageURL"));
            }
           
            if (request.getParameterValues("genres") != null && request.getParameterValues("genres").length > 0) {
                String[] genres = request.getParameterValues("genres");
                List<Genre> genresList = new ArrayList();
                for (String s : genres) {
                    //prendo il genere dal DB e NON ci metto gli slash perché nel DB ce li ha già e non serve di toglierli perché non devo usarlo
                    genresList.add(RESTSecurityLayer.addSlashes(getDataLayer().getGenre(SecurityLayer.checkNumeric(s))));
                }
                
                    user.setGenres(genresList);
            }
            
            if (request.getParameter("gender") != null && request.getParameter("gender").length() > 0
                    && !(request.getParameter("gender").equals(user.getGender()))) {
                switch(SecurityLayer.checkNumeric(request.getParameter("gender"))) {
                    case 1: user.setGender(User.MALE);
                    break;
                    case 2: user.setGender(User.FEMALE);
                    break;
                    default: action_error(request, response, "Field Error");
                }
            }
            System.err.println(user);
            getDataLayer().storeUser(RESTSecurityLayer.addSlashes(user));
            response.sendRedirect("ModificaProfiloPersonale?sezione=2");
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_activate_ProfileUserNotifySettings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) == null) {
            result.activate("logIn.ftl.html", request, response);
        }
        try {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("userProfileContent_tpl", "userNotifySettings.ftl.html");
            result.activate("userProfile/userProfileOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }

    }

    private void action_submit_ProfileUserNotifySettings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (request.getParameter("a") != null && request.getParameter("a").length() > 0) {
                 user.setNotificationStatus(true);
                 getDataLayer().storeUser(user);
                 if (request.getParameter("t") != null && request.getParameter("t").length() > 0) {
                 List<UserSeries> userseriesList = getDataLayer().getUserSeries(user);
                 for (UserSeries us : userseriesList) {
                      us.setAnticipationNotification(new Date((SecurityLayer.checkNumeric(request.getParameter("t")) * RESTSortLayer.HOUR_IN_MILLISECONDS) - RESTSortLayer.HOUR_IN_MILLISECONDS));
                      getDataLayer().storeUserSeries(RESTSecurityLayer.addSlashes(us));
                        }
                    }
                 
            }else {   
                    user.setNotificationStatus(false);
                    getDataLayer().storeUser(user);
                    if (request.getParameter("t") != null && request.getParameter("t").length() > 0) {
                        action_error(request, response, "Field Error");
                    }
            }
            response.sendRedirect("ModificaProfiloPersonale?sezione=3");
        } catch (NumberFormatException ex) {
            action_error(request, response, ex.getMessage());
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        try{
        int section = SecurityLayer.checkNumeric(request.getParameter("sezione"));
        switch (section) {
            case 1: //Siamo nel ramo "Modifica dati di registrazione"...
                if (request.getParameter("modifySignUpData") != null) { //Abbiamo inviato i dati modificati...
                    try {
                        action_submit_ProfileUserSignUpData(request, response);
                    } catch (IOException ex) {
                        action_error(request, response, ex.getMessage());
                    }
                } else { //Se siamo qui abbiamo solo richiesto la schermata di modifica, quindi la mostro...
                    try {
                        action_activate_ProfileUserSignUpData(request, response);
                    } catch (IOException ex) {
                        action_error(request, response, ex.getMessage());
                    }
                }
                break;
            case 2: //Siamo nel ramo "Modifica dati opzionali"...
                if (request.getParameter("modifyOptionalData") != null) { //Abbiamo inviato i dati modificati...
                    try {
                        action_submit_ProfileUserOptionalData(request, response);
                    } catch (IOException ex) {
                        action_error(request, response, ex.getMessage());
                    }
                } else { //Se siamo qui abbiamo solo richiesto la schermata di modifica, quindi la mostro...
                    try {
                        action_activate_ProfileUserOptionalData(request, response);
                    } catch (IOException ex) {
                        action_error(request, response, ex.getMessage());
                    }
                }
                break;
            case 3: //Siamo nel ramo "Impostazioni Notifiche"...
                if (request.getParameter("modifyNotifySettings") != null) { //Abbiamo inviato i dati modificati...
                    try {
                        action_submit_ProfileUserNotifySettings(request, response);
                    } catch (IOException ex) {
                        action_error(request, response, ex.getMessage());
                    }
                } else { //Se siamo qui abbiamo solo richiesto la schermata di modifica, quindi la mostro...
                    try {
                        action_activate_ProfileUserNotifySettings(request, response);
                    } catch (IOException ex) {
                        action_error(request, response, ex.getMessage());
                    }
                }
                break;
            default:
                action_error(request, response, "The requested resource is not available");
        }
        }catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
