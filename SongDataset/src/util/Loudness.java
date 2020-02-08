package util;

import java.io.Serializable;

public class Loudness implements Serializable{
	public float loudness;
	public int count;
	
	public Loudness(float loudness, int count) {
		this.loudness = loudness;
		this.count = count;
	}
	
	public float getAverageLoudness() {
		return this.loudness / this.count;
	}
}
