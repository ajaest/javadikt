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

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiDicsEnum;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.lib.data.interfaces.Selector;

//TODO: JavaDoc
public class DictionaryOrderDef extends NumberOrderDef {

	Selector<KanjiDicsEnum> selected;
	
	protected DictionaryOrderDef(Selector<KanjiDicsEnum> selected, List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(KanjiFieldEnum.KANJI_DIC_REFERENCE, booleanConnector, domains);

		if (selected == null || selected.getSelected() == null)
			throw new IllegalArgumentException("Dictionary not selected");

		this.selected = selected;
	}
	
	public Selector<KanjiDicsEnum> getSelectedDic() {
		
		return selected;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("[Order by ");
		sb.append(selected.getSelected().toString().toUpperCase());
		sb.append(" dictionary reference ");
		sb.append(getOrderBy());
		sb.append(']');

		return sb.toString();
	}

}
