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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ajaest.jdk.data.auxi.KanjiEnums.RadicalTypeEnum;

/**
 * Immutable class that stores information about a radical. Because the only
 * attribute shared between radical's variants are the radical number, further
 * information about the radical must be searched in the specific variant.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class RadicalTag implements Comparable<RadicalTag> {

	protected Integer radicalNumber;
	
	protected List<RadicalVariantTag> radsVars;

	protected Map<RadicalTypeEnum, Set<RadicalVariantTag>> radsTypes;

	protected RadicalTag(Integer number) {
		this.radicalNumber = number;
		radsVars = new ArrayList<RadicalVariantTag>();
		
		// FIXME: EnumMap is much faster than HashMap, but Neodatis confuses it with
		// a HashMap and crashes. Bug reported.
		//radsTypes = new EnumMap<RadicalTypeEnum, Set<RadicalVariantTag>>(RadicalTypeEnum.class);
		radsTypes = new HashMap<RadicalTypeEnum, Set<RadicalVariantTag>>();
	}

	/**
	 * Returns the radical indexing number in classic radical list.
	 * 
	 * @return and integer representing the number of this radical
	 */
	public Integer getNumber() {
		return radicalNumber;
	}

	/**
	 * Returns the radical variant specified by it's radical variant number.
	 * 
	 * @param variantNumber
	 *            the variant number of the radical variant to be retrieved
	 * @return this radical's specified variant
	 */
	public RadicalVariantTag getRadicalVariant(Integer variantNumber) {
		
		RadicalVariantTag rad = null;
		
		if(radsVars.size()>variantNumber)
			rad = radsVars.get(variantNumber - 1);
		
		return rad;
	}

	/**
	 * Returns this radical's variants specified by it's type
	 * 
	 * @param rte
	 *            the radical type of the radical variant to be retrieved
	 * @return this radical's variants which are of the specified type
	 */
	public Set<RadicalVariantTag> getRadicalVariant(RadicalTypeEnum rte) {

		return Collections.unmodifiableSet(radsTypes.get(rte));
	}

	/**
	 * Returns all the variants of this radical.
	 * 
	 * @return all the variants of this radical.
	 */
	public List<RadicalVariantTag> getVariants() {

		return Collections.unmodifiableList(radsVars);
	}

	@Override
	public int compareTo(RadicalTag other) {

		return this.getNumber().compareTo(other.getNumber());
	}
	
	@Override
	public String toString() {

		Integer firstUnicode = null;

		for (RadicalVariantTag r : getVariants()) {
			if (r.getUnicode() != null) {
				firstUnicode = r.getUnicode();
				break;
			}
		}

		return firstUnicode != null ? new String(Character.toChars(firstUnicode)) : "[radiclNumber=" + radicalNumber + "]";
	}

}
