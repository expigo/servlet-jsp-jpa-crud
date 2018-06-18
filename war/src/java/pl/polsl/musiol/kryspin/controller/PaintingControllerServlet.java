/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.musiol.kryspin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.musiol.kryspin.ejb.ModelFacadeBean;
import pl.polsl.musiol.kryspin.ejb.ModelFacadeBeanLocal;
import pl.polsl.musiol.kryspin.model.Painter;
import pl.polsl.musiol.kryspin.model.Painting;

/**
 * Servlet for processing actions on Painting entiy.
 *
 * @author Kryspin Musiol
 *
 * @version 1.0
 */
@WebServlet
public class PaintingControllerServlet extends HttpServlet {
    
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
            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet PaintingControllerServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet PaintingControllerServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");

            try {

                String action = request.getParameter("action");

                List<Painter> painters = model.getAllPainters();

                
                System.out.println(painters);

                if (action == null) {
                    action = "LIST";
                }
                switch (action) {
                    case "LIST":
                        listPaintings(request, response);
                        break;
                    case "ADD":
                        addPainting(request, response);
                        break;
                    case "LOAD":
                        loadPainting(request, response);
                        break;
                    case "UPDATE":
                        updatePainting(request, response);
                        break;
                    case "DEL":
                        deletePainting(request, response);
                        break;
                    default:
                        listPaintings(request, response);
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
     * Method used for listing all Paintings stored in the DB
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void listPaintings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        List<Painting> paintings = model.getAllPaintings();
        request.setAttribute("PAINTING_LIST", paintings);

        List<Painter> painters = model.getAllPainters();
        //request.setAttribute("PAINTER_LIST", painters.toString());

        session.setAttribute("PAINTER_LIST", painters);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/list_paintings.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Method that sends a Painting entity to the EJB module responsible for
     * persisting it to the DB (data defining the new Painting is taken from
     * form at JSP page)
     *
     * @param request
     * @param response
     * @throws NumberFormatException
     * @throws ServletException
     * @throws IOException
     */
    private void addPainting(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ServletException, IOException {

        Cookie aCookie = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie tempCookie : cookies) {
                // Cookie name cannot be null or empty
                if (tempCookie.getName().equals("aCookie")) {
                    // Cookie values are only Strings
                    Integer count = Integer.parseInt(tempCookie.getValue());
                    count++;
                    tempCookie.setValue(Integer.toString(count));
                    aCookie = tempCookie;
                    break;
                }
            }
        }

        if (aCookie == null) {
            aCookie = new Cookie("aCookie", "1");
        }
        response.addCookie(aCookie);

        int id;
        String title = null;
        Painting painting = null;
        try {

            title = request.getParameter("title");
            id = Integer.parseInt(request.getParameter("painter"));

            if (title == null || title.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
            } else {
                painting = new Painting();
                painting.setTitle(title);
                Integer newPaintingId = model.createPainting(painting);
                model.assignPaintingToPainter(id, newPaintingId);
                listPaintings(request, response);
            }

        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException("Problem with parsing data...");
        }

    }

    /**
     *
     ** Method used to prepopulate the form when the Update action is invoked
     * (Searches the particualar Painting in DB by its ID)
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void loadPainting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Integer paintingId = null;
            paintingId = Integer.parseInt(request.getParameter("paintingId"));
            System.out.println("paintingId attr: " + paintingId);
            //if (paintingId != null) {
            Painting painting = model.findPaintingById(paintingId);

            // attribute to prepopulate painter drop down list (option selected="...")
            request.setAttribute("selectedPainter", painting.getPainter().getId());

            request.setAttribute("THE_PAINTING", painting);

            request.getRequestDispatcher("/update-painting-form.jsp").forward(request, response);
            // }
        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException(getClass().getSimpleName() + ">>Problem while loading attribute to populate");
        } catch (ServletException servletException) {
            throw new ServletException(getClass().getName());
        } catch (IOException iOException) {
            throw new IOException(getClass().getName() + "Problem while prepopulating data");
        }
    }

    /**
     * Method that takes data from form, that is later used to updated the
     * selected Painting
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void updatePainting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie uCookie = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie tempCookie : cookies) {
                // Cookie name cannot be null or empty
                if (tempCookie.getName().equals("uCookie")) {
                    // Cookie values are only Strings
                    Integer count = Integer.parseInt(tempCookie.getValue());
                    count++;
                    tempCookie.setValue(Integer.toString(count));
                    uCookie = tempCookie;
                    break;
                }
            }
        }

        if (uCookie == null) {
            uCookie = new Cookie("uCookie", "1");
        }
        response.addCookie(uCookie);

        try {
            Integer id = null;
            id = Integer.parseInt(request.getParameter("paintingId"));
            String title = request.getParameter("title");

            Integer painterId = null;
            painterId = Integer.parseInt(request.getParameter("painter"));

            Painter painter = model.findPainterById(painterId);
            Painting paintingtoBeUpdated = model.findPaintingById(id);

            if (paintingtoBeUpdated != null && painter != null) {
                paintingtoBeUpdated.setTitle(title);
                paintingtoBeUpdated.setPainter(painter);

                request.setAttribute("paintingFound", paintingtoBeUpdated);
                model.updatePainting(paintingtoBeUpdated);

                listPaintings(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Painter or painting not found");
            }

        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException(getClass().getSimpleName() + ">>Problem while parsing attribute");
        }

    }

    /**
     * Method that invokes the EJB module responsible for accesing objects in DB
     * which then deletes the Painting sepecified by its ID
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deletePainting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie dCookie = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie tempCookie : cookies) {
                // Cookie name cannot be null or empty
                if (tempCookie.getName().equals("dCookie")) {
                    // Cookie values are only Strings
                    Integer count = Integer.parseInt(tempCookie.getValue());
                    count++;
                    tempCookie.setValue(Integer.toString(count));
                    dCookie = tempCookie;
                    break;
                }
            }
        }

        if (dCookie == null) {
            dCookie = new Cookie("dCookie", "1");
        }
        response.addCookie(dCookie);

        Integer paintingId = null;
        paintingId = Integer.parseInt(request.getParameter("paintingId"));
        System.out.println("paintingId attr: " + paintingId);

        Painting painting = model.findPaintingById(paintingId);

        model.deletePainting(paintingId);

        request.setAttribute("THE_PAINTING_DELETED", painting);

        listPaintings(request, response);
    }

}
