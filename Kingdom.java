package dominionAgents;

import java.util.ArrayList;

public class Kingdom {

	private int emptySupplyPiles = 0;
	private ArrayList<SupplyPile> supply;
	private ArrayList<Card> trash;
	
	//default constructor(2 player setup)
	public Kingdom() {
		//initialize empty trash pile
		trash = new ArrayList<Card>();
		
		//initialize supply
		supply = new ArrayList<SupplyPile>();
		//Add 2 player defaults for VP and treasure cards
		supply.add(new SupplyPile(new Card("Province"), 8));
		supply.add(new SupplyPile(new Card("Duchy"), 8));
		supply.add(new SupplyPile(new Card("Estate"), 8));
		supply.add(new SupplyPile(new Card("Copper"), 60));
		supply.add(new SupplyPile(new Card("Silver"), 40));
		supply.add(new SupplyPile(new Card("Gold"), 30));
		supply.add(new SupplyPile(new Card("Curse"), 10));
		
		ArrayList<Card> cards = CardData.getRandomKingdom();
		
		for(int i = 0;i < cards.size();i ++) {
			supply.add(new SupplyPile(cards.get(i),10));
		}
		
	}
	
	//return number of empty supply piles
	public int emptySupplyPiles() {
		return emptySupplyPiles;
	}
	
	//find card index by name
	public int kingdomIndex(String name) {
		for(int i = 0;i < supply.size();i ++) {
			if(name.equalsIgnoreCase(supply.get(i).getName())) {
				return i;
			}
		}
		return -1;
	}
	
	//decrement card count(given name)
	public void cardRemoved(String name) {
		int index = kingdomIndex(name);
		supply.get(index).decrementRemaining();
		if(supply.get(index).getCardsRemaining() == 0) {
			emptySupplyPiles ++;
		}
	}
	
	//decrement card count(given index)
	public void cardRemoved(int index) {
		supply.get(index).decrementRemaining();
		if(supply.get(index).getCardsRemaining() == 0) {
			emptySupplyPiles ++;
		}
	}
	
	public ArrayList<SupplyPile> getSupplyPiles(){
		return supply;
	}
	
	public void addCardToTrashPile(Card c) {
		trash.add(c);
	}
	
	public String toString() {
		String s = "";
		for(SupplyPile sp: supply) {
			s += sp.getName() + ": " + sp.getCardsRemaining() + "\n";
		}
		return s;
	}
	
}
