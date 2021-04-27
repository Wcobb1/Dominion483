package dominionAgents;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import dominionAgents.CardData.CardType;

class MontePlayer extends BasicBotV1_0 {
    public MontePlayer(Kingdom k, PlayerCommunication pc){
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
        // Generate Simulations with a purchase of a selected card.
        for (Card c : cards){
            //System.out.print(c.getName() + " : ");
            Node n = new Node(c.getName());
            cardNodes.add(n);
            
            // Parallelism
            MonteSimThread simN = new MonteSimThread(c.getName(), n, parentCount, kingdom);
            simList.add(simN);
            simN.start();
            threadList.add(simN.getThread());
        }

        try {
            for (int i = 0; i < threadList.size(); i++){
                threadList.get(i).join();
            }
        } catch (InterruptedException e) {
            System.out.println("MontePlayer thread Interrupted");
        }

        //System.out.println();
        if (cardNodes.size() <= 0){
            return "NA";
        }
 
        // Return the most visited card.
        // Get the most visited node.
        String mostVisited = bestStatCard(cardNodes);

        return mostVisited;
    }

    @Override
    protected void resolveBuyPhase() {
        if (buys > 0){
            if(turnNumber % TURNS_TIL_MONTE == 0){
                String c = getBestCard();
                //System.out.println(c);
                if (!c.equalsIgnoreCase("NA")){
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
    private void playGames(Node n, AtomicReference<Integer> pVisits){
        Kingdom k = new Kingdom(kingdom);
        PlayerCommunication playerC = new PlayerCommunication();
        Player us = new MoneyMakingBotV1_0_2(k, playerC, n.getCardName());
        Player opp = new MoneyMakingBotV1_0_2(k, playerC);
        GameSimulator rs = new GameSimulator(us, opp);
        int result = rs.runGame();
        int[] scores = rs.getScores();
        //System.out.print(scores[1] + " ");
        //System.out.println(scores[0]);
        n.setAvgScore(n.getAvgScore() + scores[0]);
        n.setNodeVisits(n.getNodeVisits() + 1.0);
        if (result == 0){
            n.setNodewins(n.getNodewins() + 1.0);
        }
        else if (result == 1){
            n.setNodewins(n.getNodewins() - 1.0);
        }
        else if (result == 2){
            n.setNodewins(n.getNodewins() + 0.5);
        }
        pVisits.set(pVisits.get() + 1);
    }


    private String bestStatCard(ArrayList<Node> cardNodes){
        double winRate = 0;
        double avgScore = 0;
        String c = "NA";
        for (Node n : cardNodes){
            double nodeWRate = n.getNodewins() / n.getNodeVisits();
            double nodeAScore = n.getAvgScore() / n.getNodewins();
            //System.out.println(n.getCardName() + " : " + "  :  " + nodeAScore);
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

    private final int TURNS_TIL_MONTE = 10;
}