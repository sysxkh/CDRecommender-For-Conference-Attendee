/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;



/**
 *
 * @author Kehao Xu
 */
public class getSocialRecommendation extends HttpServlet {

   



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
        String name=request.getParameter("name").replace("%20", " ").replace("+", " ");
        String cid = request.getParameter("cid");
        
        String OS = System.getProperty("os.name").toLowerCase();
        String path = "";
        if(OS.indexOf("linux")>=0)
            path = "/home/kex3";
        if(OS.indexOf("windows")>=0)
            path = "C:\\study";
      
        File dir = null;
        if(name.contains("@"))
            dir =new File(path+File.separator+"socialCache"+File.separator+cid+File.separator+name);
        else
        {
            File conference = new File(path+File.separator+"socialCache"+File.separator+cid);
            
            String[] list = conference.list();
            for(int i=0;i<list.length;i++)
            {
                if(list[i].contains(name))
                    dir=new File(conference.getAbsolutePath()+File.separator+list[i]);
                else
                    continue;
            }
        }
 //       System.out.println(dir.toString());
        
//        if(dir==null)
//            dir = new File(path+File.separator+"socialCache"+File.separator+cid+File.separator+"nosuchfile");
        String result = "";
        if(dir.exists())
        {
            File rlJSON = new File(dir.getAbsolutePath()+File.separator+"RecommendationList.json");
            if(rlJSON.exists())
            {
                BufferedReader r = new BufferedReader(new FileReader(rlJSON.getAbsolutePath())); 
                String line = "";
                 JSONArray ja = new JSONArray();
                 JSONArray temp = new JSONArray();
                if((line = r.readLine())!=null)
                {   
                //    result = line;
                    temp = new JSONArray(line);
                   
                    for(int i=0;i<temp.length();i++)
                    {
                        JSONObject tempJO = temp.getJSONObject(i);
                        JSONObject jo = new JSONObject();
                        
                        @SuppressWarnings("unchecked")
                        Iterator<String> keys =  tempJO.keys();
                        while(keys.hasNext())
                        {
                            
                            String key = keys.next();
                            String value = "";
                            if(key.equals("Name"))
                            {
                                value = tempJO.getString(key); 
                                key = key.toLowerCase();
                                jo.put(key,value);
                                continue;
                            }
                            else
                            {
                                value =String.valueOf( tempJO.getDouble(key) * 100 );
                                if(!value.endsWith("0"))
                                {
                                    if(value.length()>4)
                                    {
                                        value = value.substring(0, 4) ;
                                    }
                                    else
                                    {
                                        value = value.substring(0, value.length()) ;
                                    }
                                }
//                                else
//                                    value = value+"%";
                                jo.put(key, Double.valueOf(value));
                            }

                                                           
                        }
                        ja.put(jo);
//                        jo.put("name", tempJO.getString("Name"));
//                        jo.put("googlescholarcs", tempJO.getDouble("GoogleScholarCS"));
                    }
                    result = ja.toString();
                }
                r.close();
               
            }
       
        }
        response.setContentType("text/html; charset=UTF-8");
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
