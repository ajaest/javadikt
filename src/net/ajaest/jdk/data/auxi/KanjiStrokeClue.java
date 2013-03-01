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

import net.ajaest.lib.data.Pair;

/**
 * Immutable class that stores a spatial relation between two strokes of a
 * kanji.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiStrokeClue extends Pair<Integer, Integer> implements KanjiReference, Serializable {

	private static final long serialVersionUID = -1541008924357663515L;
	private AllowedStrokePointEnum firstStrokePoint;
	private AllowedStrokePointEnum secondStrokePoint;
	private AllowedStrokeClueEnum strokeClue;
	private Integer unicodeRef;

	/**
	 * @param first
	 *            the order of the first stroke
	 * @param second
	 *            the order of the second stroke
	 * @param firstStrokePoint
	 *            the point of the first stroke where the relation is made
	 * @param secondStrokePoint
	 *            the point of the second stroke where the relation is made
	 * @param strokeClue
	 *            the relation between two strokes
	 * @param unicodeRef
	 *            the unicode value of the referenced kanji
	 */
	public KanjiStrokeClue(Integer first, Integer second, AllowedStrokePointEnum firstStrokePoint, AllowedStrokePointEnum secondStrokePoint, AllowedStrokeClueEnum strokeClue, Integer unicodeRef) {
		super(first, second);
		this.firstStrokePoint = firstStrokePoint;
		this.secondStrokePoint = secondStrokePoint;
		this.strokeClue = strokeClue;
		this.unicodeRef = unicodeRef;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		KanjiStrokeClue other = (KanjiStrokeClue) obj;
		if (firstStrokePoint == null) {
			if (other.firstStrokePoint != null)
				return false;
		} else if (!firstStrokePoint.equals(other.firstStrokePoint))
			return false;
		if (secondStrokePoint == null) {
			if (other.secondStrokePoint != null)
				return false;
		} else if (!secondStrokePoint.equals(other.secondStrokePoint))
			return false;
		if (strokeClue == null) {
			if (other.strokeClue != null)
				return false;
		} else if (!strokeClue.equals(other.strokeClue))
			return false;
		return true;
	}

	/**
	 * 
	 * @return The point of the first stroke where the relation is made
	 */
	public AllowedStrokePointEnum getFirstStrokePoint() {

		return firstStrokePoint;
	}

	/**
	 * 
	 * @return The point of the second stroke where the relation is made
	 */
	public AllowedStrokePointEnum getSecondStrokePoint() {
		return secondStrokePoint;
	}

	/**
	 * 
	 * @return The relation between two strokes
	 */
	public AllowedStrokeClueEnum getStrokeClue() {
		return strokeClue;
	}

	@Override
	public Integer getUnicodeRef() {

		return unicodeRef;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((firstStrokePoint == null) ? 0 : firstStrokePoint.hashCode());
		result = prime * result + ((secondStrokePoint == null) ? 0 : secondStrokePoint.hashCode());
		result = prime * result + ((strokeClue == null) ? 0 : strokeClue.hashCode());
		return result;
	}

	/**
	 * The object it's intended to be inmutable, so it will always throw {@code
	 * UnsupportedOperationException}
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void setFirst(Integer first) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The object it's intended to be inmutable, so it will always throw {@code
	 * UnsupportedOperationException}
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void setSecond(Integer second) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		switch (strokeClue) {

			case isAtLeftOf :
			case isBelowOf :
			case isLongerThan :
				sb.append("The ");
				sb.append(firstStrokePoint.toString());
				sb.append(" of stroke ");
				sb.append(getFirst().toString());
				sb.append(" ");
				sb.append(strokeClue.toString());
				sb.append(" the ");
				sb.append(secondStrokePoint.toString());
				sb.append(" of stroke ");
				sb.append(getSecond());
				break;

			case areIntheSamePoint :
				sb.append("The ");
				sb.append(firstStrokePoint.toString());
				sb.append(" of stroke ");
				sb.append(getFirst().toString());
				sb.append(" and the ");
				sb.append(secondStrokePoint.toString());
				sb.append(" of stroke ");
				sb.append(getSecond());
				sb.append(" ");
				sb.append(strokeClue.toString());
				break;

			case fromToSmallerThanHalfGridSize :
				sb.append("From the ");
				sb.append(firstStrokePoint.toString());
				sb.append(" of stroke ");
				sb.append(getFirst().toString());
				sb.append(" to the ");
				sb.append(secondStrokePoint.toString());
				sb.append(" of stroke ");
				sb.append(getSecond());
				sb.append(" lenghtIsSmallerThanHalfGrid");
				break;
			default :
		}

		return sb.toString();
	}
}
