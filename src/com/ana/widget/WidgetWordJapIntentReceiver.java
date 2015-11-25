package com.ana.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ana.core.NewWord;
import com.example.japaneseapp.R;
import com.example.japaneseapp.R.id;
import com.example.japaneseapp.R.layout;
import com.example.japaneseapp.R.raw;

public class WidgetWordJapIntentReceiver extends BroadcastReceiver {

	private ArrayList<NewWord> listWord;
	private static int count = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals("jap.newword.intent.action.CHANGE_WORD")) {
			updateWidgetNewWord(context, count);
		}

	}

	private void updateWidgetNewWord(Context context, int count) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.jap_widget_layout);
		listWord = new ArrayList<NewWord>();
		readData(context);
		int i = getPosition();
		
		remoteViews.setTextViewText(R.id.tvWidgetJp, listWord.get(i)
				.getJpWord());
		remoteViews.setTextViewText(R.id.tvWidgetVn, listWord.get(i)
				.getVnWord());
		remoteViews.setOnClickPendingIntent(R.id.btnWidgetChange,
				WidgetWordJapProvider.buildButtonPendingIntent(context));

		WidgetWordJapProvider.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
		count++;
	}

	private int getPosition() {
		count++;
		return count;
	}

	public void readData(Context context) {
		
		for (int i = 0; i < 24; i++) {
			InputStream in = context.getResources()
					.openRawResource(NameUnit[i]);
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
					listWord.add(newWord);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Collections.shuffle(listWord);
		}
	}

	public int NameUnit[] = { R.raw.unit1, R.raw.unit2, R.raw.unit3,
			R.raw.unit4, R.raw.unit5, R.raw.unit6, R.raw.unit7,
			R.raw.unit8, R.raw.unit9, R.raw.unit10, R.raw.unit11,
			R.raw.unit12, R.raw.unit13, R.raw.unit14, R.raw.unit15,
			R.raw.unit16, R.raw.unit17, R.raw.unit18, R.raw.unit19,
			R.raw.unit20, R.raw.unit21, R.raw.unit22, R.raw.unit23,
			R.raw.unit24, R.raw.unit25 };
}
