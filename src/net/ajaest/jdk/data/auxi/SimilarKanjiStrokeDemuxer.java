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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.ajaest.jdk.data.dict.KanjiDict;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.KanjiQuery;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;

//TODO: JavaDoc
public class SimilarKanjiStrokeDemuxer {

	private Map<KanjiStroke, Set<KanjiStroke>> similarityMap;
	private Set<List<KanjiStroke>> allowedkSubGraphs;
	private Set<KanjiGraph> allowedGraphs;
	
	public SimilarKanjiStrokeDemuxer(Map<KanjiStroke, Set<KanjiStroke>> similarityMap, Set<List<KanjiStroke>> allowedkSubGraphs, Set<KanjiGraph> allowedGraphs) {

		this.allowedkSubGraphs = allowedkSubGraphs;
		this.similarityMap = similarityMap;
		this.allowedGraphs = allowedGraphs;
	}


	public SortedSet<KanjiGraph> getSimilar(KanjiGraph kg, Integer graphCount) {
		
		SortedSet<KanjiGraph> similarGraph = getSimilar(kg);
		SortedSet<KanjiGraph> similarGraphSubSet = new TreeSet<KanjiGraph>(similarGraph.comparator());
		
		Iterator<KanjiGraph> similarIterator = similarGraph.iterator();
		
		int count = 0;
		while (similarIterator.hasNext() && count < graphCount) {
			similarGraphSubSet.add(similarIterator.next());
			count++;
		}

		return similarGraphSubSet;

	}

	public SortedSet<KanjiGraph> getSimilar(KanjiGraph kg) {

		SortedSet<KanjiGraph> similarGraph;
		
		Set<List<KanjiStroke>> similarStrokes = null;
		Set<KanjiStroke> demuxedStroke;
		for (KanjiStroke ks : kg.getStrokes()) {

			demuxedStroke = similarityMap.get(ks);

			if (demuxedStroke == null) {
				similarStrokes = new HashSet<List<KanjiStroke>>();
				break;
			} else {
				similarStrokes = expandStrokes(similarStrokes, demuxedStroke);
			}
		}

		similarGraph = translateToGraph(similarStrokes, kg);
		
		return similarGraph;
	}

	private SortedSet<KanjiGraph> translateToGraph(Set<List<KanjiStroke>> similarStrokes, KanjiGraph kg) {

		SortedSet<KanjiGraph> translated = new TreeSet<KanjiGraph>(new GraphSimilarityComparator(kg));

		KanjiGraph newKg;
		for (List<KanjiStroke> kss : similarStrokes) {

			if (kss.size() == kg.strokeCount()){
				newKg = new KanjiGraph(kss, kg.getStrokesClues(), kg.getUnicodeRef());

				if (allowedGraphs.contains(newKg))
					translated.add(newKg);
			}
		}

		return translated;
	}

	private Set<List<KanjiStroke>> expandStrokes(Set<List<KanjiStroke>> similarStrokes, Set<KanjiStroke> demuxedStroke) {

		Set<List<KanjiStroke>> expanded = new HashSet<List<KanjiStroke>>();

		List<KanjiStroke> expandedStroke;
		if (similarStrokes == null) {

			for (KanjiStroke ks : demuxedStroke) {
				expandedStroke = new ArrayList<KanjiStroke>();
				expandedStroke.add(ks);
				if (allowedkSubGraphs.contains(expandedStroke))
					expanded.add(expandedStroke);
			}
		} else {

			for (KanjiStroke ks : demuxedStroke) {
				for (List<KanjiStroke> current : similarStrokes) {
					expandedStroke = new ArrayList<KanjiStroke>(current);
					expandedStroke.add(ks);
					if (allowedkSubGraphs.contains(expandedStroke))
						expanded.add(expandedStroke);
				}
			}
		}

		return expanded;
	}

	private static KanjiGraph testKanjiGraph() {
		List<KanjiStroke> ks = new ArrayList<KanjiStroke>();

		ks.add(new KanjiStroke("B", 1, -1));
		ks.add(new KanjiStroke("Fb", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("Fb", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("Fb", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("A", 1, -1));
		ks.add(new KanjiStroke("B", 1, -1));
		ks.add(new KanjiStroke("A", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("B", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));
		ks.add(new KanjiStroke("F", 1, -1));

		Set<KanjiStrokeClue> ksc = new HashSet<KanjiStrokeClue>();

		ksc.add(new KanjiStrokeClue(1, 2, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.begin, AllowedStrokeClueEnum.isAtLeftOf, -1));
		ksc.add(new KanjiStrokeClue(2, 3, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.ave, AllowedStrokeClueEnum.areIntheSamePoint, -1));

		KanjiGraph kg = new KanjiGraph(ks, ksc, -1);

		return kg;
	}


	@SuppressWarnings("unchecked")
	public static void main(String... strings) throws FileNotFoundException, IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/data/subgraphset.obj"));
		Set<List<KanjiStroke>> allowedsubGraphs = (Set<List<KanjiStroke>>) ois.readObject();
		ois.close();

		ois = new ObjectInputStream(new FileInputStream("resources/data/strokemap.obj"));
		Map<KanjiStroke, Set<KanjiStroke>> similarityMap = (Map<KanjiStroke, Set<KanjiStroke>>) ois.readObject();
		ois.close();

		ois = new ObjectInputStream(new FileInputStream("resources/data/graphset.obj"));
		Set<KanjiGraph> allowedGraph = (Set<KanjiGraph>) ois.readObject();
		ois.close();

		SimilarKanjiStrokeDemuxer sksd = new SimilarKanjiStrokeDemuxer(similarityMap, allowedsubGraphs, allowedGraph);

		Set<KanjiGraph> similar = new TreeSet<KanjiGraph>(new GraphSimilarityComparator(testKanjiGraph())); 

		similar.addAll(sksd.getSimilar(testKanjiGraph()));

		for (KanjiGraph kg : similar) {
			System.out.println(kg);
		}

		System.out.println(similar.size());

		KanjiQuery kq = new KanjiQuery();
		KanjiExpression ke = null;


		for (KanjiGraph kg : similar) {
			ke = kq.kanji_graph().equal(kg);
			kq = ke.OR();
		}

		String kanjPath = "resources/data/kanjidict.jdk";
		String strokePath = "resources/data/strokedata.txt";

		KanjiDict data = new KanjiDict(kanjPath, strokePath);

		List<KanjiTag> result = data.executeQuery(ke);

		Set<KanjiTag> sortedResult = new TreeSet<KanjiTag>(new Comparator<KanjiTag>() {

			Comparator<KanjiGraph> comp = new GraphSimilarityComparator(testKanjiGraph());
			@Override
			public int compare(KanjiTag o1, KanjiTag o2) {

				return comp.compare(o1.getGraph(), o2.getGraph());
			}
		});

		sortedResult.addAll(result);

		for (KanjiTag kt : sortedResult) {

			System.out.println(kt + " " + kt.getGraph().getStrokes());
		}

	}
}
