package textHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import resultInfo.Author;


public class fileHelper 
{
    public static String readRecord(String recordPath)
    {
            String record = "";
            File records = new File(recordPath+File.separator+"list.txt");
            try
            {
                if(!records.exists())
                {
                    records.createNewFile();
                    return "";
                }
                 BufferedReader r = new BufferedReader(new FileReader(records.getAbsolutePath())); 
                 String allRecord = "";
                 StringBuilder sb = new StringBuilder();
                 
                 if((allRecord=r.readLine())!=null && !allRecord.equals(" "))
                 {
                     r.close();
                     BufferedWriter w = new BufferedWriter(new FileWriter(records.getAbsolutePath()));
                     String[] allRecords = allRecord.split(" ");
                     record = allRecords[0];
                     if(allRecords.length==1)
                         sb.append("");
                     else
                     {
                         for(int i=1;i<allRecords.length;i++)
                         {
                             sb.append(allRecords[i]);
                             sb.append(" ");
                         }
                     }
                     w.write(sb.toString());
                     w.close();
                     String waitingPath = recordPath+File.separator+record;
                     File waiting = new File(waitingPath);
                     waiting.createNewFile();
                    
                 }
                 else
                 {
                     r.close();
                     return "";
                 }
                
            }catch(IOException e)
            {
                e.printStackTrace();
                return "";
            }
            return record;
    }
    
    public static boolean writeRecord(String cid,String authorid,String recordPath)
    {
        String record = cid+"@"+authorid+" ";
        String listPath = recordPath+File.separator+"list.txt";
        File list = new File(listPath);
        String waitingPath = recordPath+File.separator+record;
        File waiting = new File(waitingPath);
        
        try
        {
            if(!list.exists())
                list.createNewFile();
            if(!waiting.exists())
            {
                BufferedReader r = new BufferedReader(new FileReader(list.getAbsolutePath()));
                String allRecord = r.readLine();
                if(allRecord!=null)
                {
                    if(allRecord.contains(record))
                    {
                        r.close();
                        return true;
                    }
                    else
                    {
                        r.close();
                        BufferedWriter w = new BufferedWriter(new FileWriter(list.getAbsolutePath(),true));
                        w.write(record);
                        w.close();
                    }
                }
                else
                {
                    r.close();
                    BufferedWriter w = new BufferedWriter(new FileWriter(list.getAbsolutePath(),true));
                    w.write(record);
                    w.close();
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
            
        
        return true;
    }
    
    
    
    
/*				the data file is store the top 100 list for one author.
 * 				the file is store the data like :
 * 
 * 				authorid*score authorid*score authorid*score .........
 * 
 * 				the start and length of each author is stored in the index file
 * 				so the data file is using randomaccess and file channel to R/W
	*/
	public static boolean addData(File indexFile, File dataFile, List<Author> result,int authorid)
	{
		boolean isSuccessful = false;
		String index = "";
		StringBuilder data = new StringBuilder();
		
		for(int i=0;i<result.size();i++)
		{
			data.append(result.get(i).getId());
			data.append("*");
			data.append(result.get(i).getScore());
			data.append(" ");
		}	
		data.deleteCharAt(data.length()-1);
		try 
		{
			RandomAccessFile raf = new RandomAccessFile(dataFile.getAbsolutePath(),"rw");
			byte[] bdata = data.toString().getBytes();
			long length = bdata.length;
			long position = raf.length();
			raf.seek(raf.length());
			raf.write(bdata);
			index =authorid+" "+String.valueOf(position)+"*"+String.valueOf(length);
			isSuccessful = addIndex(indexFile,index);
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		
		return isSuccessful;
	}
	
	public static ArrayList<Author> readRecommandationList(File dataFile,String index)
	{
		index = index.split(" ")[1];
		ArrayList<Author> result = new ArrayList<Author>();
		int start = Integer.valueOf(index.split("\\*")[0]);
		int length = Integer.valueOf(index.split("\\*")[1]);
		Author temp = null;
		String readData = "";
		
		try 
		{
			RandomAccessFile RA =new RandomAccessFile(dataFile.getAbsolutePath(), "r");
//			FileChannel FC = RA.getChannel();
//			MappedByteBuffer out = FC.map(FileChannel.MapMode.READ_ONLY, start, length);  
//			for(int i=0)
//			byte[] bdata = new byte[out.];
//			readData = out.asCharBuffer().toString();
			byte[] bData = new byte[length];
			RA.seek(start);
			RA.readFully(bData, 0, length);;
			readData = new String(bData,"UTF-8");
			RA.close();
			//FC.close();
		} catch (IOException e) {
			// TODO � ��
			e.printStackTrace();
		}  
		String[] data = readData.split(" ");
		for(int i=0;i<data.length;i++)
		{
			temp = new Author(Integer.valueOf(data[i].split("\\*")[0]));
			temp.setScore(Double.valueOf(data[i].split("\\*")[1]));
			result.add(temp);
		}
		
		return result;
	}
/*	
	The index file is record the authorid and where do the recommendation list store in the data file
	the file is like : authorid1 thePosititonOfStart*theLengthOfRecord
					   authorid2 thePosititonOfStart*theLengthOfRecord
					   		...
					   		...
					   		...
					   		
	each author will be store in one line.
	
	*/
	public static String seekIndex(File indexFile ,int authorid)
	{
		String index = "";
		String line = "";
		String id = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(indexFile.getAbsolutePath()));
			while((line=reader.readLine())!=null)
			{
				id = line.split(" ")[0];
				if(!id.equals(String.valueOf(authorid)))
					continue;
				else
				{
					index = line;
					break;
				}
			}	
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return index;
	}
	
	public static boolean addIndex(File indexFile,String indexLine)
	{
		StringBuilder index = new StringBuilder();
		boolean isAdded = false;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try 
		{
			String line = "";
			reader = new BufferedReader(new FileReader(indexFile.getAbsolutePath()));
			String authorid = indexLine.split(" ")[0];
			while((line=reader.readLine())!=null)
			{

				if(line.split(" ")[0].equals(authorid))
				{
					index.append(indexLine);
					index.append("\n");
					isAdded = true;
				}
				else
				{
					index.append(line);
					index.append("\n");
				}
			}
			reader.close();
			
			writer = new BufferedWriter(new FileWriter(indexFile.getAbsolutePath())); 
			if(isAdded)
			{
				writer.write(index.toString());
				writer.close();
			}
			else
			{
				index.append(indexLine);
				index.append("\n");
				writer.write(index.toString());
				isAdded=true;
			}
			writer.close();
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		
		return isAdded;
	}

}
