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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiDicsEnum;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiReadingTypesEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.auxi.SortCaseEnum;
import net.ajaest.jdk.data.dict.sort.DictionaryOrderDef;
import net.ajaest.jdk.data.dict.sort.GraphOrderDef;
import net.ajaest.jdk.data.dict.sort.KanjiSortExpression;
import net.ajaest.jdk.data.dict.sort.MeaningOrderDef;
import net.ajaest.jdk.data.dict.sort.OrderDef;
import net.ajaest.jdk.data.dict.sort.ReadingOrderDef;
import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

//TODO: JavaDoc
public class KanjiComparator  implements Comparator<KanjiTag> {


	private KanjiSortExpression kse;
	private List<Comparator<KanjiTag>> subComp;
	
	public KanjiComparator(KanjiSortExpression kse) {

		this.kse = kse;
		this.subComp = parseSortingExpression(kse);
	}
	
	@Override
	public int compare(KanjiTag k1, KanjiTag k2) {

		int compare = 0;

		for (Comparator<KanjiTag> ktc : subComp) {
			compare = ktc.compare(k1, k2);
			if (compare != 0)
				break;
		}

		return compare;
	}

	public KanjiSortExpression getKanjiSortExpression() {
		return kse;
	}

	public List<Comparator<KanjiTag>> getSubComparators() {
		return subComp;
	}

	// ___________________________________________________________________________
	// ___________________________________________________________________________
	
	public static List<Comparator<KanjiTag>> parseSortingExpression(KanjiSortExpression kse) {

		List<Comparator<KanjiTag>> compStr = new ArrayList<Comparator<KanjiTag>>();
		List<QAbout> domains = kse.getDomains();
		
		OrderDef od;
		boolean inverse;
		for (QAbout qa : domains) {
			od = (OrderDef) qa;
			inverse = getInverse(od);
			if (od.getOrderBy().equals(SortCaseEnum.Ramdomly))
				compStr.add(createRamdomComparator());
			else {
			switch (od.getSortField()) {

				case KANJI_UNICODE_VALUE :
					compStr.add(createUnicodeComparator(inverse));
					break;
				case KANJI_JIS_CHARSET :
					compStr.add(createJISCharsetComparator(inverse));
					break;
				case KANJI_JIS_CODE :
					compStr.add(createJISCodeComparator(inverse));
					break;
				case KANJI_CLASSIC_NELSON :
					compStr.add(createNelsonRadicalComparator(inverse));
					break;
				case KANJI_CLASSIC_RADICAL :
					compStr.add(createClassicRadicalComparator(inverse));
					break;
				case KANJI_GRADE :
					compStr.add(createGradeComparator(inverse));
					break;
				case KANJI_STROKE_COUNT :
					compStr.add(createStrokeCountComparator(inverse));
					break;
				case KANJI_FREQUENCY :
					compStr.add(createFrequencyComparator(inverse));
					break;
				case KANJI_SKIP :
					compStr.add(createSKIPComparator(inverse));
					break;
				case KANJI_SPAHN_HADAMITZKY :
					compStr.add(createSpahnHadamitzkyComparator(inverse));
					break;
				case KANJI_FOUR_CORNER :
					compStr.add(createFourCornerComparator(inverse));
					break;
				case KANJI_DE_ROO :
					compStr.add(createDeRooComparator(inverse));
					break;
				case KANJI_GRAPH :
					GraphOrderDef god = (GraphOrderDef) od;
					if (god.getOrderBy() == SortCaseEnum.Similarity)
						compStr.add(createGraphSimilarityComparator(god.getSimilarGraph()));
					else
						compStr.add(createGraphComparator(inverse));
					break;
				case KANJI_DIC_REFERENCE :
					DictionaryOrderDef dod = (DictionaryOrderDef) od;
					compStr.add(createDicComparator(dod.getSelectedDic().getSelected(), inverse));
					break;
				case KANJI_FIRST_MEANING :
					MeaningOrderDef mod = (MeaningOrderDef) od;
					compStr.add(createMeaningComparator(mod.getSelectedLanguaje().getSelected(), inverse));
					break;
				case KANJI_FIRST_READING :
					ReadingOrderDef rod = (ReadingOrderDef) od;
						compStr.add(createReadingComparator(rod.getSelectedReading().getSelected(), inverse));
					break;
				default :
					throw new IllegalArgumentException();
			}
			}
		}

		return compStr;
	}

