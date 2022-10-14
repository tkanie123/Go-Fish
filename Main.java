/**
* Created by: Tiffanie Ku
* This program simulates Go Fish
*/
import java.util.*;

public class Main {
	public static void main(String[] args) {
		System.out.println("Welcome to Go Fish!");

		System.out.println("What is your name?");
		Scanner nameScan = new Scanner(System.in);
		String playerName = nameScan.nextLine();

		String rules;
		Scanner ruleScanner = new Scanner(System.in);
		do 
		{
			System.out.println("\nWould you like to view the rules? (yes/no)");
			rules = ruleScanner.nextLine();
		} 
		while (!(rules.equals("yes") || rules.equals("no")));
		
		if(rules.equals("yes"))
		{
			System.out.println("Start: You and your opponent will be dealt 7 cards each; remaining cards will be the draw pile." + "\n1) During your turn, ask the opponent for a number based on the cards in your hand." + 
			"\n2) If the opponent does not have the cards, go fish! Draw a card from the draw pile. If the opponent does have the card, he/she will give you all cards of that value. " + 
			"\n3) Your goal is to create sets of four of the same number cards (called books). When either you or your opponent have no more cards at hand, or the draw pile has no cards, the player with the most books wins!\n");

			String start;
			Scanner startScanner = new Scanner(System.in);
			do 
			{
				System.out.println("\nType 'start' to begin!");
				start = startScanner.nextLine();
			} 
			while (!(start.equals("start")));
		}

		String[] sym = {"♦", "♣", "♥", "♠"};
		String[] num = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

		ArrayList<String> cards = new ArrayList<String>();

		for(String i : sym)
		{
			for(String j : num)
			{
				cards.add(j+i);	
			}
		}	

		/** 
		* Initializes the starting hand with 7 cards.
		*
		* @cards.size() : ensures that the code does not consider used cards 
		*/

		ArrayList<String> playerCards = new ArrayList<String>();
		for(int i = 0; i < 7; i++)
		{
			int index = (int)(Math.floor(Math.random()*(cards.size())));
			playerCards.add(cards.get(index));
			cards.remove(index);
		}

		ArrayList<String> compCards = new ArrayList<String>();
		for(int i = 0; i < 7; i++)
		{
			int index = (int)(Math.floor(Math.random()*(cards.size())));
			compCards.add(cards.get(index));
			cards.remove(index);
		}

		Collections.sort(playerCards);
		Collections.sort(compCards);

		Player player = new Player(playerName, 7, playerCards, 0, false);
		Computer computer = new Computer(true, "Computer", 7, compCards, 0, false);

		/* For the sake of polymorphism*/
		Player theAllMighty = new Computer(true, "The Divine", 0, cards, 0, true);

		System.out.println("Your hand: " + player.getHand());

		/* Removes all books in both players' hands */
		dupes(player, computer, cards, playerCards, true);
		dupes(player, computer, cards,  compCards, false);

		double randomTurn = Math.random();
		boolean randTurn = true;
		if(randomTurn > 0.5)
		{
			randTurn = true;
		} else {
			randTurn = false;
		}

		if(randTurn == true)
		{
			playerTurn(player, computer, playerCards, compCards, cards);
		} else {
			computerTurn(player, computer, playerCards, compCards, cards);
		}
	}

