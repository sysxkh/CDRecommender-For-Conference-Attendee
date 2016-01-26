/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import application.RL_Writer_Conference;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import textHandler.fileHelper;

/**
 *
 * @author Kehao Xu
 */
public class updateFromRecord extends HttpServlet {

  


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
            String result = "";
            String OS = System.getProperty("os.name").toLowerCase();
            String swrPath = "";
            String cacheDir = "";
            if(OS.contains("linux"))
            {
                swrPath = "/home/kex3";
                cacheDir = "/var/lib/tomcat7/webapps/AR/cache";
            }
            if(OS.contains("windows"))
            {
                swrPath = "C:\\study\\Java\\IndependentStudy";
                cacheDir = "C:\\study\\AR\\web\\cache";
            }
            
            while(true)
            {
                String record = fileHelper.readRecord(cacheDir+File.separator+"waitingList");
                if(!record.isEmpty() && !record.equals(" "))
                {
 
                    swrPath = swrPath+File.separator+"stop_words.txt";

                    boolean success = false;
                    String cid = record.split("@")[0];
                    String authorid = record.split("@")[1];
                    RL_Writer_Conference w = new RL_Writer_Conference(Integer.valueOf(cid),Integer.valueOf(authorid),swrPath,cacheDir);
                    try {
                        success=w.process();
                    } catch ( ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    String waitingPath = cacheDir+File.separator+"waitingList"+File.separator+record;
                    File waiting = new File(waitingPath);
                    waiting.delete();
                    if(success)
                    {
                        result = result +authorid+ "success ";
                    }
                    else
                        result = result + authorid+"fail ";
                    
                }
                else
                {
                    if(result.equals(""))
                        result = "NoRecord";
                    else
                        result = result+"Done";
                    break;
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
