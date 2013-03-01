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
import java.util.Locale;

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.lib.data.interfaces.Selector;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

//TODO: JavaDoc
public class MeaningOrderDef extends StringOrderDef {

	private Selector<ISO639ー1> selected;

	protected MeaningOrderDef(Selector<ISO639ー1> selected, List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(KanjiFieldEnum.KANJI_FIRST_MEANING, booleanConnector, domains);

		this.selected = selected;
	}

	public Selector<ISO639ー1> getSelectedLanguaje() {
		return selected;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("[Order by meaning in ");
		sb.append(new Locale(selected.getSelected().toString()).getDisplayLanguage());
		sb.append(" ");
		sb.append(getOrderBy());
		sb.append(']');

		return sb.toString();
	}
}
