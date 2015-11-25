package com.ana.core;

public class ShowResult {
	private String jpWord;
	private String vnWord;
	private boolean isCorrect;

	public ShowResult(String jpword, String vnword, boolean isCorrect) {
		// TODO Auto-generated constructor stub
		jpWord = jpword;
		vnWord = vnword;
		this.isCorrect = isCorrect;
	}

	public String getJpWord() {
		return jpWord;
	}

	public String getVnWord() {
		return vnWord;
	}
	
	public boolean getIsCorrect() {
		return isCorrect;
	}
}