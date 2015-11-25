package com.ana.Character;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.example.japaneseapp.R;

public class SlideCharacterActivity extends Activity implements OnClickListener{
	private ViewPager mPager;
	private SlideCharacterAdapter adapter;
	private Button btnback, btnNext;
	private int position;
	private View rlCharacter;
	private String character;
	private PronunciationCharacter pronunciationCharacter;
	private boolean isHiragana;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams wp = getWindow().getAttributes();
		wp.dimAmount = 0.9f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		setContentView(R.layout.fragment_view_slide_character);
		mPager = (ViewPager) findViewById(R.id.flashcharacter);
		Intent intent = getIntent();

		isHiragana = intent.getBooleanExtra("ISHIRAGANA",true);
		adapter = new SlideCharacterAdapter(SlideCharacterActivity.this,this, isHiragana );
		mPager.setAdapter(adapter);
		
		character = intent.getStringExtra("CHARACTER");
		position = adapter.getPositionChar(character);		
		mPager.setCurrentItem(position);
		
		pronunciationCharacter = new PronunciationCharacter(
				SlideCharacterActivity.this);
		
		
		btnback = (Button) findViewById(R.id.backChar);
		btnback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				position--;
				if (position < 0){position = adapter.getCount()-1;}
				Log.d("MYAPP","i "+position);
				mPager.setCurrentItem(position);
//				pronunciationCharacter.play(adapter.getChar(position));
			}
		});

		btnNext = (Button) findViewById(R.id.nextChar);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				position++;
				if (position == adapter.getCount()){position =0;}
				Log.d("MYAPP", ""+position);
				mPager.setCurrentItem(position);
				
			}
		});
		
		rlCharacter = findViewById(R.id.backToActivity);
		rlCharacter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				position= arg0;
				pronunciationCharacter.play(adapter.getChar(position));
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
				
			}
		});
	}

	@Override
	protected void onResume() {
		overridePendingTransition(R.anim.hold, R.anim.zoom_out_call);
		pronunciationCharacter.play(character);
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		
		pronunciationCharacter.play(adapter.getChar(position));
	}
	public class PronunciationCharacter {
		private Context mContext;
		private int id;

		public PronunciationCharacter(Context context) {
			// set up our audio player
			mContext = context;

		}

		public int getPositon(String character) {
			for (int i = 0; i < romajiAdapter.length; i++) {
				if (character.equals(romajiAdapter[i])) {
					return i;
				}
			}
			return -1;
		}

		public void play(String character) {

			MediaPlayer mediaPlayer = MediaPlayer.create(mContext,
					IdSoundCharacter[getPositon(character)]);
			mediaPlayer.setVolume(1.0f, 1.0f);
			mediaPlayer.start();
		}
		

		private int IdSoundCharacter[] = { R.raw.a, R.raw.i, R.raw.u, R.raw.e,
				R.raw.o, R.raw.ka, R.raw.ki, R.raw.ku, R.raw.ke, R.raw.ko,
				R.raw.sa, R.raw.shi, R.raw.su, R.raw.se, R.raw.so, R.raw.ta,
				R.raw.chi, R.raw.tsu, R.raw.te, R.raw.to, R.raw.na, R.raw.ni,
				R.raw.nu, R.raw.ne, R.raw.no, R.raw.ha, R.raw.hi, R.raw.fu,
				R.raw.he, R.raw.ho, R.raw.ma, R.raw.mi, R.raw.mu, R.raw.me,
				R.raw.mo, R.raw.ya, R.raw.yu, R.raw.yo, R.raw.ra, R.raw.ri,
				R.raw.ru, R.raw.re, R.raw.ro, R.raw.wa, R.raw.wo, R.raw.n,
				R.raw.ga, R.raw.gi, R.raw.gi, R.raw.ge, R.raw.go, R.raw.za,
				R.raw.ji, R.raw.zu, R.raw.ze, R.raw.zo, R.raw.da, R.raw.ji,
				R.raw.zu, R.raw.de, R.raw.doo, R.raw.ba, R.raw.bi, R.raw.bu,
				R.raw.be, R.raw.bo, R.raw.pa, R.raw.pi, R.raw.pu, R.raw.pe,
				R.raw.po, R.raw.kya, R.raw.kyu, R.raw.kyo, R.raw.sha, R.raw.shu,
				R.raw.sho, R.raw.cha, R.raw.chu, R.raw.cho, R.raw.nya, R.raw.nyu,
				R.raw.nyo, R.raw.hya, R.raw.hyu, R.raw.hyo, R.raw.mya, R.raw.myu,
				R.raw.myo, R.raw.rya, R.raw.ryu, R.raw.ryo, R.raw.gya, R.raw.gyu,
				R.raw.gyo, R.raw.ja, R.raw.ju, R.raw.jo, R.raw.bya, R.raw.byu,
				R.raw.byo, R.raw.pya, R.raw.pyu, R.raw.pyo };
		public String[] romajiAdapter = { "a", "i", "u", "e", "o", "ka", "ki",
				"ku", "ke", "ko", "sa", "shi", "su", "se", "so", "ta", "chi",
				"tsu", "te", "to", "na", "ni", "nu", "ne", "no", "ha", "hi", "fu",
				"he", "ho", "ma", "mi", "mu", "me", "mo", "ya", "yu", "yo", "ra",
				"ri", "ru", "re", "ro", "wa", "wo", "n", "ga", "gi", "gu", "ge",
				"go", "za", "ji", "zu", "ze", "zo", "da", "ji", "zu", "de", "do",
				"ba", " bi", " bu", "be", "bo", "pa", "pi", "pu", "pe", "po",
				"kya", "kyu", "kyo", "sha", "shu", "sho", "cha", "chu", "cho",
				"rya", "ryu", "ryo", "hya", "hyu", "hyo", "mya", "myu", "myo",
				"rya", "ryu", "ryo", "gya", "gyu", "gyo", "ja", "ju", "jo", "bya",
				"byu", "byo", "pya", "pyu", "pyo" };
	}

}
