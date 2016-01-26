package application;

public class Author 
{
	private int id;
	private double score;
	
	public Author(int id, double score)
	{
		this.setId(id);
		this.setScore(score);
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
	
	
	
}
