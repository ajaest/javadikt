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
 * Representation of a JIS code.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class JISPair extends KanjiStringPair {

	public static final String JIS_X_208 = "jis208";
	public static final String JIS_X_212 = "jis212";
	public static final String JIS_X_213 = "jis213";

	/**
	 * 
	 * 
	 * @param first
	 *            Jis standar,could be JIS_X_208, JIS_X_212 or JIS_X_213
	 * @param second
	 *            Kuten code, should be "nn-nn" for JIS_X_208 and JIS_X_212 or
	 *            "p-nn-nn" for JIS_X_213
	 * @param unicodeRef
	 *            Referenced kanji
	 * @throws IllegalArgumentException
	 *             If the specified JIS standar does not exists
	 */
	public JISPair(String first, String second, Integer unicodeRef) {
		super(first, second, unicodeRef);

		if (!(first.equals(JIS_X_208) || first.equals(JIS_X_212) || first.equals(JIS_X_213)))
			throw new IllegalArgumentException("not a jis standar, please select JIS_X_208, JIS_X_212 or JIS_X_213.");

	}
}
