# Dominion483
CMSC 483 Final Project

Main driver to run: WinrateMatrixDriver.java

Notes: 
- GiniBot is run with DecisionTreePlayer1_0 and DecisionTreePlayerTrainer objects

- MontePlayer is a parallelized version of Monte Carlo AI Bot. MonteSerial should be used to perform sequential Monte Carlo.

- AIs that can be run : AttackingBotV1_0, BasicBotV1_0, DecisionTreePlayerV1_0, MoneyMakingBotV1_0, MontePlayer (relatively slow), MonteSerial (slower than MontePlayer), RushBotV1_0 

Drivers:

- WinrateMatrixDriver : Performs matchups of different AIs serially/sequentially or parallel, depending on what you select. To change to  sequential execution, change serial to true on line 29. WinrateMatrixDriver calculates the time it takes to run each matchup and all matchups combined, along with providing a matrix for the win rate of each bot against other bots.

- VarianceAnalysisDriver : This driver can be used to analyze how long it takes for two bots to even out their win rates, meaning identifying the variance in the game setups (Someone please correct this). 

- StatsDriver.java : Provides an average analysis of each player's deck at the end of a game. This can be used to identify which cards does the AI favor.

- Driver.java : In need of some renovations due to it mainly being a MontePlayer driver, but as of now, mainly useful to run either parallel or serial matchup of MontePlayer. Parallel matchup execution occurs with Driver2.java, which can be changed to change MontePlayer to other bots. Similarly, serial execution can be changed from MontePlayer to other AI bots.

- Driver2.java : Runnable class for parallel execution of games for Monte (as of now). Can't run as not really a driver, but due to accidentally unfortunate naming, have to mention here. DO NOT RUN!


