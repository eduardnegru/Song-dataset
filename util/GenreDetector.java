package util;

/**
 * Detects the musical genre of a song, based on its ARTIST_TERMS and their
 * ARTIST_TERMS_FREQ.
 * 
 * @author calinburloiu
 */
public class GenreDetector {
	
	protected String[][] genreKeywords;
	
	protected final int GENRES_COUNT = Genre.values().length;
	
	public GenreDetector()
	{
		genreKeywords = new String[GENRES_COUNT][];
		
		populateGenreKeywords();
	}

	/**
	 * Returns the music genre that best matches for a song.
	 * 
	 * @param terms an array containing the ARTIST_TERMS for the song
	 * @param importances an array containing the ARTIST_TERMS_FREQ for the 
	 * song
	 * @return the musical genre that best matches for the song
	 */
	public Genre detectGenre(String[] terms, float[] importances)
	{
		float maxImportance = 0.0f;
		Genre maxGenre = Genre.OTHER;
		float[] genreImportances = new float[GENRES_COUNT];
		
		for (int i=0; i<GENRES_COUNT; i++)
		{
			genreImportances[i] = 0.0f;
		}
		
		for (int i=0; i<terms.length; i++)
		{
			updateFromTerm(genreImportances, terms[i], importances[i]);
		}
		
		// Choose the genre as the one with the maximum accumulated importance.
		for (Genre genre: Genre.values())
		{
			float crtImportance = genreImportances[genre.ordinal()];
			if (crtImportance > maxImportance)
			{
				maxImportance = crtImportance;
				maxGenre = genre;
			}
		}
		
		return maxGenre;
	}
	
	private void populateGenreKeywords()
	{
		/*
		 * WRITE HERE KEYWORDS FOR EACH GENRE.
		 */
		genreKeywords[Genre.ROCK.ordinal()] = new String[] {
			"rock", "punk", "indie", "new wave", "metal", "hardcore", 
			"grunge", "rockabilly", "grind", "gothic", "emo", "screamo", 
			"dark wave", "darkwave", "beat", "psychobilly", "garage", "glam", 
			"retro", "straight edge", "no wave", "sludge", "jrock", "aor", 
			"thrash", "jive", "riot grrrl", "power violence", "powerviolence", 
			"nwobhm"
		};
		genreKeywords[Genre.POP.ordinal()] = new String[] {
			"pop", "twee", "disco", "crooner", "cpop"
		};
		genreKeywords[Genre.HIP_HOP.ordinal()] = new String[] {
			"hip hop", "rap", "grime", "gangsta", "crunk", "g funk", 
			"hyphy", "bounce"
		};
		genreKeywords[Genre.EDM.ordinal()] = new String[] {
			"electro", "house", "techno", "trance", "breakbeat", 
			"drum and bass", "jungle", "freestyle", "gabba", "hardstyle", 
			"new beat", "tribal", "eurodance", "dubstep", "grime", 
			"electroclash", "rave", "goa", "speed core", "eurobeat", 
			"bubblegum", "ghettotech", "ghetto tech", "schranz"
		};
		genreKeywords[Genre.JAZZ.ordinal()] = new String[] {
			"jazz", "swing", "bossa nova", "hard bop", "bebop", "ragtime", 
			"cabaret", "honky tonk", "post-bop", "dixieland", "skiffle", 
			"afro-cuban"
		};
		genreKeywords[Genre.FOLK.ordinal()] = new String[] {
			"folk"
		};
		genreKeywords[Genre.BLUES.ordinal()] = new String[] {
			"blues", "gospel", "delta blues"
		};
		genreKeywords[Genre.FUNK.ordinal()] = new String[] {
			"funk"
		};
		genreKeywords[Genre.SOUL.ordinal()] = new String[] {
			"soul"
		};
		genreKeywords[Genre.AMBIENT.ordinal()] = new String[] {
			"ambient", "chill-out", "chill out", "trip hop", "lounge", 
			"new age", "relax", "illbient", "meditation", "drone", 
			"calming", "relaxation"
		};
		genreKeywords[Genre.REGGAE.ordinal()] = new String[] {
			"reggae", "dub", "ska", "rocksteady", "rock steady", "rasta",
			"ragamuffin", "ragga"
		};
		genreKeywords[Genre.WORLD.ordinal()] = new String[] {
			"world", "country", "bluegrass", "celtic", "flamenco", 
			"traditional", "calypso", "ethnic", "bolero", "african", 
			"soca", "exotica", "zouk", "zydeco", "klezmer", "polka", 
			"arabic", "vallenato", "balkan", "newgrass", "fado", "greek", 
			"turkish", "ethno", "soukous", "oriental", "laika", "bouzouki", 
			"rebetika"
		};
		genreKeywords[Genre.LATIN.ordinal()] = new String[] {
			"latin", "samba", "salsa", "mambo", "tango", "rumba", "latino", 
			"reggaeton", "bolero", "flamenco", "tropical", "merengue", 
			"mariachi", "ranchera", "tejano", "boogaloo", "batucada", 
			"banda", "bachata", "lambada", "charanga", "tex-mex", "latina"
		};
		genreKeywords[Genre.CLASSICAL.ordinal()] = new String[] {
			"classical", "symphonic", "opera", "baroque", "symphony"
		};
		genreKeywords[Genre.R_B.ordinal()] = new String[] {
			"r&b", "doo-wop"
		};
		
		genreKeywords[Genre.OTHER.ordinal()] = new String[0];
	}
	
	/**
	 * Update genreImportances for each musical genre where its keywords are 
	 * found in the term.
	 * 
	 * For each keyword matched importance is added to the genreImportances for
	 * the corresponding genre.
	 * 
	 * @param term an item from ARTIST_TEMRS MSD property
	 * @param importance the value of an item from ARTIST_TERMS_FREQ property
	 * for the corresponding term
	 */
	protected void updateFromTerm(float[] genreImportances, String term, float importance)
	{
		String keyword;
		
		for (Genre genre: Genre.values())
		{
			for (int i=0; i < genreKeywords[genre.ordinal()].length; i++)
			{
				keyword = genreKeywords[genre.ordinal()][i];
				if (term.contains(keyword))
					genreImportances[genre.ordinal()] += importance;
			}
		}
	}
}
