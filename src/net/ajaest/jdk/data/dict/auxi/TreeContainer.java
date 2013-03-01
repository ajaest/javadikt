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

package net.ajaest.jdk.data.dict.auxi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipFile;

import net.ajaest.jdk.core.main.ExceptionHandler;
import net.ajaest.jdk.core.main.ExceptionHandler.KNOWN_EXCEPTIONS;
import net.ajaest.jdk.data.dict.query.IndexPairQAbout;
import net.ajaest.jdk.data.dict.query.IntegerValueQAbout;
import net.ajaest.jdk.data.dict.query.StringPairQAbout;
import net.ajaest.jdk.data.dict.query.StringValueQAbout;
import net.ajaest.jdk.data.dict.query.ValueQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.KanjiGraphQAbout;
import net.ajaest.lib.data.SequenceTree;
import net.ajaest.lib.string.Strings;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

//TODO: JavaDoc
public class TreeContainer {


	// trees
	private TreeSet<Integer> 									kanjiUnicodeTree;

	private SequenceTree<AllowedStrokeLineEnum, Integer> 		kanjiGraphTree;
	private SequenceTree<Integer, Integer> 						kanjiJisCodeTree; // 1
	private SequenceTree<Integer, Integer> 						kanjiSKIPCodeTree;
	private SequenceTree<Character, Integer> 					kanjiDeRooCodeTree;
	private SequenceTree<Character, Integer> 					kanjiFourCornerTree;
	private SequenceTree<Character, Integer> 					kanjiMeaningTree; // 2
	private SequenceTree<Character, Integer> 					kanjiReadingTree; // 3
	private SequenceTree<Character, Integer> 					kanjiSpahnHadamitzkyCodeTree;
	private SequenceTree<Character, Integer> 					kanjiVariantTree; // 4

	private HashMap<String, Set<Integer>> 						kanjiJisCharset; // 1
	private HashMap<String, Set<Integer>> 						kanjiDicNameTree; // 5
	private HashMap<String, Set<Integer>> 						kanjiMeaningLangTree; // 2
	private HashMap<String, Set<Integer>> 						kanjiReadingTypeTree; // 3
	private HashMap<String, Set<Integer>> 						kanjiVariantTypeTree; // 4

	private TreeMap<Integer, Integer> 							kanjiFrequencyTreeSet;
	private TreeMap<Integer, Set<Integer>> 						kanjiClassicRadicalTree;
	private TreeMap<Integer, Set<Integer>> 						kanjiGradeTree;
	private TreeMap<Integer, Set<Integer>> 						kanjiJLPTLevelTree;
	private TreeMap<Integer, Set<Integer>> 						kanjiNelsonRadicalTree;
	private TreeMap<Integer, Set<Integer>> 						kanjiStrokeCountTree;
	private TreeMap<Integer, Set<Integer>> 						kanjiStrokeMiscountsTree;
	private TreeMap<Integer, HashMap<String,Set<Integer>>>		kanjiDicReferenceTree;
	private TreeMap<Integer, Set<Integer>> 						kanjiDicReferencePlainTree;

	// #############################################################
	// ############# Constructor methods ###########################
	// #############################################################

	public TreeContainer(File zobj) {
		this.loadTrees(zobj);
	}

	// #############################################################
	// ############# Public methods ################################
	// #############################################################