	public static void playerTurn(Player player, Computer computer, ArrayList playerCards, ArrayList compCards, ArrayList cards)
	{
		if(playerCards.size() == 0 || cards.size() == 0)
		{
			if(playerCards.size() == 0)
			{
				System.out.println("You have no more cards in your hand!");
			} else if(cards.size() == 0) {
				System.out.println("There are no more cards in the draw pile!");
			}
			winCondition(player, computer);
			
		} else {
			ArrayList<String> copyOfCompCards = new ArrayList<String>(compCards);

			System.out.println("");
			try 
			{
				for(int i = 0; i < 5; i++)
				{
					Thread.sleep(200);
					System.out.print(">(((('> ");
				}
			} catch (InterruptedException e) {
				System.out.println(" ");
			}
			System.out.println("\n\nIt's your turn!");
			System.out.println("Your hand: " + playerCards);

			/**
			* Gets the numbers of each card in preporation to ask the ai for a card 
			*
			*	@var nums: stores the number values of each card in hand, EXCLUDING dupes. 
			*/

			String l = "";
			ArrayList<String> nums = new ArrayList<String>(playerCards);
			for(int ca = 0; ca < nums.size(); ca++)
			{
				if(nums.get(ca).length() == 3)
				{
					l = nums.get(ca).substring(0,2);
				} else {
					l = String.valueOf(nums.get(ca).charAt(0));
				}
				
				nums.set(ca, l);
			}

			for(int i = 0; i < nums.size(); i++)
			{
				if(Collections.frequency(nums, nums.get(i)) > 1)
				{
					nums.remove((i));
					i--;
				}
			}
			Collections.sort(nums);

			/* Player asks ai for a card */

			String ask;
			Scanner askScanner = new Scanner(System.in);
			do 
			{
				System.out.println("\nSelect a value to ask " + computer.getName() + " for! " + nums);
				ask = askScanner.nextLine();
			} 
			while (!(Collections.frequency(nums, ask) == 1));
			System.out.println(" ");

			/* Looks for the cards with the corresponding numeric vales */

			String ll = "";
			int count = 0;
			for(int i = 0; i < copyOfCompCards.size(); i++)
			{
				if(copyOfCompCards.get(i).length() == 3)
				{
					ll = copyOfCompCards.get(i).substring(0,2);
				} else {
					ll = String.valueOf(copyOfCompCards.get(i).charAt(0));
				}

				if(ll.equals(ask))
				{
					try 
					{
						Thread.sleep(200);
						System.out.println("You received " + copyOfCompCards.get(i));
					} catch (InterruptedException e) {}
					
					playerCards.add(copyOfCompCards.get(i));
					copyOfCompCards.remove(i);
					i--;
				}
				count++;
			}

			/* If the ai does not have any card of value, call GoFish */

			if(count == copyOfCompCards.size())
			{
				goFish(player, computer, playerCards, compCards, cards, true);
			} else {
				dupes(player, computer, cards, playerCards, true);
			}

			System.out.println("Your hand: " + playerCards);
			computer.setHand(copyOfCompCards);
			Collections.sort(computer.getHand());

			if(playerCards.size() != 0 && cards.size() != 0)
			{
				computerTurn(player, computer, playerCards, computer.getHand(), cards);
			} else  if (player.getWin() == false) {
				if(playerCards.size() == 0)
				{
					System.out.println("You have no more cards in your hand!");
				} else if (cards.size() == 0) {
					System.out.println("There are no more cards in the draw pile!");
				}
				winCondition(player, computer);
			}
		}
	}

	public static void goFish(Player player, Computer computer, ArrayList playerCards, ArrayList compCards, ArrayList cards, boolean isPlayerTurn)
	{
		int index = (int)(Math.floor(Math.random()*(cards.size())));
		if(isPlayerTurn == true)
		{
			System.out.println("Go Fish!");

			System.out.println("You drew " + cards.get(index) + "\n");
			playerCards.add(cards.get(index));
			cards.remove(index);

			dupes(player, computer, cards, playerCards, true);
		} else{
			System.out.println("\nYou tell the computer to go fish!");
			compCards.add(cards.get(index));
			cards.remove(index); 

			dupes(player, computer, cards,  compCards, false);
		}
	}

	public static void computerTurn(Player player, Computer computer, ArrayList playerCards, ArrayList compCards, ArrayList cards)
	{
		if(compCards.size() == 0 || cards.size() == 0)
		{
			if(compCards.size() == 0)
			{
				System.out.println("The computer has no more cards at hand!");
			} else if (cards.size() == 0) {
				System.out.println("There are no more cards in the draw pile!");
			}
			winCondition(player, computer);
			
		} else {
			ArrayList<String> copyOfPlayerCards = new ArrayList<String>(playerCards);

			System.out.println("");
			try 
			{
				for(int i = 0; i < 5; i++)
				{
					Thread.sleep(200);
					System.out.print(">(((('> ");
				}
			} catch (InterruptedException e) {}
			System.out.println("\n\nIt's the computer's turn!\n");

			/**
			* Gets the numbers of each card in preporation to ask the player for a card 
			* 
			*	@var nums: stores the number values of each card in hand, EXCLUDING dupes. 
			*/

			String l = "";
			ArrayList<String> numsComp = new ArrayList<String>(compCards);
			for(int ca = 0; ca < numsComp.size(); ca++)
			{
				if(numsComp.get(ca).length() == 3)
				{
					l = numsComp.get(ca).substring(0,2);
				} else {
					l = String.valueOf(numsComp.get(ca).charAt(0));
				}
				
				numsComp.set(ca, l);
			}

			for(int i = 0; i < numsComp.size(); i++)
			{
				if(Collections.frequency(numsComp, numsComp.get(i)) > 1)
				{
					numsComp.remove((i));
					i--;
				}
			}

			/* Computer asks player for a card */

			int randomCard = (int)(Math.floor(Math.random()*(numsComp.size())));

			System.out.println("Do you have a " + numsComp.get(randomCard) + "?");
			String answer;
			Scanner answerScanner = new Scanner(System.in);

			/* Looks for the cards with the corresponding numeric vales */

			String ll = "";
			int count = 0;
			for(int i = 0; i < copyOfPlayerCards.size(); i++)
			{
				if(copyOfPlayerCards.get(i).length() == 3)
				{
					ll = copyOfPlayerCards.get(i).substring(0,2);
				} else {
					ll = String.valueOf(copyOfPlayerCards.get(i).charAt(0));
				}

				if(ll.equals(numsComp.get(randomCard)))
				{
					compCards.add(copyOfPlayerCards.get(i));
					copyOfPlayerCards.remove(i);
					i--;
				}
				count++;
			}

			/* If the player does not have any card of value, call GoFish */

			if(count == copyOfPlayerCards.size())
			{
				do 
				{
					System.out.println("Type 'no' to tell the computer to go fish!");
					answer = answerScanner.nextLine();
				} 
				while (!(answer.equalsIgnoreCase("no")));

				goFish(player, computer, playerCards, compCards, cards, false);
			} else {
				do 
				{
					System.out.println("Type 'yes' to give your cards with a " + numsComp.get(randomCard) + "!");
					answer = answerScanner.nextLine();
				} 
				while (!(answer.equalsIgnoreCase("yes")));
				
				dupes(player, computer, cards,  compCards, false);
			}

			Collections.sort(copyOfPlayerCards);
			player.setHand(copyOfPlayerCards);

			if(compCards.size() != 0 && cards.size() != 0)
			{
				playerTurn(player, computer, player.getHand(), compCards, cards);
			} else if (player.getWin() == false){
				if(compCards.size() == 0)
				{
					System.out.println("The computer does not have any more cards at hand!");
					winCondition(player, computer);
				} else if(cards.size() == 0){
					System.out.println("There are no more cards in the draw pile!");
					winCondition(player, computer);
				}
			}
		}
	}

