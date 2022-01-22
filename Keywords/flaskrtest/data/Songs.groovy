package flaskrtest.data

public class Songs {

	private static final List<Song> songs = [
		new Song(
		"title": "時代",
		"by": "中島みゆき",
		"lyric": """まわるまわるよ時代は回る
喜び悲しみくり返し
今日は別れた恋人たちも
生まれ変わってめぐりあうよ"""),
		new Song(
		'title': "We Are the Champions",
		'by': "Queen",
		'lyric': '''We are the champions, my friend
And we'll keep on fighting 'til the end
We are the champions
We are the champions
No time for losers
'Cause we are the champions of the world''',
		),
	]

	static Song get(int index) {
		return songs[index]
	}
}
