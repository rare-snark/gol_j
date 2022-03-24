import java.io.File;
import java.util.Scanner;
import java.io.IOException;
public class testing {
    public static void main(String [] args) throws IOException, InterruptedException {
        GOL gol = new GOL(150, "default");
        gol.play();
    }
}
