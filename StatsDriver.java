package dominionAgents;

import java.io.File;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class StatsDriver {

	private static int games = 100;

	public static void main(String[] args) {
		int[] wins = {0,0,0};

		ArrayList<StringInt[]> p1Decks = new ArrayList<StringInt[]>();
		ArrayList<StringInt[]> p2Decks = new ArrayList<StringInt[]>();
		ArrayList<String[]> cardsInKingdoms = new ArrayList<String[]>();
		ArrayList<String> deckCSVList = new ArrayList<>();
		
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
		
		deckCSVList.add(games + " games played in " + Double.valueOf(elapsedTimeInSeconds).toString() + " seconds:");
		deckCSVList.add("\n");
		
		System.out.println("Player 1 win %: " + wins[0]/(double)games*100.0);
		
		deckCSVList.add("Player 1 win %: ");
		deckCSVList.add(",");
		deckCSVList.add(Double.valueOf(wins[0]/games*100.0).toString());
		deckCSVList.add("\n");
		
		System.out.println("Player 2 win %: " + wins[1]/(double)games*100.0);
		
		deckCSVList.add("Player 2 win %: ");
		deckCSVList.add(",");
		deckCSVList.add(Double.valueOf(wins[1]/games*100.0).toString());
		deckCSVList.add("\n");
		
		System.out.println("Draw %: " + wins[2]/(double)games*100.0);

		deckCSVList.add("Draw %: ");
		deckCSVList.add(",");
		deckCSVList.add(Double.valueOf(wins[2]/games*100.0).toString());
		deckCSVList.add("\n");
		
		ArrayList<Double> p1AvgDeck = calculateAverageDeck(p1Decks, cardsInKingdoms);
		ArrayList<Double> p2AvgDeck = calculateAverageDeck(p2Decks, cardsInKingdoms);
		
		deckCSVList.add("\n");
		
		System.out.println("P1 Avg. Deck: ");
		
		deckCSVList.add("P1 Avg. Deck: ");
		deckCSVList.add("\n");
		for(int i = 0;i < p1AvgDeck.size();i ++) {
			System.out.println(CardData.getCardName(i) + ": " + p1AvgDeck.get(i));
			deckCSVList.add(CardData.getCardName(i));
			deckCSVList.add(",");
			deckCSVList.add(Double.valueOf(p1AvgDeck.get(i)).toString());
			deckCSVList.add("\n");
		}
		
		System.out.println("P2 Avg. Deck: ");

		deckCSVList.add("\n");
		deckCSVList.add("\t");

		deckCSVList.add("P2 Avg. Deck: ");
		deckCSVList.add("\n");
		
		for(int i = 0;i < p2AvgDeck.size();i ++) {
			System.out.println(CardData.getCardName(i) + ": " + p2AvgDeck.get(i));
		
			deckCSVList.add(CardData.getCardName(i));
			deckCSVList.add(",");
			deckCSVList.add(Double.valueOf(p2AvgDeck.get(i)).toString());
			deckCSVList.add("\n");
		
		}
		exportToCSV(deckCSVList);
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

	private static void exportToCSV(ArrayList<String> decks) {
		File csvDeck = new File("CSV_Decks.csv");
		for(int i = 1; csvDeck.exists(); i++) {
			csvDeck = new File("CSV_Decks_"+i+".csv");
		}
		try (PrintWriter writer = new PrintWriter(csvDeck)){
			StringBuilder sb = new StringBuilder();
			for (String s : decks) {
				sb.append(s);
			}
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Successfully exported Avg. Deck Data to " + csvDeck.getName());
	}
}