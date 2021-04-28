package dominionAgents;

public class DecisionTreePlayerTrainer{

	private SortableGiniCardList earlyPrioList;
	private SortableGiniCardList midPrioList;
	private SortableGiniCardList latePrioList;
	private int turns;
	private int cardsPlayed;
	private Object lock = new Object();
	private int maxThreads = 4;
	private double trainTime = 0.0;
	
	public DecisionTreePlayerTrainer(int iterations, boolean parallelTraining) {
		if(parallelTraining) {
			trainParallel(iterations);
		}else {
			train(iterations);
		}
	}
	
	public DecisionTreePlayerTrainer(int iterations, int maxThreads) {
		this.maxThreads = maxThreads;
		trainParallel(iterations);
	}
	
	public void trainParallel(int iterations){
		int games = iterations;
		int[] winner = {0,0,0};
		turns = 0;
		cardsPlayed = 0;
		int[] cumulativeScores = {0,0};
		StatAnalysis sa = new StatAnalysis();
		
		long startTime = System.nanoTime();
		
		Thread lastThread = null;
		
		for(int i = 0;i < maxThreads;i ++) {
		
			Thread t = new Thread(new Runnable() {
				public void run() {
					for(int i = 0;i < (games/maxThreads);i ++) {
						Kingdom kingdom = new Kingdom();
						PlayerCommunication pc = new PlayerCommunication();
						Player p1 = new BasicBotV1_0(kingdom, pc);
						Player p2 = new BasicBotV1_0(kingdom, pc);
						GameSimulator gs = new GameSimulator(p1, p2);
						int result = gs.runGame();
						int[] scores = gs.getScores();
						synchronized(lock){
							winner[result] ++;
							cumulativeScores[0] += scores[0];
							cumulativeScores[1] += scores[1];
							turns += gs.getTurns();
							cardsPlayed += gs.getCardsPlayed();
							sa.addWinnerCards(gs.getWinnerCardsOwned());
							sa.addCardsFirstTwoTurns(gs.getPlayerCardsGainedFirstTwoTurns(true), gs.getPlayerCardsGainedFirstTwoTurns(false));
							sa.addCardsMidGame(gs.getPlayerCardsGainedMidGame(true), gs.getPlayerCardsGainedMidGame(false));
							sa.addCardsLateGame(gs.getPlayerCardsGainedLateGame(true), gs.getPlayerCardsGainedLateGame(false));
						}
					}
				}
			});
			lastThread = t;
			t.start();
		}
		
		//wait for lastThread to finish
		try {
			lastThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//create early prio list
		earlyPrioList = sa.getFirstTwoTurnsGiniData();
		
		//create mid prio list
		midPrioList = sa.getMidGameGiniData();
		
		//create late prio list
		latePrioList = sa.getLateGameGiniData();
		
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
		
		trainTime = elapsedTimeInSeconds;
		
//		System.out.println("DecisionTreePlayerTrainer took " + elapsedTimeInSeconds + " seconds to train on " + iterations + " iterations.");
//		System.out.println("Early Prio List:");
//		System.out.println(earlyPrioList.toString());
//		System.out.println("Mid Prio List:");
//		System.out.println(midPrioList.toString());
//		System.out.println("Late Prio List:");
//		System.out.println(latePrioList.toString());
	}
	
	public void train(int iterations){
		int games = iterations;
		int[] winner = {0,0,0};
		turns = 0;
		cardsPlayed = 0;
		int[] cumulativeScores = {0,0};
		StatAnalysis sa = new StatAnalysis();
		
		long startTime = System.nanoTime();
		
		for(int i = 0;i < games;i ++) {
			Kingdom kingdom = new Kingdom();
			PlayerCommunication pc = new PlayerCommunication();
			Player p1 = new BasicBotV1_0(kingdom, pc);
			Player p2 = new BasicBotV1_0(kingdom, pc);
			GameSimulator gs = new GameSimulator(p1, p2);
			int result = gs.runGame();
			int[] scores = gs.getScores();
			winner[result] ++;
			cumulativeScores[0] += scores[0];
			cumulativeScores[1] += scores[1];
			turns += gs.getTurns();
			cardsPlayed += gs.getCardsPlayed();
			sa.addWinnerCards(gs.getWinnerCardsOwned());
			sa.addCardsFirstTwoTurns(gs.getPlayerCardsGainedFirstTwoTurns(true), gs.getPlayerCardsGainedFirstTwoTurns(false));
			sa.addCardsMidGame(gs.getPlayerCardsGainedMidGame(true), gs.getPlayerCardsGainedMidGame(false));
			sa.addCardsLateGame(gs.getPlayerCardsGainedLateGame(true), gs.getPlayerCardsGainedLateGame(false));
			
		}
		
		
		//create early prio list
		earlyPrioList = sa.getFirstTwoTurnsGiniData();
		
		//create mid prio list
		midPrioList = sa.getMidGameGiniData();
		
		//create late prio list
		latePrioList = sa.getLateGameGiniData();
		
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
		
		trainTime = elapsedTimeInSeconds;
		
//		System.out.println("DecisionTreePlayerTrainer took " + elapsedTimeInSeconds + " seconds to train on " + iterations + " iterations.");
//		System.out.println("Early Prio List:");
//		System.out.println(earlyPrioList.toString());
//		System.out.println("Mid Prio List:");
//		System.out.println(midPrioList.toString());
//		System.out.println("Late Prio List:");
//		System.out.println(latePrioList.toString());
	}
	
	public double getTrainTime() {
		return trainTime;
	}
	
	public SortableGiniCardList getEarlyPrioList() {
		return earlyPrioList;
	}
	
	public SortableGiniCardList getMidPrioList() {
		return midPrioList;
	}
	
	public SortableGiniCardList getLatePrioList() {
		return latePrioList;
	}
	
}
