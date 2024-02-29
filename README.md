# Logical-AI

## Overview

This project aims to implement an intelligent agent capable of playing and solving the Mosaic logic puzzle, a variation inspired by the classic Minesweeper game. This game is played on an N x N grid where the objective is to paint or clear cells to reveal a hidden picture using clues.

## Objective

The goal is to explore various reasoning strategies to enable the agent to effectively solve the Mosaic game. This involves perceiving facts about the game state and acting accordingly to either paint or clear cells based on logical and probabilistic reasoning.

## Components

- **Logical Agent**: Implements logic to interpret clues and decide on actions.
- **Probabilistic Reasoning**: Enhances decision-making by incorporating probabilistic analysis.
- **Solver Strategies**: Various strategies are explored, from simple logic to complex probabilistic reasoning, for solving the game.

## Setup
- **Agent A**: Implements logic to interpret clues and decide on actions.
- **Agent B**: Enhances decision-making by incorporating probabilistic analysis.
- **Solver Strategies**: Various strat

  
### Requirements

- Java (Developed and tested on Amazon Corretto 17)
- LogicNG library (for logic operations)
- SAT4J (for satisfiability problems)
- JUnit (for testing purposes)

### Running the Project
./playMosaic.sh <AgentType> [<game string>]
AgentType: A, B, C1, C2, C3, D indicating the type of agent.
the game string can be found in the input files
