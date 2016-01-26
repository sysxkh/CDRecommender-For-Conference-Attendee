package resultInfo;

public class Content 
{
	private int id;
	private String content;
	private String affiliations;
	
	public Content(int id, String content,String affiliations)
	{
		this.setId(id);
		this.setContent(content);
		this.setAffiliations(affiliations);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}

}
