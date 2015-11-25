package com.ana.Character;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.japaneseapp.R;

public class CharacterAdapter extends BaseAdapter {

	private Context mContext;
	private String[] gridDisplay;

	public CharacterAdapter(Context c) {
		mContext = c;

	}

	public void setCharacterApdapter(boolean isHiragana) {
		if (isHiragana) {
			gridDisplay = hiraganaAdapter;
		} else {
			gridDisplay = katakanaAdapter;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gridDisplay.length;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return romajiAdapter[position];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View grid = convertView;

		if (grid == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			grid = inflater.inflate(R.layout.gird_character, parent, false);
			// grid.findViewById(id).set
		} else {

		}

		TextView textHiragana = (TextView) grid
				.findViewById(R.id.characterHiragana);
		TextView textRomaji = (TextView) grid
				.findViewById(R.id.characterRomaji);
		if (romajiAdapter[position].equals("")) {
			grid.findViewById(R.id.lnH).setBackgroundColor(Color.WHITE);
		} else {
			grid.findViewById(R.id.lnH).setBackgroundResource(R.drawable.border_grid_up);
		}
		textHiragana.setText(gridDisplay[position]);
		textRomaji.setText(romajiAdapter[position]);

		return grid;
	}

	public String[] romajiAdapter = { "a", "i", "u", "e", "o", "ka", "ki",
			"ku", "ke", "ko", "sa", "shi", "su", "se", "so", "ta", "chi",
			"tsu", "te", "to", "na", "ni", "nu", "ne", "no", "ha", "hi", "fu",
			"he", "ho", "ma", "mi", "mu", "me", "mo", "ya", "", "yu", "", "yo",
			"ra", "ri", "ru", "re", "ro", "wa", "", "wo", "", "n", "ga", "gi",
			"gu", "ge", "go", "za", "ji", "zu", "ze", "zo", "da", "ji", "zu",
			"de", "do", "ba", " bi", " bu", "be", "bo", "pa", "pi", "pu", "pe",
			"po", "kya", "", "kyu", "", "kyo", "sha", "", "shu", "", "sho",
			"cha", "", "chu", "", "cho", "rya", "", "ryu", "", "ryo", "hya",
			"", "hyu", "", "hyo", "mya", "", "myu", "", "myo", "rya", "",
			"ryu", "", "ryo", "gya", "", "gyu", "", "gyo", "ja", "", "ju", "",
			"jo", "bya", "", "byu", "", "byo", "pya", "", "pyu", "", "pyo" };

	public String[] hiraganaAdapter = { "あ", "い", "う", "え", "お", "か", "き", "く",
			"け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な",
			"に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む", "め",
			"も", "や", "", "ゆ", "", "よ", "ら", "り", "る", "れ", "ろ", "わ", "", "を",
			"", "ん", "が", "ぎ", "ぐ", "げ", "ご", "ざ", "じ", "ず", "ぜ", "ぞ", "だ",
			"ぢ", "づ", "で", "ど", "ば", "び", "ぶ", "べ", "ぼ", "ぱ", "ぴ", "ぷ", "ぺ",
			"ぽ", "きゃ", "", "きゅ", "", "きょ", "しゃ", "", "しゅ", "", "しょ", "ちゃ", "",
			"ちゅ", "", "ちょ", "にゃ", "", "にゅ", "", "にょ", "ひゃ", "", "ひゅ", "", "ひょ",
			"みゃ", "", "みゅ", "", "みょ", "りゃ", "", "りゅ", "", "りょ", "ぎゃ", "", "ぎゅ",
			"", "ぎょ", "じゃ", "", "じゅ", "", "じょ", "びゃ", "", "びゅ", "", "びょ", "ぴゃ",
			"", "ぴゅ", "", "ぴょ" };

	public String[] katakanaAdapter = { "ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク",
			"ケ", "コ", "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト", "ナ",
			"ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ", "マ", "ミ", "ム", "メ",
			"モ", "ヤ", "", "ユ", "", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ワ", "", "ヲ",
			"", "ン", "ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ", "ダ",
			"ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ", "パ", "ピ", "プ", "ペ",
			"ポ", "キャ", "", "キュ", "", "キョ", "シャ", "", "シュ", "", "ショ", "チャ", "",
			"チュ", "", "チョ", "ニャ", "", "ニュ", "", "ニョ", "ヒャ", "", "ヒュ", "", "ヒョ",
			"ミャ", "", "ミュ", "", "ミョ", "リャ", "", "リュ", "", "リョ", "ギャ", "", "ギュ",
			"", "ギョ", "ジャ", "", "ジュ", "", "ジョ", "ビャ", "", "ビュ", "", "ビョ", "ピャ",
			"", "ピュ", "", "ピョ" };
}
