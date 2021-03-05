package dominionAgents;

import java.util.ArrayList;

public class Card {

	private String name;
	private ArrayList<Integer> cardTypes;
	private int actionsAdded;
	private int cardsAdded;
	private int coinsAdded;
	private int buysAdded;
	private boolean customEffect;
	private int cost;
	
	public Card(String name) {
		
		this.name = name;
		CardData.setupCard(this, name);
		
	}

	public ArrayList<Integer> getCardTypes() {
		return cardTypes;
	}

	public void setCardTypes(ArrayList<Integer> cardTypes) {
		this.cardTypes = cardTypes;
	}

	public int getActionsAdded() {
		return actionsAdded;
	}

	public void setActionsAdded(int actionsAdded) {
		this.actionsAdded = actionsAdded;
	}

	public int getCardsAdded() {
		return cardsAdded;
	}

	public void setCardsAdded(int cardsAdded) {
		this.cardsAdded = cardsAdded;
	}

	public int getCoinsAdded() {
		return coinsAdded;
	}

	public void setCoinsAdded(int coinsAdded) {
		this.coinsAdded = coinsAdded;
	}

	public int getBuysAdded() {
		return buysAdded;
	}

	public void setBuysAdded(int buysAdded) {
		this.buysAdded = buysAdded;
	}

	public boolean isCustomEffect() {
		return customEffect;
	}

	public void setCustomEffect(boolean customEffect) {
		this.customEffect = customEffect;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		return cost;
	}

	public boolean isCardType(CardData.CardType ct) {
		//convert CardType enum value to int
		int given = ct.ordinal();
		//compare this int to all Integers in cardTypes
		for(Integer i: cardTypes) {
			if(i == given) {
				//card is of requested type
				return true;
			}
		}
		//card type not found in cardTypes
		return false;
	}
	
	public String getName() {
		return name;
	}
	
}
