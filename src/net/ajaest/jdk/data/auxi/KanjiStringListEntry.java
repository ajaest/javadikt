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

import java.util.Collections;
import java.util.List;

/**
 * Generic immutable class that stores a list of {@code String} associated to a
 * {@code String} key in order to reference some of the kanji properties.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiStringListEntry /* extends ListEntry<String, String> */implements KanjiReference {

	// it seems that neodatis database manager does not likes generic types.
	// Let's try implementing this without inheriting from dicEntry
	private Integer unicodeRef;
	private String key;
	private List<String> elements;

	public KanjiStringListEntry(String key, List<String> elements, Integer unicodeRef) {

		this.unicodeRef = unicodeRef;
		this.key = key;
		this.elements = elements;
	}

	/**
	 * Returns the immutable list of elements. Read the constructor or class
	 * info for further information about the meaning of "elements".
	 */
	public List<String> getElements() {
		return Collections.unmodifiableList(elements);
	}

	public String getKey() {
		return key;
	}

	@Override
	public Integer getUnicodeRef() {

		return unicodeRef;
	}
}
