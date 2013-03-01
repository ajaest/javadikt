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

package net.ajaest.jdk.tools.test.data.kanji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import net.ajaest.jdk.data.auxi.AllowedStrokeClueEnum;
import net.ajaest.jdk.data.auxi.AllowedStrokePointEnum;
import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.auxi.KanjiStrokeClue;
import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.JISPair;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiQueryCodes;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.Radical;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.jdk.data.kanji.VariantPair;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

//TODO: JavaDoc
public class KanjiTest extends TestCase {

	public static char generateRandomKanjiBMP() {

		int ucs = 0;
		while (!Kanji.isKanji(ucs))
			ucs = (int) (Math.random() * 0xFAFF);
		return (char) ucs;
	}

	// ***************************************
	// ///////////////////////////////////////
	// ///////// JUnit override methods
	// ///////////////////////////////////////
	// ***************************************

	// ***************************************
	// ///////////////////////////////////////
	// ///////// Support methods
	// ///////////////////////////////////////
	// ***************************************
	public static Integer generateRandomKanjiUCS() {

		Integer ucs = 0;
		while (!Kanji.isKanji(ucs))
			ucs = (int) (Math.random() * 0x2FA1F);

		return ucs;
	}

	private Kanji k;

	@Override
	protected void setUp() {
		this.k = new Kanji('生');
		this.k.setJisCode(new JISPair("jis208", "32-24", this.k.getUnicodeRef()));
		this.k.setClassicRadical(new Radical(101));
		this.k.setNelsonRadical(100);// not real
		this.k.setFrequency(29);
		this.k.setGrade(1);
		this.k.setJLPTLevel(4);
		this.k.setStrokeCount(5);

		Set<Integer> strokeMis = new HashSet<Integer>();
		strokeMis.add(4);
		strokeMis.add(6);

		this.k.setStrokeMiscounts(strokeMis);// not real

		Set<DicReferencePair> drp = new HashSet<DicReferencePair>();
		drp.add(new DicReferencePair("nelson_c", " 2991", 2991, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("nelson_n", "3715", 3715, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("halpern_njecd", " 3497", 3492, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("halpern_kkld", " 2179", 2179, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("heisig", " 1555", 1555, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("gakken", " 29", 29, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("oneill_names", " 214", 214, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("oneill_kk", " 67", 67, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("moro", "21670  | vol 7, page 1027", 21670, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("henshall", " 42", 42, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("sh_kk", " 44", 44, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("sakade", " 34", 34, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("jf_cards", " 71", 71, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("henshall3", " 44", 44, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("tutt_cards", " 43", 43, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("crowley", " 9", 9, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("kanji_in_context", " 49", 49, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("busy_people", " 2.4", 24, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("kodansha_compact", "1327", 1327, this.k.getUnicodeRef()));
		drp.add(new DicReferencePair("maniette", "1569", 1569, this.k.getUnicodeRef()));
		this.k.setDicReferences(drp);

		Set<ReadingEntry> res = new HashSet<ReadingEntry>();

		List<String> pinyinR = new ArrayList<String>();
		pinyinR.add("sheng1");
		ReadingEntry pinyin = new ReadingEntry("pinyin", pinyinR, this.k.getUnicodeRef());
		res.add(pinyin);

		List<String> korean_rR = new ArrayList<String>();
		korean_rR.add("saeng");
		ReadingEntry korean_r = new ReadingEntry("korean_r", korean_rR, this.k.getUnicodeRef());
		res.add(korean_r);

		List<String> korean_hR = new ArrayList<String>();
		korean_hR.add("■");
		ReadingEntry korean_h = new ReadingEntry("korean_h", korean_hR, this.k.getUnicodeRef());
		res.add(korean_h);

		List<String> ja_onR = new ArrayList<String>();
		ja_onR.add("セイ");
		ja_onR.add("ショウ");
		ReadingEntry ja_on = new ReadingEntry("ja_on", ja_onR, this.k.getUnicodeRef());
		res.add(ja_on);

		List<String> ja_kunR = new ArrayList<String>();
		ja_kunR.add("い.きる");
		ja_kunR.add("い.かす");
		ja_kunR.add("い.ける");
		ja_kunR.add("う.まれる");
		ja_kunR.add("うま.れる");
		ja_kunR.add("う.まれ");
		ja_kunR.add("うまれ");
		ja_kunR.add("う.む");
		ja_kunR.add("お.う");
		ja_kunR.add("は.える");
		ja_kunR.add("は.やす");
		ja_kunR.add("き");
		ja_kunR.add("なま");
		ja_kunR.add("なま-");
		ja_kunR.add("な.る");
		ja_kunR.add("な.す");
		ja_kunR.add("む.す");
		ja_kunR.add("-う");
		ReadingEntry ja_kun = new ReadingEntry("ja_kun", ja_kunR, this.k.getUnicodeRef());
		res.add(ja_kun);

		List<String> nanoriR = new ArrayList<String>();
		nanoriR.add("あさ");
		nanoriR.add("いき");
		nanoriR.add("いく");
		nanoriR.add("いけ");
		nanoriR.add("うぶ");
		nanoriR.add("うまい");
		nanoriR.add("え");
		nanoriR.add("おい");
		nanoriR.add("ぎゅう");
		nanoriR.add("くるみ ");
		nanoriR.add("ごせ");
		nanoriR.add("さ");
		nanoriR.add("じょう");
		nanoriR.add("すぎ");
		nanoriR.add("そ");
		nanoriR.add("そう");
		nanoriR.add("ちる");
		nanoriR.add("なば");
		nanoriR.add("にう");
		nanoriR.add("にゅう");
		nanoriR.add("ふ");
		nanoriR.add("み");
		nanoriR.add("もう");
		nanoriR.add("よい");
		nanoriR.add("りゅう");
		ReadingEntry nanori = new ReadingEntry("nanori", nanoriR, this.k.getUnicodeRef());
		res.add(nanori);
		this.k.setReadings(res);

		Set<MeaningEntry> men = new HashSet<MeaningEntry>();
		List<String> enM = new ArrayList<String>();
		enM.add("life");
		enM.add("genuine");
		enM.add("birth");
		MeaningEntry en = new MeaningEntry("en", enM, this.k.getUnicodeRef());
		men.add(en);

		List<String> frM = new ArrayList<String>();
		frM.add("vie");
		frM.add("naissance");
		frM.add("authentique");
		frM.add("cru");
		MeaningEntry fr = new MeaningEntry("fr", frM, this.k.getUnicodeRef());
		men.add(fr);

		List<String> esM = new ArrayList<String>();
		esM.add("vida");
		esM.add("nacimiento");
		esM.add("existir");
		esM.add("nacer");
		esM.add("dar a luz");
		esM.add("puro");
		esM.add("crudo");
		MeaningEntry es = new MeaningEntry("es", esM, this.k.getUnicodeRef());
		men.add(es);

		List<String> ptM = new ArrayList<String>();
		ptM.add("vida");
		ptM.add("genuína");
		ptM.add("nascimento");
		MeaningEntry pt = new MeaningEntry("pt", ptM, this.k.getUnicodeRef());
		men.add(pt);

		this.k.setMeanings(men);

		Set<VariantPair> var = new HashSet<VariantPair>();
		var.add(new VariantPair("jis_208", "2-32", this.k.getUnicodeRef()));

		this.k.setVariants(var);// not real

		this.k.setQueryCodes(new KanjiQueryCodes("4-5-2", "0a5.29", "2510.0", "2472", this.k.getUnicodeRef()));

		List<AllowedStrokeLineEnum> tempS;
		List<KanjiStroke> ks = new ArrayList<KanjiStroke>();

		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.A);
		ks.add(new KanjiStroke(tempS, 1, this.k.getUnicodeRef()));

		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.F);
		ks.add(new KanjiStroke(tempS, 1, this.k.getUnicodeRef()));

		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.B);
		ks.add(new KanjiStroke(tempS, 1, this.k.getUnicodeRef()));

		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.F);
		ks.add(new KanjiStroke(tempS, 1, this.k.getUnicodeRef()));

		tempS = new ArrayList<AllowedStrokeLineEnum>();
		tempS.add(AllowedStrokeLineEnum.F);
		ks.add(new KanjiStroke(tempS, 1, this.k.getUnicodeRef()));

		Set<KanjiStrokeClue> ksc = new HashSet<KanjiStrokeClue>();

		ksc.add(new KanjiStrokeClue(1, 2, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.begin, AllowedStrokeClueEnum.isAtLeftOf, this.k.getUnicodeRef()));
		ksc.add(new KanjiStrokeClue(2, 3, AllowedStrokePointEnum.ave, AllowedStrokePointEnum.ave, AllowedStrokeClueEnum.areIntheSamePoint, this.k.getUnicodeRef()));

		KanjiGraph kg = new KanjiGraph(ks, ksc, this.k.getUnicodeRef());

		this.k.setGraph(kg);
	}

	@Override
	protected void tearDown() {
	}

	public void testCompareTo() {

		Kanji greater = new Kanji(this.k.getUnicodeRef() + 1);
		Kanji lower = new Kanji(this.k.getUnicodeRef() - 1);

		boolean azzert = this.k.compareTo(greater) < 0;
		azzert = azzert && (this.k.compareTo(lower) > 0);
		azzert = azzert && (this.k.compareTo(this.k) == 0);

		assertTrue(azzert);

	}

	public void testEqualsObject() {
		Kanji k1 = new Kanji(this.k.getUnicodeRef());
		boolean azzert = this.k.equals(k1);
		Character c1 = new Character((char) this.k.getUnicodeRef().intValue());
		azzert = azzert && this.k.equals(c1);
		assertTrue(azzert);
	}

	public void testFormattedDescriptionString() {

		String formatted =
			"Character: 生\n\n" +
			"\tCodification\n" +
			"\t\teuc: 751f\n" +
			"\t\tjis208: 32-24\n\n" +
			"\tRadical\n" +
			"\t\tClassical: 100\n" +
			"\t\tNelson: 100\n\n" +
			"\tMiscelanea\n" +
			"\t\tFrequency: 29\n" +
			"\t\tGrade: 1\n" +
			"\t\tJPTL level: 4\n" +
			"\t\tStroke count: 5\n" +
			"\t\tStroke common miscounts : 4,6\n" +
			"\t\tVariants: \n" +
			"\t\t\t[jis_208,2-32]\n\n" +
			"\tDictionary References\n" +
			"\t\tgakken:  29\n" +
			"\t\tkanji_in_context:  49\n" +
			"\t\thalpern_kkld:  2179\n" +
			"\t\tnelson_c:  2991\n" +
			"\t\tmaniette: 1569\n" +
			"\t\toneill_kk:  67\n" +
			"\t\thalpern_njecd:  3497\n" +
			"\t\thenshall:  42\n" +
			"\t\tkodansha_compact: 1327\n" +
			"\t\tjf_cards:  71\n" +
			"\t\thenshall3:  44\n" +
			"\t\tsakade:  34\n" +
			"\t\toneill_names:  214\n" +
			"\t\tbusy_people:  2.4\n" +
			"\t\theisig:  1555\n" +
			"\t\tcrowley:  9\n" +
			"\t\tnelson_n: 3715\n" +
			"\t\ttutt_cards:  43\n" +
			"\t\tmoro:  vol 7, page 1027, 21670\n" +
			"\t\tsh_kk:  44\n\n" +
			"\tReadings\n" +
			"\t\tpinyin: sheng1\n" +
			"\t\tja_kun: うまれ\n" +
			"\t\tja_kun: い.かす\n" +
			"\t\tja_kun: うま.れる\n" +
			"\t\tja_kun: う.まれ\n" +
			"\t\tja_kun: なま-\n" +
			"\t\tja_kun: -う\n" +
			"\t\tja_kun: き\n" +
			"\t\tja_kun: む.す\n" +
			"\t\tja_kun: お.う\n" +
			"\t\tja_kun: う.む\n" +
			"\t\tja_kun: い.ける\n" +
			"\t\tja_kun: は.える\n" +
			"\t\tja_kun: な.す\n" +
			"\t\tja_kun: な.る\n" +
			"\t\tja_kun: い.きる\n" +
			"\t\tja_kun: は.やす\n" +
			"\t\tja_kun: なま\n" +
			"\t\tja_kun: う.まれる\n" +
			"\t\tkorean_h: ■\n" +
			"\t\tnanori: そう\n" +
			"\t\tnanori: もう\n" +
			"\t\tnanori: うぶ\n" +
			"\t\tnanori: ふ\n" +
			"\t\tnanori: じょう\n" +
			"\t\tnanori: にゅう\n" +
			"\t\tnanori: くるみ \n" +
			"\t\tnanori: すぎ\n" +
			"\t\tnanori: にう\n" +
			"\t\tnanori: み\n" +
			"\t\tnanori: ごせ\n" +
			"\t\tnanori: おい\n" +
			"\t\tnanori: あさ\n" +
			"\t\tnanori: りゅう\n" +
			"\t\tnanori: ぎゅう\n" +
			"\t\tnanori: なば\n" +
			"\t\tnanori: え\n" +
			"\t\tnanori: ちる\n" +
			"\t\tnanori: よい\n" +
			"\t\tnanori: いけ\n" +
			"\t\tnanori: さ\n" +
			"\t\tnanori: いく\n" +
			"\t\tnanori: いき\n" +
			"\t\tnanori: うまい\n" +
			"\t\tnanori: そ\n" +
			"\t\tja_on: セイ\n" +
			"\t\tja_on: ショウ\n" +
			"\t\tkorean_r: saeng\n\n" +
			"\tMeanings\n" +
			"\t\tfr: naissance\n" +
			"\t\tfr: cru\n" +
			"\t\tfr: authentique\n" +
			"\t\tfr: vie\n" +
			"\t\tpt: vida\n" +
			"\t\tpt: nascimento\n" +
			"\t\tpt: genuína\n" +
			"\t\ten: birth\n" +
			"\t\ten: life\n" +
			"\t\ten: genuine\n" +
			"\t\tes: vida\n" +
			"\t\tes: existir\n" +
			"\t\tes: nacimiento\n" +
			"\t\tes: puro\n" +
			"\t\tes: nacer\n" +
			"\t\tes: dar a luz\n" +
			"\t\tes: crudo\n\n" +
			"\tQuery Codes\n" +
			"\t\tKanjiQueryCodes [deRoo=2510.0, fourCorner=2472, skip=4-5-2, spahnHadamitzky=0a5.29]\n\n" +
			"\tGraph: \n" +
			"\t\tAFBFF, The ave of stroke 1 isAtLeftOf the begin of stroke 2, The ave of stroke 2 and the ave of stroke 3 areIntheSamePoint\n";

		assertEquals(formatted, this.k.formattedDescriptionString());
	}

	public void testHashCode() {

		Integer arg = generateRandomKanjiUCS();
		this.k = new Kanji(arg);
		assertTrue(arg.hashCode() == this.k.hashCode());
	}

	// ***************************************
	// ///////////////////////////////////////
	// ///////// Test methods
	// ///////////////////////////////////////
	// ***************************************

	public void testIsKanji() {

		boolean azzert = true;

		for (int i = 0x4E00; i <= 0x9FFF; i++) {
			azzert = azzert && Kanji.isKanji(i);
		}
		for (int i = 0x3400; i <= 0x4DFF; i++) {
			azzert = azzert && Kanji.isKanji(i);
		}
		for (int i = 0x20000; i <= 0x2A6DF; i++) {
			azzert = azzert && Kanji.isKanji(i);
		}
		for (int i = 0xF900; i <= 0xFAFF; i++) {
			azzert = azzert && Kanji.isKanji(i);
		}
		for (int i = 0x2F800; i <= 0x2FA1F; i++) {
			azzert = azzert && Kanji.isKanji(i);
		}

		assertTrue(azzert);

	}

	public void testKanaToRomaji() {

		boolean azzert = true;
		String failedS = "";
		boolean failed = false;
		// simple conversion
		String p1 = "あアぁァいイぃィう ウ ぅ ゥえエぇェおオぉォ";
		String p2 = "aaaaiiiiu< >u< >u< >ueeeeoooo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "かカがガきキぎギくクぐグけケげゲこコごゴ";
		p2 = "kakagagakikigigikukugugukekegegekokogogo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "さサざザしシじジすスずズせセぜゼそソぞゾ";
		p2 = "sasazazashishijijisusuzuzusesezezesosozozo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "たタだダちチぢヂつツづヅてテでデとトどド";
		p2 = "tatadadachichitzitzitsutsutzutzutetededetotododo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "なナにニぬヌねネのノはハばバぱパひヒびビぴピふフぶブぷプへヘべベぺペほホぼボぽポ";
		p2 = "nananininununenenonohahababapapahihibibipipifufububupupuhehebebepepehohobobopopo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "まマみミむムめメもモやヤゆユよヨ";
		p2 = "mamamimimumumememomoyayayuyuyoyo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "らラりリるルれレろロわワをヲんン";
		p2 = "rararirirururererorowawawowonn";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		// advanced conversion

		p1 = "きゃきゅきょぎゃぎゅぎょキャキュキョギャギュギョ";
		p2 = "kyakyukyogyagyugyokyakyukyogyagyugyo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "しゃしゅしょじゃじゅじょシャシュショジャジュジョ";
		p2 = "shashushojajujoshashushojajujo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}
		p1 = "ちゃちゅちょぢゃぢゅぢょチャチュチョヂャヂュヂョ";
		p2 = "chachuchotzatzutzochachuchotzatzutzo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "にゃにゅにょニャニュニョ";
		p2 = "nyanyunyonyanyunyo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "ひゃひゅひょびゃびゅびょぴゃぴゅぴょヒャヒュヒョビャビュビョピャピュピョ";
		p2 = "hyahyuhyobyabyubyopyapyupyohyahyuhyobyabyubyopyapyupyo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		p1 = "りゃりゅりょリャリュリョ";
		p2 = "ryaryuryoryaryuryo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		// vocal extensions

		p1 = "おうううきょうきゅうオウウウキョウキュウアーイーウーエーオーあーいーうーえーおー";
		p2 = "ôûkyôkyûôûkyôkyûâîûêôâîûêô";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		// japanization kana singularities

		p1 = "ティディヴァヴィヴヴェヴォ";
		p2 = "tidivavivuvevo";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		// punctuation and brackets

		p1 = "「」()[]。、…～~";
		p2 = "「」()[].,...~~";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		// okurigana or suffix marker

		p1 = "か.う-";
		p2 = "kau-";
		azzert = azzert && Kanji.kanaToRomaji(p1).equals(p2);
		if (!azzert && !failed) {
			failed = true;
			failedS = "in 『" + p1 + "』\n　where 『" + p2 + "』\nmust equals 『" + Kanji.kanaToRomaji(p1) + "』";
		}

		assertTrue(failedS, azzert);
	}

	public void testKanjiCharacter() {

		Character arg = new Character(generateRandomKanjiBMP());
		this.k = new Kanji(arg);
		assertEquals(new Character(this.k.toChar()[0]), arg);

	}

	// ***************************************
	// ///////////////////////////////////////
	// ///////// Test constructors
	// ///////////////////////////////////////
	// ***************************************
	public void testKanjiCharArray() {

		char[] arg = Character.toChars(generateRandomKanjiUCS());
		this.k = new Kanji(arg);
		assertTrue(Arrays.equals(arg, this.k.toChar()));
	}

	public void testKanjiInt() {

		int arg = generateRandomKanjiUCS();
		this.k = new Kanji(arg);
		assertTrue(arg == this.k.getUnicodeRef().intValue());
	}

	public void testKanjiInteger() {

		Integer arg = generateRandomKanjiUCS();
		this.k = new Kanji(arg);
		assertEquals(arg, this.k.getUnicodeRef());
	}

	public void testToChar() {
		boolean azzert = true;
		azzert = azzert && (this.k.toChar().length == 1);
		azzert = azzert && (this.k.toChar()[0] == '生');

		Kanji k1 = new Kanji(0x20000);
		azzert = azzert && (k1.toChar().length == 2);
		azzert = azzert && (Character.codePointAt(k1.toChar(), 0) == 0x20000);

		assertTrue(azzert);
	}

	public void testToRomaji() {

		KanjiTag k1 = this.k.toRomaji();

		Set<ReadingEntry> res = new HashSet<ReadingEntry>();

		for (ReadingEntry refe : this.k.getReadings()) {

			if (refe.getKey().equals("ja_on") || refe.getKey().equals("ja_kun") || refe.getKey().equals("nanori")) {// only
				// kana
				// readings

				ReadingEntry ret = null;
				List<String> elements = new ArrayList<String>();

				for (String s : refe.getElements())
					elements.add(KanjiTag.kanaToRomaji(s));

				ret = new ReadingEntry(refe.getKey(), elements, refe.getUnicodeRef());

				res.add(ret);
			}
		}

		assertEquals(k1.getReadings(), res);
	}

	public void testToString() {
		Character c = new Character((char) this.k.getUnicodeRef().intValue());

		assertEquals("" + c, this.k.toString());
	}
}
