import javax.swing.*;
public class Driver
{
    public static void main(String[] args)
    {
        //create a JFrame object
        JFrame frame = new JFrame( "BUBBLE BLITZ" );
        //enbale the red close button to work
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        //set the location of the frame
        frame.setLocation( 460, 240 );
        //make the frame visible
        frame.setVisible( true );
        
        //create a Board object
        Board b = new Board();
        //put the Board into the JFrame
        frame.getContentPane().add( b );
        //adjust the size of the frame to the size of the Board
        frame.pack();
        //initiate the animation
        b.go();
    }
}