package dominionAgents;

public class SupplyPile {
	
	private Card c;
	private int cardsRemaining;
	
	public SupplyPile(Card c, int cardsRemaining) {
		this.c = c;
		this.cardsRemaining = cardsRemaining;
	}
	
	public String getName() {
		return c.getName();
	}
	
	public Card getCard() {
		return c;
	}
	
	public int getCardsRemaining() {
		return cardsRemaining;
	}
	
	public void decrementRemaining() {
		cardsRemaining --;
	}
	
}
