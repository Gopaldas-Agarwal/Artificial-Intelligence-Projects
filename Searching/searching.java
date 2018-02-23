package homework1;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

public class Homework {

    public static String algo;
    public static short nurserySize;
    public static short babyLizards;
    public static short nursery[][];
    public static LizardBFS answerBFS;
    public static LizardDFS answerDFS;
    public static LizardSA answerSA;
    public static Homework homew;
    public static String outputPath;
    public static String inputPath;
    public static void main(String[] args) {
        outputPath="C:\\Users\\GOPAL\\Documents\\NetBeansProjects\\Homework1SA\\src\\homework1\\output.txt";
        inputPath="C:\\Users\\GOPAL\\Documents\\NetBeansProjects\\Homework1SA\\src\\homework1\\input.txt";
        homew = new Homework();
        String inputString = homew.readInputFile();
        homew.parseInput(inputString);
        String temp = "Algo is" + algo + ".";
        if (algo.equals("BFS")) {
            performBFS();
        } else if (algo.equals("DFS")) {
            performDFS();
        } else if (algo.equals("SA")) {
            performSA();
        } else {
            performBoth();
        }

    }

    public static void performBFS() {
        answerBFS = new LizardBFS(nurserySize, babyLizards, nursery);
        homew.outputFile(answerBFS.finalState, answerBFS.nursery.myNursery, answerBFS.nursery.size);
    }

    public static void performDFS() {
        answerDFS = new LizardDFS(nurserySize, babyLizards, nursery);
        homew.outputFile(answerDFS.finalState, answerDFS.nursery.myNursery, answerDFS.nursery.size);
    }

    public static void performSA() {
        answerSA = new LizardSA(nurserySize, babyLizards, nursery);
        homew.outputFile(answerSA.finalState, answerSA.mainState.nursery, answerSA.size);
    }

    public static void performBoth() {
        answerDFS = new LizardDFS(nurserySize, babyLizards, nursery);
        homew.outputFile(answerDFS.finalState, answerDFS.nursery.myNursery, answerDFS.nursery.size);
        answerBFS = new LizardBFS(nurserySize, babyLizards, nursery);
        homew.outputFile(answerBFS.finalState, answerBFS.nursery.myNursery, answerBFS.nursery.size);
    }

