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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ajaest.jdk.data.auxi.SimilarKanjiStrokeDemuxer;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.auxi.QueryCaseEnum;
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
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.lib.data.Pair;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

/**
 * This single-instance class provide methods to adapt a kanji expression in
 * order to expand or limit the range of defined kanji of the expression.
 * Example usage could be:<br>
 * 
 * &nbsp;{@code KanjiExpression ke = QueryAdaptor.limitToJôyô().meaningsInLanguaje(ISO639ー1.EN).adapt(kanjiExpression);}
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class QueryAdaptor {

	private static boolean DEMUX = false;
	private static boolean SELECT_MEANING_LANGUAJE = false;
	private static boolean LIMIT_TO_JÔYÔ_KANJI = false;
	private static boolean DEMUX_READINGS = false;
	private static boolean GROUP_PAIRS = false;

	private static Integer DEMUXES;
	private static SimilarKanjiStrokeDemuxer GRAPHDEMUXER;

	private static ISO639ー1 MEANINGLANG;

	private boolean limitToJöyöKanji;
	private boolean demux;
	private boolean selectMeaningLanguaje;
	private boolean demuxReadings;
	private boolean groupPairs;

	private Integer demuxes;
	private SimilarKanjiStrokeDemuxer graphDemuxer;

	private ISO639ー1 meaningLang;

	private QueryAdaptor(SimilarKanjiStrokeDemuxer graphDemuxer, boolean demux, Integer demuxes,
						 boolean selectMeaningLanguaje, ISO639ー1 meaningLang, 
						 boolean limitToJöyöKanji, boolean demuxReadings, boolean demuxDicRefs) {

		this.demux = demux;
		this.selectMeaningLanguaje = selectMeaningLanguaje;
		this.limitToJöyöKanji = limitToJöyöKanji;
		this.demuxReadings = demuxReadings;
		this.groupPairs = demuxDicRefs;

		this.graphDemuxer = graphDemuxer;
		this.demuxes = demuxes;

		this.meaningLang = meaningLang;

		DEMUX = demux;
		SELECT_MEANING_LANGUAJE = selectMeaningLanguaje;
		LIMIT_TO_JÔYÔ_KANJI = limitToJöyöKanji;
		DEMUX_READINGS = demuxReadings;
		GROUP_PAIRS = demuxDicRefs;

		GRAPHDEMUXER = graphDemuxer;
		DEMUXES = demuxes;

		MEANINGLANG = meaningLang;
	}

	/**
	 * Sets that in the query to be adapted all occurrences of graphs will be
	 * demuxed the in the specified number of new graphs using the specified
	 * stroke demuxer
	 * 
	 * @param sksd
	 *            the stroke demuxer to be used to demux all graph occurrences
	 *            in the expression
	 * @param demuxes
	 *            count of desired graph demuxes per graph instance in query
	 * @return a query adaptor with option "demuxGraph" set on
	 */
	public static QueryAdaptor demuxGraphs(SimilarKanjiStrokeDemuxer sksd, Integer demuxes) {

		return new QueryAdaptor(sksd, true, demuxes, SELECT_MEANING_LANGUAJE, MEANINGLANG, LIMIT_TO_JÔYÔ_KANJI, DEMUX_READINGS, GROUP_PAIRS);
	}

	/**
	 * Sets that in the query to be adapted all occurrences of meaning consult
	 * will be limited to an specified language
	 * 
	 * @param lang
	 *            the ISO-639-1 language in which all meaning consults will be
	 *            limited
	 * 
	 * @return a query adaptor with option "meaningsInLanguaje" set on
	 */
	public static QueryAdaptor meaningsInLanguaje(ISO639ー1 lang) {

		return new QueryAdaptor(GRAPHDEMUXER, DEMUX, DEMUXES, true, lang, LIMIT_TO_JÔYÔ_KANJI, DEMUX_READINGS, GROUP_PAIRS);
	}

	/**
	 * Sets that the query to be adapted will only refer to kanjis which grade
	 * is lower than 10
	 * 
	 * @return a query adaptor with option "limitToJôyô" set on
	 */
	public static QueryAdaptor limitToJôyô() {

		return new QueryAdaptor(GRAPHDEMUXER, DEMUX, DEMUXES, SELECT_MEANING_LANGUAJE, MEANINGLANG, true, DEMUX_READINGS, GROUP_PAIRS);
	}

	/**
	 * Sets that in the query to be adapted all reading consults will be demuxed
	 * in order to being able to find readings with kanji markers. For example,
	 * the reading 「たべる」 will be demuxed as follows:<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 「たべる」<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 「た.べる」<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 「たべ.る」<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 「-たべる」<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 「たべる-」<br>
	 * 
	 * @return a query adaptor with option "demuxReadings" set on
	 */
	public static QueryAdaptor demuxReadings() {

		return new QueryAdaptor(GRAPHDEMUXER, DEMUX, DEMUXES, SELECT_MEANING_LANGUAJE, MEANINGLANG, LIMIT_TO_JÔYÔ_KANJI, true, GROUP_PAIRS);
	}

	/**
	 * Group pairs criteria into a unique criteria. This means, for example,
	 * that the expression:<br>
	 * <br> {@code [[Dictionary name EQUALS nelson_c] AND [Dictionary reference
	 * EQUALS 22]] } <br>
	 * <br>
	 * will be translated into:<br>
	 * <br> {@code [[Dictionary pair EQUALS [Pair =[first = nelson_c, second = 22]]
	 * } <br>
	 * <br>
	 * 
	 * Affected pairs are:<br>
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; DicNameQAbout <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; DicReferenceQAbout <br>
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; JISCharsetQAbout <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; JISCodeQAbout <br>
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; MeaningLanguageQAbout <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; MeaningQAbout <br>
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; ReadingTypeQAbout <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; ReadingQAbout <br>
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; VariantTypeQAbout <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; VariantReferenceQAbout <br>
	 * <br>
	 * 
	 * 
	 * @return a query adaptor with option "groupPairs" set on
	 */
	public static QueryAdaptor groupPairs() {

		return new QueryAdaptor(GRAPHDEMUXER, DEMUX, DEMUXES, SELECT_MEANING_LANGUAJE, MEANINGLANG, LIMIT_TO_JÔYÔ_KANJI, DEMUX_READINGS, true);
	}

	public KanjiExpression adapt(KanjiExpression ke) {

		KanjiExpression adaptedKe = null;
		KanjiQuery kq;
		boolean tempGroupPairs = groupPairs; // deactivate group pair adaption. This action will be done later for expressions
		groupPairs = false;

		KanjiExpression subKe;
		QAbout domain;
		for (int i = 0; i < ke.getDomains().size(); i++) {
			domain = ke.getDomains().get(i);
			kq = selectConnector(adaptedKe, ke.getBooleanCases().get(i));

			if (domain instanceof KanjiExpression) {
				subKe = adapt((KanjiExpression) domain);
				adaptedKe = addDomain(kq, subKe);
			} else if (demux && (domain instanceof KanjiGraphQAbout)) {
				subKe = demuxGraph((KanjiGraphQAbout) domain);
				if (subKe != null)
					adaptedKe = addDomain(kq, subKe);
				else
					adaptedKe = addDomain(kq, domain);
			} else if (selectMeaningLanguaje && (domain instanceof MeaningQAbout)) {
				kq = kq.meaning_language().equal(meaningLang.toString().toLowerCase()).AND();
				adaptedKe = addDomain(kq, domain);
			} else if (selectMeaningLanguaje && (domain instanceof MeaningLanguageQAbout)) {
				adaptedKe = selectCase(kq.meaning_language(), ((MeaningLanguageQAbout) domain).getQueryCase(), meaningLang.toString().toLowerCase());
			} else if (demuxReadings && (domain instanceof ReadingQAbout)) {
				subKe = demuxReadings((ReadingQAbout) domain);
				adaptedKe = addDomain(kq, subKe);
			} else {
				adaptedKe = addDomain(kq, domain);
			}
		}

		if (limitToJöyöKanji) {
			adaptedKe = new KanjiQuery().expression(adaptedKe).AND().grade().equalsOrLessThan(10);
		}

		groupPairs = tempGroupPairs; // Activate groupPairs check
		if (groupPairs) {
			adaptedKe = groupPairs(adaptedKe);
		}

		return adaptedKe;
	}

	private KanjiExpression groupPairs(KanjiExpression adaptedKe) {

		int size = adaptedKe.size();
		
		if (size > 0) {
			
			Set<ValueQAbout<String>> 					dicName = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<Integer>> 					dicRef = new HashSet<ValueQAbout<Integer>>();
			Set<ValueQAbout<Pair<String, Integer>>> 	dicPair = new HashSet<ValueQAbout<Pair<String, Integer>>>();
			Set<ValueQAbout<String>> 					jisCharset = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<String>> 					jisCode = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<Pair<String, String>>> 		jisPair = new HashSet<ValueQAbout<Pair<String, String>>>();
			Set<ValueQAbout<String>> 					meanLang = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<String>> 					mean = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<Pair<String, String>>> 		meanPair = new HashSet<ValueQAbout<Pair<String, String>>>();
			Set<ValueQAbout<String>> 					readType = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<String>> 					read = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<Pair<String, String>>> 		readPair = new HashSet<ValueQAbout<Pair<String, String>>>();
			Set<ValueQAbout<String>> 					varType = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<String>> 					var = new HashSet<ValueQAbout<String>>();
			Set<ValueQAbout<Pair<String, String>>> 		varPair = new HashSet<ValueQAbout<Pair<String, String>>>();
			Set<KanjiExpression>						kes = new HashSet<KanjiExpression>();
			Set<QAbout> 								others = new HashSet<QAbout>();
			
			List<QAbout> domains = new ArrayList<QAbout>();
			List<ConnectorEnum> booleanConnector = new ArrayList<ConnectorEnum>();
			booleanConnector.add(ConnectorEnum.FIRST);
			
			ConnectorEnum ce;
			int 		  i;
			QAbout 		  qa;
			i = 0;
			while (i < size) {
				qa = adaptedKe.getDomains().get(i);
				ce = adaptedKe.getBooleanCases().remove(i);
				if (ce.equals(ConnectorEnum.OR)) {

					demuxPairs(dicPair, dicName, dicRef, booleanConnector, domains, KanjiFieldEnum.PAIR_DIC);
					demuxPairs(jisPair, jisCharset, jisCode, booleanConnector, domains, KanjiFieldEnum.PAIR_JIS);
					demuxPairs(meanPair, meanLang, mean, booleanConnector, domains, KanjiFieldEnum.PAIR_MEANING);
					demuxPairs(readPair, readType, read, booleanConnector, domains, KanjiFieldEnum.PAIR_READING);
					demuxPairs(varPair, varType, var, booleanConnector, domains, KanjiFieldEnum.PAIR_VARIANT);

					domains.addAll(kes);
					domains.addAll(others);
					booleanConnector.addAll(Collections.nCopies(others.size() + kes.size(), ConnectorEnum.AND));

					if (booleanConnector.size() != 0)
						booleanConnector.remove(booleanConnector.size() - 1);

					booleanConnector.add(ConnectorEnum.OR);

					dicName.clear();
					dicRef.clear();
					dicPair.clear();
					jisCharset.clear();
					jisCode.clear();
					jisPair.clear();
					meanLang.clear();
					mean.clear();
					meanPair.clear();
					readType.clear();
					read.clear();
					readPair.clear();
					varType.clear();
					var.clear();
					varPair.clear();
					kes.clear();
					others.clear();
				}

				if (qa instanceof DicNameQAbout) {
					dicName.add((DicNameQAbout) qa);
				} else if (qa instanceof DicReferenceQAbout) {
					dicRef.add((DicReferenceQAbout) qa);
				} else if (qa instanceof DicPairQAbout) {
					dicPair.add((DicPairQAbout) qa);
				} else if (qa instanceof JISCharsetQAbout) {
					jisCharset.add((JISCharsetQAbout) qa);
				} else if (qa instanceof JISCodeQAbout) {
					jisCode.add((JISCodeQAbout) qa);
				} else if (qa instanceof JISPairQAbout) {
					jisPair.add((JISPairQAbout) qa);
				} else if (qa instanceof MeaningLanguageQAbout) {
					meanLang.add((MeaningLanguageQAbout) qa);
				} else if (qa instanceof MeaningQAbout) {
					mean.add((MeaningQAbout) qa);
				} else if (qa instanceof MeaningPairQAbout) {
					meanPair.add((MeaningPairQAbout) qa);
				} else if (qa instanceof ReadingTypeQAbout) {
					readType.add((ReadingTypeQAbout) qa);
				} else if (qa instanceof ReadingQAbout) {
					read.add((ReadingQAbout) qa);
				} else if (qa instanceof ReadingPairQAbout) {
					readPair.add((ReadingPairQAbout) qa);
				} else if (qa instanceof VariantTypeQAbout) {
					varType.add((VariantTypeQAbout) qa);
				} else if (qa instanceof VariantReferenceQAbout) {
					var.add((VariantReferenceQAbout) qa);
				} else if (qa instanceof VariantPairQAbout) {
					varPair.add((VariantPairQAbout) qa);
				} else if (qa instanceof KanjiExpression) {
					kes.add(groupPairs((KanjiExpression) qa));// TODO: current demux values must be passed to lower levels
				} else {
					others.add(qa);
				}

				adaptedKe.getDomains().remove(i);
				size--;
			}
			demuxPairs(dicPair, dicName, dicRef, booleanConnector, domains, KanjiFieldEnum.PAIR_DIC);
			demuxPairs(jisPair, jisCharset, jisCode, booleanConnector, domains, KanjiFieldEnum.PAIR_JIS);
			demuxPairs(meanPair, meanLang, mean, booleanConnector, domains, KanjiFieldEnum.PAIR_MEANING);
			demuxPairs(readPair, readType, read, booleanConnector, domains, KanjiFieldEnum.PAIR_READING);
			demuxPairs(varPair, varType, var, booleanConnector, domains, KanjiFieldEnum.PAIR_VARIANT);

			if (others.size() != 0 || kes.size() != 0) {
				domains.addAll(kes);
				domains.addAll(others);
				booleanConnector.addAll(Collections.nCopies(others.size() + kes.size() - 1, ConnectorEnum.AND));
			}

			booleanConnector.add(ConnectorEnum.OR);

			booleanConnector.set(0, ConnectorEnum.FIRST);

			adaptedKe = new KanjiExpression(booleanConnector, domains);
		}
		
		return adaptedKe;
	}

	@SuppressWarnings("unchecked")
	private <VQ extends ValueQAbout<Pair<E1, E2>>, E1, E2> void demuxPairs(Set<VQ> vqps, Set<ValueQAbout<E1>> vq1s, Set<ValueQAbout<E2>> vq2s, List<ConnectorEnum> booleanConnector, List<QAbout> domains, KanjiFieldEnum c) {

		Set<VQ> pairs = new HashSet<VQ>(vqps);

		if (	vqps.size() != 0 && vq1s.size() != 0 ||
				vqps.size() != 0 && vq1s.size() != 0 ||
				vq1s.size() != 0 && vq2s.size() != 0) {

			VQ vpair;
			for (ValueQAbout<E1> vq1 : vq1s) {
				for (ValueQAbout<E2> vq2 : vq2s) {
					vpair = (VQ) KanjiFieldsQAboutFactory.createIntstace(c, booleanConnector, domains);
					vpair.qc = vq2.qc;
					vpair.value = new Pair<E1, E2>(vq1.getValue(), vq2.getValue());
					pairs.add(vpair);
				}
			}

			for (VQ vqp : vqps) {
				for (VQ p : pairs) {
					vpair = (VQ) KanjiFieldsQAboutFactory.createIntstace(c, booleanConnector, domains);
					vpair.qc = p.qc;
					vpair.value = new Pair<E1, E2>(p.getValue().getFirst(), vqp.getValue().getSecond());
					pairs.add(vpair);

					vpair = (VQ) KanjiFieldsQAboutFactory.createIntstace(c, booleanConnector, domains);
					vpair.qc = p.qc;
					vpair.value = new Pair<E1, E2>(vqp.getValue().getFirst(), p.getValue().getSecond());
					pairs.add(vpair);

					vpair = (VQ) KanjiFieldsQAboutFactory.createIntstace(c, booleanConnector, domains);
					vpair.qc = vqp.qc;
					vpair.value = new Pair<E1, E2>(p.getValue().getFirst(), vqp.getValue().getSecond());
					pairs.add(vpair);

					vpair = (VQ) KanjiFieldsQAboutFactory.createIntstace(c, booleanConnector, domains);
					vpair.qc = vqp.qc;
					vpair.value = new Pair<E1, E2>(vqp.getValue().getFirst(), p.getValue().getSecond());
					pairs.add(vpair);
				}
			}
		} else {
			domains.addAll(vq1s);
			domains.addAll(vq2s);
			booleanConnector.addAll(Collections.nCopies(vq1s.size() + vq2s.size(), ConnectorEnum.AND));
		}

		domains.addAll(pairs);

		booleanConnector.addAll(Collections.nCopies(pairs.size(), ConnectorEnum.AND));
	}
	
	private KanjiExpression demuxReadings(ReadingQAbout domain) {
		
		Set<String> strs = new HashSet<String>();

		String mainReading = domain.getValue();

		// okurigana markers

		String demuxed;
		for (int i = 1; i < mainReading.length(); i++) {
			demuxed = mainReading.substring(0, i);
			demuxed += '.';
			demuxed += mainReading.substring(i);
			strs.add(demuxed);
		}

		// prefixes and suffixes
		Set<String> addTostrs = new HashSet<String>();
		for (String dmx1 : strs) {
			addTostrs.add(dmx1 + "-");
			addTostrs.add("-" + dmx1);
		}

		strs.addAll(addTostrs);

		strs.add(mainReading + "-");
		strs.add("-" + mainReading);

		// builds expression

		KanjiExpression ke = new KanjiQuery().reading().equal(mainReading);

		for (String s : strs) {
			ke = ke.OR().reading().equal(s);
		}

		return ke;
	}

	private KanjiExpression demuxGraph(KanjiGraphQAbout kgqa) {

		Set<KanjiGraph> demuxed = graphDemuxer.getSimilar(kgqa.getValue(), demuxes);
		
		KanjiExpression subKe = new KanjiQuery().kanji_graph().equal(kgqa.getValue());
		KanjiQuery kq = subKe.OR();
		for(KanjiGraph kg : demuxed){
			subKe = kq.kanji_graph().equal(kg);
			kq = subKe.OR();
		}

		return subKe;
	}
	
	private KanjiQuery selectConnector(KanjiExpression ke, ConnectorEnum connector) {

		switch (connector) {
			case AND :
				return ke.AND();
			case OR :
				return ke.OR();
			case FIRST :
				return new KanjiQuery();
			default :
				throw new IllegalArgumentException();
		}
	}

	private KanjiExpression addDomain(KanjiQuery kq, QAbout domain) {

		KanjiExpression ke;

		if (domain instanceof KanjiExpression) {
			ke = kq.expression((KanjiExpression) domain);
		} else if (domain instanceof StringValueQAbout) {

			StringValueQAbout strDomain = (StringValueQAbout) domain;

			if (domain instanceof DeRooQAbout) {
				ke = selectCase(kq.De_Roo_code(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof DicNameQAbout) {
				ke = selectCase(kq.dictionary_name(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof FourCornerQAbout) {
				ke = selectCase(kq.four_corner_code(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof JISCharsetQAbout) {
				ke = selectCase(kq.JIS_charset(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof JISCodeQAbout) {
				ke = selectCase(kq.JIS_code_value(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof MeaningLanguageQAbout) {
				ke = selectCase(kq.meaning_language(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof MeaningQAbout) {
				ke = selectCase(kq.meaning(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof ReadingTypeQAbout) {
				ke = selectCase(kq.reading_type(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof ReadingQAbout) {
				ke = selectCase(kq.reading(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof SkipQAbout) {
				ke = selectCase(kq.skip_code(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof SpahnHadamitzkyQAbout) {
				ke = selectCase(kq.SpahnーHadamizky_code(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof VariantReferenceQAbout) {
				ke = selectCase(kq.variant_reference(), strDomain.getQueryCase(), strDomain.getValue());
			} else if (domain instanceof VariantTypeQAbout) {
				ke = selectCase(kq.variant_type(), strDomain.getQueryCase(), strDomain.getValue());
			} else {
				throw new IllegalArgumentException();
			}


		} else if (domain instanceof IntegerValueQAbout) {

			IntegerValueQAbout intDomain = (IntegerValueQAbout) domain;

			if (domain instanceof ClassicNelsonQAbout) {
				ke = selectCase(kq.classic_nelson(), intDomain.getQueryCase(), intDomain.getValue());
			} else if (domain instanceof DicReferenceQAbout) {
				ke = selectCase(kq.dictionary_reference(), intDomain.getQueryCase(), intDomain.getValue());
			} else if (domain instanceof ClassicRadicalQAbout) {
				ke = selectCase(kq.classic_radical(), intDomain.getQueryCase(), intDomain.getValue());
			} else if (domain instanceof CommonStrokeMiscountQAbout) {
				ke = selectCase(kq.stroke_miscount(), intDomain.getQueryCase(), intDomain.getValue());
			} else if (domain instanceof FrequencyQAbout) {
				ke = selectCase(kq.frequency(), intDomain.getQueryCase(), intDomain.getValue());
			} else if (domain instanceof GradeQAbout) {
				ke = selectCase(kq.grade(), intDomain.getQueryCase(), intDomain.getValue());
			} else if (domain instanceof JLPTLevelQAbout) {
				ke = selectCase(kq.JLPT_level(), intDomain.getQueryCase(), intDomain.getValue());
			} else if (domain instanceof StrokeCountQAbout) {
				ke = selectCase(kq.stroke_count(), intDomain.getQueryCase(), intDomain.getValue());
			} else if (domain instanceof UnicodeValueQAbout) {
				ke = selectCase(kq.unicode_value(), intDomain.getQueryCase(), intDomain.getValue());
			} else {
				throw new IllegalArgumentException();
			}

		} else if (domain instanceof IndexPairQAbout) {

			IndexPairQAbout intPairDomain = (IndexPairQAbout) domain;

			if (domain instanceof DicPairQAbout) {
				ke = selectCase(kq.dictionary_entry(), intPairDomain.getQueryCase(), intPairDomain.getValue());
			} else {
				throw new IllegalArgumentException();
			}

		} else if (domain instanceof StringPairQAbout) {

			StringPairQAbout stPairDomain = (StringPairQAbout) domain;

			if (stPairDomain instanceof JISPairQAbout) {
				ke = selectCase(kq.jis_code_entry(), stPairDomain.getQueryCase(), stPairDomain.getValue());
			} else if (stPairDomain instanceof MeaningPairQAbout) {
				ke = selectCase(kq.meaning_entry(), stPairDomain.getQueryCase(), stPairDomain.getValue());
			} else if (stPairDomain instanceof ReadingPairQAbout) {
				ke = selectCase(kq.reading_entry(), stPairDomain.getQueryCase(), stPairDomain.getValue());
			} else if (stPairDomain instanceof VariantPairQAbout) {
				ke = selectCase(kq.variant_entry(), stPairDomain.getQueryCase(), stPairDomain.getValue());
			} else {
				throw new IllegalArgumentException();
			}

		} else {
			if (domain instanceof ValueQAbout<?>) {
				
				Object unknowValue = ((ValueQAbout<?>) domain).getValue();
				
				if(unknowValue instanceof KanjiGraph){
					ke = selectCase(kq.kanji_graph(), ((ValueQAbout<?>) domain).getQueryCase(), (KanjiGraph) unknowValue);
				} else {
					throw new IllegalArgumentException();
				}
			} else {
				throw new IllegalArgumentException();
			}

		}

		return ke;
	}

	private <E> KanjiExpression selectCase(ValueQAbout<E> vqa, QueryCaseEnum caze, E value) {

		switch (caze) {
			case EQUALS:
				return vqa.equal(value);
			case GREATER_OR_EQUALS_THAN:
				return vqa.equaslOrGreatherThan(value);
			case GREATER_THAN:
				return vqa.greatherThan(value);
			case LESS_OR_EQUALS_THAN:
				return vqa.equalsOrLessThan(value);
			case LESS_THAN:
				return vqa.lessThan(value);
			case NOT_EQUALS :
				return vqa.notEquals(value);
			case NULL :
				return vqa.isNull();
			default :
				throw new IllegalArgumentException();
				
		}
	}

	@Override
	protected void finalize() {
		DEMUX = false;
		SELECT_MEANING_LANGUAJE = false;
		LIMIT_TO_JÔYÔ_KANJI = false;
		DEMUX_READINGS = false;
		GROUP_PAIRS = false;
	}
}
