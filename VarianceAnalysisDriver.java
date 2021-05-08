package dominionAgents;

public class VarianceAnalysisDriver {

	public static void main(String[] args) {
		
		double avg = 0.0;
		
		for(int i = 0;i < 50;i ++) {
			avg += gamesRunToEven();
		}
		
		avg = avg/(double)50.0;
		
		System.out.println("Avg: " + avg);
		
	}
	
	public static int gamesRunToEven() {
		
		int games = 100;
		int maxGames = 8000;
		int[] winner = {0,0,0};
		int num = games;
		
		for(int i = 0;i < games;i ++) {
			runSimulation(winner);
		}
		
		for(int i = games;i < maxGames && winner[0] != winner[1];i ++) {
			runSimulation(winner);
			num ++;
		}
		
		//System.out.println("NumGames: " + num);
		
		return num;
		
	}
	
	public static void runSimulation(int[] winner){
		Kingdom kingdom = new Kingdom();
		PlayerCommunication pc = new PlayerCommunication();
		//Set player types
		Player p1 = new BasicBotV1_0(kingdom, pc);
		Player p2 = new BasicBotV1_0(kingdom, pc);
		GameSimulator gs = new GameSimulator(p1, p2);
		int result = gs.runGame();
		winner[result] ++;
	}

}