	private Collection<? extends Integer> getFromDicTree(TreeMap<Integer, HashMap<String, Set<Integer>>> map, QueryCaseEnum qc, IndexPairQAbout qa) {

		Set<Integer> dicResult;
		Set<Integer> result = new HashSet<Integer>();
		Collection<HashMap<String, Set<Integer>>> plainedDics;
		switch (qc) {
		case EQUALS :
			result.addAll(map.get(qa.getValue().getSecond()).get(qa.getValue().getFirst()));
			break;
		case NOT_EQUALS :
			for (Set<Integer> refs : this.kanjiDicReferencePlainTree.values())
				result.addAll(refs);
			result.removeAll(map.get(qa.getValue().getSecond()).get(qa.getValue().getFirst()));
			break;
		case GREATER_OR_EQUALS_THAN :
			plainedDics = map.tailMap(qa.getValue().getSecond(), true).values();
			for (HashMap<String, Set<Integer>> dicRefs : plainedDics) {
				dicResult = dicRefs.get(qa.getValue().getFirst());
				if (dicResult != null)
					result.addAll(dicResult);
			}
			break;
		case GREATER_THAN :
			plainedDics = map.tailMap(qa.getValue().getSecond(), false).values();
			for (HashMap<String, Set<Integer>> dicRefs : plainedDics) {
				dicResult = dicRefs.get(qa.getValue().getFirst());
				if (dicResult != null)
					result.addAll(dicResult);
			}
			break;
		case LESS_OR_EQUALS_THAN :
			plainedDics = map.headMap(qa.getValue().getSecond(), true).values();
			for (HashMap<String, Set<Integer>> dicRefs : plainedDics) {
				dicResult = dicRefs.get(qa.getValue().getFirst());
				if (dicResult != null)
					result.addAll(dicResult);
			}
			break;
		case LESS_THAN :
			plainedDics = map.headMap(qa.getValue().getSecond(), false).values();
			for (HashMap<String, Set<Integer>> dicRefs : plainedDics) {
				dicResult = dicRefs.get(qa.getValue().getFirst());
				if (dicResult != null)
					result.addAll(dicResult);
			}
			break;
		case NULL :
			result.addAll(this.kanjiUnicodeTree);// TODO:slow
			for (Set<Integer> s : this.kanjiDicReferencePlainTree.values())
				result.removeAll(s);
			break;
		default :
			throw new IllegalArgumentException("Not allowed query case:" + qc);
		}

		return result;
	}

	private <E> Collection<Integer> getFromHashMap(HashMap<E, Set<Integer>> map, QueryCaseEnum qc, E value) {

		Set<Integer> result = new HashSet<Integer>();

		switch (qc) {
		case EQUALS :
			result.addAll(map.get(value));
			break;
		case NOT_EQUALS :
			result.addAll(this.kanjiUnicodeTree);
			result.removeAll(map.get(value));
			break;
		case NULL :
			result.addAll(this.kanjiUnicodeTree);
			result.removeAll(map.values());
		default :
			throw new IllegalArgumentException("Not allowed query case:" + qc);
		}

		return result;
	}

	private <E> Collection<Integer> getFromSequenceTree(SequenceTree<E, Integer> tree, QueryCaseEnum qc, List<E> seq) {

		Set<Integer> result = new HashSet<Integer>();

		switch (qc) {
		case EQUALS :
			result.addAll(tree.search(seq));
			break;
		case NOT_EQUALS :
			result.addAll(this.kanjiUnicodeTree);
			result.removeAll(tree.search(seq));
			break;
		case NULL :
			result.addAll(this.kanjiUnicodeTree);
			for (SequenceTree<E, Integer> subTree : tree)
				result.removeAll(subTree.getFinalNodes());
			break;
		default :
			throw new IllegalArgumentException("Not allowed query case:" + qc);
		}

		return result;
	}

	private <E> Collection<Integer> getFromTreeMap(TreeMap<E, Set<Integer>> map, QueryCaseEnum qc, E value) {

		Set<Integer> result = new HashSet<Integer>();

		switch (qc) {
		case EQUALS :
			result.addAll(map.get(value));
			break;
		case NOT_EQUALS :
			result.addAll(this.kanjiUnicodeTree);
			result.removeAll(map.get(value));
			break;
		case GREATER_OR_EQUALS_THAN :
			for (Set<Integer> val : map.tailMap(value, true).values())
				result.addAll(val);

			break;
		case GREATER_THAN :
			for (Set<Integer> val : map.tailMap(value, false).values())
				result.addAll(val);

			break;
		case LESS_OR_EQUALS_THAN :
			for (Set<Integer> val : map.headMap(value, true).values())
				result.addAll(val);

			break;
		case LESS_THAN :
			for (Set<Integer> val : map.headMap(value, true).values())
				result.addAll(val);
			break;
		case NULL :
			result.addAll(this.kanjiUnicodeTree);
			result.removeAll(map.values());
			break;
		default :
			throw new IllegalArgumentException("Not allowed query case:" + qc);
		}

		return result;
	}

