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

package net.ajaest.jdk.tools.test.data.auxi;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.tools.parsers.StrokeOrderParser;
import net.ajaest.lib.data.SequenceTree;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

//TODO: JavaDoc
public class testSequenceTree extends TestCase {

	public static List<AllowedStrokeLineEnum> getLineSequence(KanjiGraph kg) {

		List<AllowedStrokeLineEnum> sequence = new ArrayList<AllowedStrokeLineEnum>();

		for (KanjiStroke ks : kg.getStrokes()) {
			for (AllowedStrokeLineEnum asl : ks.getStrokeList()) {

				sequence.add(asl);
			}
		}

		return sequence;
	}
	public static void main(String... args) throws Exception {

		testSequenceTree t1 = new testSequenceTree();
		SequenceTree<AllowedStrokeLineEnum, Integer> test;
		t1.setUp();
		test = t1.ost;

		ODB odb = ODBFactory.open("resources/test/aaa.odb");
		odb.store(test);

	}

	// ***************************************
	// ///////////////////////////////////////
	// ///////// JUnit override methods
	// ///////////////////////////////////////
	// ***************************************

	SequenceTree<AllowedStrokeLineEnum, Integer> ost = null;

	SequenceTree<AllowedStrokeLineEnum, Integer> temp = null;

	public void printSequenceTree(SequenceTree<AllowedStrokeLineEnum, Integer> st) {

		StringBuilder b = new StringBuilder("ordered by ");
		b.append(st.getSequenceValue());
		b.append(": [");

		char c;
		for (Integer obj : st.getFinalNodes()) {
			c = (char) obj.intValue();
			b.append(c);
			b.append(", ");
		}
		if (b.lastIndexOf(",") == -1)
			b.append(']');
		else
			b.setCharAt(b.length() - 1, ']');

		System.out.println(b.toString());
	}
	// ***************************************
	// ///////////////////////////////////////
	// ///////// Test constructors
	// ///////////////////////////////////////
	// ***************************************

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// void tree
		this.temp = new SequenceTree<AllowedStrokeLineEnum, Integer>(false);

		// full tree
		this.ost = new SequenceTree<AllowedStrokeLineEnum, Integer>(false);

		Map<Integer, KanjiGraph> kgmap = new StrokeOrderParser(
				"resources/data/dict/strokedata.txt", false).parse();

		List<AllowedStrokeLineEnum> sequence= null;
		for (Integer i : kgmap.keySet()) {
			sequence = getLineSequence(kgmap.get(i));

			this.ost.add(sequence, i);
		}

		File testDatabase = new File("resources/test/testSequenceTree.odb");
		if (testDatabase.exists())
			testDatabase.delete();

		ODB odb = ODBFactory.open("resources/test/testSequenceTree.odb");
		odb.store(this.ost);

		CriteriaQuery mainStrokeTree = new CriteriaQuery(SequenceTree.class, Where.isNull("parent"));
		Objects<SequenceTree<AllowedStrokeLineEnum, Integer>> graphTreeResult = odb.getObjects(mainStrokeTree);

		if (testDatabase.exists())
			testDatabase.delete();

		System.out.println(graphTreeResult.size());
		odb.close();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	// ***************************************
	// ///////////////////////////////////////
	// ///////// Support methods
	// ///////////////////////////////////////
	// ***************************************

	// ***************************************
	// ///////////////////////////////////////
	// ///////// Test methods
	// ///////////////////////////////////////
	// ***************************************



