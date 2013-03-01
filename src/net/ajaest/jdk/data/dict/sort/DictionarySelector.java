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
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.lib.data.interfaces.Selector;


//TODO: JavaDoc
public class DictionarySelector implements Selector<KanjiDicsEnum> {

	private KanjiDicsEnum selected;

	private List<ConnectorEnum> booleanConnector;
	private List<QAbout> domains;

	protected DictionarySelector(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {

		this.booleanConnector = booleanConnector;
		this.domains = domains;
	}

	@Override
	public KanjiDicsEnum getSelected() {

		return selected;
	}

	@Override
	public void setSelected(KanjiDicsEnum selected) {

		this.selected = selected;
	}
	
	public DictionaryOrderDef Modern_Readers_JapaneseーEnglish_Character_Dictionary() {
		this.setSelected(KanjiDicsEnum.DIC_nelson_c);
		return new DictionaryOrderDef(this, booleanConnector, domains);
		
	}
	
	public DictionaryOrderDef The_New_Nelson_JapaneseーEnglish_Character_Dictionary(){
		this.setSelected(KanjiDicsEnum.DIC_nelson_n);
		return new DictionaryOrderDef(this, booleanConnector, domains);
		
	}
	public DictionaryOrderDef New_JapaneseーEnglish_Character_Dictionary(){
		this.setSelected(KanjiDicsEnum.DIC_halpern_njecd);
		return new DictionaryOrderDef (this,booleanConnector,domains);
		
	}
	public DictionaryOrderDef Kanji_Learners_Dictionary() {
		this.setSelected(KanjiDicsEnum.DIC_halpern_kkld);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Remembering_The__Kanji() {
		this.setSelected(KanjiDicsEnum.DIC_heisig);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef New_Dictionary_of_Kanji_Usage() {
		this.setSelected(KanjiDicsEnum.DIC_gakken);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Japanese_Names() {
		this.setSelected(KanjiDicsEnum.DIC_oneill_names);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Essential_Kanji() {
		this.setSelected(KanjiDicsEnum.DIC_oneill_kk);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Daikanwajiten() {
		this.setSelected(KanjiDicsEnum.DIC_moro);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef A_Guide_To_Remembering_Japanese_Characters() {
		this.setSelected(KanjiDicsEnum.DIC_henshall);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Kanji_and_Kana() {
		this.setSelected(KanjiDicsEnum.DIC_sh_kk);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef A_Guide_To_Reading_and_Writing_Japanese_by_Sakade() {
		this.setSelected(KanjiDicsEnum.DIC_sakade);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Japanese_Kanji_Flashcards() {
		this.setSelected(KanjiDicsEnum.DIC_jf_cards);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef A_Guide_To_Reading_and_Writing_Japanese_by_Henshall() {
		this.setSelected(KanjiDicsEnum.DIC_henshall3);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Tuttle_Kanji_Cards() {
		this.setSelected(KanjiDicsEnum.DIC_tutt_cards);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef The_Kanji_Way_to_Japanese_Language_Power() {
		this.setSelected(KanjiDicsEnum.DIC_crowley);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Kanji_in_Context() {
		this.setSelected(KanjiDicsEnum.DIC_kanji_in_context);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Japanese_For_Busy_People() {
		this.setSelected(KanjiDicsEnum.DIC_busy_people);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Kodansha_Compact_Kanji_Guide() {
		this.setSelected(KanjiDicsEnum.DIC_kodansha_compact);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Les_Kanjis_dans_la_tete() {
		this.setSelected(KanjiDicsEnum.DIC_maniette);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}
	public DictionaryOrderDef Kanji_Basic_Book() {
		this.setSelected(KanjiDicsEnum.DIC_kanji_basic_book);
		return new DictionaryOrderDef(this, booleanConnector, domains);

	}

	public DictionaryOrderDef dic_enum_equals(KanjiDicsEnum kde) {
		this.setSelected(kde);
		return new DictionaryOrderDef(this, booleanConnector, domains);
	}

	public String toString() {

		return selected.toString();
	}

}
