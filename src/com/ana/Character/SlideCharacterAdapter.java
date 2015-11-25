package com.ana.Character;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.japaneseapp.R;

public class SlideCharacterAdapter extends PagerAdapter {
	private TextView tvChar;
	private TextView tvDetailRomaji;
	private WebView webView;
	private Context mContext;
	private boolean isHiragana;

	OnClickListener viewSlideCharacter;
	AssetManager assetManager;
	public SlideCharacterAdapter(Context context,
			OnClickListener viewSlideCharacter, boolean isHiragana) {
		this.mContext = context;
		this.viewSlideCharacter = viewSlideCharacter;
		this.isHiragana = isHiragana;
		this.assetManager =context.getAssets();
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_show_detail_character,
				null);
		tvChar = (TextView) view.findViewById(R.id.detailCharacter);
		tvDetailRomaji = (TextView) view.findViewById(R.id.detailRomaji);
		String url = romajiAdapter[position]+".gif";
		if (isHiragana){
			tvChar.setText(hiraganaAdapter[position]);
			url = "h_" + url;
		}else {
			tvChar.setText(katakanaAdapter[position]);
			url = "k_"+url;
		}
		 InputStream ims = null;
		try {
            ims = assetManager.open(url);
            ims.close();
        }
        catch(IOException ex) {
        }
		if(ims == null){ url = "default.jpg";}
		tvDetailRomaji.setText(romajiAdapter[position]);
		webView = (WebView) view.findViewById(R.id.wv_detail);
		String data = "<body style=\"  margin-left: 0;    margin-top: 0;\"><img width= \"160\" height= \"120\" src=\""
				+ url + "\"/></body>";
		Button sound = (Button) view.findViewById(R.id.sound);
		sound.setOnClickListener(viewSlideCharacter);
		webView.loadDataWithBaseURL("file:///android_asset/", data,
				"text/html", "utf-8", null);

		((ViewPager) container).addView(view, 0);
		return view;
	}

	@Override
	public int getCount() {
		return romajiAdapter.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
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

	public int getPositionChar(String character) {
		for (int i = 0; i < romajiAdapter.length; i++) {
			if (character.equals(romajiAdapter[i])) {
				return i;
				
			}
		}
		return -1;
	}

	public String getChar(int position) {
		return romajiAdapter[position];
	}
	public void setHiragana(boolean isHiragana) {
		this.isHiragana = isHiragana;
	}

	private String[] romajiAdapter = { "a", "i", "u", "e", "o", "ka", "ki",
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

	private String[] hiraganaAdapter = { "あ", "い", "う", "え", "お", "か", "き",
			"く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と",
			"な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む",
			"め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ", "を",
			"ん", "が", "ぎ", "ぐ", "げ", "ご", "ざ", "じ", "ず", "ぜ", "ぞ", "だ", "ぢ",
			"づ", "で", "ど", "ば", "び", "ぶ", "べ", "ぼ", "ぱ", "ぴ", "ぷ", "ぺ", "ぽ",
			"きゃ", "きゅ", "きょ", "しゃ", "しゅ", "しょ", "ちゃ", "ちゅ", "ちょ", "にゃ", "にゅ",
			"にょ", "ひゃ", "ひゅ", "ひょ", "みゃ", "みゅ", "みょ", "りゃ", "りゅ", "りょ", "ぎゃ",
			"ぎゅ", "ぎょ", "じゃ", "じゅ", "じょ", "びゃ", "びゅ", "びょ", "ぴゃ", "ぴゅ", "ぴょ" };

	private String[] katakanaAdapter = { "ア", "イ", "ウ", "エ", "オ", "カ", "キ",
			"ク", "ケ", "コ", "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト",
			"ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ", "マ", "ミ", "ム",
			"メ", "モ", "ヤ", "ユ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ワ", "ヲ", "ン",
			"ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ", "ダ", "ヂ", "ヅ",
			"デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ", "パ", "ピ", "プ", "ペ", "ポ", "キャ",
			"キュ", "キョ", "シャ", "シュ", "ショ", "チャ", "チュ", "チョ", "ニャ", "ニュ", "ニョ",
			"ヒャ", "ヒュ", "ヒョ", "ミャ", "ミュ", "ミョ", "リャ", "リュ", "リョ", "ギャ", "ギュ",
			"ギョ", "ジャ", "ジュ", "ジョ", "ビャ", "ビュ", "ビョ", "ピャ", "ピュ", "ピョ" };

	

}