	public void testAdd() {

		SequenceTree<AllowedStrokeLineEnum, Integer> test = this.temp.getSubTree(AllowedStrokeLineEnum.B);

		boolean azzert = (test == null);

		List<AllowedStrokeLineEnum> asl = new ArrayList<AllowedStrokeLineEnum>();
		asl.add(AllowedStrokeLineEnum.B);
		asl.add(AllowedStrokeLineEnum.B);
		asl.add(AllowedStrokeLineEnum.F);
		asl.add(AllowedStrokeLineEnum.A);
		asl.add(AllowedStrokeLineEnum.A);
		asl.add(AllowedStrokeLineEnum.C);
		asl.add(AllowedStrokeLineEnum.B);
		asl.add(AllowedStrokeLineEnum.A);
		asl.add(AllowedStrokeLineEnum.F);
		asl.add(AllowedStrokeLineEnum.F);
		asl.add(AllowedStrokeLineEnum.B);
		asl.add(AllowedStrokeLineEnum.A);
		asl.add(AllowedStrokeLineEnum.C);

		this.temp.add(asl, (int) '案');

		test = this.temp.getSubTree(AllowedStrokeLineEnum.B);
		test = test.getSubTree(AllowedStrokeLineEnum.B);
		test = test.getSubTree(AllowedStrokeLineEnum.F);
		test = test.getSubTree(AllowedStrokeLineEnum.A);
		test = test.getSubTree(AllowedStrokeLineEnum.A);
		test = test.getSubTree(AllowedStrokeLineEnum.C);
		test = test.getSubTree(AllowedStrokeLineEnum.B);
		test = test.getSubTree(AllowedStrokeLineEnum.A);
		test = test.getSubTree(AllowedStrokeLineEnum.F);
		test = test.getSubTree(AllowedStrokeLineEnum.F);
		test = test.getSubTree(AllowedStrokeLineEnum.B);
		test = test.getSubTree(AllowedStrokeLineEnum.A);
		test = test.getSubTree(AllowedStrokeLineEnum.C);

		azzert = azzert && (test.getFinalNodes().size() != 0);
		azzert = azzert && test.getFinalNodes().get(0).equals((int) '案');

		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		test = test.getParent();
		// test = test.getSubTree(AllowedStrokeLineEnum.C);

		azzert = azzert && test.equals(this.temp) && (test.getParent() == null);

		assertTrue(azzert);

	}


	public void testAddFinalNode() {

		int previousSize = this.temp.getFinalNodes().size();

		this.temp.addFinalNode(32);

		boolean azzert = this.temp.getFinalNodes().size() == previousSize + 1;
		azzert = azzert && (this.temp.getFinalNodes().get(this.temp.getFinalNodes().size() - 1) == 32);

		assertTrue(azzert);

	}

	public void testAddSubTree() {

		boolean azzert = this.temp.getSubTree(AllowedStrokeLineEnum.A) == null;
		this.temp.addSubTree(AllowedStrokeLineEnum.A, this.ost);
		azzert = azzert && this.temp.getSubTree(AllowedStrokeLineEnum.A).equals(this.ost);

		assertTrue(azzert);
	}

	public void testConstructor() {

		boolean azzert = true;

		this.temp = new SequenceTree<AllowedStrokeLineEnum, Integer>(true);

		azzert = azzert && this.temp.getFinalNodes().getClass().equals(LinkedList.class);

		this.temp = new SequenceTree<AllowedStrokeLineEnum, Integer>(false);

		azzert = azzert && this.temp.getFinalNodes().getClass().equals(ArrayList.class);

		assertTrue(azzert);

	}
	public void testGetParent() {

		this.temp.addSubTree(AllowedStrokeLineEnum.E, this.ost);

		assertEquals(this.temp, this.ost.getParent());
	}

	public void testGetSubTrees() {

		this.temp.addSubTree(AllowedStrokeLineEnum.E, this.ost);

		assertEquals(this.temp.getSubTrees().get(AllowedStrokeLineEnum.E), this.ost);
	}

	public void testRemoveFinalNodeE() {

		this.temp.addFinalNode(1);
		this.temp.addFinalNode(2);
		this.temp.addFinalNode(666);

		int previousSize = this.temp.getFinalNodes().size();

		boolean azzert = !this.temp.removeFinalNode(new Integer(4));

		azzert = azzert && this.temp.removeFinalNode(new Integer(2));

		azzert = azzert && (this.temp.getFinalNodes().size() == previousSize - 1);

		azzert = azzert && (this.temp.getFinalNodes().get(0) == 1);
		azzert = azzert && (this.temp.getFinalNodes().get(1) == 666);

		assertTrue(azzert);
	}