    /*
    Creats an output file
     */
    public void outputFile(String status, short[][] nursery, short size) {
        Writer writer = null;

        try {
            File file = new File(outputPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(status + "\n");
            bw.flush();
            if (status.equals("FAIL")) {
                return;
            }
            for (short i = 0; i < size; ++i) {
                for (short j = 0; j < size; ++j) {
                    bw.write(new Integer(nursery[i][j]).toString());
                }
                bw.write("\n");
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

    public void outputFile(String status, int[][] nursery, int size) {
        Writer writer = null;

        try {
            File file = new File(outputPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(status + "\n");
            bw.flush();

            if (status.equals("FAIL")) {
                return;
            }
            for (short i = 0; i < size; ++i) {
                for (short j = 0; j < size; ++j) {
                    bw.write(new Integer(nursery[i][j]).toString());
                }
                bw.write("\n");
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

    public void outputFile(String status) {
        Writer writer = null;

        try {
            File file = new File(outputPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(status + "\n");
            bw.flush();
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                writer.close();

            } catch (Exception ex) {/*ignore*/
            }
        }
    }

    /*
        Parses the input read from input.txt into respective variables
     */
    public void parseInput(String inputString) {
        int hasTrees = 0;
        String lines[] = inputString.split("\n");
        algo = lines[0].trim();

        nurserySize = (short) Integer.parseInt(lines[1].trim());

        babyLizards = (short) Integer.parseInt(lines[2].trim());

        nursery = new short[nurserySize][nurserySize];
        for (short i = 3; i < 3 + nurserySize; ++i) {
            for (short j = 0; j < nurserySize; ++j) {
                nursery[i - 3][j] = (short) Character.getNumericValue(lines[i].charAt(j));
                if (nursery[i - 3][j] == 2) {
                    hasTrees++;
                }
            }
        }
        if (hasTrees==0) {
            if (nurserySize < babyLizards) {
                outputFile("FAILED");
                System.exit(0);
            }
        }
        int emptySpaces=(nurserySize*nurserySize)-hasTrees;
        if(emptySpaces<babyLizards){
            outputFile("FAILED");
                System.exit(0);
        }
    }

    /*
        Reads the input.txt file and retruns a string
     */
    public String readInputFile() {
        FileReader inputFile = null;
        String inputString = "";
        short i = 0;
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

class LizardBFS {

    short size;
    Queue<Node> frontier = new LinkedList<>();
    Node nextNode;
    Solution nursery;
    Solution problem;
    String finalState;

    LizardBFS(short size, short lizards, short[][] ques) {
        this.size = (short) size;
        this.nursery = new Solution(lizards, size);
        this.problem = new Solution(lizards, size, ques);
        mainBFS();
    }

    public void mainBFS() {
        Node rootNode = new Node();
        rootNode.noOfQueens = 0;
        frontier.add(rootNode);
        int counter = 1;
        boolean fail = true;
        while (frontier.peek() != null) {

            nextNode = frontier.poll();
            if (nextNode.noOfQueens == nursery.lizards) {
                fail = false;
                break;
            }
            createNursery();

            addChildren();
            counter++;
        }
        if (fail == true) {
            finalState = "FAIL";
        } else {
            finalState = "OK";
        }
        createNursery();
        printNursery();
    }

    public void printNursery() {
        for (short i = 0; i < nursery.size; ++i) {
            for (short j = 0; j < nursery.size; ++j) {
            }
        }
    }

    public void addChildren() {
        short i, j;
        boolean placed = false;
        if (nextNode.noOfQueens > 0) {
            short column = (short) (nextNode.positions.lastElement().y + 1);

            for (i = nextNode.positions.lastElement().x; i < nursery.size; i++) {
                for (j = column; j < nursery.size; j++) {
                    if (nursery.myNursery[i][j] == 0 && isSafe(i, j)) {
                        Node newNode = new Node(nextNode);
                        newNode.noOfQueens++;
                        newNode.positions.add(new Location(i, j));
                        if (newNode.noOfQueens == nursery.lizards) {
                            frontier.clear();
                            placed = true;
                            frontier.add(newNode);
                            break;
                        }

                        frontier.add(newNode);

                        placed = true;
                    }
                }
                if (placed) {
                    break;

                } else {
                    column = 0;
                }
            }
        } else {
            for (i = 0; i < nursery.size; i++) {
                for (j = 0; j < nursery.size; j++) {
                    if (nursery.myNursery[i][j] == 0 && isSafe(i, j)) {
                        Node newNode = new Node(nextNode);
                        newNode.noOfQueens++;
                        newNode.positions.add(new Location(i, j));
                        if (newNode.noOfQueens == nursery.lizards) {
                            frontier.clear();
                        }
                        frontier.add(newNode);

                        placed = true;
                    }
                }
                if (placed) {
                    break;
                }
            }
        }
    }

    public boolean isSafe(short x, short y) {
        boolean frow, fcol, fdr, fdl;
        frow = true;
        fcol = true;
        fdr = true;
        fdl = true;
        short i, j;
        if (nursery.myNursery[x][y] != 0) {
            return false;
        }

        for (i = (short) (y + 1); i < nursery.size; ++i) {
            if (nursery.myNursery[x][i] == 1) {
                frow = false;
                return false;
            } else if (nursery.myNursery[x][i] == 2) {
                break;
            }
        }

        for (i = (short) (y - 1); i >= 0; --i) {
            if (nursery.myNursery[x][i] == 1) {
                frow = false;
                return false;
            } else if (nursery.myNursery[x][i] == 2) {
                break;
            }
        }
        for (i = (short) (x + 1); i < nursery.size; ++i) {
            if (nursery.myNursery[i][y] == 1) {
                fcol = false;
                return false;
            } else if (nursery.myNursery[i][y] == 2) {
                break;
            }
        }
        for (i = (short) (x - 1); i >= 0; --i) {
            if (nursery.myNursery[i][y] == 1) {
                fcol = false;
                return false;
            } else if (nursery.myNursery[i][y] == 2) {
                break;
            }
        }
        i = (short) (x - 1);
        j = (short) (y - 1);
        for (; i >= 0 && j >= 0; --i, --j) {
            if (nursery.myNursery[i][j] == 1) {
                fdl = false;
                return false;
            } else if (nursery.myNursery[i][j] == 2) {
                break;
            }
        }

        i = (short) (x + 1);
        j = (short) (y + 1);
        for (; i < nursery.size && j < nursery.size; ++i, ++j) {
            if (nursery.myNursery[i][j] == 1) {
                fdl = false;
                return false;
            } else if (nursery.myNursery[i][j] == 2) {
                break;
            }
        }

        i = (short) (x + 1);
        j = (short) (y - 1);
        for (; i < nursery.size && j >= 0; ++i, --j) {
            if (nursery.myNursery[i][j] == 1) {
                fdr = false;
                return false;
            } else if (nursery.myNursery[i][j] == 2) {
                break;
            }
        }

        i = (short) (x - 1);
        j = (short) (y + 1);
        for (; i >= 0 && j < nursery.size; --i, ++j) {
            if (nursery.myNursery[i][j] == 1) {
                fdr = false;
                return false;
            } else if (nursery.myNursery[i][j] == 2) {
                break;
            }
        }

        return (frow && fcol && fdr && fdl);
    }

    public void createNursery() {
        if (nextNode.noOfQueens >= 0) {
            nursery.myNursery = new short[nursery.size][nursery.size];
            for (short i = 0; i < nursery.size; ++i) {
                for (short j = 0; j < nursery.size; ++j) {
                    nursery.myNursery[i][j] = (short) problem.myNursery[i][j];
                }
            }
            Iterator<Location> itr = nextNode.positions.iterator();
            while (itr.hasNext()) {
                Location mylocation = itr.next();
                nursery.myNursery[mylocation.x][mylocation.y] = 1;
            }
        }
    }
}

class LizardDFS {

    short size;
    Stack<Node> frontier = new Stack<Node>();
    Node nextNode;
    Solution nursery;
    Solution problem;
    String finalState;

    LizardDFS(short size, short lizards, short[][] ques) {
        this.size = size;
        this.nursery = new Solution(lizards, size);
        this.problem = new Solution(lizards, size, ques);
        mainDFS();
    }

    public void mainDFS() {
        Node rootNode = new Node();
        rootNode.noOfQueens = 0;
        frontier.push(rootNode);
        int counter = 1;
        boolean fail = true;
        boolean success = false;
        while (frontier.size() > 0) {

            nextNode = frontier.pop();
            if (nextNode.noOfQueens == nursery.lizards) {
                fail = false;
                break;
            }
            createNursery();
            success = addChildren();
            if (success) {
                success = false;
            }
        }
        if (fail == true) {
            finalState = "FAIL";
        } else {
            finalState = "OK";
        }
        createNursery();
    }

    public void printNursery() {
        for (short i = 0; i < nursery.size; ++i) {
            for (short j = 0; j < nursery.size; ++j) {
            }
        }
    }

    public void exploreRoot() {
        short i, j;
        boolean placed = false;
        for (i = 0; i < nursery.size; i++) {
            for (j = 0; j < nursery.size; j++) {
                if (nursery.myNursery[i][j] == 0 && isSafe(i, j)) {
                    Node newNode = new Node(nextNode);
                    newNode.noOfQueens++;
                    newNode.positions.add(new Location(i, j));
                    frontier.push(newNode);
                    if (newNode.noOfQueens == nursery.lizards) {
                    }
                    placed = true;
                }
            }
            if (placed) {
                break;
            }
        }
    }

    public boolean addChildren() {
        short i, j;
        boolean placed = false;
        if (nextNode.noOfQueens > 0) {
            short column = (short) (nextNode.positions.lastElement().y + 1);

            for (i = nextNode.positions.lastElement().x; i < nursery.size; i++) {
                for (j = column; j < nursery.size; j++) {
                    if (nursery.myNursery[i][j] == 0 && isSafe(i, j)) {
                        Node newNode = new Node(nextNode);
                        newNode.noOfQueens++;
                        newNode.positions.add(new Location(i, j));

                        if (newNode.noOfQueens == nursery.lizards) {
                            frontier.clear();
                        }
                        frontier.push(newNode);
                        placed = true;
                    }
                }
                if (placed) {
                    break;

                } else {
                    column = 0;
                }
            }
        } else {
            for (i = 0; i < nursery.size; i++) {
                for (j = 0; j < nursery.size; j++) {
                    if (nursery.myNursery[i][j] == 0 && isSafe(i, j)) {
                        Node newNode = new Node(nextNode);
                        newNode.noOfQueens++;
                        newNode.positions.add(new Location(i, j));
                        if (newNode.noOfQueens == nursery.lizards) {
                            frontier.clear();
                        }
                        frontier.push(newNode);
                        placed = true;
                    }
                }
                if (placed) {
                    break;
                }
            }
        }
        return false;
    }

    public boolean isSafe(short x, short y) {
        boolean frow, fcol, fdr, fdl;
        frow = true;
        fcol = true;
        fdr = true;
        fdl = true;
        short i, j;
        if (nursery.myNursery[x][y] != 0) {
            return false;
        }

        for (i = (short) (y + 1); i < nursery.size; ++i) {
            if (nursery.myNursery[x][i] == 1) {
                frow = false;
                return false;
            } else if (nursery.myNursery[x][i] == 2) {
                break;
            }
        }

        for (i = (short) (y - 1); i >= 0; --i) {
            if (nursery.myNursery[x][i] == 1) {
                frow = false;
                return false;
            } else if (nursery.myNursery[x][i] == 2) {
                break;
            }
        }
        for (i = (short) (x + 1); i < nursery.size; ++i) {
            if (nursery.myNursery[i][y] == 1) {
                fcol = false;
                return false;
            } else if (nursery.myNursery[i][y] == 2) {
                break;
            }
        }
        for (i = (short) (x - 1); i >= 0; --i) {
            if (nursery.myNursery[i][y] == 1) {
                fcol = false;
                return false;
            } else if (nursery.myNursery[i][y] == 2) {
                break;
            }
        }
        i = (short) (x - 1);
        j = (short) (y - 1);
        for (; i >= 0 && j >= 0; --i, --j) {
            if (nursery.myNursery[i][j] == 1) {
                fdl = false;
                return false;
            } else if (nursery.myNursery[i][j] == 2) {
                break;
            }
        }
        i = (short) (x + 1);
        j = (short) (y + 1);
        for (; i < nursery.size && j < nursery.size; ++i, ++j) {
            if (nursery.myNursery[i][j] == 1) {
                fdl = false;
                return false;
            } else if (nursery.myNursery[i][j] == 2) {
                break;
            }
        }
        i = (short) (x + 1);
        j = (short) (y - 1);
        for (; i < nursery.size && j >= 0; ++i, --j) {
            if (nursery.myNursery[i][j] == 1) {
                fdr = false;
                return false;
            } else if (nursery.myNursery[i][j] == 2) {
                break;
            }
        }
        i = (short) (x - 1);
        j = (short) (y + 1);
        for (; i >= 0 && j < nursery.size; --i, ++j) {
            if (nursery.myNursery[i][j] == 1) {
                fdr = false;
                return false;
            } else if (nursery.myNursery[i][j] == 2) {
                break;
            }
        }
        return (frow && fcol && fdr && fdl);
    }

    public void createNursery() {
        if (nextNode.noOfQueens >= 0) {
            nursery.myNursery = new short[nursery.size][nursery.size];
            for (int i = 0; i < nursery.size; ++i) {
                for (int j = 0; j < nursery.size; ++j) {
                    nursery.myNursery[i][j] = (short) problem.myNursery[i][j];
                }
            }
            Iterator<Location> itr = nextNode.positions.iterator();
            while (itr.hasNext()) {
                Location mylocation = itr.next();
                nursery.myNursery[mylocation.x][mylocation.y] = 1;
            }
        }
    }
}

class LizardSA {

    int size;
    int lizards;
    double temperature;
    double rateOfDecrease;
    int conflicts;
    State mainState;
    String finalState;
    double lowerThreshold;
    long startTIme;

    LizardSA(short size, short lizards, short[][] ques) {
        this.lizards = lizards;
        this.temperature = (double) 1;
        this.rateOfDecrease = (double) 0.0001;
        this.size = size;
        conflicts = 0;
        startTIme = System.currentTimeMillis();
        mainState = new State(size);
        this.lowerThreshold = 0.05;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                mainState.nursery[i][j] = ques[i][j];
            }
        }
        finalState = "FAIL";
        randomlyPlaceLizards();
    }

    public void randomlyPlaceLizards() {
        int i = 0;
        while (i < lizards) {
            Location placeHere = randomLocationGenerator(size, size);
            if (mainState.nursery[placeHere.x][placeHere.y] == 0) {
                i++;
                mainState.nursery[placeHere.x][placeHere.y] = 1;
                mainState.frontier.add(placeHere);
            }
        }
        lowerThreshold = 0;
        initEmptySpace();
        conflicts = calculateConfilcts(mainState.nursery, mainState.frontier);
        mainSA();
    }

    public void mainSA() {
        State newCopy;
        boolean foundAns = false;
        int newCollisions;
        int counter = 2;
        int counter2 = 2;
        if (conflicts == 0) {
            finalState = "OK";
            foundAns=true;
        }
        int maxTime;
        if (size >= 0 && size <= 5) {
            maxTime = 5000;
        } else if (size > 5 && size <= 10) {
            maxTime = 15000;
        } else if (size > 10 && size <= 25) {
            maxTime = 30000;
        } else if (size > 25 && size <= 40) {
            maxTime = 60000;
        } else if (size > 40 && size <= 50) {
            maxTime = 120000;
        } else if (size > 50 && size <= 60) {
            maxTime = 160000;
        } else if (size > 60 && size <= 70) {
            maxTime = 180000;
        } else {
            maxTime = 210000;
        }
        
        while (temperature > 0 && conflicts > 0) {
            counter++;
            if (System.currentTimeMillis() - startTIme > maxTime) {
                break;
            }
            newCopy = new State(size);
            newCopy.copy(mainState, newCopy, size);
            moveQueen(newCopy);
            newCollisions = calculateConfilcts(newCopy.nursery, newCopy.frontier);

            if (newCollisions == 0) {
                finalState = "OK";
                foundAns = true;
                printQueens(newCopy);
                mainState.copy(newCopy, mainState, size);
                printNursery(newCopy);
                break;
            }

            if (getProbability(temperature, newCollisions - conflicts)) {
                counter2++;
                mainState.copy(newCopy, mainState, size);
                conflicts = newCollisions;
                temperature = 1 / Math.log(counter2);

            }

        }
        if (!foundAns) {
        }
    }

    public void moveQueen(State s) {
        int moveQueenNo = (new Random().nextInt(lizards));
        int moveToLocation = (new Random().nextInt(mainState.emptyPlaces.size()));
        s.nursery[s.frontier.get(moveQueenNo).x][s.frontier.get(moveQueenNo).y] = 0;
        s.emptyPlaces.add(s.frontier.get(moveQueenNo));
        s.frontier.remove(moveQueenNo);
        s.nursery[s.emptyPlaces.get(moveToLocation).x][s.emptyPlaces.get(moveToLocation).y] = 1;
        s.frontier.add(s.emptyPlaces.get(moveToLocation));
        s.emptyPlaces.remove(moveToLocation);
    }

    public boolean getProbability(double temperature, int delta) {

        if (delta < 0) {
            return true;
        }
        double coeff = Math.exp(-delta / temperature);
        double threshold = Math.random();
        if (threshold < coeff) {
            return true;
        }
        return false;
    }

    public Location randomLocationGenerator(int maxX, int maxY) {
        return (new Location((short) (new Random().nextInt(maxX)), (short) (new Random().nextInt(maxY))));
    }

    public void initEmptySpace() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (mainState.nursery[i][j] == 0) {
                    mainState.emptyPlaces.add(new Location((short) i, (short) j));
                }
            }
        }
    }

    public int calculateConfilcts(int[][] nursery, ArrayList<Location> lizardPos) {
        int countConflicts = 0;

        for (int k = 0; k < lizardPos.size(); k++) {
            int i, j;
            int x, y;
            x = lizardPos.get(k).x;
            y = lizardPos.get(k).y;
            for (i = (y + 1); i < size; ++i) {
                if (nursery[x][i] == 1) {
                    countConflicts++;
                } else if (nursery[x][i] == 2) {
                    break;
                }
            }

            for (i = (y - 1); i >= 0; --i) {
                if (nursery[x][i] == 1) {
                    countConflicts++;
                } else if (nursery[x][i] == 2) {
                    break;
                }
            }

            for (i = (x + 1); i < size; ++i) {
                if (nursery[i][y] == 1) {
                    countConflicts++;
                } else if (nursery[i][y] == 2) {
                    break;
                }
            }

            for (i = (x - 1); i >= 0; --i) {
                if (nursery[i][y] == 1) {
                    countConflicts++;
                } else if (nursery[i][y] == 2) {
                    break;
                }
            }

            i = (x - 1);
            j = (y - 1);
            for (; i >= 0 && j >= 0; --i, --j) {
                if (nursery[i][j] == 1) {
                    countConflicts++;
                } else if (nursery[i][j] == 2) {
                    break;
                }
            }

            i = (x + 1);
            j = (y + 1);
            for (; i < size && j < size; ++i, ++j) {
                if (nursery[i][j] == 1) {
                    countConflicts++;
                } else if (nursery[i][j] == 2) {
                    break;
                }
            }

            i = (x + 1);
            j = (y - 1);
            for (; i < size && j >= 0; ++i, --j) {
                if (nursery[i][j] == 1) {
                    countConflicts++;
                } else if (nursery[i][j] == 2) {
                    break;
                }
            }

            i = (x - 1);
            j = (y + 1);
            for (; i >= 0 && j < size; --i, ++j) {
                if (nursery[i][j] == 1) {
                    countConflicts++;
                } else if (nursery[i][j] == 2) {
                    break;
                }
            }
        }

        return countConflicts;
    }

    public int calculateConfilcts2(int[][] nursery, ArrayList<Location> lizardPos) {
        int countConflicts = 0;

        for (int k = 0; k < lizardPos.size(); k++) {
            int i, j;
            int x, y;
            x = lizardPos.get(k).x;
            y = lizardPos.get(k).y;
            for (i = (y + 1); i < size; ++i) {
                if (nursery[x][i] == 1) {
                    countConflicts++;
                } else if (nursery[x][i] == 2) {
                    break;
                }
            }

            for (i = (y - 1); i >= 0; --i) {
                if (nursery[x][i] == 1) {
                    countConflicts++;
                } else if (nursery[x][i] == 2) {
                    break;
                }
            }

            for (i = (x + 1); i < size; ++i) {
                if (nursery[i][y] == 1) {
                    countConflicts++;
                } else if (nursery[i][y] == 2) {
                    break;
                }
            }

            for (i = (x - 1); i >= 0; --i) {
                if (nursery[i][y] == 1) {
                    countConflicts++;
                } else if (nursery[i][y] == 2) {
                    break;
                }
            }

            i = (x - 1);
            j = (y - 1);
            for (; i >= 0 && j >= 0; --i, --j) {
                if (nursery[i][j] == 1) {
                    countConflicts++;
                } else if (nursery[i][j] == 2) {
                    break;
                }
            }

            i = (x + 1);
            j = (y + 1);
            for (; i < size && j < size; ++i, ++j) {
                if (nursery[i][j] == 1) {
                    countConflicts++;
                } else if (nursery[i][j] == 2) {
                    break;
                }
            }

            i = (x + 1);
            j = (y - 1);
            for (; i < size && j >= 0; ++i, --j) {
                if (nursery[i][j] == 1) {
                    countConflicts++;
                } else if (nursery[i][j] == 2) {
                    break;
                }
            }

            i = (x - 1);
            j = (y + 1);
            for (; i >= 0 && j < size; --i, ++j) {
                if (nursery[i][j] == 1) {
                    countConflicts++;
                } else if (nursery[i][j] == 2) {
                    break;
                }
            }
        }
        return countConflicts;
    }

    public void printNursery(State s) {
        for (short i = 0; i < size; ++i) {
            for (short j = 0; j < size; ++j) {
            }
        }
    }

    public void printQueens(State currState) {
        for (int i = 0; i < lizards; ++i) {
        }
    }

    public void printEmpty(State currState) {
        for (int i = 0; i < currState.emptyPlaces.size(); ++i) {
        }
    }
}

class State {

    ArrayList<Location> frontier = new ArrayList<>();
    ArrayList<Location> emptyPlaces = new ArrayList<>();
    int nursery[][];

    State(int size) {
        nursery = new int[size][size];
    }

    State(State s) {

        this.frontier.addAll(s.frontier);
        this.nursery = s.nursery.clone();
        this.emptyPlaces.addAll(s.emptyPlaces);
    }

    public State copy(State copyFrom, State copyTo, int size) {
        nursery = new int[size][size];
        frontier = new ArrayList<>();
        emptyPlaces = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copyTo.nursery[i][j] = copyFrom.nursery[i][j];
            }
        }
        for (int i = 0; i < copyFrom.frontier.size(); ++i) {
            copyTo.frontier.add(copyFrom.frontier.get(i));
        }
        for (int i = 0; i < copyFrom.emptyPlaces.size(); ++i) {
            copyTo.emptyPlaces.add(copyFrom.emptyPlaces.get(i));
        }
        return copyTo;
    }

}

class Location {

    short x, y;

    Location(short x, short y) {
        this.x = x;
        this.y = y;
    }
}

class Node {

    Vector<Location> positions = new Vector<Location>();
    short noOfQueens;

    public Node() {
    }

    public Node(Node node) {
        this.noOfQueens = node.noOfQueens;
        this.positions = (Vector<Location>) node.positions.clone();
    }
}

class Solution {

    short size;
    short lizards;
    short placed;
    short myNursery[][];

    Solution(short lizards, short size) {
        this.size = (short) size;
        this.lizards = (short) lizards;
        this.placed = 0;
        myNursery = new short[size][size];
    }

    Solution(short lizards, short size, short[][] ques) {
        this.size = (short) size;
        this.lizards = (short) lizards;
        this.placed = 0;
        myNursery = new short[size][size];
        myNursery = ques;
    }
}
