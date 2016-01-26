/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import social.SocialInfo;
import textHandler.StopwordsRemover;

/**
 *
 * @author Kehao Xu
 */
public class cacheSearchResult extends HttpServlet {


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
            throws ServletException, IOException 
    {
        StopwordsRemover swr = null;
        String OS = System.getProperty("os.name").toLowerCase();
        String swrpath = "";
        if(OS.indexOf("linux")>=0)
            swrpath = "/home/kex3";
        if(OS.indexOf("windows")>=0)
            swrpath = "C:\\study";
        try 
        {
            swr = new StopwordsRemover((new FileInputStream(swrpath+File.separator+"stop_words.txt")));
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        String cid = request.getParameter("cid");
        String result = "";
        String path = swrpath;
        File dir =new File(path+File.separator+"socialCache"+File.separator+cid);
        String[] info = dir.list();

        
        if(request.getParameter("start")!=null)
        {
            int start = Integer.valueOf(request.getParameter("start"));
            
            if(start>=info.length)
                result = "Out of boundary";
            else
            {
                for(int i=start;i<info.length;i++)
                {
                    String folderName = info[i];
                    String name = folderName.split("@")[0];
                    System.out.println(name);
                    String folderDir = dir.getAbsolutePath()+File.separator+folderName;
                    System.out.println(folderDir);
                    SocialInfo si = new SocialInfo(name,0,folderDir);
                    if(!si.getInfo())
                    {
                        result=result+name+"info ";
                        System.out.println(name);
                        continue;
                    }
                    if(!si.cache())
                    {
                        result=result+name+"cache ";
                        System.out.println(name);
                        continue;
                    }
                }
            }
        }
        if(request.getParameter("index")!=null)
        {
            int index =Integer.valueOf(request.getParameter("index")) ;
             if(index>=info.length)
                result = "Out of boundary";
            else
            {

                    String folderName = info[index];
                    String name = folderName.split("@")[0];
                    System.out.println(name);
                    String folderDir = dir.getAbsolutePath()+File.separator+folderName;
                    System.out.println(folderDir);
                    SocialInfo si = new SocialInfo(name,0,folderDir);
                    if(!si.getInfo())
                    {
                        result=result+name+"info ";
                        System.out.println(name);
                        
                    }
                    if(!si.cache())
                    {
                        result=result+name+"cache ";
                        System.out.println(name);
                       
                    }
                
            }
        }
 
        response.getWriter().print(result);
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
