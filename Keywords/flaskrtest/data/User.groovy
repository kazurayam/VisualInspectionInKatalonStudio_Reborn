package flaskrtest.data

enum User {

	Alice("AliceInTheWonderland"),
	Bob("LikeARollingStone"),
	Chage("Say Yes!")
	;

	private final String password
	private User(String password) {
		this.password = password
	}
	String getPassword() {
		return this.password
	}
}
