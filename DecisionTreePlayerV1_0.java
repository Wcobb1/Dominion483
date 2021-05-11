package dominionAgents;

import java.util.ArrayList;

public class DecisionTreePlayerV1_0 extends BasicBotV1_0{

	private SortableGiniCardList earlyPrioList;
	private SortableGiniCardList midPrioList;
	private SortableGiniCardList latePrioList;
	
	public DecisionTreePlayerV1_0(Kingdom k, PlayerCommunication pc, SortableGiniCardList earlyPrioList, SortableGiniCardList midPrioList, SortableGiniCardList latePrioList) {
		super(k, pc);
		this.earlyPrioList = earlyPrioList;
		this.midPrioList = midPrioList;
		this.latePrioList = latePrioList;
		removeUnusedCards();
	}
	
	public void removeUnusedCardsFromList(SortableGiniCardList sgcl) {
		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		for(int i = 0;i < sgcl.size();i ++) {
			boolean found = false;
			for(int j = 0;j < supply.size() && !found;j ++) {
				if(sgcl != null) {
					if(sgcl.get(i) != null) {
						if(supply.get(j) != null) {
							if(supply.get(j).getName().equalsIgnoreCase(sgcl.get(i).getName())) {
								found = true;
							}
						}
					}
				}
			}
			if(!found) {
				sgcl.remove(i);
				i --;
			}
		}
	}
	
	public void removeUnusedCards() {
		removeUnusedCardsFromList(earlyPrioList);
		removeUnusedCardsFromList(midPrioList);
		removeUnusedCardsFromList(latePrioList);
	}
	
	public String chooseHighestPriorityCardForCost(int cost, SortableGiniCardList sgcl) {
		for(SortableGiniCard sgc:sgcl) {
			if(sgc.getCost() == cost) {
				return sgc.getName();
			}
		}
		//recurse with lower cost
		if(cost > 1) {
			chooseHighestPriorityCardForCost(cost-1, sgcl);
		}
		return "NA";
	}
	
	public void resolveBuyPhase() {
		String cardName = "NA";
		if(turnNumber < 3) {
			cardName = chooseHighestPriorityCardForCost(coins, earlyPrioList);
		}else if(turnNumber < 14) {
			cardName = chooseHighestPriorityCardForCost(coins, midPrioList);
		}else {
			cardName = chooseHighestPriorityCardForCost(coins, latePrioList);
		}
		if(!cardName.equalsIgnoreCase("NA")) {
			buyCard(cardName);
		}
		super.resolveBuyPhase();
	}

}
