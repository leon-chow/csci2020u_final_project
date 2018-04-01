
package src.sample;
import javax.swing.*;
public class ClientTest {
    public static void main(String[] args){
        src.sample.Client charlie;
        charlie = new src.sample.Client("127.0.0.1");
        charlie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        charlie.startRunning();
    }

}
