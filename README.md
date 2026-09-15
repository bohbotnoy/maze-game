# Maze Game

A full desktop maze application built in Java over three stages: a core library of maze-generation and pathfinding algorithms, a multi-client server layer with custom compression, and a JavaFX desktop game built on an MVVM architecture.

Developed as a team project for the *Advanced Software Systems Development (ATP)* course at Ben-Gurion University, with an emphasis on clean object-oriented design (SOLID principles, design patterns, and a generic, reusable algorithm hierarchy). All parts were developed jointly by the team.

## Repository Structure

This repository contains two projects, each opened independently:

```
maze-game/
├── part-a-b/          Core library: algorithms, compression, client-server (plain Java project)
└── part-c-javafx/     Desktop game: JavaFX GUI in MVVM (Maven project)
```

> **Opening the projects:** open each part as its **own** project in IntelliJ (`File → Open` on `part-a-b` or `part-c-javafx`) rather than opening the `maze-game` root — the two parts have different build setups (plain Java vs. Maven), so opening them separately lets the IDE configure each correctly.

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 15+ | Core language |
| JavaFX | Desktop GUI (Part C) |
| Maven | Dependency management (Part C) |
| Log4j2 | Server-side logging (Part C) |
| JUnit 5 | Unit testing |
| Design patterns | Strategy, Adapter, Decorator, MVVM |

---

## Part A + B — Core Library & Server

A plain Java project containing the maze engine, algorithms, compression, and networking.

### Maze Generation

Multiple generators built on a shared abstract-class / interface hierarchy (`IMazeGenerator` → `AMazeGenerator`), so a new algorithm only implements what differs:

- **EmptyMazeGenerator** — an open maze with no walls.
- **SimpleMazeGenerator** — random walls.
- **MyMazeGenerator** — a "real" maze with branching paths and dead ends, based on a maze-generation algorithm, optimized to build a 1000×1000 maze in under a minute.

### Maze Solving

Three **generic** search algorithms that operate on any problem implementing the `ISearchable` interface — the same solvers work unchanged on a 2D maze (or any other search problem):

- **BreadthFirstSearch (BFS)** — ranks states by number of steps.
- **DepthFirstSearch (DFS)**.
- **BestFirstSearch** — extends BFS with a priority queue (heap), assigning different costs to straight vs. diagonal moves to find the cheapest path.

An **Adapter** (`SearchableMaze`) adapts a `Maze` into a searchable problem, supporting diagonal movement.

### Compression

A custom maze-compression scheme built into Java's stream **Decorator** pattern (`MyCompressorOutputStream` / `MyDecompressorInputStream` extend `OutputStream` / `InputStream`), so the same compression works transparently over both files and network channels. Uses run-length encoding to store the maze in far fewer bytes than the raw representation.

### Client-Server

Two multi-client servers built on a generic `Server` + **Strategy** design, using a thread pool to handle many clients concurrently:

- **Generate server** — receives dimensions, returns a compressed maze.
- **Solve server** — receives a maze, returns its solution, and **caches** solved mazes to disk so repeated problems are served from file instead of recomputed.

Server behavior (thread-pool size, which generation/solving algorithms to use) is controlled by a `Configurations` singleton backed by a `config.properties` file — no recompilation needed to change settings.

### Running Part A + B

The project includes runnable classes (with `main`) under the `test` package:

- `RunMazeGenerator` — generates and prints mazes, with timing.
- `RunSearchOnMaze` — solves a maze with all three algorithms and reports nodes evaluated.
- `RunCompressDecompressMaze` — compresses a maze to file and verifies a lossless round-trip.
- `RunCommunicateWithServers` — starts both servers and runs sample clients against them.

> **JUnit note:** Part A+B is a plain Java project, so JUnit isn't managed automatically. If the test imports show errors when you open it, place the cursor on `@Test`, press `Alt + Enter`, and add **JUnit 5** to the classpath.

---

## Part C — JavaFX Desktop Game

A desktop maze game built in **JavaFX** on a clean **MVVM** architecture, consuming the Part A+B library (packaged as a JAR).

### Architecture

Strict three-layer separation:

- **Model** — talks to the servers, runs the algorithms, and holds current game state (the maze and the character's position).
- **ViewModel** — connects the View to the Model via data binding.
- **View** — FXML views with their controllers, CSS styling, and a custom maze board **widget**. The game board is a self-contained, reusable control (extending `Canvas`) that receives its behavior from the outside via listeners, so it can be dropped into another project.

### Features

- Generate mazes with user-chosen dimensions.
- Play the maze by moving the character with the NumPad — including diagonals.
- Request a solution, drawn over the maze while you keep playing.
- Save the current maze to disk and load a saved maze.
- Drag the character with the mouse (respecting walls), and `Ctrl + scroll` to zoom.
- Background music during play and a different track on solving.
- A resizable window with a full menu bar (File / Options / Help / About) and error handling via alert dialogs.
- Server activity logged to file with Log4j2 (managed via Maven).

### Running Part C

Open `part-c-javafx` as a Maven project. Once IntelliJ imports the `pom.xml`, run the JavaFX application's main class (`HelloApplication`).
