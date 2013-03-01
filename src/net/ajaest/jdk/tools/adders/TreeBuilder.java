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

package net.ajaest.jdk.tools.adders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.JISPair;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.jdk.data.kanji.VariantPair;
import net.ajaest.lib.data.SequenceEnumTree;
import net.ajaest.lib.data.SequenceTree;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

//TODO: JavaDoc
public class TreeBuilder {

	public static TreeMap<Integer, Set<Integer>> buildKanjiClassicRadicalTree(Set<Kanji> kanjis) {

		TreeMap<Integer, Set<Integer>> map = new TreeMap<Integer, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getClassicRadical() != null) {
				elems = map.get(k.getClassicRadical().getNumber());
				if (elems == null) {
					elems = new HashSet<Integer>();
					map.put(k.getClassicRadical().getNumber(), elems);
				}
				elems.add(k.getUnicodeRef());
			}
		}

		return map;
	}

	public static SequenceTree<Character, Integer> buildKanjiDeRooCodeTree(Set<Kanji> kanjis) {

		SequenceTree<Character, Integer> deRooTree = new SequenceTree<Character, Integer>(false);

		List<Character> seq = new ArrayList<Character>();
		char[] seqStr;
		for (Kanji k : kanjis) {
			if ((k.getQueryCodes() != null) && (k.getQueryCodes().getDeRooCode() != null)) {
				{
					seqStr = k.getQueryCodes().getDeRooCode().toCharArray();
					for (char c : seqStr)
						seq.add(new Character(c));
					deRooTree.add(seq, k.getUnicodeRef());
					seq = new ArrayList<Character>();
				}
			}
		}

		return deRooTree;
	}

	public static HashMap<String, Set<Integer>> buildKanjiDicNameTree(Set<Kanji> kanjis) {

		HashMap<String, Set<Integer>> map = new HashMap<String, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getDicReferences() != null) {
				for (DicReferencePair vp : k.getDicReferences()) {
					elems = map.get(vp.getFirst());
					if (elems == null) {
						elems = new HashSet<Integer>();
						map.put(vp.getFirst(), elems);
					}
					elems.add(k.getUnicodeRef());
				}
			}
		}

		return map;
	}

	/**
	 * [Dic_ref(any)] -> [Map[[Dic_name->Dic_ref(Dic_name)]<br>
	 * <br>
	 * To get a concrete dictionary reference list(many dictionaries has more
	 * than one reference per index: map.get(reference):<br>
	 * <br> {@code Collection<Integer> indexList = map.get(index).get(dicName)}<br>
	 * <br>
	 * To get all the references no matter the dic<br>
	 * <br> {@code Collection<Integer> indexList =
	 * CollectionsExtra.plainToList(map.get(index).values)}<br>
	 * <br>
	 * To get a sorted dic's reference list <br>
	 * <br> {@code List<Integer> refList;}<br> {@code for(HashMap<Integer> refs :
	 * map.values())}<br>
	 * &nbsp &nbsp &nbsp {@code
	 * reflist.addAll(refs.get(dicName);//be careful with null maps!}
	 * 
	 * 
	 */
	public static TreeMap<Integer, HashMap<String, Set<Integer>>> buildKanjiDicReferenceTree(Set<Kanji> kanjis) {

		TreeMap<Integer, HashMap<String, Set<Integer>>> map = new TreeMap<Integer, HashMap<String, Set<Integer>>>();

		Set<Integer> elems;
		HashMap<String, Set<Integer>> dicMap;
		for (Kanji k : kanjis) {
			if (k.getDicReferences() != null) {
				for (DicReferencePair drp : k.getDicReferences()) {
					if (drp.getNumReference() != null) {
						dicMap = map.get(drp.getNumReference());
						if (dicMap == null) {
							dicMap = new HashMap<String, Set<Integer>>();
							map.put(drp.getNumReference(), dicMap);
						}
						elems = dicMap.get(drp.getFirst());
						if (elems == null) {
							elems = new HashSet<Integer>();
							dicMap.put(drp.getFirst(), elems);
						}
						elems.add(k.getUnicodeRef());
					}
				}
			}
		}

		return map;
	}

	public static SequenceTree<Character, Integer> buildKanjiFourCornerCodeTree(Set<Kanji> kanjis) {

		SequenceTree<Character, Integer> fourCornerTree = new SequenceTree<Character, Integer>(false);

		List<Character> seq = new ArrayList<Character>();
		char[] seqStr;
		for (Kanji k : kanjis) {
			if ((k.getQueryCodes() != null) && (k.getQueryCodes().getFourCornerCode() != null)) {
				{
					seqStr = k.getQueryCodes().getFourCornerCode().toCharArray();
					for (char c : seqStr)
						seq.add(new Character(c));
					fourCornerTree.add(seq, k.getUnicodeRef());
					seq = new ArrayList<Character>();
				}
			}
		}

		return fourCornerTree;
	}

	public static TreeMap<Integer, Integer> buildKanjiFrequencyTreeSet(Set<Kanji> kanjis) {

		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();

		for (Kanji k : kanjis) {
			if (k.getFrequency() != null) {
				map.put(k.getFrequency(), k.getUnicodeRef());
			}
		}

		return map;
	}

	public static TreeMap<Integer, Set<Integer>> buildKanjiGradeTree(Set<Kanji> kanjis) {

		TreeMap<Integer, Set<Integer>> map = new TreeMap<Integer, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getGrade() != null) {
				elems = map.get(k.getGrade());
				if (elems == null) {
					elems = new HashSet<Integer>();
					map.put(k.getGrade(), elems);
				}
				elems.add(k.getUnicodeRef());
			}
		}

		return map;
	}

	public static SequenceTree<AllowedStrokeLineEnum, Integer> buildKanjiGraphTree(Set<Kanji> kanjis) throws IOException {

		SequenceEnumTree<AllowedStrokeLineEnum, Integer> graphTree;

		graphTree = new SequenceEnumTree<AllowedStrokeLineEnum, Integer>(false, AllowedStrokeLineEnum.class);

		for (Kanji k : kanjis) {
			if (k.getGraph() != null)
				graphTree.add(k.getGraph().getLineSequence(), k.getUnicodeRef());
		}

		return graphTree;
	}

	public static HashMap<String, Set<Integer>> buildKanjiJisCharset(Set<Kanji> kanjis) {

		HashMap<String, Set<Integer>> map = new HashMap<String, Set<Integer>>();

		map.put(JISPair.JIS_X_208, new HashSet<Integer>());
		map.put(JISPair.JIS_X_212, new HashSet<Integer>());
		map.put(JISPair.JIS_X_213, new HashSet<Integer>());

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			elems = map.get(k.getJisCode().getFirst());
			elems.add(k.getUnicodeRef());
		}

		return map;
	}

	public static SequenceTree<Integer, Integer> buildKanjiJisCodeTree(Set<Kanji> kanjis) {

		SequenceTree<Integer, Integer> jisCodeTree = new SequenceTree<Integer, Integer>(false);

		List<Integer> seq = new ArrayList<Integer>();
		String[] seqStr;
		for (Kanji k : kanjis) {

			seqStr = k.getJisCode().getSecond().split("-");

			if (seqStr.length == 2) // JIS plane 1
				seq.add(1);

			for (String s : seqStr)
				seq.add(Integer.valueOf(s));

			jisCodeTree.add(seq, k.getUnicodeRef());

			seq = new ArrayList<Integer>();
		}

		return jisCodeTree;
	}

	public static TreeMap<Integer, Set<Integer>> buildKanjiJLPTLevelTree(Set<Kanji> kanjis) {

		TreeMap<Integer, Set<Integer>> map = new TreeMap<Integer, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getJLPTLevel() != null) {

				elems = map.get(k.getJLPTLevel());
				if (elems == null) {
					elems = new HashSet<Integer>();
					map.put(k.getJLPTLevel(), elems);
				}
				elems.add(k.getUnicodeRef());
			}
		}

		return map;
	}

	public static HashMap<String, Set<Integer>> buildKanjiMeaningLangTree(Set<Kanji> kanjis) {

		HashMap<String, Set<Integer>> map = new HashMap<String, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getMeanings() != null) {
				for (MeaningEntry vp : k.getMeanings()) {
					elems = map.get(vp.getKey());
					if (elems == null) {
						elems = new HashSet<Integer>();
						map.put(vp.getKey(), elems);
					}
					elems.add(k.getUnicodeRef());
				}
			}
		}

		return map;
	}

	/**
	 * Search a meaning in any language(always lowercase):<br>
	 * <br> {@code List<Integer> refs = tree.search("meaning");}<br>
	 * <br>
	 * Search a meaning in the language ES(always in iso639.1 and lowecase)<br>
	 * <br> {@code List<Integer> refs = tree.search("meaning" + "#es");}<br>
	 * <br>
	 */
	public static SequenceTree<Character, Integer> buildKanjiMeaningTree(Set<Kanji> kanjis) {

		SequenceTree<Character, Integer> meaningTree = new SequenceTree<Character, Integer>(false);

		List<Character> seq = new ArrayList<Character>();
		char[] seqStr;
		for (Kanji k : kanjis) {
			if (k.getMeanings() != null) {
				for (MeaningEntry me : k.getMeanings()) {
					for (String read : me.getElements()) {
						seqStr = read.toCharArray();
						for (char c : seqStr)
							seq.add(new Character(c));
						meaningTree.add(seq, k.getUnicodeRef()); // adds search
						// for all
						// languages
						seqStr = me.getKey().toLowerCase().toCharArray();
						seq.add('#');
						for (char c : seqStr)
							seq.add(new Character(c));
						meaningTree.add(seq, k.getUnicodeRef()); // adds search
						// for a
						// language
						seq = new ArrayList<Character>();
					}
				}
			}
		}

		return meaningTree;
	}

	public static TreeMap<Integer, Set<Integer>> buildKanjiNelsonRadicalTree(Set<Kanji> kanjis) {

		TreeMap<Integer, Set<Integer>> map = new TreeMap<Integer, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getNelsonRadical() != null) {
				elems = map.get(k.getNelsonRadical());
				if (elems == null) {
					elems = new HashSet<Integer>();
					map.put(k.getNelsonRadical(), elems);
				}
				elems.add(k.getUnicodeRef());
			}
		}

		return map;
	}

	/**
	 * Search reading of any reading type:<br>
	 * <br> {@code List<Integer> refs = tree.search("reading");}<br>
	 * <br>
	 * Search a reading of a particual reading type:<br>
	 * <br> {@code List<Integer> refs = tree.search("reading" + "#readingtype");}<br>
	 * <br>
	 */
	public static SequenceTree<Character, Integer> buildKanjiReadingTree(Set<Kanji> kanjis) {

		SequenceTree<Character, Integer> variantTree = new SequenceTree<Character, Integer>(false);

		List<Character> seq = new ArrayList<Character>();
		char[] seqStr;
		for (Kanji k : kanjis) {
			if (k.getReadings() != null) {
				for (ReadingEntry vp : k.getReadings()) {
					for (String read : vp.getElements()) {
						seqStr = read.toCharArray();
						for (char c : seqStr)
							seq.add(new Character(c));
						variantTree.add(seq, k.getUnicodeRef()); // adds plain
						// reading
						seq.add('#');
						seqStr = vp.getKey().toCharArray();
						for (char c : seqStr)
							seq.add(new Character(c));
						variantTree.add(seq, k.getUnicodeRef());// adds reading
						// and reading
						// type
						seq = new ArrayList<Character>();
					}
				}
			}
		}

		return variantTree;
	}

	public static HashMap<String, Set<Integer>> buildKanjiReadingTypeTree(Set<Kanji> kanjis) {

		HashMap<String, Set<Integer>> map = new HashMap<String, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getReadings() != null) {
				for (ReadingEntry vp : k.getReadings()) {
					elems = map.get(vp.getKey());
					if (elems == null) {
						elems = new HashSet<Integer>();
						map.put(vp.getKey(), elems);
					}
					elems.add(k.getUnicodeRef());
				}
			}
		}

		return map;
	}

	public static SequenceTree<Integer, Integer> buildKanjiSKIPCodeTree(Set<Kanji> kanjis) {

		SequenceTree<Integer, Integer> SKIPCodeTree = new SequenceTree<Integer, Integer>(false);

		List<Integer> seq = new ArrayList<Integer>();
		String[] seqStr;
		for (Kanji k : kanjis) {
			if ((k.getQueryCodes() != null) && (k.getQueryCodes().getSkipCode() != null)) {

				seqStr = k.getQueryCodes().getSkipCode().split("-");
				for (String s : seqStr)
					seq.add(Integer.valueOf(s));

				SKIPCodeTree.add(seq, k.getUnicodeRef());

				seq = new ArrayList<Integer>();
			}
		}

		return SKIPCodeTree;
	}

	public static SequenceTree<Character, Integer> buildKanjiSpahnHadamitzkyCodeTree(Set<Kanji> kanjis) {

		SequenceTree<Character, Integer> spahnHadamitzkyTree = new SequenceTree<Character, Integer>(false);

		List<Character> seq = new ArrayList<Character>();
		char[] seqStr;
		for (Kanji k : kanjis) {
			if ((k.getQueryCodes() != null) && (k.getQueryCodes().getSpahnHadamitzkyCode() != null)) {
				{
					seqStr = k.getQueryCodes().getSpahnHadamitzkyCode().toCharArray();
					for (char c : seqStr)
						seq.add(new Character(c));
					spahnHadamitzkyTree.add(seq, k.getUnicodeRef());
					seq = new ArrayList<Character>();
				}
			}
		}

		return spahnHadamitzkyTree;
	}

	public static TreeMap<Integer, Set<Integer>> buildKanjiStrokeCountTree(Set<Kanji> kanjis) {

		TreeMap<Integer, Set<Integer>> map = new TreeMap<Integer, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getStrokeCount() != null) {
				elems = map.get(k.getStrokeCount());
				if (elems == null) {
					elems = new HashSet<Integer>();
					map.put(k.getStrokeCount(), elems);
				}
				elems.add(k.getUnicodeRef());
			}
		}

		return map;
	}

	public static TreeMap<Integer, Set<Integer>> buildKanjiStrokeMiscountsTree(Set<Kanji> kanjis) {

		TreeMap<Integer, Set<Integer>> map = new TreeMap<Integer, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getStrokeMiscounts() != null) {
				for (Integer strMscnt : k.getStrokeMiscounts()) {
					elems = map.get(strMscnt);
					if (elems == null) {
						elems = new HashSet<Integer>();
						map.put(strMscnt, elems);
					}
					elems.add(k.getUnicodeRef());
				}
			}
		}

		return map;
	}

	public static TreeSet<Integer> buildKanjiUnicodeTree(Set<Kanji> kanjis) {

		TreeSet<Integer> unicodeMap = new TreeSet<Integer>();

		for (Kanji kt : kanjis) {
			unicodeMap.add(kt.getUnicodeRef());
		}

		return unicodeMap;
	}

	/**
	 * Search variant of any variant type:<br>
	 * <br> {@code List<Integer> refs = tree.search("variant");}<br>
	 * <br>
	 * Search a variant of a particular variant type:<br>
	 * <br> {@code List<Integer> refs = tree.search("variant" + "#variantType");}<br>
	 * <br>
	 */
	public static SequenceTree<Character, Integer> buildKanjiVariantTree(Set<Kanji> kanjis) {

		SequenceTree<Character, Integer> variantTree = new SequenceTree<Character, Integer>(false);

		List<Character> seq = new ArrayList<Character>();
		char[] seqStr;
		for (Kanji k : kanjis) {
			if (k.getVariants() != null) {
				for (VariantPair vp : k.getVariants()) {
					seqStr = vp.getSecond().toCharArray();
					for (char c : seqStr)
						seq.add(new Character(c));
					variantTree.add(seq, k.getUnicodeRef()); // adds variant of
					// any type
					seq.add('#');
					seqStr = vp.getFirst().toCharArray();
					for (char c : seqStr)
						seq.add(new Character(c));
					variantTree.add(seq, k.getUnicodeRef());// adds variant an
					// specific type
					seq = new ArrayList<Character>();
				}
			}
		}

		return variantTree;
	}

	public static HashMap<String, Set<Integer>> buildKanjiVariantTypeTree(Set<Kanji> kanjis) {

		HashMap<String, Set<Integer>> map = new HashMap<String, Set<Integer>>();

		Set<Integer> elems;
		for (Kanji k : kanjis) {
			if (k.getVariants() != null) {
				for (VariantPair vp : k.getVariants()) {
					elems = map.get(vp.getFirst());
					if (elems == null) {
						elems = new HashSet<Integer>();
						map.put(vp.getFirst(), elems);
					}
					elems.add(k.getUnicodeRef());
				}
			}
		}

		return map;
	}
}
