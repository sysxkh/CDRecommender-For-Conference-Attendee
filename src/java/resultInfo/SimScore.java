package resultInfo;

public class SimScore 
{
	private String affliations;
	private double score;
	
	public SimScore(String affliations, double score)
	{
		this.setAffliations(affliations);
		this.setScore(score);
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getAffliations() {
		return affliations;
	}

	public void setAffliations(String affliations) {
		this.affliations = affliations;
	}



}
