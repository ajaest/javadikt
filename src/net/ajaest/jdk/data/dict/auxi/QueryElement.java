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

import java.util.Set;

import net.ajaest.jdk.data.dict.query.ValueQAbout;

//TODO: JavaDoc
public class QueryElement extends AbstractQueryElement {

	private TreeContainer 	kanjiTree;
	private ValueQAbout<?> 	vqa;

	public QueryElement(TreeContainer kanjiTree, Integer priority, ValueQAbout<?> vqa) {

		this.kanjiTree = kanjiTree;
		this.priority = priority;
		this.vqa = vqa;
	}

	@Override
	public Set<Integer> getRefs() {

		return kanjiTree.getRefs(vqa);
	}
}