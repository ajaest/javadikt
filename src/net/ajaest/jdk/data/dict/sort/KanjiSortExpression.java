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

import java.util.ArrayList;
import java.util.List;

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;

//TODO: JavaDoc
public class KanjiSortExpression extends QAbout {

	protected KanjiSortExpression(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
	}

	public KanjiOrdering and_if_equals() {
		super.previousBConnector.add(ConnectorEnum.IF_EQUALS);
		return new KanjiOrdering(super.previousBConnector, super.queryDomains);
	}

	public List<KanjiFieldEnum> getInvolvedFieldsInOrdering() {

		List<KanjiFieldEnum> involved = new ArrayList<KanjiFieldEnum>();

		OrderDef od;
		for (QAbout domain : getDomains()) {
			od = (OrderDef) domain;
			involved.add(od.getSortField());
		}

		return involved;
	}

	public List<Object> getInvolvedValuesInOrdering() {

		List<Object> involved = new ArrayList<Object>();
		
		for (QAbout domain : getDomains()) {
			if (domain instanceof DictionaryOrderDef) {
				involved.add(((DictionaryOrderDef) domain).getSelectedDic().getSelected().toString().replace("DIC_", ""));
			} else if(domain instanceof MeaningOrderDef) {
				involved.add(((MeaningOrderDef) domain).getSelectedLanguaje().getSelected().toString());
			} else if(domain instanceof ReadingOrderDef) {
				involved.add(((ReadingOrderDef) domain).getSelectedReading().getSelected().toString().replace("READ_", "").toLowerCase());
			} else {
				involved.add(null);
			}
		}

		return involved;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		
		OrderDef temp;
		for (int i = 0; i < queryDomains.size(); i++) {

			if (previousBConnector.get(i) != ConnectorEnum.FIRST)
				sb.append(" and " + previousBConnector.get(i) + " ");
			temp = (OrderDef) queryDomains.get(i);
			sb.append(temp.toString());

		}
		
		return sb.toString();
	}
}
