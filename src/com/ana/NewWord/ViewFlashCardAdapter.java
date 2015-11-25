package com.ana.NewWord;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ana.core.NewWord;
import com.example.japaneseapp.R;

public class ViewFlashCardAdapter extends PagerAdapter {

	private Context mContext;
	private ArrayList<NewWord> listWords = null;
	private String[] jpWordlist;
	private String[] vnWordlist;
	private boolean isJapWord;
	public ViewFlashCardAdapter(Context context, ArrayList<NewWord> listWords) {
		this.mContext = context;
		this.listWords = listWords;
		this.isJapWord = true;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_flashcard_word, null);
		TextView tvWord = (TextView) view.findViewById(R.id.tvWordOfFlashCard);
		if(isJapWord){
			tvWord.setText(listWords.get(position).getJpWord());
		}
		else {
			tvWord.setText(listWords.get(position).getVnWord());
		}
		((ViewPager) container).addView(view, 0);
		return view;
	}

	public void setMeaning(boolean isJpWord){
		this.isJapWord = isJpWord;
	}
	@Override
	public int getCount() {
		return listWords.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == ((View) arg1);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
}
