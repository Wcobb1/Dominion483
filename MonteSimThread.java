package dominionAgents;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import dominionAgents.Node;

public class MonteSimThread implements Runnable {
    private Thread t;
    private String threadName;
    public Kingdom kingdom;
    public Node cNode;
    public AtomicReference<Integer> pVisits;

    MonteSimThread(String name, Node cardNode, AtomicReference<Integer> parentCount, Kingdom kingdom){
        this.cNode = cardNode;
        this.pVisits = parentCount;
        this.threadName = name;
        this.kingdom = kingdom;
        //System.out.println("Created Thread : " + threadName);
    }

    public void run(){
        //System.out.println("Running " + threadName);
        for (int i = 0; i < 150; i++){
            try {
                Kingdom k = new Kingdom(kingdom);
                PlayerCommunication playerC = new PlayerCommunication();
                Player us = new MoneyMakingBotV1_0_2(k, playerC, this.cNode.getCardName());
                Player opp = new MoneyMakingBotV1_0_2(k, playerC);
                GameSimulator rs = new GameSimulator(us, opp);
                int result = rs.runGame();
                int[] scores = rs.getScores();
                //System.out.print(scores[1] + " ");
                //System.out.println(scores[0]);
                this.cNode.setAvgScore(this.cNode.getAvgScore() + scores[0]);
                this.cNode.setNodeVisits(this.cNode.getNodeVisits() + 1.0);
                if (result == 0){
                    this.cNode.setNodewins(this.cNode.getNodewins() + 1.0);
                }
                else if (result == 1){
                    this.cNode.setNodewins(this.cNode.getNodewins() - 1.0);
                }
                else if (result == 2){
                    this.cNode.setNodewins(this.cNode.getNodewins() + 0.5);
                }
                pVisits.set(pVisits.get() + 1);

                if (Thread.interrupted())  // Clears interrupted status!
                    throw new InterruptedException();

            } catch (InterruptedException e) {
                System.out.println("Thread " +  threadName + " interrupted.");
            }
        }
    }

    public void start() {
        //System.out.println("Starting " +  threadName );
        if (t == null){
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public Thread getThread(){
        return this.t;
    }
}
