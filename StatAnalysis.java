package dominionAgents;

import java.util.ArrayList;

import dominionAgents.CardData.CardType;

public class StatAnalysis {
	
	private ArrayList<ArrayList<StringInt>> winnerCards;
	private ArrayList<ArrayList<StringInt>> firstTwoTurns;
	private ArrayList<ArrayList<StringInt>> midGame;
	private ArrayList<ArrayList<StringInt>> lateGame;
	private boolean calculated = false;
	
	int[] winnerOwnCount = {
		//PROVINCE
		0,
		//DUCHY
		0,
		//ESTATE
		0,
		//GOLD
		0,
		//SILVER
		0,
		//COPPER
		0,
		//WITCH
		0,
		//MINE
		0,
		//MARKET
		0,
		//LIBRARY
		0,
		//LABRATORY
		0,
		//FESTIVAL
		0,
		//CURSE
		0,
		//COUNCIL_ROOM
		0,
		//THRONE_ROOM
		0,
		//SMITHY
		0,
		//REMODEL
		0,
		//MONEYLENDER
		0,
		//MILITIA
		0,
		//GARDENS
		0,
		//BUREAUCRAT
		0,
		//WORKSHOP
		0,
		//VILLAGE
		0,
		//MOAT
		0,
		//CHAPEL
		0,
		//CELLAR
		0,
		//FEAST
		0,
		//CHANCELLOR
		0,
		//WOODCUTTER
		0,
		//ADVENTURER
		0,
		//SPY
		0,
		//THIEF
		0
	};
	
	int[] cardGameCount = {
		//PROVINCE
		0,
		//DUCHY
		0,
		//ESTATE
		0,
		//GOLD
		0,
		//SILVER
		0,
		//COPPER
		0,
		//WITCH
		0,
		//MINE
		0,
		//MARKET
		0,
		//LIBRARY
		0,
		//LABRATORY
		0,
		//FESTIVAL
		0,
		//CURSE
		0,
		//COUNCIL_ROOM
		0,
		//THRONE_ROOM
		0,
		//SMITHY
		0,
		//REMODEL
		0,
		//MONEYLENDER
		0,
		//MILITIA
		0,
		//GARDENS
		0,
		//BUREAUCRAT
		0,
		//WORKSHOP
		0,
		//VILLAGE
		0,
		//MOAT
		0,
		//CHAPEL
		0,
		//CELLAR
		0,
		//FEAST
		0,
		//CHANCELLOR
		0,
		//WOODCUTTER
		0,
		//ADVENTURER
		0,
		//SPY
		0,
		//THIEF
		0
	};
	
	//{winCount, loseCount} for each card
	int[][] cardFirstTwoTurnsWinLoseCount = {
		//PROVINCE
		{0,0},
		//DUCHY
		{0,0},
		//ESTATE
		{0,0},
		//GOLD
		{0,0},
		//SILVER
		{0,0},
		//COPPER
		{0,0},
		//WITCH
		{0,0},
		//MINE
		{0,0},
		//MARKET
		{0,0},
		//LIBRARY
		{0,0},
		//LABRATORY
		{0,0},
		//FESTIVAL
		{0,0},
		//CURSE
		{0,0},
		//COUNCIL_ROOM
		{0,0},
		//THRONE_ROOM
		{0,0},
		//SMITHY
		{0,0},
		//REMODEL
		{0,0},
		//MONEYLENDER
		{0,0},
		//MILITIA
		{0,0},
		//GARDENS
		{0,0},
		//BUREAUCRAT
		{0,0},
		//WORKSHOP
		{0,0},
		//VILLAGE
		{0,0},
		//MOAT
		{0,0},
		//CHAPEL
		{0,0},
		//CELLAR
		{0,0},
		//FEAST
		{0,0},
		//CHANCELLOR
		{0,0},
		//WOODCUTTER
		{0,0},
		//ADVENTURER
		{0,0},
		//SPY
		{0,0},
		//THIEF
		{0,0}
	};
	
	//{winCount, loseCount} for each card
	int[][] cardsMidGameWinLoseCount = {
		//PROVINCE
		{0,0},
		//DUCHY
		{0,0},
		//ESTATE
		{0,0},
		//GOLD
		{0,0},
		//SILVER
		{0,0},
		//COPPER
		{0,0},
		//WITCH
		{0,0},
		//MINE
		{0,0},
		//MARKET
		{0,0},
		//LIBRARY
		{0,0},
		//LABRATORY
		{0,0},
		//FESTIVAL
		{0,0},
		//CURSE
		{0,0},
		//COUNCIL_ROOM
		{0,0},
		//THRONE_ROOM
		{0,0},
		//SMITHY
		{0,0},
		//REMODEL
		{0,0},
		//MONEYLENDER
		{0,0},
		//MILITIA
		{0,0},
		//GARDENS
		{0,0},
		//BUREAUCRAT
		{0,0},
		//WORKSHOP
		{0,0},
		//VILLAGE
		{0,0},
		//MOAT
		{0,0},
		//CHAPEL
		{0,0},
		//CELLAR
		{0,0},
		//FEAST
		{0,0},
		//CHANCELLOR
		{0,0},
		//WOODCUTTER
		{0,0},
		//ADVENTURER
		{0,0},
		//SPY
		{0,0},
		//THIEF
		{0,0}
	};
	
