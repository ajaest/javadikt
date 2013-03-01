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
public class RadicalVariant extends RadicalVariantTag {
	
	public RadicalVariant(Integer radicalNumber) {
		super(radicalNumber);
	}
	
	public void setAuxNumber(Integer auxNumber) {
		super.auxNumber = auxNumber;
	}

	public void setNumber(Integer radicalNumber) {
		super.radicalNumber = radicalNumber;
	}

	public void setUnicodeName(String unicodeName) {
		super.unicodeName = unicodeName;
	}

	public void setNames(List<String> names) {
		super.names = names;
	}

	public void setUnicode(Integer unicode) {
		super.unicode = unicode;
	}

	public void setKangxiUnicode(Integer kangxiUnicode) {
		super.kangxiUnicode = kangxiUnicode;
	}

	public void setRadicalType(RadicalTypeEnum radicalType) {
		super.radicalType = radicalType;
	}

	/**
	 * The returned list is not immutable like in RadicalTag
	 */
	@Override
	public List<String> getNames() {
		return super.names;
	}

	/**
	 * This enum describes the radical variant enum type according it's position
	 * natural position in the kanji. "Original" means that the radical it's a
	 * kanji itself.
	 * 
	 * @author Luis Alfonso Arce González
	 * 
	 */
}

