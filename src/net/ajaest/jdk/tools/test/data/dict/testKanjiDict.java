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

package net.ajaest.jdk.tools.test.data.dict;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import net.ajaest.jdk.data.auxi.AllowedStrokeClueEnum;
import net.ajaest.jdk.data.auxi.AllowedStrokePointEnum;
import net.ajaest.jdk.data.auxi.KanjiReference;
import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.auxi.KanjiStrokeClue;
import net.ajaest.jdk.data.dict.KanjiDict;
import net.ajaest.jdk.data.dict.auxi.TreeContainer;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.KanjiQuery;
import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.JISPair;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiQueryCodes;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.jdk.data.kanji.VariantPair;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.And;
import org.neodatis.odb.core.query.criteria.Or;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

//TODO: JavaDoc
public class testKanjiDict extends TestCase {

	private KanjiDict data = null;
	private TreeContainer tree = null;
	private final String kanjPath = "resources/data/dict/kanjidict.jdk";
	private final String strokePath = "resources/data/trees/trees.zobj";

	private <E> List<E> executeNeodatisQuery(CriteriaQuery qc) {

		ODB lldb = this.data.getKanjiDatabaseManager().getLowLevelKanjiDatabaseManager();
		Objects<E> obs = lldb.getObjects(qc);

		List<E> ret = new ArrayList<E>();

		for (E obj : obs)
			ret.add(obj);

		return ret;
	}

	private List<KanjiTag> getKanjis(List<KanjiReference> kr) {

		ODB lldb = this.data.getKanjiDatabaseManager().getLowLevelKanjiDatabaseManager();

		Or or = Where.or();

		for (KanjiReference obj : kr) {
			or.add(Where.equal("unicode", obj.getUnicodeRef()));
		}

		Objects<KanjiTag> obs = lldb.getObjects(new CriteriaQuery(Kanji.class, or));

		List<KanjiTag> ret = new ArrayList<KanjiTag>();

		for (KanjiTag obj : obs)
			ret.add(obj);

		return ret;

	}

	// ***************************************
	// ///////////////////////////////////////
	// ///////// Support methods /////////////
	// ///////////////////////////////////////
	// ***************************************

	private List<AllowedStrokeLineEnum> getLineSequence(KanjiGraph kg) {

		List<AllowedStrokeLineEnum> sequence = new ArrayList<AllowedStrokeLineEnum>();

		for (KanjiStroke ks : kg.getStrokes()) {
			for (AllowedStrokeLineEnum asl : ks.getStrokeList()) {

				sequence.add(asl);
			}
		}

		return sequence;
	}

	private List<KanjiReference> getReferencesFromIntegers(List<Integer> ref) {

		List<KanjiReference> kr = new ArrayList<KanjiReference>();

		for (final Integer i : ref) {
			kr.add(new KanjiReference() {

				@Override
				public Integer getUnicodeRef() {

					return i;
				}
			});
		}

		return kr;

	}
	// ***************************************
	// ///////////////////////////////////////
	// ///////// Test constructors ///////////
	// ///////////////////////////////////////
	// ***************************************

	// ***************************************
	// ///////////////////////////////////////
	// ///////// JUnit override methods //////
	// ///////////////////////////////////////
	// ***************************************
	@Override
	protected void setUp() throws Exception {
		this.data = new KanjiDict(this.kanjPath, this.strokePath);
		this.tree = this.data.getKanjiDatabaseManager().getTreeContainer();
	}

	@Override
	protected void tearDown() throws Exception {
		this.data.close();
	}

	public void testExecuteQuery() {

		long t1, t2, partial1, partial2;
		List<KanjiTag> temp = null;
		List<KanjiTag> expected = null;
		List<KanjiReference> tempByReference = null;
		KanjiExpression kq;


		////////////////////
		////Simple query, one or more elements per query
		////////////////////
		boolean activeSimpleQuery = true;
		if (activeSimpleQuery) {
			partial1 = System.currentTimeMillis();
			////////////////////
			System.out.println("\t--Simple query, one or more elements per query");
			// **nelson radical
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().classic_nelson().equal(4);
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.equal("nelsonRadical", 4)));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**nelson radical. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **classic radical
			// t1 = System.currentTimeMillis();
			// kq = new KanjiQuery().classic_radical().equal(4);
			// temp = data.executeQuery(kq);
			// expected = executeNeodatisQuery(new CriteriaQuery(Kanji.class,
			// Where.equal("classicRadical", 4)));
			// assertTrue(temp.size() > 0);
			// assertEquals(new HashSet<KanjiTag>(expected), new
			// HashSet<KanjiTag>(temp));
			// t2 = System.currentTimeMillis();
			// System.out.println("\t\t**classic radical. Time elapsed: " +
			// ((float) (t2 - t1)) / 1000 + "s");

