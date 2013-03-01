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

package net.ajaest.jdk.tools.test.data.kanji;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;
import net.ajaest.jdk.data.auxi.KanjiComparator;
import net.ajaest.jdk.data.dict.KanjiDict;
import net.ajaest.jdk.data.dict.query.KanjiQuery;
import net.ajaest.jdk.data.kanji.KanjiTag;

//TODO: JavaDoc
public class KanjiComparatorTest extends TestCase {

	private List<KanjiTag> kanjiSet;
	private SortedSet<KanjiTag> kanjiSortedSet;

	// _____________________________________________________________________________
	// _____________________________________________________________________________
	
	public KanjiComparatorTest() throws IOException {

		KanjiDict kd = new KanjiDict("dict/kanjidict.jdk", "dict/strokedata.txt");

		// kanjiSet = kd.executeQuery(new
		// KanjiQuery().reading().equal("うえ").OR().classic_nelson().equal(49).OR().skip_code().isNull().AND().classic_nelson().lessThan(10));

		kanjiSet = kd.executeQuery(new KanjiQuery().unicode_value().greatherThan(0));

		// for (KanjiTag kt : kanjiSet)
		// System.out.println(kt.formattedDescriptionString());

		// System.out.println(kanjiSet.size());
		kd.close();
	}

	// _____________________________________________________________________________
	// _____________________________________________________________________________



	// _____________________________________________________________________________
	// _____________________________________________________________________________

