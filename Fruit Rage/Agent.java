import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.lang.management.*;

class Location {

    short x, y;

    Location(short x1, short y1) {
        this.x = x1;
        this.y = y1;
    }

    Location(int x1, int y1) {
        this.x = (short) x1;
        this.y = (short) y1;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class CalibrationInfo {

    double totalTime, totalIterations, timePerMove, movesPerSecond;

    CalibrationInfo() {
    }

    CalibrationInfo(CalibrationInfo cInfo) {
        this.totalTime = cInfo.totalTime;
        this.totalIterations = cInfo.totalIterations;
        this.timePerMove = cInfo.timePerMove;
        this.movesPerSecond = cInfo.movesPerSecond;
    }
}

class Node {

    ArrayList<Location> move;
    int score;

    Node() {
        move = new ArrayList<>();
        score = 0;
    }

    void setScore(int score) {
        this.score = score;
    }

    void Node(Node n) {
        this.score = n.score;
        for (short i = 0; i < move.size(); ++i) {
            this.move.get(i).x = n.move.get(i).x;
            this.move.get(i).x = n.move.get(i).x;
        }
    }

    public String toString() {
        String str = "Moves=";
        for (short i = 0; i < move.size(); ++i) {
            str += "(" + move.get(i).x + "," + move.get(i).y + ")";
        }
        str += " score=" + score;
        return str;
    }
}

class GameState {

    short boardSize, fruits;
    float remainingTime;
    char board[][];

    GameState(short boardSize, short fruits, float time) {
        this.boardSize = boardSize;
        this.fruits = fruits;
        remainingTime = time;
        board = new char[boardSize][boardSize];
    }

    GameState(int boardSize, int fruits, float time) {
        this.boardSize = (short) boardSize;
        this.fruits = (short) fruits;
        remainingTime = time;
        board = new char[boardSize][boardSize];
    }

    GameState() {
    }

    GameState(GameState game) {
        this.boardSize = game.boardSize;
        this.fruits = game.fruits;
        this.remainingTime = game.remainingTime;
        board = new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                this.board[i][j] = game.board[i][j];
            }
        }
    }

    void displayGameinfo() {

        System.out.println("Board size=" + boardSize);
        System.out.println("Types of fruits=" + fruits);
        System.out.println("Time Reamining=" + remainingTime);
        for (int i = 0; i < boardSize; ++i) {

            for (int j = 0; j < boardSize; ++j) {
                System.out.print(board[i][j]);
            }
            System.out.print("\n");
        }

    }

}

class ReadCalibrationInfo {

    CalibrationInfo cInfo;
    String inputPath;

    ReadCalibrationInfo() {
        inputPath = "calibration.txt";
        cInfo = new CalibrationInfo();
    }

    void getInputFromFile() {
        String inputString = readInputFile();
        cInfo = parseInput(inputString);
    }

    String readInputFile() {
        FileReader inputFile = null;
        String inputString = "";
        int i = 0;
        try {
            inputFile = new FileReader(inputPath);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        try {
            BufferedReader br = new BufferedReader(inputFile);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            inputString = sb.toString();

        } catch (Exception e) {
            System.out.println("Error reading file:" + e);
        } finally {
            try {
                inputFile.close();
            } catch (Exception e) {
                System.out.println("Error Closing File");
            }
        }
        return inputString;
    }

    CalibrationInfo parseInput(String inputString) {
        String lines[] = inputString.split("\n");
        cInfo.totalIterations = Double.parseDouble(lines[0].trim());
        cInfo.totalTime = Double.parseDouble(lines[1].trim());
        cInfo.timePerMove = Double.parseDouble(lines[2].trim());
        cInfo.movesPerSecond = Double.parseDouble(lines[3].trim());

        return cInfo;
    }

}

class ReadGameInfo {

    String inputPath;
    GameState gameInf;

    ReadGameInfo() {

        inputPath = "input.txt";
        gameInf = new GameState();
    }

    void getInputFromFile() {
        String inputString = readInputFile();
        gameInf = parseInput(inputString);
    }

    GameState returnGameStateInfo() {
        return gameInf;
    }

    GameState parseInput(String inputString) {
        String lines[] = inputString.split("\n");
        gameInf.boardSize = (short) Integer.parseInt(lines[0].trim());
        gameInf.fruits = (short) Integer.parseInt(lines[1].trim());
        gameInf.remainingTime = (float) Float.parseFloat(lines[2].trim());
        gameInf.board = new char[gameInf.boardSize][gameInf.boardSize];
        for (int i = 3; i < 3 + gameInf.boardSize; ++i) {
            for (int j = 0; j < gameInf.boardSize; ++j) {
                gameInf.board[i - 3][j] = lines[i].charAt(j);
            }
        }
        return gameInf;
    }

