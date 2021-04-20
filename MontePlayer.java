package dominionAgents;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class MontePlayer extends BasicBotV1_0 {
    public MontePlayer(Kingdom k, PlayerCommunication pc){
        super(k, pc);
    }

    public Card getBestPossibleCard(){
        // Get Kingdom Supply Pile
        ArrayList<SupplyPile> sp = kingdom.getSupplyPiles();
        ArrayList<Node> cards = new ArrayList<Node>();
        AtomicReference<Integer> parentCount = new AtomicReference<Integer>(0);
        // Get Cards purchasable
        for (SupplyPile s : sp){
            Card c = s.getCard();
            if(c.getCost() <= coins && s.getCardsRemaining() > 0){
                Node n = new Node(c);
                cards.add(n);
                playGames(n, kingdom, pc, parentCount);
                updateUCT(cards, parentCount);
            }
        }

        // Run a number of games with the kingdom state and the card Node with highest UCT Value
        int times = 25;
        for (int i = 0; i < times; i++){
            Node n = getHighestUCTChildNode(cards);
            playGames(n, kingdom, pc, parentCount);
            updateUCT(cards, parentCount);
        }
        int mostVisited = -1;
        Card c = null;
        for (Node i : cards){
            if (i.getNodeVisits() > mostVisited){
                mostVisited = i.getNodeVisits();
                c = i.getCard();
            }
        }
        //System.out.println(parentCount);

        return c;
    }

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

    public void playGames(Node n, Kingdom k, PlayerCommunication pc, AtomicReference<Integer> parentCount){
        int games = 5;

        for(int i = 0; i < games; i++){
            Kingdom newK = new Kingdom(k);
            PlayerCommunication p = pc;
            Player ourP = new BasicBotV1_0_2(newK, p, n.getCard());
            Player opp = new BasicBotV1_0(newK, p);
            GameSimulator gs = new GameSimulator(ourP, opp);
            int result = gs.runGame();
            n.setNodewins(n.getNodewins() + (double)result);
            n.setNodeVisits(n.getNodeVisits() + 1);
            parentCount.set(parentCount.get() + 1);
        }
    }

    public void updateUCT(ArrayList<Node> cards, AtomicReference<Integer> parentCount){
        for (Node n : cards){
            double uct = 0;
            uct = n.getNodewins() + (constant * (Math.sqrt(Math.log(parentCount.get()) / n.getNodeVisits())));
            n.setUCTVal(uct);    
            //System.out.print("UCT Values of Node with card ", n.getCard().getName(), " = ", n.getUCTVal());
        }
    }   

    public void resolveBuyPhase(){
        Card cardBuy = getBestPossibleCard();
        //System.out.println(cardBuy.getName());
        buyCard(cardBuy.getName());            
    }


    private double constant = (double)0.5;
}
