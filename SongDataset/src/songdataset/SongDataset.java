package songdataset;
import scala.Tuple2;
import util.DatasetProperty;
import util.Genre;
import util.GenreDetector;
import util.InputParser;
import util.Loudness;
import util.SadSong;
import util.SongProperty;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.PairFunction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class SongDataset {
	private static final Pattern SPACE = Pattern.compile(" ");

	public static void task1(String dataFile) throws IOException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(dataFile + ".output", "UTF-8");
		InputParser inputParser = new InputParser();
		SparkConf sparkConf = new SparkConf().setAppName("SongDataset").setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		
		ctx.hadoopConfiguration().set("mapreduce.input.fileinputformat.input.dir.recursive","true");
		JavaRDD<String> lines = ctx.textFile(dataFile, 1);
		
		JavaPairRDD<String, Loudness> ones = lines.mapToPair((String s) -> new Tuple2<>(
				inputParser.getSongProperties(s, DatasetProperty.getLoudnessProperties()).get(SongProperty.YEAR),
				new Loudness(
						Float.parseFloat(inputParser.getSongProperties(s, DatasetProperty.getLoudnessProperties()).get(SongProperty.LOUDNESS)),
						1
				)
		));
		
		JavaPairRDD<String, Loudness> counts = ones.reduceByKey((Loudness p1, Loudness p2) -> new Loudness(p1.loudness + p2.loudness, p1.count + p2.count));
		counts = counts.sortByKey(true);
		List<Tuple2<String, Loudness>> yearToLoudness = counts.collect();

		for(Tuple2<?, ?> tuple: yearToLoudness) {
			if(Integer.parseInt((String)tuple._1()) != 0) {
				System.out.println(tuple._1() + " " + ((Loudness)tuple._2()).getAverageLoudness());
				writer.println((tuple._1() + " " +  ((Loudness)tuple._2()).getAverageLoudness()));
			}
		}
		
		writer.close();
		
		ctx.stop();
	}
	public static final int MINOR = 0;
	public static final int MAJOR = 1;
	
	public static void extractTempoAndLoudnessByMode(JavaRDD<SadSong> songs, String dataFile, int mode) throws FileNotFoundException, UnsupportedEncodingException {
		
		JavaRDD<SadSong> filteredKeySongs = songs.filter((SadSong s) -> s.mode == mode);
		JavaRDD<Float> filteredKeySongsTempo = filteredKeySongs.map((SadSong s) -> s.tempo);
		JavaRDD<Float> filteredKeySongsLoudness = filteredKeySongs.map((SadSong s) -> s.loudness);
		
		JavaRDD<Float> filteredKeySongsTempoSorted = filteredKeySongsTempo.sortBy(new Function<Float, Float>() {
			 @Override
			  public Float call(Float s) throws Exception {
			    return s;
			  }
		}, true, 1);
		
		JavaRDD<Float> filteredKeySongsLoudnessSorted = filteredKeySongsLoudness.sortBy(new Function<Float, Float>() {
			 @Override
			  public Float call(Float s) throws Exception {
			    return s;
			  }
		}, true, 1);

		List<Float> filteredKeySongsTempoSortedList = filteredKeySongsTempoSorted.collect();
		List<Float> filteredKeySongsLoudnessSortedList = filteredKeySongsLoudnessSorted.collect();

		PrintWriter writer = new PrintWriter(dataFile + "." + mode + ".tempo.output", "UTF-8");
		
		for(Float tempo: filteredKeySongsTempoSortedList) {
				writer.println(tempo);
		}
		
		writer.close();
		
		writer = new PrintWriter(dataFile + "." + mode +  ".loudness.output", "UTF-8");
		
		for(Float loudness: filteredKeySongsLoudnessSortedList) {
			writer.println(loudness);
		}

		writer.close();		
	}
	
	public static void task2(String dataFile) throws IOException, UnsupportedEncodingException {
		
		InputParser inputParser = new InputParser();
		SparkConf sparkConf = new SparkConf().setAppName("SongDataset").setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		
		ctx.hadoopConfiguration().set("mapreduce.input.fileinputformat.input.dir.recursive","true");
		JavaRDD<String> lines = ctx.textFile(dataFile, 1);
		
		JavaRDD<SadSong> songs = lines.map((String s) -> SadSong.fromHashMap(inputParser.getSongProperties(s, DatasetProperty.getSadSongProperties())));
		
		extractTempoAndLoudnessByMode(songs, dataFile, MINOR);
		extractTempoAndLoudnessByMode(songs, dataFile, MAJOR);

		ctx.stop();
	}
	
	public static void printMatrix(float matrix[][], String dataFile) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(dataFile + ".matrix.output", "UTF-8");
		int N = Genre.values().length;

		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				writer.printf("%f ", matrix[i][j]);
				System.out.printf("%f ", matrix[i][j]);
			}
			System.out.println();
			writer.println();
		}
		
		writer.close();
	}
	
	public static void task3(String dataFile) throws IOException, UnsupportedEncodingException {
		InputParser inputParser = new InputParser();
		SparkConf sparkConf = new SparkConf().setAppName("SongDataset").setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		
		ctx.hadoopConfiguration().set("mapreduce.input.fileinputformat.input.dir.recursive","true");

		JavaRDD<String> lines = ctx.textFile(dataFile, 1);

		JavaPairRDD<Integer, List<Float>> genreScores = lines.mapToPair(new PairFunction<String, Integer, List<Float>>() {

			@Override
			public Tuple2<Integer, List<Float>> call(String line) throws Exception {
				HashMap<SongProperty,String> properties = inputParser.getSongProperties(line, DatasetProperty.getGenreCrossoverProperties());
				String termsLine = properties.get(SongProperty.ARTIST_TERMS);
				String importancesLine = properties.get(SongProperty.ARTIST_TERMS_FREQ);
				
				String terms[] = termsLine.split(",");
				String imp[] = importancesLine.split(",");
				
				float importances[] = new float[imp.length];
				for(int i = 0; i < imp.length; i++) {
					try {
						importances[i] = Float.parseFloat(imp[i]);	
					} catch (Exception e) {
//						importances[i] = 0.0f;
//						terms[i] = " ";
					}	
				}

				GenreDetector genreDetector = new GenreDetector();
				HashMap<Integer, List<Float>> genre = genreDetector.detectGenre(terms, importances);
				Integer genreIndex = genre.keySet().iterator().next();
				return new Tuple2<Integer, List<Float>>(genreIndex, genre.get(genreIndex));
			}
		});
		
		List<Tuple2<Integer, List<Float>>> IndexToScores = genreScores.collect();
		int N = Genre.values().length;
		float matrix[][] = new float [N][N];
		int count[][] = new int[N][N];
		
	
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				count[i][j] = 0;	
			}
		}
		
		for(Tuple2<Integer, List<Float>> tuple: IndexToScores) {
			
			Integer genreIndex = tuple._1();
			List<Float> genresScores = tuple._2();
			
			for(int i = 0; i < N; i++) {
				matrix[genreIndex][i] += genresScores.get(i);
				count[genreIndex][i] += 1;
			}
		}
		for (int i = 0; i < N; i++) {
			System.out.println(count[i]);
		}
		for (int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(count[i][j] != 0) {
					matrix[i][j] = matrix[i][j] / count[i][j];	
				}
			}
		}
		
		printMatrix(matrix, dataFile);
		
		ctx.stop();	
	}
	
	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Usage: SongDataset <file or directory>");
			System.exit(1);
		}
		
		try {
			task3(args[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
