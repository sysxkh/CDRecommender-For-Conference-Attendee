package resultInfo;

public class Author 
{
	private int id;
	private double score;
	private double simscore;
	private double jcscore;
	private String name;
	private String affiliations;
	
	public Author(int id)
	{
		this.setId(id);	
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSimscore() {
		return simscore;
	}

	public void setSimscore(double simscore) {
		this.simscore = simscore;
	}

	public double getJcscore() {
		return jcscore;
	}

	public void setJcscore(double jcscore) {
		this.jcscore = jcscore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}
	
	
	
}
