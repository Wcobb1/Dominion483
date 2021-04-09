package dominionAgents;

public class DecisionTreePlayerTrainer{

	private SortableGiniCardList earlyPrioList;
	
	public DecisionTreePlayerTrainer(int iterations) {
		train(iterations);
	}
	
	public void train(int iterations) {
		int games = iterations;
		int[] winner = {0,0,0};
		int turns = 0;
		int cardsPlayed = 0;
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
			winner[result] ++;
			int[] scores = gs.getScores();
			cumulativeScores[0] += scores[0];
			cumulativeScores[1] += scores[1];
			turns += gs.getTurns();
			cardsPlayed += gs.getCardsPlayed();
			sa.addWinnerCards(gs.getWinnerCardsOwned());
			sa.addCardsFirstTwoTurns(gs.getPlayerCardsGainedFirstTwoTurns(true), gs.getPlayerCardsGainedFirstTwoTurns(false));
		}
		
		//create early prio list
		earlyPrioList = sa.getFirstTwoTurnsGiniData();
		
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
		
		System.out.println("DecisionTreePlayerTrainer took " + elapsedTimeInSeconds + " seconds to train on " + iterations + " iterations.");
//		System.out.println("Early Prio List:");
//		System.out.println(earlyPrioList.toString());
		
	}
	
	public SortableGiniCardList getEarlyPrioList() {
		return earlyPrioList;
	}
	
	
	
}
