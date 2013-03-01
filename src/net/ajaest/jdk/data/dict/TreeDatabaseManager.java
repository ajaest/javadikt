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

package net.ajaest.jdk.data.dict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ajaest.jdk.core.main.ExceptionHandler;
import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.core.main.ExceptionHandler.KNOWN_EXCEPTIONS;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.DatabaseManager;
import net.ajaest.jdk.data.dict.auxi.KanjiDatabaseInfo;
import net.ajaest.jdk.data.dict.auxi.ORQueue;
import net.ajaest.jdk.data.dict.auxi.TreeContainer;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.KanjiQuery;
import net.ajaest.jdk.data.dict.sort.KanjiSortExpression;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;

import org.neodatis.odb.CorruptedDatabaseException;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.query.criteria.Or;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

/**
 * This class provides methods to manage a kanji database.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class TreeDatabaseManager implements
		DatabaseManager<KanjiTag, KanjiExpression> {

	private KanjiDatabaseInfo 		kdbi;
	
	private ODB 					kanjiDb;
	private Objects<KanjiTag> 		kanjis;
	private CriteriaQuery 			cq;
	
	private TreeContainer			kanjiTree;
	
	private Set<Integer> 			previousSearchRefs;
	private Set<KanjiTag>			previousSearch;

	private FrequencyRefComparator 	frc;

	public TreeDatabaseManager(String zobjPath, String jdkPath)
			throws IOException {
		this(new File(zobjPath), new File(jdkPath));
		frc = new FrequencyRefComparator(this.kanjiTree);
	}

	public TreeDatabaseManager(File zobj, File jdk) {

		Messages.localePrintln("**TreeDatabaseManager: loading kanji database");
		initKanjiDB(jdk);
		Messages.localePrintln("**TreeDatabaseManager: loading search trees");
		initTrees(zobj);
	}

	public Set<Integer> executeRefQuery(KanjiExpression query) {

		ORQueue oq = new ORQueue(kanjiTree, query);

		previousSearchRefs = oq.getRefs();

		return previousSearchRefs;
	}

	@Override
	public Set<KanjiTag> executeQuery(KanjiExpression query) {
		
		executeRefQuery(query);
		
		Or or = Where.or();
		for (Integer unicode : previousSearchRefs) {
			or.add(Where.equal("unicode", unicode));
		}

		kanjis = kanjiDb.getObjects(new CriteriaQuery(Kanji.class, or));
		previousSearch = new HashSet<KanjiTag>();

		for (KanjiTag kt : kanjis) {
			previousSearch.add(kt);
		}

		return previousSearch;
	}

	public KanjiTag getKanji(Integer unicode) {

		cq = new CriteriaQuery(Kanji.class, Where.equal("unicode", unicode));
		kanjis = kanjiDb.getObjects(cq);
		
		return kanjis.size() != 0 ? kanjis.getFirst() : null;
	}


	public KanjiDatabaseInfo getKanjiDatabaseInformation() {
		return kdbi;
	}

	public ODB getLowLevelKanjiDatabaseManager() {
		return kanjiDb;
	}

	public TreeContainer getTreeContainer() {
		return kanjiTree;
	}

	public void sortRefs(KanjiSortExpression kse, List<Integer> elems) {
		// TODO: implement criteria
		if (kse.getInvolvedFieldsInOrdering().size() > 1) {
			if (kse.size() != 1 || !kse.getInvolvedFieldsInOrdering().contains(KanjiFieldEnum.KANJI_FREQUENCY)) {
				throw new IllegalArgumentException("Yet not implemented sorting criteria: " + kse.getInvolvedFieldsInOrdering().get(0));
			}
		}

		Collections.sort(elems, this.frc);
	}

	@Override
	public void close() {

		kanjiTree = null;

		kanjiDb.commit();
		kanjiDb.close();
	}

	
	//#############################################################
	//############# Private methods ###############################
	//#############################################################

	private void initKanjiDB(File jdk) {

		if (!jdk.exists() || !jdk.isFile())
			ExceptionHandler.handleException(new FileNotFoundException(jdk.getName()), KNOWN_EXCEPTIONS.KANJIDB_IOEXCEPTION);

		kanjiDb = ODBFactory.open(jdk.getAbsolutePath());
		Objects<KanjiDatabaseInfo> kdbiObjs = kanjiDb.getObjects(KanjiDatabaseInfo.class);

		if (kdbiObjs.size() != 1) {
			ExceptionHandler.handleException(new CorruptedDatabaseException(NeoDatisError.UNEXPECTED_SITUATION, "There are multiples data headers or does not exists anyone."), KNOWN_EXCEPTIONS.KANJIDB_CORRUPTED);
		} else {
			this.kdbi = kdbiObjs.getFirst();
		}
	}

	private void initTrees(File zobj) {
		kanjiTree = new TreeContainer(zobj);
	}
	// #############################################################
	// ############# Private class #################################
	// #############################################################
	
	private class FrequencyRefComparator implements Comparator<Integer> {

		private Map<Integer, Integer> map;

		private FrequencyRefComparator(TreeContainer tc) {
			
			map = new HashMap<Integer,Integer>();
			for (Integer key : tc.getKanjiFrequencyTreeSet().keySet()) {
				map.put(tc.getKanjiFrequencyTreeSet().get(key), key);
			}
		}

		@Override
		public int compare(Integer o1, Integer o2) {

			int compare;

			Integer ref1 = map.get(o1);
			Integer ref2 = map.get(o2);

			if (ref1 != null && ref2 != null) {
				compare = ref1.compareTo(ref2);
			} else if (ref1 == null) {
				compare = 1;
			} else
				compare = -1;

			return compare;
		}

	}

	public static void main(String... args) throws IOException {

		long t1, t2;

		t1 = System.currentTimeMillis();
		TreeDatabaseManager tdm = new TreeDatabaseManager("resources/data/trees/trees.zobj", "resources/data/dict/kanjidict.jdk");
		t2 = System.currentTimeMillis();
		System.out.println("Database loaded: " + ((float) (t2 - t1)) / 1000 + "s");

		List<KanjiTag> kts = new ArrayList<KanjiTag>();

		t1 = System.currentTimeMillis();
		List<Character> seq = new ArrayList<Character>();
		for (char c : "sol".toCharArray()) {
			seq.add(new Character(c));
		}
		KanjiExpression ke= new KanjiQuery().meaning().equal("sol");
		Set<Integer> mean = tdm.executeRefQuery(ke);
		
		Objects<KanjiTag> objs;
		for (Integer unicode : mean) {
			objs = tdm.kanjiDb.getObjects(new CriteriaQuery(Kanji.class, Where.equal("unicode", unicode)));
			kts.addAll(objs);
		}
		t2 = System.currentTimeMillis();
		System.out.println("New database meaning query time: " + ((float) (t2 - t1)) / 1000 + "s");
		System.out.println(kts);
		kts = new ArrayList<KanjiTag>();

		t1 = System.currentTimeMillis();

		Objects<MeaningEntry> mes = tdm.kanjiDb.getObjects(new CriteriaQuery(MeaningEntry.class, Where.contain("elements", "sol")));

		for (MeaningEntry me : mes) {
			objs = tdm.kanjiDb.getObjects(new CriteriaQuery(Kanji.class, Where.equal("unicode", me.getUnicodeRef())));
			kts.addAll(objs);
		}

		t2 = System.currentTimeMillis();
		System.out.println("Old database meaning query time: " + ((float) (t2 - t1)) / 1000 + "s");

		System.out.println(kts);

	}
}
