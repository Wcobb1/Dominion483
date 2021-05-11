package dominionAgents;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import dominionAgents.CardData.CardType;

class MonteSerial extends BasicBotV1_0 {
    public MonteSerial(Kingdom k, PlayerCommunication pc){
        super(k, pc);
    }

    // Monte Carlo Method
    public String getBestCard(){
        // Get a list of highest affordable cards.
        ArrayList<Thread> threadList = new ArrayList<>();
        ArrayList<MonteSimThread> simList = new ArrayList<>();
        ArrayList<Card> cards = highestCostList(coins);
        ArrayList<Node> cardNodes = new ArrayList<Node>();
        AtomicReference<Integer> parentCount = new AtomicReference<Integer>(0);

        // Create Nodes
        for (Card c : cards){
            cardNodes.add(new Node(c.getName(), c.getCost()));
        }        
        
        cardNodes.add(new Node());
        
        // Generate Simulations with a purchase of a selected card.
        for (Node n : cardNodes){            
            // Parallelism
            for(int i = 0; i < 3; i++){
                MonteSimThread simN = new MonteSimThread(n.getCardName(), n, parentCount, kingdom, null);
                simList.add(simN);
                simN.run();
                threadList.add(simN.getThread());    
            }
        }

        if (cardNodes.size() <= 0){
            return "NA";
        }
            

        // Return the most visited card.
        // Get the most visited node.
        String cardToBuy = "NA";

        cardToBuy = bestStatCard(cardNodes);
    
        return cardToBuy;
    }

    @Override
    protected void resolveBuyPhase() {
        if (buys > 0){
            if(turnNumber <= 1 || turnNumber % TURNS_TILL_MONTE == 0){
                String c = getBestCard();
                if (!c.equalsIgnoreCase("NA") && buys >= 1){
                    buyCard(c);
                    if (buys > 0 && coins > 1){
                        resolveBuyPhase();
                    }
                }
            }
            else {
                super.resolveBuyPhase();
            }
        }
    }


    //// Private /////
    private String bestStatCard(ArrayList<Node> cardNodes){
        double winRate = 0;
        double avgScore = 0;
        String c = "NA";
        for (Node n : cardNodes){
            double nodeWRate = n.getNodewins() / n.getNodeVisits();
            double nodeAScore = n.getAvgScore() / n.getNodewins();
            if ((nodeWRate * nodeAScore) > (winRate * avgScore)){
                winRate = nodeWRate;
                avgScore = nodeAScore;
                c = n.getCardName();
            }
        }
        return c;
    }
    

    private ArrayList<Card> highestCostList(int cost){
		int highestTreasure = 0;
        int highestVictory = 0;

		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		ArrayList<Card> choices = new ArrayList<Card>();
		for(SupplyPile sp: supply) {
			Card c = sp.getCard();
			if(c.getCost() <= cost && sp.getCardsRemaining() > 0 && !c.getName().equalsIgnoreCase("CURSE")) {
                if (c.isCardType(CardType.VICTORY) || c.isCardType(CardType.TREASURE)){
                    choices.add(c);
                    if (c.isCardType(CardType.VICTORY) && c.getCost() > highestVictory){
                        highestVictory = c.getCost();
                    }
                    if (c.isCardType(CardType.TREASURE) && c.getCost() > highestTreasure){
                        highestTreasure = c.getCost();
                    }    
                }
			}
		}
		
		ArrayList<Card> mostExpensiveChoices = new ArrayList<Card>();
		
		if(choices.size() > 0) {
			for(Card c: choices) {
				if(c.getCost() == highestTreasure && c.isCardType(CardType.TREASURE)) {
					mostExpensiveChoices.add(c);
				}
                if(c.getCost() == highestVictory && c.isCardType(CardType.VICTORY)){
                    mostExpensiveChoices.add(c);
                }
			}
		}
		
		return mostExpensiveChoices;
	}

    private final int TURNS_TILL_MONTE = 3;
}