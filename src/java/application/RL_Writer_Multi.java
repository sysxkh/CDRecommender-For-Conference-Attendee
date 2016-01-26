package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

import resultInfo.Author;
import textHandler.StopwordsRemover;

public class RL_Writer_Multi 
{
	private int authorid;
	private String swPath;
	private String path;
	
	public RL_Writer_Multi(int authorid,String swPath,String path)
	{
		this.swPath = swPath;
		this.path = path;
		this.authorid=authorid;
	}
	public boolean process() throws InterruptedException, ExecutionException
	{
		StopwordsRemover swr = null;
		int maxStore = 100;
		try {
			swr = new StopwordsRemover((new FileInputStream(swPath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int sid = 1;
		int eid = 1712433;
		int method = 1;

		int top = 100;
		int[] CN = CommonNeighbor.getHasCNList(sid, eid, authorid);
		int size = 200000;
		int taskSize = (eid-sid)/size;  
		Callable<Object> c = null;
		Future<Object> f = null;
		ArrayList<Author> results = new ArrayList<Author>();
		// create a thread pool  
		
		ExecutorService pool = Executors.newFixedThreadPool(taskSize);    
		List<Future<Object>> resultlist = new ArrayList<Future<Object>>(); 
		
		for (int i = 0; i < taskSize; i++) 
		{  
			c = new RecommendationList_MultiThread(i*size,(i+1)*size-1,authorid,method,CN,top,swr);  
			f = pool.submit(c);  
			resultlist.add(f);  
		}
		
		c = new RecommendationList_MultiThread(taskSize*size,eid,authorid,method,CN,top,swr);
		f = pool.submit(c);  
		resultlist.add(f);

		pool.shutdown();  
  
    
		for (Future<Object> fr : resultlist) 
		{  
			Author[] result = (Author[]) fr.get();
			for(int i=0;i<result.length;i++)
				if(result[i].getId()!=authorid)
					results.add(result[i]);
				else
					continue;
		}
		
		Collections.sort(results, new RecommendationList.result_comparator());
		List<Author> authors = results.subList(0,maxStore);
		
		File jsonFile = new File(path+File.separator+authorid+".json");

		try 
		{
			jsonFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject jo = null;
		JSONArray ja = new JSONArray();
		for(int i=0;i<authors.size();i++)
		{
			jo = new JSONObject();
			jo.put("id", authors.get(i).getId());
			jo.put("score", authors.get(i).getScore());
			ja.put(jo);
		}
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile.getAbsolutePath()));
			writer.write(ja.toString());
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		} 
		
		return true;
	}
}
