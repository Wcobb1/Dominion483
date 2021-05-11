package dominionAgents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class WinrateMatrixDriver {

	private static int cardsPlayed = 0;
	private static int turns = 0;
	private static Object lock = new Object();
	
	private static String[] playerNames = {
			"AttackBot",
			"BasicBot",
			"GiniPlayer",
			"MoneyBot",
			"RushBot",
			"MontePlayer"
	};
	
	private static Vector<Double> playerRunTimes;
	private static ArrayList<ArrayList<String>> csvList = new ArrayList<>();
	private static boolean serial = false;
	private static boolean noMonte = false;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<String> mainCSV = new ArrayList<>();
		//initialize runTime vector
		playerRunTimes = new Vector<Double>();
		if (noMonte){
			for(int i = 0;i < playerNames.length-1;i ++) {
				playerRunTimes.add(0.0);
			}	
		}
		else {
			for(int i = 0;i < playerNames.length;i ++) {
				playerRunTimes.add(0.0);
			}				
		}

		int gamesToPlayInEachMatchup = 1000;
		int playerNameLen = (noMonte) ? playerNames.length - 1 : playerNames.length;
		// Run in Serial
		if (serial){
			printWinrates(gamesToPlayInEachMatchup, false);
				
			for(int i = 0;i < playerRunTimes.size();i ++) {
				playerRunTimes.set(i, (double)playerRunTimes.get(i)/playerRunTimes.size());
			}
					
			System.out.println("Avg. player run times: ");
			for(int i = 0;i < playerNameLen;i ++) {
				System.out.println(playerNames[i] + ": " + playerRunTimes.get(i));
				mainCSV.add(playerNames[i]);
				mainCSV.add(",");
				mainCSV.add(playerRunTimes.get(i).toString());
				mainCSV.add("\n");
				playerRunTimes.set(i, 0.0);
			}	
						
		}
		else {
			//run in parallel
			printWinrates(gamesToPlayInEachMatchup, true);
			for(int i = 0;i < playerRunTimes.size();i ++) {
				playerRunTimes.set(i, (double)playerRunTimes.get(i)/playerRunTimes.size());
			}
			
			System.out.println("Avg. player run times: ");
			for(int j = 0;j < playerNameLen;j ++) {
				System.out.println(playerNames[j] + ": " + playerRunTimes.get(j));
				mainCSV.add(playerNames[j]);
				mainCSV.add(",");
				mainCSV.add(playerRunTimes.get(j).toString());
				mainCSV.add("\n");
			}
		}		
		exportToCSV(csvList, serial);
	}
	
	public static void printWinrates(int games, boolean parallel) {

		double[][] winrates;
		if(parallel) {
			winrates = getWinratesParallel(games);
		}else {
			winrates = getWinratesSerial(games);
		}
		
		ArrayList<String> getWinData = new ArrayList<>();
		
		System.out.println("Winrates:");
		getWinData.add("Winrates:");
		getWinData.add("\n");
		System.out.print("Player\t\t");
		getWinData.add("Player");
		getWinData.add(",");
		for(String s:playerNames) {
			if (noMonte && !s.equalsIgnoreCase("MontePlayer")) {
				System.out.print(s+"\t");
				getWinData.add(s);
				getWinData.add(",");				
			}
			else {
				System.out.print(s+"\t");
				getWinData.add(s);
				getWinData.add(",");				
			}
		}
		getWinData.add("\n");
		System.out.print("\n");
		for(int i = 0;i < winrates.length;i ++) {
			System.out.print(playerNames[i] + "\t");
			getWinData.add(playerNames[i]);
			getWinData.add(",");
			for(int j = 0;j < winrates.length;j ++) {
				System.out.print(winrates[i][j] + "\t\t");
				getWinData.add(Double.valueOf(winrates[i][j]).toString());
				getWinData.add(",");
			}
			getWinData.add("\n");
			System.out.print("\n");
		}
		csvList.add(getWinData);
	}
	
	public static double[][] getWinratesSerial(int games){
		int numPlayers = 6;
		if (noMonte){
			numPlayers = 5;
		}
		double matchupWins[][] = new double[numPlayers][numPlayers];
		int cardsPlayed = 0;
		int turns = 0;
		
		long startTime = System.nanoTime();
		
		DecisionTreePlayerTrainer dtpTrainer = new DecisionTreePlayerTrainer(games, false);
		
		long midTime = System.nanoTime();
		long midTotalTime = midTime - startTime;
		double midTimeInSeconds = (double)midTotalTime/1_000_000_000;
		System.out.println("Gini player took " + midTimeInSeconds + " seconds to train in serial.");
		ArrayList<String> strSerialWinRate = new ArrayList<>();
		strSerialWinRate.add("Gini player took " + Double.valueOf(midTimeInSeconds).toString() + " seconds to train in serial.");
		strSerialWinRate.add("\n");
		//run matchups
		for(int i = 0;i < numPlayers;i ++) {
			for(int j = i;j < numPlayers;j ++) {
				if(i == j) {
					//set wins to 0(don't care about player playing themself)
					matchupWins[i][j] = 0;
				}else {
					//run matchup
					//System.out.println(i + ", " + j);
					long serialT = System.nanoTime();
					
					int[] results = runMatchup(i,j,games,dtpTrainer);
					turns += results[3];
					cardsPlayed += results[4];
					matchupWins[i][j] = (double)results[0]/(double)games;
					matchupWins[j][i] = (double)results[1]/(double)games;
					
					long endMatchUp = System.nanoTime();
					double elapsed = Double.valueOf(endMatchUp - serialT)/1_000_000_000;
					playerRunTimes.set(i, playerRunTimes.get(i) + elapsed);
					playerRunTimes.set(j, playerRunTimes.get(j) + elapsed);
					
				}
			}
		}
		
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
		
		
		System.out.println("Time elapsed: " + elapsedTime + " ns == " + elapsedTimeInSeconds + " seconds");
		System.out.println(cardsPlayed + " cards played in " + turns + " turns.");
		System.out.println("Cards/second: " + (double)cardsPlayed/elapsedTimeInSeconds);
		
		strSerialWinRate.add("Time elapsed: " + Long.valueOf(elapsedTime).toString() + " ns == " + Double.valueOf(elapsedTimeInSeconds).toString() + " seconds");
		strSerialWinRate.add("\n");
		strSerialWinRate.add(Integer.valueOf(cardsPlayed).toString() + " cards played in " + Integer.valueOf(turns).toString() + " turns.");
		strSerialWinRate.add("\n");
		strSerialWinRate.add("Cards/second: " + Double.valueOf(elapsedTimeInSeconds/elapsedTimeInSeconds).toString());
		strSerialWinRate.add("\n");

		csvList.add(strSerialWinRate);
		
		return matchupWins;
		
	}
	
	public static double[][] getWinratesParallel(int games){
		int numPlayers = 6;
		if (noMonte){
			numPlayers = 5;
		}
		double matchupWins[][] = new double[numPlayers][numPlayers];
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		ArrayList<String> strParallelWinRate = new ArrayList<>();

		long startTime = System.nanoTime();
		
		DecisionTreePlayerTrainer dtpTrainer = new DecisionTreePlayerTrainer(games, true);
		
		long midTime = System.nanoTime();
		long midTotalTime = midTime - startTime;
		double midTimeInSeconds = (double)midTotalTime/1_000_000_000;
		System.out.println("Gini player took " + midTimeInSeconds + " seconds to train in parallel.");
		strParallelWinRate.add("Gini player took " + Double.valueOf(midTimeInSeconds).toString() + " seconds to train in parallel.");
		strParallelWinRate.add("\n");
		//run matchups
		for(int i = 0;i < numPlayers;i ++) {
			for(int j = i;j < numPlayers;j ++) {
				if(i == j) {
					//set wins to 0(don't care about player playing themself)
					matchupWins[i][j] = 0;
				}else {
					
					int index1 = i;
					int index2 = j;
					
					//run matchup
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							long startTime = System.nanoTime();
							int[] results = runMatchup(index1,index2,games,dtpTrainer);
							
							//ensure multiple threads don't set data at same time
							synchronized(lock) {
								turns += results[3];
								cardsPlayed += results[4];
								matchupWins[index1][index2] = (double)results[0]/(double)games;
								matchupWins[index2][index1] = (double)results[1]/(double)games;
							}
							long endTime = System.nanoTime();
							long elapsedTime = endTime - startTime;
							double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
							playerRunTimes.set(index1, playerRunTimes.get(index1) + elapsedTimeInSeconds);
							playerRunTimes.set(index2, playerRunTimes.get(index2) + elapsedTimeInSeconds);
							System.out.println("Thread: " + playerNames[index1] + ", " + playerNames[index2]
									+ ": " + elapsedTimeInSeconds + " seconds to run.");
						}
						
					});
					
					threads.add(t);
					t.start();
				}
			}
		}
		
		//wait for last thread to finish before returning
		try {
			for(Thread t:threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		double elapsedTimeInSeconds = (double)elapsedTime/1_000_000_000;
		
		System.out.println("--------------------");
		strParallelWinRate.add("--------------------");
		strParallelWinRate.add("\n");
		System.out.println("Total time elapsed: " + elapsedTime + " ns == " + elapsedTimeInSeconds + " seconds");
		strParallelWinRate.add("Total time elapsed: " + elapsedTime + " ns == " + elapsedTimeInSeconds + " seconds");
		strParallelWinRate.add("\n");
		System.out.println(cardsPlayed + " cards played in " + turns + " turns.");
		strParallelWinRate.add(cardsPlayed + " cards played in " + turns + " turns.");
		strParallelWinRate.add("\n");
		System.out.println("Cards/second: " + (double)cardsPlayed/elapsedTimeInSeconds);
		strParallelWinRate.add("Cards/second: " + Double.valueOf(cardsPlayed/elapsedTimeInSeconds).toString());
		strParallelWinRate.add("\n");
		
		csvList.add(strParallelWinRate);
		
		return matchupWins;
		
	}
	
	private static int[] runMatchup(int p1, int p2, int games, DecisionTreePlayerTrainer dtpt) {
		int[] results = new int[5];
		int turns = 0;
		int cardsPlayed = 0;
		
		for(int i = 0;i < games;i ++) {
			Kingdom kingdom = new Kingdom();
			PlayerCommunication pc = new PlayerCommunication();
			//Set player types
			Player[] players = setupPlayers(p1, p2, kingdom, pc, dtpt);
			
			GameSimulator gs = new GameSimulator(players[0], players[1]);
			int result = gs.runGame();
			
			results[result] ++;
			
			results[3] += gs.getTurns();
			results[4] += gs.getCardsPlayed();
		}
		
		return results;
		
	}
	
	private static Player[] setupPlayers(int p1, int p2, Kingdom k, PlayerCommunication pc, DecisionTreePlayerTrainer dtpt) {
		Player[] players = new Player[2];
		players[0] = setupPlayer(p1, k, pc, dtpt);
		players[1] = setupPlayer(p2, k, pc, dtpt);
		return players;
	}
	
	private static Player setupPlayer(int p, Kingdom k, PlayerCommunication pc, DecisionTreePlayerTrainer dtpt) {
		if(p == 0) {
			return new AttackBotV1_0(k, pc);
		}else if(p == 1) {
			return new BasicBotV1_0(k, pc);
		}else if(p == 2) {
			return new DecisionTreePlayerV1_0(k, pc, dtpt.getEarlyPrioList(), dtpt.getMidPrioList(), dtpt.getLatePrioList());
		}else if(p == 3) {
			return new MoneyMakingBotV1_0(k, pc);
		}else if (p == 4) {
			return new RushBotV1_0(k, pc);
		} else {
			return new MontePlayer(k, pc);
		}
	}

	private static void exportToCSV(ArrayList<ArrayList<String>> arrList, boolean serial) {
		File file;
		if (!serial) {
			file = new File("Output_parallel.csv");
			for(int i = 0; file.exists() == true; i++) {
				file = new File("Output_parallel_"+i+".csv");
			}			
		}
		else {
			file = new File("Output_serial.csv");
			for(int i = 1; file.exists() == true; i++) {
				file = new File("Output_serial_"+i+".csv");
			}	
		}
		try (PrintWriter write = new PrintWriter(file)) {
			StringBuilder sb = new StringBuilder();
			for (ArrayList<String> arr : arrList) {
				for(String s : arr) {
					sb.append(s);
				}
			}
			
			write.write(sb.toString());
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Exported WinRate Data to CSV file, " + file.getName());
	}
}
