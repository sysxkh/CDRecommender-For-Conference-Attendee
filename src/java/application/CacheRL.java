package application;

import java.util.concurrent.ExecutionException;

public class CacheRL extends Thread
{
	private int[] authorlist;
	private int cid;
	
	public CacheRL(int[] authorlist,int cid)
	{
		this.setAuthorlist(authorlist);
		this.setCid(cid);
	}
	
	
	public void run()
	{
            String OS = System.getProperty("os.name").toLowerCase();
            String swPath = "";
            String Path = "";
            if(OS.indexOf("linux")>=0)
            {
                 swPath = "/home/kex3/stop_words.txt";
                 Path = "/var/lib/tomcat7/webapps/AR/cache";
            }
            if(OS.indexOf("windows")>=0)
            {
                 swPath = "C:\\study\\Java\\IndependentStudy\\stop_words.txt";
                 Path = "C:\\study\\Java\\IndependentStudy\\cache";
            }
		

		for(int i=0;i<authorlist.length;i++)
		{
			RL_Writer_Conference w = new RL_Writer_Conference(cid,authorlist[i],swPath,Path);
			try {
				w.process();
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			} catch (ExecutionException e) {
				
				e.printStackTrace();
			}
		}
	}


	public int[] getAuthorlist() {
		return authorlist;
	}


	public void setAuthorlist(int[] authorlist) {
		this.authorlist = authorlist;
	}


	public int getCid() {
		return cid;
	}


	public void setCid(int cid) {
		this.cid = cid;
	}

}
