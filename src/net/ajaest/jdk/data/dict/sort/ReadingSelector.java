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

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiReadingTypesEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.lib.data.interfaces.Selector;

//TODO: JavaDoc
public class ReadingSelector implements Selector<KanjiReadingTypesEnum> {

	private KanjiReadingTypesEnum selected;
	
	private List<ConnectorEnum> booleanConnector;
	private List<QAbout> domains;

	public ReadingSelector(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {

		this.booleanConnector = booleanConnector;
		this.domains = domains;
	}

	public ReadingOrderDef on_yomi() {
		selected = KanjiReadingTypesEnum.READ_JA_ON;
		return new ReadingOrderDef(this, booleanConnector, domains);
	}

	public ReadingOrderDef kun_yomi() {
		selected = KanjiReadingTypesEnum.READ_JA_KUN;
		return new ReadingOrderDef(this, booleanConnector, domains);
	}

	public ReadingOrderDef nanori_yomi() {
		selected = KanjiReadingTypesEnum.READ_NANORI;
		return new ReadingOrderDef(this, booleanConnector, domains);
	}

	public ReadingOrderDef pinyin_yomi() {
		selected = KanjiReadingTypesEnum.READ_PINYIN;
		return new ReadingOrderDef(this, booleanConnector, domains);
	}

	public ReadingOrderDef korean_hangul_yomi() {
		selected = KanjiReadingTypesEnum.READ_KOREAN_H;
		return new ReadingOrderDef(this, booleanConnector, domains);
	}

	public ReadingOrderDef korean_romanized_yomi() {
		selected = KanjiReadingTypesEnum.READ_KOREAN_R;
		return new ReadingOrderDef(this, booleanConnector, domains);
	}

	@Override
	public KanjiReadingTypesEnum getSelected() {
		return selected;
	}

	@Override
	public void setSelected(KanjiReadingTypesEnum selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return selected.toString();
	}

	public ReadingOrderDef reading_enum_equals(KanjiReadingTypesEnum krte) {
		selected = krte;
		return new ReadingOrderDef(this, booleanConnector, domains);
	}

}
