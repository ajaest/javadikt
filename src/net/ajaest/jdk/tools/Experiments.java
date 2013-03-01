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

package net.ajaest.jdk.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.ajaest.jdk.data.auxi.KanjiComparator;
import net.ajaest.jdk.data.dict.sort.KanjiOrdering;
import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.jdk.data.kanji.VariantPair;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;

//TODO: JavaDoc
public class Experiments {

	public static void main(String... args) {
		getMaxNumberOfReadsAndMeanings();
	}

	public static void getMaxNumberOfReadsAndMeanings() {
		ODB db = ODBFactory.open("resources/data/dict/kanjidict.jdk");

		Objects<Kanji> obj = db.getObjects(Kanji.class);

		int maxLength = 0;
		int testSize = 10;
		int greatherThanTestSize = 0;
		int gradeLowerThan8 = 0;
		Set<KanjiTag> kts = new TreeSet<KanjiTag>(new KanjiComparator(new KanjiOrdering().sort_by_frequency().from_least_to_greatest()));
		for (KanjiTag kt : obj) {

			for (MeaningEntry me : kt.getMeanings()) {
				if(me.getElements().size()>maxLength)
					maxLength = me.getElements().size();
				if (me.getElements().size() > testSize) {
					greatherThanTestSize++;
					if (kt.getGrade() != null && kt.getGrade() <= 8) {
						gradeLowerThan8++;
						kts.add(kt);
					}
				}
			}


			for (ReadingEntry re : kt.getReadings()) {
				if ((re.getKey().equals("ja_on") || re.getKey().equals("ja_kun")) && re.getElements().size() > maxLength) {
					maxLength = re.getElements().size();
				}
				if ((re.getKey().equals("ja_on") || re.getKey().equals("ja_kun")) && re.getElements().size() > greatherThanTestSize) {
					greatherThanTestSize++;
					if (kt.getGrade() != null && kt.getGrade() <= 8) {
						gradeLowerThan8++;
						kts.add(kt);
					}
				}
			}
		}
		
		System.out.println("Max length : " + maxLength);
		System.out.println("greatherThanTestSize : " + greatherThanTestSize + ", lower than 8 = " + gradeLowerThan8);
		for (KanjiTag kt : kts) {
			System.out.println("\t" + kt);
		}
	}

	public static void showAllDicRefsSorted() {
		ODB db = ODBFactory.open("resources/data/dict/kanjidict.jdk");

		Objects<DicReferencePair> obj = db.getObjects(DicReferencePair.class);

		SortedMap<String, SortedSet<Integer>> allRefs = new TreeMap<String, SortedSet<Integer>>();
		SortedSet<Integer> temp;
		for (DicReferencePair drp : obj) {
			temp = allRefs.get(drp.getFirst());
			if(temp==null){
				temp = new TreeSet<Integer>();
				allRefs.put(drp.getFirst(), temp);
			}
			temp.add(drp.getNumReference());
		}

		for (String s : allRefs.keySet())
			for (Integer s1 : allRefs.get(s)) {
				System.out.println("[" + s + "] -> " + s1);
			}

	}
	
