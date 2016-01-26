/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author Kehao Xu
 */
public class getSession extends HttpServlet {

 


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
            throws ServletException, IOException 
    {
        HttpSession session = request.getSession();
        String result = "";
        JSONObject jo = new JSONObject();
        if(session.getAttribute("name")!=null && session.getAttribute("affiliation")!=null && session.getAttribute("cid")!=null)
        {
            jo.put("name", session.getAttribute("name"));
            jo.put("affiliation", session.getAttribute("affiliation"));
            jo.put("cid", session.getAttribute("cid"));
            result = jo.toString();
        }
        else
            result = "null";
        response.getWriter().write(result);
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
