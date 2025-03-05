import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //for storing segments of snake body
import java.util.Random; //make sure snack spawns at random spots
import javax.swing.*;

public class SnackGame extends JPanel implements ActionListener, KeyListener{
    //private class to keep track of all tile coordinates
    private class Tile{
        int x;
        int y;

        //constructor
        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    //initialise variables
    int boardWidth;
    int boardHeight;
    int tileSize = 25; //size of tiles that board is divided into

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityx;
    int velocityy;
    boolean gameOver = false;

    //constructor
    SnackGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.DARK_GRAY);
        addKeyListener(this);
        setFocusable(true);

        /**default starting spot for snake head
         * is 5squares x, 5squares y
         * which is the equivalent of 125 pixels each way
         * since each square is 25 x 25 pixels
         */
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityx = 0;
        velocityy = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    /**paint function
     * 
     * @param g used for drawing 
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    /**draw function
     * @param g
     * 
     */
    public void draw(Graphics g){
        
        //draw gridlines for easier visualisation for now...
        //for(int i = 0; i < boardWidth/tileSize; i++){
        //    //x1, y1, x2, y2
        //    g.drawLine(i * tileSize, 0, i * tileSize, boardHeight); // vertical lines
        //    g.drawLine(0, i * tileSize, boardWidth, i * tileSize) ; //horizontal lines
        //}

        //food
        g.setColor(Color.WHITE);
        //g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        //snake head
        g.setColor(Color.GREEN);

        /**draw rectangle
         *snakeHead.x must multiply by tileSize
         *otherwise the fill takes it as literally 5 pixels
         *instead of 5 units of 25 pixels each 
         */
        //g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //snake body
        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Try harder next time: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else{
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    /**function to spawn food randomly each time
     * 
     */
    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    //move function
    public void move(){

        //eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //snake body
        for(int i = snakeBody.size()-1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //snake head
        snakeHead.x += velocityx;
        snakeHead.y += velocityy;

        //game over condition
        for(int i = 0; i <snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //collide with snake head
            if(collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight){
                gameOver = true;
            }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityy != 1){
            velocityx = 0;
            velocityy = -1;
        }

        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityy != -1){
            velocityx = 0;
            velocityy = 1;
        }

        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityx != 1){
            velocityx = -1;
            velocityy = 0;
        }

        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityx != -1){
            velocityx = 1;
            velocityy = 0;
        }
    }

    //do not need
    @Override
    public void keyTyped(KeyEvent e) {
    }



    @Override
    public void keyReleased(KeyEvent e) {
    }
}
