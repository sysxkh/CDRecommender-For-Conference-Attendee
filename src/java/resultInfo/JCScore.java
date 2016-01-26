package resultInfo;

public class JCScore 
{
	
	private String name;
	private double score;
	
	public JCScore(String name, double score)
	{
		this.setName(name);
		this.setScore(score);
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
