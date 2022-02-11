package com.kazurayam.uitestjava.flaskr.pom.data;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Objects;

public class Song {

	public Song(Map<String, String> map) {
		this(map.get("title"), map.get("by"), map.get("lyric"));
	}

	public Song(String title, String by, String lyric) {
		Objects.requireNonNull(title);
		Objects.requireNonNull(by);
		Objects.requireNonNull(lyric);
		this.title = title;
		this.by = by;
		this.lyric = lyric;
	}

	public final String getTitle() {
		return title;
	}

	public final String getBy() {
		return by;
	}

	public final String getLyric() {
		return lyric;
	}

	private final String title;
	private final String by;
	private final String lyric;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Song)) {
			return false;
		}
		Song other = (Song) obj;
		return this.title.equals(other.title) ||
				this.by.equals(other.by) ||
				this.lyric.equals(other.lyric);
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"title\":");
		sb.append(gson.toJson(this.title));
		sb.append(",");
		sb.append("\"by\":");
		sb.append(gson.toJson(this.by));
		sb.append(",");
		sb.append("\"lyric\":");
		sb.append(gson.toJson(this.lyric));
		sb.append("}");
		return sb.toString();
	}
}
