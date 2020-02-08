package util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatasetProperty {
	
	public DatasetProperty() {
		
	}
	
	public static Set<SongProperty> getAllProperties() {
		Set<SongProperty> properties = new HashSet();
		for (SongProperty property : SongProperty.values()) {
			properties.add(property);
		}
		return properties;
	}
	
	public static Set<SongProperty> getLoudnessProperties() {
		return Stream.of(
				SongProperty.TRACK_ID,
				SongProperty.TITLE,
				SongProperty.LOUDNESS,
				SongProperty.YEAR
		).collect(Collectors.toSet());
	}
	
	public static Set<SongProperty> getSadSongProperties() {
		return Stream.of(
				SongProperty.LOUDNESS,
				SongProperty.MODE,
				SongProperty.TEMPO
		).collect(Collectors.toSet());
	}
	
	public static Set<SongProperty> getGenreCrossoverProperties() {
		return Stream.of(
				SongProperty.ARTIST_TERMS,
				SongProperty.ARTIST_TERMS_FREQ
		).collect(Collectors.toSet());
	}
}
