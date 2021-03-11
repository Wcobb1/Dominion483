package dominionAgents;

import java.util.ArrayList;

import dominionAgents.CardData.CardType;

public class StatAnalysis {
	
	ArrayList<ArrayList<StringInt>> winnerCards;
	
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
	
	public StatAnalysis() {
		winnerCards = new ArrayList();
	}
	
	public void addWinnerCards(ArrayList<StringInt> wsi) {
		winnerCards.add(wsi);
	}
	
	public void printWinnerOwnPercentage() {
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
		//calculate and print results
		System.out.println("Card\t\t% games owned by winner:");
		for(int i = 0;i < cardGameCount.length;i ++) {
			float result = (float)winnerOwnCount[i]/cardGameCount[i];
			String s = String.format("%-20s %s", CardData.cardNames[i], result);
			System.out.println(s);
		}
	}
	
	
}