    String readInputFile() {
        FileReader inputFile = null;
        String inputString = "";
        int i = 0;
        try {
            inputFile = new FileReader(inputPath);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        try {
            BufferedReader br = new BufferedReader(inputFile);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            inputString = sb.toString();

        } catch (IOException e) {
            System.out.println("Error reading file:" + e);
        } finally {
            try {
                inputFile.close();
            } catch (IOException e) {
                System.out.println("Error Closing File");
            }
        }
        return inputString;
    }

}

class WriteGameInfo {

    String outputPath;
    GameState game;
    Location move;

    WriteGameInfo() {
        outputPath = "output.txt";
    }

    WriteGameInfo(GameState game, Location move) {
        this();
        this.move.x = move.x;
        this.move.y = move.y;
        game = new GameState(game);
    }

    void writeOutputToFile(Location move, GameState game) {
        Writer writer = null;
        try {
            File file = new File(outputPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write((move.y + 65));
            bw.write((move.x + 1) + "");
            bw.flush();

            for (int i = 0; i < game.boardSize; ++i) {
                bw.write("\n");
                for (int j = 0; j < game.boardSize; ++j) {
                    bw.write(game.board[i][j]);
                }
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/
            }

        }
    }
}

class PlayGame {

    GameState mygame;
    CalibrationInfo cInfo;
    Node finalMove;
    int count;
    int maxDepth;

    PlayGame(GameState game, CalibrationInfo cInfo) {
        this(game);
        this.cInfo = new CalibrationInfo(cInfo);
    }

    PlayGame(GameState game) {
        finalMove = new Node();
        count = 0;
        this.mygame = new GameState(game);
    }

