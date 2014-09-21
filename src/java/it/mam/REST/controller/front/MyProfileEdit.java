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

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {
        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    // Activates the sign up data template
    private void action_activate_ProfileUserSignUpData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "profile");
        if (SecurityLayer.checkSession(request) != null) {
        
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("userProfileContent_tpl", "userSignUpData.ftl.html");
            result.activate("userProfile/userProfileOutline.ftl.html", request, response);
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Allows to insert/modify user's sign up data
    private void action_submit_ProfileUserSignUpData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
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
            request.setAttribute("success", "Dati di registrazione modificati correttamente!");
            action_activate_ProfileUserSignUpData(request, response);
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

    // Activates the optional data template
    private void action_activate_ProfileUserOptionalData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "profile");
        if (SecurityLayer.checkSession(request) != null) {
        
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("genres", getDataLayer().getGenres());
            request.setAttribute("userProfileContent_tpl", "userOptionalData.ftl.html");
            result.activate("userProfile/userProfileOutline.ftl.html", request, response);
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Allows to insert/modify user's optional data
    private void action_submit_ProfileUserOptionalData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
             TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
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

                    user.setAge(SecurityLayer.checkNumeric(request.getParameter("age")));
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
                switch (SecurityLayer.checkNumeric(request.getParameter("gender"))) {
                    case 1:
                        user.setGender(User.MALE);
                        break;
                    case 2:
                        user.setGender(User.FEMALE);
                        break;
                    default:
                        action_error(request, response, "Field Error");
                }
            }
            System.err.println(user);
            getDataLayer().storeUser(RESTSecurityLayer.addSlashes(user));
               request.setAttribute("success", "Dati opzionali modificati correttamente!");
                action_activate_ProfileUserOptionalData(request, response);
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id or age or genre id or gender is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }
    
    // Activates the notify settings template
    private void action_activate_ProfileUserNotifySettings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("where", "profile");
        if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            request.setAttribute("user", user);
            request.setAttribute("userProfileContent_tpl", "userNotifySettings.ftl.html");
            result.activate("userProfile/userProfileOutline.ftl.html", request, response);
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id is not a number
            action_error(request, response, "Riprova di nuovo!");
        }

    }

    // Allows to insert/modify user's notify settings
    private void action_submit_ProfileUserNotifySettings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        TemplateResult result = new TemplateResult(getServletContext());
        if (SecurityLayer.checkSession(request) != null) {
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
                        action_error(request, response, "Riprova di nuovo!");
                        return;
                    }
            }
            request.setAttribute("success", "Impostazioni notifiche modificate correttamente!");
            action_activate_ProfileUserNotifySettings(request, response);
         } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
        } catch (NumberFormatException ex) {
            //User id or t is not a number
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        try{
        int section = SecurityLayer.checkNumeric(request.getParameter("sezione"));
        switch (section) {
            case 1: //"Modifica dati di registrazione"...
                request.setAttribute("currentSection", section+2);
                if (request.getParameter("modifySignUpData") != null) { //Modified data sent...
                        action_submit_ProfileUserSignUpData(request, response);
                } else { //Show modify screen...
                        action_activate_ProfileUserSignUpData(request, response);
                }
                break;
            case 2: //"Modifica dati opzionali"...
                request.setAttribute("currentSection", section+2);
                if (request.getParameter("modifyOptionalData") != null) { //Modified data sent...
                        action_submit_ProfileUserOptionalData(request, response);
                } else { //Show modify screen...
                        action_activate_ProfileUserOptionalData(request, response);
                }
                break;
            case 3: //"Impostazioni Notifiche"...
                request.setAttribute("currentSection", section+2);
                if (request.getParameter("modifyNotifySettings") != null) { //Modified data sent...
                        action_submit_ProfileUserNotifySettings(request, response);
                } else { //Show modify screen...
                        action_activate_ProfileUserNotifySettings(request, response);
                }
                break;
            default:
                action_error(request, response, "Riprova di nuovo!");
        }
        } catch (IOException ex) {
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet contains some things related to user profile. It shows sign up data template, optional data template"
                + "and notify settings template and allow to change that data";
    }

}
