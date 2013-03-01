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

package net.ajaest.jdk.data.dict.query;

import java.util.List;

import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ClassicNelsonQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ClassicRadicalQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.CommonStrokeMiscountQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DeRooQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DicNameQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DicPairQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DicReferenceQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.FourCornerQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.FrequencyQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.GradeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JISCharsetQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JISCodeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JISPairQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JLPTLevelQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.KanjiFieldsQAboutFactory;
import net.ajaest.jdk.data.dict.query.kanjiFields.KanjiGraphQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.MeaningLanguageQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.MeaningPairQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.MeaningQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ReadingPairQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ReadingQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ReadingTypeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.SkipQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.SpahnHadamitzkyQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.StrokeCountQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.UnicodeValueQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.VariantPairQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.VariantReferenceQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.VariantTypeQAbout;

/**
 * This class is the beginning step in order to build a consistent kanji
 * expression that contains relations between kanji fields properties, what it
 * means that this class does not represents a self-consistent expression(only
 * {@code KanjiExpression} does}.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiQuery extends QAbout {

	public KanjiQuery() {
		super();
	}

	protected KanjiQuery(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
	}

	// appending complete kanji expression

	public KanjiExpression expression(KanjiExpression ke) {

		super.queryDomains.add(ke);
		return new KanjiExpression(super.previousBConnector, super.queryDomains);
	}

	// ClassicNelsonQAbout

	public ClassicNelsonQAbout classic_nelson() {
		return KanjiFieldsQAboutFactory.createClassicNelsonQAbout(super.previousBConnector, super.queryDomains);
	}

	// ClassicRadicalQAbout

	public ClassicRadicalQAbout classic_radical() {
		return KanjiFieldsQAboutFactory.createClassicRadicalQAbout(super.previousBConnector, super.queryDomains);
	}

	// CommonStrokeMiscountQAbout

	public CommonStrokeMiscountQAbout stroke_miscount() {
		return KanjiFieldsQAboutFactory.createCommonStrokeMiscountQAbout(super.previousBConnector, super.queryDomains);
	}

	// DeRooQAbout

	public DeRooQAbout De_Roo_code() {
		return KanjiFieldsQAboutFactory.createDeRooQAbout(super.previousBConnector, super.queryDomains);
	}

	// DicNameQAbout

	public DicNameQAbout dictionary_name() {
		return KanjiFieldsQAboutFactory.createDicNameQAbout(super.previousBConnector, super.queryDomains);
	}

	// DicReferenceQAbout

	public DicReferenceQAbout dictionary_reference() {
		return KanjiFieldsQAboutFactory.createDicReferenceQAbout(super.previousBConnector, super.queryDomains);
	}

	// FourCornerQAbout

	public FourCornerQAbout four_corner_code() {
		return KanjiFieldsQAboutFactory.createFourCornerQAbout(super.previousBConnector, super.queryDomains);
	}

	// FrequencyQAbout

	public FrequencyQAbout frequency() {
		return KanjiFieldsQAboutFactory.createFrequencyQAbout(super.previousBConnector, super.queryDomains);
	}

	// GradeQAbout

	public GradeQAbout grade() {
		return KanjiFieldsQAboutFactory.createGradeQAbout(super.previousBConnector, super.queryDomains);
	}

	// JISCharsetQAbout

	public JISCharsetQAbout JIS_charset() {
		return KanjiFieldsQAboutFactory.createJISCharsetQAbout(super.previousBConnector, super.queryDomains);
	}

	// JISCodeQAbout

	public JISCodeQAbout JIS_code_value() {
		return KanjiFieldsQAboutFactory.createJISCodeQAbout(super.previousBConnector, super.queryDomains);
	}

	// JLPTLevelQAbout

	public JLPTLevelQAbout JLPT_level() {
		return KanjiFieldsQAboutFactory.createJLPTLevelQAbout(super.previousBConnector, super.queryDomains);
	}

	// KanjiGraphQAbout

	public KanjiGraphQAbout kanji_graph() {
		return KanjiFieldsQAboutFactory.createKanjiGraphQAbout(super.previousBConnector, super.queryDomains);
	}

	// MeaningLanguageQAbout

	public MeaningLanguageQAbout meaning_language() {
		return KanjiFieldsQAboutFactory.createMeaningLanguageQAbout(super.previousBConnector, super.queryDomains);
	}

	// MeaningQAbout

	public MeaningQAbout meaning() {
		return KanjiFieldsQAboutFactory.createMeaningQAbout(super.previousBConnector, super.queryDomains);
	}

	// ReadingQAbout

	public ReadingQAbout reading() {
		return KanjiFieldsQAboutFactory.createReadingQAbout(super.previousBConnector, super.queryDomains);
	}

	// ReadingTypeQAbout

	public ReadingTypeQAbout reading_type() {
		return KanjiFieldsQAboutFactory.createReadingTypeQAbout(super.previousBConnector, super.queryDomains);
	}

	// SkipQAbout

	public SkipQAbout skip_code() {
		return KanjiFieldsQAboutFactory.createSkipQAbout(super.previousBConnector, super.queryDomains);
	}

	// SpahnHadamitzkyQAbout

	public SpahnHadamitzkyQAbout SpahnーHadamizky_code() {
		return KanjiFieldsQAboutFactory.createSpahnHadamitzkyQAbout(super.previousBConnector, super.queryDomains);
	}

	// StrokeCountQAbout

	public StrokeCountQAbout stroke_count() {
		return KanjiFieldsQAboutFactory.createStrokeCountQAbout(super.previousBConnector, super.queryDomains);
	}

	// UnicodeValueQAbout

	public UnicodeValueQAbout unicode_value() {
		return KanjiFieldsQAboutFactory.createUnicodeValueQAbout(super.previousBConnector, super.queryDomains);
	}

	// VariantReferenceQAbout

	public VariantReferenceQAbout variant_reference() {
		return KanjiFieldsQAboutFactory.createVariantReferenceQAbout(super.previousBConnector, super.queryDomains);
	}

	// VariantTypeQAbout

	public VariantTypeQAbout variant_type() {
		return KanjiFieldsQAboutFactory.createVariantTypeQAbout(super.previousBConnector, super.queryDomains);
	}

	/* Pairs */

	public DicPairQAbout dictionary_entry() {
		return KanjiFieldsQAboutFactory.createDicPairQAbout(super.previousBConnector, super.queryDomains);
	}
	
	public MeaningPairQAbout meaning_entry() {
		return KanjiFieldsQAboutFactory.createMeaningPairQAbout(super.previousBConnector, super.queryDomains);
	}
	
	public ReadingPairQAbout reading_entry() {
		return KanjiFieldsQAboutFactory.createReadingPairQAbout(super.previousBConnector, super.queryDomains);
	}
	
	public JISPairQAbout jis_code_entry() {
		return KanjiFieldsQAboutFactory.createJISPairQAbout(super.previousBConnector, super.queryDomains);
	}
	public VariantPairQAbout variant_entry() {
		return KanjiFieldsQAboutFactory.createVariantPairQAbout(super.previousBConnector, super.queryDomains);
	}

}
