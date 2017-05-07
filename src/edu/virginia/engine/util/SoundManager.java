package edu.virginia.engine.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
	
	private HashMap<String, String> soundList;
	private HashMap<String, String> musicList;
	
	public SoundManager() {
		soundList = new HashMap<String, String>();
		musicList = new HashMap<String, String>();
	}
	
	public void LoadSoundEffect(String id, String filename) {
		soundList.put(id, filename);
	}
	
	//sound effects are short and are removed once complete
	public void PlaySoundEffect(String id) throws IOException, UnsupportedAudioFileException { 
		if (soundList.containsKey(id)) {
			try {
				Clip myClip = AudioSystem.getClip();
				if (!myClip.isRunning()) {
					if (myClip.isOpen()) {
						myClip.stop();
						myClip.close();
					}
					myClip.open(AudioSystem.getAudioInputStream(new File(soundList.get(id)).getAbsoluteFile()));
					myClip.start();
				}
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void LoadMusic(String id, String filename) {
		musicList.put(id, filename);
	}
	
	public void PlayMusic(String id) throws IOException, UnsupportedAudioFileException {
		if (musicList.containsKey(id)) {
			try {
				Clip myClip = AudioSystem.getClip();
				if (!myClip.isRunning()) {
					if (myClip.isOpen()) {
						myClip.close();
					}
					myClip.open(AudioSystem.getAudioInputStream(new File(musicList.get(id)).getAbsoluteFile()));
					myClip.start();
				}
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
