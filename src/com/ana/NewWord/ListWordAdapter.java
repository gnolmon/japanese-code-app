package com.ana.NewWord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ana.core.NewWord;
import com.example.japaneseapp.R;

public class ListWordAdapter extends ArrayAdapter<NewWord> {
	Activity context = null;
	ArrayList<NewWord> listWord = null;
	int layoutId;

	public ListWordAdapter(Activity context, int layoutId,
			int idUnit) {
		super(context, layoutId, idUnit);

		this.context = context;
		this.layoutId = layoutId;
		listWord = new ArrayList<NewWord>();
		readData(context, idUnit);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listView;
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			listView = inflater.inflate(R.layout.item_newword, null);
		} else {
			listView = convertView;
		}
		TextView tvJpWord = (TextView) listView.findViewById(R.id.JapWord);
		TextView tvVnWord = (TextView) listView.findViewById(R.id.VnWord);
		NewWord newword = listWord.get(position);
		tvJpWord.setText(newword.getJpWord());
		tvVnWord.setText(newword.getVnWord());
		return listView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listWord.size();
	}
	
	public String getWord(int position){
		return listWord.get(position).getRomajiWord();
	}
	
	public void readData(Context context, int idUnit) {
		InputStream in = context.getResources().openRawResource(
				idUnit);
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
				NewWord newWord = new NewWord(jpWord, vnWord,romajiWord);
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

}
