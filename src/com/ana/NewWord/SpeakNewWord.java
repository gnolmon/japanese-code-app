package com.ana.NewWord;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class SpeakNewWord {
	private static final String TAG = "iSpeech SDK Sample";
	SpeechSynthesis synthesis;
	Context _context;
	Activity activity;

	public SpeakNewWord(Activity activity, Context context) {
		this.activity = activity;
		this._context = context;
		prepareTTSEngine();
	}

	private void prepareTTSEngine() {
		try {
			synthesis = SpeechSynthesis.getInstance(activity);
			synthesis.setSpeechSynthesisEvent(new SpeechSynthesisEvent() {

				public void onPlaySuccessful() {
					Log.i(TAG, "onPlaySuccessful");
				}

				public void onPlayStopped() {
					Log.i(TAG, "onPlayStopped");
				}

				public void onPlayFailed(Exception e) {
					Log.e(TAG, "onPlayFailed");

				}

				public void onPlayStart() {
					Log.i(TAG, "onPlayStart");
				}

				@Override
				public void onPlayCanceled() {
					Log.i(TAG, "onPlayCanceled");
				}

			});

		} catch (InvalidApiKeyException e) {
			Log.e(TAG, "Invalid API key\n" + e.getStackTrace());
			Toast.makeText(_context, "ERROR: Invalid API key",
					Toast.LENGTH_LONG).show();
		}

	}

	public void speakWord(String ttsText) {
		try {
			synthesis.setVoiceType("jpjapanesefemale");
			synthesis.speak(ttsText);

		} catch (BusyException e) {
			Log.e(TAG, "SDK is busy");
			e.printStackTrace();
			Toast.makeText(_context, "ERROR: SDK is busy", Toast.LENGTH_LONG)
					.show();
		} catch (NoNetworkException e) {
			Log.e(TAG, "Network is not available\n" + e.getStackTrace());
			Toast.makeText(_context, "ERROR: Network is not available",
					Toast.LENGTH_LONG).show();
		}
	}
}
