package stack;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Stack extends JFrame implements KeyListener
{
    /**
     * 
     */
    private static final long serialVersionUID = 5081524895930036611L;

    public static void main(String[] args)
    {
        new Stack();
    }

    public Stack()
    {
        setTitle("Stack");
//        addKeyListener(this);
        setBackground(Color.WHITE);
        setSize(450, 450);
        setVisible(true);
        setLocation(0, 0);
        repaint();
        StackPanel stackerPanel = new StackPanel(this);
        add(stackerPanel);
//        stackerPanel.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case (KeyEvent.VK_ESCAPE):
                System.exit(0);
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
}
