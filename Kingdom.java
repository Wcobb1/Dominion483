package dominionAgents;

import java.util.ArrayList;

public class Kingdom{

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
	
	public Kingdom(Kingdom k){
		this.trash = new ArrayList<Card>();
		for (Card c : k.trash){
			Card card = new Card(c.getName());
			trash.add(card);
		}

		this.supply = new ArrayList<SupplyPile>();
		for (SupplyPile sp : k.supply){
			SupplyPile s = new SupplyPile(sp.getCard(), sp.getCardsRemaining());
			this.supply.add(s);
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
	
	public String cardNameFromIndex(int index) {
		return supply.get(index).getName();
	}
	
	public boolean canBuy(String name) {
		int index = kingdomIndex(name);
		if(supply.get(index).getCardsRemaining() > 0) {
			return true;
		}
		return false;
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
