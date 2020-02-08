package util;

import java.io.Serializable;
import java.util.HashMap;

public class SadSong implements Serializable{
	public int mode;
	public float loudness;
	public float tempo;
	
	public SadSong() {
		
	}
	
	public SadSong(int mode, float loudness, float tempo) {
		this.mode = mode;
		this.loudness = loudness;
		this.tempo = tempo;
	}
	
	public static SadSong fromHashMap(HashMap<SongProperty, String> properties) {
		SadSong song = new SadSong();
		song.mode = Integer.parseInt(properties.get(SongProperty.MODE));
		song.loudness = Float.parseFloat(properties.get(SongProperty.LOUDNESS));
		song.tempo = Float.parseFloat(properties.get(SongProperty.TEMPO));
		return song;
	}
}
