/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import application.RL_Reader;
import application.RL_Writer_Conference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import resultInfo.Author;

/**
 *
 * @author Kehao Xu
 */
public class getRecommandation extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
        String result = "";
        String authorid=request.getParameter("authorid");
        int top = Integer.valueOf( request.getParameter("top") );
        String cid = "134";

        String OS = System.getProperty("os.name").toLowerCase();
        String swrPath = "";
        boolean hasAuthor = false;
        String cacheDir = "";
        if(OS.contains("linux"))
        {
            swrPath = "/home/kex3";
            cacheDir = "/var/lib/tomcat7/webapps/AR/cache";
        }
        if(OS.contains("windows"))
        {
            swrPath = "C:\\study\\Java\\IndependentStudy";
            cacheDir = System.getProperty("user.dir");
        }
        
        
        String cachePath = cacheDir+File.separator+cid+"_"+authorid+".json";
        RL_Reader r = new RL_Reader(cachePath);
        
        boolean isExist = r.Result();
        
        if(isExist)
        {            
           // result = getHtml(r.getAL(),top);
        }
        else
        {
            swrPath = swrPath+File.separator+"stop_words.txt";
            RL_Writer_Conference w = new RL_Writer_Conference(Integer.valueOf(cid),Integer.valueOf(authorid),swrPath,cacheDir);
            try {
                hasAuthor=w.process();
            } catch ( ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            if(hasAuthor)
            {
//                if(r.Result())
//                    result = getHtml(r.getAL(),top);
//                else 
//                {
//                    result = "Cannot write cache at "+cachePath;
////                    try {
////                        result = getHtml(w.process_AL(),top);
////                    } catch ( ExecutionException e) {
////                        e.printStackTrace();
////                    } catch (InterruptedException e){
////                        e.printStackTrace();
////                    }                  
//                }
            }
            else
                result = "No Such Author";
                
        }
        
        result = result + " " + cachePath;
        response.getWriter().print(result);
    }
    public String getHtml(ArrayList<Author> list,int top)
    {
        top = top>list.size()?list.size():top;
        StringBuilder html = new StringBuilder();
        for(int i=0;i<top;i++)
        {
            html.append("<h3>");
            html.append(String.valueOf(i+1));
            html.append(". Author ID: ");
            html.append(String.valueOf(list.get(i).getId()));
            html.append(" Score: ");
            html.append(String.valueOf(list.get(i).getScore()));
            html.append("</h3>");
        }       
        return html.toString();
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