	public static void dupes(Player player, Computer computer, ArrayList cards, ArrayList hand, boolean isTurn)
	{
		String l = "";
		String m = "";
		ArrayList<String> val = new ArrayList<String>(hand); 
		ArrayList<String> symbol = new ArrayList<String>(hand);

		/** 
		* Separates the numbers from the symbols to find the dupes
		*
		* @var l : the number
		* @var m : the symbol
		*/

		for(int ca = 0; ca < val.size(); ca++)
		{
			if(val.get(ca).length() == 3)
			{
				l = val.get(ca).substring(0,2);
				m = String.valueOf(symbol.get(ca).charAt(2));
			}	else {
				l = String.valueOf(val.get(ca).charAt(0));
				m = String.valueOf(symbol.get(ca).charAt(1));
			}
			
			val.set(ca, l);
			symbol.set(ca, m);
		}

		/* Find book using the numbers */

		ArrayList<Integer> dupeIndexes = new ArrayList<Integer>();

		for(int i = 0; i < val.size(); i++)
		{
			if(Collections.frequency(val, val.get(i)) == 4)
			{
				for(int j = 0; j < val.size(); j++)
				{
					if(val.get(j).equals(val.get(i)))
					{
						dupeIndexes.add(j);
					}
				}
				val.removeAll(Collections.singleton(val.get(i)));
				
				if(isTurn == true)
				{
					player.setBook();
					System.out.println("You got +1 book!\n");
				} else {
					computer.setBook();
					System.out.println("\nThe computer got +1 book!\n");
				}
			}
		}

		/**
		* Finds the indexes of the removed numbers from dupeIndexes and removes the corresponding symbols. 
		*
		* Precondition: the hand will never have a case where there are two sets (or more) of a book. 
		*/
		
		int runs = 0;
		for(int s : dupeIndexes)
		{
			symbol.remove(s-runs);
			runs++;
		}

		/* New hand w/o dupes */
		hand.clear();
		for(int i = 0; i < val.size(); i++)
		{
			hand.add(val.get(i)+symbol.get(i));
		}

		if(hand.size() == 0 || cards.size() == 0)
		{
			if (cards.size() == 0)
			{
				System.out.println("There are no more cards in the draw pile!");
			}
			else if (player.getNumCards() == 0)
			{
				System.out.println("You have no more cards in your hand!");
			}
			else if(computer.getNumCards() == 0)
			{
				System.out.println("The computer has no more cards at hand!");
			}
			winCondition(player, computer); 
		}
	}

	public static void winCondition(Player player, Computer computer)
	{
		System.out.println("The game is over!");
		if(player.getBook() > computer.getBook())
		{
			System.out.println("Congratulations! You won!");
		} 
		else if (computer.getBook() > player.getBook()) 
		{
			System.out.println("Oh no! The computer won! Better luck next time.");
		} else {
			System.out.println("It's a tie!");
		}
		player.setWin();
	}
}
