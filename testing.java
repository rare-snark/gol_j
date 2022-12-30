import java.io.File;
import java.util.Scanner;
import java.io.IOException;
public class testing {
    public static void main(String [] args) throws IOException, InterruptedException {
        GOL golFromFile = new GOL(150, "default");

        golFromFile.play(100);

        GOL ranGolFullLaptop = new GOL(179, 28, 100, "default");

//        ranGolFullLaptop.play(100);
    }
}