			// **de Roo Code
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().De_Roo_code().equal("1262");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(KanjiQueryCodes.class, Where.equal("deRoo", "1262")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**de Roo Code. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **Contains certain dictionary reference name
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().dictionary_name().equal("crowley");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(DicReferencePair.class, Where.equal("first", "crowley")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**Contains certain dictionary reference name. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **Contains certain dictionary reference
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().dictionary_reference().equal(2991);
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(DicReferencePair.class, Where.equal("numRef", 2991)));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**Contains certain dictionary reference. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **four corner Code
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().four_corner_code().equal("2510.0");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(KanjiQueryCodes.class, Where.equal("fourCorner", "2510.0")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**four corner Code. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **frequency
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().frequency().equal(1);
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.equal("frequency", 1)));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**frequency. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **grade
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().grade().equal(1);
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.equal("grade", 1)));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**grade. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// // **JIS charset
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().JIS_charset().equal(JISPair.JIS_X_208);
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(JISPair.class, Where.equal("first", JISPair.JIS_X_208)));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**JIS charset. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **JIS code
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().JIS_code_value().equal("32-24");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(JISPair.class, Where.equal("second", "32-24")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**JIS code. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **JLPTLevel
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().JLPT_level().equal(2);
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.equal("JLPTLevel", 2)));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**JLPTLevel. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **Kanji graph
			List<AllowedStrokeLineEnum> tempS = new ArrayList<AllowedStrokeLineEnum>();
			List<KanjiStroke> ks = new ArrayList<KanjiStroke>();

			tempS.add(AllowedStrokeLineEnum.A);
			ks.add(new KanjiStroke(tempS, 0, 0));

			tempS = new ArrayList<AllowedStrokeLineEnum>();
			tempS.add(AllowedStrokeLineEnum.F);
			ks.add(new KanjiStroke(tempS, 1, 0));

			tempS = new ArrayList<AllowedStrokeLineEnum>();
			tempS.add(AllowedStrokeLineEnum.B);
			ks.add(new KanjiStroke(tempS, 2, 0));

			tempS = new ArrayList<AllowedStrokeLineEnum>();
			tempS.add(AllowedStrokeLineEnum.F);
			ks.add(new KanjiStroke(tempS, 3, 0));

			tempS = new ArrayList<AllowedStrokeLineEnum>();
			tempS.add(AllowedStrokeLineEnum.F);
			ks.add(new KanjiStroke(tempS, 4, 0));

			Set<KanjiStrokeClue> ksc = new HashSet<KanjiStrokeClue>();

			ksc.add(new KanjiStrokeClue(1, 2, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.begin, AllowedStrokeClueEnum.isAtLeftOf, 0));
			ksc.add(new KanjiStrokeClue(2, 3, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.ave, AllowedStrokeClueEnum.areIntheSamePoint, 0));

			KanjiGraph kg = new KanjiGraph(ks, ksc, 0);

			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().kanji_graph().equal(kg);
			temp = this.data.executeQuery(kq);
			tempByReference = new ArrayList<KanjiReference>(this.getReferencesFromIntegers(this.tree.getKanjiGraphTree().search(this.getLineSequence(kg))));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**Kanji graph. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **meaning languaje
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().meaning_language().equal("pt");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(MeaningEntry.class, Where.equal("key", "pt")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**meaning languaje. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **meaning
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().meaning().equal("vida");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(MeaningEntry.class, Where.contain("elements", "vida")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**meaning. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **reading type
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().reading_type().equal("nanori");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(ReadingEntry.class, Where.equal("key", "nanori")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**reading type. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **reading
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().reading().equal("きょう");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(ReadingEntry.class, Where.contain("elements", "きょう")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**reading. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **skip Code
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().skip_code().equal("4-5-2");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(KanjiQueryCodes.class, Where.equal("skip", "4-5-2")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**skip Code. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **Spahn-Hadamiztky code
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().SpahnーHadamizky_code().equal("0a5.29");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(KanjiQueryCodes.class, Where.equal("spahnHadamitzky", "0a5.29")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**Spahn-Hadamiztky code. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **stroke count
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().stroke_count().equal(12);
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.equal("strokeCount", 12)));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**stroke count. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **stroke miscount
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().stroke_miscount().equal(12);
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.contain("strokeMiscounts", 12)));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**stroke miscount. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **variant reference
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().variant_reference().equal("16-61");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(VariantPair.class, Where.equal("second", "16-61")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**variant reference. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **variant type
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().variant_type().equal("jis212");
			temp = this.data.executeQuery(kq);
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(VariantPair.class, Where.equal("first", "jis212")));
			expected = this.getKanjis(tempByReference);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**variant type. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **unicode value
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().unicode_value().equal((int) '海');
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.equal("unicode", (int) '海')));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**unicode value. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");
			////////////////////
			partial2 = System.currentTimeMillis();
			System.out.println("\t**Symple query. Time elapsed: " + ((float) (partial2 - partial1)) / 1000 + "s");
		}

		And tempAnd = null;
		Or tempOr = null;
		int previousQuerySize = -1;
		////////////////////
		// //Complex query, two or more elements per query
		////////////////////

		boolean activeComplexQuery = true;
		if (activeComplexQuery) {
			System.out.println("\t--Complex query, two or more elements per query");
			partial1 = System.currentTimeMillis();
			// **only kanji class value, And
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().unicode_value().lessThan((int) '海').AND().stroke_count().equal(4);
			temp = this.data.executeQuery(kq);
			tempAnd = Where.and();
			tempAnd.add(Where.lt("unicode", (int) '海'));
			tempAnd.add(Where.equal("strokeCount", 4));
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, tempAnd));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**multiple kanji class value, AND. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **only kanji class value, OR
			t1 = System.currentTimeMillis();
			previousQuerySize = temp.size();
			kq = new KanjiQuery().unicode_value().lessThan((int) '海').AND().stroke_count().equal(4);
			kq = kq.OR().unicode_value().greatherThan((int) '海').AND().stroke_count().equal(5);
			temp = this.data.executeQuery(kq);
			tempAnd = Where.and();
			tempAnd.add(Where.lt("unicode", (int) '海'));
			tempAnd.add(Where.equal("strokeCount", 4));
			tempOr = Where.or();
			tempOr.add(tempAnd);
			tempAnd = Where.and();
			tempAnd.add(Where.gt("unicode", (int) '海'));
			tempAnd.add(Where.equal("strokeCount", 5));
			tempOr.add(tempAnd);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, tempOr));
			assertTrue(temp.size() > 0);
			assertTrue(temp.size() > previousQuerySize);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**multiple kanji class value, OR. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **multiple kanji and no kanji class value, And
			t1 = System.currentTimeMillis();
			kq = new KanjiQuery().unicode_value().equalsOrLessThan((int) '努').AND().reading().equal("ド");
			kq = kq.AND().variant_reference().equal("16-61").AND().dictionary_reference().equal(717);
			kq = kq.AND().dictionary_name().equal("nelson_c").AND().meaning().equal("esfuerzo");
			kq = kq.AND().De_Roo_code().equal("1745");
			temp = this.data.executeQuery(kq);
			// this query can only define [努] kanji
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.equal("unicode", (int) '努')));
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**multiple kanji and no kanji class value, And. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **multiple kanji and no kanji class value, OR
			t1 = System.currentTimeMillis();

			List<AllowedStrokeLineEnum> tempS = new ArrayList<AllowedStrokeLineEnum>();
			List<KanjiStroke> ks = new ArrayList<KanjiStroke>();
			tempS.add(AllowedStrokeLineEnum.A);
			ks.add(new KanjiStroke(tempS, 0, 0));
			tempS = new ArrayList<AllowedStrokeLineEnum>();
			tempS.add(AllowedStrokeLineEnum.F);
			ks.add(new KanjiStroke(tempS, 1, 0));
			tempS = new ArrayList<AllowedStrokeLineEnum>();
			tempS.add(AllowedStrokeLineEnum.B);
			ks.add(new KanjiStroke(tempS, 2, 0));
			tempS = new ArrayList<AllowedStrokeLineEnum>();
			tempS.add(AllowedStrokeLineEnum.F);
			ks.add(new KanjiStroke(tempS, 3, 0));
			tempS = new ArrayList<AllowedStrokeLineEnum>();
			tempS.add(AllowedStrokeLineEnum.F);
			ks.add(new KanjiStroke(tempS, 4, 0));
			Set<KanjiStrokeClue> ksc = new HashSet<KanjiStrokeClue>();
			ksc.add(new KanjiStrokeClue(1, 2, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.begin, AllowedStrokeClueEnum.isAtLeftOf, 0));
			ksc.add(new KanjiStrokeClue(2, 3, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.ave, AllowedStrokeClueEnum.areIntheSamePoint, 0));
			KanjiGraph kg = new KanjiGraph(ks, ksc, 0);

			kq = new KanjiQuery().unicode_value().equalsOrLessThan((int) '努').AND().reading().equal("ド");
			kq = kq.OR().kanji_graph().equal(kg).AND().unicode_value().equaslOrGreatherThan((int) '生');
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.le("unicode", (int) '努')));
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(ReadingEntry.class, Where.contain("elements", "ド")));
			expected.retainAll(this.getKanjis(tempByReference));
			List<KanjiTag> expected1 = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.ge("unicode", (int) '生')));
			tempByReference = new ArrayList<KanjiReference>(this.getReferencesFromIntegers(this.tree.getKanjiGraphTree().search(this.getLineSequence(kg))));
			expected1.retainAll(this.getKanjis(tempByReference));
			expected.addAll(expected1);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**multiple kanji and no kanji class value, And. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			// **multiple kanji and no kanji class value,Query into Query

			KanjiExpression kq1 = new KanjiQuery().unicode_value().equalsOrLessThan((int) '努').AND().reading().equal("ド");
			KanjiExpression kq2 = new KanjiQuery().kanji_graph().equal(kg).AND().unicode_value().equaslOrGreatherThan((int) '生');
			kq = new KanjiQuery().expression(kq1).OR().expression(kq2);
			temp = this.data.executeQuery(kq);
			expected = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.le("unicode", (int) '努')));
			tempByReference = this.executeNeodatisQuery(new CriteriaQuery(ReadingEntry.class, Where.contain("elements", "ド")));
			expected.retainAll(this.getKanjis(tempByReference));
			expected1 = this.executeNeodatisQuery(new CriteriaQuery(Kanji.class, Where.ge("unicode", (int) '生')));
			tempByReference = new ArrayList<KanjiReference>(this.getReferencesFromIntegers(this.tree.getKanjiGraphTree().search(this.getLineSequence(kg))));
			expected1.retainAll(this.getKanjis(tempByReference));
			expected.addAll(expected1);
			assertTrue(temp.size() > 0);
			assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
			t2 = System.currentTimeMillis();
			System.out.println("\t\t**multiple kanji and no kanji class value,Query into Query. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

			////////////////////
			partial2 = System.currentTimeMillis();
			System.out.println("\t**complex query. Time elapsed: " + ((float) (partial2 - partial1)) / 1000 + "s");
		}

	}

	public void testExecuteQueryFromPrevious() {
		KanjiExpression kq = null;
		List<KanjiTag> temp;
		List<KanjiTag> expected = new ArrayList<KanjiTag>();
		expected.add(new Kanji('生'));

		List<AllowedStrokeLineEnum> tempS = new ArrayList<AllowedStrokeLineEnum>();
		List<KanjiStroke> ks = new ArrayList<KanjiStroke>();
		tempS.add(AllowedStrokeLineEnum.A);
		ks.add(new KanjiStroke(tempS, 0, 0));
		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.F);
		ks.add(new KanjiStroke(tempS, 1, 0));
		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.B);
		ks.add(new KanjiStroke(tempS, 2, 0));
		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.F);
		ks.add(new KanjiStroke(tempS, 3, 0));
		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.F);
		ks.add(new KanjiStroke(tempS, 4, 0));
		Set<KanjiStrokeClue> ksc = new HashSet<KanjiStrokeClue>();
		ksc.add(new KanjiStrokeClue(1, 2, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.begin, AllowedStrokeClueEnum.isAtLeftOf, 0));
		ksc.add(new KanjiStrokeClue(2, 3, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.ave, AllowedStrokeClueEnum.areIntheSamePoint, 0));
		KanjiGraph kg = new KanjiGraph(ks, ksc, 0);

		kq = new KanjiQuery().unicode_value().equalsOrLessThan((int) '努').AND().reading().equal("ド");
		kq = kq.OR().kanji_graph().equal(kg).AND().unicode_value().equaslOrGreatherThan((int) '生');
		temp = this.data.executeQuery(kq);

		kq = new KanjiQuery().meaning().equal("vida");
		temp = this.data.executeFromPrevious(kq);
		assertEquals(new HashSet<KanjiTag>(expected), new HashSet<KanjiTag>(temp));
	}

	// ***************************************
	// ///////////////////////////////////////
	// ///////// Test methods ////////////////
	// ///////////////////////////////////////
	// ***************************************

	public void testGetKanjiByUnicode() {

		int codePoint = '海';

		assertEquals(new Integer(codePoint), this.data.getKanjiByUnicode(codePoint).getUnicodeRef());

	}

	public void testKanjiDictFile() throws Exception {

		this.tearDown();

		File f = new File(this.kanjPath);
		File f1 = new File(this.strokePath);

		boolean azzert = true;

		this.data = new KanjiDict(f, f1);

		azzert = azzert && (this.data.getDatabaseInfo() != null);
		azzert = azzert && (this.data.getKanjiDatabaseManager().getTreeContainer() != null);

		assertTrue(azzert);
	}

	public void testKanjiDictString() throws Exception {

		this.tearDown();

		File f = new File(this.kanjPath);
		File f1 = new File(this.strokePath);

		boolean azzert = true;

		this.data = new KanjiDict(f.getPath(), f1.getPath());

		azzert = azzert && (this.data.getDatabaseInfo() != null);
		azzert = azzert && (this.data.getKanjiDatabaseManager().getTreeContainer() != null);

		assertTrue(azzert);
	}
}
