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

import java.util.HashSet;
import java.util.Set;

/**
 * Mutable class that stores some different informations about a kanji
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class Kanji extends KanjiTag {

	private static final long serialVersionUID = -7855221431012833264L;

	/**
	 * Constructs a kanji extracting the code point of the stored {@code char}
	 * in the position 0 of the {@code char} array. It work with surrogates.
	 * 
	 * @param c
	 *            {@code char} representation of a kanji.
	 * @throws IllegalArgumentException
	 *             If the
	 */
	public Kanji(char[] c) {

		Integer unicode = Character.codePointAt(c, 0);

		if (unicode == null || !isKanji(unicode))
			throw new IllegalArgumentException("It is not unicode");

		this.unicode = unicode;

		this.strokeMiscounts = new HashSet<Integer>();
		this.variants = new HashSet<VariantPair>();
		this.dicReferences = new HashSet<DicReferencePair>();
		this.readings = new HashSet<ReadingEntry>();
		this.meanings = new HashSet<MeaningEntry>();

		this.queryCodes = new KanjiQueryCodes(null, null, null, null, this.unicode);

	}

	/**
	 * Constructs a kanji from a character representation of a kanji. It does
	 * not support surrogation.
	 * 
	 * @param c
	 *            kanji character
	 */
	public Kanji(Character c) {
		this(c.charValue());
	}

	/**
	 * Constructs a kanji by his unicode value, that will be used as primary
	 * key.
	 */
	public Kanji(int unicodeRef) {
		this(new Integer(unicodeRef));
	}

	/**
	 * Constructs a kanji by his unicode value, that will be used as primary
	 * key.
	 */
	public Kanji(Integer unicodeRef) {

		if (unicodeRef == null || !isKanji(unicodeRef))
			throw new IllegalArgumentException("It is not unicode");

		this.unicode = unicodeRef;

		this.strokeMiscounts = new HashSet<Integer>();
		this.variants = new HashSet<VariantPair>();
		this.dicReferences = new HashSet<DicReferencePair>();
		this.readings = new HashSet<ReadingEntry>();
		this.meanings = new HashSet<MeaningEntry>();

		this.queryCodes = new KanjiQueryCodes(null, null, null, null, this.unicode);

	}

	/**
	 * The returned list is not immutable like in KanjiTag
	 */
	@Override
	public Set<DicReferencePair> getDicReferences() {
		return super.dicReferences;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public void setClassicRadical(RadicalTag rad) {
		this.classicRad = rad;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public void setGraph(KanjiGraph graph) {
		this.graph = graph;
	}

	public void setJisCode(JISPair jisCode) {
		this.jisCode = jisCode;
	}

	public void setJLPTLevel(Integer jLPTLevel) {
		JLPTLevel = jLPTLevel;
	}

	/**
	 * The returned list is not immutable like in KanjiTag
	 */
	@Override
	public Set<MeaningEntry> getMeanings() {
		return super.meanings;
	}

	public void setNelsonRadical(Integer nelsonRadical) {
		this.nelsonRadical = nelsonRadical;
	}

	public void setQueryCodes(KanjiQueryCodes queryCodes) {
		this.queryCodes = queryCodes;
	}

	@Override
	public Set<ReadingEntry> getReadings() {
		return super.readings;
	}

	public void setStrokeCount(Integer strokeCount) {
		this.strokeCount = strokeCount;
	}

	/**
	 * The returned list is not immutable like in KanjiTag
	 */
	@Override
	public Set<Integer> getStrokeMiscounts() {
		return super.strokeMiscounts;
	}

	/**
	 * The returned list is not immutable like in KanjiTag
	 */
	@Override
	public Set<VariantPair> getVariants() {
		return super.variants;
	}

	public void setStrokeMiscounts(Set<Integer> strokeMis) {
		super.strokeMiscounts = strokeMis;
	}

	public void setDicReferences(Set<DicReferencePair> drp) {
		super.dicReferences = drp;
	}

	public void setReadings(Set<ReadingEntry> res) {
		super.readings = res;
	}

	public void setMeanings(Set<MeaningEntry> men) {
		super.meanings = men;

	}

	public void setVariants(Set<VariantPair> var) {
		super.variants = var;
	}

}
