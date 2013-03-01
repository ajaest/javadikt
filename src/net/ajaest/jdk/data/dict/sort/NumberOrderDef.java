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

package net.ajaest.jdk.data.dict.sort;

import java.util.List;

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.auxi.SortCaseEnum;

//TODO: JavaDoc
public class NumberOrderDef extends OrderDef {

	protected NumberOrderDef(KanjiFieldEnum field, List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(field, booleanConnector, domains);
	}

	public KanjiSortExpression from_least_to_greatest() {
		this.setOrderBy(SortCaseEnum.FromLeastToGreather);
		return new KanjiSortExpression(super.previousBConnector, super.queryDomains);
	}

	public KanjiSortExpression from_greatest_to_least() {
		this.setOrderBy(SortCaseEnum.FromGreatherToLeast);
		return new KanjiSortExpression(super.previousBConnector, super.queryDomains);
	}

}
