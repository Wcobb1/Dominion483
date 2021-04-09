package dominionAgents;

import java.util.ArrayList;

public class DecisionTreePlayerV1_0 extends BasicBotV1_0{

	private SortableGiniCardList earlyPrioList;
	
	public DecisionTreePlayerV1_0(Kingdom k, PlayerCommunication pc, SortableGiniCardList earlyPrioList) {
		super(k, pc);
		this.earlyPrioList = earlyPrioList;
		removeUnusedCards();
	}
	
	public void removeUnusedCards() {
		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		for(int i = 0;i < earlyPrioList.size();i ++) {
			boolean found = false;
			for(int j = 0;j < supply.size() && !found;j ++) {
				if(supply.get(j).getName().equalsIgnoreCase(earlyPrioList.get(i).getName())) {
					found = true;
				}
			}
			if(!found) {
				earlyPrioList.remove(i);
				i --;
			}
		}
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
		if(turnNumber < 3) {
			String cardName = chooseHighestPriorityCardForCost(coins, earlyPrioList);
			if(!cardName.equalsIgnoreCase("NA")) {
				buyCard(cardName);
			}
		}
		super.resolveBuyPhase();
	}

}
