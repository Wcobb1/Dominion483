package dominionAgents;

import java.util.ArrayList;

public class StatsDriver {
	
	private static int games = 100;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int[] wins = {0,0,0};
		
		ArrayList<StringInt[]> p1Decks = new ArrayList<StringInt[]>();
		ArrayList<StringInt[]> p2Decks = new ArrayList<StringInt[]>();
		ArrayList<String[]> cardsInKingdoms = new ArrayList<String[]>();
		
		long startTime = System.nanoTime();
		
		for(int i = 0;i < games;i ++) {
			
			Kingdom kingdom = new Kingdom();
			PlayerCommunication pc = new PlayerCommunication();
			//Set player types
			Player p1 = new BasicBotV1_0(kingdom, pc);
			Player p2 = new RushBotV1_0(kingdom, pc);
			
			GameSimulator gs = new GameSimulator(p1, p2);
			int result = gs.runGame();
			
			wins[result] ++;
		
			StringInt[][] si = gs.getDecks();
			p1Decks.add(si[0]);
			p2Decks.add(si[1]);
			
			cardsInKingdoms.add(gs.getCardsInKingdom());
		
		}
		
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
		
		System.out.println(games + " games played in " + elapsedTimeInSeconds + " seconds:");
		System.out.println("Player 1 win %: " + wins[0]/(double)games*100.0);
		System.out.println("Player 2 win %: " + wins[1]/(double)games*100.0);
		System.out.println("Draw %: " + wins[2]/(double)games*100.0);
		
		ArrayList<Double> p1AvgDeck = calculateAverageDeck(p1Decks, cardsInKingdoms);
		ArrayList<Double> p2AvgDeck = calculateAverageDeck(p2Decks, cardsInKingdoms);
		
		System.out.println("P1 Avg. Deck: ");
		for(int i = 0;i < p1AvgDeck.size();i ++) {
			System.out.println(CardData.getCardName(i) + ": " + p1AvgDeck.get(i));
		}
		System.out.println("P2 Avg. Deck: ");
		for(int i = 0;i < p2AvgDeck.size();i ++) {
			System.out.println(CardData.getCardName(i) + ": " + p2AvgDeck.get(i));
		}
	}
	
	private static ArrayList<Double> calculateAverageDeck(ArrayList<StringInt[]> input, ArrayList<String[]> kingdoms){
		ArrayList<Double> avgDeck = new ArrayList<Double>();
		int[] numAppeared = new int[CardData.getNumCards()];
		
		//initialize 0s
		for(int i = 0;i < CardData.getNumCards();i ++) {
			avgDeck.add(new Double(0));
		}
		//initialize values
		for(int i = 0;i < numAppeared.length;i ++) {
			if(i < 6 || i == 12) {
				//treasure, victory, curse cards
				numAppeared[i] = games;
			}else {
				numAppeared[i] = 0;
			}
		}
		
		int numDecks = input.size();
		
		//Count each card
		for(int i = 0;i < numDecks;i ++) {
			for(int j = 0;j < input.get(i).length;j ++) {
				String cardName = input.get(i)[j].getString();
				int numOfCard = input.get(i)[j].getInt();
				int cardIndex = CardData.getCardNumber(cardName);
				avgDeck.set(cardIndex, avgDeck.get(cardIndex) + numOfCard);
			}
			//count for numAppeared
			for(int j = 0;j < kingdoms.get(i).length;j ++) {
				int index = CardData.getCardNumber(kingdoms.get(i)[j]);
				numAppeared[index] ++;
			}
		}
		//Divide by number of kingdoms that that card appeared in
		for(int i = 0;i < CardData.getNumCards();i ++) {
			avgDeck.set(i, avgDeck.get(i)/(double)numAppeared[i]);
		}
		return avgDeck;
	}

}
