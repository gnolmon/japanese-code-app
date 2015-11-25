package com.ana.NewWord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.ana.core.NewWord;
import com.example.japaneseapp.R;

public class FlashCardNewWordActivity extends Activity implements
		AnimationListener {
	private Animation animationToMiddle;
	private Animation animationFromMiddle;
	private View layoutFlashCard, backUnit;
	private ViewPager mPager;
	private ArrayList<NewWord> listWords;
	private ViewFlashCardAdapter adapterJapWord;
	private boolean isJapWord;
	private int postionWord, tempPosition;
	private int idUnit;
	private Button btnShuffle, btnPlay, btnFlip, btnSpeak;
	private Handler handler = new Handler();
	private Thread autoSlide;
	private Boolean isPlaying;

	private SpeakNewWord speakNewWord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_flash_card_new_word);
		postionWord = 0;
		isPlaying = false;
		animationToMiddle = AnimationUtils
				.loadAnimation(this, R.anim.to_middle);
		animationToMiddle.setAnimationListener(this);
		animationFromMiddle = AnimationUtils.loadAnimation(this,
				R.anim.from_middle);
		animationFromMiddle.setAnimationListener(this);
		layoutFlashCard = findViewById(R.id.spaceflashcard);
		Intent intent = getIntent();
		idUnit = intent.getIntExtra("ID_UNIT", 0);

		mPager = (ViewPager) findViewById(R.id.flashcardWord);
		listWords = new ArrayList<NewWord>();
		readData(idUnit);
		adapterJapWord = new ViewFlashCardAdapter(this, listWords);
		mPager.setAdapter(adapterJapWord);

		speakNewWord = new SpeakNewWord(FlashCardNewWordActivity.this, getApplicationContext());
		isJapWord = true;
		btnShuffle = (Button) findViewById(R.id.btnShufflecard);
		btnPlay = (Button) findViewById(R.id.btnPlaycard);
		btnShuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<NewWord> newlist = listWords;
				Collections.shuffle(newlist);
				ViewFlashCardAdapter newadapter = new ViewFlashCardAdapter(
						getBaseContext(), newlist);
				mPager.setAdapter(newadapter);
			}
		});
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isPlaying) {
					btnPlay.setBackgroundResource(R.drawable.pausebutton);
					postionWord = tempPosition;
					autoSlide = new Thread(new Runnable() {
						@Override
						public void run() {
							isPlaying = true;
							while (postionWord < listWords.size() - 1) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										mPager.setCurrentItem(postionWord, true);

									}
								});
								try {
									postionWord++;
									Thread.sleep(6000);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}

						}
					});
					autoSlide.start();
				} else {
					btnPlay.setBackgroundResource(R.drawable.playbutton);
					tempPosition = postionWord;
					postionWord = listWords.size();
					isPlaying = false;
				}

			}
		});

		backUnit = findViewById(R.id.backUnit);
		backUnit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnFlip = (Button) findViewById(R.id.btnFlipcard);
		btnFlip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutFlashCard.clearAnimation();
				layoutFlashCard.setAnimation(animationToMiddle);
				layoutFlashCard.startAnimation(animationToMiddle);

			}
		});
	
		btnSpeak = (Button) findViewById(R.id.btnSpeak);
		btnSpeak.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int i = mPager.getCurrentItem();
				speakNewWord.speakWord(listWords.get(i).getRomajiWord());
			}
		});
	
	}

	public void readData(int idUnit) {
		InputStream in = this.getResources().openRawResource(NameUnit[idUnit]);
		InputStreamReader inReader = new InputStreamReader(in);
		BufferedReader bufferedReader = new BufferedReader(inReader);
		String word, jpWord, vnWord,romajiWord;
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
				NewWord newWord = new NewWord(jpWord, vnWord,romajiWord);
				listWords.add(newWord);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			layoutFlashCard.clearAnimation();
			layoutFlashCard.setAnimation(animationToMiddle);
			layoutFlashCard.startAnimation(animationToMiddle);
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (animation == animationToMiddle) {
			layoutFlashCard.clearAnimation();
			layoutFlashCard.setAnimation(animationFromMiddle);
			layoutFlashCard.startAnimation(animationFromMiddle);
			if (isJapWord) {
				isJapWord = false;
				postionWord = mPager.getCurrentItem();
				adapterJapWord.setMeaning(isJapWord);
				mPager.setAdapter(adapterJapWord);
				mPager.setCurrentItem(postionWord);
			} else {
				isJapWord = true;
				postionWord = mPager.getCurrentItem();
				adapterJapWord.setMeaning(isJapWord);
				mPager.setAdapter(adapterJapWord);
				mPager.setCurrentItem(postionWord);
			}
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	public int NameUnit[] = { R.raw.unit1, R.raw.unit2, R.raw.unit3,
			R.raw.unit4, R.raw.unit5, R.raw.unit6, R.raw.unit7,
			R.raw.unit8, R.raw.unit9, R.raw.unit10, R.raw.unit11,
			R.raw.unit12, R.raw.unit13, R.raw.unit14, R.raw.unit15,
			R.raw.unit16, R.raw.unit17, R.raw.unit18, R.raw.unit19,
			R.raw.unit20, R.raw.unit21, R.raw.unit22, R.raw.unit23,
			R.raw.unit24, R.raw.unit25 };
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
		super.onPause();
	}


}
