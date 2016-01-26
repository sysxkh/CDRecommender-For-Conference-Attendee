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
import textHandler.StopwordsRemover;

/**
 *
 * @author Kehao Xu
 */
public class getSimilarity extends HttpServlet {




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
       
        int id1=Integer.valueOf(request.getParameter("author1"));
        int id2=Integer.valueOf(request.getParameter("author2"));
        StopwordsRemover swr = null;
        //System.out.println(System.getProperty("user.dir"));
        String OS = System.getProperty("os.name").toLowerCase();
        String path = "";
        if(OS.indexOf("linux")>=0)
            path = "/home/kex3";
        if(OS.indexOf("windows")>=0)
            path = "C:\\study\\Java\\IndependentStudy";
        try 
        {
            swr = new StopwordsRemover((new FileInputStream(path+File.separator+"stop_words.txt")));
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        double result = application.Similarity.getSimilarity(id1, id2, swr);
        
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