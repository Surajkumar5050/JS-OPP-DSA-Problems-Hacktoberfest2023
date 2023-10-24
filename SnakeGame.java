import java.util.LinkedList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SnakeGame extends JPanel implements KeyListener, ActionListener {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int CELL_SIZE = 20;
    private static final int DELAY = 100;

    private LinkedList<Point> snake;
    private Point food;
    private int direction;
    private boolean isRunning;
    private Timer timer;

    public SnakeGame() {
        snake = new LinkedList<>();
        snake.add(new Point(5, 5));
        food = new Point(10, 10);
        direction = KeyEvent.VK_RIGHT;
        isRunning = true;

        setPreferredSize(new Dimension(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSnake(g);
        drawFood(g);
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.green);
        for (Point p : snake) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void move() {
        Point head = new Point(snake.getFirst());
        Point newHead = (Point) head.clone();

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.translate(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                newHead.translate(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                newHead.translate(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                newHead.translate(1, 0);
                break;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            generateFood();
        } else {
            snake.removeLast();
        }
    }

    private void generateFood() {
        Random rand = new Random();
        int x, y;

        do {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
        } while (snake.contains(new Point(x, y)));

        food.setLocation(x, y);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT) && (direction != KeyEvent.VK_RIGHT)) {
            direction = KeyEvent.VK_LEFT;
        } else if ((key == KeyEvent.VK_RIGHT) && (direction != KeyEvent.VK_LEFT)) {
            direction = KeyEvent.VK_RIGHT;
        } else if ((key == KeyEvent.VK_UP) && (direction != KeyEvent.VK_DOWN)) {
            direction = KeyEvent.VK_UP;
        } else if ((key == KeyEvent.VK_DOWN) && (direction != KeyEvent.VK_UP)) {
            direction = KeyEvent.VK_DOWN;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            if (collision()) {
                isRunning = false;
            }
            repaint();
        }
    }

    private boolean collision() {
        Point head = snake.getFirst();
        if (head.equals(food)) {
            return false;
        }
        return snake.subList(1, snake.size()).contains(head) || head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
