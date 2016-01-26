package test;


//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.util.ArrayList;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Arrays;
import java.util.Comparator;
//import java.util.Hashtable;
//import java.util.Iterator;
//import java.util.TreeMap;







import java.util.concurrent.ExecutionException;

import resultInfo.Author;
//import textHandler.StopwordsRemover;
//import textHandler.fileHelper;
import application.RL_Reader;
import application.RL_Writer_Conference;
//import application.RL_Writer_Multi;
//import application.CommonNeighbor;
//import application.JaccardCoefficient;
//import application.Similarity;
//import dbHelper.ExcuteSQL;


public class Test 
{
	public static void main(String[] args)
	{
		double start = System.nanoTime();
		boolean hasAuthor = false;
		RL_Writer_Conference w = new RL_Writer_Conference(134,689432,"C:\\study\\Java\\IndependentStudy\\stop_words.txt","C:\\study\\Java\\IndependentStudy");
		try {
			hasAuthor=w.process();
		} catch ( ExecutionException e) {
		
			e.printStackTrace();
		} catch (InterruptedException e){
                    e.printStackTrace();
                }
		if(hasAuthor)
		{
			RL_Reader r = new RL_Reader("C:\\study\\Java\\IndependentStudy\\689432.json");
			r.Result();
	
		}
		double end = System.nanoTime();
		System.out.println((end-start)/1000000000);
	}
	
	static class result_comparator implements Comparator<Author> //a comparator to sort
	{

		@Override
		public int compare(Author a1, Author a2) {
			if(a1.getScore()>a2.getScore())
				return -1;
			else if(a1.getScore()<a2.getScore())
				return 1;
			else
				return 0;
		}
	
	}
}
