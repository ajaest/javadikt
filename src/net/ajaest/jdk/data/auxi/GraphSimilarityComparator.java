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

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.ajaest.jdk.data.kanji.KanjiGraph;

//TODO: JavaDoc
public class GraphSimilarityComparator implements Comparator<KanjiGraph> {

	private Map<KanjiStroke, Map<KanjiStroke, Set<Integer>>> modelStrokeDistances;
	private Map<KanjiGraph, Integer> graphScoring;
	private Set<KanjiStroke> modelStrokes;
	private Integer modelDistances;
	private Integer modelLineCount;
	private Integer modelStrokecount;

	public GraphSimilarityComparator(KanjiGraph kg) {

		modelLineCount = getLineCount(kg);
		modelStrokecount = kg.strokeCount();
		modelDistances = calcBinomial(kg.strokeCount(), 2);
		modelStrokeDistances = getDistanceMap(kg);
		modelStrokes = modelStrokeDistances.keySet();
		graphScoring = new HashMap<KanjiGraph, Integer>();
		graphScoring.put(kg, 100);

	}

	@Override
	public int compare(KanjiGraph o1, KanjiGraph o2) {

		int compare;

		if (o1 == null)
			if (o2 == null)
				compare = 0;
			else
				compare = -1;
		else {
			if (o2 == null)
				compare = 1;
			else {
				Integer score1 = scoreGraph(o1);
				Integer score2 = scoreGraph(o2);

				compare = score1.compareTo(score2);
				
				// If two elements have the same scoring and are stored in a Set container, one of them will be removed.
				if (compare == 0 && !o1.equals(o2))
					compare = 1;
			}
		}

		return -compare;
	}

	private Integer scoreGraph(KanjiGraph kg) {

		Integer scoring = graphScoring.get(kg);

		if (scoring == null) {

			scoring = 0;

			scoring += getDistancesScoring(kg);

			scoring += getStrokeEqualityScoring(kg);

			scoring += getStrokeLineScoring(kg);

			graphScoring.put(kg, scoring);
		}

		return scoring;
	}

	/**
	 * 20/100
	 * 
	 * @param kg
	 */
	private Integer getStrokeLineScoring(KanjiGraph kg) {

		double lineScoring;
		double strokeScoring;

		Integer graphLines = getLineCount(kg);

		lineScoring = Math.abs(graphLines - modelLineCount);

		if (lineScoring >= graphLines) {
			lineScoring = 0;
		} else {

			lineScoring = 10 * (graphLines - lineScoring) / graphLines;
		}

		strokeScoring = Math.abs(kg.strokeCount() - modelStrokecount);

		if (strokeScoring >= kg.strokeCount()) {
			strokeScoring = 0;
		} else {

			strokeScoring = 10 * (kg.strokeCount() - strokeScoring) / kg.strokeCount();
		}

		return new Integer((int) (lineScoring + strokeScoring));
	}

	private Integer getStrokeEqualityScoring(KanjiGraph kg) {

		Integer equalityScoring = 0;

		Set<KanjiStroke> contained = new HashSet<KanjiStroke>();

		for (KanjiStroke ks : kg.getStrokes()) {
			if (modelStrokes.contains(ks) && !contained.contains(ks)) {
				equalityScoring++;
				contained.add(ks);
			}
		}

		return 40 * equalityScoring / modelStrokecount;
	}

	private Integer getDistancesScoring(KanjiGraph kg) {

		Integer distancesScoring = 0;

		Map<KanjiStroke, Set<Integer>> toMap;
		Set<Integer> distances;
		KanjiStroke from;
		KanjiStroke to;
		for (int i = 0; i < kg.getStrokes().size() && i < modelStrokecount; i++) {
			from = kg.getStrokes().get(i);
			for (int j = 1 + i; j < kg.getStrokes().size() && j < modelStrokecount; j++) {
				to = kg.getStrokes().get(j);
				toMap = modelStrokeDistances.get(from);
				if (toMap != null) {
					distances = toMap.get(to);
					if (distances != null) {
						if (distances.contains((Math.abs(i - j))))
								distancesScoring++;
					}
				}
			}
		}

		if (modelDistances == 0) {
			distancesScoring = 1;
			modelDistances = 1;
		}

		return 40 * distancesScoring / modelDistances;
	}
	private Integer getLineCount(KanjiGraph kg) {

		Integer count = 0;

		for (KanjiStroke ks : kg.getStrokes()) {
			count += ks.getStrokeList().size();
		}

		return count;

	}

	private Map<KanjiStroke, Map<KanjiStroke, Set<Integer>>> getDistanceMap(KanjiGraph kg) {

		Map<KanjiStroke, Map<KanjiStroke, Set<Integer>>> distanceMap = new HashMap<KanjiStroke, Map<KanjiStroke, Set<Integer>>>();

		Set<Integer> distances;
		Map<KanjiStroke, Set<Integer>> toMap;
		KanjiStroke from;
		KanjiStroke to;
		for (int i = 0; i < kg.getStrokes().size(); i++) {
			from = kg.getStrokes().get(i);
			for (int j = 0; j < kg.getStrokes().size(); j++) {
				to = kg.getStrokes().get(j);

				toMap = distanceMap.get(from);
				if (toMap == null) {
					toMap = new HashMap<KanjiStroke, Set<Integer>>();
					distanceMap.put(from, toMap);
				}

				distances = toMap.get(to);
				if (distances == null) {
					distances = new HashSet<Integer>();
					toMap.put(to, distances);
				}

				distances.add(new Integer((int) Math.abs(i - j)));
			}
		}

		return distanceMap;
	}

	private Integer calcBinomial(Integer n, Integer k) {

		int[][] binomial = new int[n + 1][k + 1];

		for (int i = 1; i <= k; i++)
			binomial[0][i] = 0;
		for (int i = 0; i <= n; i++)
			binomial[i][0] = 1;

		for (int i = 1; i <= n; i++)
			for (int j = 1; j <= k; j++)
				binomial[i][j] = binomial[i - 1][j - 1] + binomial[i - 1][j];

		return new Integer(binomial[n][k]);
	}

}
