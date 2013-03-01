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

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import net.ajaest.jdk.data.dict.query.KanjiExpression;

//TODO: JavaDoc
public class ORQueue extends AbstractQueryElement {

	private TreeContainer 			kanjiTree;
	private PriorityQueue<ANDQueue> executeQueue;
	
	
	// #############################################################
	// ############# Constructor methods ###########################
	// #############################################################

	public ORQueue(TreeContainer kanjiTree, KanjiExpression ke) {
		this.kanjiTree = kanjiTree;
		initQueue(ke);
		calcPriority();
	}

	// #############################################################
	// ############# Public methods ################################
	// #############################################################

	@Override
	public Set<Integer> getRefs() {
		
		Set<Integer> refs = new HashSet<Integer>();

		if (executeQueue.size() > 0) {
			refs.addAll(executeQueue.poll().getRefs());

			while (executeQueue.size() > 0) {
				refs.addAll(executeQueue.poll().getRefs());
			}
		}

		return refs;
	}

	// #############################################################
	// ############# Private methods ###############################
	// #############################################################

	private void calcPriority() {

		float f = 0f;

		for (AbstractQueryElement fe : executeQueue) {
			f += fe.priority;
		}

		priority = new Integer((int) (f / executeQueue.size()));
	}

	private void initQueue(KanjiExpression ke) {

		executeQueue = new PriorityQueue<ANDQueue>(ke.size());

		Set<QAbout> andStatment = new HashSet<QAbout>();

		if (ke.size() != 0) {
			List<QAbout> qas = ke.getDomains();
			List<ConnectorEnum> connectors = ke.getBooleanCases();
			
			andStatment.add(qas.get(0));
			int size = ke.size();
			for (int i = 1; i < size; i++) {
				switch (connectors.get(i)) {
					case AND :
					case FIRST :
						andStatment.add(qas.get(i));
						break;
					case OR :
						executeQueue.add(new ANDQueue(kanjiTree, andStatment));
						andStatment = new HashSet<QAbout>();
						andStatment.add(qas.get(i));
						break;
					default :
						throw new IllegalArgumentException("Not allowed connector:" + connectors.get(i));
				}
			}
			executeQueue.add(new ANDQueue(kanjiTree, andStatment));
		}
	}
}
