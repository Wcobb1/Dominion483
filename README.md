# Dominion483
CMSC 483 Final Project

Main driver to run: WinrateMatrixDriver.java

Notes: 
- GiniBot is run with DecisionTreePlayer1_0 and DecisionTreePlayerTrainer objects

- MontePlayer is a parallelized version of Monte Carlo AI Bot. MonteSerial should be used to perform sequential Monte Carlo.

- AIs that can be run : AttackBotV1_0, BasicBotV1_0, DecisionTreePlayerV1_0, MoneyMakingBotV1_0, MontePlayer (relatively slow), MonteSerial (slower than MontePlayer), RushBotV1_0 

Drivers:

- WinrateMatrixDriver : Performs matchups of different AIs serially/sequentially or parallel, depending on what you select. To change to  sequential execution, change serial to true on line 29. WinrateMatrixDriver calculates the time it takes to run each matchup and all matchups combined, along with providing a matrix for the win rate of each bot against other bots.

- StatsDriver.java : Provides an average analysis of each player's deck at the end of a game. This can be used to identify which cards the AI favors.

