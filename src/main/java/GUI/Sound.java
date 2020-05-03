package GUI;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    public Clip clip;
    public Sound(String name) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File("./Data/Sounds/" + name + ".wav").getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
