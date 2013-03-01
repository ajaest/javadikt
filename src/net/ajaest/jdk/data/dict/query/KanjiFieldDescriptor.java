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

package net.ajaest.jdk.data.dict.query;

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;

/**
 * Interface that provides methods to get information of the field of a kanji
 * that a class that implement it represents.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public interface KanjiFieldDescriptor {

	/**
	 * 
	 * @return the name of the kanji property this class represents
	 */
	String getFieldName();

	/**
	 * 
	 * @return the enum field representation of the kanji property this class
	 *         represents
	 */
	KanjiFieldEnum getFieldEnum();
}
