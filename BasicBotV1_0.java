package dominionAgents;

import java.util.ArrayList;
import java.util.Random;

import dominionAgents.PlayerCommunication.PlayerCode;

public class BasicBotV1_0 extends Player {

	public BasicBotV1_0(Kingdom k, PlayerCommunication pc) {
		super(k, pc);
	}
	
	@Override
	//Google Drawing of this algorithm: https://docs.google.com/drawings/d/1q42KrM8pCHeYYhbGws4F10Yqcqm_uGcYb_P---WAUy0/edit?usp=sharing
	protected void resolveActionPhase() {
		
		//Steps 1 and 2A
		playPhaseAlgorithm1();
		
		//Step 2: Check for coin adder
		int highest = 0;
		int index = 0;
		
		for(int i = 0;i < hand.getHand().size();i ++) {
			if(hand.getHand().get(i).getCoinsAdded() > highest) {
				highest = hand.getHand().get(i).getCoinsAdded();
				index = i;
			}
		}
		
		//if coin adder was found, play highest coin adder
		if(highest > 0) {
			//play coin adder
			playCard(hand.getHand().get(index));
			
		}
		
		//only continue if there are remaining actions
		if(actions > 0) {
			//reset highest and index to 0
			highest = 0;
			index = 0;
			
			//Step 3: Check for card adder
			for(int i = 0;i < hand.getHand().size();i ++) {
				if(hand.getHand().get(i).getCardsAdded() > highest) {
					highest = hand.getHand().get(i).getCardsAdded();
					index = i;
				}
			}
			
			//if card adder was found, play highest card adder
			if(highest > 0) {
				//play card adder
				playCard(hand.getHand().get(index));
			}
		}
		
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
		for(int i = 0;i < hand.getHand().size();i ++) {
			if(hand.getHand().get(i).getActionsAdded() > 0) {
				//play action adder
				playCard(hand.getHand().get(i));
				//recurse to step 1(it's possible new cards were drawn, for example, if a village were played in this step)
				playPhaseAlgorithm1();
			}
		}
		
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

	//Google Drawing of this algorithm: https://docs.google.com/drawings/d/1vjC6Dd4vrZFLf848YE90ssPsTT5bSQL_mGbvids2aPs/edit?usp=sharing
	@Override
	protected void resolveBuyPhase() {
		
		ArrayList<Card> mostExpensiveChoices = highestCostList(coins);
		
		if(mostExpensiveChoices.size() > 0) {
			Random rand = new Random();
			int choice = rand.nextInt(mostExpensiveChoices.size());
			//buy random card from mostExpensiveChoices
			buyCard(mostExpensiveChoices.get(choice).getName());
			
			//if there is still at least 1 buy and at least 2 coins, recurse
			if(buys > 0 && coins > 1) {
				resolveBuyPhase();
			}
		}
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
		ArrayList<Card> bestChoices = highestCostList(4);
		if(bestChoices.size() > 0) {
			Random rand = new Random();
			int choice = rand.nextInt(bestChoices.size());
			gainCard(bestChoices.get(choice).getName());
		}
	}
	
	@Override
	public void resolveFeast() {
		ArrayList<Card> bestChoices = highestCostList(5);
		if(bestChoices.size() > 0) {
			Random rand = new Random();
			int choice = rand.nextInt(bestChoices.size());
			gainCard(bestChoices.get(choice).getName());
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
