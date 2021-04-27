package dominionAgents;

import java.lang.Thread.State;
import java.util.ArrayList;

import org.graalvm.compiler.nodes.java.ArrayLengthNode;

public class Driver {
    public static void main(String[] args){
        // If threads, 0. If serial run, 1;
        boolean thread_run = true;
        if (thread_run){
            ArrayList<Thread> threadList = new ArrayList<>();
            ArrayList<Driver2> driver2List = new ArrayList<>();

            int games = 1000;
            String[] threadN = {"Thread-1", "Thread-2", "Thread-3", "Thread-4"};
            for (int i = 0; i < 4; i++){
                Driver2 d1 = new Driver2(threadN[i]);
                driver2List.add(d1);
                d1.start();
                threadList.add(d1.getThread());    
            }

            ArrayList<ArrayList<Double>> threadStats = new ArrayList<>();
            try {
                System.out.println("Waiting for threads to finish.");
                for (int i = 0; i < 4; i++){
                    threadList.get(i).join();
                }
            } catch (InterruptedException e) {
                System.out.println("Main thread Interrupted");
            }

            for (Driver2 d : driver2List){
                threadStats.add(d.call());
            }

        	// 0 - winner[0], 1 - winner[1], 2 - winner[2], 3 - cumulativeScores[0], 4 - cumulativeScores[1], 5 - turns, 6 - cardsPlayed, 7 - elapsedTime
            double player1Wins = 0, player2Wins = 0, draws = 0, player1Score = 0, player2Score = 0, turns = 0, cardsPlayed = 0;
            double time = 0;
            for (ArrayList<Double> tI : threadStats){
                player1Wins += tI.get(0);
                player2Wins += tI.get(1);
                draws += tI.get(2);
                player1Score += tI.get(3);
                player2Score += tI.get(4);
                turns += tI.get(5);
                cardsPlayed += tI.get(6);
                time += tI.get(7);
            }

            System.out.println("Parallelized : ");
            System.out.println("(Average) Time elapsed: " + time/4 + " ns == " + (time/1_000_000_000)/4 + " seconds");
            System.out.println(cardsPlayed + " cards played in " + turns + " turns in " + games + " games:");
            System.out.println("Player 1: Win %: " + 100 *player1Wins/games + " - Avg. Score: " + player1Score/games);
            System.out.println("Player 2: Win %: " + 100 *player2Wins/games + " - Avg. Score: " + player2Score/games);
            System.out.println("Draw %: " + 100 *draws/games);

            thread_run = false;
        }
        
        if (!thread_run) {
            int games = 1000;
            int[] winner = {0,0,0};
            int turns = 0;
            int cardsPlayed = 0;
            int[] cumulativeScores = {0,0};
            StatAnalysis sa = new StatAnalysis();
            
            long startTime = System.nanoTime();
            
            //DecisionTreePlayerTrainer trainer = new DecisionTreePlayerTrainer(1000);
            //DecisionTreePlayerTrainer trainer2 = new DecisionTreePlayerTrainer(1000);
    
            for(int i = 0;i < games;i ++) {
                System.out.println("Game #" + (i+1));
                Kingdom kingdom = new Kingdom();
                PlayerCommunication pc = new PlayerCommunication();
                //Set player types
                //Player p2 = new BasicBotV1_0(kingdom, pc);
                //Player p1 = new BasicBotV1_0(kingdom, pc);
                //Player p1 = new DecisionTreePlayerV1_0(kingdom, pc, trainer.getEarlyPrioList(), trainer.getMidPrioList(), trainer.getLatePrioList());
                Player p1 = new AttackBotV1_0(kingdom, pc);
                //Player p1 = new MoneyMakingBotV1_0(kingdom, pc);
                Player p2 = new MontePlayer(kingdom, pc);
                //Player p2 = new RushBotV1_0(kingdom, pc);
                //Player p2 = new DecisionTreePlayerV1_0(kingdom, pc, trainer2.getEarlyPrioList(), trainer2.getMidPrioList(), trainer2.getLatePrioList());			
    
                GameSimulator gs = new GameSimulator(p1, p2);
                int result = gs.runGame();
                winner[result] ++;
                int[] scores = gs.getScores();
                cumulativeScores[0] += scores[0];
                cumulativeScores[1] += scores[1];
                turns += gs.getTurns();
                cardsPlayed += gs.getCardsPlayed();
                sa.addWinnerCards(gs.getWinnerCardsOwned());
            }
            
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
            
            System.out.println("\n Serial Run : ");
            System.out.println("Time elapsed: " + elapsedTime + " ns == " + elapsedTimeInSeconds + " seconds");
            System.out.println(cardsPlayed + " cards played in " + turns + " turns in " + games + " games:");
            System.out.println("Player 1: Win %: " + 100 *(double)winner[0]/games + " - Avg. Score: " + (double)cumulativeScores[0]/games);
            System.out.println("Player 2: Win %: " + 100 *(double)winner[1]/games + " - Avg. Score: " + (double)cumulativeScores[1]/games);
            System.out.println("Draw %: " + 100 *(double)winner[2]/games);
            
             
    
            //sa.printFirstTwoRounds();
            
            //sa.calculateWinnerOwnPercentage();
            //sa.printWinnerOwnPercentage();
                
        }
    }
}

