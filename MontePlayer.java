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
        ArrayList<Card> cards = highestCostList(coins);
        ArrayList<Node> cardNodes = new ArrayList<Node>();
        AtomicReference<Integer> parentCount = new AtomicReference<Integer>(0);
        // Generate Random Simulations with a purchase of a selected card.
        for (Card c : cards){
            //System.out.print(c.getName() + " : ");
            Node n = new Node(c.getName());
            cardNodes.add(n);
            runSimulations(n, cardNodes, parentCount);
        }
        //System.out.println();
        if (cardNodes.size() <= 0){
            return "NA";
        }

        // Run Simulations.
        //for (Node n : cardNodes){
            for (int i = 0; i < randomSimNum; i++){
                Node highestNode = getHighestUCTChildNode(cardNodes);
                runSimulations(highestNode, cardNodes, parentCount);
            }                
        //}

        /*double avgScore = 0;
        String mostVisited = "NA";
        for (Node node : cardNodes){
            if (node.getAvgScore() / node.getNodeVisits() > avgScore){
                avgScore = node.getAvgScore() / node.getNodeVisits();
                mostVisited = node.getCardName();
            }
        }*/

        // Return the most visited card.
        // Get the most visited node.
        String mostVisited = getMostVisited(cardNodes);

        return mostVisited;
    }

    @Override
    protected void resolveBuyPhase() {
        if (buys > 0){
            if(turnNumber % 6 == 0){
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

    // Run a number of simulations.
    private void runSimulations(Node n, ArrayList<Node> cardNodes, AtomicReference<Integer> pVisits){
        // Run a game and update 
        playGames(n, pVisits);
        updateUCT(cardNodes, pVisits);
    }


    // Get the Highest UCT Child Node to select.
    private Node getHighestUCTChildNode(ArrayList<Node> cardNodes){
        double highestUCT = Double.NEGATIVE_INFINITY;
        Node highestUCTNode = null;
        for (Node n : cardNodes){
            if (n.getUCTVal() >= highestUCT){
                highestUCT = n.getUCTVal();
                highestUCTNode = n;
            }
        }
        return highestUCTNode;
    }

    // Update UCT Values for all Nodes / cardNodes
    private void updateUCT(ArrayList<Node> cardNodes, AtomicReference<Integer> parentCount){
        for (Node n : cardNodes){
            double uct = Double.NEGATIVE_INFINITY;
            if (n.getNodeVisits() > 0){
                uct =  ((n.getNodewins()/(double)n.getNodeVisits()) + (uctConst * Math.sqrt(Math.log((double)parentCount.get()) / (double)n.getNodeVisits())));
                n.setUCTVal(uct);        
            }
            //System.out.println("Card : " + n.getCardName() + " ::: " + uct);
        }
    }   

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
        n.setNodeVisits(n.getNodeVisits() + 1);
        if (result == 0){
            n.setNodewins(n.getNodewins() + 1);
        }
        if (result == 1){
            n.setNodewins(n.getNodewins() - 1);
        }
        pVisits.set(pVisits.get() + 1);
    }

    private String getMostVisited(ArrayList<Node> cardNodes){
        double visits = 0;
        Node bestNode = null;
        for (Node n : cardNodes){
            if (n.getNodeVisits() > visits){
                visits = n.getNodeVisits();
                bestNode = n;
            }
        }
        return bestNode.getCardName();
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

    private ArrayList<Card> highestCostListTwo(int cost){
		int highestPossible = 0;
		
		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		ArrayList<Card> choices = new ArrayList<Card>();
		for(SupplyPile sp: supply) {
			Card c = sp.getCard();
			if(c.getCost() <= cost && sp.getCardsRemaining() > 0 && !c.getName().equalsIgnoreCase("CURSE")) {
				choices.add(c);
				if(c.getCost() > highestPossible) {
					highestPossible = c.getCost();
				}
			}
		}
		
		ArrayList<Card> mostExpensiveChoices = new ArrayList<Card>();
		
		if(choices.size() > 0) {
			for(Card c: choices) {
				if(c.getCost() == highestPossible) {
					mostExpensiveChoices.add(c);
				}
			}
		}
		
		return mostExpensiveChoices;
	}

    private final int randomSimNum = 25;
    private final double uctConst = Math.sqrt(3);
}