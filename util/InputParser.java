package util;

import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This class contains methods helpful for parsing and reading from text files
 * with songs.
 * 
 * This text files can be obtained from MSD stored on Amazon S3 at
 * http://tbmmsd.s3.amazonaws.com/ or by from converting the MSD HDF5 files
 * using the Python script "hdf5-to-text/convert_all.py".
 * 
 * @author calinburloiu
 */
public class InputParser {
	
	public static final int N_PROPERTIES = 54;
	
	/**
	 * Returns a map with the required properties extracted from a string
	 * song line.
	 * 
	 * If you want the request all properties pass null to reqProperties.
	 * However this is not recommended for performance reasons.
	 * 
	 * The values of the properties extracted are Strings. You have to make
	 * conversion to other types manually by consulting MSD documentation:
	 * http://labrosa.ee.columbia.edu/millionsong/pages/field-list
	 * 
	 * @param songLine a String representing a song taken from a text file
	 * @param reqProperties a set of properties to return of type SongProperty
	 * @return a Map of requested properties with String values
	 */
	public static HashMap<SongProperty,String> getSongProperties(
			String songLine,
			Set<SongProperty> reqProperties)
	{
		if (songLine.isEmpty())
			throw new InputParserException("songLine is empty");
		
		HashMap<SongProperty,String> res = new HashMap<SongProperty,String>(
				N_PROPERTIES);
		int songLineLen = songLine.length();
		String tok;
		int start = 0, end;
		int count = 0;
//		int nReqProperties = 
//				(reqProperties == null ? -1 : reqProperties.size());
		
		for (SongProperty property: util.SongProperty.values())
		{
			if (start > songLineLen)
				throw new InputParserException(
					"song line contains less properties than expected");
			
			end = songLine.indexOf('\t', start);
			
			if (end == -1)
				end = songLineLen;
			
			tok = songLine.substring(start, end);
			
			if (reqProperties == null || reqProperties.contains(property))
			{
				res.put(property, tok);
				count++;
				
//				if (count == nReqProperties)
//					break;
			}
			
			start = end + 1;
		}
		
		return res;
	}
	
	public static String[] stringToArrayString(String propertyString)
	{
		StringTokenizer st = new StringTokenizer(propertyString, ",");
		final int N_TOKENS = st.countTokens();
		String[] res = new String[N_TOKENS];
		
		for (int i=0; i<N_TOKENS; i++)
		{
			res[i] = st.nextToken();
		}
		
		return res;		
	}
	
	public static int[] stringToArrayInt(String propertyString)
	{
		StringTokenizer st = new StringTokenizer(propertyString, ",");
		final int N_TOKENS = st.countTokens();
		int[] res = new int[N_TOKENS];
		
		for (int i=0; i<N_TOKENS; i++)
		{
			res[i] = Integer.parseInt(st.nextToken());
		}
		
		return res;		
	}
	
	public static float[] stringToArrayFloat(String propertyString)
	{
		StringTokenizer st = new StringTokenizer(propertyString, ",");
		final int N_TOKENS = st.countTokens();
		float[] res = new float[N_TOKENS];
		
		for (int i=0; i<N_TOKENS; i++)
		{
			res[i] = Float.parseFloat(st.nextToken());
		}
		
		return res;		
	}
}