	private static boolean getInverse(OrderDef od) {

		switch (od.getOrderBy()){
			case Alphabetically :
			case FromLeastToGreather :
			case Similarity :
				return false;
			case AlphabeticallyInverse :
			case FromGreatherToLeast :
			default :
				return true;
		}
		
	}

	// ___________________________________________________________________________
	// ___________________________________________________________________________
	
	private static class RamdomComparator implements Comparator<KanjiTag> {

		@Override
		public int compare(KanjiTag o1, KanjiTag o2) {

			return (int) (1000 * (Math.random() - 0.5));
		}

	}
	
	private static class KanjiGraphSimilarityComparator implements Comparator<KanjiTag> {

		private GraphSimilarityComparator gc;

		protected KanjiGraphSimilarityComparator(KanjiGraph kg) {
			gc = new GraphSimilarityComparator(kg);
		}

		@Override
		public int compare(KanjiTag o1, KanjiTag o2) {

			return gc.compare(o1.getGraph(), o2.getGraph());
		}
		
	}
	
	private static class GraphComparator implements Comparator<KanjiTag> {

		private int inverse;

		private Map<Integer, KanjiGraph> retrievedGraphs;

		private KanjiGraph defGraph;

		protected GraphComparator(boolean inverse) {

			retrievedGraphs = new HashMap<Integer, KanjiGraph>();

			defGraph = new KanjiGraph(new ArrayList<KanjiStroke>(), new HashSet<KanjiStrokeClue>(), -1);

			if (inverse)
				this.inverse = -1;
			else
				this.inverse = 1;

		}

		@Override
		public int compare(KanjiTag o1, KanjiTag o2) {

			int compare = -1;

			KanjiGraph ref1 = retrievedGraphs.get(o1.getUnicodeRef());
			KanjiGraph ref2 = retrievedGraphs.get(o2.getUnicodeRef());

			if (ref1 == null) {
				addReference(o1);
				ref1 = retrievedGraphs.get(o1.getUnicodeRef());
			}
			if (ref2 == null) {
				addReference(o2);
				ref2 = retrievedGraphs.get(o2.getUnicodeRef());
			}

			if (ref1.getUnicodeRef() != -1 && ref2.getUnicodeRef() != -1) {

				compare = inverse * ref1.compareTo(ref2);

			} else if (ref1.getUnicodeRef() == 1) {
				compare = -1;
			} else
				compare = 1;

			return compare;
		}

		protected void addReference(KanjiTag kt) {
			
			if(kt.getGraph()!=null)
				retrievedGraphs.put(kt.getUnicodeRef(), kt.getGraph());
			else
				retrievedGraphs.put(kt.getUnicodeRef(), defGraph);
		}

	}

	private static class PlainIntegerFieldComparator extends IntegerAbstractComparator<KanjiFieldEnum> {

		protected PlainIntegerFieldComparator(KanjiFieldEnum key, boolean inverse) {
			super(key, inverse);
		}

		@Override
		protected void addReference(KanjiTag kt) {

			Integer i = (Integer) kt.getByEnum(super.key);

			if (i == null)
				i = -1;

			super.retrievedIntegerss.put(kt.getUnicodeRef(), i);
		}

	}

	private static class PlainStringFieldComparator extends StringAbstractComparator<KanjiFieldEnum> {

		protected PlainStringFieldComparator(KanjiFieldEnum key, boolean isNumString, boolean inverse) {
			super(key, isNumString, inverse);
		}

		@Override
		protected void addReference(KanjiTag kt) {

			String s = (String) kt.getByEnum(super.key);

			if (s == null)
				s = "";

			super.retrievedInts.put(kt.getUnicodeRef(), s);
		}
	}

	private static class ReadingComparator extends StringAbstractComparator<KanjiReadingTypesEnum> {

		protected ReadingComparator(KanjiReadingTypesEnum key, boolean inverse) {
			super(key, false, inverse);
		}

