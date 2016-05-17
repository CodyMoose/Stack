package stack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

public class StackPanel extends JPanel implements KeyListener
{
    /**
     * 
     */
    private static final long          serialVersionUID = -5303734901252872436L;
    public static ArrayList<Color>     rectColors       = new ArrayList<Color>();
    public static ArrayList<Rectangle> rects            = new ArrayList<Rectangle>();
    public static int                  score            = 0;
    public static DoubleDimension      startSizeRatio   = new DoubleDimension(1. / 3., 1. / 3.);
    public static Rectangle            currentRect      = new Rectangle(0, 0, 300, 300);
    public static Color                currentColor     = new Color(0, 0, 0);
    public static boolean              lastTop          = true;
    public static DoubleDimension      currentSize      = new DoubleDimension(currentRect.getSize());
    public static Random               rand             = new Random();
    public static Rectangle            blackBase        = new Rectangle(0, 0, 0, 0);
    public static final double         SPEED            = 2.;
    /**
     * The time at which the game was started in miliseconds. Used for refresh
     * rate.
     */
    private static final long          START_TIME       = System.currentTimeMillis();
    /**
     * The optimal fps to keep the game running at.
     */
    private static final int           OPTIMAL_FPS      = 60;
    /**
     * Amount of miliseconds between each frame at optimal frame rate
     */
    private static final double        SKIP_TICKS       = 1000 / OPTIMAL_FPS;
    /**
     * The time at which the next game tick should occur
     */
    private static double              nextGameTick     = START_TIME + SKIP_TICKS;
    /**
     * How many ticks to sleep each cycle to keep with the optimal FPS
     */
    private static double              sleepTicks       = 0;
    private static boolean             goingForward     = true;

