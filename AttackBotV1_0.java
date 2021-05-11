package dominionAgents;

import java.util.ArrayList;
import java.util.Random;

public class AttackBotV1_0 extends BasicBotV1_0{
	
	boolean attacksInSupply = false;
	
	public AttackBotV1_0(Kingdom k, PlayerCommunication pc) {
		super(k, pc);
		
		ArrayList<SupplyPile> cardsInSupply = k.getSupplyPiles();
		for(int i = 0;i < cardsInSupply.size() && !attacksInSupply;i ++) {
			Card c = cardsInSupply.get(i).getCard();
			if(c.isCardType(CardData.CardType.ATTACK)) {
				attacksInSupply = true;
			}
		}
		
	}
	
	protected void resolveBuyPhase() {

		if (buys >= 1){
			if(attacksInSupply) {
					//buy an attack card if possible
					Card potentialCard = getHighestAttackPossible(coins);
					if(potentialCard != null) {
						buyCard(potentialCard.getName());
					}
					
					super.resolveBuyPhase();
			}else {
				super.resolveBuyPhase();
			}
		}
	}
	
	public Card getHighestAttackPossible(int numCoins) {
		Card c = null;
		int highestFound = 0;
		for(SupplyPile sp:kingdom.getSupplyPiles()) {
			Card supplyCard = sp.getCard();
			if(supplyCard.isCardType(CardData.CardType.ATTACK) && supplyCard.getCost() <= numCoins && supplyCard.getCost() > highestFound) {
				highestFound = supplyCard.getCost();
				c = supplyCard;
			}
		}
		return c;
	}
	
}
