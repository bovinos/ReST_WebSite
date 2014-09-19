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

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    private void action_insert_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (SecurityLayer.checkSession(request) == null) {
                result.activate("logIn.ftl.html", request, response);
            }
            if (user.getGroup().getID() != Group.ADMIN) {
                result.activate("newsList.ftl.html", request, response);
            }
            request.setAttribute("user", user);
            request.setAttribute("services", getDataLayer().getServices());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertGroup.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_save_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (SecurityLayer.checkSession(request) == null) {
                result.activate("logIn.ftl.html", request, response);
            }
            if (user.getGroup().getID() != Group.ADMIN) {
                result.activate("newsList.ftl.html", request, response);
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Group group = getDataLayer().createGroup();
            // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
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
                action_error(request, response, "Inserire i campi obbligatori");
                return;
            }
            getDataLayer().storeGroup(RESTSecurityLayer.addSlashes(group));
            request.setAttribute("backContent_tpl", "insertGroup.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_insert_service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (SecurityLayer.checkSession(request) == null) {
                result.activate("logIn.ftl.html", request, response);
            }
            if (user.getGroup().getID() != Group.ADMIN) {
                result.activate("newsList.ftl.html", request, response);
            }
            request.setAttribute("user", user);
            request.setAttribute("groups", getDataLayer().getGroups());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertService.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_save_service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (SecurityLayer.checkSession(request) == null) {
                result.activate("logIn.ftl.html", request, response);
            }
            if (user.getGroup().getID() != Group.ADMIN) {
                result.activate("newsList.ftl.html", request, response);
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Service service = getDataLayer().createService();
            // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
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
                action_error(request, response, "Inserire i campi obbligatori");
            }
            getDataLayer().storeService(RESTSecurityLayer.addSlashes(service));
            request.setAttribute("backContent_tpl", "insertService.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_insert_serviceGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (SecurityLayer.checkSession(request) == null) {
                result.activate("logIn.ftl.html", request, response);
            }
            if (user.getGroup().getID() != Group.ADMIN) {
                result.activate("newsList.ftl.html", request, response);
            }
            request.setAttribute("user", user);
            request.setAttribute("services", getDataLayer().getServices());
            request.setAttribute("groups", getDataLayer().getGroups());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertServiceGroup.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        }
    }

    private void action_save_serviceGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (SecurityLayer.checkSession(request) == null) {
                result.activate("logIn.ftl.html", request, response);
            }
            if (user.getGroup().getID() != Group.ADMIN) {
                result.activate("newsList.ftl.html", request, response);
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
            request.setAttribute("backContent_tpl", "insertServiceGroup.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
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
        } catch (NumberFormatException ex) {
            action_error(request, response, "Field Error");
        } catch (IOException ex) {
            action_error(request, response, "Internal Error");
        }

    }

    private boolean checkGroupInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("groupName") != null && request.getParameter("groupName").length() > 0
                && request.getParameter("groupDescription") != null && request.getParameter("groupDescription").length() > 0;
        // && request.getParameterValues("services") != null && request.getParameterValues("services").length > 0;
    }

    private boolean checkServiceInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("serviceName") != null && request.getParameter("serviceName").length() > 0
                && request.getParameter("serviceDescription") != null && request.getParameter("serviceDescription").length() > 0;
        //&& request.getParameterValues("groups") != null && request.getParameterValues("groups").length > 0;
    }

    private boolean checkServiceGroupInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("service") != null && request.getParameter("service").length() > 0
                && request.getParameter("group") != null && request.getParameter("group").length() > 0;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
