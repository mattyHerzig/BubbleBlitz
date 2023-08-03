import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import java.io.*;
import java.awt.image.*;
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener
{
    //instance variable
    //create a reference for a Square
    private Square[] squares;
    private int count;
    private Player player;
    private Sound kill;
    private int timer;
    private JLabel timeTracker;
    private JLabel lifeTracker;
    private JLabel gunstatus;
    private JLabel ending;
    private JLabel remaining;
    private int shot;
    private int deadcount;
    private boolean killwin = false;
    private Image image;
    private JButton restart;
    private boolean shotonce = false;
    private int mX, mY;
    private boolean offscreen = false;
    //constructor
    public Board()
    {
        this.setLayout( null );
        this.setPreferredSize( new Dimension( 1000, 600 ) );
        try
        {
            this.getFileImage( "oceanfloor.jpg" );
        }catch( Exception e ){}

        this.setBackground( new Color( 135, 206, 235 ) );
        count = 10;
        squares = new Square[count];
        for( int i = 0; i < squares.length; i++ )
        {
            squares[i] = new Square();
        }
        //create a Player object
        player = new Player();

        kill = new Sound( "ABShot.wav" );

        //create a JLabel object
        timeTracker = new JLabel( "Timer: " + timer );
        this.add(timeTracker);
        timeTracker.setBounds(10,10,480,50);
        timeTracker.setFont(new Font("Serif",Font.BOLD,48));
        timeTracker.setForeground(Color.RED);
        timeTracker.setVisible(true);

        lifeTracker = new JLabel("Life: " + player.getLife());
        this.add(lifeTracker);
        lifeTracker.setBounds(510,10,480,50);
        lifeTracker.setFont(new Font("Serif",Font.BOLD,48));
        lifeTracker.setForeground(Color.WHITE);
        lifeTracker.setHorizontalAlignment(SwingConstants.RIGHT);

        gunstatus = new JLabel( "GUN: READY" );
        this.add(gunstatus);
        gunstatus.setBounds(10,550,480,50);
        gunstatus.setFont(new Font("Serif",Font.BOLD,34));
        gunstatus.setForeground(Color.GREEN);
        gunstatus.setHorizontalAlignment(SwingConstants.LEFT);

        remaining = new JLabel( "REMAINING: " + ( count - deadcount ) );
        this.add(remaining);
        remaining.setBounds(510,550,480,50);
        remaining.setFont(new Font("Serif",Font.BOLD,34));
        remaining.setForeground(Color.WHITE);
        remaining.setHorizontalAlignment(SwingConstants.RIGHT);

        ending = new JLabel();
        this.add(ending);
        ending.setBounds(0,250,1000,100);
        ending.setFont(new Font("Arial",Font.BOLD,100));
        ending.setForeground(Color.BLACK);
        ending.setHorizontalAlignment(SwingConstants.CENTER);
        ending.setVisible(false);

        restart = new JButton( "Go Again?" );
        this.add( restart );
        restart.setBounds( 400, 400, 200, 100 );
        restart.setVisible( false );

        //create a keyListener
        this.addKeyListener(this);
        restart.addActionListener( this );
        this.addMouseListener( this );
        this.addMouseMotionListener( this );
        this.setFocusable(true);
    }

    public void actionPerformed( ActionEvent event )
    {
        JButton temp = (JButton)event.getSource();
        if( temp == restart )
        {
            squares = new Square[count];
            for( int i = 0; i < squares.length; i++ )
            {
                squares[i] = new Square();
            }
            player = new Player();
            timer = 0;
            restart.setVisible( false );
            ending.setVisible( false );
            timeTracker.setForeground(Color.RED);
            remaining.setText( "REMAINING: " + count );
            lifeTracker.setText("Life: "+ player.getLife());
            killwin = false;
            shot = 0;
            player.resetbullet();
        }
    }

    private void getFileImage( String fileName ) throws InterruptedException, IOException
    {
        FileInputStream in = new FileInputStream( fileName );
        byte[] b = new byte[in.available()];
        in.read( b );
        in.close();
        image = Toolkit.getDefaultToolkit().createImage( b );
        MediaTracker mt = new MediaTracker( this );
        mt.addImage( image, 0 );
        mt.waitForAll();
    }

    public void go()
    {
        while( true )
        {
            try
            {
                Thread.sleep( 500 );
            }catch( InterruptedException ex ){}
            timer = 0;
            while( timer < 10000 && player.getLife() > 0 && killwin == false )
            {
                player.move();
                player.aim( mX, mY );
                if( shotonce )
                {
                    player.travel( timer );
                }
                //tells the square to move
                for( int i = 0; i < squares.length; i++ )
                {
                    squares[i].move();
                }
                for( int i = 0; i < squares.length; i++ )
                {
                    if( shot( squares[i].getCenterX(), squares[i].getCenterY(), player.getbX(), player.getbY(), squares[i].getRadius() ) )
                    {
                        squares[i].die();
                        kill.play();
                    }
                }
                for( int i = 0; i < squares.length; i++ )
                {
                    if( timer > 1000 && collide( squares[i] ) == true )
                    {
                        player.hit();
                        lifeTracker.setText("Life: "+player.getLife());
                    }
                }
                for( int i = 0; i < squares.length; i++ )
                {
                    if( squares[i].life() == false )
                    {
                        deadcount++;
                        remaining.setText( "REMAINING: " + ( count - deadcount ) );
                    }
                    if( deadcount == count )
                    {
                        killwin = true;
                    }
                }
                deadcount = 0;
                if( timer - shot >= 500 && offscreen == false )
                {
                    gunstatus.setForeground(Color.GREEN);
                    gunstatus.setText( "GUN: READY" );
                }
                try
                {
                    //causes the program to pause for 10 milliseconds
                    Thread.sleep( 5 );
                    timer += 5;
                    if( timer == 1000 )
                    {
                        timeTracker.setForeground(Color.WHITE);
                    }
                    timeTracker.setText( "Timer: " + timer/1000 );
                }catch( InterruptedException ex ){}
                //updates the screen
                this.repaint();
                this.requestFocusInWindow();
            }
            if( timer >= 10000 || killwin )
            {
                ending.setText( "YOU WON!" );
                player.invincible();
            }
            else
            {
                ending.setText( "YOU DIED!" );
            }
            ending.setVisible( true );
            restart.setVisible( true );
        }
    }

    public boolean collide( Square s )
    {
        int xDiff = s.getCenterX() - player.getCenterX();
        int yDiff = s.getCenterY() - player.getCenterY();
        //find the distance between the centers
        double dist = Math.sqrt(xDiff*xDiff+yDiff*yDiff);
        //check to see if touching
        if(dist<=s.getRadius()+player.getRadius())
        {
            return true;
        }
        return false;
    }

    public void mouseMoved( MouseEvent event )
    {
        mX = event.getX();
        mY = event.getY();
    }

    public void mouseDragged( MouseEvent event )
    {}

    //never use this one
    public void mouseClicked( MouseEvent event )
    {}

    public void mousePressed( MouseEvent event )
    {
        shotonce = true;
        if( timer - shot >= 500 )
        {
            int mouseX = event.getX();
            int mouseY = event.getY();
            player.shoot( mouseX, mouseY, timer );
            shot = timer;
            gunstatus.setForeground(Color.ORANGE);
            gunstatus.setText( "GUN: RELOADING" );
        } 
    }

    public void mouseReleased( MouseEvent event )
    {}

    public void mouseEntered( MouseEvent event )
    {
        gunstatus.setForeground(Color.GREEN);
        gunstatus.setText( "GUN: READY" );
        offscreen = false;
    }

    public void mouseExited( MouseEvent event )
    {
        gunstatus.setForeground(Color.RED);
        gunstatus.setText( "GUN: INACTIVE" );
        offscreen = true;
    }

    public void keyTyped( KeyEvent event )
    {}

    public void keyPressed( KeyEvent event )
    {
        if(event.getKeyCode()==KeyEvent.VK_D)
        {
            player.setRight(true);
            player.setLeft(false);
        }
        else if(event.getKeyCode()==KeyEvent.VK_A)
        {
            player.setLeft(true);
            player.setRight(false);
        }
        else if(event.getKeyCode()==KeyEvent.VK_W)
        {
            player.setUp(true);
            player.setDown(false);
        }
        else if(event.getKeyCode()==KeyEvent.VK_S)
        {
            player.setDown(true);
            player.setUp(false);
        }
    }

    public void keyReleased( KeyEvent event )
    {
        if(event.getKeyCode()==KeyEvent.VK_D)
        {
            player.setRight(false);
        }
        else if(event.getKeyCode()==KeyEvent.VK_A)
        {
            player.setLeft(false);
        }
        else if(event.getKeyCode()==KeyEvent.VK_W)
        {
            player.setUp(false);
        }
        else if(event.getKeyCode()==KeyEvent.VK_S)
        {
            player.setDown(false);
        }
    }

    //update the shapes on the Board
    public void paintComponent( Graphics page )
    {
        //clears the Board of previous shapes
        super.paintComponent( page );
        page.drawImage( image, 0, 0, this );
        //draws the square
        for( int i = 0; i < squares.length; i++ )
        {
            squares[i].draw( page );
        }
        player.draw( page );
    }

    public int collide( Square a, Square b )
    {
        if(a.getRight()>=b.getLeft()&&a.getLeft()<=b.getRight()
        &&a.getBottom()>=b.getTop()&&a.getTop()<=b.getBottom())
        {
            //a collision happened
            //find the smallest x difference
            int xDiff=Math.abs(a.getLeft()-b.getRight());
            if(Math.abs(a.getRight()-b.getLeft())<xDiff)
            {
                xDiff=Math.abs(a.getRight()-b.getLeft());
            }
            //find the smallest y difference
            int yDiff=Math.abs(a.getTop()-b.getBottom());
            if(Math.abs(a.getBottom()-b.getTop())<yDiff)
            {
                yDiff=Math.abs(a.getBottom()-b.getTop());
            }
            //determine direction of collision
            if( xDiff < yDiff )
            {
                return 1; //horizontal collision
            }
            return 2; //vertical collision
        }
        return 0;
    }

    public boolean shot( int x, int y, int bx, int by, int r )
    {
        if( Math.sqrt( ( x - bx ) * (x - bx ) + ( y - by ) * ( y - by ) ) <= r + 10 )
        {
            return true;
        }
        return false;
    }
}