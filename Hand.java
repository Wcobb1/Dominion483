package dominionAgents;

import java.util.ArrayList;
import java.util.Objects;

public class Hand {

	//underlying ArrayList for cards in hand
	private ArrayList<Card> hand;
	//Deck object associated with Hand
	private Deck deck;
	
	//constructor
	public Hand(Deck deck) {
		this.deck = deck;
		hand = new ArrayList<Card>();
	}

	//draw a card from deck
	public void drawCard() {
		Card c = deck.popTopCard();
		if(Objects.nonNull(c)) {
			hand.add(0,c);
		}
	}
	
	//discard all cards in hand
	public void discardHand() {
		while(hand.size() > 0) {
			Card c = hand.get(0);
			hand.remove(0);
			deck.addCardToDiscard(c);
		}
	}
	
	//print out all cards in hand
	public void printHand() {
		System.out.print("Hand: ");
		for(Card c: hand) {
			System.out.print(c.getName() + ", ");
		}
		System.out.print("\n");
	}
	
	public void removeCard(Card c) {
		hand.remove(c);
	}
	
	public void discardCard(int index) {
		Card c = hand.get(index);
		hand.remove(c);
		deck.addCardToDiscard(c);
	}
	
	public ArrayList<Card> getHand(){
		return hand;
	}
	
}
