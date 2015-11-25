package com.ana.core;

public class NewWord {
	private String jpWord;
	private String vnWord;
	private String romajiWord;

	public NewWord(String jpword, String vnword, String romajiWord) {
		// TODO Auto-generated constructor stub
		this.jpWord = jpword;
		this.vnWord = vnword;
		this.romajiWord = romajiWord; 
	}

	public String getJpWord() {
		return jpWord;
	}

	public String getVnWord() {
		return vnWord;
	}
	
	public String getRomajiWord() {
		return romajiWord;
	}
}
