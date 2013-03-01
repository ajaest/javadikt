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
public class OrderDef extends QAbout{

	private KanjiFieldEnum sortField;

	private SortCaseEnum orderCase;
	
	protected OrderDef(KanjiFieldEnum sortField, List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
		super.queryDomains.add(this);
		this.sortField = sortField;
	}

	public void setOrderBy(SortCaseEnum obe) {
		this.orderCase = obe;
	}

	public KanjiFieldEnum getSortField() {
		return sortField;
	}

	public SortCaseEnum getOrderBy() {
		return orderCase;
	}

	public void setSortField(KanjiFieldEnum sortField) {
		this.sortField = sortField;
	}

	public KanjiSortExpression ramdomly() {
		this.setOrderBy(SortCaseEnum.Ramdomly);
		return new KanjiSortExpression(super.previousBConnector, super.queryDomains);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("[Order by ");
		sb.append(sortField);
		sb.append(" ");
		sb.append(orderCase);
		sb.append(']');

		return sb.toString();
	}

}
