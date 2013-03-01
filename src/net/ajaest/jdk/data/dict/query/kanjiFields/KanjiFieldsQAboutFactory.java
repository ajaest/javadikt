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

package net.ajaest.jdk.data.dict.query.kanjiFields;

import java.util.List;

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.query.ValueQAbout;

/**
 * Factory that constructs kanji domain representation objects.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiFieldsQAboutFactory {

	public static ClassicNelsonQAbout createClassicNelsonQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new ClassicNelsonQAbout(booleanConnector, domains);
	}

	public static ClassicRadicalQAbout createClassicRadicalQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {

		return new ClassicRadicalQAbout(booleanConnector, domains);
	}

	public static CommonStrokeMiscountQAbout createCommonStrokeMiscountQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new CommonStrokeMiscountQAbout(booleanConnector, domains);
	}

	public static DeRooQAbout createDeRooQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new DeRooQAbout(booleanConnector, domains);
	}

	public static DicNameQAbout createDicNameQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new DicNameQAbout(booleanConnector, domains);
	}

	public static DicReferenceQAbout createDicReferenceQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new DicReferenceQAbout(booleanConnector, domains);
	}

	public static FourCornerQAbout createFourCornerQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new FourCornerQAbout(booleanConnector, domains);
	}

	public static FrequencyQAbout createFrequencyQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new FrequencyQAbout(booleanConnector, domains);
	}

	public static GradeQAbout createGradeQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new GradeQAbout(booleanConnector, domains);
	}

	public static JISCharsetQAbout createJISCharsetQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new JISCharsetQAbout(booleanConnector, domains);
	}

	public static JISCodeQAbout createJISCodeQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new JISCodeQAbout(booleanConnector, domains);
	}

	public static JLPTLevelQAbout createJLPTLevelQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new JLPTLevelQAbout(booleanConnector, domains);
	}

	public static KanjiGraphQAbout createKanjiGraphQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new KanjiGraphQAbout(booleanConnector, domains);
	}

	public static MeaningLanguageQAbout createMeaningLanguageQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new MeaningLanguageQAbout(booleanConnector, domains);
	}

	public static MeaningQAbout createMeaningQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new MeaningQAbout(booleanConnector, domains);
	}

	public static ReadingQAbout createReadingQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new ReadingQAbout(booleanConnector, domains);
	}

	public static ReadingTypeQAbout createReadingTypeQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new ReadingTypeQAbout(booleanConnector, domains);
	}

	public static SkipQAbout createSkipQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new SkipQAbout(booleanConnector, domains);
	}

	public static SpahnHadamitzkyQAbout createSpahnHadamitzkyQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new SpahnHadamitzkyQAbout(booleanConnector, domains);
	}

	public static StrokeCountQAbout createStrokeCountQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new StrokeCountQAbout(booleanConnector, domains);
	}

	public static UnicodeValueQAbout createUnicodeValueQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new UnicodeValueQAbout(booleanConnector, domains);
	}

	public static VariantReferenceQAbout createVariantReferenceQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new VariantReferenceQAbout(booleanConnector, domains);
	}

	public static VariantTypeQAbout createVariantTypeQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new VariantTypeQAbout(booleanConnector, domains);
	}

	public static DicPairQAbout createDicPairQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new DicPairQAbout(booleanConnector, domains);
	}

	public static JISPairQAbout createJISPairQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new JISPairQAbout(booleanConnector, domains);
	}

	public static MeaningPairQAbout createMeaningPairQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new MeaningPairQAbout(booleanConnector, domains);
	}

	public static ReadingPairQAbout createReadingPairQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new ReadingPairQAbout(booleanConnector, domains);
	}

	public static VariantPairQAbout createVariantPairQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		return new VariantPairQAbout(booleanConnector, domains);
	}
	
	public static ValueQAbout<?> createIntstace(KanjiFieldEnum kfe, List<ConnectorEnum> booleanConnector, List<QAbout> domains) {

		// TODO: finish
		switch (kfe) {
			case PAIR_DIC:
				return createDicPairQAbout(booleanConnector, domains);
			case PAIR_JIS:
				return createJISPairQAbout(booleanConnector, domains);
			case PAIR_MEANING :
				return createMeaningPairQAbout(booleanConnector, domains);
			case PAIR_READING :
				return createMeaningPairQAbout(booleanConnector, domains);
			case PAIR_VARIANT :
				return createVariantPairQAbout(booleanConnector, domains);
			default :
				throw new RuntimeException("Not implemented");
		}
	}
}
