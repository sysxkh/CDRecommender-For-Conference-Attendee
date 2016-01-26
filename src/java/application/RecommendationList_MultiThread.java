package application;
import java.util.Arrays;
import java.util.concurrent.Callable;

import resultInfo.Author;
import textHandler.StopwordsRemover;
	
	
	
public class RecommendationList_MultiThread implements Callable<Object> 
{  
	private int sid;
	private int eid;
	private int authorid;
	private int method;
	private int[] CN;
	private int top;
	private StopwordsRemover swr;
	public RecommendationList_MultiThread(int sid,int eid,int authorid,int method, int[] CN,int top,StopwordsRemover swr)
	{  
	   this.swr = swr;
	   this.top = top;
	   this.CN = CN;	
	   this.sid = sid;
	   this.eid = eid;
	   this.authorid = authorid;
	   this.method = method;
	}  
  
	public Object call() throws Exception 
	{
	   System.out.println("Start "+sid+" From "+eid);
	   Author[] result = LCScore.getScores(sid, eid, authorid, method, CN,swr);
	   Arrays.sort(result, new RecommendationList.result_comparator());
	   Author[]	results = new Author[top+1];
	   for(int i=0;i<top+1;i++)
		   results[i]=result[i];

	   System.out.println("end "+sid+" From "+eid);
	   return  results;  
	}  
}  
	