	public static void showAllRefsTipes(){
		ODB db = ODBFactory.open("resources/data/kanjidict.jdk");

		Objects<KanjiTag> obj = db.getObjects(Kanji.class);
		
		Map<String, Integer> variTypes = new HashMap<String, Integer>();
		
		String currentVar;
		Integer currentVarCount;
		for(KanjiTag kt : obj){
			if(kt.getVariants()!=null){
				for(VariantPair vp : kt.getVariants()){
					currentVar = vp.getFirst();
					if(variTypes.containsKey(currentVar)){
						currentVarCount = variTypes.get(currentVar);
						currentVarCount++;
					}else{
						currentVarCount = 1;
					}
					variTypes.put(currentVar, currentVarCount);
				}
			}
		}
		
		for(String s : variTypes.keySet()){
			System.out.println("「" + s + "」          \t\t-> " + variTypes.get(s));
		}
	}

//	public static void getDicRefsMaxLengths() {
//		ODB db = ODBFactory.open("resources/data/kanjidict.jdk");
//
//		Objects<DicReferencePair> obj = db.getObjects(DicReferencePair.class);
//
//		Map<String, Integer> numericDicRefsMaxLength = new HashMap<String, Integer>();
//
//		Integer currentDicMaxSize;
//		for (DicReferencePair drp : obj) {
//			/*if (!drp.getFirst().equals("moro") && !drp.getFirst().equals("busy_people") && !drp.getFirst().equals("oneill_names"))*/ {
//				currentDicMaxSize = numericDicRefsMaxLength.get(drp.getFirst());
//				if (currentDicMaxSize == null)
//					numericDicRefsMaxLength.put(drp.getFirst(), drp.getSecond().length());
//				else {
//					if (drp.getSecond().length() > currentDicMaxSize)
//						numericDicRefsMaxLength.put(drp.getFirst(), drp.getSecond().length());
//				}
//			}
//		}
//
//		for (String dicName : numericDicRefsMaxLength.keySet()) {
//			System.out.println("dicRefsMaxLength.put(KanjiDicsEnum.DIC_" + dicName + "," + numericDicRefsMaxLength.get(dicName) + ");");
//		}
//
//		db.close();
//
//	}

//	public static void getMaxMinLengths() {
//		ODB db = ODBFactory.open("resources/data/kanjidict.jdk");
//
//		Objects<DicReferencePair> obj = db.getObjects(DicReferencePair.class);
//
//		Map<String, Integer> numericDicRefsMaxLength = new HashMap<String, Integer>();
//		Map<String, Integer> numericDicRefsMinLength = new HashMap<String, Integer>();
//
//		Integer currentDicMaxSize, currentDicMinSize;
//		for (DicReferencePair drp : obj) {
//			if (drp.getFirst() != "moro" && drp.getFirst() != "busy_people" && drp.getFirst() != "oneill_names") {
//				currentDicMaxSize = numericDicRefsMaxLength.get(drp.getFirst());
//				if (currentDicMaxSize == null)
//					numericDicRefsMaxLength.put(drp.getFirst(), drp.getSecond().length());
//				else {
//					if (drp.getSecond().length() > currentDicMaxSize)
//						numericDicRefsMaxLength.put(drp.getFirst(), drp.getSecond().length());
//				}
//
//				currentDicMinSize = numericDicRefsMinLength.get(drp.getFirst());
//				if (currentDicMinSize == null)
//					numericDicRefsMinLength.put(drp.getFirst(), drp.getSecond().length());
//				else {
//					if (drp.getSecond().length() < currentDicMinSize)
//						numericDicRefsMinLength.put(drp.getFirst(), drp.getSecond().length());
//				}
//			}
//		}
//		
//		for (String dicName : numericDicRefsMaxLength.keySet()) {
//			System.out.println(dicName + ": " + numericDicRefsMaxLength.get(dicName) + "<->" + numericDicRefsMinLength.get(dicName));
//		}
//
//		db.close();
//
//	}
//
//	public static void nonNumericDicReferencesExperiment() {
//		ODB db = ODBFactory.open("resources/data/kanjidict.jdk");
//
//		Objects<DicReferencePair> obj = db.getObjects(DicReferencePair.class);
//
//		int i = 0;
//		int maxlength = 0;
//		int minlength = 1000;
//		String minstring = "";
//		String maxstring = "";
//		SortedSet<String> dicReferences = new TreeSet<String>();
//		for (DicReferencePair drp : obj) {
//			try {
//				Integer.decode(drp.getSecond());
//				if (drp.getFirst().equals("moro")) {
//					dicReferences.add(drp.getSecond());
//				}
//			} catch (NumberFormatException e) {
//				if (drp.getFirst().equals("moro")) {
//					dicReferences.add(drp.getSecond());
//					// System.out.println("Dic: " + drp.getFirst() + ", Ref: " +
//					// drp.getSecond());
//					if (drp.getSecond().length() > maxlength) {
//						maxlength = drp.getSecond().length();
//						maxstring = drp.getSecond();
//					}
//
//					if (drp.getSecond().length() < minlength) {
//						minlength = drp.getSecond().length();
//						minstring = drp.getSecond();
//					}
//					i++;
//				}
//			}
//
//		}
//
//		int j = 0;
//		for (String s : dicReferences) {
//			j++;
//
//			System.out.println(s);
//
//		}
//
//		System.out.println("[" + i + "]" + " maxlength = " + maxlength + "-" + maxstring + ", minlength = " + minlength + "-" + minstring);
//
//		db.close();
//	}

}
