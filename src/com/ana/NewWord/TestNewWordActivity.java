package com.ana.NewWord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ana.core.NewWord;
import com.example.japaneseapp.R;
import com.example.japaneseapp.R.anim;
import com.example.japaneseapp.R.id;
import com.example.japaneseapp.R.layout;
import com.example.japaneseapp.R.menu;
import com.example.japaneseapp.R.raw;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class TestNewWordActivity extends Activity {
	private static int idUnit;
	private static ArrayList<NewWord> listWord;
	private static ArrayList<NewWord> listWordResult;
	private static ArrayList<Integer> listResult;
	private static Activity activity;
	private static ResultSlidingMenu menu;
	private static int languge;
@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_test_new_word);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		Intent intent = getIntent();
		idUnit = intent.getIntExtra("ID_UNIT", 0);
		languge = intent.getIntExtra("LANGUAGE", 0);
		listWord = new ArrayList<NewWord>();
		listResult = new ArrayList<Integer>();
		listWordResult = new ArrayList<NewWord>();
		activity = this;
		readData(idUnit);

		this.menu = new ResultSlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setBehindOffset(250);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_new_word, menu);
		return true;
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

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
		super.onPause();
	}

	public static class PlaceholderFragment extends Fragment {
		private TextView tvQuestion, tvResult;
		private int[] btnArr = { R.id.btnanswer1, R.id.btnanswer2,
				R.id.btnanswer3, R.id.btnanswer4 };

		private int rightAnswerPosition;
		private int positionQuestion;
		private Random r;
		private int size;
		private ProgressBar prgTime;
		private Handler mHandler = new Handler();
		private int mProgressStatus = 0;
		private Thread thrQuestionDisplay, thrTimeDisplay;
		private int numWrong, numRight;
		private Button btn, btnRight, btnFinish, btnReplay, btnShowResult, btnChange;
		private View backFlashCard, backNewWord;
		private View context;
		private boolean isRuning = true;

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_test_new_word,
					container, false);
			context = rootView;
			btnReplay = (Button) rootView.findViewById(R.id.btnReplay);
			btnShowResult = (Button) rootView.findViewById(R.id.btnShowResult);
			tvQuestion = (TextView) rootView.findViewById(R.id.tvquestion);
			tvResult = (TextView) rootView.findViewById(R.id.tvResult);
			prgTime = (ProgressBar) rootView.findViewById(R.id.prgTime);
			btnFinish = (Button) rootView.findViewById(R.id.btnFinishTest);

			btnFinish.setVisibility(View.VISIBLE);
			btnReplay.setVisibility(View.INVISIBLE);
			btnShowResult.setVisibility(View.INVISIBLE);

			
			prgTime.setMax(1000);
			numWrong = 0;
			numRight = 0;
			positionQuestion = 0;

			Collections.shuffle(listWord);
			size = listWord.size();
			thrQuestionDisplay = new Thread(new Runnable() {
				@Override
				public void run() {
					// Update the progress bar
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							displayQuestionAnswer(positionQuestion);
						}
					});
				}
			});

			thrTimeDisplay = new Thread(new Runnable() {
				@Override
				public void run() {
					thrQuestionDisplay.run();
					while (mProgressStatus <= 1000
							&& positionQuestion < size - 1 && isRuning) {
						mProgressStatus++;
						int i = 0;
						for (i = 0; i < 4; i++) {
							final int k = i;
							btn = (Button) context.findViewById(btnArr[i]);

							btn.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (k == rightAnswerPosition) {
										numRight++;
										listResult.add(1);
										listWordResult.add(listWord
												.get(positionQuestion));
									} else {
										listResult.add(2);
										listWordResult.add(listWord
												.get(positionQuestion));
										numWrong++;
									}
									mProgressStatus = 0;
									positionQuestion++;
									thrQuestionDisplay.run();
								}
							});
						}
						// Update the progress bar
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								prgTime.setProgress(mProgressStatus);
							}
						});
						try {
							Thread.sleep(5);
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (mProgressStatus == 1000) {
							mProgressStatus = 0;
							positionQuestion++;
							thrQuestionDisplay.run();
						}

					}
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							int result = (numRight * 100 / size);

							for (int i = 0; i < 4; i++) {
								Button b;
								b = (Button) context.findViewById(btnArr[i]);
								b.setOnClickListener(null);
							}
							tvResult.setText("Kết quả:" + numRight + "/"
									+ (numRight + numWrong));
							
							btnFinish.setVisibility(View.INVISIBLE);
							btnReplay.setVisibility(View.VISIBLE);
							btnShowResult.setVisibility(View.VISIBLE);
							menu.setShowResult(listWordResult, listResult);
							menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
						}
					});
				}
			});
			thrTimeDisplay.start();

			backNewWord = rootView.findViewById(R.id.backWord);
			backNewWord.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().finish();
