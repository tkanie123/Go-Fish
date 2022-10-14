import java.util.*;

public class Player 
{
	private int cardsNum;
	private String playerName;
	private ArrayList hand;
	private int book;
	private String tempName;
	private boolean won;

	public Player(String n, int c, ArrayList h, int b, boolean w) {
		playerName = n;
		cardsNum = c;
		hand = h;
		book = b;
		won = w;
	}

	public Player(String x) {
		tempName = x;
	}

	public String getName() {
		return playerName;
	}

	public int getNumCards() {
		return cardsNum;
	}

	public ArrayList getHand() {
		return hand;
	}

	public void setHand(ArrayList newHand) {
		Collections.sort(newHand);
		hand = newHand;
	}

	public void setBook() {
		book++;
	}

	public int getBook() {
		return book;
	}

	public String toString()
	{
	        return "You say hi";
	}

	public boolean equals(Player play) {
		return playerName.equals(play.playerName);
	}

	public boolean getWin()
	{
		return won; 
	}

	public void setWin()
	{
		won = true;
	}
}