	//{winCount, loseCount} for each card
	int[][] cardsLateGameWinLoseCount = {
		//PROVINCE
		{0,0},
		//DUCHY
		{0,0},
		//ESTATE
		{0,0},
		//GOLD
		{0,0},
		//SILVER
		{0,0},
		//COPPER
		{0,0},
		//WITCH
		{0,0},
		//MINE
		{0,0},
		//MARKET
		{0,0},
		//LIBRARY
		{0,0},
		//LABRATORY
		{0,0},
		//FESTIVAL
		{0,0},
		//CURSE
		{0,0},
		//COUNCIL_ROOM
		{0,0},
		//THRONE_ROOM
		{0,0},
		//SMITHY
		{0,0},
		//REMODEL
		{0,0},
		//MONEYLENDER
		{0,0},
		//MILITIA
		{0,0},
		//GARDENS
		{0,0},
		//BUREAUCRAT
		{0,0},
		//WORKSHOP
		{0,0},
		//VILLAGE
		{0,0},
		//MOAT
		{0,0},
		//CHAPEL
		{0,0},
		//CELLAR
		{0,0},
		//FEAST
		{0,0},
		//CHANCELLOR
		{0,0},
		//WOODCUTTER
		{0,0},
		//ADVENTURER
		{0,0},
		//SPY
		{0,0},
		//THIEF
		{0,0}
	};
	
	public StatAnalysis() {
		winnerCards = new ArrayList<ArrayList<StringInt>>();
		firstTwoTurns = new ArrayList<ArrayList<StringInt>>();
		midGame = new ArrayList<ArrayList<StringInt>>();
		lateGame = new ArrayList<ArrayList<StringInt>>();
	}
	
	public void addWinnerCards(ArrayList<StringInt> wsi) {
		winnerCards.add(wsi);
	}
	
	public void addCardsFirstTwoTurns(ArrayList<String> winner, ArrayList<String> loser) {
		ArrayList<StringInt> temp = new ArrayList<StringInt>();
		for(String cardName:winner) {
			temp.add(new StringInt(cardName, 1));
			int cardNum = CardData.getCardNumber(cardName);
			cardFirstTwoTurnsWinLoseCount[cardNum][0] ++;
		}
		firstTwoTurns.add(temp);
		temp = new ArrayList<StringInt>();
		for(String cardName:loser) {
			temp.add(new StringInt(cardName, 0));
			int cardNum = CardData.getCardNumber(cardName);
			cardFirstTwoTurnsWinLoseCount[cardNum][1] ++;
		}
		firstTwoTurns.add(temp);
	}
	
	public void addCardsMidGame(ArrayList<String> winner, ArrayList<String> loser) {
		ArrayList<StringInt> temp = new ArrayList<StringInt>();
		for(String cardName:winner) {
			temp.add(new StringInt(cardName, 1));
			int cardNum = CardData.getCardNumber(cardName);
			cardsMidGameWinLoseCount[cardNum][0] ++;
		}
		firstTwoTurns.add(temp);
		temp = new ArrayList<StringInt>();
		for(String cardName:loser) {
			temp.add(new StringInt(cardName, 0));
			int cardNum = CardData.getCardNumber(cardName);
			cardsMidGameWinLoseCount[cardNum][1] ++;
		}
		midGame.add(temp);
	}
	
	public void addCardsLateGame(ArrayList<String> winner, ArrayList<String> loser) {
		ArrayList<StringInt> temp = new ArrayList<StringInt>();
		for(String cardName:winner) {
			temp.add(new StringInt(cardName, 1));
			int cardNum = CardData.getCardNumber(cardName);
			cardsLateGameWinLoseCount[cardNum][0] ++;
		}
		firstTwoTurns.add(temp);
		temp = new ArrayList<StringInt>();
		for(String cardName:loser) {
			temp.add(new StringInt(cardName, 0));
			int cardNum = CardData.getCardNumber(cardName);
			cardsLateGameWinLoseCount[cardNum][1] ++;
		}
		lateGame.add(temp);
	}
	
	public void calculateWinnerOwnPercentage() {
		//process cards
		for(int i = 0;i < winnerCards.size();i ++) {
			for(int j = 0;j < winnerCards.get(i).size();j ++) {
				StringInt si = winnerCards.get(i).get(j);
				//find card index for storing data
				int index = CardData.getCardNumber(si.getString());
				//store appropriate data
				cardGameCount[index] ++;
				if(si.getInt() > 0) {
					winnerOwnCount[index] ++;
				}
			}
		}
		calculated = true;
	}
	