	public void testUnicodeComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createUnicodeComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current = null;
		while (it.hasNext()) {
			current = it.next();
			if (current.getUnicodeRef() == null)
				break;
			azzert = azzert && (previous.getUnicodeRef().compareTo(current.getUnicodeRef()) <= 0);
			if (azzert == false) {
				System.out.println("Unicode: " + previous.getUnicodeRef() + " " + current.getUnicodeRef());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getUnicodeRef() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("Unicode「null」: " + previous.getUnicodeRef() + " " + current.getUnicodeRef());
			}
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createUnicodeComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getUnicodeRef() == null)
				break;
			azzert = azzert && (previous.getUnicodeRef().compareTo(current.getUnicodeRef()) >= 0);
			if (azzert == false) {
				System.out.println("Unicode「inverse」: " + previous.getUnicodeRef() + " " + current.getUnicodeRef());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getUnicodeRef() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("Unicode「inverse,null」: " + previous.getUnicodeRef() + " " + current.getUnicodeRef());
			}
		}

		assertTrue(azzert);
	}

	public void testJISCharsetComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createJISCharsetComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current;
		while (it.hasNext()) {
			current = it.next();
			if (current.getJisCode().getFirst() == null)
				break;
			azzert = azzert && previous.getJisCode().getFirst().compareTo(current.getJisCode().getFirst()) <= (0);
			if (azzert == false) {
				System.out.println("JIS charset: " + previous.getJisCode().getFirst() + " " + current.getJisCode().getFirst());
			}
			previous = current;
		}

		while (it.hasNext()) {
			current = it.next();
			azzert = azzert && current.getJisCode().getFirst() == null;
			previous = current;
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createJISCharsetComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getJisCode().getFirst() == null)
				break;
			azzert = azzert && previous.getJisCode().getFirst().compareTo(current.getJisCode().getFirst()) >= (0);
			if (azzert == false) {
				System.out.println("JIS charset「inverse」: " + previous.getJisCode().getFirst() + " " + current.getJisCode().getFirst());
			}
			previous = current;
		}

		while (it.hasNext()) {
			current = it.next();
			azzert = azzert && current.getJisCode().getFirst() == null;
			previous = current;
		}

		assertTrue(azzert);
	}

	public void testJISCodeComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createJISCodeComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current;
		while (it.hasNext()) {
			current = it.next();
			if (current.getJisCode().getSecond() == null)
				break;
			azzert = azzert &&    (previous.getJisCode().getSecond().length() < previous.getJisCode().getSecond().length() ||
							  ( (!(previous.getJisCode().getSecond().length() < previous.getJisCode().getSecond().length())) &&
							 	   previous.getJisCode().getSecond().compareTo(current.getJisCode().getSecond()) <= (0)));
			if (azzert == false) {
				System.out.println("JIS code: " + previous.getJisCode().getSecond() + " " + current.getJisCode().getSecond());
			}
			previous = current;
		}

		while (it.hasNext()) {
			current = it.next();
			azzert = azzert && current.getJisCode().getSecond() == null;
			previous = current;
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createJISCharsetComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getJisCode().getSecond() == null)
				break;
			azzert = azzert &&  (previous.getJisCode().getSecond().length() > previous.getJisCode().getSecond().length() ||
					  		( (!(previous.getJisCode().getSecond().length() > previous.getJisCode().getSecond().length())) &&
					  			 previous.getJisCode().getSecond().compareTo(current.getJisCode().getSecond()) >= (0)));
			if (azzert == false) {
				System.out.println("JIS code「inverse」: " + previous.getJisCode().getSecond() + " " + current.getJisCode().getSecond());
			}
			previous = current;
		}

		while (it.hasNext()) {
			current = it.next();
			azzert = azzert && current.getJisCode().getSecond() == null;
			previous = current;
		}

		assertTrue(azzert);
	}

	public void testNelsonRadicalComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createNelsonRadicalComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current = null;
		while (it.hasNext()) {
			current = it.next();
			if (current.getNelsonRadical() == null)
				break;
			azzert = azzert && (previous.getNelsonRadical().compareTo(current.getNelsonRadical()) <= 0);
			if (azzert == false) {
				System.out.println("Nelson radical: " + previous.getNelsonRadical() + " " + current.getNelsonRadical());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getNelsonRadical() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("Nelson radical「null」: " + previous.getNelsonRadical() + " " + current.getNelsonRadical());
			}
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createNelsonRadicalComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getNelsonRadical() == null)
				break;
			azzert = azzert && (previous.getNelsonRadical().compareTo(current.getNelsonRadical()) >= 0);
			if (azzert == false) {
				System.out.println("Nelson radical「inverse」: " + previous.getNelsonRadical() + " " + current.getNelsonRadical());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getNelsonRadical() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("Nelson radical「inverse,null」: " + previous.getNelsonRadical() + " " + current.getNelsonRadical());
			}
		}

		assertTrue(azzert);
	}

	public void testClassicRadicalComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createClassicRadicalComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current = null;
		while (it.hasNext()) {
			current = it.next();
			if (current.getClassicRadical().getNumber() == null)
				break;
			azzert = azzert && (previous.getClassicRadical().getNumber().compareTo(current.getClassicRadical().getNumber()) <= 0);
			if (azzert == false) {
				System.out.println("classic radical: " + previous.getClassicRadical().getNumber() + " " + current.getClassicRadical().getNumber());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getClassicRadical().getNumber() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("classic radical「null」: " + previous.getClassicRadical().getNumber() + " " + current.getClassicRadical().getNumber());
			}
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createClassicRadicalComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getClassicRadical().getNumber() == null)
				break;
			azzert = azzert && (previous.getClassicRadical().getNumber().compareTo(current.getClassicRadical().getNumber()) >= 0);
			if (azzert == false) {
				System.out.println("classic radical「inverse」: " + previous.getClassicRadical().getNumber() + " " + current.getClassicRadical().getNumber());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getClassicRadical().getNumber() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("classic radical「inverse,null」: " + previous.getClassicRadical().getNumber() + " " + current.getClassicRadical().getNumber());
			}
		}

		assertTrue(azzert);
	}

	public void testGradeComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createGradeComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current = null;
		while (it.hasNext()) {
			current = it.next();
			if (current.getGrade() == null)
				break;
			azzert = azzert && (previous.getGrade().compareTo(current.getGrade()) <= 0);
			if (azzert == false) {
				System.out.println("Grade: " + previous.getGrade() + " " + current.getGrade());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getGrade() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("Grade「null」: " + previous.getGrade() + " " + current.getGrade());
			}
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createGradeComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getGrade() == null)
				break;
			azzert = azzert && (previous.getGrade().compareTo(current.getGrade()) >= 0);
			if (azzert == false) {
				System.out.println("Grade「inverse」: " + previous.getGrade() + " " + current.getGrade());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getGrade() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("Grade「inverse,null」: " + previous.getGrade() + " " + current.getGrade());
			}
		}

		assertTrue(azzert);
	}

	public void testStrokeCountComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createGradeComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current = null;
		while (it.hasNext()) {
			current = it.next();
			if (current.getGrade() == null)
				break;
			azzert = azzert && (previous.getGrade().compareTo(current.getGrade()) <= 0);
			if (azzert == false) {
				System.out.println("StrokeCount: " + previous.getGrade() + " " + current.getGrade());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getGrade() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("StrokeCount「null」: " + previous.getGrade() + " " + current.getGrade());
			}
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createGradeComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getGrade() == null)
				break;
			azzert = azzert && (previous.getGrade().compareTo(current.getGrade()) >= 0);
			if (azzert == false) {
				System.out.println("StrokeCount「inverse」: " + previous.getGrade() + " " + current.getGrade());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getGrade() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("StrokeCount「inverse,null」: " + previous.getGrade() + " " + current.getGrade());
			}
		}

		assertTrue(azzert);
	}

	public void testFrequencyComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createFrequencyComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current = null;
		while (it.hasNext()) {
			current = it.next();
			if (current.getFrequency() == null)
				break;
			azzert = azzert && (previous.getFrequency().compareTo(current.getFrequency()) <= 0);
			if (azzert == false) {
				System.out.println("Frequency: " + previous.getFrequency() + " " + current.getFrequency());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getFrequency() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("Frequency「null」: " + previous.getFrequency() + " " + current.getFrequency());
			}
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createFrequencyComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getFrequency() == null)
				break;
			azzert = azzert && (previous.getFrequency().compareTo(current.getFrequency()) >= 0);
			if (azzert == false) {
				System.out.println("Frequency「inverse」: " + previous.getFrequency() + " " + current.getFrequency());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getFrequency() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("Frequency「inverse,null」: " + previous.getFrequency() + " " + current.getFrequency());
			}
		}

		assertTrue(azzert);
	}

	public void testSKIPComparator() {

		boolean azzert = true;

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createSKIPComparator(false));
		kanjiSortedSet.addAll(kanjiSet);

		Iterator<KanjiTag> it = kanjiSortedSet.iterator();

		KanjiTag previous = it.next();
		KanjiTag current = null;
		while (it.hasNext()) {
			current = it.next();
			if (current.getQueryCodes().getSkipCode() == null)
				break;
			azzert = azzert && (previous.getQueryCodes().getSkipCode().compareTo(current.getQueryCodes().getSkipCode()) <= 0);
			if (azzert == false) {
				System.out.println("SKIP: " + previous.getQueryCodes().getSkipCode() + " " + current.getQueryCodes().getSkipCode());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getQueryCodes().getSkipCode() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("SKIP「null」: " + previous.getQueryCodes().getSkipCode() + " " + current.getQueryCodes().getSkipCode());
			}
		}

		// inverse

		kanjiSortedSet = new TreeSet<KanjiTag>(KanjiComparator.createSKIPComparator(true));
		kanjiSortedSet.addAll(kanjiSet);

		it = kanjiSortedSet.iterator();

		previous = it.next();
		while (it.hasNext()) {
			current = it.next();
			if (current.getQueryCodes().getSkipCode() == null)
				break;
			azzert = azzert && (previous.getQueryCodes().getSkipCode().compareTo(current.getQueryCodes().getSkipCode()) >= 0);
			if (azzert == false) {
				System.out.println("SKIP「inverse」: " + previous.getQueryCodes().getSkipCode() + " " + current.getQueryCodes().getSkipCode());
			}
			previous = current;
		}

		while (it.hasNext()) {
			azzert = azzert && current.getQueryCodes().getSkipCode() == null;
			current = it.next();
			if (azzert == false) {
				System.out.println("SKIP「inverse,null」: " + previous.getQueryCodes().getSkipCode() + " " + current.getQueryCodes().getSkipCode());
			}
		}

		assertTrue(azzert);
	}

	// TODO; IMPORTANT,these tests are not finished!

}
