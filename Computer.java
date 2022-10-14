import java.util.*;

public class Computer extends Player {
	private boolean isGood;

	public Computer(boolean g, String n, int c, ArrayList h, int b, boolean w) {
		super(n, c, h, b, w);
		isGood = g;
	}

	public Computer(String x) {
		super(x);
	}

	public String getName() {
		return super.getName();
	}

	public int getNumCards() {
		return super.getNumCards();
	}

	public ArrayList getHand() {
		return super.getHand();
	}

	public void setHand(ArrayList newHand) {
		super.setHand(newHand);
	}

	public void setBook() {
		super.setBook();
	}

	public int getBook() {
		return super.getBook();
	}

	public String toString() {
		return super.toString() + " and " + getName() + " says hi too.";
	}

	public boolean equals(Computer pc) {
		return getName().equals(pc.getName());
	}

	public boolean getWin()
	{
		return super.getWin();
	}

	public void setWin()
	{
		super.setWin();
	}
}