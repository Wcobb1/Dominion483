package dominionAgents;

import java.util.ArrayList;

import dominionAgents.CardData.CardType;
import dominionAgents.PlayerCommunication.MessageCode;

public abstract class Player {
	
	protected Deck deck;
	protected Hand hand;
	protected ArrayList<Card> inPlay;
	protected ArrayList<Card> cardsGained;
	protected int actions;
	protected int coins;
	protected int buys;
	protected Kingdom kingdom;
	protected String turnLog;
	protected PlayerCommunication pc;
	protected int cardsPlayedThisTurn;
	//used for stats
	protected int turnNumber = 0;
	protected PlayerStats ps;
	
	//constructor
	public Player(Kingdom k, PlayerCommunication pc) {
		deck = new Deck();
		hand = new Hand(deck);
		kingdom = k;
		//initial 5 card draw for hand
		drawHand();
		//initialize ArrayLists to be empty
		inPlay = new ArrayList<Card>();
		cardsGained = new ArrayList<Card>();
		this.pc = pc;
		ps = new PlayerStats(k);
	}
	
	/**Abstract methods(must be implemented in child classes)**/
	//plays action cards
	protected abstract void resolveActionPhase();
	
	//uses available coins & buys to buy card(s) of choice
	protected abstract void resolveBuyPhase();
	
	public abstract Card chooseCardForMine();
	
	public abstract boolean keepOrDiscardForLibrary(Card c);
	
	public abstract Card chooseActionCardForThroneRoom();
	
	public abstract void resolveRemodel();
	
	public abstract void resolveWorkshop();
	
	public abstract void resolveFeast();
	
	public abstract ArrayList<Card> chooseCardsForCellar();
	
	public abstract void resolveChapel();
	
	public abstract boolean chancellorDeckToDiscard();
	
	public abstract boolean discardOrPutBack(Card c, PlayerCommunication.PlayerCode pcd);
	
	public abstract boolean chooseToGainCard(Card c);
	
	public abstract void discardDownTo(int numLeft);
		
	//draw 5 cards
	public void drawHand() {
		for(int i = 0;i < 5;i ++) {
			hand.drawCard();
		}
	}
	
	public Kingdom getKingdom() {
		return kingdom;
	}
	
	public Hand getHand() {
		return hand;
	}
	
	public Deck getDeck() {
		return deck;
	}
	
	public void takeTurn() {
		//initialize values
		actions = 1;
		coins = 0;
		buys = 1;
		turnLog = "";
		cardsPlayedThisTurn = 0;
		
		//stats
		turnNumber ++;
		
		//for game log
		turnLog += "Cards Played: ";
		
		//Play phase
		playActionAdders();
		resolveActionPhase();
		playTreasures();
		
		//for game log
		turnLog += "\nCards Gained: ";
		
		//Buy phase
		resolveBuyPhase();
		
		turnLog += "\n";
		
		//finish turn
		cleanup();
	}
	
	//discard hand and draw 5 new cards to hand, reset values
	public void cleanup() {
		
		//reset coins, actions, & buys
		coins = 0;
		actions = 1;
		buys = 1;
		
		while(inPlay.size() > 0) {
			Card c = inPlay.get(0);
			deck.addCardToDiscard(c);
			inPlay.remove(0);
		}
		
		while(cardsGained.size() > 0) {
			//for stats/analysis/training
			if(turnNumber < 3) {
				for(Card c:cardsGained) {
					ps.addCardFirstTwoTurns(c);
				}
			}else if(turnNumber < 14) {
				for(Card c:cardsGained) {
					ps.addCardMidGame(c);
				}
			}else {
				for(Card c:cardsGained) {
					ps.addCardLateGame(c);
				}
			}
			Card c = cardsGained.get(0);
			turnLog += c.getName() + ", ";
			deck.addCardToDiscard(c);
			cardsGained.remove(0);
		}
		
		hand.discardHand();
		drawHand();
	}
	
