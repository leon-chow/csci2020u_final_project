
package sample;
import javax.swing.JFrame;
public class ClientTest {
    public static void main(String[] args){
        Cilent charlie;
        charlie = new Cilent("127.0.0.1");
        charlie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        charlie.startRunning();
    }

}