    public StackPanel(Stack stack)
    {
        stack.addKeyListener(this);
        addKeyListener(this);
        setBackground(Color.WHITE);
        setSize(450, 450);
        setVisible(true);
        setLocation(0, 0);
        currentRect.setSize((int) (getWidth() * startSizeRatio.getX()), (int) (getHeight() * startSizeRatio.getY()));
        currentRect.setLocation((int) -currentRect.getWidth(), (int) (getHeight() / 2 - currentRect.getHeight() / 2));
        currentColor = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        requestFocus();
        blackBase = new Rectangle((int) (getWidth() / 2 - currentRect.getWidth() / 2),
                        (int) (getHeight() / 2 - currentRect.getHeight() / 2),
                        (int) (getWidth() * startSizeRatio.getX()), (int) (getHeight() * startSizeRatio.getY()));
        rects.add(new Rectangle(blackBase));
        rectColors.add(Color.BLACK);
        repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.WHITE);
        g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
        for (int i = 0; i < rects.size(); i++)
        {
            g2.setPaint(rectColors.get(i));
            g2.fill(rects.get(i));
        }
        g2.setPaint(currentColor);
        if (currentRect.getMaxX() > getWidth())
        {
            goingForward = false;
        }
        else if (currentRect.getX() < 0) goingForward = true;
        if (currentRect.getMaxY() > getHeight()) goingForward = false;
        else if (currentRect.getY() < 0) goingForward = true;
        if (lastTop)
        {
            if (goingForward) currentRect.setLocation((int) (currentRect.getX() + SPEED), (int) currentRect.getY());
            else if (!goingForward)
                currentRect.setLocation((int) (currentRect.getX() - SPEED), (int) currentRect.getY());
        }
        else if (!lastTop)
        {
            if (goingForward) currentRect.setLocation((int) currentRect.getX(), (int) (currentRect.getY() + SPEED));
            else if (!goingForward)
                currentRect.setLocation((int) currentRect.getX(), (int) (currentRect.getY() - SPEED));
        }
        g2.fill(currentRect);
        g2.setPaint(Color.BLACK);
        g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 32));
        int score = (rects.get(rects.size() - 1).getWidth() > 0 && rects.get(rects.size() - 1).getHeight() > 0) ? rects.size() - 1 : rects.size() - 2;
        g2.drawString(Integer.toString(score), 10, 40);
        sleepUntilNextFrame();
        repaint();
    }

    private void sleepUntilNextFrame()
    {
        nextGameTick += SKIP_TICKS;
        sleepTicks = nextGameTick - System.currentTimeMillis();
        if (sleepTicks >= 0)
        {
            try
            {
                Thread.sleep((long) sleepTicks);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case (KeyEvent.VK_ESCAPE):
                System.exit(0);
                break;
            case (KeyEvent.VK_SPACE):
                if (rects.get(rects.size() - 1).getWidth() > 0 && rects.get(rects.size() - 1).getHeight() > 0
                                && currentRect.getWidth() > 0 && currentRect.getHeight() > 0)
                {
                    lastTop = !lastTop;
                    Rectangle prevRect = rects.get(rects.size() - 1);
                    Rectangle thisRect = new Rectangle(currentRect);
                    // rects.add(new Rectangle(currentRect));
                    rectColors.add(currentColor);
                    currentColor = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
                    int width = 0;
                    int height = 0;
                    if (lastTop)
                    {
                        width = (int) ((thisRect.getX() > prevRect.getX())
                                        ? prevRect.getX() + prevRect.getWidth() - thisRect.getX()
                                        : thisRect.getX() + thisRect.getWidth() - prevRect.getX());
                        if (width > thisRect.getWidth()) width = (int) thisRect.getWidth();
                        height = (int) ((thisRect.getY() > prevRect.getY())
                                        ? prevRect.getY() + prevRect.getHeight() - thisRect.getY()
                                        : thisRect.getY() + thisRect.getHeight() - prevRect.getY());
                        if (height > thisRect.getHeight()) height = (int) thisRect.getHeight();
                        currentRect.setSize(width, height);
                        currentRect.setLocation((int) -currentRect.getWidth(),
                                        (int) ((thisRect.getY() > prevRect.getY()) ? thisRect.getY()
                                                        : prevRect.getY()));
                    }
                    else if (!lastTop)
                    {
                        width = (int) ((thisRect.getX() > prevRect.getX())
                                        ? prevRect.getX() + prevRect.getWidth() - thisRect.getX()
                                        : thisRect.getX() + thisRect.getWidth() - prevRect.getX());
                        if (width > thisRect.getWidth()) width = (int) thisRect.getWidth();
                        height = (int) ((thisRect.getY() > prevRect.getY())
                                        ? prevRect.getY() + prevRect.getHeight() - thisRect.getY()
                                        : thisRect.getY() + thisRect.getHeight() - prevRect.getY());
                        if (height > thisRect.getHeight()) height = (int) thisRect.getHeight();
                        currentRect.setSize(width, height);
                        currentRect.setLocation(
                                        (int) ((thisRect.getX() > prevRect.getX()) ? thisRect.getX() : prevRect.getX()),
                                        (int) -currentRect.getHeight());
                    }
                    int x = (int) ((thisRect.getX() < prevRect.getX()) ? prevRect.getX() : thisRect.getX());
                    int y = (int) ((thisRect.getY() < prevRect.getY()) ? prevRect.getY() : thisRect.getY());
                    rects.add(new Rectangle(x, y, width, height));
                    goingForward = true;
                }
                break;
            case KeyEvent.VK_R:
                restart();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent arg0)
    {
        // TODO Auto-generated method stub
    }

    public void restart()
    {
        rectColors = new ArrayList<Color>();
        rects = new ArrayList<Rectangle>();
        currentRect.setSize((int) (getWidth() * startSizeRatio.getX()), (int) (getHeight() * startSizeRatio.getY()));
        currentRect.setLocation((int) -currentRect.getWidth(), (int) (getHeight() / 2 - currentRect.getHeight() / 2));
        currentColor = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        requestFocus();
        blackBase = new Rectangle((int) (getWidth() / 2 - currentRect.getWidth() / 2),
                        (int) (getHeight() / 2 - currentRect.getHeight() / 2),
                        (int) (getWidth() * startSizeRatio.getX()), (int) (getHeight() * startSizeRatio.getY()));
        rects.add(new Rectangle(blackBase));
        rectColors.add(Color.BLACK);
    }
}
