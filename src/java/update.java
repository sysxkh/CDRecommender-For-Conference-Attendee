/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import application.CacheRL;
import dbHelper.ExcuteSQL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Kehao Xu
 */
public class update extends HttpServlet {




    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
               
                
                String conf_id = request.getParameter("cid");
                String tn=request.getParameter("tn");
                int cid = Integer.valueOf(conf_id);
                int threadNumber = Integer.valueOf(tn);
                String result = "";
		String sql = "Select aminer_id from Map where conf_id="+conf_id;
		int[] authorlist = ExcuteSQL.ExcuteSelect_IA(sql, 1);
		ArrayList<Integer> dAuthorList =  getDistinct(authorlist);
		ExecutorService exe = Executors.newFixedThreadPool(threadNumber+1); 
		int size = dAuthorList.size()/threadNumber;
		for(int i=0;i<threadNumber;i++)
		{
			int[] temp = new int[size];
			for(int j=0;j<size;j++)
			{
				temp[j] = dAuthorList.remove(0);
			}
			exe.execute(new CacheRL(temp,cid));
		}
		if(!dAuthorList.isEmpty())
		{
			int [] temp = new int[dAuthorList.size()];
			for(int j=0;j<temp.length;j++)
			{
				temp[j] = dAuthorList.remove(0);
			}
			exe.execute(new CacheRL(temp,cid));
		}
                exe.shutdown();
                while (true) 
                {  
                    if (exe.isTerminated()) 
                    {  
                        result = "success";  
                        break;  
                    }  
                    try{
                        Thread.sleep(200);
                    }catch(InterruptedException e)
                    {
                        result = e.toString();
                    }                
                }
                
                 response.getWriter().print(result);
                
    }

 	public static ArrayList<Integer> getDistinct(int[] al)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<al.length;i++)
		{
			if(list.contains(al[i]) || al[i]==0)
				continue;
			else
				list.add(al[i]);
		}
		return list;
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
