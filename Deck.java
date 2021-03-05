package dominionAgents;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

	//both the deck and discard are ArrayLists essentially used as stacks
	private ArrayList<Card> deck;
	private ArrayList<Card> discard;
	
	//Initialize empty discard pile and deck with 7 Copper, 3 Estate
	public Deck() {
		discard = new ArrayList<Card>();
		deck = new ArrayList<Card>();
		for(int i = 0;i < 7;i ++) {
			deck.add(new Card("Copper"));
		}
		for(int i = 0;i < 3;i ++) {
			deck.add(new Card("Estate"));
		}
		shuffleDeck();
	}
	
	//randomize order of all cards in deck
	public void shuffleDeck() {
		//in random order, pull cards out of deck and place them into newDeck
		ArrayList<Card> newDeck = new ArrayList<Card>();
		while(deck.size() > 0) {
			Random rand = new Random();
			int chosenIndex = rand.nextInt(deck.size());
			newDeck.add(0,deck.get(chosenIndex));
			deck.remove(chosenIndex);
		}
		//deck is replaced with newDeck
		deck = newDeck;
	}

	//returns true if 0 cards in deck, false otherwise
	private boolean deckEmpty() {
		if(deck.size() < 1) {
			return true;
		}else {
			return false;
		}
	}
	
	//check if the deck is empty, if so, shuffle discard pile(if any) into deck
	private void checkDeckEmpty() {
		//only run if deck is empty
		if(deck.size() < 1) {
			//move all cards from discard pile to deck
			for(int i = 0;i < discard.size();i ++) {
				deck.add(discard.get(i));
				discard.remove(discard.get(i));
			}
			//now shuffle new deck
			shuffleDeck();
		}
	}
	
	//remove and return top card of deck, null if deck is empty
	public Card popTopCard() {
		checkDeckEmpty();
		if(!deckEmpty()) {
			Card c = deck.get(0);
			deck.remove(0);
			return c;
		}
		return null;
	}

	//place a card on top of the deck
	public void placeCardOnDeck(Card cardToReturn) {
		deck.add(0, cardToReturn);
	}
	
	//add a card to the discard pile
	public void addCardToDiscard(Card c) {
		discard.add(0,c);
	}
	
	public void printDeckAndDiscard() {
		System.out.print("Deck: ");
		for(Card c: deck) {
			System.out.print(c.getName() + ", ");
		}
		System.out.print("\nDiscard: ");
		for(Card c: discard) {
			System.out.print(c.getName() + ", ");
		}
		System.out.print("\n");
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public ArrayList<Card> getDiscard() {
		return discard;
	}
	
}
