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

import java.util.Collection;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import net.ajaest.jdk.data.dict.query.IndexPairQAbout;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.StringPairQAbout;
import net.ajaest.jdk.data.dict.query.StringValueQAbout;
import net.ajaest.jdk.data.dict.query.ValueQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.KanjiGraphQAbout;

//TODO: JavaDoc
public class ANDQueue extends AbstractQueryElement {

	private final TreeContainer 			kanjiTree;
	private final PriorityQueue<AbstractQueryElement> executeQueue;
	

	// #############################################################
	// ############# Constructor methods ###########################
	// #############################################################

	public ANDQueue(TreeContainer kanjiTree, Collection<QAbout> statements) {
		this.kanjiTree = kanjiTree;
		this.executeQueue = new PriorityQueue<AbstractQueryElement>(statements.size());
		initQueue(statements);
		calcPriority();
	}

	// #############################################################
	// ############# Public methods ################################
	// #############################################################

	@Override
	public Set<Integer> getRefs() {

		boolean resultEmpty = false;
		Set<Integer> refs = new HashSet<Integer>();

		if (executeQueue.size() > 0) {
			refs.addAll(executeQueue.poll().getRefs());

			while (executeQueue.size() > 0 && !resultEmpty) {
				refs.retainAll(executeQueue.poll().getRefs());
				resultEmpty = refs.size() == 0;
			}
		}

		return refs;

	}

	public void setPriority(Integer p) {
		if (p == null)
			priority = Integer.MAX_VALUE;
		else
			priority = p;

	}

	// #############################################################
	// ############# Private methods ###############################
	// #############################################################

	private void initQueue(Collection<QAbout> statements) {

		for (QAbout stmnt : statements) {
			if (stmnt instanceof ValueQAbout<?>)
				executeQueue.add(new QueryElement(kanjiTree, getPriorityFactor((ValueQAbout<?>) stmnt), (ValueQAbout<?>) stmnt));
			else if(stmnt instanceof KanjiExpression)
				executeQueue.add(new ORQueue(kanjiTree, (KanjiExpression) stmnt));
		}
	}

	private void calcPriority() {

		float f = 0f;

		for (AbstractQueryElement fe : executeQueue) {
			f += fe.priority;
		}
		
		priority = new Integer((int) (f / executeQueue.size()));
	}

	// TODO: deep study of priority factors
	private Integer getPriorityFactor(ValueQAbout<?> vqa) {

		StringValueQAbout strQa;
		KanjiGraphQAbout kgQa;
		StringPairQAbout spQa;
		IndexPairQAbout ipQa;

		Integer queryPriority = Integer.MIN_VALUE;

		switch (vqa.getFieldEnum()) {

			case KANJI_UNICODE_VALUE :
			case KANJI_LITERAL :
				queryPriority += getCasePenalization(vqa.getQueryCase());
				break;
			case KANJI_FREQUENCY :
				queryPriority += 2 * getCasePenalization(vqa.getQueryCase());
				break;
			case KANJI_JIS_CHARSET :
			case KANJI_DIC_NAME :
			case KANJI_MEANING_LANGUAGE :
			case KANJI_READING_TYPE :
			case KANJI_VARIANT_TYPE :
				strQa = (StringValueQAbout) vqa;
				queryPriority += 10 * getCasePenalization(vqa.getQueryCase()) * strQa.getValue().length();
				break;
			case KANJI_CLASSIC_RADICAL :
			case KANJI_DIC_REFERENCE :
			case KANJI_GRADE :
			case KANJI_DIC_INDEX :
			case KANJI_JLPT_LEVEL :
			case KANJI_CLASSIC_NELSON :
			case KANJI_STROKE_COUNT :
			case KANJI_STROKE_MISCOUNT :
				queryPriority += 4 * getCasePenalization(vqa.getQueryCase());
				break;
			case KANJI_GRAPH :
				kgQa = (KanjiGraphQAbout) vqa;
				queryPriority += 6 * getCasePenalization(vqa.getQueryCase()) * kgQa.getValue().getLineSequence().size();
				break;
			case KANJI_JIS_CODE :
			case KANJI_SKIP :
				queryPriority += 5 * getCasePenalization(vqa.getQueryCase());
				break;
			case KANJI_DE_ROO :
			case KANJI_FOUR_CORNER :
			case KANJI_SPAHN_HADAMITZKY :
				strQa = (StringValueQAbout) vqa;
				queryPriority += 9 * getCasePenalization(vqa.getQueryCase()) * strQa.getValue().length();
				break;
			case KANJI_MEANING :
			case KANJI_READING :
			case KANJI_VARIANT_INDEX :
				strQa = (StringValueQAbout) vqa;
				queryPriority += 6 * getCasePenalization(vqa.getQueryCase()) * strQa.getValue().length();
				break;
			case PAIR_DIC :
				queryPriority += 4 * getCasePenalization(vqa.getQueryCase());
				ipQa = (IndexPairQAbout) vqa;
				queryPriority += 6 * getCasePenalization(vqa.getQueryCase()) * ipQa.getValue().getFirst().length();
				queryPriority *= 2;
				break;
			case PAIR_JIS :
			case PAIR_MEANING :
			case PAIR_READING :
			case PAIR_VARIANT :
				spQa = (StringPairQAbout) vqa;
				queryPriority += 6 * getCasePenalization(vqa.getQueryCase()) * spQa.getValue().getFirst().length();
				queryPriority += 6 * getCasePenalization(vqa.getQueryCase()) * spQa.getValue().getSecond().length();
				queryPriority *= 2;
				break;
			default :
				throw new IllegalArgumentException("Not allowed field:" + vqa.getFieldEnum());
		}

		return queryPriority;
	}

	private Integer getCasePenalization(QueryCaseEnum queryCase) {

		switch (queryCase) {
			case EQUALS:
			case NULL :
				return 1;
			case GREATER_OR_EQUALS_THAN:
			case LESS_OR_EQUALS_THAN:
				return 30;
			case GREATER_THAN:
			case LESS_THAN:
				return 25;
			case NOT_EQUALS :
				return 70;
			default :
				throw new IllegalArgumentException("Not allowed query case:" + queryCase);
		}
	}
}
