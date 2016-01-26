package application;

import resultInfo.Author;
import textHandler.StopwordsRemover;

public class RecommendationList_Conference
{
	private int authorid;
	private int cid;
	private StopwordsRemover swr;
	
	public RecommendationList_Conference(int authorid,int cid,StopwordsRemover swr)
	{
		this.authorid = authorid;
		this.cid = cid;
		this.swr = swr;
	}
	
	public Author[] GenerateList()
	{
		return LCScore.getScores(cid, authorid, swr);
	}

}
