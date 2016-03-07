# amdpCommands #
"Grounding Language to Reward Functions Within Abstract Markov Decision Processes"

## Objective ##
The objective of our project is to explore how Abstract Markov Decision Processes (AMDPs) can be
used for efficiently solving sequential decision making problems. We will demonstrate a system
for natural language understanding by mapping commands to different levels of abstraction in an
example AMDP for Sokoban's Cleanup World domain. In doing so, we hope to show that AMDPs can be
used effectively to create layers of abstractions and inherent sub-goals towards the problem being
solved, in turn reducing the amount of time needed to arrive at a solution. The empirical results
of the project will test our hypotheses regarding the quality of the layers of abstraction used in
the AMDP.


## Timeline ##

[ ] - The first phase involves replicating results of the original paper by [Macglashan, et. al]
      (https://github.com/jmacglashan/commandsToTasks) on grounding English
      commands to reward functions for standard MDPs. The process of mapping a single natural language
      command to the reward function of a MDP is an atomic unit of our project that we must be
      able to replicate ourselves. We will be using the original AMT dataset (found in the data/
      directory), with our own implementation of the IBM Model 2 Translation System.

[ ] - The next step involves compiling a new dataset from the original AMT dataset used by
      MacGlashan et.al. which will contain mappings of high, mid, and low level natural language
      commands to the corresponding machine language output. We will create this dataset ourselves
      rather than use Mechanical Turk.

[ ] - The final phase of the project involves combining the first two parts on the existing AMDP for
      the cleanup world domain (in BURLAP) and compile a table of results similar to those of the original
      paper. We hope to show that we get the best results when the level of the natural language command
      matches the level of the machine language output (i.e. that a high level command corresponds
      the best with the respective high level of the AMDP, rather than the low or mid levels).