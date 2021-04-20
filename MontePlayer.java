package dominionAgents;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import dominionAgents.CardData.CardType;

/*
    1. Check BasicBot_V1_0_2's resolve.
    2. Stuck in a potential "infinite" loop. So, got to fix that.
*/

public class MontePlayer extends BasicBotV1_0 {
    public MontePlayer(Kingdom k, PlayerCommunication pc){
        super(k, pc);
    }

    public String getBestPossibleCard(){
        // Get Kingdom Supply Pile
        ArrayList<SupplyPile> sp = kingdom.getSupplyPiles();
        ArrayList<Node> cards = new ArrayList<Node>();
        AtomicReference<Integer> parentCount = new AtomicReference<Integer>(0);
        // Get Cards purchasable
        for (SupplyPile s : sp){
            Card c = s.getCard();
            if(c.getCost() <= coins && s.getCardsRemaining() > 0){
                //System.out.println("For Card " + c.getName());
                Node n = new Node(c.getName());
                cards.add(n);
                for (int i = 0; i < 1; i++){
                    playGame(n, kingdom, pc, parentCount);
                    updateUCT(cards, parentCount);    
                }
                //System.out.println();
            }
        }
        // Add NA in case, decision to not buy a card.
        Node nA = new Node();
        cards.add(nA);
        for (int i = 0; i < 1; i++){
            playGame(nA, kingdom, pc, parentCount);
            updateUCT(cards, parentCount);    
        }
        //System.out.println();
        // Run a number of games with the kingdom state and the card Node with highest UCT Value
        int times = 10;
        for (int i = 0; i < times; i++){
            Node n = getHighestUCTChildNode(cards);
            System.out.println(n.getCardName());
            playGame(n, kingdom, pc, parentCount);
            updateUCT(cards, parentCount);
            //System.out.println();
        }
        String cName = null;
        Node bestNode = getHighestUCTChildNode(cards);
        cName = bestNode.getCardName();
        //System.out.println("End of getBestCard()");

        return cName;
    }

    // Get the Highest UCT Child Node to select.
    public Node getHighestUCTChildNode(ArrayList<Node> cards){
        double highestUCT = 0;
        Node highestUCTNode = null;
        for (Node n : cards){
            if (n.getUCTVal() > highestUCT){
                highestUCT = n.getUCTVal();
                highestUCTNode = n;
            }
        }
        return highestUCTNode;
    }

    // Random Simulations with first car bought being the card in the Node (n).
    public void playGame(Node n, Kingdom k, PlayerCommunication pc, AtomicReference<Integer> parentCount){
        //int games = 50;
        Kingdom newK = new Kingdom(k);
        PlayerCommunication p = pc;
        Player ourP = new BasicBotV1_0_2(newK, p, n.getCardName());
        Player opp = new BasicBotV1_0_2(newK, p, "NA");
        GameSimulator gs = new GameSimulator(ourP, opp);
        int result = gs.runGame();
        if (result == 1){
            n.setNodewins(n.getNodewins() + (double)result);
        }
        if (result == 0){
            n.setNodewins(n.getNodewins() + (double)0.5);
        }
        n.setNodeVisits(n.getNodeVisits() + 1);
        parentCount.set(parentCount.get() + 1);
    }

    // Update UCT Values for all Nodes/Cards
    public void updateUCT(ArrayList<Node> cards, AtomicReference<Integer> parentCount){
        for (Node n : cards){
            double uct = Double.NEGATIVE_INFINITY;
            if (n.getNodeVisits() > 0){
                uct = (n.getNodewins()/(double)n.getNodeVisits()) + (constant * Math.sqrt(Math.log((double)parentCount.get()) / (double)n.getNodeVisits()));
                n.setUCTVal(uct);        
            }
            System.out.println("Card : " + n.getCardName() + " ::: " + uct);
        }
    }   

    //
    protected void resolveBuyPhase(){
        if (buys > 0){
            String cardBuy = "NA";
            cardBuy = getBestPossibleCard();
            //System.out.println("\n Coins :" + coins);
            System.out.println("Card Bought :" + cardBuy);
            if(!cardBuy.equalsIgnoreCase("NA")) {
                buyCard(cardBuy);
            }
        }
        if (buys > 0 && coins > 1){
            resolveBuyPhase();
        }
        super.resolveBuyPhase();
    }


    private double constant = Math.sqrt(2);
}
