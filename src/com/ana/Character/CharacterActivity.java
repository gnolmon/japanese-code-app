package com.ana.Character;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ana.DrawCharacter.TestDrawCharacterActivity;
import com.ana.NewWord.WordActivity;
import com.example.japaneseapp.R;

public class CharacterActivity extends Activity {

	private CharacterAdapter adapterCharacter;
	private GridView gridviewOfCharacter;
	private View btnChangGridTypeChar;
	private View layoutTest, spaceToNewWord;
	private boolean isHiragana = true;
	private Button btnTranslate;
	private ToggleButton btnChange;
	private Translator translator;
	private TextView tv_hiragana;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);

		btnChangGridTypeChar = (View) findViewById(R.id.btnChangeTypeChar);
		tv_hiragana = (TextView) findViewById(R.id.btnTest1);
		tv_hiragana.setText(R.string.hiragana);
		layoutTest = findViewById(R.id.spaceOfButtonCharacter);
		spaceToNewWord = findViewById(R.id.toNewWord);
		// make and set a adapter to grid view
		adapterCharacter = new CharacterAdapter(this);
		adapterCharacter.setCharacterApdapter(isHiragana);
		gridviewOfCharacter = (GridView) findViewById(R.id.characterHiraganaGrid);
		gridviewOfCharacter.setAdapter(adapterCharacter);
		gridviewOfCharacter
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (!adapterCharacter.getItem(position).equals("")) {
							Intent intent = new Intent(CharacterActivity.this,
									SlideCharacterActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							intent.putExtra("CHARACTER",
									adapterCharacter.getItem(position));
							intent.putExtra("ISHIRAGANA", isHiragana);
							startActivity(intent);
						}
					}
				});
		btnChangGridTypeChar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isHiragana) {
					tv_hiragana.setText(R.string.katakana);

					isHiragana = false;
				} else {
					tv_hiragana.setText(R.string.hiragana);
					isHiragana = true;
				}
				adapterCharacter.setCharacterApdapter(isHiragana);
				gridviewOfCharacter = (GridView) findViewById(R.id.characterHiraganaGrid);
				gridviewOfCharacter.setAdapter(adapterCharacter);

			}
		});
		super.onCreate(savedInstanceState);

		layoutTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CharacterActivity.this,
						TestDrawCharacterActivity.class);
				intent.putExtra("ISHIRAGANA", isHiragana);

				startActivity(intent);
				overridePendingTransition(R.anim.pull_in_left,
						R.anim.push_out_right);
			}
		});

		spaceToNewWord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CharacterActivity.this,
						WordActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.pull_in_right,
						R.anim.push_out_left);
			}
		});

		btnTranslate = (Button) findViewById(R.id.btnTranslate);
		btnTranslate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				translator = new Translator(CharacterActivity.this);
				translator.execute();
			}
		});

		btnChange = (ToggleButton) findViewById(R.id.btnChange);
		btnChange.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				translator = new Translator(CharacterActivity.this);
				translator.setLanguage(isChecked);
			}
		});

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
			if (Math.abs(distance) > 400 && y < x) {

				Intent intent = new Intent(CharacterActivity.this,
						WordActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.pull_in_right,
						R.anim.push_out_left);
			}
			// if (Math.abs(distance) > 400 && y > x) {
			//
			// Intent intent = new Intent(CharacterActivity.this,
			// GrammaActivity.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.pull_in_left,
			// R.anim.push_out_right);
			// }

			break;

		}
		return super.onTouchEvent(event);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		return this.onTouchEvent(ev);
	}
}
