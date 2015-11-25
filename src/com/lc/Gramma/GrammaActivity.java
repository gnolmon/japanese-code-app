package com.lc.Gramma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.R.menu;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ana.NewWord.UnitSlideMenu;
import com.ana.NewWord.UnitSlideMenu.UnitChangeListener;
import com.example.japaneseapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class GrammaActivity extends Activity implements UnitChangeListener {

	private TextView tv_data;
	private Button btnLight;
	private int turnOn;
	private RelativeLayout relativeLayout;
	private UnitSlideMenu unit;
	private View toUnit, backCharacter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_gramma);
		
		this.unit = new UnitSlideMenu(this);
		unit.setMode(SlidingMenu.LEFT);
		unit.setBehindOffset(250);
		unit.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_data.setMovementMethod(new ScrollingMovementMethod());
		readData(NameUnit[0]);
		turnOn = 1;
		relativeLayout = (RelativeLayout) findViewById(R.id.spaceGramma);
		btnLight = (Button) findViewById(R.id.btnlight);
		btnLight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(turnOn == 1){
					relativeLayout.setBackgroundColor(Color.parseColor("#404040"));
					tv_data.setTextColor(Color.parseColor("#FFFFFF"));
					turnOn = 0;
				} else {
					relativeLayout.setBackgroundColor(Color.parseColor("#F6F7F8"));
					tv_data.setTextColor(Color.parseColor("#404040"));
					turnOn = 1;
				}
					
			}
		});
		
		toUnit = findViewById(R.id.toUnit);
		toUnit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				unit.showMenu();
			}
		});
		
		backCharacter = findViewById(R.id.backCharacter);
		backCharacter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void readData(int idUnit) {
		String data;
		InputStream in = getResources().openRawResource(idUnit);
		InputStreamReader inreader = new InputStreamReader(in);
		BufferedReader bufreader = new BufferedReader(inreader);
		StringBuilder builder = new StringBuilder();
		if (in != null) {
			try {
				while ((data = bufreader.readLine()) != null) {
					builder.append(data);
					builder.append("\n");
				}
				in.close();
				tv_data.setText(builder.toString());
			} catch (IOException ex) {
				Log.e("ERROR", ex.getMessage());
			}
		}
	}

	@Override
	public void onChangeUnit(int idUnit) {
		readData(NameUnit[idUnit]);
		unit.showContent();
	}
	
	public int NameUnit[] = { R.raw.nguphap1 };

}