	//move card from hand to inPlay, update actions remaining, and call resolveCard
	public void playCard(Card c) {
		//log card played
		turnLog += c.getName() + ", ";
		//remove card from hand
		hand.removeCard(c);
		//playing an action card costs 1 action
		if(c.isCardType(CardData.CardType.ACTION)) {
			actions --;
		}
		//add card to inPlay ArrayList<Card>
		inPlay.add(0,c);
		//resolve card functionality
		resolveCard(c);
		cardsPlayedThisTurn ++;
	}
	
	//resolve functionality of card
	public void resolveCard(Card c) {
		if(c.isCardType(CardData.CardType.ACTION)) {
			resolveAction(c);
		}
		if(c.isCardType(CardData.CardType.TREASURE)){
			resolveTreasure(c);
		}
	}
	
	public void resolveAction(Card c) {
		//Step 1: resolve basic adders
		actions += c.getActionsAdded();
		coins += c.getCoinsAdded();
		buys += c.getBuysAdded();
		//Step 2: draw cards
		for(int i = 0;i < c.getCardsAdded();i ++) {
			hand.drawCard();
		}
		//Step 3: If card has other custom functionality, pass off to resolveAdvancedAction(c)
		if(c.isCustomEffect()) {
			resolveAdvancedAction(c);
		}
	}
	
	//add card to cardsGained ArrayList<Card> and update corresponding card's SupplyPile object in kingdom
	public void buyCard(String cardName) {
		if(kingdom.canBuy(cardName)) {
			Card newCard = new Card(cardName);
			if(coins >= newCard.getCost() && buys > 0) {
				cardsGained.add(newCard);
				kingdom.cardRemoved(cardName);
				buys --;
				coins -= newCard.getCost();
			}
		}
	}
	
	//same as buyCard, but does not deal with buys or coins
	public void gainCard(String cardName) {
		Card newCard = new Card(cardName);
		cardsGained.add(newCard);
		kingdom.cardRemoved(cardName);
	}
	
	//For those cards that require a bit more work
	public void resolveAdvancedAction(Card c) {
		String name = c.getName();
		if(name.equalsIgnoreCase("WITCH")){
			pc.sendMessageToAllOtherPlayers(this, MessageCode.ENEMY_WITCH_PLAYED);
		}else if(name.equalsIgnoreCase("MINE")){
			resolveMine();
		}else if(name.equalsIgnoreCase("LIBRARY")){
			resolveLibrary();
		}else if(name.equalsIgnoreCase("COUNCIL ROOM")){
			pc.sendMessageToAllOtherPlayers(this, MessageCode.DRAW_CARD);
		}else if(name.equalsIgnoreCase("THRONE ROOM")){
			resolveThroneRoom();
		}else if(name.equalsIgnoreCase("REMODEL")){
			resolveRemodel();
		}else if(name.equalsIgnoreCase("MONEYLENDER")){
			boolean done = false;
			for(int i = 0;i < hand.getHand().size() && !done;i ++) {
				if(hand.getHand().get(i).getName().equalsIgnoreCase("Copper")) {
					trashCard(hand.getHand(), hand.getHand().get(i));
					coins += 3;
					done = true;
				}
			}
		}else if(name.equalsIgnoreCase("MILITIA")){
			pc.sendMessageToAllOtherPlayers(this, MessageCode.ENEMY_MILITIA_PLAYED);
		}else if(name.equalsIgnoreCase("BUREAUCRAT")){
			pc.sendMessageToAllOtherPlayers(this, MessageCode.ENEMY_BUREAUCRAT_PLAYED);
		}else if(name.equalsIgnoreCase("WORKSHOP")){
			resolveWorkshop();
		}else if(name.equalsIgnoreCase("CHAPEL")){
			resolveChapel();
		}else if(name.equalsIgnoreCase("CELLAR")){
			resolveCellar();
		}else if(name.equalsIgnoreCase("FEAST")){
			resolveFeast();
			trashCard(inPlay, c);
		}else if(name.equalsIgnoreCase("CHANCELLOR")){
			boolean discard = chancellorDeckToDiscard();
			if(discard) {
				while(deck.getDeck().size() > 0) {
					deck.addCardToDiscard(deck.popTopCard());
				}
			}
		}else if(name.equalsIgnoreCase("ADVENTURER")){
			ArrayList<Card> revealedUnused = new ArrayList<Card>();
			ArrayList<Card> treasures = new ArrayList<Card>();
			Card revealedCard = deck.popTopCard();
			while(treasures.size() < 2 && revealedCard != null) {
				if(revealedCard.isCardType(CardData.CardType.TREASURE)) {
					treasures.add(revealedCard);
				}else {
					revealedUnused.add(revealedCard);
				}
				revealedCard = deck.popTopCard();
			}
			//place treasure cards into hand
			while(treasures.size() > 0) {
				hand.getHand().add(treasures.get(0));
				treasures.remove(0);
			}
			//discard other revealed cards
			while(revealedUnused.size() > 0) {
				deck.addCardToDiscard(revealedUnused.get(0));
				revealedUnused.remove(0);
			}
			
		}else if(name.equalsIgnoreCase("SPY")){
			pc.playSpy(this);
		}else if(name.equalsIgnoreCase("THIEF")){
			pc.playThief(this);
		}
	}
	
