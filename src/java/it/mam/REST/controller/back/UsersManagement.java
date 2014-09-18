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
import java.util.ArrayList;
import java.util.List;
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

    // passa la lista delle serie al template "insert_news.ftl"
    private void action_insert_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response);}
        if(user.getGroup().getID()!= Group.ADMIN) { result.activate("newsList.ftl.html", request, response);}
        request.setAttribute("services", getDataLayer().getServices());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
         request.setAttribute("backContent_tpl", "insertGroup.ftl.html");
        result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
    }

    // controlla l'inserimento corretto di tutti i dati di una news e la salva sul DB
    private void action_save_group(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response);}
        if(user.getGroup().getID()!= Group.ADMIN) { result.activate("newsList.ftl.html", request, response);}
        Group group = getDataLayer().createGroup(); 
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkGroupInputData(request, response)){
            group.setName(request.getParameter("name"));
            group.setDescription(request.getParameter("description"));
          
            String[] services = request.getParameterValues("series");
            List<Service> serviceList = new ArrayList();
                for (String s: services){
                    serviceList.add(getDataLayer().getService(SecurityLayer.checkNumeric(s)));
                }
                group.setServices(serviceList);
            } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
        getDataLayer().storeGroup(RESTSecurityLayer.addSlashes(group));
        } catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
    }
    
    private void action_insert_service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response);}
        if(user.getGroup().getID()!= Group.ADMIN) { result.activate("newsList.ftl.html", request, response);}
        request.setAttribute("groups", getDataLayer().getGroups());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
         request.setAttribute("backContent_tpl", "insertService.ftl.html");
        result.activate("../back/backOutline.ftl.html", request, response);
        } catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
    }
    
    private void action_save_service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
        TemplateResult result = new TemplateResult(getServletContext());
        User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
        if(SecurityLayer.checkSession(request) == null){ result.activate("logIn.ftl.html", request, response);}
        if(user.getGroup().getID()!= Group.ADMIN) { result.activate("newsList.ftl.html", request, response);}
        Service service = getDataLayer().createService(); 
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkServiceInputData(request, response)){
            service.setName(request.getParameter("name"));
            service.setDescription(request.getParameter("description"));
          
            String[] groups = request.getParameterValues("groups");
            List<Group> groupList = new ArrayList();
                for (String g: groups){
                    groupList.add(getDataLayer().getGroup(SecurityLayer.checkNumeric(g)));
                }
                service.setGroups(groupList);
            } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
        getDataLayer().storeService(RESTSecurityLayer.addSlashes(service));
        } catch (NumberFormatException ex){
            action_error(request, response, "Field Error");
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
        int sezione = SecurityLayer.checkNumeric(request.getParameter("sezione"));
        switch(sezione){
            case 1: if((request.getParameter("ig")) != null) { 
                action_save_group(request, response);
                } else {
                action_insert_group(request, response);
                }
            break;
            case 2: if((request.getParameter("is")) != null) { 
                action_save_service(request, response);
                } else {
                action_insert_service(request, response);
                }
            break;
            default: action_error(request, response, "Field Error");
        }
        } catch (NumberFormatException ex ) {
            action_error(request, response, "Field Error");
        } catch (IOException ex) {
            action_error(request, response, "Internal Error");
        }
     
    }

    private boolean checkGroupInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("description") != null && request.getParameter("description").length() > 0
                && request.getParameterValues("services") != null && request.getParameterValues("services").length > 0;
    }
    
    private boolean checkServiceInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("description") != null && request.getParameter("description").length() > 0
                && request.getParameterValues("groups") != null && request.getParameterValues("groups").length > 0;
    }
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
