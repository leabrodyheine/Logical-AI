# Logical-AI

## Overview
This project aims to implement an intelligent agent capable of playing and solving the Mosaic logic puzzle, a variation inspired by the classic Minesweeper game. This game is played on an N x N grid where the objective is to paint or clear cells to reveal a hidden picture using clues.

## Objective
The goal is to explore various reasoning strategies to enable the agent to effectively solve the Mosaic game. This involves perceiving facts about the game state and acting accordingly to either paint or clear cells based on logical and probabilistic reasoning.

## Components
- **Logical Agent**: Implements logic to interpret clues and decide on actions.
- **Probabilistic Reasoning**: Enhances decision-making by incorporating probabilistic analysis.
- **Solver Strategies**: Various strategies are explored, from simple logic to complex probabilistic reasoning, for solving the game.

## Implementation Parts
- **Agent A**: Basic agent functionality.
- **Agent B**: Implementation using a Single Point Strategy.
- **Agent C1**: Utilizes the satisfiability test reasoning strategy (SATS), leveraging DNF encoding to transform the current partial view of the world into a logic sentence. This approach, in addition to the Single Point Strategy (SPS), uses the LogicNG library to determine whether a cell should be painted or cleared. The agent ceases to make deductions when no further logical inferences about cell states can be made, at which point it returns a final status and prints its view of the game world.
- **Agent C2**: Employs the satisfiability test strategy with a focus on direct CNF encoding, alongside the SPS. Logic sentences are written directly in conjunctive normal form and transformed into DIMACS format before being fed into a SAT solver, specifically SAT4J Core. Unlike C1, LogicNG is not utilized for this part, and no other libraries or custom code for transforming logical sentences into CNF are allowed. The process halts when the strategy no longer provides logical deductions about the cell states, leading to a final status report and an agent's game world view.
- **Agent C3**: Applies probabilistic reasoning strategy (PROBS) in addition to SPS to navigate the game. This agent assesses the current game view to decide which cells are more likely to be painted or cleared based on probability. It continues to make moves until no further logical or probabilistic inferences can be made, at which point it concludes the game by returning a final status and displaying its view of the game world.
  
### Requirements
- Java (Developed and tested on Amazon Corretto 17)
- LogicNG library (for logic operations)
- SAT4J (for satisfiability problems)
- JUnit (for testing purposes)

### Running the Project
- ./playMosaic.sh <AgentType> [<game_string>]
- AgentType: A, B, C1, C2, C3, D indicating the type of agent.
- game strings can be found in the input folder