    void playTheGameStatic(int depth) {
        maxDepth = depth;
        minimax(maxDepth, new Node(), true, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    void playTheGameDynamic() {
        maxDepth = decideDepth();
        minimax(maxDepth, new Node(), true, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

 	int possibleMoves(){
    	return getAllPossibleMoves(new GameState(mygame), new Node()).size();
    }
 
    int decideDepth() {
        double possibleMoves = getAllPossibleMoves(new GameState(mygame), new Node()).size();
        double timeForMove = (mygame.remainingTime / possibleMoves);
        double possibleMovesInTime = timeForMove * cInfo.movesPerSecond;
        int depth = 1;
        for (int i = 1; i < 10; ++i) {
            depth++;
            if (Math.pow(possibleMoves, i) > possibleMovesInTime) {
                break;
            }

        }
        System.out.println("Computed depth=" + depth);
        return depth;
    }

    void giveOutput() {
        executeMove(mygame, finalMove.move.get(0));
        new WriteGameInfo().writeOutputToFile(finalMove.move.get(0), mygame);
    }

    int minimax(int depth, Node n, boolean maxPlayer, int alpha, int beta) {
        ++count;
        if (depth == 0) {
            return n.score;
        }
     	
        ArrayList<Node> possibleMoves = getAllPossibleMoves(new GameState(mygame), n);
        if (possibleMoves.size() == 0) {
            return n.score;
        }
        appendPossibleMoves(n, possibleMoves, maxPlayer);
        if (maxPlayer) {
            sortPossibleMovesForPlayer(possibleMoves, -1);
            for (int i = 0; i < possibleMoves.size(); ++i) {
                int currScore = minimax(depth - 1, possibleMoves.get(i), false, alpha, beta);
                if (currScore > alpha) {
                    alpha = currScore;
                    if (depth == maxDepth) {
                        finalMove = possibleMoves.get(i);
                        finalMove.score = currScore;
                    }
                }
                if (alpha >= beta) {
                }
            }
            return alpha;
        } else {
            sortPossibleMovesForPlayer(possibleMoves, 1);
            for (int i = 0; i < possibleMoves.size(); ++i) {
                int currScore = minimax(depth - 1, possibleMoves.get(i), true, alpha, beta);
                beta = Math.min(currScore, beta);
                if (alpha >= beta) {
                }
            }
            return beta;
        }
    }

    void sortPossibleMovesForPlayer(ArrayList<Node> possibleMoves, int x) {
        Collections.sort(possibleMoves, new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                if (n1.score == n2.score) {
                    return 0;
                } else if (n1.score > n2.score) {
                    return x;
                } else {
                    return -x;
                }
            }
        });
    }

    void displayMovesList(ArrayList<Node> possibleMoves) {
        System.out.println("\nSorted:");
        for (int i = 0; i < possibleMoves.size(); ++i) {
            System.out.println(possibleMoves.get(i));
        }
    }

    void appendPossibleMoves(Node n, ArrayList<Node> possibleMoves, boolean maxPlayer) {
        for (int i = 0; i < possibleMoves.size(); i++) {
            Node newNode = new Node();
            newNode.score = n.score;
            for (int j = 0; j < n.move.size(); j++) {
                newNode.move.add(n.move.get(j));
            }
            if (maxPlayer) {
                newNode.score += possibleMoves.get(i).score;
            } else {
                newNode.score -= possibleMoves.get(i).score;
            }
            newNode.move.add(possibleMoves.get(i).move.get(0));
            possibleMoves.set(i, newNode);
        }
    }

    ArrayList<Node> appendPossibleMoves2(Node n, ArrayList<Node> possibleMoves, boolean maxPlayer) {
        ArrayList<Node> newPossibleMoves = new ArrayList<>();
        for (int i = 0; i < possibleMoves.size(); i++) {
            Node newNode = new Node();
            newNode.score = n.score;
            for (int j = 0; j < n.move.size(); j++) {
                newNode.move.add(n.move.get(j));
            }
            if (maxPlayer) {
                newNode.score += possibleMoves.get(i).score;
            } else {
                newNode.score -= possibleMoves.get(i).score;
            }
            newNode.move.add(possibleMoves.get(i).move.get(0));
            newPossibleMoves.add(newNode);

        }
        return newPossibleMoves;
    }

    ArrayList<Node> getAllPossibleMoves(GameState game, Node n) {
        ArrayList<Node> newPossibleMoves = new ArrayList<>();
        int k = 0;
        boolean traversed[][] = new boolean[game.boardSize][game.boardSize];
        executeSequence(n, game);
        for (int i = 0; i < game.boardSize; i++) {
            for (int j = 0; j < game.boardSize; j++) {
                if (game.board[i][j] == '*') {
                    traversed[i][j] = true;
                }
                if (!traversed[i][j]) {
                    newPossibleMoves.add(new Node());
                    newPossibleMoves.get(k).move.add(new Location(i, j));
                    Queue<Location> adjacent = new LinkedList<>();
                    adjacent.add(new Location(i, j));
                    traversed[i][j] = true;
                    int score = 0;
                    while (adjacent.size() > 0) {
                        score++;
                        Location lastNewMove = adjacent.poll();
                        int row = lastNewMove.x, col = lastNewMove.y;
                        char ch = game.board[row][col];
                        if ((row + 1) < game.boardSize && game.board[row + 1][col] == ch && !traversed[row + 1][col]) {
                            adjacent.add(new Location(row + 1, col));
                            traversed[row + 1][col] = true;
                        }
                        if ((col + 1) < game.boardSize && game.board[row][col + 1] == ch && !traversed[row][col + 1]) {
                            adjacent.add(new Location(row, col + 1));
                            traversed[row][col + 1] = true;
                        }
                        if ((row - 1) >= 0 && game.board[row - 1][col] == ch && !traversed[row - 1][col]) {
                            adjacent.add(new Location(row - 1, col));
                            traversed[row - 1][col] = true;
                        }
                        if ((col - 1) >= 0 && game.board[row][col - 1] == ch && !traversed[row][col - 1]) {
                            adjacent.add(new Location(row, col - 1));
                            traversed[row][col - 1] = true;
                        }
                    }
                    newPossibleMoves.get(k).score = (score * score);
                    k++;
                }
            }
        }
        return newPossibleMoves;
    }

    void executeSequence(Node n, GameState game) {
        for (int i = 0; i < n.move.size(); ++i) {
            executeMove(game, n.move.get(i));
        }
    }

    void executeMove(GameState game, Location executeMove) {

        Queue<Location> frontier = new LinkedList<>();
        Queue<Location> visited = new LinkedList<>();
        boolean traversed[][] = new boolean[game.boardSize][game.boardSize];
        frontier.add(executeMove);
        Location nextCell;

        while (frontier.peek() != null) {

            nextCell = frontier.poll();
            int row = nextCell.x;
            int col = nextCell.y;
            char currChar = game.board[row][col];
            traversed[row][col] = true;
            if (((row + 1) < game.boardSize) && (currChar == game.board[row + 1][col])) {
                if (!traversed[row + 1][col]) {
                    traversed[row + 1][col] = true;
                    frontier.add(new Location((int) (row + 1), col));
                }
            }

            if (((col + 1) < game.boardSize) && (currChar == game.board[row][col + 1])) {
                if (!traversed[row][col + 1]) {
                    traversed[row][col + 1] = true;
                    frontier.add(new Location((int) (row), col + 1));
                }
            }

            if (((row - 1) >= 0) && (currChar == game.board[row - 1][col])) {
                if (!traversed[row - 1][col]) {
                    traversed[row - 1][col] = true;
                    frontier.add(new Location((int) (row - 1), col));
                }
            }

            if (((col - 1) >= 0) && (currChar == game.board[row][col - 1])) {
                if (!traversed[row][col - 1]) {
                    traversed[row][col - 1] = true;
                    frontier.add(new Location((int) (row), col - 1));
                }
            }
            visited.add(nextCell);
            game.board[row][col] = '*';
        }
        applyGravity(game, visited);
    }

    int executeMoveWithoutGravity(GameState game, Location executeMove) {

        Queue<Location> frontier = new LinkedList<>();
        Queue<Location> visited = new LinkedList<>();
        boolean traversed[][] = new boolean[game.boardSize][game.boardSize];
        frontier.add(executeMove);
        Location nextCell;

        while (frontier.peek() != null) {

            nextCell = frontier.poll();
            int row = nextCell.x;
            int col = nextCell.y;
            char currChar = game.board[row][col];
            traversed[row][col] = true;
            if (((row + 1) < game.boardSize) && (currChar == game.board[row + 1][col])) {
                if (!traversed[row + 1][col]) {
                    traversed[row + 1][col] = true;
                    frontier.add(new Location((int) (row + 1), col));
                }
            }

            if (((col + 1) < game.boardSize) && (currChar == game.board[row][col + 1])) {
                if (!traversed[row][col + 1]) {
                    traversed[row][col + 1] = true;
                    frontier.add(new Location((int) (row), col + 1));
                }
            }

            if (((row - 1) >= 0) && (currChar == game.board[row - 1][col])) {
                if (!traversed[row - 1][col]) {
                    traversed[row - 1][col] = true;
                    frontier.add(new Location((int) (row - 1), col));
                }
            }

            if (((col - 1) >= 0) && (currChar == game.board[row][col - 1])) {
                if (!traversed[row][col - 1]) {
                    traversed[row][col - 1] = true;
                    frontier.add(new Location((int) (row), col - 1));
                }
            }
            visited.add(nextCell);
            game.board[row][col] = '*';
        }
        int score = visited.size();
        return score;
    }

    void applyGravity(GameState game, Queue<Location> visited) {
        while (visited.peek() != null) {
            Location move = visited.poll();
            int x = move.x;
            int x1 = x;
            int y = move.y;
            while (x >= 0) {
                if (game.board[x][y] != '*') {
                    char temp = game.board[x][y];
                    game.board[x][y] = game.board[x1][y];
                    game.board[x1][y] = temp;
                    x1 = x;
                }
                x--;
            }
        }
    }

}

public class Agent {

    public static void main(String[] args) {

        ReadGameInfo readNewGame = new ReadGameInfo();
        readNewGame.getInputFromFile();
        GameState initialGameState = new GameState(readNewGame.returnGameStateInfo());

        PlayGame fruitRage = new PlayGame(initialGameState);

        int possibleMoves = fruitRage.possibleMoves();
        int boardSize = fruitRage.mygame.boardSize;
        int depth = 3;
			
			
			if (fruitRage.mygame.remainingTime < 1) {
            depth = 1;
        } else if (fruitRage.mygame.remainingTime < 2) {
            depth = 2;
        } else {
            if (boardSize < 6) {
                depth = 6;
            } else if (boardSize < 17) {
                if (possibleMoves <= 100) {
                    depth = 5;
                } else if (possibleMoves <= 200) {
                    depth = 4;
                }
            } else if (boardSize < 22) {
                if (possibleMoves < 60) {
                    depth = 5;
                } else if (possibleMoves < 100) {
                    depth = 4;
                }
            } else if (boardSize < 27) {
                if (possibleMoves < 40) {
                    depth = 5;
                } else if (possibleMoves < 80) {
                    depth = 4;
                }
            }
        }
        fruitRage.playTheGameStatic(depth);
        fruitRage.giveOutput();
    }
}
