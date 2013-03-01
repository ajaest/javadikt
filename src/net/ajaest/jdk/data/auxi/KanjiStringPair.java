//    Copyright (C) 2010  Luis Alfonso Arce González, ajaest[@]gmail.com
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package net.ajaest.jdk.data.auxi;


/**
 * 
 * Generic immutable class that stores two strings in order to reference some of
 * the kanji properties.
 * 
 * @author Luis Alfonso Arce González
 */
public class KanjiStringPair /* extends Pair<String, String> */implements KanjiReference {

	// it seems that neodatis database manager does not likes generic types.
	// Let's try implementing this without inheriting
	private Integer unicodeRef;
	private String first;
	public String getFirst() {
		return first;
	}

	public String getSecond() {
		return second;
	}

	private String second;

	public KanjiStringPair(String first, String second, Integer unicodeRef) {
		this.first = first;
		this.second = second;
		this.unicodeRef = unicodeRef;
	}

	public Integer getUnicodeRef() {

		return unicodeRef;
	}
}
