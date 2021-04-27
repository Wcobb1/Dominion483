package dominionAgents;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Driver2 implements Runnable, Callable {
	private Thread t;
	private String threadName;
	private int games;
	// 0 - winner[0], 1 - winner[1], 2 - winner[2], 3 - cumulativeScores[0], 4 - cumulativeScores[1], 5 - turns, 6 - cardsPlayed, 7 - elapsedTime
	private ArrayList<Double> info = new ArrayList<>();

	Driver2(String name){
		this.threadName = name;
		this.games = 250;
		//System.out.println("Creating " +  threadName );
	}

	public void run(){
		//System.out.println("Running " +  threadName );
		try{
			int[] winner = {0,0,0};
			int turns = 0;
			int cardsPlayed = 0;
			int[] cumulativeScores = {0,0};
			StatAnalysis sa = new StatAnalysis();
			
			long startTime = System.nanoTime();

			//DecisionTreePlayerTrainer trainer = new DecisionTreePlayerTrainer(1000);
			//DecisionTreePlayerTrainer trainer2 = new DecisionTreePlayerTrainer(1000);

			for(int i = 0;i < games;i ++) {
				//System.out.println("Game #" + (i+1));
				Kingdom kingdom = new Kingdom();
				PlayerCommunication pc = new PlayerCommunication();
				//Set player types
				//Player p2 = new BasicBotV1_0(kingdom, pc);
				//Player p1 = new BasicBotV1_0(kingdom, pc);
				//Player p1 = new DecisionTreePlayerV1_0(kingdom, pc, trainer.getEarlyPrioList(), trainer.getMidPrioList(), trainer.getLatePrioList());
				//Player p1 = new AttackBotV1_0(kingdom, pc);
				Player p1 = new MoneyMakingBotV1_0(kingdom, pc);
				//Player p2 = new MontePlayer(kingdom, pc);
				Player p2 = new RushBotV1_0(kingdom, pc);
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
			/*double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
			System.out.println("On Thread-" + this.threadName);
			System.out.println("Time elapsed: " + elapsedTime + " ns == " + elapsedTimeInSeconds + " seconds");
			System.out.println(cardsPlayed + " cards played in " + turns + " turns in " + games + " games:");
			System.out.println("Player 1: Win %: " + 100 *(double)winner[0]/games + " - Avg. Score: " + (double)cumulativeScores[0]/games);
			System.out.println("Player 2: Win %: " + 100 *(double)winner[1]/games + " - Avg. Score: " + (double)cumulativeScores[1]/games);
			System.out.println("Draw %: " + 100 *(double)winner[2]/games);
			System.out.println();
				*/
			this.info.add(Double.valueOf(winner[0]));
			this.info.add(Double.valueOf(winner[1]));
			this.info.add(Double.valueOf(winner[2]));
			this.info.add(Double.valueOf(cumulativeScores[0]));
			this.info.add(Double.valueOf(cumulativeScores[1]));
			this.info.add(Double.valueOf(turns));
			this.info.add(Double.valueOf(cardsPlayed));
			this.info.add(Double.valueOf(elapsedTime));
			this.info.add(Double.valueOf(games));

			if (Thread.interrupted())  // Clears interrupted status!
      			throw new InterruptedException();
		
			} catch (InterruptedException e){
			System.out.println("Thread " +  threadName + " interrupted.");
		}
		System.out.println("Thread " +  threadName + " exiting.");
	}

	public void start(){
		//System.out.println("Starting " +  threadName );
		if (t == null) {
			t = new Thread (this, threadName);
			t.start ();
		}
	}

	public Thread getThread(){
		return this.t;
	}

	public ArrayList<Double> call()  {
		return this.info;
	}
}
