import java.awt.*;
public class Square
{
    //instance variables for position and size
    private int x;
    private int y;
    private int xInc;
    private int xN;
    private int yN;
    private int yInc;
    private int size;
    private Color color;
    private boolean alive = true;

    //constructor to initialize instance variables
    public Square()
    {
        //x is 100 to 800
        x = (int)(Math.random()*701) + 100;
        //y is 100 to 400
        y = (int)(Math.random()*301) + 100;
        xN = (int)(Math.random()*2);
        if( xN == 0 )
        {
            xN = 1;
        }
        else
        {
            xN = -1;
        }
        yN = (int)(Math.random()*2);
        if( yN == 0 )
        {
            yN = 1;
        }
        else
        {
            yN = -1;
        }
        xInc = xN * ( (int)(Math.random()*3) + 1 );
        yInc = yN * ( (int)(Math.random()*3) + 1 );
        //size is 50 to 100
        size = (int)(Math.random()*51) + 50;
        color = new Color( (int)(Math.random()*128),(int)(Math.random()*128),255 );
    }

    public int getCenterX()
    {
        return x + size/2;
    }

    public int getCenterY()
    {
        return y + size/2;
    }

    public int getRadius()
    {
        return size/2;
    }

    public void bounceX()
    {
        xInc *= -1;
    }

    public void bounceY()
    {
        yInc *= -1;
    }

    public int getLeft()
    {
        return x;
    }

    public int getRight()
    {
        return x + size;
    }

    public int getTop()
    {
        return y;
    }

    public int getBottom()
    {
        return y + size;
    }

    public void die()
    {
        xInc = 0;
        yInc = 0;
        x = -100;
        y = -100;
        alive = false;
    }

    public boolean life()
    {
        return alive;
    }

    //move the square 
    public void move()
    {
        x += xInc;
        y += yInc;
        if( x <= 0 || x + size >= 1000 )
        {
            xInc *= -1;
        }
        if( y <= 0 || y + size >=600 )
        {
            yInc *= -1;
        }
    }

    public void draw( Graphics page )
    {
        page.setColor( color );
        page.fillOval( x, y, size, size );
    }
}