	public void trashCard(ArrayList<Card> source, Card c) {
		source.remove(c);
		kingdom.addCardToTrashPile(c);
	}
	
	//Play all trasure cards in hand
	public void playTreasures() {
		for(int i = 0;i < hand.getHand().size();i ++) {
			if(hand.getHand().get(i).isCardType(CardData.CardType.TREASURE)) {
				playCard(hand.getHand().get(i));
				i --;
			}
		}
	}
	
	public void resolveTreasure(Card c) {
		coins += c.getCoinsAdded();
	}
	
	public void playActionAdders() {
		//Step 1: Play action adders
		for(int i = 0;i < hand.getHand().size();i ++) {
			if(hand.getHand().get(i).getActionsAdded() > 0) {
				//play action adder
				playCard(hand.getHand().get(i));
				//recurse to step 1(it's possible new cards were drawn, for example, if a village were played in this step)
				playActionAdders();
			}
		}
	}
	
	public void resolveMine() {
		//Child classes must define chooseCardForMine()
		Card highestTreasure = chooseCardForMine();
		//only continue of highest treasure is non-null
		if(highestTreasure != null) {
			int cost = highestTreasure.getCost();
			//remove card from hand
			hand.getHand().remove(highestTreasure);
			//add card to trash pile
			kingdom.addCardToTrashPile(highestTreasure);
			
			//gain new card
			if(cost == 0) {
				//new card is Silver
				gainCard("Silver");
			}else if(cost == 3){
				//new card is Gold
				gainCard("Gold");
			}
			
		}
	}
	
	public void resolveCellar() {
		ArrayList<Card> cardsToDiscard = chooseCardsForCellar();
		
		int cardsDiscarded = 0;
		while(cardsToDiscard.size() > 0) {
			//discard cards from hand
			hand.removeCard(cardsToDiscard.get(0));
			deck.addCardToDiscard(cardsToDiscard.get(0));
			cardsToDiscard.remove(0);
			cardsDiscarded ++;
		}
		
		//draw cards equal to the number of the cards discarded
		while(cardsDiscarded > 0) {
			hand.drawCard();
			cardsDiscarded --;
		}
		
	}
	
	public void resolveThroneRoom() {
		Card chosenCard = chooseActionCardForThroneRoom();
		if(chosenCard != null) {
			//put card in play
			inPlay.add(chosenCard);
			//resolve card's action twice
			resolveAction(chosenCard);
			resolveAction(chosenCard);
		}
	}
	
	public void resolveLibrary() {
		ArrayList<Card> setAside = new ArrayList<Card>();
		Card cardDrawn = deck.popTopCard();
		while(hand.getHand().size() < 7 && cardDrawn != null) {
			boolean keep = true;
			if(cardDrawn.isCardType(CardData.CardType.ACTION)) {
				keep = keepOrDiscardForLibrary(cardDrawn);
			}
			if(keep) {
				//keep card
				hand.getHand().add(cardDrawn);
			}else {
				//discard
				setAside.add(cardDrawn);
			}
			cardDrawn = deck.popTopCard();
		}
		
		//discard all cards set aside
		while(setAside.size() > 0) {
			deck.addCardToDiscard(setAside.get(0));
			setAside.remove(0);
		}
		
	}
	
