package com.ana.Character;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import com.example.japaneseapp.R;
import com.example.japaneseapp.R.id;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Translator extends AsyncTask<Void, Integer, Void> {
	private String translatedText;
	private Context activity;
	private boolean toJp;

	public Translator(Context activity) {
		this.activity = activity;
		toJp = true;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			String text = ((EditText) ((Activity) activity)
					.findViewById(R.id.edtTranslate)).getText().toString();
			translatedText = translate(text);
			// publishProgress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			translatedText = e.toString();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		((TextView) ((Activity) activity).findViewById(R.id.tv_resultTranslate))
				.setText(translatedText);
		super.onPostExecute(result);
	}

	public String translate(String text) throws Exception {

		// Set the Client ID / Client Secret once per JVM. It is set statically
		// and applies to all services
		Translate.setClientId("JapaneseAppCoding"); // Change this
		Translate
				.setClientSecret("SXN0U5/BOuHH9dIsjLUcfQpStB+ddef/wU0mUk4T9Vw="); // change

		String translatedText = "";
		if (toJp) {
			translatedText = Translate.execute(text,Language.VIETNAMESE, Language.JAPANESE);

		} else {
			translatedText = Translate.execute(text,Language.JAPANESE, Language.VIETNAMESE);
		}
		return translatedText;
	}

	public void setLanguage(boolean toJp) {
		this.toJp = toJp;
	}
}
