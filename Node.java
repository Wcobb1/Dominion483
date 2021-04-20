package dominionAgents;

import java.util.ArrayList;

public class Node {
    
    public Node(){
        this.c = new Card("NA");
        this.visited = 0;
        this.wins = (double)0;
        this.uctVal = Double.NEGATIVE_INFINITY;
        this.childArray = null;
    }

    public Node(Card c){
        this.c = c;
        this.parent = null;
        this.visited = 0;
        this.wins = 0;
        this.uctVal = Double.NEGATIVE_INFINITY;
        this.childArray = null;
    }

    public Node getNodeParent(){
        return this.parent;
    }

    public void setNodeParent(Node parent){
        this.parent = parent;
    }

    public int getNodeVisits(){
        return this.visited;
    }

    public void setNodeVisits(int visits){
        this.visited = visits;
    }

    public double getNodewins(){
        return this.wins;
    }

    public void setNodewins(double wins){
        this.wins = wins;
    }

    public Card getCard(){
        return this.c;
    }

    public void setCard(Card c){
        this.c = c;
    }
    
    public double getUCTVal(){
        return this.uctVal;
    }

    public void setUCTVal(double uctVal){
        this.uctVal = uctVal;
    }

    public ArrayList<Node> getChildrenNodes(){
        return this.childArray;
    }

    public void setChildrenNodes(ArrayList<Node> children){
        this.childArray = children;
    }

    private Card c;
    private Node parent;
    //private int parentVisits;
    private ArrayList<Node> childArray;
    private int visited;
    private double wins;
    private double uctVal;
}
