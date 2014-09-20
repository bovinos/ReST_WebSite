package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.Service;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class UsersManagement extends RESTBaseController {

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    /* ======================================== INSERT - SAVE =================================================*/
    
    // Activates the insert group template
    private void action_insert_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("services", getDataLayer().getServices());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertGroup.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
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

    // Receives all the necessary data to insert a group
    private void action_save_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Group group = getDataLayer().createGroup();
            if (checkGroupInputData(request, response)) {
                group.setName(request.getParameter("groupName"));
                group.setDescription(request.getParameter("groupDescription"));

//                String[] services = request.getParameterValues("series");
//                List<Service> serviceList = new ArrayList();
//                for (String s : services) {
//                    serviceList.add(getDataLayer().getService(SecurityLayer.checkNumeric(s)));
//                }
//                group.setServices(serviceList);
            } else {
                request.setAttribute("error", "Uno dei campi è vuoto!");
                action_insert_group(request, response);
                return;
            }
            getDataLayer().storeGroup(RESTSecurityLayer.addSlashes(group));
                request.setAttribute("success", "Gruppo inserito correttamente!");
                action_insert_group(request, response);
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

    // Activates the insert service template
    private void action_insert_service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("groups", getDataLayer().getGroups());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertService.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
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

    // Receives all the necessary data to insert a service
    private void action_save_service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Service service = getDataLayer().createService();
            if (checkServiceInputData(request, response)) {
                service.setName(request.getParameter("serviceName"));
                service.setDescription(request.getParameter("serviceDescription"));

//                String[] groups = request.getParameterValues("groups");
//                List<Group> groupList = new ArrayList();
//                for (String g : groups) {
//                    groupList.add(getDataLayer().getGroup(SecurityLayer.checkNumeric(g)));
//                }
//                service.setGroups(groupList);
            } else {
                request.setAttribute("error", "Uno dei campi è vuoto!");
                action_insert_service(request, response);
                return;
            }
            getDataLayer().storeService(RESTSecurityLayer.addSlashes(service));
            request.setAttribute("success", "Servizio inserito correttamente!");
            action_insert_service(request, response);
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

    // Activates the insert service-group template
    private void action_insert_serviceGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("services", getDataLayer().getServices());
            request.setAttribute("groups", getDataLayer().getGroups());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertServiceGroup.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
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

    // Receives all the necessary data to link a service to a group
    private void action_save_serviceGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("services", getDataLayer().getServices());
            request.setAttribute("groups", getDataLayer().getGroups());
            if (checkServiceGroupInputData(request, response)) {
                Service service = getDataLayer().getService(SecurityLayer.checkNumeric(request.getParameter("service")));
                service.addGroup(getDataLayer().getGroup(SecurityLayer.checkNumeric(request.getParameter("group"))));
                getDataLayer().storeService(RESTSecurityLayer.addSlashes(service));
            }
                request.setAttribute("success", "Servizio e gruppo associati correttamente!");
                action_insert_serviceGroup(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or service id or group id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    /*============================================= REMOVE - DELETE ==================================================*/
    
    // Activates the remove group template
    private void action_remove_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("groups", getDataLayer().getGroups());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "removeGroup.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
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

    // Receives all the necessary data to delete a group
    private void action_delete_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            getDataLayer().removeGroup(getDataLayer().getGroup(SecurityLayer.checkNumeric(request.getParameter("groups"))));
                request.setAttribute("success", "Rimozione gruppi completata!");
                action_remove_group(request, response);
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

    // Activates the remove service template
    private void action_remove_service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("services", getDataLayer().getServices());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "removeService.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
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

    // Receives all the necessary data to delete a service
    private void action_delete_service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            getDataLayer().removeService(getDataLayer().getService(SecurityLayer.checkNumeric(request.getParameter("services"))));
            request.setAttribute("success", "Servizio inserito correttamente!");
            action_remove_service(request, response);
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

    // Activates the remove service-group template
    private void action_remove_serviceGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("services", getDataLayer().getServices());
            request.setAttribute("groups", getDataLayer().getGroups());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "removeServiceGroup.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
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

    // Receives all the necessary data to destroy link between service and group
    private void action_delete_serviceGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Group g = getDataLayer().getGroup(SecurityLayer.checkNumeric(request.getParameter("group")));
            Service s = getDataLayer().getService(SecurityLayer.checkNumeric(request.getParameter("service")));
            if(!(g.getServices().contains(s))) {
                request.setAttribute("error", "Questo gruppo e questo servizio non sono associati!");
                action_remove_serviceGroup(request, response);
                return;
            }
                g.removeService(s);
                System.err.println(g.getServices().isEmpty());
                getDataLayer().storeGroup(g);
                request.setAttribute("success", "Servizio e gruppo separati correttamente!");
                action_remove_serviceGroup(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or service id or group id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            int sezione = SecurityLayer.checkNumeric(request.getParameter("sezione"));
            switch (sezione) {
                case 1:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("ig")) != null) {
                        action_save_group(request, response);
                    } else {
                        action_insert_group(request, response);
                    }
                    break;
                case 2:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("is")) != null) {
                        action_save_service(request, response);
                    } else {
                        action_insert_service(request, response);
                    }
                    break;
                case 3:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("isg")) != null) {
                        action_save_serviceGroup(request, response);
                    } else {
                        action_insert_serviceGroup(request, response);
                    }
                    break;
                 case 4:
                     request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rg")) != null) {
                        action_delete_group(request, response);
                    } else {
                        action_remove_group(request, response);
                    }
                    break;
                case 5:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rs")) != null) {
                        action_delete_service(request, response);
                    } else {
                        action_remove_service(request, response);
                    }
                    break;
                case 6:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rsg")) != null) {
                        action_delete_serviceGroup(request, response);
                    } else {
                        action_remove_serviceGroup(request, response);
                    }
                    break;
                default:
                    action_error(request, response, "Field Error");
            }
        } catch (NumberFormatException | IOException ex) {
            action_error(request, response, "Riprova di nuovo!");
        }

    }
    
    // Checks if all the input fields have been filled
    private boolean checkGroupInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("groupName") != null && request.getParameter("groupName").length() > 0
                && request.getParameter("groupDescription") != null && request.getParameter("groupDescription").length() > 0;
        // && request.getParameterValues("services") != null && request.getParameterValues("services").length > 0;
    }
    // Checks if all the input fields have been filled
    private boolean checkServiceInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("serviceName") != null && request.getParameter("serviceName").length() > 0
                && request.getParameter("serviceDescription") != null && request.getParameter("serviceDescription").length() > 0;
        //&& request.getParameterValues("groups") != null && request.getParameterValues("groups").length > 0;
    }
    // Checks if all the input fields have been filled
    private boolean checkServiceGroupInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("service") != null && request.getParameter("service").length() > 0
                && request.getParameter("group") != null && request.getParameter("group").length() > 0;
    }

    @Override
    public String getServletInfo() {
        return "This servlet activates all the user management templates and allow to insert groups, services and to link them";
    }

}
