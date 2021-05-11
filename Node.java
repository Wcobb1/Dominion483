package dominionAgents;

import java.util.ArrayList;

public class Node {
    
    public Node(){
        this.cardName = "NA";
        this.visited = 0;
        this.wins = 0;
        this.uctVal = Double.NEGATIVE_INFINITY;
        //this.childArray = null;
        this.avgScore = 0;
        this.cost = 0;
    }

    public Node(String cardName, int cost){
        //this.c = new Card(cardName);
        this.cardName = cardName;
        //this.parent = null;
        this.visited = 0;
        this.wins = 0;
        this.uctVal = Double.NEGATIVE_INFINITY;
        //this.childArray = null;
        this.avgScore = 0;
        this.cost = cost;
    }

    /*public Node getNodeParent(){
        return this.parent;
    }

    public void setNodeParent(Node parent){
        this.parent = parent;
    }*/

    public double getNodeVisits(){
        return this.visited;
    }

    public void setNodeVisits(double visits){
        this.visited = visits;
    }

    public double getNodewins(){
        return this.wins;
    }

    public void setNodewins(double wins){
        this.wins = wins;
    }
    /*
    public Card getCard(){
        return this.c;
    }

    public void setCard(Card c){
        this.c = c;
    }
    */
    public double getUCTVal(){
        return this.uctVal;
    }

    public void setUCTVal(double uctVal){
        this.uctVal = uctVal;
    }
    /*
    public ArrayList<Node> getChildrenNodes(){
        return this.childArray;
    }

    public void setChildrenNodes(ArrayList<Node> children){
        this.childArray = children;
    }*/

    public String getCardName(){
        return this.cardName;
    }

    public void setCardName(String cardName){
        this.cardName = cardName;
    }

    public double getAvgScore(){
        return this.avgScore;
    }
    
    public void setAvgScore(double score){
        this.avgScore = score;
    }

    public int getCost(){
        return this.cost;
    }

    //private Card c;
    //private Node parent;
    private String cardName;
    //private int parentVisits;
    //private ArrayList<Node> childArray;
    private double visited;
    private double wins;
    private double avgScore;
    private double uctVal;
    private int cost;
}
