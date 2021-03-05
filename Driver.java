package dominionAgents;

public class Driver {

	public static void main(String[] args) {
		
		int games = 1000;
		int[] winner = {0,0,0};
		int turns = 0;
		int cardsPlayed = 0;
		int[] cumulativeScores = {0,0};
		
		long startTime = System.nanoTime();
		
		for(int i = 0;i < games;i ++) {
			GameSimulator gs = new GameSimulator();
			int result = gs.runGame();
			winner[result] ++;
			int[] scores = gs.getScores();
			cumulativeScores[0] += scores[0];
			cumulativeScores[1] += scores[1];
			turns += gs.getTurns();
			cardsPlayed += gs.getCardsPlayed();
		}
		
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
		
		System.out.println("Time elapsed: " + elapsedTime + " ns == " + elapsedTimeInSeconds + " seconds");
		System.out.println(cardsPlayed + " cards played in " + turns + " turns in " + games + " games:");
		System.out.println("Player 1: Win %: " + 100 *(double)winner[0]/games + " - Avg. Score: " + (double)cumulativeScores[0]/games);
		System.out.println("Player 2: Win %: " + 100 *(double)winner[1]/games + " - Avg. Score: " + (double)cumulativeScores[1]/games);
		System.out.println("Draw %: " + 100 *(double)winner[2]/games);
		
	}

}
