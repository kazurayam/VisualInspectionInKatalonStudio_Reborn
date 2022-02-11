package com.kazurayam.uitestjava.flaskr.pom.data;

public enum User {

	Alice("AliceInTheWonderland"),
	Bob("LikeARollingStone"),
	Chage("Say Yes!");

	User(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	private final String password;
}