	public void printWinnerOwnPercentage() {
		if(calculated) {
			//calculate and print results
			System.out.println("Card\t\t% games owned by winner:");
			for(int i = 0;i < cardGameCount.length;i ++) {
				float result = (float)winnerOwnCount[i]/cardGameCount[i];
				String s = String.format("%-20s %s", CardData.cardNames[i], result);
				System.out.println(s+"\t\t"+winnerOwnCount[i]+"/"+cardGameCount[i]);
			}
		}
	}
	
	public SortableGiniCardList getFirstTwoTurnsGiniData() {
		SortableGiniCardList list = new SortableGiniCardList();
		for(int i = 0;i < cardFirstTwoTurnsWinLoseCount.length;i ++) {
			int wins = cardFirstTwoTurnsWinLoseCount[i][0];
			int losses = cardFirstTwoTurnsWinLoseCount[i][1];
			int totalEntries = wins + losses;
			//placeholder N/A value
			double giniIndex = -1;
			boolean winGreaterThanLoss = false;
			if(totalEntries > 0) {
				double winFraction = ((double)wins/totalEntries);
				double lossFraction = ((double)losses/totalEntries);
				if(winFraction > lossFraction) {
					winGreaterThanLoss = true;
				}
				giniIndex = 1.0 - (double)(Math.pow(winFraction,2) + Math.pow(lossFraction,2));
			}
			//custom value using gini index to make successful cards highest priority
			if(!winGreaterThanLoss) {
				giniIndex = 1.0 - giniIndex;
			}
			SortableGiniCard sgc = new SortableGiniCard(CardData.getCardName(i), giniIndex);
			list.add(sgc);
		}
		return list;
	}
	
	public SortableGiniCardList getMidGameGiniData() {
		SortableGiniCardList list = new SortableGiniCardList();
		for(int i = 0;i < cardsMidGameWinLoseCount.length;i ++) {
			int wins = cardsMidGameWinLoseCount[i][0];
			int losses = cardsMidGameWinLoseCount[i][1];
			int totalEntries = wins + losses;
			//placeholder N/A value
			double giniIndex = -1;
			boolean winGreaterThanLoss = false;
			if(totalEntries > 0) {
				double winFraction = ((double)wins/totalEntries);
				double lossFraction = ((double)losses/totalEntries);
				if(winFraction > lossFraction) {
					winGreaterThanLoss = true;
				}
				giniIndex = 1.0 - (double)(Math.pow(winFraction,2) + Math.pow(lossFraction,2));
			}
			//custom value using gini index to make successful cards highest priority
			if(!winGreaterThanLoss) {
				giniIndex = 1.0 - giniIndex;
			}
			SortableGiniCard sgc = new SortableGiniCard(CardData.getCardName(i), giniIndex);
			list.add(sgc);
		}
		return list;
	}
	
	public SortableGiniCardList getLateGameGiniData() {
		SortableGiniCardList list = new SortableGiniCardList();
		for(int i = 0;i < cardsLateGameWinLoseCount.length;i ++) {
			int wins = cardsLateGameWinLoseCount[i][0];
			int losses = cardsLateGameWinLoseCount[i][1];
			int totalEntries = wins + losses;
			//placeholder N/A value
			double giniIndex = -1;
			boolean winGreaterThanLoss = false;
			if(totalEntries > 0) {
				double winFraction = ((double)wins/totalEntries);
				double lossFraction = ((double)losses/totalEntries);
				if(winFraction > lossFraction) {
					winGreaterThanLoss = true;
				}
				giniIndex = 1.0 - (double)(Math.pow(winFraction,2) + Math.pow(lossFraction,2));
			}
			//custom value using gini index to make successful cards highest priority
			if(!winGreaterThanLoss) {
				giniIndex = 1.0 - giniIndex;
			}
			SortableGiniCard sgc = new SortableGiniCard(CardData.getCardName(i), giniIndex);
			list.add(sgc);
		}
		return list;
	}
	
	public void printFirstTwoRounds() {
//		for(ArrayList<StringInt> al:firstTwoTurns) {
//			for(StringInt si:al) {
//				System.out.print(si.getString() + ", ");
//			}
//			String winLoseMessage = al.get(0).getInt() + "\n";
//			winLoseMessage = winLoseMessage.replace("0", "L");
//			winLoseMessage = winLoseMessage.replace("1", "W");
//			System.out.print(winLoseMessage);
//		}
		for(int i = 0;i < cardFirstTwoTurnsWinLoseCount.length;i ++) {
			String message = CardData.getCardName(i);
			int wins = cardFirstTwoTurnsWinLoseCount[i][0];
			int losses = cardFirstTwoTurnsWinLoseCount[i][1];
			message += ": {W:" + wins + ", L:" +
					losses + "}";
			int totalEntries = wins + losses;
			String giniIndexString = "N/A";
			if(totalEntries > 0) {
				double winFraction = ((double)wins/totalEntries);
				double lossFraction = ((double)losses/totalEntries);
				double giniIndex = 1.0 - (double)(Math.pow(winFraction,2) + Math.pow(lossFraction,2));
				giniIndexString = Double.toString(giniIndex);
			}
			message += "\t\tGini Index: " + giniIndexString;
			System.out.println(message);
		}
	}
	
	
}
