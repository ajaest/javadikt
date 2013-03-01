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

package net.ajaest.jdk.data.kanji;

import java.util.List;

import net.ajaest.jdk.data.auxi.KanjiStringListEntry;

/**
 * Generic immutable class that stores a list of {@code String} representing
 * meanings of the refered kanji in the language represented by the {@code
 * String key} in ISO 639-1.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class MeaningEntry extends KanjiStringListEntry {

	/**
	 * 
	 * @param key
	 *            Language string representation, should be ISO 639-1
	 * @param unicodeRef
	 *            Referenced kanji
	 */
	public MeaningEntry(String key, List<String> meanings, Integer unicodeRef) {
		super(key, meanings, unicodeRef);
	}

}