		@Override
		protected void addReference(KanjiTag kt) {
			
			String ref = "";
			String key = super.key.toString().replace("READ_", "").toLowerCase();
			
			for (ReadingEntry re : kt.getReadings()) {
				if (re.getKey().equals(key)) {
					ref = re.getElements().get(0);
					break;
				}
			}

			super.retrievedInts.put(kt.getUnicodeRef(), ref);
		}

	}

	private static class DicComparator extends StringAbstractComparator<KanjiDicsEnum> {

		protected DicComparator(KanjiDicsEnum dicName, boolean inverse) {
			// super(dicName, true, inverse);
			super(dicName, false, inverse);
		}

		protected void addReference(KanjiTag kt) {

			String ret = "";

			String dicKey = super.key.toString();
			String dicName = dicKey.replace("DIC_", "");

			for (DicReferencePair s : kt.getDicReferences())
				if (s.getFirst().equals(dicName)) {
					ret = s.getSecond();
					break;
				}

			super.retrievedInts.put(kt.getUnicodeRef(), ret);
		}
	}
	
	private static class MeaningComparator extends StringAbstractComparator<ISO639ー1> {

		protected MeaningComparator(ISO639ー1 lang, boolean inverse) {
			super(lang, false, inverse);
		}

		@Override
		protected void addReference(KanjiTag kt) {

			String ret = "";

			for (MeaningEntry me : kt.getMeanings())
				if (me.getKey().toUpperCase().equals(super.key.toString())) {
					if (me.getElements().size() > 0)
						ret = me.getElements().get(0);
					break;
				}

			super.retrievedInts.put(kt.getUnicodeRef(), Normalizer.normalize(ret.toLowerCase(), Normalizer.Form.NFD));
		}
		
	}

	// ___________________________________________________________________________
	// ___________________________________________________________________________


	private static abstract class StringAbstractComparator<E> implements Comparator<KanjiTag> {

		private E key;
		private boolean isNumString;
		private int inverse;

		private Map<Integer, String> retrievedInts;

		protected StringAbstractComparator(E key, boolean isNumString, boolean inverse) {
			this.key = key;
			this.isNumString = isNumString;
			retrievedInts = new HashMap<Integer, String>();

			if (inverse)
				this.inverse = -1;
			else
				this.inverse = 1;

		}

		@Override
		public int compare(KanjiTag o1, KanjiTag o2) {

			int compare = -1;

			String ref1 = retrievedInts.get(o1.getUnicodeRef());
			String ref2 = retrievedInts.get(o2.getUnicodeRef());

			if (ref1 == null) {
				addReference(o1);
				ref1 = retrievedInts.get(o1.getUnicodeRef());
			}
			if (ref2 == null) {
				addReference(o2);
				ref2 = retrievedInts.get(o2.getUnicodeRef());
			}

			if (ref1.length() > 0 && ref2.length() > 0) {

				if (isNumString && (ref1.length() != ref2.length()))
					compare = inverse * (ref1.length() - ref2.length());
				else
					compare = inverse * ref1.compareTo(ref2);

			} else if (ref1.length() > 0) {
				compare = -1;
			} else
				compare = 1;

			return compare;
		}

		protected abstract void addReference(KanjiTag kt);
	}
	
	private static abstract class IntegerAbstractComparator<E> implements Comparator<KanjiTag> {

		private int inverse;

		private E key;

		private Map<Integer, Integer> retrievedIntegerss;

		protected IntegerAbstractComparator(E key, boolean inverse) {

			this.key = key;
			retrievedIntegerss = new HashMap<Integer, Integer>();

			if (inverse)
				this.inverse = -1;
			else
				this.inverse = 1;

		}

		@Override
		public int compare(KanjiTag o1, KanjiTag o2) {

			int compare;

			Integer ref1 = retrievedIntegerss.get(o1.getUnicodeRef());
			Integer ref2 = retrievedIntegerss.get(o2.getUnicodeRef());

			if (ref1 == null) {
				addReference(o1);
				ref1 = retrievedIntegerss.get(o1.getUnicodeRef());
			}
			if (ref2 == null) {
				addReference(o2);
				ref2 = retrievedIntegerss.get(o2.getUnicodeRef());
			}

			if (ref1 != -1 && ref2 != -1) {
				compare = inverse * ref1.compareTo(ref2);
			} else if (ref1 == -1) {
				compare = 1;
			} else
				compare = -1;

			return compare;
		}

