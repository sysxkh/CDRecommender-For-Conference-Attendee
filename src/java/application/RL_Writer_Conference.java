package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Arrays;


import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import resultInfo.Author;
import textHandler.StopwordsRemover;

public class RL_Writer_Conference
{
	private int cid;
	private int authorid;
	private String swPath;
	private String path;
	
	public RL_Writer_Conference(int cid,int authorid,String swPath,String path)
	{
		this.cid = cid;
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

		RecommendationList_Conference RLC = new RecommendationList_Conference(authorid,cid,swr);
		Author[] results = RLC.GenerateList();
		
		Arrays.sort(results, new RecommendationList.result_comparator());
		Author[] authors = null;
		if(results.length>maxStore)
			authors = Arrays.copyOfRange(results, 1, maxStore+1);
		else
			authors = Arrays.copyOfRange(results,1, results.length);
		
		File jsonFile = new File(path+File.separator+cid+"_"+authorid+".json");

		try 
		{
			jsonFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject jo = null;
		JSONArray ja = new JSONArray();
                DecimalFormat df = new DecimalFormat("#0.0000");
		for(int i=0;i<authors.length;i++)
		{
                        String score = String.valueOf(df.format(authors[i].getScore()));
                        String simscore = String.valueOf(df.format(authors[i].getSimscore()));
                        String jcscore = String.valueOf(df.format(authors[i].getJcscore()));
			jo = new JSONObject();
                        jo.put("name", authors[i].getName());
			jo.put("id", authors[i].getId());
			jo.put("score", score);	
                        jo.put("simscore", simscore);
			jo.put("jcscore", jcscore);
			jo.put("affiliations", authors[i].getAffiliations());
			ja.put(jo);
		}
		try 
		{
                        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(jsonFile), "UTF-8"); 
			writer.write(ja.toString());
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		} 
		
		return true;
	}
}
