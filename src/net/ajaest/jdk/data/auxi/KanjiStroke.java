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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;


/**
 * Generic inmutable class that represents a kanji stroke.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiStroke implements KanjiReference, Comparable<KanjiStroke>, Serializable {

	private static final long serialVersionUID = -4780530081773979168L;

	private final Integer unicodeRef;

	private final List<AllowedStrokeLineEnum> strokeList;

	private final Integer order;

	public KanjiStroke(List<AllowedStrokeLineEnum> strokeList, Integer order, Integer unicodeRef) {
		this.order = order;
		this.strokeList = strokeList;
		this.unicodeRef = unicodeRef;
	}

	public KanjiStroke(String strokeString, Integer order, Integer unicodeRef) {

		List<AllowedStrokeLineEnum> asl = new ArrayList<AllowedStrokeLineEnum>();

		for (char line : strokeString.toCharArray()) {

			asl.add(AllowedStrokeLineEnum.valueOf(new String(new char[]{line}).toUpperCase()));
		}

		this.strokeList = asl;

		this.order = order;

		this.unicodeRef = unicodeRef;
	}

	@Override
	public int compareTo(KanjiStroke o) {

		int compare = new Integer(this.getStrokeList().size()).compareTo(o.getStrokeList().size());

		if (compare == 0) {
			for (int i = 0; i < this.getStrokeList().size(); i++) {
				compare = this.getStrokeList().get(i).toString().compareTo(o.getStrokeList().get(i).toString());
				if (compare != 0)
					break;
			}

		}

		return compare;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		KanjiStroke other = (KanjiStroke) obj;
		if (this.strokeList == null) {
			if (other.strokeList != null)
				return false;
		} else if (!this.strokeList.equals(other.strokeList))
			return false;
		return true;
	}

	public Integer getOrder() {
		return this.order;
	}

	/**
	 * 
	 * @return The sorted list of lines that compounds the stroke
	 */
	public List<AllowedStrokeLineEnum> getStrokeList() {
		return Collections.unmodifiableList(this.strokeList);
	}

	@Override
	public Integer getUnicodeRef() {

		return this.unicodeRef;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.strokeList == null) ? 0 : this.strokeList.hashCode());
		return result;
	}

	@Override
	public String toString() {

		StringBuilder ts = new StringBuilder();

		Iterator<AllowedStrokeLineEnum> it = this.strokeList.iterator();

		if (it.hasNext())
			ts.append(it.next().toString());
		while (it.hasNext())
			ts.append(it.next().toString().toLowerCase());

		return ts.toString();
	}
}
