import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class GOL {
//            0 1 2 j [0, golx[i].length-1]
//
//        0   0 0 0
//        1   0 0 0
//        2   0 0 0
//        i
//        [0, golx.length-1]

    private int width, length, delay;
    private int[][] gol1, gol2;
    private String ruleset;
    private boolean fromFile;

    public static void main(String [] args) throws InterruptedException{
        // Math.random() * (max - min + 1) + min
        int width = 228;
        int length = 37;
        int[][] gol1 = new int[length][width];
        int[][] gol2 = new int[length][width];



        for (int i = 0; i < gol1.length; i++)
            for (int j = 0; j < gol1[i].length; j++)
                gol1[i][j] = (int) (Math.random() * 2);

        int isAlive, nCount;
        boolean canLeft, canRight, canUp, canDown;

        String lightShade = "\u2591";
        String fullBlock = "\u2588";

        String aliveCell = fullBlock;
        String deadCell = lightShade;

        System.out.println("Generation 0:");
        for (int i = 0; i < gol1.length; i++) {
            for (int j = 0; j < gol1[i].length; j++) {
                if (gol1[i][j]==0) System.out.print(deadCell);
                else System.out.print(aliveCell);
            }
            System.out.println();
        }

        for (int x = 0; x < 500; x++) {
            Thread.sleep(100);
            System.out.printf("Generation %d:%n", x + 1);
            for (int i = 0; i < gol1.length; i++) {
                for (int j = 0; j < gol1[i].length; j++) {

                    isAlive = gol1[i][j];
                    nCount = 0;
                    canLeft = false;
                    canRight = false;
                    canUp = false;
                    canDown = false;

                    if (i - 1 != -1) canUp = true;
                    if (i + 1 != gol1.length) canDown = true;
                    if (j - 1 != -1) canLeft = true;
                    if (j + 1 != gol1[i].length) canRight = true;

                    if (canUp) nCount += gol1[i - 1][j];
                    if (canDown) nCount += gol1[i + 1][j];
                    if (canLeft) nCount += gol1[i][j - 1];
                    if (canRight) nCount += gol1[i][j + 1];

                    if (canUp && canLeft) nCount += gol1[i - 1][j - 1];
                    if (canUp && canRight) nCount += gol1[i - 1][j + 1];
                    if (canDown && canLeft) nCount += gol1[i + 1][j - 1];
                    if (canDown && canRight) nCount += gol1[i + 1][j + 1];

                    gol2[i][j] = 0;
                    if (isAlive == 1 && (nCount == 2 || nCount == 3))
                        gol2[i][j] = 1;
                    else if (isAlive == 0 && nCount == 3)
                        gol2[i][j] = 1;

                    //System.out.println(String.format("isAlive: %s\ncanUp: %s\ncanDown: %s\ncanLeft: %s\ncanRight: %s\nnCount: %s", isAlive, canUp, canDown, canLeft, canRight, nCount));
                }
            }
            //System.out.println();
            //iterOut2D(gol2);
            for (int i = 0; i < gol2.length; i++) {
                for (int j = 0; j < gol2[i].length; j++) {
                    if (gol2[i][j]==0) System.out.print(deadCell);
                    else System.out.print(aliveCell);
                }
                System.out.println();
            }
            for (int i = 0; i < gol1.length; i++)
                for (int j = 0; j < gol1[i].length; j++)
                    gol1[i][j] = gol2[i][j];
            //Thread.sleep(1);
        }
    }

    //constructors
    //clone existing
    public GOL(GOL other){
        this.width = other.width;
        this.length = other.length;
        this.gol1 = other.gol1;
        this.gol2 = other.gol2;
        this.ruleset = other.ruleset;
        this.fromFile = other.fromFile;
    }
    //default settings. fills out intellij on default font size
    public GOL(){
        this.width = 227;
        this.length = 37;
        this.delay = 100;
        this.ruleset = "default";
        fromFile = false;
    }
    //customizable settings
    public GOL(int width, int length, int delay, String ruleset){
        this.width = width;
        this.length = length;
        this.delay = delay;
        this.ruleset = ruleset;
        fromFile = false;
    }
    //feed array from code, custom settings
    public GOL(int arr[][], int delay, String ruleset){
        this.delay = delay;
        this.ruleset = ruleset;
        gol1 = arr;
        gol2 = new int[arr.length][arr[1].length];
        fromFile = false;
    }
    //feed array from file, custom settings
    public GOL(int delay, String ruleset) throws FileNotFoundException {
        this.delay = delay;
        this.ruleset = ruleset;
        fromFile = true;
    }

    //make the arrays to the specified size and randomize the contents of the first array
    private void initRandArr(){
        this.gol1 = new int[this.length][this.width];
        this.gol2 = new int[this.length][this.width];

        for (int i = 0; i < gol1.length; i++)
            for (int j = 0; j < gol1[i].length; j++)
                gol1[i][j] = (int) (Math.random() * 2);
    }

    //pull array from file
    private void arrFromFile() throws FileNotFoundException {
        File arrIn = new File("arrIn.txt");
        Scanner s = new Scanner(arrIn);

        int width = s.nextLine().length();
        int length = 1;
        while(s.hasNext()){
            length++;
            s.nextLine();
        }
        s.close();

        gol1 = new int[length][width];
        gol2 = new int[length][width];

        Scanner w = new Scanner(arrIn);

        String row;
        for (int i = 0; i < length; i++) {
            row = w.nextLine();
            for (int j = 0; j < width; j++) {
                gol1[i][j] = 48 == row.charAt(j)?0:1;
            }
        }
    }

    //play functions. each function calls the bottom most function
    //default play setting. perpetual, 500 generations per run, 100 ms delay b/w generations
    public void play() throws InterruptedException, FileNotFoundException{ //TODO
        while (true) play(1, 500);
    }
    //custom perpetual. perpetual, <int generations> per run, <int delay> ms delay b/w generations
    public void play(int generations) throws InterruptedException, FileNotFoundException{
        while (true) play(1, generations);
    }
    //customizable settings. <int iterations> runs, <int generations> generations per run, <int delay> ms delay b/w generations
    public void play(int iterations, int generations) throws InterruptedException, FileNotFoundException{ //TODO
        int isAlive, nCount;
        boolean canLeft, canRight, canUp, canDown;

        for (int l = 0; l < iterations; l++) {
            if (fromFile){
                arrFromFile();
            } else{
                initRandArr();
            }
            System.out.println("Generation 0:");
            iterOutGOL(gol1);
            for (int x = 0; x < generations; x++) {
                Thread.sleep(delay);
                System.out.printf("Generation %d:%n", x + 1);
                for (int i = 0; i < gol1.length; i++) {
                    for (int j = 0; j < gol1[i].length; j++) {

                        isAlive = gol1[i][j];
                        nCount = 0;
                        canLeft = false;
                        canRight = false;
                        canUp = false;
                        canDown = false;

                        if (i - 1 != -1) canUp = true;
                        if (i + 1 != gol1.length) canDown = true;
                        if (j - 1 != -1) canLeft = true;
                        if (j + 1 != gol1[i].length) canRight = true;

                        if (canUp) nCount += gol1[i - 1][j];
                        if (canDown) nCount += gol1[i + 1][j];
                        if (canLeft) nCount += gol1[i][j - 1];
                        if (canRight) nCount += gol1[i][j + 1];

                        if (canUp && canLeft) nCount += gol1[i - 1][j - 1];
                        if (canUp && canRight) nCount += gol1[i - 1][j + 1];
                        if (canDown && canLeft) nCount += gol1[i + 1][j - 1];
                        if (canDown && canRight) nCount += gol1[i + 1][j + 1];

                        gol2[i][j] = ruleCheck(isAlive, nCount);

                        //debugger
                        //System.out.println(String.format("isAlive: %s\ncanUp: %s\ncanDown: %s\ncanLeft: %s\ncanRight: %s\nnCount: %s", isAlive, canUp, canDown, canLeft, canRight, nCount));
                    }
                }
                iterOutGOL(gol2);
                for (int i = 0; i < gol1.length; i++)
                    for (int j = 0; j < gol1[i].length; j++)
                        gol1[i][j] = gol2[i][j];
            }
        }
    }

    //rule checker and rulesets, allows for different rulesets based off of an arg passed in initialization
    private int ruleCheck(int isAlive, int nCount){
        int[] b = {}, s = {};
        switch (ruleset){
            // certified John Conway classic
            case "default":
                b = new int[]{3};
                s = new int[]{2, 3};
                break;
            // every pattern is eventually replaced by multiple copies of itself by Edward Fredkin
            case "replicator":
                b = new int[]{1, 3, 5, 7};
                s = new int[]{1, 3, 5, 7};
                break;
            // phoenix mode. extremely chaotic and explosive. some known patterns w/ complex behavior
            case "seeds":
                b = new int[]{2};
                break;
            // small self-replicating patter
            // can bounce glider back and forth
            case "blank":
            case "kuhaku":
                b = new int[]{2, 5};
                s = new int[]{4};
                break;
            // chaotic growth and ladder-like patterns
            case "life without death":
            case "life w/o death":
            case "inkspot":
            case "flakes":
                b = new int[]{3};
                s = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
                break;
            // originally thought to be stable, larger patterns explode
            // many oscillators and spaceships
            case "34 life":
            case "34":
                b = new int[]{3, 4};
                s = new int[]{3, 4};
                break;
            // forms large diamonds with chaotically fluctuating boundaries
            // interesting story behind this one
            case "diamoeba":
            case "diamond":
            case "amoeba":
                b = new int[]{3, 5, 6, 7, 8};
                s = new int[]{5, 6, 7, 8};
                break;
            // make block
            case "2x2":
                b = new int[]{3, 6};
                s = new int[]{1, 2, 5};
                break;
            // similar to life but w/ small self-replicating pattern
            case "highlife":
                b = new int[]{3, 6};
                s = new int[]{2, 3};
                break;
            // interesting engineered patterns w/ highly complex behavior
            case "day and night":
                b = new int[]{3, 6, 7, 8};
                s = new int[]{3, 4, 6, 7, 8};
                break;
            //supports very high-period and slow spaceships, named after Stephen Morley
            case "morley":
            case "move":
                b = new int[]{3, 6, 8};
                s = new int[]{2, 4, 5};
                break;
            // approximates curve-shortening flow on the boundaries b/w life and death
            case "anneal":
            case "twisted majority rule":
                b = new int[]{4, 6, 7, 8};
                s = new int[]{3, 5, 6, 7, 8};
        }
        return ((isAlive == 1 && Arrays.binarySearch(s, nCount) >= 0)||(isAlive == 0 && Arrays.binarySearch(b, nCount) >= 0))? 1: 0;
    }

    //TODO make dead and alive cell text a choice on initialization
    //iterates through and prints out play the game of life array
    private void iterOutGOL(int[][] arr){

        //emojis
        String skull = "\uD83D\uDC80";
        String slightSmile = "\uD83D\uDE42";
        //unicode shaders
        String lightShade = "\u2591";
        String fullBlock = "\u2588";

        String aliveCell = fullBlock;
        String deadCell = lightShade;

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j]==0) System.out.print(deadCell);
                else System.out.print(aliveCell);
            }
            System.out.println();
        }
    }
}