package it.mam.REST.controller;

import it.mam.REST.data.impl.RESTDataLayerMySQL;
import it.mam.REST.data.model.RESTDataLayer;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author alex
 */
public abstract class RESTBaseController extends HttpServlet {

    public RESTDataLayer dataLayer;

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {

        try {
            this.dataLayer = new RESTDataLayerMySQL((DataSource) getServletContext().getAttribute("datasource"));
            this.dataLayer.init();
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(RESTBaseController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(RESTBaseController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataLayerException ex) {
            Logger.getLogger(RESTBaseController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            Logger.getLogger(RESTBaseController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.dataLayer.destroy();
            } catch (DataLayerException ex) {
                Logger.getLogger(RESTBaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public RESTDataLayer getDataLayer() {
        return this.dataLayer;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
