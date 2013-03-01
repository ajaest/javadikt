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

import net.ajaest.jdk.data.auxi.KanjiStringPair;

/**
 * Immutable representation of a kanji variant reference.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class VariantPair extends KanjiStringPair {

	/**
	 * 
	 * @param first
	 *            The name of the variant
	 * @param second
	 *            The indexed reference of the variant in the variant name
	 *            context
	 * @param unicodeRef
	 *            Referenced kanji
	 */
	public VariantPair(String first, String second, Integer unicodeRef) {
		super(first, second, unicodeRef);
	}

}
