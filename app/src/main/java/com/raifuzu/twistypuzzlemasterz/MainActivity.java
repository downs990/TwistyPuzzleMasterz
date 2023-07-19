package com.raifuzu.twistypuzzlemasterz;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	static TimesDatabase timesDB;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Changes the color of the Action Bar
		ActionBar bar = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#03a9f4"));
		bar.setBackgroundDrawable(colorDrawable);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

		// For AppCompat use getSupportFragmentManager
		FragmentManager fragmentManager = getFragmentManager();

		switch (position) {

		case 0:
			fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
					.commit();
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case 1:
			// WARNING: IF YOU REMOVE THIS AND OPEN THE APP AND SELECT THE
			// RECORDED TIMES FRAGMENT THEN SELECT
			// THE ALGORITHMS FRAGMENT THEN GO BACK TO THE RECORDED TIMES
			// FRAGMENT THEN IT WILL ADD EXTRA ITEMS
			// TO THE LIST VIEW FOR SOME REASON.
			fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(0 + 1)).commit();
			///////////////////////////////////////////////////////////////

			fragmentManager.beginTransaction().replace(R.id.container, RecordedTimesFragment.newInstance(position + 1))
					.commit();
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case 3:

			fragmentManager.beginTransaction().replace(R.id.container, AlgorithmFragment.newInstance(position + 1))
					.commit();

			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case 5:
			fragmentManager.beginTransaction().replace(R.id.container, SolverFragment.newInstance(position + 1))
					.commit();

			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

			break;
		case 6:
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			Intent intent = new Intent(this, OpenCVActivity.class);
			startActivity(intent);
			break;


		default:
		break;
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 5:
			mTitle = getString(R.string.title_section5);
			break;
		case 6:
			mTitle = getString(R.string.title_section6);
			break;
		case 7:
			mTitle = getString(R.string.title_section7);
			break;
		case 8:
			mTitle = getString(R.string.title_section8);
			break;

		}
	}

	@SuppressWarnings("deprecation")
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends Fragment {

		private TextView scrambleView;
		// These notations were stripped from the cubetimer.com web site.
		private String notation2x2 = "U' F L' D F' U2 B R B' F2 R' R2 U L D' B2 D2 L2";
		private String[] movesFor2x2 = notation2x2.split(" ");
		// MovesFor3x3 also contains movesFor2x2
		// { "M", "M'", "M2", "E", "E'", "E2", "S", "S'", "S2" };
		private String[] movesFor3x3 = movesFor2x2;
		private String notation4x4 = "B' U Lw2 B2 Rw' D' Rw U' Uw Dw' R2 Bw' F L2 Lw' Fw' F' R Dw2 Fw"
				+ " F2 Lw Bw2 Fw2 U2 Uw2 D2 L' B L D Dw Rw2 Uw' Bw R'";
		private String[] movesFor4x4 = notation4x4.split(" ");
		private AdvancedArrayList<String> selectCubesSymbols = new AdvancedArrayList<String>(movesFor2x2);
		// Thread variables
		private TextView minutesView;
		private TextView secondsView;
		private TextView millisecondsView;
		private TextView allTimesView;

		private long recordedMinutes = 0;
		private long oldMinutes = 0;
		private long milliseconds = 0;
		private long seconds = 0;
		private long minutes = 0;
		private Thread thread;
		private boolean timerRunning = true;
		private boolean timer = true;
		private CubeType currentCubeType = CubeType.STANDARD_3x3;// Default is 3x3
		private Spinner cubesSpinner;
		private final int SCRAMBLE_SIZE = 26;// Must always be even

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);

			allTimesView = (TextView) rootView.findViewById(R.id.allTimes);

			// Sets the font family of the clock
			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "font/digital-7.ttf");
			minutesView = (TextView) rootView.findViewById(R.id.minutesTV);
			secondsView = (TextView) rootView.findViewById(R.id.secondsTV);
			millisecondsView = (TextView) rootView.findViewById(R.id.millisecondsTV);
			minutesView.setTypeface(tf);
			secondsView.setTypeface(tf);
			millisecondsView.setTypeface(tf);

			timesDB = new TimesDatabase(this.getActivity());

			// SCRAMBLE_SIZE needs to be even number
			String newScramble = generateScrambleAlgorithm(SCRAMBLE_SIZE);
			scrambleView = (TextView) rootView.findViewById(R.id.textView1);
			scrambleView.setText(newScramble);

			RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.timerActivity);
			layout.setOnTouchListener(new View.OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// If timer is not running and down press
						if (timer == true) {
							// Keep back light on
							getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
						} else {
							stopTimer();
						}

						return true;
					case MotionEvent.ACTION_UP:

						// If timer is not running and up press
						if (timer == true) {
							startTimer();
							timer = false;
						} else {
							// Release back light
							getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
							timer = true;
						}
						return true;
					}
					return false;
				}
			});
			cubesSpinner = (Spinner) rootView.findViewById(R.id.cubesSpinner);
			ArrayAdapter<CharSequence> cubeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
					R.array.cube_type_array, android.R.layout.simple_spinner_item);
			cubeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			cubesSpinner.setAdapter(cubeAdapter);
			cubesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
					// Get selected cube
					String selectedCube = parent.getSelectedItem().toString();
					currentCubeType = stringToCubeType(selectedCube);

					sendCubeTypeToDB(currentCubeType);

					// Update the set of notations
					setNotationForCube(selectedCube);
					String newScramble = generateScrambleAlgorithm(SCRAMBLE_SIZE);
					scrambleView.setText(newScramble);

					// Update recorded times at bottom of screen
					allTimesView.setText("Best: " + timesDB.getBestTime(currentCubeType) + "   " + "Worst: "
							+ timesDB.getWorstTime(currentCubeType) + "   " + "\nAvg 5: \nAvg 10:");

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

			return rootView;
		}

		private void sendCubeTypeToDB(CubeType cube) {
			SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("CUBE_KEY", cube.toString());// Key, Value
			editor.commit();

		}

		private void stopWatch() {

			// Thank you Internet. <3 
			final StopWatch timer = new StopWatch();

			thread = new Thread() {
				@Override
				public void run() {
					
					while (timerRunning) {
						milliseconds = timer.getElapsedTime().getElapsedRealtimeMillis();
						seconds = milliseconds / 1000;
						minutes = recordedMinutes + (seconds / 60);
						if (seconds == 60) {
							timer.reset();
							oldMinutes = 0;
							oldMinutes = minutes;
							recordedMinutes = oldMinutes;
						}
						
						// Can't use any spinner while time is running
						cubesSpinner.setClickable(false);
						try {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									String millisecondsString = Long.toString(milliseconds);
									String secondsString = Long.toString(seconds);
									String minutesString = Long.toString(minutes);

									// Conditions for updating UI
									if (seconds < 10) {
										secondsString = "0" + secondsString;
									}
									if (minutes < 10) {
										minutesString = "0" + minutesString;
									}
									if (milliseconds < 10) {
										millisecondsString = "00" + millisecondsString;
									} else if (milliseconds >= 10 && milliseconds < 100) {
										millisecondsString = "0" + millisecondsString;
									}

									int start = millisecondsString.length() - 3;
									int end = millisecondsString.length();
									millisecondsString = millisecondsString.substring(start, end);
									minutesView.setText(minutesString + ":");
									secondsView.setText(secondsString + ".");
									millisecondsView.setText(millisecondsString);
								}
							});
							Thread.sleep(1);// millisecond
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// Spinner are available again.
					cubesSpinner.setClickable(true);
				}
			};

		}

		public void setNotationForCube(String cube) {

			switch (cube) {

			case "2x2x2":
				selectCubesSymbols = new AdvancedArrayList<String>(movesFor2x2);
				break;
			case "3x3x3":
				selectCubesSymbols = new AdvancedArrayList<String>(movesFor3x3);
				;
				break;
			case "4x4x4":
				selectCubesSymbols = new AdvancedArrayList<String>(movesFor4x4);
				break;

			default:

				break;
			}

		}

		public void startTimer() {
			timerRunning = true;
			stopWatch();
			thread.start();
		}

		public void stopTimer() {
			timerRunning = false;
			String oldScrambleAlg = scrambleView.getText().toString();
			String newScramble = generateScrambleAlgorithm(SCRAMBLE_SIZE);
			scrambleView.setText(newScramble);

			// Save time
			try {
				String timeToSave = minutesView.getText().toString() + secondsView.getText().toString()
						+ millisecondsView.getText().toString();
				timesDB.saveTime(timeToSave, currentCubeType, oldScrambleAlg);

				allTimesView.setText("Best: " + timesDB.getBestTime(currentCubeType) + "   " + "Worst: "
						+ timesDB.getWorstTime(currentCubeType) + "   " + "\nAvg 5: \nAvg 10:");

				// TODO - displaying time on screen and recorded time should be
				// the same.

			} catch (Exception e) {
				String errorMessage = "Something went wrong when saving time. Are you using a valid ENUM?";
				Toast.makeText(this.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
			}
			
			oldMinutes = 0;
			recordedMinutes = 0;
		}

		// Size must always be even (26)
		public String generateScrambleAlgorithm(int size) {

			String output = "";
			for (int i = 0; i < size; i++) {

				String newSymbol = generateRandomSymbol();
				if (i == size - 1) {
					output += newSymbol;
				} else {
					output += newSymbol + " ";
				}
			}

			return improveScrambleAlg(output);
		}

		private String generateRandomSymbol() {
			String notations = selectCubesSymbols.toString();
			String[] moves = notations.split(" ");

			int min = 0;
			int max = moves.length - 1;

			// Generates random numbers between minutes and max,
			// inclusive on both ends
			int randomMove = min + (int) (Math.random() * (max - (min - 1)));
			return moves[randomMove];
		}

		// remove all occurrences of exactly 2 identical symbols that are
		// adjacent.
		private String improveScrambleAlg(String initialAlg) {
			String[] movesFor3x3 = initialAlg.split(" ");
			AdvancedArrayList<String> cubeSymbols = new AdvancedArrayList<String>(movesFor3x3);

			for (int i = 0; i < cubeSymbols.size() - 2; i += 2) {
				String symbol1 = cubeSymbols.get(i);
				String symbol2 = cubeSymbols.get(i + 1);
				String symbol3 = cubeSymbols.get(i + 2);

				// CharAt(0) Gets the letter value of the move
				// Checks the following: 0 (1 2) (3 4) (5 6) (7 8) 9
				// and
				// Checks the following: (0 1) (2 3) (4 5) (6 7) (8 9)
				while (symbol1.charAt(0) == symbol2.charAt(0) || symbol2.charAt(0) == symbol3.charAt(0)) {
					symbol2 = generateRandomSymbol();
					cubeSymbols.remove(i + 1);
					cubeSymbols.add(i + 1, symbol2);
				}

				// Checks the last two moves of the algorithm
				while (cubeSymbols.get(cubeSymbols.size() - 1).charAt(0) == (cubeSymbols.get(cubeSymbols.size() - 2))
						.charAt(0)) {
					String newLastSymbol = generateRandomSymbol();
					cubeSymbols.add(cubeSymbols.size() - 1, newLastSymbol);
				}
			}

			return cubeSymbols.toString();
		}

		private CubeType stringToCubeType(String cube) {
			CubeType result = null;

			switch (cube) {
			case "2x2x2":
				result = CubeType.STANDARD_2x2;
				break;
			case "3x3x3":
				result = CubeType.STANDARD_3x3;
				break;
			case "4x4x4":
				result = CubeType.STANDARD_4x4;
				break;

			default:
				break;
			}

			return result;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}

	}

}