	private <E> Collection<Integer> getFromTreeMap(TreeMap<Integer, Integer> map, QueryCaseEnum qc, Integer value) {

		Set<Integer> result = new HashSet<Integer>();

		switch (qc) {
		case EQUALS :
			result.add(map.get(value));
			break;
		case NOT_EQUALS :
			result.addAll(this.kanjiUnicodeTree);
			result.remove(map.get(value));
			break;
		case GREATER_OR_EQUALS_THAN :
			result.addAll(map.tailMap(value, true).values());
			break;
		case GREATER_THAN :
			result.addAll(map.tailMap(value, false).values());
			break;
		case LESS_OR_EQUALS_THAN :
			result.addAll(map.headMap(value, true).values());
			break;
		case LESS_THAN :
			result.addAll(map.headMap(value, true).values());
			break;
		case NULL :
			result.addAll(this.kanjiUnicodeTree);
			result.removeAll(map.values());
			break;
		default :
			throw new IllegalArgumentException("Not allowed query case:" + qc);
		}

		return result;
	}

	private Collection<Integer> getFromTreeSet(NavigableSet<Integer> set, QueryCaseEnum qc, Integer value) {

		Set<Integer> result = new HashSet<Integer>();

		switch (qc) {
		case EQUALS :
			if (set.contains(value))
				result.add(value);
			break;
		case NOT_EQUALS :
			result.addAll(this.kanjiUnicodeTree);
			result.remove(value);
			break;
		case GREATER_OR_EQUALS_THAN :
			result.addAll(set.tailSet(value, true));
			break;
		case GREATER_THAN :
			result.addAll(set.tailSet(value, false));
			break;
		case LESS_OR_EQUALS_THAN :
			result.addAll(set.headSet(value, true));
			break;
		case LESS_THAN :
			result.addAll(set.headSet(value, false));
			break;
		case NULL :
			result.addAll(this.kanjiUnicodeTree);
			result.removeAll(set);
			break;
		default :
			throw new IllegalArgumentException("Not allowed query case:" + qc);
		}

		return result;
	}

	public TreeMap<Integer, Set<Integer>> getKanjiClassicRadicalTree() {
		return this.kanjiClassicRadicalTree;
	}

	public SequenceTree<Character, Integer> getKanjiDeRooCodeTree() {
		return this.kanjiDeRooCodeTree;
	}

	public HashMap<String, Set<Integer>> getKanjiDicNameTree() {
		return this.kanjiDicNameTree;
	}

	public TreeMap<Integer, Set<Integer>> getKanjiDicReferencePlainTree() {
		return this.kanjiDicReferencePlainTree;
	}

	public TreeMap<Integer, HashMap<String, Set<Integer>>> getKanjiDicReferenceTree() {
		return this.kanjiDicReferenceTree;
	}

	public SequenceTree<Character, Integer> getKanjiFourCornerTree() {
		return this.kanjiFourCornerTree;
	}

	public TreeMap<Integer, Integer> getKanjiFrequencyTreeSet() {
		return this.kanjiFrequencyTreeSet;
	}

	public TreeMap<Integer, Set<Integer>> getKanjiGradeTree() {
		return this.kanjiGradeTree;
	}

	public SequenceTree<AllowedStrokeLineEnum, Integer> getKanjiGraphTree() {
		return this.kanjiGraphTree;
	}

	public HashMap<String, Set<Integer>> getKanjiJisCharset() {
		return this.kanjiJisCharset;
	}

	public SequenceTree<Integer, Integer> getKanjiJisCodeTree() {
		return this.kanjiJisCodeTree;
	}

