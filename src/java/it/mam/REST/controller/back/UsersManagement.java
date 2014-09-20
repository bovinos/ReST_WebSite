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
            // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
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

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            int sezione = SecurityLayer.checkNumeric(request.getParameter("sezione"));
            switch (sezione) {
                case 1:
                    if ((request.getParameter("ig")) != null) {
                        action_save_group(request, response);
                    } else {
                        action_insert_group(request, response);
                    }
                    break;
                case 2:
                    if ((request.getParameter("is")) != null) {
                        action_save_service(request, response);
                    } else {
                        action_insert_service(request, response);
                    }
                    break;
                case 3:
                    if ((request.getParameter("isg")) != null) {
                        action_save_serviceGroup(request, response);
                    } else {
                        action_insert_serviceGroup(request, response);
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
