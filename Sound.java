import javax.swing.JApplet;
import java.applet.*;
public class Sound
{
    private AudioClip clip;
    
    public Sound( String file )
    {
        clip = Applet.newAudioClip(getClass().getResource(file));
    }
    
    public void play()
    {
        clip.play();
    }
    
    public void stop()
    {
        clip.stop();
    }
    
    public void loop()
    {
        clip.loop();
    }
}