	public TreeMap<Integer, Set<Integer>> getKanjiJLPTLevelTree() {
		return this.kanjiJLPTLevelTree;
	}

	public HashMap<String, Set<Integer>> getKanjiMeaningLangTree() {
		return this.kanjiMeaningLangTree;
	}

	public SequenceTree<Character, Integer> getKanjiMeaningTree() {
		return this.kanjiMeaningTree;
	}

	public TreeMap<Integer, Set<Integer>> getKanjiNelsonRadicalTree() {
		return this.kanjiNelsonRadicalTree;
	}

	public SequenceTree<Character, Integer> getKanjiReadingTree() {
		return this.kanjiReadingTree;
	}

	public HashMap<String, Set<Integer>> getKanjiReadingTypeTree() {
		return this.kanjiReadingTypeTree;
	}

	public SequenceTree<Integer, Integer> getKanjiSKIPCodeTree() {
		return this.kanjiSKIPCodeTree;
	}

	public SequenceTree<Character, Integer> getKanjiSpahnHadamitzkyCodeTree() {
		return this.kanjiSpahnHadamitzkyCodeTree;
	}

	// #############################################################
	// ############# Private methods ###############################
	// #############################################################

	public TreeMap<Integer, Set<Integer>> getKanjiStrokeCountTree() {
		return this.kanjiStrokeCountTree;
	}

	public TreeMap<Integer, Set<Integer>> getKanjiStrokeMiscountsTree() {
		return this.kanjiStrokeMiscountsTree;
	}

	public TreeSet<Integer> getKanjiUnicodeTree() {
		return this.kanjiUnicodeTree;
	}


	public SequenceTree<Character, Integer> getKanjiVariantTree() {
		return this.kanjiVariantTree;
	}

	public HashMap<String, Set<Integer>> getKanjiVariantTypeTree() {
		return this.kanjiVariantTypeTree;
	}

