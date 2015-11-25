package com.ana.NewWord;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ana.Character.CharacterActivity;
import com.ana.NewWord.UnitSlideMenu.UnitChangeListener;
import com.example.japaneseapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class WordActivity extends Activity implements UnitChangeListener {
	private ListView lvlistWord;
	private ListWordAdapter listWordAdapter;
	private int idUnit;
	private UnitSlideMenu menu;
	private View backCharacter, toUnit, clickToTest;
	private View btnFlashCard;
	private boolean isScroll = false;
	private boolean flag = false;
	private SpeakNewWord speakNewWord;
	private TextView tv_titleUnit;
	Context _context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rightside_word_pane);
		this.menu = new UnitSlideMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setBehindOffset(250);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		_context = this.getApplicationContext();
		
		
		speakNewWord = new SpeakNewWord(WordActivity.this, getApplicationContext());
		
		lvlistWord = (ListView) findViewById(R.id.listWordEachUnit);
		idUnit = 0;
		tv_titleUnit = (TextView) findViewById(R.id.titleUnit);
		tv_titleUnit.setText("Bài học " + (idUnit+ 1));

		listWordAdapter = new ListWordAdapter(WordActivity.this,
				R.layout.item_newword, NameUnit[idUnit]);
		lvlistWord.setAdapter(listWordAdapter);

		lvlistWord.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				speakNewWord.speakWord(listWordAdapter.getWord(position));
			}
		});
		
		
		
		clickToTest = findViewById(R.id.clickToTestNewWord);
		clickToTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				flag = true;
				Intent intent = new Intent(WordActivity.this,
						TestNewWordActivity.class);
				intent.putExtra("ID_UNIT", idUnit);
				
				startActivity(intent);
			}
		});
		

		toUnit = findViewById(R.id.toUnit);
		toUnit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menu.showMenu();
			}
		});

		backCharacter = findViewById(R.id.backCharacter);
		backCharacter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnFlashCard = (View) findViewById(R.id.btnFlashCardShow);
		btnFlashCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {// TODO Auto-generated method stub
				flag = true;
				Intent intent = new Intent(WordActivity.this,
						FlashCardNewWordActivity.class);
				startActivity(intent);
			}
		});
		
		lvlistWord.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				new  Thread(new Runnable() {
					
					@Override
					public void run() {
						isScroll = true;
						try {
							
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						runOnUiThread( new Runnable() {
							public void run() {
								btnFlashCard.setVisibility(View.INVISIBLE);
								
							}
						});
					}
				}).start();
				
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
//					
					int visibleItemCount, int totalItemCount) {
				
				if (!isScroll) {
					btnFlashCard.setVisibility(View.INVISIBLE);
				}else {
					btnFlashCard.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		
	}
	
	
@Override
protected void onResume() {
	flag = false;
	super.onResume();
}

	@Override
	protected void onPause() {
		if (flag)
			overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
			else
		overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
		

		super.onPause();
	};

	public int NameUnit[] = { R.raw.unit1, R.raw.unit2, R.raw.unit3,
			R.raw.unit4, R.raw.unit5, R.raw.unit6, R.raw.unit7,
			R.raw.unit8, R.raw.unit9, R.raw.unit10, R.raw.unit11,
			R.raw.unit12, R.raw.unit13, R.raw.unit14, R.raw.unit15,
			R.raw.unit16, R.raw.unit17, R.raw.unit18, R.raw.unit19,
			R.raw.unit20, R.raw.unit21, R.raw.unit22, R.raw.unit23,
			R.raw.unit24, R.raw.unit25 };

	public void setUnit(int idUnit) {
		this.idUnit = idUnit;
		tv_titleUnit.setText("Bài học " + (idUnit+ 1));
		lvlistWord = (ListView) findViewById(R.id.listWordEachUnit);
		listWordAdapter = new ListWordAdapter(WordActivity.this,
				R.layout.item_newword, NameUnit[idUnit]);
		lvlistWord.setAdapter(listWordAdapter);
	}

	private float x;
	float y;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			y = event.getX();
			float distance = x - y;
			if (Math.abs(distance) > 400 && y > x) {
				finish();
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onChangeUnit(int idUnit) {
		setUnit(idUnit);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		return this.onTouchEvent(ev);
	}
	
}
