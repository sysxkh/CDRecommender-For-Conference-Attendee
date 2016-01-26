package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import resultInfo.Author;
import textHandler.StopwordsRemover;
import textHandler.fileHelper;


public class RL_Writer_OLD 
	
{
	public static void main(String[] args) throws ExecutionException,  InterruptedException 
	{  
		
		
		if( args==null /*|| args.length<1*/ )
		{
			System.out.println("Usage:");
			System.out.println("  args[0]: The recommanding Author's ID");
			System.out.println("  args[1]: The calculate method [1-2]");
			//System.out.println("  args[4]: The max return number [10-100]");
			System.exit(0);
		}
		double start = System.nanoTime();
		StopwordsRemover swr = null;
		String path = System.getProperty("user.dir");
		try {
			swr = new StopwordsRemover((new FileInputStream(path+File.separator+"stop_words.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//int sid = Integer.valueOf(args[0]);
		int sid = 1;
		//int eid = Integer.valueOf(args[1]);
		int eid = 1712433;
		int authorid = Integer.valueOf(args[0]);
		//int method = Integer.valueOf(args[1]);
		int method = 1;
		//int top = Integer.valueOf(args[4]);
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
		
		System.out.println("Start make multithreads !");
		
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
		

		File indexFile = new File(path+File.separator+"index.txt");
		File dataFile = new File(path+File.separator+"data.txt");

		try {
			if(!indexFile.exists())
				indexFile.createNewFile();
			if(!dataFile.exists())
				dataFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		boolean isSuccess = fileHelper.addData(indexFile,dataFile,results.subList(0, 100),authorid);
		
		System.out.println(isSuccess);
		
		double end = System.nanoTime();
		System.out.println("total time is : " + (end-start)/1000000000);
	 
	}  
	
	

}
