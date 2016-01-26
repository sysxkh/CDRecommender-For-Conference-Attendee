/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import resultInfo.SScore;
import social.SocialInfo;
import social.SocialScore;
import textHandler.StopwordsRemover;

/**
 *
 * @author Kehao Xu
 */
public class cacheSocialInfo extends HttpServlet {



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
        int start = 0;
        if(request.getParameter("start")!=null)
            start = Integer.valueOf(request.getParameter("start"));
        String path = swrpath;
        File dir =new File(path+File.separator+"socialCache"+File.separator+cid);
        String[] info = dir.list();
        String result = "";

        
        for(int i=start;i<info.length;i++)
        {
            ArrayList<SScore> ssArray = new ArrayList<SScore>();
            String folderName = info[i];
            String name = folderName.split("@")[0];
            System.out.println(name);
            String folderDir = dir.getAbsolutePath()+File.separator+folderName;
            System.out.println("Start calculate in "+folderDir);
            SocialInfo si1 = new SocialInfo();
            if(!si1.getInfo(folderDir))
            {
                result=result+name+" ";
                System.out.println(name);
                continue;
            }
            for(int j=0;j<info.length;j++)
            {
                String otherName = info[j].split("@")[0];
                if(otherName.equals(name))
                    continue;
                System.out.println("     sub author name : "+otherName);
                String otherFolderDir = dir.getAbsolutePath()+File.separator+info[j];
                SocialInfo si2 = new SocialInfo();
                if(!si2.getInfo(otherFolderDir))
                {
                    result = result + otherName+"";
                    continue;
                }
                SocialScore ss = new SocialScore(si1,si2,swr);
                ss.setName(otherName);
                ssArray.add(ss.calcScore());
            }
            Collections.sort(ssArray, new result_comparator());
            JSONArray ja = new JSONArray();
            for(int k=0;k<ssArray.size();k++)
            {
                SScore temp = ssArray.get(k);
                JSONObject jo = new JSONObject();
               
                jo.put("Name", temp.getName());
                jo.put("GoogleScholarCS", temp.getGsCS());
                jo.put("ResearchGateCS", temp.getRgCS());
                jo.put("TwitterCS", temp.getTtCS());
                jo.put("WikiCS", temp.getWkCS());
                jo.put("NormalCS", temp.getNmCS());
                jo.put("GoogleScholarJC", temp.getGsJC());
                jo.put("ResearchGateJC", temp.getRgJC());
                jo.put("TwitterJC", temp.getTtJC());
                jo.put("TotalCS", temp.getTotalCS());
                jo.put("TotalJC", temp.getTotalJC());
                jo.put("Total", temp.getTotal());
                ja.put(jo);
            }
            File list = new File(folderDir+File.separator+"RecommendationList.json");
            if(!list.exists())
                list.createNewFile();
            BufferedWriter w = new BufferedWriter(new FileWriter(list.getAbsolutePath()));
            w.write(ja.toString());
            w.close();
        }
         response.getWriter().print(result);
    }
    static class result_comparator implements Comparator<SScore> //a comparator to sort
    {

            @Override
            public int compare(SScore a1, SScore a2) {
                    if(a1.getTotal()>a2.getTotal())
                            return -1;
                    else if(a1.getTotal()<a2.getTotal())
                            return 1;
                    else
                            return 0;
            }

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
