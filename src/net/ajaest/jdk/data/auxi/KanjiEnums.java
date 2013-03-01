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

package net.ajaest.jdk.data.auxi;

// TODO: javadoc
public class KanjiEnums {

	public enum RadicalTypeEnum {
		original,
		top,
		bottom, 
		left, 
		right, 
		enclosure, 
		variable
	}
	
	public enum KanjiFieldEnum {
		
		KANJI_CLASSIC_NELSON,
		KANJI_CLASSIC_RADICAL ,
		KANJI_DE_ROO ,
		KANJI_DIC_NAME ,
		KANJI_DIC_INDEX ,
		KANJI_FOUR_CORNER ,
		KANJI_FREQUENCY ,
		KANJI_GRADE ,
		KANJI_GRAPH,
		KANJI_JIS_CHARSET ,
		KANJI_JIS_CODE ,
		KANJI_JLPT_LEVEL ,
		KANJI_LITERAL,
		KANJI_MEANING ,
		KANJI_MEANING_LANGUAGE ,
		KANJI_READING_TYPE ,
		KANJI_READING ,
		KANJI_SKIP ,
		KANJI_SPAHN_HADAMITZKY ,
		KANJI_STROKE_COUNT ,
		KANJI_STROKE_MISCOUNT,
		KANJI_UNICODE_VALUE ,
		KANJI_VARIANT_INDEX ,
		KANJI_VARIANT_TYPE, 
		KANJI_FIRST_MEANING,
		KANJI_FIRST_READING,
		KANJI_DIC_REFERENCE,
		NONE, 
		PAIR_DIC, 
		PAIR_VARIANT, 
		PAIR_READING, 
		PAIR_JIS,
		PAIR_MEANING;
	}

	public enum KanjiDicsEnum {

		 DIC_nelson_c ,
		 DIC_nelson_n ,
		 DIC_halpern_njecd ,
		 DIC_halpern_kkld , 
		 DIC_heisig , 
		 DIC_gakken , 
		 DIC_kanji_basic_book , 
		 DIC_oneill_names , 
		 DIC_oneill_kk , 
		 DIC_moro , 
		 DIC_henshall , 
		 DIC_sh_kk , 
		 DIC_sakade , 
		 DIC_jf_cards ,
		 DIC_henshall3 ,
		 DIC_tutt_cards ,
		 DIC_crowley , 
		 DIC_kanji_in_context ,
		 DIC_busy_people , 
		 DIC_kodansha_compact ,
		 DIC_maniette , 
	}

	public enum KanjiReadingTypesEnum {

		READ_JA_ON,
		READ_JA_KUN, 
		READ_NANORI,
		READ_PINYIN, 
		READ_KOREAN_H, 
		READ_KOREAN_R,
	}

	public enum FieldTypes {
		STRING, INTEGER, INTEGER_LIST, ORDENABLE_STRING, NONE
	}

	public enum FieldContainer {
		PAIR, PAIR_SET, ENTRY_SET, BASIC, SET, QUERYCODE
	}

	public static FieldTypes getFieldType(String s) {

		if (s == null)
			return FieldTypes.NONE;

		if (s.equals("KANJI_CLASSIC_NELSON") || s.equals("KANJI_CLASSIC_RADICAL") || s.equals("KANJI_FREQUENCY") || s.equals("KANJI_GRADE") || s.equals("KANJI_JLPT_LEVEL") || s.equals("KANJI_STROKE_COUNT") || s.equals("KANJI_UNICODE_VALUE") ||
			s.equals("KANJI_LITERAL") ||
			s.equals("KANJI_DIC_INDEX"))
			return FieldTypes.INTEGER;

		if (s.equals("KANJI_DIC_NAME") || s.equals("KANJI_DE_ROO") || s.equals("KANJI_FOUR_CORNER") || s.equals("KANJI_JIS_CHARSET") || s.equals("KANJI_JIS_CODE") || s.equals("KANJI_MEANING") || s.equals("KANJI_MEANING_LANGUAGE") || s.equals("KANJI_READING_TYPE") || s.equals("KANJI_READING") || s.equals("KANJI_SKIP") || s.equals("KANJI_SPAHN_HADAMITZKY") || s.equals("KANJI_VARIANT_INDEX") || s.equals("KANJI_VARIANT_TYPE") ||
			s.equals("KANJI_GRAPH") )
			return FieldTypes.STRING;

		if (s.equals("KANJI_STROKE_MISCOUNT"))
			return FieldTypes.INTEGER_LIST;

		return FieldTypes.NONE;
	}

	public static FieldTypes getFieldType(KanjiFieldEnum kfe) {

		switch (kfe) {
			case KANJI_CLASSIC_NELSON :
			case KANJI_CLASSIC_RADICAL :
			case KANJI_FREQUENCY :
			case KANJI_GRADE :
			case KANJI_JLPT_LEVEL :
			case KANJI_STROKE_COUNT :
			case KANJI_UNICODE_VALUE :
			case KANJI_GRAPH :
			case KANJI_LITERAL :
				return FieldTypes.INTEGER;
			case KANJI_DIC_NAME :
			case KANJI_DIC_INDEX :
				return FieldTypes.ORDENABLE_STRING;
			case KANJI_DE_ROO :
			case KANJI_FOUR_CORNER :
			case KANJI_JIS_CHARSET :
			case KANJI_JIS_CODE :
			case KANJI_MEANING :
			case KANJI_MEANING_LANGUAGE :
			case KANJI_READING_TYPE :
			case KANJI_READING :
			case KANJI_SKIP :
			case KANJI_SPAHN_HADAMITZKY :
			case KANJI_VARIANT_INDEX :
			case KANJI_VARIANT_TYPE :
				return FieldTypes.STRING;

			case KANJI_STROKE_MISCOUNT :
				return FieldTypes.INTEGER_LIST;
		}

		return FieldTypes.NONE;
	}

	public static FieldContainer getFieldContainer(KanjiFieldEnum kfe) {

		switch (kfe) {
			case KANJI_CLASSIC_NELSON :
			case KANJI_CLASSIC_RADICAL :
			case KANJI_FREQUENCY :
			case KANJI_GRADE :
			case KANJI_STROKE_COUNT :
			case KANJI_UNICODE_VALUE :
			case KANJI_JLPT_LEVEL :
			case KANJI_GRAPH :
			case KANJI_LITERAL :
				return FieldContainer.BASIC;
			case KANJI_JIS_CHARSET :
			case KANJI_JIS_CODE :
				return FieldContainer.PAIR;
			case KANJI_DE_ROO :
			case KANJI_SPAHN_HADAMITZKY :
			case KANJI_SKIP :
			case KANJI_FOUR_CORNER :
				return FieldContainer.QUERYCODE;
			case KANJI_DIC_NAME :
			case KANJI_DIC_INDEX :
			case KANJI_VARIANT_INDEX :
			case KANJI_VARIANT_TYPE :
				return FieldContainer.PAIR_SET;
			case KANJI_MEANING :
			case KANJI_MEANING_LANGUAGE :
			case KANJI_READING_TYPE :
			case KANJI_READING :
				return FieldContainer.ENTRY_SET;

			case KANJI_STROKE_MISCOUNT :
				return FieldContainer.SET;
		}

		throw new IllegalArgumentException();
	}
}