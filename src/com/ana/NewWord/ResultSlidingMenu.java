package com.ana.NewWord;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ana.NewWord.TestNewWordActivity.SetShowResult;
import com.ana.core.NewWord;
import com.example.japaneseapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ResultSlidingMenu extends SlidingMenu implements SetShowResult {
	Activity activity;
	private ListView lvResult;
	ArrayList<NewWord> listWord;
	ArrayList<Integer> listResult;
	private int idUnit = 0;
	private LinearLayout spaceShowResult;

	public ResultSlidingMenu(Activity activity) {
		super(activity);
		setFadeDegree(0.35f);
		this.activity = activity;
		setMenu(R.layout.showresult_menu);

		attachToActivity(activity, SLIDING_WINDOW);
		lvResult = (ListView) findViewById(R.id.listShowResult);
		spaceShowResult = (LinearLayout) findViewById(R.id.spaceShowResult);
	}

	@Override
	public void setShowResult(ArrayList<NewWord> listword,
			ArrayList<Integer> listResult) {
		this.listWord = listword;
		this.listResult = listResult;
		ListShowResult listShowResult = new ListShowResult(activity, listWord, listResult, R.layout.showresult_menu);
		lvResult.setAdapter(listShowResult);
		int count=0;
		for (int i = 0; i < listResult.size(); i++) {
			if(listResult.get(i)==1){
				count++;
			}
		}
		if(count>=(listResult.size()/2)){
			spaceShowResult.setBackgroundColor(Color.parseColor("#63BB41"));
		}
		else {
			spaceShowResult.setBackgroundColor(Color.parseColor("#EB372A"));

		}
	}

public class ListShowResult extends ArrayAdapter<String> {
	private Activity context = null;
	ArrayList<NewWord> listWord = null;
	ArrayList<Integer> listResult = null;
	private int layoutId;

	public ListShowResult(Activity context, ArrayList<NewWord> list,ArrayList<Integer> listResult, int layoutId) {
		super(context, layoutId);
		this.context = context;
		this.layoutId = layoutId;
		this.listWord = list;
		this.listResult = listResult;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listView;
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			listView = inflater.inflate(R.layout.item_showresult, null);
		} else {
			listView = convertView;
		}
		TextView tvJpWord = (TextView) listView.findViewById(R.id.resultJapWord);
		TextView tvVnWord = (TextView) listView.findViewById(R.id.resultVnWord);
		NewWord newword = listWord.get(position);
		if(listResult.get(position) == 1){
			tvJpWord.setText(newword.getJpWord());
			tvVnWord.setText(newword.getVnWord());
			tvJpWord.setTextColor(Color.parseColor("#63BB41"));
			tvVnWord.setTextColor(Color.parseColor("#63BB41"));
		} else if (listResult.get(position) == 2){
			tvJpWord.setText(newword.getJpWord());
			tvVnWord.setText(newword.getVnWord());
			tvJpWord.setTextColor(Color.parseColor("#EB372A"));
			tvVnWord.setTextColor(Color.parseColor("#EB372A"));
		} 
		
		
		
		return listView;
	}
	
	@Override
	public int getCount() {
		return listWord.size();
	}
}
	
}
