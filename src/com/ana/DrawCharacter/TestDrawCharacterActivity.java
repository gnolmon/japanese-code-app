package com.ana.DrawCharacter;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ana.DrawCharacter.CharContainer.CompleteCharacterExeption;
import com.ana.DrawCharacter.CharContainer.NotFoundCharacterExeption;
import com.ana.DrawCharacter.DrawView.OnCheckStrokeListener;
import com.example.japaneseapp.R;

public class TestDrawCharacterActivity extends Activity implements
		OnCheckStrokeListener, AnimationListener {
	private Button btnNext, btnForward, btnReset, btnCheck;
	private TextView tvTarget;
	private int position = 0;
	private View backToCharacter;
	private CharContainer charContainer;
	private DrawView drView;
	private RelativeLayout relativeLayout;
	private Animation animationToMiddle;
	private Animation animationFromMiddle;
	private boolean isFront;
	private boolean isHiragana;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_test_character);
		Intent intent = getIntent();
		isHiragana = intent.getBooleanExtra("ISHIRAGANA", true);
		charContainer = new CharContainer();
		try {
			charContainer.appendCharacterData(getResources().openRawResource(
					R.raw.chardata));
			charContainer.appendStrokeOrderContainer(getResources()
					.openRawResource(R.raw.strokedata));

		} catch (NotFoundException | IOException e) {
			e.printStackTrace();
		}
		charContainer.printoutdata();
		drView = (DrawView) findViewById(R.id.drawview);
		drView.setOnCheckListener(this);
		relativeLayout = (RelativeLayout) findViewById(R.id.backHint);
		tvTarget = (TextView) findViewById(R.id.tvTarget);
		tvTarget.setText(romajiAdapter[position]);
		isFront = true;
		animationToMiddle = AnimationUtils
				.loadAnimation(this, R.anim.to_middle);
		animationToMiddle.setAnimationListener(this);
		animationFromMiddle = AnimationUtils.loadAnimation(this,
				R.anim.from_middle);
		animationFromMiddle.setAnimationListener(this);
		webView = (WebView) findViewById(R.id.wv_detail);

		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// DrawView drView = (DrawView) findViewById(R.id.drawview);
				drView.erase();
			}
		});
		btnNext = (Button) findViewById(R.id.btnCharNext);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				position++;
				if (position == romajiAdapter.length) {
					position = 0;
				}
				tvTarget.setText(romajiAdapter[position]);
			}
		});

		btnForward = (Button) findViewById(R.id.btnCharBack);
		btnForward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				position--;
				if (position < 0) {
					position = romajiAdapter.length - 1;
				}
				tvTarget.setText(romajiAdapter[position]);
			}
		});

		backToCharacter = findViewById(R.id.backToChar);
		backToCharacter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnCheck = (Button) findViewById(R.id.btnCheck);
		btnCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isFront) {
					drView.clearAnimation();
					drView.setAnimation(animationToMiddle);
					drView.startAnimation(animationToMiddle);
					drView.setVisibility(View.INVISIBLE);
				} else {
					relativeLayout.clearAnimation();
					relativeLayout.setAnimation(animationToMiddle);
					relativeLayout.startAnimation(animationToMiddle);
					relativeLayout.setVisibility(View.INVISIBLE);
				}

			}
		});
	}

	@Override
	public void onCheck() {
		if (charContainer != null) {
			try {
				String getname = getCharacternameData((String) tvTarget
						.getText());
				int offset = charContainer.compareStrokeByStroke(getname,
						drView.getCharStrokes());
				drView.setRemaintoOffsetStroke(offset);
			} catch (NotFoundCharacterExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CompleteCharacterExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
		super.onPause();
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (animation == animationToMiddle) {

			if (isFront) {
				isFront = false;
				relativeLayout.setVisibility(View.VISIBLE);
				String url = romajiAdapter[position] + ".gif";
				if (isHiragana) {
					;
					url = "h_" + url;
				} else {
					url = "k_" + url;
				}
				String data = "<body style=\"  margin-left: 0;    margin-top: 0;\"><img width= \"320\" height= \"240\" src=\""
						+ url + "\"/></body>";
				webView.loadDataWithBaseURL("file:///android_asset/", data,
						"text/html", "utf-8", null);
				webView.clearAnimation();
				webView.setAnimation(animationFromMiddle);
				webView.startAnimation(animationFromMiddle);
				// postionWord = mPager.getCurrentItem();
				// adapterJapWord.setMeaning(isJapWord);
				// mPager.setAdapter(adapterJapWord);
				// mPager.setCurrentItem(postionWord);
			} else {
				isFront = true;
				drView.setVisibility(View.VISIBLE);
				drView.clearAnimation();
				drView.setAnimation(animationFromMiddle);
				drView.startAnimation(animationFromMiddle);
				// postionWord = mPager.getCurrentItem();
				// adapterJapWord.setMeaning(isJapWord);
				// mPager.setAdapter(adapterJapWord);
				// mPager.setCurrentItem(postionWord);

			}
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	private String getCharacternameData(String romaji) {
		for (int i = 0; i < romajiAdapter.length; i++) {
			if (romaji.equals(romajiAdapter[i])) {
				if (isHiragana) {
					return HiraganaData[i];
				}
				return KataganaData[i];
			}
		}
		return null;
	}

	public String[] romajiAdapter = { "a", "i", "u", "e", "o", "ka", "ki",
			"ku", "ke", "ko", "sa", "shi", "su", "se", "so", "ta", "chi",
			"tsu", "te", "to", "na", "ni", "nu", "ne", "no", "ha", "hi", "fu",
			"he", "ho", "ma", "mi", "mu", "me", "mo", "ya", "yu", "yo", "ra",
			"ri", "ru", "re", "ro", "wa", "wo", "n" };
	public String[] HiraganaData = { "h_a", "h_i", "h_u", "h_e", "h_o", "h_ka",
			"h_ki", "h_ku", "h_ke", "h_ko", "h_sa", "h_si", "h_su", "h_se",
			"h_so", "h_ta", "h_chi", "h_tsu", "h_te", "h_to", "h_na", "h_ni",
			"h_nu", "h_ne", "h_no", "h_ha", "h_hi", "h_fu", "h_he", "h_ho",
			"h_ma", "h_mi", "h_mu", "h_me", "h_mo", "h_ya", "h_yu", "h_yo",
			"h_ra", "h_ri", "h_ru", "h_re", "h_ro", "h_wa", "h_wo", "h_n" };

	public String[] KataganaData = { "k_a", "k_i", "k_u", "k_e", "k_o", "k_ka",
			"k_ki", "k_ku", "k_ke", "k_ko", "k_sa", "k_si", "k_su", "k_se",
			"k_so", "k_ta", "k_chi", "k_tsu", "k_te", "k_to", "k_na", "k_ni",
			"k_nu", "k_ne", "k_no", "k_ha", "k_hi", "k_fu", "k_he", "k_ho",
			"k_ma", "k_mi", "k_mu", "k_me", "k_mo", "k_ya", "k_yu", "k_yo",
			"k_ra", "k_ri", "k_ru", "k_re", "k_ro", "k_wa", "k_wo", "k_n" };
}
