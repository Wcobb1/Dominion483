package dominionAgents;

import java.util.ArrayList;
import java.util.Random;

import dominionAgents.PlayerCommunication.PlayerCode;

public class RushBotV1_0 extends Player{
	
	//lower value = higher priority
	public int[] cardPriorities = {
//			"PROVINCE",
			0,
//			"DUCHY",
			9,
//			"ESTATE",
			8,
//			"GOLD",
			11,
//			"SILVER",
			10,
//			"COPPER",
			13,
//			"WITCH",
			15,
//			"MINE",
			15,
//			"MARKET",
			3,
//			"LIBRARY",
			15,
//			"LABRATORY",
			15,
//			"FESTIVAL",
			12,
//			"CURSE",
			25,
//			"COUNCIL ROOM",
			15,
//			"THRONE ROOM",
			15,
//			"SMITHY",
			15,
//			"REMODEL",
			15,
//			"MONEYLENDER",
			15,
//			"MILITIA",
			15,
//			"GARDENS",
			1,
//			"BUREAUCRAT",
			15,
//			"WORKSHOP",
			2,
//			"VILLAGE",
			5,
//			"MOAT",
			7,
//			"CHAPEL",
			15,
//			"CELLAR",
			4,
//			"FEAST",
			15,
//			"CHANCELLOR",
			15,
//			"WOODCUTTER",
			6,
//			"ADVENTURER",
			15,
//			"SPY",
			15,
//			"THIEF"
			15
		};
	
	public RushBotV1_0(Kingdom k, PlayerCommunication pc) {
		super(k, pc);
	}
	
	@Override
	protected void resolveActionPhase() {
		
		//Steps 1 and 2A
		playPhaseAlgorithm1();
		
		//Step 2: Play any workshops
		for(int i = 0;i < hand.getHand().size() && actions > 0;i ++) {
			if(hand.getHand().get(i).getName().equalsIgnoreCase("WORKSHOP")) {
				playCard(hand.getHand().get(i));
			}
		}
		
		//Step 3: Play highest buy adders
		playBuyAdders();
		
		//Step 4: play remaining action cards from left to right
		for(int i = 0;i < hand.getHand().size() && actions > 0;i ++) {
			if(hand.getHand().get(i).isCardType(CardData.CardType.ACTION)) {
				playCard(hand.getHand().get(i));
				i --;
			}
		}
		
		//Step 5: Play treasure cards(this is done in takeTurn() in Player.java)
		
	}
	
	//Step 1 and 2A of the play phase algorithm
	protected void playPhaseAlgorithm1() {
		
		//Step 1: Play action adders
		//(Done in Player.java)
		
		//Step 1B: Check if actions > 1
		if(actions > 1) {
			//Step 2: Find highest card adder(if one exists)
			int mostCards = 0;
			int index = 0;
			
			for(int i = 0;i < hand.getHand().size();i ++) {
				if(hand.getHand().get(i).getCardsAdded() > mostCards) {
					mostCards = hand.getHand().get(i).getCardsAdded();
					index = i;
				}
			}
			//check if there is at least 1 card adder
			if(mostCards > 0) {
				//play card adder
				playCard(hand.getHand().get(index));
				//recursive call
				playPhaseAlgorithm1();
			}
		}
	}

	public void playBuyAdders() {
		int highest = 0;
		int index = 0;
		//find highest buy adder(if one is in hand)
		for(int j = 0;j < hand.getHand().size();j ++) {
			if(hand.getHand().get(j).getBuysAdded() > highest) {
				highest = hand.getHand().get(j).getBuysAdded();
				index = j;
			}
		}
		
		if(highest > 0) {
			playCard(hand.getHand().get(index));
			if(actions > 0) {
				playBuyAdders();
			}
		}
		
	}
	
	@Override
	protected void resolveBuyPhase() {
		
		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		int lowest = 16;
		String cardChosen = null;
		for(SupplyPile sp:supply) {
			int index = CardData.getCardNumber(sp.getCard().getName());
			if(cardPriorities[index] < lowest && kingdom.canBuy(sp.getCard().getName()) && sp.getCard().getCost() < coins) {
				cardChosen = sp.getCard().getName();
				lowest = cardPriorities[index];
			}
		}
		if(cardChosen != null && buys >= 1) {
			buyCard(cardChosen);
			if(buys > 0) {
				resolveBuyPhase();
			}
		}
	}
	
	//Super primitive: just discards randomly
	@Override
	public void discardDownTo(int numLeft) {
		
		Random rand = new Random();
		while(hand.getHand().size() > numLeft) {
			int choice = rand.nextInt(hand.getHand().size());
			hand.discardCard(choice);
		}
		
	}

