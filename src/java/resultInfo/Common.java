package resultInfo;

import java.util.ArrayList;

public class Common 
{
	private int id;
	private ArrayList<Integer> commonList;
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Integer> getCommonList() {
		return commonList;
	}
	public void setCommonList(ArrayList<Integer> commonList) {
		this.commonList = commonList;
	}
	
	public Common(int id,String name)
	{
		this.name = name;
		this.id = id;
		this.commonList = new ArrayList<Integer>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