//					Intent intent = new Intent(getActivity(),
//							WordActivity.class);
//					intent.putExtra("ID_UNIT", idUnit);
//
//					try {
//						
//						startActivity(intent);
//					} catch (Exception e) {
//						startActivity(intent);
//					}
				}
			});

			btnFinish.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isRuning = false;
					tvResult.setText("Kết quả:" + numRight + "/"
							+ (numRight + numWrong));
					btnFinish.setVisibility(View.INVISIBLE);
					btnChange.setVisibility(View.INVISIBLE);
					btnReplay.setVisibility(View.VISIBLE);
					btnShowResult.setVisibility(View.VISIBLE);
					menu.setShowResult(listWordResult, listResult);
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}
			});

			btnReplay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = getActivity().getIntent();
					getActivity().finish();
					startActivity(intent);

				}
			});

			btnShowResult.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					menu.showMenu();
				}
			});
			
			btnChange = (Button) rootView.findViewById(R.id.btnChangeLanguage);
			btnChange.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = getActivity().getIntent();
					if(languge == 0){
						intent.putExtra("LANGUAGE", 1);
					}else {
						intent.putExtra("LANGUAGE", 0);
					}
					getActivity().finish();
					startActivity(intent);
				}
			});
			return rootView;
		}

		private void displayQuestionAnswer(int positionQuestion) {
			r = new Random();
			rightAnswerPosition = r.nextInt(3);
			int i = positionQuestion;

			if (languge == 0) {
				tvQuestion.setText(listWord.get(i).getJpWord());
				btnRight = (Button) context
						.findViewById(btnArr[rightAnswerPosition]);
				btnRight.setText(listWord.get(i).getVnWord());
				for (int j = 0; j < rightAnswerPosition; j++) {
					int k = i;
					while (k == i) {
						k = r.nextInt(size);
						Button b;
						b = (Button) context.findViewById(btnArr[j]);
						b.setText(listWord.get(k).getVnWord());
					}
				}
				for (int j = rightAnswerPosition + 1; j < 4; j++) {
					int k = i;
					while (k == i) {
						k = r.nextInt(size);
						Button b;
						b = (Button) context.findViewById(btnArr[j]);
						b.setText(listWord.get(k).getVnWord());
					}
				}
			} else if (languge ==1 ){
				tvQuestion.setText(listWord.get(i).getVnWord());
				btnRight = (Button) context
						.findViewById(btnArr[rightAnswerPosition]);
				btnRight.setText(listWord.get(i).getJpWord());
				for (int j = 0; j < rightAnswerPosition; j++) {
					int k = i;
					while (k == i) {
						k = r.nextInt(size);
						Button b;
						b = (Button) context.findViewById(btnArr[j]);
						b.setText(listWord.get(k).getJpWord());
					}
				}
				for (int j = rightAnswerPosition + 1; j < 4; j++) {
					int k = i;
					while (k == i) {
						k = r.nextInt(size);
						Button b;
						b = (Button) context.findViewById(btnArr[j]);
						b.setText(listWord.get(k).getJpWord());
					}
				}
			}

		}

	}

	public void readData(int idUnit) {
		InputStream in = this.getResources().openRawResource(NameUnit[idUnit]);
		InputStreamReader inReader = new InputStreamReader(in);
		BufferedReader bufferedReader = new BufferedReader(inReader);
		String word, jpWord, vnWord, romajiWord;
		StringTokenizer stringTokenizer;
		while (true) {
			try {
				word = bufferedReader.readLine();
				if (word == null)
					break;
				stringTokenizer = new StringTokenizer(word, "/");
				jpWord = stringTokenizer.nextToken();
				romajiWord = stringTokenizer.nextToken();
				vnWord = stringTokenizer.nextToken();
				NewWord newWord = new NewWord(jpWord, vnWord, romajiWord);
				listWord.add(newWord);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int NameUnit[] = { R.raw.unit1, R.raw.unit2, R.raw.unit3,
			R.raw.unit4, R.raw.unit5, R.raw.unit6, R.raw.unit7,
			R.raw.unit8, R.raw.unit9, R.raw.unit10, R.raw.unit11,
			R.raw.unit12, R.raw.unit13, R.raw.unit14, R.raw.unit15,
			R.raw.unit16, R.raw.unit17, R.raw.unit18, R.raw.unit19,
			R.raw.unit20, R.raw.unit21, R.raw.unit22, R.raw.unit23,
			R.raw.unit24, R.raw.unit25 };

	public interface SetShowResult {
		public void setShowResult(ArrayList<NewWord> listword,
				ArrayList<Integer> listResult);
	}
}
