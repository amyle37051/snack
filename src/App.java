//import for window
import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        
        //define variables
        int boardWidth = 600;
        int boardHeight = boardWidth;

        //create window
        JFrame frame = new JFrame("snack");

        //make frame visible
        frame.setVisible(true);

        //frame size
        frame.setSize(boardWidth, boardHeight);

        //open window at centre of screen
        frame.setLocationRelativeTo(null); //null ensures it's in centre

        //make frame one solid size
        frame.setResizable(false);

        //make sure program terminate when user clicks on x
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        SnackGame snackGame = new SnackGame(boardWidth, boardHeight);
        frame.add(snackGame);

        //make sure title bar doesn't take up board height
        frame.pack();

        snackGame.requestFocus();
    }
}
