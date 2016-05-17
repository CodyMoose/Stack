package stack;

import java.awt.Dimension;

public class DoubleDimension
{
    private double thisX = 0;
    private double thisY = 0;

    /**
     * Dimension of doubles, same as Java's built in Dimension, but that only allows ints in its dimensions
     * @param x ordinate argument
     * @param y abscissa argument
     */
    public DoubleDimension(double x, double y)
    {
        thisX = x;
        thisY = y;
    }
    public DoubleDimension(Dimension d){
        thisX = d.getWidth();
        thisY = d.getHeight();
    }

    public double getX()
    {
        return thisX;
    }

    public void setX(double x)
    {
        thisX = x;
    }

    public double getY()
    {
        return thisY;
    }

    public void setY(double y)
    {
        thisY = y;
    }
}
