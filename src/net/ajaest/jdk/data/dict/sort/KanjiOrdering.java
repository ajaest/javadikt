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

//TODO: JavaDoc
public class KanjiOrdering extends QAbout {

	public KanjiOrdering() {
		super();
	}

	protected KanjiOrdering(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
	}

	public NumberOrderDef sort_by_Unicode() {
		return new NumberOrderDef(KanjiFieldEnum.KANJI_UNICODE_VALUE, super.previousBConnector, super.queryDomains);
	}
	
	public StringOrderDef sort_by_JIS_charset() {
		return new StringOrderDef(KanjiFieldEnum.KANJI_JIS_CHARSET, super.previousBConnector, super.queryDomains);
	}
	
	public StringOrderDef sort_by_JIS_code(){
		return new StringOrderDef(KanjiFieldEnum.KANJI_JIS_CODE, super.previousBConnector, super.queryDomains);
	}
	
	public NumberOrderDef sort_by_Nelson_radical(){
		return new NumberOrderDef(KanjiFieldEnum.KANJI_CLASSIC_NELSON, super.previousBConnector, super.queryDomains);
	}
	public NumberOrderDef sort_by_classic_radical(){
		return new NumberOrderDef(KanjiFieldEnum.KANJI_CLASSIC_RADICAL, super.previousBConnector, super.queryDomains);
	}
	public NumberOrderDef sort_by_grade() {
		return new NumberOrderDef(KanjiFieldEnum.KANJI_GRADE, super.previousBConnector, super.queryDomains);
	}
	public NumberOrderDef sort_by_stroke_count() {
		return new NumberOrderDef(KanjiFieldEnum.KANJI_STROKE_COUNT, super.previousBConnector, super.queryDomains);
	}
	public NumberOrderDef sort_by_frequency() {
		return new NumberOrderDef(KanjiFieldEnum.KANJI_FREQUENCY, super.previousBConnector, super.queryDomains);
	}
	public StringOrderDef sort_by_SKIP_code() {
		return new StringOrderDef(KanjiFieldEnum.KANJI_SKIP, super.previousBConnector, super.queryDomains);
	}
	public StringOrderDef sort_by_SpahnーHadamitzky_code() {
		return new StringOrderDef(KanjiFieldEnum.KANJI_SPAHN_HADAMITZKY, super.previousBConnector, super.queryDomains);
	}
	public StringOrderDef sort_by_four_corner_code() {
		return new StringOrderDef(KanjiFieldEnum.KANJI_FOUR_CORNER, super.previousBConnector, super.queryDomains);
	}
	public StringOrderDef sort_by_De_Roo_code() {
		return new StringOrderDef(KanjiFieldEnum.KANJI_DE_ROO, super.previousBConnector, super.queryDomains);
	}
	public GraphOrderDef sort_by_graph() {
		return new GraphOrderDef(super.previousBConnector, super.queryDomains);
	}
	public DictionarySelector sort_by_dictionary_reference() {
		return new DictionarySelector(super.previousBConnector, super.queryDomains);
	}
	
	public MeaningLanguageSelector sort_by_first_meaning() {
		return new MeaningLanguageSelector(super.previousBConnector, super.queryDomains);
	}
	
	public ReadingSelector sort_by_first_reading() {
		return new ReadingSelector(super.previousBConnector, super.queryDomains);
	}

	/**
	 * Must be last possible level
	 */
	public KanjiSortExpression sort_ramdomly() {

		new RamdomOrderDef(KanjiFieldEnum.NONE, super.previousBConnector, super.queryDomains);
		return new KanjiSortExpression(super.previousBConnector, super.queryDomains);
	}

	public static void main(String... args) {
		System.out.println(new KanjiOrdering().sort_by_first_reading().on_yomi().alphabetically().and_if_equals().sort_by_classic_radical().from_greatest_to_least());
	}


}