		/**
		 * "" String must be added to the map when the field is empty or null
		 * 
		 * @param kt
		 */
		protected abstract void addReference(KanjiTag kt);
	}
 
	// ___________________________________________________________________________
	// ___________________________________________________________________________

	public static Comparator<KanjiTag> createUnicodeComparator(boolean inverse) {
		return new PlainIntegerFieldComparator(KanjiFieldEnum.KANJI_UNICODE_VALUE, inverse);
	}

	public static Comparator<KanjiTag> createJISCharsetComparator(boolean inverse) {
		return new PlainStringFieldComparator(KanjiFieldEnum.KANJI_JIS_CHARSET, false, inverse);
	}

	public static Comparator<KanjiTag> createJISCodeComparator(boolean inverse) {
		return new PlainStringFieldComparator(KanjiFieldEnum.KANJI_JIS_CODE, false, inverse);
	}

	public static Comparator<KanjiTag> createNelsonRadicalComparator(boolean inverse) {
		return new PlainIntegerFieldComparator(KanjiFieldEnum.KANJI_CLASSIC_NELSON, inverse);
	}

	public static Comparator<KanjiTag> createClassicRadicalComparator(boolean inverse) {
		return new PlainIntegerFieldComparator(KanjiFieldEnum.KANJI_CLASSIC_RADICAL, inverse);
	}

	public static Comparator<KanjiTag> createGradeComparator(boolean inverse) {
		return new PlainIntegerFieldComparator(KanjiFieldEnum.KANJI_GRADE, inverse);
	}

	public static Comparator<KanjiTag> createStrokeCountComparator(boolean inverse) {
		return new PlainIntegerFieldComparator(KanjiFieldEnum.KANJI_STROKE_COUNT, inverse);
	}

	public static Comparator<KanjiTag> createFrequencyComparator(boolean inverse) {
		return new PlainIntegerFieldComparator(KanjiFieldEnum.KANJI_FREQUENCY, inverse);
	}

	public static Comparator<KanjiTag> createSKIPComparator(boolean inverse) {
		return new PlainStringFieldComparator(KanjiFieldEnum.KANJI_SKIP, false, inverse);
	}

	public static Comparator<KanjiTag> createSpahnHadamitzkyComparator(boolean inverse) {
		return new PlainStringFieldComparator(KanjiFieldEnum.KANJI_SPAHN_HADAMITZKY, false, inverse);
	}

	public static Comparator<KanjiTag> createFourCornerComparator(boolean inverse) {
		return new PlainStringFieldComparator(KanjiFieldEnum.KANJI_FOUR_CORNER, false, inverse);
	}

	public static Comparator<KanjiTag> createDeRooComparator(boolean inverse) {
		return new PlainStringFieldComparator(KanjiFieldEnum.KANJI_DE_ROO, true, inverse);
	}

	public static Comparator<KanjiTag> createGraphComparator(boolean inverse) {
		return new GraphComparator(inverse);
	}

	public static Comparator<KanjiTag> createDicComparator(KanjiDicsEnum dicName, boolean inverse) {
		return new DicComparator(dicName, inverse);
	}

	public static Comparator<KanjiTag> createMeaningComparator(ISO639ー1 lang, boolean inverse) {
		return new MeaningComparator(lang, inverse);
	}

	public static Comparator<KanjiTag> createReadingComparator(KanjiReadingTypesEnum krte, boolean inverse) {
		return new ReadingComparator(krte, inverse);
	}

	public static Comparator<KanjiTag> createRamdomComparator() {
		return new RamdomComparator();
	}
	private static Comparator<KanjiTag> createGraphSimilarityComparator(KanjiGraph kanjiGraph) {
		return new KanjiGraphSimilarityComparator(kanjiGraph);
	}

	// ___________________________________________________________________________
	// ___________________________________________________________________________
	
}

	