	public void testRemoveFinalNodeInt() {

		this.temp.addFinalNode(1);
		this.temp.addFinalNode(2);
		this.temp.addFinalNode(666);

		int previousSize = this.temp.getFinalNodes().size();

		boolean azzert = this.temp.getFinalNodes().get(1) == 2;

		this.temp.removeFinalNode(1);

		azzert = azzert && (this.temp.getFinalNodes().size() == previousSize - 1);

		azzert = azzert && (this.temp.getFinalNodes().get(0) == 1);
		azzert = azzert && (this.temp.getFinalNodes().get(1) == 666);

		assertTrue(azzert);

	}

	public void testRemoveSubTree() {

		this.temp.addSubTree(AllowedStrokeLineEnum.A, this.ost);
		boolean azzert = this.temp.getSubTree(AllowedStrokeLineEnum.A).equals(this.ost);

		this.temp.removeSubTree(AllowedStrokeLineEnum.A);

		azzert = azzert && (this.temp.getSubTree(AllowedStrokeLineEnum.A) == null);

		assertTrue(azzert);
	}

	public void testSearch() {

		// complicated existing sequence
		List<AllowedStrokeLineEnum> 案 = new ArrayList<AllowedStrokeLineEnum>();

		案.add(AllowedStrokeLineEnum.B);
		案.add(AllowedStrokeLineEnum.B);
		案.add(AllowedStrokeLineEnum.F);
		案.add(AllowedStrokeLineEnum.A);
		案.add(AllowedStrokeLineEnum.A);
		案.add(AllowedStrokeLineEnum.C);
		案.add(AllowedStrokeLineEnum.B);
		案.add(AllowedStrokeLineEnum.A);
		案.add(AllowedStrokeLineEnum.F);
		案.add(AllowedStrokeLineEnum.F);
		案.add(AllowedStrokeLineEnum.B);
		案.add(AllowedStrokeLineEnum.A);
		案.add(AllowedStrokeLineEnum.C);

		List<Integer> results = this.ost.search(案);

		boolean azzert = results.contains((int) '案');

		// first level
		List<AllowedStrokeLineEnum> 一 = new ArrayList<AllowedStrokeLineEnum>();
		// ichi kanji
		一.add(AllowedStrokeLineEnum.F);

		results = this.ost.search(一);

		azzert = azzert && results.contains((int) '一');

		// void search, returns the current sequencetree final nodes

		List<AllowedStrokeLineEnum> voiz = new ArrayList<AllowedStrokeLineEnum>();

		this.ost.addFinalNode(666);
		results = this.ost.search(voiz);

		azzert = azzert && (results.size() == 1);
		azzert = azzert && (results.get(0) == 666);

		assertTrue(azzert);

		// no exists, return void

		List<AllowedStrokeLineEnum> noexists = new ArrayList<AllowedStrokeLineEnum>();
		noexists.add(AllowedStrokeLineEnum.F);
		noexists.add(AllowedStrokeLineEnum.F);
		noexists.add(AllowedStrokeLineEnum.F);
		noexists.add(AllowedStrokeLineEnum.F);

		results = this.ost.search(noexists);

		azzert = azzert && (results.size() == 0);

		assertTrue(azzert);
	}

	public void testSetFinalNode() {


		boolean azzert = false;

		try {
			this.temp.setFinalNode(2, 32);
		} catch (IndexOutOfBoundsException e) {
			azzert = true;
		}

		this.temp.addFinalNode(1);
		this.temp.addFinalNode(2);
		this.temp.addFinalNode(666);

		this.temp.setFinalNode(2, 32);

		azzert = azzert && (this.temp.getFinalNodes().get(2) != 666);
		azzert = azzert && (this.temp.getFinalNodes().get(2) == 32);

		assertTrue(azzert);
	}

}
