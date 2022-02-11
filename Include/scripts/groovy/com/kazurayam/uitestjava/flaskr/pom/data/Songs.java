package com.kazurayam.uitestjava.flaskr.pom.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Songs {

	private static final List<Song> songs;

	public static Song get(int index) {
		return songs.get(index);
	}

	static {
		LinkedHashMap<String, String> map = new LinkedHashMap<>(3);
		map.put("title", "時代");
		map.put("by", "中島みゆき");
		map.put("lyric", "まわるまわるよ時代は回る\n喜び悲しみくり返し\n今日は別れた恋人たちも\n生まれ変わってめぐりあうよ");
		LinkedHashMap<String, String> map1 = new LinkedHashMap<>(3);
		map1.put("title", "We Are the Champions");
		map1.put("by", "Queen");
		map1.put("lyric", "We are the champions, my friend\nAnd we'll keep on fighting 'til the end\nWe are the champions\nWe are the champions\nNo time for losers\n'Cause we are the champions of the world");
		songs = new ArrayList<>(Arrays.asList(new Song(map), new Song(map1)));
	}

}
