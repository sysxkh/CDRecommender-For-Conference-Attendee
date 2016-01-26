package application;

import java.io.File;
import java.util.List;

import resultInfo.Author;
import textHandler.fileHelper;

public class RL_Reader_OLD 
{
	public static void main(String[] args)
	{
		if( args==null || args.length<2 )
		{
			System.out.println("Usage:");
			System.out.println("  args[0]: The Author's ID");
			System.out.println("  args[1]: The max return number");
			System.exit(0);
		}
		int authorid = Integer.valueOf(args[0]);
		int top = Integer.valueOf(args[1]);
		String path = System.getProperty("user.dir");
		File indexFile = new File(path+File.separator+"index.txt");
		File dataFile = new File(path+File.separator+"data.txt");

		if(!indexFile.exists() || !dataFile.exists())
		{
			System.out.println("Please check you file or run the RL_Writer to build the data");
			System.exit(0);
		}
		
		String index = fileHelper.seekIndex(indexFile, authorid);
		
		if(index.isEmpty())
		{
			System.out.println("This Author have not been cache now, Please check you file or run the RL_Writer to build the data");
			System.exit(0);
		}
		
		List<Author> results = fileHelper.readRecommandationList(dataFile, index).subList(0, top);
				

		System.out.println("The top "+top+" recommanded author for "+authorid+" is :");
		for(int i=0;i<top;i++)
		{		
			System.out.println((i+1)+" the Author "+results.get(i).getId()+" get the score: "+results.get(i).getScore());
		}
	}

}