	public Set<Integer> getRefs(ValueQAbout<?> vqa) {

		Set<Integer> result = new HashSet<Integer>();

		if (vqa instanceof IntegerValueQAbout) {

			IntegerValueQAbout ivaq = (IntegerValueQAbout) vqa;
			switch (ivaq.getFieldEnum()) {
			case KANJI_CLASSIC_RADICAL :
				result.addAll(this.getFromTreeMap(this.kanjiClassicRadicalTree, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			case KANJI_CLASSIC_NELSON :
				result.addAll(this.getFromTreeMap(this.kanjiNelsonRadicalTree, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			case KANJI_STROKE_MISCOUNT :
				result.addAll(this.getFromTreeMap(this.kanjiStrokeMiscountsTree, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			case KANJI_DIC_INDEX : // TODO: remove one of this elements
			case KANJI_DIC_REFERENCE :
				result.addAll(this.getFromTreeMap(this.kanjiDicReferencePlainTree, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			case KANJI_FREQUENCY :
				result.addAll(this.getFromTreeMap(this.kanjiFrequencyTreeSet, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			case KANJI_GRADE :
				result.addAll(this.getFromTreeMap(this.kanjiGradeTree, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			case KANJI_JLPT_LEVEL :
				result.addAll(this.getFromTreeMap(this.kanjiJLPTLevelTree, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			case KANJI_STROKE_COUNT :
				result.addAll(this.getFromTreeMap(this.kanjiStrokeCountTree, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			case KANJI_UNICODE_VALUE :
				result.addAll(this.getFromTreeSet(this.kanjiUnicodeTree, ivaq.getQueryCase(), ivaq.getValue()));
				break;
			default :
				throw new IllegalArgumentException("Not allowed field:" + vqa.getFieldEnum());
			}
		} else if (vqa instanceof StringValueQAbout) {

			List<Integer> seqi;
			LinkedList<Character> seqc;
			StringValueQAbout svqa = (StringValueQAbout) vqa;
			switch (svqa.getFieldEnum()) {
			case KANJI_JIS_CHARSET :
				result.addAll(this.getFromHashMap(this.kanjiJisCharset, svqa.getQueryCase(), svqa.getValue()));
				break;
			case KANJI_DIC_NAME :
				result.addAll(this.getFromHashMap(this.kanjiDicNameTree, svqa.getQueryCase(), svqa.getValue()));
				break;
			case KANJI_MEANING_LANGUAGE :
				result.addAll(this.getFromHashMap(this.kanjiMeaningLangTree, svqa.getQueryCase(), svqa.getValue()));
				break;
			case KANJI_READING_TYPE :
				result.addAll(this.getFromHashMap(this.kanjiReadingTypeTree, svqa.getQueryCase(), svqa.getValue()));
				break;
			case KANJI_VARIANT_TYPE :
				result.addAll(this.getFromHashMap(this.kanjiVariantTypeTree, svqa.getQueryCase(), svqa.getValue()));
				break;
			case KANJI_JIS_CODE :
				// TODO: JIS to integer in KanjiTag.java
				seqi = new ArrayList<Integer>();
				for (String c : svqa.getValue().split("-"))
					seqi.add(new Integer(c));
				if (seqi.size() == 2) {
					seqi.add(seqi.get(1));
					seqi.set(1, seqi.get(0));
					seqi.set(0, 1);
				}
				result.addAll(this.getFromSequenceTree(this.kanjiJisCodeTree, svqa.getQueryCase(), seqi));
				break;
			case KANJI_SKIP :
				seqi = new ArrayList<Integer>();
				for (String c : svqa.getValue().split("-"))
					seqi.add(new Integer(c));
				result.addAll(this.getFromSequenceTree(this.kanjiSKIPCodeTree, svqa.getQueryCase(), seqi));
				break;
			case KANJI_DE_ROO :
				seqc = Strings.toCharacters(svqa.getValue());
				result.addAll(this.getFromSequenceTree(this.kanjiDeRooCodeTree, svqa.getQueryCase(), seqc));
				break;
			case KANJI_FOUR_CORNER :
				seqc = Strings.toCharacters(svqa.getValue());
				result.addAll(this.getFromSequenceTree(this.kanjiFourCornerTree, svqa.getQueryCase(), seqc));
				break;
			case KANJI_SPAHN_HADAMITZKY :
				seqc = Strings.toCharacters(svqa.getValue());
				result.addAll(this.getFromSequenceTree(this.kanjiSpahnHadamitzkyCodeTree, svqa.getQueryCase(), seqc));
				break;
			case KANJI_MEANING :
				seqc = Strings.toCharacters(svqa.getValue());
				result.addAll(this.getFromSequenceTree(this.kanjiMeaningTree, svqa.getQueryCase(), seqc));
				break;
			case KANJI_READING :
				seqc = Strings.toCharacters(svqa.getValue());
				result.addAll(this.getFromSequenceTree(this.kanjiReadingTree, svqa.getQueryCase(), seqc));
				break;
			case KANJI_VARIANT_INDEX :
				seqc = Strings.toCharacters(svqa.getValue());
				result.addAll(this.getFromSequenceTree(this.kanjiVariantTree, svqa.getQueryCase(), seqc));
				break;
			default :
				throw new IllegalArgumentException("Not allowed field:" + vqa.getFieldEnum());

			}
		} else if (vqa instanceof IndexPairQAbout) {
			IndexPairQAbout ipqa = (IndexPairQAbout) vqa;
			switch (ipqa.getFieldEnum()) {
			case PAIR_DIC :
				result.addAll(this.getFromDicTree(this.kanjiDicReferenceTree, ipqa.getQueryCase(), ipqa));
				break;
			default :
				throw new IllegalArgumentException("Not allowed field:" + vqa.getFieldEnum());
			}
		} else if (vqa instanceof StringPairQAbout) {
			List<Integer> seqi;
			LinkedList<Character> seqc;

			StringPairQAbout spqa = (StringPairQAbout) vqa;
			switch (spqa.getFieldEnum()) {
			case PAIR_JIS :
				// TODO: JIS to integer in KanjiTag.java
				seqi = new ArrayList<Integer>();
				for (String c : spqa.getValue().getSecond().split("-"))
					seqi.add(new Integer(c));
				if (seqi.size() == 2) {
					seqi.add(seqi.get(1));
					seqi.set(1, seqi.get(0));
					seqi.set(0, 1);
				}
				result.addAll(this.getFromSequenceTree(this.kanjiJisCodeTree, spqa.getQueryCase(), seqi));
				result.retainAll(this.getFromHashMap(this.kanjiJisCharset, spqa.getQueryCase(), spqa.getValue().getFirst()));
				break;
			case PAIR_MEANING :
				seqi = new ArrayList<Integer>();
				seqc = Strings.toCharacters(spqa.getValue().getSecond());
				seqc.add('#');
				seqc.addAll(Strings.toCharacters(spqa.getValue().getFirst()));
				result.addAll(this.getFromSequenceTree(this.kanjiMeaningTree, spqa.getQueryCase(), seqc));
				break;
			case PAIR_READING :
				seqi = new ArrayList<Integer>();
				seqc = Strings.toCharacters(spqa.getValue().getSecond());
				seqc.add('#');
				seqc.addAll(Strings.toCharacters(spqa.getValue().getFirst()));
				result.addAll(this.getFromSequenceTree(this.kanjiReadingTree, spqa.getQueryCase(), seqc));
				break;
			case PAIR_VARIANT :
				seqi = new ArrayList<Integer>();
				seqc = Strings.toCharacters(spqa.getValue().getSecond());
				seqc.add('#');
				seqc.addAll(Strings.toCharacters(spqa.getValue().getFirst()));
				result.addAll(this.getFromSequenceTree(this.kanjiVariantTree, spqa.getQueryCase(), seqc));
				break;
			default :
				throw new IllegalArgumentException("Not allowed field:" + vqa.getFieldEnum());
			}

		} else if (vqa instanceof KanjiGraphQAbout) {
			KanjiGraphQAbout kgqa = (KanjiGraphQAbout) vqa;
			switch (vqa.getFieldEnum()) {
			case KANJI_GRAPH :
				result.addAll(this.getFromSequenceTree(this.kanjiGraphTree, kgqa.getQueryCase(), kgqa.getValue().getLineSequence()));
				break;
			default :
				throw new IllegalArgumentException("Not allowed field:" + vqa.getFieldEnum());
			}
		} else {
			throw new IllegalArgumentException("Not allowed field:" + vqa.getFieldEnum());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private void loadTrees(File zobj) {

		ZipFile zip;
		try {
			zip = new ZipFile(zobj);

			this.kanjiSKIPCodeTree = (SequenceTree<Integer, Integer>) this.readObjectFromZip(zip, "kanjiSKIPCodeTree");

			this.kanjiFourCornerTree = (SequenceTree<Character, Integer>) this.readObjectFromZip(zip, "kanjiFourCornerTree");

			this.kanjiSpahnHadamitzkyCodeTree = (SequenceTree<Character, Integer>) this.readObjectFromZip(zip, "kanjiSpahnHadamitzkyCodeTree");

			this.kanjiDeRooCodeTree = (SequenceTree<Character, Integer>) this.readObjectFromZip(zip, "kanjiDeRooCodeTree");

			this.kanjiGraphTree = (SequenceTree<AllowedStrokeLineEnum, Integer>) this.readObjectFromZip(zip, "kanjiGraphTree");

			this.kanjiJisCharset = (HashMap<String, Set<Integer>>) this.readObjectFromZip(zip, "kanjiJisCharset");

			this.kanjiMeaningTree = (SequenceTree<Character, Integer>) this.readObjectFromZip(zip, "kanjiMeaningTree");

			this.kanjiReadingTree = (SequenceTree<Character, Integer>) this.readObjectFromZip(zip, "kanjiReadingTree");

			this.kanjiJisCodeTree = (SequenceTree<Integer, Integer>) this.readObjectFromZip(zip, "kanjiJisCodeTree");

			this.kanjiVariantTree = (SequenceTree<Character, Integer>) this.readObjectFromZip(zip, "kanjiVariantTree");

			this.kanjiUnicodeTree = (TreeSet<Integer>) this.readObjectFromZip(zip, "kanjiUnicodeTree");

			this.kanjiClassicRadicalTree = (TreeMap<Integer, Set<Integer>>) this.readObjectFromZip(zip, "kanjiClassicRadicalTree");

			this.kanjiDicNameTree = (HashMap<String, Set<Integer>>) this.readObjectFromZip(zip, "kanjiDicNameTree");

			this.kanjiDicReferenceTree = (TreeMap<Integer, HashMap<String,Set<Integer>>>) this.readObjectFromZip(zip, "kanjiDicReferenceTree");

			this.kanjiDicReferencePlainTree = new TreeMap<Integer, Set<Integer>>(); //TODO: remove, slow and unnecesary
			Set<Integer> plainedRefs;
			for (Integer key : this.kanjiDicReferenceTree.keySet()) {
				for (Set<Integer> dicRefs : this.kanjiDicReferenceTree.get(key).values()) {
					plainedRefs = this.kanjiDicReferencePlainTree.get(key);
					if (plainedRefs == null) {
						plainedRefs = new HashSet<Integer>();
						this.kanjiDicReferencePlainTree.put(key, plainedRefs);
					}
					plainedRefs.addAll(dicRefs);
				}
			}

			this.kanjiFrequencyTreeSet = (TreeMap<Integer, Integer>) this.readObjectFromZip(zip, "kanjiFrequencyTreeSet");

			this.kanjiGradeTree = (TreeMap<Integer, Set<Integer>>) this.readObjectFromZip(zip, "kanjiGradeTree");

			this.kanjiJLPTLevelTree = (TreeMap<Integer, Set<Integer>>) this.readObjectFromZip(zip, "kanjiJLPTLevelTree");

			this.kanjiMeaningLangTree = (HashMap<String, Set<Integer>>) this.readObjectFromZip(zip, "kanjiMeaningLangTree");

			this.kanjiNelsonRadicalTree = (TreeMap<Integer, Set<Integer>>) this.readObjectFromZip(zip, "kanjiNelsonRadicalTree");

			this.kanjiReadingTypeTree = (HashMap<String, Set<Integer>>) this.readObjectFromZip(zip, "kanjiReadingTypeTree");

			this.kanjiStrokeCountTree = (TreeMap<Integer, Set<Integer>>) this.readObjectFromZip(zip, "kanjiStrokeCountTree");

			this.kanjiStrokeMiscountsTree = (TreeMap<Integer, Set<Integer>>) this.readObjectFromZip(zip, "kanjiStrokeMiscountsTree");

			this.kanjiVariantTypeTree = (HashMap<String, Set<Integer>>) this.readObjectFromZip(zip, "kanjiVariantTypeTree");

		} catch (IOException e) {
			ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.TREE_IOEXCEPTION);
		} catch (ClassNotFoundException e) {
			ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.CORRUPTED_TREE);
		}
	}

	/**
	 * Returns the first object serialized into a file of the zip file
	 */
	private Object readObjectFromZip(ZipFile zip, String name) throws IOException, ClassNotFoundException {

		Object obj = null;

		InputStream zis;
		ObjectInputStream ois;
		try {
			zis = zip.getInputStream(zip.getEntry(name));
			ois = new ObjectInputStream(new BufferedInputStream(zis));

			obj = ois.readObject();
			zis.close();
			ois.close();

		} catch (RuntimeException e) {
			System.err.println("Failed when reading \"" + name + "\" zip file");
			throw e;
		} catch (IOException e) {
			System.err.println("Failed when reading \"" + name + "\" zip file");
			throw e;
		}

		return obj;
	}


}

