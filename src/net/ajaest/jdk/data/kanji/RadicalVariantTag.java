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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ajaest.jdk.data.auxi.KanjiEnums.RadicalTypeEnum;


/**
 * Mutable class that stores information about a radical variant and provides
 * methods to build radical variant information objects. The only attribute
 * shared between radical's variants are the radical number this class contains
 * much further information about the radical than the basic {@code Radical}
 * class
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class RadicalVariantTag implements Comparable<RadicalVariantTag> {

	protected Integer radicalNumber;
	protected Integer auxNumber;

	protected String unicodeName;
	protected List<String> names;
	
	protected Integer unicode;
	protected Integer kangxiUnicode;

	public RadicalTypeEnum radicalType;
	
	protected RadicalVariantTag(Integer radicalNumber) {
		this.radicalNumber = radicalNumber;
		names = new ArrayList<String>();
	}

	/**
	 * The radical auxiliary number is used to discern between the different
	 * shapes of the radical depending on the kanji which owns it.
	 * 
	 * @return the auxiliary radical number of this variant
	 */
	public Integer getAuxNumber() {
		return auxNumber;
	}

	/**
	 * Returns the unicode name of the character which code point equals the
	 * returned value of the method {@code getUnicode()}
	 * 
	 * @return the unicode name of this radical unicode character
	 */
	public String getUnicodeName() {
		return unicodeName;
	}

	/**
	 * Returns the names in Japanese of this radical variant. A radical can have
	 * many names.
	 * 
	 * @return the names list of this radical variant
	 */
	public List<String> getNames() {
		return Collections.unmodifiableList(names);
	}

	/**
	 * Returns the radical variant's character main representation code point in
	 * the unicode standard. A radical can have more than one representation in
	 * unicode standard, the main ones are generally in plane 1.
	 * 
	 * @return the main unicode code point of this radical variant
	 * @see #getKangxiUnicode()
	 */
	public Integer getUnicode() {
		return unicode;
	}

	/**
	 * Returns the radical variant's character secondary representation code
	 * point in the unicode standard. A radical can have more than one
	 * representation in unicode standard, the secondary ones can be either in
	 * plane 1 or 2
	 * 
	 * @return the secondary unicode code point of this radical variant
	 * @see #getUnicode()
	 */
	public Integer getKangxiUnicode() {
		return kangxiUnicode;
	}

	/**
	 * 
	 * @return the type of this radical variant
	 * @see RadicalTypeEnum
	 */
	public RadicalTypeEnum getRadicalType() {
		return radicalType;
	}

	/**
	 * Returns this radical's index number in classic radical list.
	 * 
	 * @return an {@code Integer} representing this radical variant number
	 * @see RadicalVariantTag#getAuxNumber()
	 */
	public Integer getNumber() {
		return radicalNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Radical ");
		builder.append(radicalNumber);
		if (auxNumber != null) {
			builder.append(".");
			builder.append(auxNumber);
		}

		builder.append(" [kangxiUnicode=");
		builder.append(String.format("%x", kangxiUnicode));
		builder.append(", names=");
		builder.append(names);
		builder.append(", radicalType=");
		builder.append(radicalType);
		builder.append(", unicode=");
		builder.append(String.format("%x", unicode));
		builder.append(", unicodeName=");
		builder.append(unicodeName);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(RadicalVariantTag o) {

		int compare = radicalNumber.compareTo(o.getNumber());

		if (compare == 0 && auxNumber != null && o.getAuxNumber() != null)
			compare = auxNumber.compareTo(o.getAuxNumber());

		return compare;
	}

}