	@Override
	//true = discard, false = put back
	public boolean discardOrPutBack(Card c, PlayerCode pcd) {
		if(c.isCardType(CardData.CardType.ACTION) || c.isCardType(CardData.CardType.TREASURE)) {
			if(pcd == PlayerCode.SELF) {
				return false;
			}
			return true;
		}else {
			if(pcd == PlayerCode.SELF) {
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean chooseToGainCard(Card c) {
		if(c.getCost() > 2) {
			return true;
		}
		return false;
	}

	@Override
	public void resolveWorkshop() {
		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		int lowest = 16;
		String cardChosen = null;
		for(SupplyPile sp:supply) {
			int index = CardData.getCardNumber(sp.getCard().getName());
			if(cardPriorities[index] < lowest) {
				cardChosen = sp.getCard().getName();
				lowest = cardPriorities[index];
			}
		}
		if(cardChosen != null) {
			gainCard(cardChosen);
		}
	}
	
	@Override
	public void resolveFeast() {
		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		int lowest = 16;
		String cardChosen = null;
		for(SupplyPile sp:supply) {
			int index = CardData.getCardNumber(sp.getCard().getName());
			if(cardPriorities[index] < lowest) {
				cardChosen = sp.getCard().getName();
				lowest = cardPriorities[index];
			}
		}
		if(cardChosen != null) {
			gainCard(cardChosen);
		}
	}

	@Override
	public ArrayList<Card> chooseCardsForCellar() {
		ArrayList<Card> cardsToDiscard = new ArrayList<Card>();
		//choose cards to discard
		for(int i = 0;i < hand.getHand().size();i ++) {
			//Discard victory, curse, copper cards
			Card c = hand.getHand().get(i);
			if(c.getName().equalsIgnoreCase("COPPER") || c.isCardType(CardData.CardType.VICTORY) || c.isCardType(CardData.CardType.CURSE)) {
				cardsToDiscard.add(c);
			}
		}
		
		return cardsToDiscard;
	}

	@Override
	public void resolveChapel() {
		int cardsTrashed = 0;
		for(int i = 0;i < hand.getHand().size() && cardsTrashed < 4;i ++) {
			Card c = hand.getHand().get(i);
			if(c.getName().equalsIgnoreCase("CURSE") || c.getName().equalsIgnoreCase("COPPER") || c.getName().equalsIgnoreCase("ESTATE") || c.getName().equalsIgnoreCase("CHAPEL")) {
				trashCard(hand.getHand(), c);
				i --;
				cardsTrashed ++;
			}
		}
	}

	@Override
	public boolean chancellorDeckToDiscard() {
		return true;
	}
	
	@Override
	public Card chooseCardForMine() {
		//find highest non-gold treasure in hand(if one exists)
		Card highestTreasure = null;
		for(int i = 0;i < hand.getHand().size();i ++) {
			Card c = hand.getHand().get(i);
			if(c.getName().equalsIgnoreCase("Copper")){
				if(highestTreasure == null) {
					highestTreasure = c;
				}
			}else if(c.getName().equalsIgnoreCase("Silver")) {
				highestTreasure = c;
			}
		}
		
		return highestTreasure;
		
	}

	@Override
	public boolean keepOrDiscardForLibrary(Card c) {
		//choose whether or not to discard it
		if(actions < 1) {
			return false;
		}
		return true;
	}

	@Override
	public Card chooseActionCardForThroneRoom() {
		//find all action cards in hand
		int highest = 0;
		ArrayList<Card> actionCardChoices = new ArrayList<Card>();
		//find most expensive action card
		for(int i = 0;i < hand.getHand().size();i ++) {
			Card c = hand.getHand().get(i);
			if(c.isCardType(CardData.CardType.ACTION)) {
				if(c.getCost() > highest) {
					highest = c.getCost();
				}
			}
		}
		//if not, no action cards were found
		if(highest > 0) {
			//put most expensive action cards into an ArrayList
			for(int i = 0;i < hand.getHand().size();i ++) {
				Card c = hand.getHand().get(i);
				if(c.isCardType(CardData.CardType.ACTION) && c.getCost() == highest) {
					hand.getHand().remove(c);
					actionCardChoices.add(c);
				}
			}
			
			int choice;
			if(actionCardChoices.size() == 1) {
				choice = 0;
			}else {
				Random rand = new Random();
				choice = rand.nextInt(actionCardChoices.size());
			}
			Card chosenCard = actionCardChoices.get(choice);
			//remove choice card from actionCardChoices and return others to hand
			actionCardChoices.remove(choice);
			while(actionCardChoices.size() > 0) {
				Card c = actionCardChoices.get(0);
				hand.getHand().add(c);
				actionCardChoices.remove(0);
			}
			return chosenCard;
		}
		return null;
	}

	private ArrayList<Card> highestCostList(int cost){
		int highestPossible = 0;
		
		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		ArrayList<Card> choices = new ArrayList<Card>();
		for(SupplyPile sp: supply) {
			Card c = sp.getCard();
			if(c.getCost() <= cost && sp.getCardsRemaining() > 0 && !c.getName().equalsIgnoreCase("CURSE")) {
				choices.add(c);
				if(c.getCost() > highestPossible) {
					highestPossible = c.getCost();
				}
			}
		}
		
		ArrayList<Card> mostExpensiveChoices = new ArrayList<Card>();
		
		if(choices.size() > 0) {
			for(Card c: choices) {
				if(c.getCost() == highestPossible) {
					mostExpensiveChoices.add(c);
				}
			}
		}
		
		return mostExpensiveChoices;
	}
	
	@Override
	public void resolveRemodel() {
		//can't trash if there are 0 cards in hand
		if(hand.getHand().size() > 0) {
			//trash random card from hand
			Random rand = new Random();
			int choice = rand.nextInt(hand.getHand().size());
			Card c = hand.getHand().get(choice);
			int cost = c.getCost();
			trashCard(hand.getHand(), c);
			
			//gain card costing up to 2 more than the trashed card
			ArrayList<Card> bestChoices = highestCostList(cost);
			if(bestChoices.size() > 0) {
				choice = rand.nextInt(bestChoices.size());
				gainCard(bestChoices.get(choice).getName());
			}
		}
		
	}
	
}
