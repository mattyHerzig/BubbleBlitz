import java.awt.*;
import java.lang.Math;
public class Player
{
    private int x, y, size;
    private boolean left, right, up, down;
    private int life;
    private int xD, yD;
    private int Ox;
    private int Oy;
    private int dx;
    private int dy;
    private double angle;
    private int bRun, bRise, shot, iX, iY; 
    private int bX = -100;
    private int bY = -100;
    private int lifeInc = 1;
    
    public Player()
    {
        size = 100;
        x = 500 - size / 2;
        y = 300 - size / 2;
        life = 100;
        up = false;
        down = false;
        left = false;
        right = false;
    }
    
    public void hit()
    {
        life-=lifeInc;
    }
    
    public void invincible()
    {
        lifeInc = 0;
    }
    
    public int getLife()
    {
        if( life >= 0 )
        {
            return life;
        }
        return 0;
    }
    
    public void move()
    {
        if( right == true && x+size < 1000 )
        {
            x+=2;
        }
        else if( left == true && x > 0 )
        {
            x-=2;
        }
        if( up == true && y > 0 )
        {
            y-=2;
        }
        else if( down == true && y+size < 600 )
        {
            y+=2;
        }
    }
    
    public void setRight( boolean t )
    {
        right = t;
    }
    
    public void setLeft( boolean t )
    {
        left = t;
    }
    
    public void setUp( boolean t )
    {
        up = t;
    }
    
    public void setDown( boolean t )
    {
        down = t;
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
    
    public double getAngle( int mX, int mY)
    {
        xD = mX - ( x + size / 2 );
        yD = mY - ( y + size / 2 );
        if( (xD > 0 && yD > 0) || ( xD > 0 && yD < 0 ) )
        {
            angle = Math.atan( (double)yD / xD );
        }
        if( xD < 0 && yD > 0 )
        {
            angle = Math.atan( (double)yD / xD ) + Math.PI;
        }
        if( xD < 0 && yD < 0 )
        {
            angle = Math.atan( (double)yD / xD ) - Math.PI;
        }
        return angle;
    }
    
    
    public void aim( int mX, int mY )
    {
        Ox = (int)((size/2) * Math.cos(getAngle( mX, mY ))) - 25;
        Oy = (int)((size/2) * Math.sin(getAngle( mX, mY ))) - 25;
        dx = mX - 5;
        dy = mY - 5;
    }
    
    public void shoot( int mX, int mY, int timer )
    {
        shot = timer;
        iX = x;
        iY = y;
        bRun = (int)(9 * Math.cos(getAngle( mX, mY )));
        bRise = (int)(9 * Math.sin(getAngle( mX, mY )));
    }
    
    public void travel( int timer )
    {
        bX = (int)(( size / 2 + 10 ) * Math.cos(angle)) + ( iX + size / 2 ) + bRun * ( (timer - shot) / 5 ) - 10;
        bY = (int)(( size / 2 + 10 ) * Math.sin(angle)) + ( iY + size / 2 ) + bRise * ( (timer - shot) / 5 ) - 10;
    }
    
    public int getbX()
    {
        return bX+10;
    }
    
    public int getbY()
    {
        return bY+10;
    }
    
    public void resetbullet()
    {
        iX = -100;
        iY = -100;
    }
    
    public void draw( Graphics page )
    {
        page.setColor( Color.RED );
        page.fillOval( bX, bY, 20, 20 );
        page.setColor( Color.ORANGE );
        page.fillOval( x, y, size, size );
        page.fillOval( ( x + size / 2 ) + Ox, ( y + size / 2 ) + Oy, 50, 50 );
        page.setColor( Color.GREEN );
        page.fillOval( dx, dy, 10, 10 );
        page.setColor( Color.RED );
        page.fillRoundRect( 800, 10, (int)( 1.95 * life ), 50, 10, 10 );
    }
}