package application;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.json.*;

import resultInfo.Author;

public class RL_Reader 
{
	private String result;
	private String path;
	
	public RL_Reader(String path)
	{
		this.path=path;
	}
	
	public String getString()
	{
		return result;
	}
	


	
	
	public boolean Result()
	{
                File datafile = new File(path);
		if(path.contains("json") && datafile.exists())
		{
			RandomAccessFile RA = null;
			byte[] bData = null;
			String jsonData = "";
			try {
				RA = new RandomAccessFile(path, "r");
				int length =(int) RA.length();
				bData = new byte[length];
				RA.readFully(bData, 0, length);
				jsonData = new String(bData,"UTF-8");
				RA.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.result = jsonData;	
			return true;
		}
		else
			return false;
	}

}
