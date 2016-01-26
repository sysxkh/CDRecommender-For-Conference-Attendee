/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Kehao Xu
 */
public class getAuthorsList extends HttpServlet {



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
        response.setContentType("text/html; charset=UTF-8");

        String OS = System.getProperty("os.name").toLowerCase();
        String path = "";
        if(OS.indexOf("linux")>=0)
            path = "/home/kex3";
        if(OS.indexOf("windows")>=0)
            path = "C:\\study";
       
        
        String cid = request.getParameter("cid");
        File dir =new File(path+File.separator+"socialCache"+File.separator+cid);
        String result = "";
        if(!dir.exists())
            result = "No result";
        else
            result = getHTML(dir,cid);
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().print(result);
    }
    
    private String getHTML(File dir,String cid)
    {
        
        StringBuilder sb = new StringBuilder();
        sb.append("<tr><th>The Name:</th> <th>Affiliations:</th> <th>Recommendation</th></tr>");
        String[] info = dir.list();
        for(int i=0;i<info.length;i++)
        {
            sb.append("<tr><td>");
            String[] na = info[i].split("@"); 
            String encodeName = "";
            try
            {
                encodeName = java.net.URLEncoder.encode(na[0], "UTF-8");
            }catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            sb.append("<a href=\"DashBoard.html?cid=");
            sb.append(cid);
            sb.append("&name=");
            sb.append(encodeName);
            sb.append("\" target=\"_blank\">");
            sb.append(na[0]);
            sb.append("</a>");
            sb.append("</td><td>");
            if(na.length>1)
                sb.append(na[1]);
            sb.append("</td><td>");
            sb.append("<a href=\"Recommendation.html?cid=");
            sb.append(cid);
            sb.append("&name=");
            sb.append(encodeName);
            sb.append("\" target=\"_blank\">");
            sb.append("List");
            sb.append("</a>");
            sb.append("</td></tr>");
        }
        
        return sb.toString();
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
