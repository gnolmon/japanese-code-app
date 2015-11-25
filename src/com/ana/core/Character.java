package com.ana.core;

public class Character {
	private char character;
	private boolean isHiragana;

	public Character(char charac) {
		super();
		this.character = charac;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char charac) {
		this.character = charac;
	}

	@Override
	public String toString() {
		return ("" + character);
	}
}
