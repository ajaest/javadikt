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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ajaest.jdk.data.auxi.KanjiReference;
import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.auxi.KanjiStrokeClue;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

/**
 * Class that represents the graph info of a kanji.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiGraph implements KanjiReference, Comparable<KanjiGraph>, Serializable {

	private static final long serialVersionUID = 3496003178888546250L;
	// WARNING: changing field names could cause neodatis database problems.
	private final List<KanjiStroke> strokes;
	private Set<KanjiStrokeClue> strokesClues;
	private final Integer unicodeRef;

	public KanjiGraph(List<KanjiStroke> strokes, Set<KanjiStrokeClue> strokesClues, Integer unicodeRef) {
		super();
		if (unicodeRef == null)
			throw new IllegalArgumentException("unicode reference is null");
		if (strokes == null)
			throw new IllegalArgumentException("stroke list is null");

		this.unicodeRef = unicodeRef;
		this.strokes = strokes;
		if (strokesClues == null) {
			this.strokesClues = new HashSet<KanjiStrokeClue>();
		} else {
			this.strokesClues = strokesClues;

		}
	}

	public KanjiGraph(String strokes, Set<KanjiStrokeClue> strokesClues, Integer unicodeRef) {
		super();
		if (unicodeRef == null)
			throw new IllegalArgumentException("unicode reference is null");

		// first character to lower case
		char[] strokeChars = strokes.toCharArray();
		if (strokeChars.length > 0)
			strokeChars[0] = Character.toLowerCase(strokeChars[0]);

		List<KanjiStroke> kss = new ArrayList<KanjiStroke>();
		List<AllowedStrokeLineEnum> currentStroke = new ArrayList<AllowedStrokeLineEnum>();
		for (char c : strokeChars) {

			if (Character.isUpperCase(c)) {
				kss.add(new KanjiStroke(currentStroke, kss.size() + 1, unicodeRef));
				currentStroke = new ArrayList<AllowedStrokeLineEnum>();
			}

			currentStroke.add(AllowedStrokeLineEnum.valueOf(new String(new char[]{Character.toUpperCase(c)})));
		}

		if (currentStroke.size() > 0) {
			kss.add(new KanjiStroke(currentStroke, kss.size() + 1, unicodeRef));
		}

		this.unicodeRef = unicodeRef;
		this.strokes = kss;
		if (strokesClues == null) {
			this.strokesClues = new HashSet<KanjiStrokeClue>();
		} else {
			this.strokesClues = strokesClues;

		}
	}

	@Override
	public int compareTo(KanjiGraph o) {

		int compare = this.strokeCount().compareTo(o.strokeCount());

		if (compare == 0) {
			for (int i = 0; i < this.strokeCount(); i++) {
				compare = this.getStrokes().get(i).compareTo(o.getStrokes().get(i));
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
		KanjiGraph other = (KanjiGraph) obj;
		if (this.strokes == null) {
			if (other.strokes != null)
				return false;
		} else if (!this.strokes.equals(other.strokes))
			return false;
		return true;
	}

	public List<AllowedStrokeLineEnum> getLineSequence() {

		List<AllowedStrokeLineEnum> sequence = new ArrayList<AllowedStrokeLineEnum>();

		for (KanjiStroke ks : this.getStrokes()) {
			for (AllowedStrokeLineEnum asl : ks.getStrokeList()) {

				sequence.add(asl);
			}
		}

		return sequence;
	}

	/**
	 * 
	 * @return The sorted list of strokes of the kanji
	 */
	public List<KanjiStroke> getStrokes() {
		return this.strokes;
	}

	/**
	 * 
	 * @return The list of spacial relacions between the strokes
	 */
	public Set<KanjiStrokeClue> getStrokesClues() {
		return this.strokesClues;
	}

	@Override
	public Integer getUnicodeRef() {
		return this.unicodeRef;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.strokes == null) ? 0 : this.strokes.hashCode());
		return result;
	}

	// TODO: add to diagram
	public Integer strokeCount() {
		return this.strokes.size();
	}

	public String toAsteriskString() {

		StringBuilder sb = new StringBuilder();

		for (KanjiStroke st : this.strokes)
			sb.append(st.toString());

		return sb.toString();
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append(this.toAsteriskString());

		for (KanjiStrokeClue ksc : this.strokesClues) {
			sb.append(", ");
			sb.append(ksc.toString());
		}

		return sb.toString();

	}
}
