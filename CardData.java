package dominionAgents;

import java.util.ArrayList;
import java.util.Random;

public class CardData {

	public static String[] cardNames = {
		"PROVINCE",
		"DUCHY",
		"ESTATE",
		"GOLD",
		"SILVER",
		"COPPER",
		"WITCH",
		"MINE",
		"MARKET",
		"LIBRARY",
		"LABRATORY",
		"FESTIVAL",
		"CURSE",
		"COUNCIL ROOM",
		"THRONE ROOM",
		"SMITHY",
		"REMODEL",
		"MONEYLENDER",
		"MILITIA",
		"GARDENS",
		"BUREAUCRAT",
		"WORKSHOP",
		"VILLAGE",
		"MOAT",
		"CHAPEL",
		"CELLAR",
		"FEAST",
		"CHANCELLOR",
		"WOODCUTTER",
		"ADVENTURER",
		"SPY",
		"THIEF"
	};
	
	public static enum Name {
		PROVINCE,
		DUCHY,
		ESTATE,
		GOLD,
		SILVER,
		COPPER,
		WITCH,
		MINE,
		MARKET,
		LIBRARY,
		LABRATORY,
		FESTIVAL,
		CURSE,
		COUNCIL_ROOM,
		THRONE_ROOM,
		SMITHY,
		REMODEL,
		MONEYLENDER,
		MILITIA,
		GARDENS,
		BUREAUCRAT,
		WORKSHOP,
		VILLAGE,
		MOAT,
		CHAPEL,
		CELLAR,
		FEAST,
		CHANCELLOR,
		WOODCUTTER,
		ADVENTURER,
		SPY,
		THIEF
	}
	
	public static enum CardType {
		VICTORY,
		TREASURE,
		ACTION,
		ATTACK,
		REACTION,
		CURSE
	}
	
	
	//format: {{card types}{actions, cards, coins, buys, customBoolean}{cost}}
	public static int[][][] cardValues = {
		
		//PROVINCE
		{{CardType.VICTORY.ordinal()},{0,0,0,0,0},{8}},
		//DUCHY
		{{CardType.VICTORY.ordinal()},{0,0,0,0,0},{5}},
		//ESTATE
		{{CardType.VICTORY.ordinal()},{0,0,0,0,0},{2}},
		//GOLD
		{{CardType.TREASURE.ordinal()},{0,0,3,0,0},{6}},
		//SILVER
		{{CardType.TREASURE.ordinal()},{0,0,2,0,0},{3}},
		//COPPER
		{{CardType.TREASURE.ordinal()},{0,0,1,0,0},{0}},
		//WITCH
		{{CardType.ACTION.ordinal(),CardType.ATTACK.ordinal()},{0,2,0,0,1},{5}},
		//MINE
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{5}},
		//MARKET
		{{CardType.ACTION.ordinal()},{1,1,1,1,0},{5}},
		//LIBRARY
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{5}},
		//LABRATORY
		{{CardType.ACTION.ordinal()},{1,2,0,0,0},{5}},
		//FESTIVAL
		{{CardType.ACTION.ordinal()},{2,0,2,1,0},{5}},
		//CURSE
		{{CardType.CURSE.ordinal()},{0,0,0,0,0},{0}},
		//COUNCIL_ROOM
		{{CardType.ACTION.ordinal()},{0,4,0,1,1},{5}},
		//THRONE_ROOM
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{4}},
		//SMITHY
		{{CardType.ACTION.ordinal()},{0,3,0,0,0},{4}},
		//REMODEL
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{4}},
		//MONEYLENDER
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{4}},
		//MILITIA
		{{CardType.ACTION.ordinal(),CardType.ATTACK.ordinal()},{0,0,2,0,1},{4}},
		//GARDENS
		{{CardType.VICTORY.ordinal()},{0,0,0,0,0},{4}},
		//BUREAUCRAT
		{{CardType.ACTION.ordinal(),CardType.ATTACK.ordinal()},{0,0,0,0,1},{4}},
		//WORKSHOP
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{3}},
		//VILLAGE
		{{CardType.ACTION.ordinal()},{2,1,0,0,0},{3}},
		//MOAT
		{{CardType.ACTION.ordinal(),CardType.REACTION.ordinal()},{0,2,0,0,0},{2}},
		//CHAPEL
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{2}},
		//CELLAR
		{{CardType.ACTION.ordinal()},{1,0,0,0,1},{2}},
		//FEAST
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{4}},
		//CHANCELLOR
		{{CardType.ACTION.ordinal()},{0,0,2,0,1},{3}},
		//WOODCUTTER
		{{CardType.ACTION.ordinal()},{0,0,2,1,0},{3}},
		//ADVENTURER
		{{CardType.ACTION.ordinal()},{0,0,0,0,1},{6}},
		//SPY
		{{CardType.ACTION.ordinal(),CardType.ATTACK.ordinal()},{1,1,0,0,1},{4}},
		//THIEF
		{{CardType.ACTION.ordinal(),CardType.ATTACK.ordinal()},{0,0,0,0,1},{4}}
	};
	
	public static ArrayList<Card> getRandomKingdom(){
		Random rand = new Random();
		ArrayList<String> cardsChosen = new ArrayList<String>();
		//Add all possible cards to ArrayList
		for(int i = 6;i < cardNames.length;i ++) {
			//Don't include Curse as an option for the kingdom
			if(i != 12) {
				cardsChosen.add(cardNames[i]);
			}
		}
		//Remove cards randomly until only 10 remain
		while(cardsChosen.size() > 10) {
			int chosen = rand.nextInt(cardsChosen.size());
			cardsChosen.remove(chosen);
		}
		
		ArrayList<Card> cards = new ArrayList<Card>();
		while(cardsChosen.size() > 0) {
			cards.add(new Card(cardsChosen.get(0)));
			cardsChosen.remove(0);
		}
		//Return 10 cards
		return cards;
	}
	
	public static int getCardNumber(String name) {
		for(int i = 0;i < cardNames.length;i ++) {
			if(name.equalsIgnoreCase(cardNames[i])) {
				return i;
			}
		}
		//Name isn't found
		return -1;
	}
	
	public static String getCardName(int num) {
		if(num < cardNames.length) {
			return cardNames[num];
		}
		//Name isn't found
		return "Card not found";
	}
	
	public static void setupCard(Card c, String cardName) {
		
		int cardNum = getCardNumber(cardName);
		ArrayList<Integer> cardTypes = new ArrayList<Integer>();
		for(Integer type: cardValues[cardNum][0]) {
			cardTypes.add(type);
		}
		c.setCardTypes(cardTypes);
		c.setActionsAdded(cardValues[cardNum][1][0]);
		c.setCardsAdded(cardValues[cardNum][1][1]);
		c.setCoinsAdded(cardValues[cardNum][1][2]);
		c.setBuysAdded(cardValues[cardNum][1][3]);
		boolean customEffect = (cardValues[cardNum][1][4] == 1) ? true : false;
		c.setCustomEffect(customEffect);
		c.setCost(cardValues[cardNum][2][0]);
	}
	

	public static ArrayList<Pair<Integer,Float>> ratioCalc(){
        
		ArrayList<Pair<Integer,Float>> wantedCards = new ArrayList<>(); 
		float ratio = 0;
		int i =0;
		for(int[][][] cp: cardValues ) {
			int[] cA = cp[i][1];
			ratio = (cA[0] + cA[1] + cA[2] + cA[3])/cp[i][2][0];
			wantedCards.add(new Pair<Integer,Float>(i,ratio));
			System.out.print(wantedCards+"\n");
			i ++;
		}
		return wantedCards;
	}

}
