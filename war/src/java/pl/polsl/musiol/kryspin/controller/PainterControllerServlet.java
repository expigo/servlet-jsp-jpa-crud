/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.musiol.kryspin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.polsl.musiol.kryspin.ejb.ModelFacadeBean;
import pl.polsl.musiol.kryspin.ejb.ModelFacadeBeanLocal;
import pl.polsl.musiol.kryspin.model.Painter;
import pl.polsl.musiol.kryspin.model.Painting;
import static pl.polsl.musiol.kryspin.model.Util.date;

/**
 * Servlet for processing actions on Painter entity.
 *
 * @author Kryspin Musiol
 * 
 * @version 1.0
 */
@WebServlet
public class PainterControllerServlet extends HttpServlet {


    /**
     * Injected local EJB module that services entities
     */
    @EJB
    private ModelFacadeBeanLocal model;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {

                String action = request.getParameter("action");

                if (action == null) {
                    action = "LIST";
                }
                switch (action) {
                    case "LIST":
                        listPainters(request, response);
                        break;
                    case "ADD":
                        addPainter(request, response);
                        break;
                    case "LOAD":
                        loadPainter(request, response);
                        break;
                    case "UPDATE":
                        updatePainter(request, response);
                        break;
                    case "DEL":
                        deletePainter(request, response);
                        break;
                    default:
                        listPainters(request, response);
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }

        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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

    /**
     * Method used for listing all Painters stored in the DB
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private void listPainters(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Painter> painters = model.getAllPainters();
        request.setAttribute("PAINTER_LIST", painters);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/list_painters.jsp");

        dispatcher.forward(request, response);
    }

    /**
     * Method that sends a Painter entity to the EJB module responsible for persisting it to the DB
     * (data defining the new Painter is taken from form at JSP page)
     * @param request
     * @param response
     * @throws ServletException 
     */
    private void addPainter(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        Cookie addCookie = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie tempCookie : cookies) {
                // Cookie name cannot be null or empty
                if (tempCookie.getName().equals("addCookie")) {
                    // Cookie values are only Strings
                    Integer count = Integer.parseInt(tempCookie.getValue());
                    count++;
                    tempCookie.setValue(Integer.toString(count));
                    addCookie = tempCookie;
                    break;
                }
            }
        }

        if (addCookie == null) {
            addCookie = new Cookie("addCookie", "1");
        }
        response.addCookie(addCookie);

        Integer id;
        String firstName = null;
        String lastName = null;
        String dayOfBirth = null;
        Painter painter = null;
        try {

            firstName = request.getParameter("firstName");
            lastName = request.getParameter("lastName");
            dayOfBirth = request.getParameter("dayOfBirth");

            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
            } else {
                painter = new Painter();
                painter.setFirstName(firstName);
                painter.setLastName(lastName);
                painter.setDateOfBirth(date(dayOfBirth));

                model.createPainter(painter);
                listPainters(request, response);
            }

        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException("Problem with parsing data...");
        } catch (IOException ex) {
            Logger.getLogger(PainterControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method used to prepopulate the form when the Update action is invoked
     * (Searches the particualar Painter in DB by his ID)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private void loadPainter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            Integer painterId = null;
            painterId = Integer.parseInt(request.getParameter("painterId"));

            Painter painter = model.findPainterById(painterId);

            request.setAttribute("THE_PAINTER", painter);

            request.getRequestDispatcher("/update-painter-form.jsp").forward(request, response);
            // }
        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException(getClass().getSimpleName() + ">>Problem while loading attribute to populate");
        } catch (ServletException servletException) {
            throw new ServletException(getClass().getName());
        } catch (IOException iOException) {
            throw new IOException(getClass().getName() + "Problem while form prepopulating");
        }
    }

    /**
     * Method that takes data from form, that is later used to updated the selected Painter
     * @param request
     * @param response
     * @throws ServletException 
     */
    private void updatePainter(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        Integer id;
        String firstName = null;
        String lastName = null;
        String dayOfBirth = null;
        Painter painter = null;
        try {

            Cookie updateCookie = null;

            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie tempCookie : cookies) {
                    // Cookie name cannot be null or empty
                    if (tempCookie.getName().equals("updateCookie")) {
                        // Cookie values are only Strings
                        Integer count = Integer.parseInt(tempCookie.getValue());
                        count++;
                        tempCookie.setValue(Integer.toString(count));
                        updateCookie = tempCookie;
                        break;
                    }
                }
            }

            if (updateCookie == null) {
                updateCookie = new Cookie("updateCookie", "1");
            }
            response.addCookie(updateCookie);

            id = Integer.parseInt(request.getParameter("painterId"));
            firstName = request.getParameter("firstName");
            lastName = request.getParameter("lastName");
//           dayOfBirth = request.getParameter("dayOfBirth");
//            
//            request.setAttribute("PAINTER_TO_UPDATE", painterToUpdate);

            painter = model.findPainterById(id);
            request.setAttribute("PAINTER_TO_UPDATE", painter);

            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
                //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
            } else {
                painter.setFirstName(firstName);
                painter.setLastName(lastName);
                // painter.setDateOfBirth(date(dayOfBirth));

                model.updatePainter(painter);
                listPainters(request, response);
            }

        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException("Problem with parsing data...");
        } catch (IOException ex) {
            Logger.getLogger(PainterControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method that invokes the EJB module responsible for accesing objects in DB and then deletes the Painter sepecified by his ID
     * @param request
     * @param response 
     */
    private void deletePainter(HttpServletRequest request, HttpServletResponse response) {

        Integer painterId = null;
        try {

            Cookie delCookie = null;

            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie tempCookie : cookies) {
                    // Cookie name cannot be null or empty
                    if (tempCookie.getName().equals("delCookie")) {
                        // Cookie values are only Strings
                        Integer count = Integer.parseInt(tempCookie.getValue());
                        count++;
                        tempCookie.setValue(Integer.toString(count));
                        delCookie = tempCookie;
                        break;
                    }
                }
            }

            if (delCookie == null) {
                delCookie = new Cookie("delCookie", "1");
            }
            response.addCookie(delCookie);
            painterId = Integer.parseInt(request.getParameter("painterId"));

            model.deletePainter(painterId);

            listPainters(request, response);
        } catch (NumberFormatException numberFormatException) {
        } catch (ServletException servletException) {
        } catch (IOException iOException) {
        }
    }

}