	//trivial to resolve, so defined here
	public void resolveEnemyBureaucrat() {
		boolean done = false;
		for(int i = 0;i < hand.getHand().size() && !done;i ++) {
			if(hand.getHand().get(i).isCardType(CardType.VICTORY)) {
				Card c = hand.getHand().get(i);
				hand.getHand().remove(i);
				deck.placeCardOnDeck(c);
				done = true;
			}
		}
	}
	
	public boolean cardInHand(String cardName) {
		for(Card c: hand.getHand()) {
			if(c.getName().equalsIgnoreCase(cardName)) {
				return true;
			}
		}
		return false;
	}
	
	//used for communication between players through PlayerCommunication object
	public void acceptMessage(PlayerCommunication.MessageCode mc) {
		switch(mc) {
		
			case ENEMY_WITCH_PLAYED:
				//having a Moat in hand blocks attack cards
				if(!cardInHand("Moat")) {
					//Curse pile
					SupplyPile sp = kingdom.getSupplyPiles().get(6);
					//Only gain curse card if there is at least 1 remaining
					if(sp.getCardsRemaining() > 0) {
						deck.addCardToDiscard(new Card("Curse"));
						kingdom.cardRemoved(6);
					}
				}
				break;
			case DRAW_CARD:
				hand.drawCard();
				break;
			case ENEMY_MILITIA_PLAYED:
				//having a Moat in hand blocks attack cards
				if(!cardInHand("Moat")) {
					if(hand.getHand().size() > 3) {
						discardDownTo(3);
					}
				}
				break;
			case ENEMY_BUREAUCRAT_PLAYED:
				//having a Moat in hand blocks attack cards
				if(!cardInHand("Moat")) {
					resolveEnemyBureaucrat();
				}
				break;
			default: 
				break;
		}
	}
	
	//return turnLog String updated each turn
	public String getTurnLog() {
		return turnLog;
	}
	
	public PlayerCommunication getPlayerCommunication() {
		return pc;
	}
	
	public int getNumCardsPlayed() {
		return cardsPlayedThisTurn;
	}
	
	public int scoreCard(String cardName, int totalCards) {
		int value = 0;
		if(cardName.equalsIgnoreCase("Province")) {
			value = 6;
		}else if(cardName.equalsIgnoreCase("Duchy")) {
			value = 3;
		}else if(cardName.equalsIgnoreCase("Estate")) {
			value = 1;
		}else if(cardName.equalsIgnoreCase("Curse")){
			value = -1;
		}else if(cardName.equalsIgnoreCase("Gardens")) {
			//truncated integer division
			value = totalCards/10;
		}
		return value;
	}
	
	//counts VPs and populates cardsOwned
	public int getVictoryPoints() {
		int victoryPoints = 0;
		
		int totalCards = hand.getHand().size() + deck.getDeck().size() + deck.getDiscard().size();
		
		//count hand
		for(Card c: hand.getHand()) {
			ps.addCardOwned(kingdom.kingdomIndex(c.getName()));
			if(c.isCardType(CardType.VICTORY) || c.isCardType(CardType.CURSE)) {
				victoryPoints += scoreCard(c.getName(), totalCards);
			}
		}
		//count deck
		for(Card c: deck.getDeck()) {
			ps.addCardOwned(kingdom.kingdomIndex(c.getName()));
			if(c.isCardType(CardType.VICTORY) || c.isCardType(CardType.CURSE)) {
				victoryPoints += scoreCard(c.getName(), totalCards);
			}
		}
		//count discard
		for(Card c: deck.getDiscard()) {
			ps.addCardOwned(kingdom.kingdomIndex(c.getName()));
			if(c.isCardType(CardType.VICTORY) || c.isCardType(CardType.CURSE)) {
				victoryPoints += scoreCard(c.getName(), totalCards);
			}
		}
		return victoryPoints;
	}
	
	public PlayerStats getPlayerStats() {
		return ps;
	}
	
}
