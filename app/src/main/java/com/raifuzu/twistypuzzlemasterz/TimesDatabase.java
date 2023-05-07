package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TimesDatabase {

	private ArrayList<String> recorded2x2Times;
	private ArrayList<String> recorded3x3Times;
	private ArrayList<String> recorded4x4Times;
	private final int SECONDS_IN_MINUTE = 60;

	private static SQLiteDatabase mydatabase;

	private final String COLUMN_1 = "times";
	private final String COLUMN_2 = "cube_types";
	private final String COLUMN_3 = "scramble_algs";

	private final String DB_NAME = "raifuzu_database";
	private final String TABLE_NAME = "cube_times";
	
	
	private ArrayList<String> timesList;
	private ArrayList<String> scramblesList;
	
	
	@SuppressWarnings("static-access")
	public TimesDatabase(Activity activity) {
		recorded2x2Times = new ArrayList<String>();
		recorded3x3Times = new ArrayList<String>();
		recorded4x4Times = new ArrayList<String>();

		// Creates SQLite DB
		mydatabase = activity.openOrCreateDatabase(DB_NAME, activity.getBaseContext().MODE_PRIVATE, null);
		mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_1 + " VARCHAR, " + COLUMN_2
				+ " VARCHAR, " + COLUMN_3 + " VARCHAR );");
		
		timesList = new ArrayList<String>();
		scramblesList = new ArrayList<String>();
	}

	
	public void delete(String time, String alg){
		alg = alg.replace("'", "''");
		mydatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE "+ COLUMN_1 
				+" = '" + time+"' AND "+ COLUMN_3 +" = '"+ alg+ "';");
	}
	
	public void deleteAllTimes(CubeType currentCube) {
		mydatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE "+COLUMN_2+" = '" + currentCube.toString()+ "';");
	}

	public void insertData(String time, CubeType cubeType, String scrambleAlg) {

		// Dereferences the apostrophe in SQLite
		String validScramble = scrambleAlg.replace("'", "''");

		// Inserts data into the "Times" table in the DB
		mydatabase.execSQL("INSERT INTO " + TABLE_NAME + " VALUES( '" + time + "', '" + cubeType.toString() + "', '"
				+ validScramble + "');");

	}

	public String viewAllData() {
		String output = "";

		Cursor cursor = mydatabase.rawQuery("Select * from " + TABLE_NAME, null);

		// Reads all data from the DB
		try {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

				int timeIndex = cursor.getColumnIndex(COLUMN_1);
				String time = cursor.getString(timeIndex);

				int typeIndex = cursor.getColumnIndex(COLUMN_2);
				String type = cursor.getString(typeIndex);

				output = output + "\n";
				int algIndex = cursor.getColumnIndex(COLUMN_3);
				String alg = cursor.getString(algIndex);

				output = output + "\n" + time + " - " + type + "\n" + alg;

			}
		} finally {
			cursor.close();
		}
		
		return output;
	}

	// Example entry: time \n scramble_alg
	public void setSpecificData(String desiredType) {

		Cursor cursor = mydatabase.rawQuery("Select * from " + TABLE_NAME + 
				" where " + COLUMN_2  + " = " + "'" + desiredType + "'", null);

		// Reads all data from the DB with cube_types as desiredType
		try {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

				int timeIndex = cursor.getColumnIndex(COLUMN_1);
				String time = cursor.getString(timeIndex);
				timesList.add(time);

				int algIndex = cursor.getColumnIndex(COLUMN_3);
				String alg = cursor.getString(algIndex);
				scramblesList.add(alg);

			}
		} finally {
			cursor.close();
		}
	}
	
	public ArrayList<String> getTimesList(){
		return timesList;
	}
	
	
	public ArrayList<String> getScrambleList(){
		return scramblesList;
	}

	
	
	public void saveTime(String newTime, CubeType cube, String scrambleAlg) throws Exception {

		// Saves time to SQLite DB
		insertData(newTime, cube, scrambleAlg);

		switch (cube) {

		case STANDARD_2x2:
			recorded2x2Times.add(newTime);
			break;
		case STANDARD_3x3:
			recorded3x3Times.add(newTime);
			break;
		case STANDARD_4x4:
			recorded4x4Times.add(newTime);
			break;

		default:
			throw (new Exception());
		}

	}

	public ArrayList<String> getAllTimes(CubeType cube) {
		ArrayList<String> result = new ArrayList<String>();

		switch (cube) {

		case STANDARD_2x2:
			result = recorded2x2Times;
			break;
		case STANDARD_3x3:
			result = recorded3x3Times;
			break;
		case STANDARD_4x4:
			result = recorded4x4Times;
			break;

		default:
			break;
		}

		return result;
	}

	// TODO - should read from times stored in the DB
	public String getAverageTime(CubeType cube) {
		String average = "";
		switch (cube) {

		case STANDARD_2x2:
			average = calculateAverage(recorded2x2Times);
			break;
		case STANDARD_3x3:
			average = calculateAverage(recorded3x3Times);
			break;
		case STANDARD_4x4:
			average = calculateAverage(recorded4x4Times);
			break;

		default:
			break;
		}
		return average;
	}

	// TODO - should read from times stored in the DB
	public String getBestTime(CubeType cube) {
		String bestTime = "";
		switch (cube) {

		case STANDARD_2x2:
			bestTime = findBestTime(recorded2x2Times);
			break;
		case STANDARD_3x3:
			bestTime = findBestTime(recorded3x3Times);
			break;
		case STANDARD_4x4:
			bestTime = findBestTime(recorded4x4Times);
			break;

		default:
			break;
		}
		return bestTime;
	}

	// TODO - should read from times stored in the DB
	public String getWorstTime(CubeType cube) {
		String worstTime = "";
		switch (cube) {

		case STANDARD_2x2:
			worstTime = findWorstTime(recorded2x2Times);
			break;
		case STANDARD_3x3:
			worstTime = findWorstTime(recorded3x3Times);
			break;
		case STANDARD_4x4:
			worstTime = findWorstTime(recorded4x4Times);
			break;

		default:
			break;
		}
		return worstTime;
	}

	private String calculateAverage(ArrayList<String> allTimes) {

		// TODO - Compare average values with these sites
		// http://www.grun1.com/utils/timeCalc.html
		// http://www.csgnetwork.com/timescalc.html

		// TODO - How to calculate average
		// http://mathforum.org/library/drmath/view/60897.html

		if (allTimes.isEmpty()) {
			return "N/A";
		}

		ArrayList<ArrayList<Double>> allTimesAsDoubles = toDoubleTimes(allTimes);

		double minutesSum = 0;
		double secondsSum = 0;

		for (int i = 1; i < allTimesAsDoubles.size(); i++) {
			double currentTimesMinutes = allTimesAsDoubles.get(i).get(0);
			double currentTimesSeconds = allTimesAsDoubles.get(i).get(1);

			minutesSum = minutesSum + currentTimesMinutes;
			secondsSum = secondsSum + (currentTimesSeconds / SECONDS_IN_MINUTE);
		}

		int totalTimes = allTimesAsDoubles.size();
		double result = (minutesSum + secondsSum) / totalTimes;
		int averageMinutes = (int) Math.floor(result);
		double decimalFromResult = getDecimalOfNumber(result);
		double averageSeconds = decimalFromResult * SECONDS_IN_MINUTE;

		return "" + averageMinutes + " : " + averageSeconds;

	}

	private double getDecimalOfNumber(double num) {

		String stringValue = Double.toString(num);
		String[] numAndDecimal = stringValue.split("\\.");
		String decimal = "0." + numAndDecimal[1];
		double decimalDouble = Double.parseDouble(decimal);

		return decimalDouble;

	}

	/**
	 * Finds the actual smallest time.
	 * 
	 * @param allTimes
	 * @return
	 */
	private String findBestTime(ArrayList<String> allTimes) {

		if (allTimes.isEmpty()) {
			return "N/A";
		}

		ArrayList<ArrayList<Double>> allTimesAsDoubles = toDoubleTimes(allTimes);

		// Set first time as best
		ArrayList<Double> bestTime = allTimesAsDoubles.get(0);

		// Compare with all other times
		for (int i = 1; i < allTimesAsDoubles.size(); i++) {
			double currentTimesMinutes = allTimesAsDoubles.get(i).get(0);
			double currentTimesSeconds = allTimesAsDoubles.get(i).get(1);
			double bestTimesMinutes = bestTime.get(0);
			double bestTimesSeconds = bestTime.get(1);

			if (currentTimesMinutes == bestTimesMinutes) {
				// Check Seconds
				if (currentTimesSeconds < bestTimesSeconds) {
					bestTime = allTimesAsDoubles.get(i);
				}
			}
			if (currentTimesMinutes < bestTimesMinutes) {
				// Found new worst time
				bestTime = allTimesAsDoubles.get(i);
			}
		}

		String output = "" + bestTime.get(0).intValue() + ":" + bestTime.get(1);
		return output;

	}

	/**
	 * Convert String times to Double times
	 * 
	 * @param allTimes
	 * @return
	 */
	private ArrayList<ArrayList<Double>> toDoubleTimes(ArrayList<String> allTimes) {
		ArrayList<ArrayList<Double>> allTimesActualValues = new ArrayList<ArrayList<Double>>();

		for (String currentTime : allTimes) {

			String[] minutesAndSeconds = currentTime.split(":");
			ArrayList<Double> actualValueOfTime = new ArrayList<Double>();

			for (String timeComponent : minutesAndSeconds) {
				Double value = Double.parseDouble(timeComponent);
				actualValueOfTime.add(value);
			}
			allTimesActualValues.add(actualValueOfTime);
		}

		return allTimesActualValues;
	}

	/**
	 * Find actual largest time.
	 * 
	 * @param allTimes
	 * @return
	 */
	private String findWorstTime(ArrayList<String> allTimes) {

		if (allTimes.isEmpty()) {
			return "N/A";
		}

		ArrayList<ArrayList<Double>> allTimesAsDoubles = toDoubleTimes(allTimes);

		// Set first time as worst
		ArrayList<Double> worstTime = allTimesAsDoubles.get(0);

		// Compare with all other times
		for (int i = 1; i < allTimesAsDoubles.size(); i++) {
			double currentTimesMinutes = allTimesAsDoubles.get(i).get(0);
			double currentTimesSeconds = allTimesAsDoubles.get(i).get(1);
			double worstTimesMinutes = worstTime.get(0);
			double worstTimesSeconds = worstTime.get(1);

			if (currentTimesMinutes == worstTimesMinutes) {
				// Check seconds
				if (currentTimesSeconds > worstTimesSeconds) {
					worstTime = allTimesAsDoubles.get(i);
				}
			}
			if (currentTimesMinutes > worstTimesMinutes) {
				// Found new worst time
				worstTime = allTimesAsDoubles.get(i);
			}
		}

		String output = "" + worstTime.get(0).intValue() + ":" + worstTime.get(1);
		return output;
	}

}
