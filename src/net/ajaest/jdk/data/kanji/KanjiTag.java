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

package net.ajaest.jdk.data.kanji;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ajaest.jdk.data.auxi.KanjiReference;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;

/**
 * Immutable class that stores some different informations about a kanji
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiTag implements Serializable, Comparable<KanjiTag>,
		KanjiReference {

	private static final long serialVersionUID = 4421336415693809451L;

	public static final boolean isKanji(Integer unicodeValue) {
		// TODO: check if really these intervals are all kanji. Remember JIS x
		// 208 Contains non kanji chars.
		boolean CJK = false;
		boolean CJKA = false;
		boolean CJKB = false;
		boolean CI = false;
		boolean CIs = false; // for legible method

		// unified CJK ideograms
		CJK = unicodeValue >= 0x4E00 && unicodeValue <= 0x9FFF;
		if (!CJK) {
			// unified CJK ideograms, extension A
			CJKA = unicodeValue >= 0x3400 && unicodeValue <= 0x4DFF;
			// unified CJK ideograms, extension B
			if (!CJKA) {
				CJKB = unicodeValue >= 0x20000 && unicodeValue <= 0x2A6DF;
				// compatibility ideograms
				if (!CJKA) {
					CI = unicodeValue >= 0xF900 && unicodeValue <= 0xFAFF;
					// compatibility ideograms supplement
					if (!CJKA)
						CIs = unicodeValue >= 0x2F800 && unicodeValue <= 0x2FA1F;
				}
				// 

			}
		}
		return CJK || CJKA || CJKB || CI || CIs;
	}

	/**
	 * Converts the input kana string into romaji string. Any non kana string
	 * will be surrounded like "\<noKanaString\>". Okurigana marks('.') will be
	 * removed and suffix marks('-') will be kept.
	 * 
	 * @param s
	 *            kana string
	 * @return string with kana chars converted into romaji
	 */
	public static final String kanaToRomaji(String s) {

		StringBuilder sb = new StringBuilder();
		String temp;
		for (int i = 0; i < s.length(); i++) {
			char c = s.toCharArray()[i];

			switch (c) {
				// vocals column
				case 'あ' :
				case 'ア' :
				case 'ぁ' :
					sb.append('a');
					break;
				case 'ァ' :
					if (sb.length() > 1) {
						temp = sb.substring(sb.length() - 2);
						if (temp.equals("vu")) {
							sb.deleteCharAt(sb.length() - 1);
						}
					}
					sb.append("a");
					break;
				case 'い' :
				case 'イ' :
				case 'ぃ' :
					sb.append('i');
					break;
				case 'ィ' :
					if (sb.length() > 1) {
						temp = sb.substring(sb.length() - 2);
						if (temp.equals("de") || temp.equals("te"))
							sb.deleteCharAt(sb.length() - 1);
						else if (temp.equals("vu"))
							sb.deleteCharAt(sb.length() - 1);
					}
					sb.append('i');
					break;
				case 'う' :
				case 'ウ' :
				case 'ぅ' :
				case 'ゥ' :
					if (sb.length() > 0) {
						temp = sb.substring(sb.length() - 1);
						if (temp.equals("o")) {
							sb.deleteCharAt(sb.length() - 1);
							sb.append("ô");
						} else if (temp.equals("u")) {
							sb.deleteCharAt(sb.length() - 1);
							sb.append("û");
						} else
							sb.append('u');
					} else
						sb.append('u');
					break;
				case 'ヴ' :
					sb.append("vu");
					break;

				case 'え' :
				case 'エ' :
				case 'ぇ' :

					sb.append('e');
					break;
				case 'ェ' :
					if (sb.length() > 1) {
						temp = sb.substring(sb.length() - 2);
						if (temp.equals("vu"))
							sb.deleteCharAt(sb.length() - 1);
					}
					sb.append("e");
					break;
				case 'お' :
				case 'オ' :
				case 'ぉ' :
					sb.append('o');
					break;
				case 'ォ' :
					if (sb.length() > 0) {
						temp = sb.substring(sb.length() - 2);
						if (temp.equals("vu"))
							sb.deleteCharAt(sb.length() - 1);
					}
					sb.append("o");
					break;
				// ka column
				case 'か' :
				case 'カ' :
					sb.append("ka");
					break;
				case 'が' :
				case 'ガ' :
					sb.append("ga");
					break;
				case 'き' :
				case 'キ' :
					sb.append("ki");
					break;
				case 'ぎ' :
				case 'ギ' :
					sb.append("gi");
					break;
				case 'く' :
				case 'ク' :
					sb.append("ku");
					break;
				case 'ぐ' :
				case 'グ' :
					sb.append("gu");
					break;
				case 'け' :
				case 'ケ' :
					sb.append("ke");
					break;
				case 'げ' :
				case 'ゲ' :
					sb.append("ge");
					break;
				case 'こ' :
				case 'コ' :
					sb.append("ko");
					break;
				case 'ご' :
				case 'ゴ' :
					sb.append("go");
					break;
				// sa column
				case 'さ' :
				case 'サ' :
					sb.append("sa");
					break;
				case 'ざ' :
				case 'ザ' :
					sb.append("za");
					break;
				case 'し' :
				case 'シ' :
					sb.append("shi");
					break;
				case 'じ' :
				case 'ジ' :
					sb.append("ji");
					break;
				case 'す' :
				case 'ス' :
					sb.append("su");
					break;
				case 'ず' :
				case 'ズ' :
					sb.append("zu");
					break;
				case 'せ' :
				case 'セ' :
					sb.append("se");
					break;
				case 'ぜ' :
				case 'ゼ' :
					sb.append("ze");
					break;
				case 'そ' :
				case 'ソ' :
					sb.append("so");
					break;
				case 'ぞ' :
				case 'ゾ' :
					sb.append("zo");
					break;
				// ta column
				case 'た' :
				case 'タ' :
					sb.append("ta");
					break;
				case 'だ' :
				case 'ダ' :
					sb.append("da");
					break;
				case 'ち' :
				case 'チ' :
					sb.append("chi");
					break;
				case 'ぢ' :
				case 'ヂ' :
					sb.append("tzi");
					break;
				case 'つ' :
				case 'ツ' :
					sb.append("tsu");
					break;
				case 'っ' :
				case 'ッ' :
					temp = s.replaceAll("[.]", ""); // Something like this may occur: みっ.つ
					if (temp.length() - (i + 1) > 0) {
						temp = KanjiTag.kanaToRomaji(temp.substring(i + 1, i + 2));
						int codepoint = Character.codePointAt(temp.toCharArray(), 0);
						if (codepoint > 97 && codepoint < 122)// ascii lowercase
							sb.append(temp.toCharArray()[0]);
					} else
						sb.append('\'');
					break;
				case 'づ' :
				case 'ヅ' :
					sb.append("tzu");
					break;
				case 'て' :
				case 'テ' :
					sb.append("te");
					break;
				case 'で' :
				case 'デ' :
					sb.append("de");
					break;
				case 'と' :
				case 'ト' :
					sb.append("to");
					break;
				case 'ど' :
				case 'ド' :
					sb.append("do");
					break;
				// na column
				case 'な' :
				case 'ナ' :
					sb.append("na");
					break;
				case 'に' :
				case 'ニ' :
					sb.append("ni");
					break;
				case 'ぬ' :
				case 'ヌ' :
					sb.append("nu");
					break;
				case 'ね' :
				case 'ネ' :
					sb.append("ne");
					break;
				case 'の' :
				case 'ノ' :
					sb.append("no");
					break;
				// ha column
				case 'は' :
				case 'ハ' :
					sb.append("ha");
					break;
				case 'ば' :
				case 'バ' :
					sb.append("ba");
					break;
				case 'ぱ' :
				case 'パ' :
					sb.append("pa");
					break;
				case 'ひ' :
				case 'ヒ' :
					sb.append("hi");
					break;
				case 'び' :
				case 'ビ' :
					sb.append("bi");
					break;
				case 'ぴ' :
				case 'ピ' :
					sb.append("pi");
					break;
				case 'ふ' :
				case 'フ' :
					sb.append("fu");
					break;
				case 'ぶ' :
				case 'ブ' :
					sb.append("bu");
					break;
				case 'ぷ' :
				case 'プ' :
					sb.append("pu");
					break;
				case 'へ' :
				case 'ヘ' :
					sb.append("he");
					break;
				case 'べ' :
				case 'ベ' :
					sb.append("be");
					break;
				case 'ぺ' :
				case 'ペ' :
					sb.append("pe");
					break;
				case 'ほ' :
				case 'ホ' :
					sb.append("ho");
					break;
				case 'ぼ' :
				case 'ボ' :
					sb.append("bo");
					break;
				case 'ぽ' :
				case 'ポ' :
					sb.append("po");
					break;
				// ma column
				case 'ま' :
				case 'マ' :
					sb.append("ma");
					break;
				case 'み' :
				case 'ミ' :
					sb.append("mi");
					break;
				case 'む' :
				case 'ム' :
					sb.append("mu");
					break;
				case 'め' :
				case 'メ' :
					sb.append("me");
					break;
				case 'も' :
				case 'モ' :
					sb.append("mo");
					break;
				// ya column
				case 'や' :
				case 'ヤ' :
					sb.append("ya");
					break;
				case 'ゃ' :
				case 'ャ' :

					if (sb.length() > 2)
						temp = sb.substring(sb.length() - 3, sb.length());
					else
						temp = sb.substring(sb.length() - 2, sb.length());

					if (temp.equals("chi") || temp.equals("shi") || temp.endsWith("ji") || temp.endsWith("tzi")) {
						sb.deleteCharAt(sb.length() - 1);
						sb.append("a");

					} else {
						sb.deleteCharAt(sb.length() - 1);
						sb.append("ya");
					}
					break;

				case 'ゆ' :
				case 'ユ' :
					sb.append("yu");
					break;
				case 'ゅ' :
				case 'ュ' :
					if (sb.length() > 2)
						temp = sb.substring(sb.length() - 3, sb.length());
					else
						temp = sb.substring(sb.length() - 2, sb.length());

					if (temp.equals("chi") || temp.equals("shi") || temp.endsWith("ji") || temp.endsWith("tzi")) {
						sb.deleteCharAt(sb.length() - 1);
						sb.append("u");

					} else {
						sb.deleteCharAt(sb.length() - 1);
						sb.append("yu");
					}
					break;
				case 'よ' :
				case 'ヨ' :
					sb.append("yo");
					break;
				case 'ょ' :
				case 'ョ' :
					if (sb.length() > 2)
						temp = sb.substring(sb.length() - 3, sb.length());
					else
						temp = sb.substring(sb.length() - 2, sb.length());

					if (temp.equals("chi") || temp.equals("shi") || temp.endsWith("ji") || temp.endsWith("tzi")) {
						sb.deleteCharAt(sb.length() - 1);
						sb.append("o");

					} else {
						sb.deleteCharAt(sb.length() - 1);
						sb.append("yo");
					}
					break;
				// ra column
				case 'ら' :
				case 'ラ' :
					sb.append("ra");
					break;
				case 'り' :
				case 'リ' :
					sb.append("ri");
					break;
				case 'る' :
				case 'ル' :
					sb.append("ru");
					break;
				case 'れ' :
				case 'レ' :
					sb.append("re");
					break;
				case 'ろ' :
				case 'ロ' :
					sb.append("ro");
					break;
				// wa column
				case 'わ' :
				case 'ワ' :
					sb.append("wa");
					break;
				case 'を' :
				case 'ヲ' :
					sb.append("wo");
					break;
				// n
				case 'ん' :
				case 'ン' :
					sb.append("n");
					break;
				// ー
				case 'ー' :
					if (sb.length() > 0) {
						temp = sb.substring(sb.length() - 1);
						if (temp.equals("a")) {
							sb.deleteCharAt(sb.length() - 1);
							sb.append("â");
						} else if (temp.equals("i")) {
							sb.deleteCharAt(sb.length() - 1);
							sb.append("î");
						} else if (temp.equals("u")) {
							sb.deleteCharAt(sb.length() - 1);
							sb.append("û");
						} else if (temp.equals("e")) {
							sb.deleteCharAt(sb.length() - 1);
							sb.append("ê");
						} else if (temp.equals("o")) {
							sb.deleteCharAt(sb.length() - 1);
							sb.append("ô");
						} else {
							sb.append("-");
						}
					} else
						sb.append("-");
					break;
				// punctuation or brackets
				case '「' :
				case '」' :
				case '(' :
				case ')' :
				case '[' :
				case ']' :
					sb.append(c);
					break;
				case '。' :
					sb.append(".");
					break;
				case '、' :
					sb.append(",");
					break;
				case '…' :
					sb.append("...");
					break;
				case '～' :
				case '~' :
					sb.append("~");
					break;
				// okurigana or suffix marker
				case '.' :
					break;
				case '-' :
					sb.append('-');
					break;
				default :
					if (sb.length() > 1)
						temp = sb.substring(sb.length() - 1, sb.length());
					else
						temp = sb.toString();
					if (temp.equals(">")) {
						sb.deleteCharAt(sb.length() - 1);
						sb.append(c + ">");

					} else {
						sb.append("<" + c + ">");
					}

			}
		}

		return sb.toString();

	}

	// They are protected in order to implement a mutable super class
	protected Integer 				unicode;
	protected JISPair 				jisCode = null;
	protected RadicalTag		 	classicRad = null; // 0-214
	protected Integer 				nelsonRadical = null; // 0-212
	protected Integer 				grade = null; // 0-10
	protected Integer 				strokeCount = null; // 0-35
	protected Set<Integer> 			strokeMiscounts;// 0-37
	protected Set<VariantPair> 		variants;
	protected Integer 				frequency = null; // 2501
	protected Integer 				JLPTLevel = null; // 0-4
	protected Set<DicReferencePair> dicReferences = null;
	protected Set<ReadingEntry> 	readings = null;
	protected Set<MeaningEntry> 	meanings = null;
	protected KanjiQueryCodes 		queryCodes = null;
	protected KanjiGraph 			graph = null;

	protected KanjiTag() {
		// Explicit. For method "toRomaji" and inheritance
	}

	/**
	 * Compares this kanji and another kanji by their's unicode value
	 * 
	 * @param o
	 *            {@code Kanji} object to be compared with
	 * @return {@code
	 *         this.getUnicodeValue().compareTo(anotherKanji.getUnicodeValue())}
	 */
	@Override
	public int compareTo(KanjiTag o) {
		return unicode.compareTo(o.getUnicodeRef());
	}

	/**
	 * Indicates if this kanji's unicode value equals other character or kanji
	 * unicode value
	 */
	@Override
	public boolean equals(Object obj) {
		// I only use unicode value because I consider it the primary key.Two
		// Kanji with same unicode value but discouraged fields should never
		// happen. It's only allowed if the same field equals null and in the
		// other not, what means that the null field's class is not updated

		if (obj == this)
			return true;
		if (obj instanceof KanjiTag) {
			return this.getUnicodeRef()
					.equals(((KanjiTag) obj).getUnicodeRef());
		}
		if (obj instanceof Character) {
			char[] temp = new char[1];
			temp[0] = ((Character) obj).charValue();

			return this.getUnicodeRef().equals(Character.codePointAt(temp, 0));
		}

		return false;
	}

	/**
	 * Returns a predefined formatted string with all the information about this
	 * Kanji
	 * 
	 * @return {@code String} with all info about the kanji.
	 */
	public String formattedDescriptionString() {

		/* Example,kanji data not necessarily correct */
		// Character: 生
		// 
		// ______Codification
		// __________euc: 751f
		// __________jis208: 32-24
		//
		// ______Radical
		// __________Classical: 100
		// __________Nelson: null
		//
		// ______Miscelanea
		// __________Frequency: 29
		// __________Grade: 1
		// __________JPTL level: 4
		// __________Stroke count: 5
		// __________Variants:
		// _______________[jis208,2,3,4]
		// _______________[sakade,43]
		//
		// ______Dictionary References
		// __________nelson_c: 2991
		// __________nelson_n: 3715
		// __________halpern_njecd: 3497
		// __________halpern_kkld: 2179
		// __________heisig: 1555
		// __________gakken: 29
		// __________oneill_names: 214
		// __________oneill_kk: 67
		// __________moro: vol 7, page 1027, 21670
		// __________henshall: 42
		// __________sh_kk: 44
		// __________sakade: 34
		// __________jf_cards: 71
		// __________henshall3: 44
		// __________tutt_cards: 43
		// __________crowley: 9
		// __________kanji_in_context: 49
		// __________busy_people: 2.4
		// __________kodansha_compact: 1327
		// __________maniette: 1569
		//
		// ______Readings
		// __________pinyin: sheng1
		// __________korean_r: saeng
		// __________korean_h: ■
		// __________ja_on: セイ
		// __________ja_on: ショウ
		// __________ja_kun: い.きる
		// __________ja_kun: い.かす
		// __________ja_kun: い.ける
		// __________ja_kun: う.まれる
		// __________ja_kun: うま.れる
		// __________ja_kun: う.まれ
		// __________ja_kun: うまれ
		// __________ja_kun: う.む
		// __________ja_kun: お.う
		// __________ja_kun: は.える
		// __________ja_kun: は.やす
		// __________ja_kun: き
		// __________ja_kun: なま
		// __________ja_kun: なま-
		// __________ja_kun: な.る
		// __________ja_kun: な.す
		// __________ja_kun: む.す
		// __________ja_kun: -う
		// __________nanori: あさ
		// __________nanori: いき
		// __________nanori: いく
		// __________nanori: いけ
		// __________nanori: うぶ
		// __________nanori: うまい
		// __________nanori: え
		// __________nanori: おい
		// __________nanori: ぎゅう
		// __________nanori: くるみ
		// __________nanori: ごせ
		// __________nanori: さ
		// __________nanori: じょう
		// __________nanori: すぎ
		// __________nanori: そ
		// __________nanori: そう
		// __________nanori: ちる
		// __________nanori: なば
		// __________nanori: にう
		// __________nanori: にゅう
		// __________nanori: ふ
		// __________nanori: み
		// __________nanori: もう
		// __________nanori: よい
		// __________nanori: りゅう
		//
		// ______Meanings
		// __________en: life
		// __________en: genuine
		// __________en: birth
		// __________fr: vie
		// __________fr: naissance
		// __________fr: authentique
		// __________fr: cru
		// __________es: vida
		// __________es: nacimiento
		// __________es: vivir
		// __________es: existir
		// __________es: nacer
		// __________es: dar a luz
		// __________es: puro
		// __________es: crudo
		// __________pt: vida
		// __________pt: genuína
		// __________pt: nascimento
		//
		// ______Query Codes
		// __________Query Codes [deroo=2472, fourCorner=2510.0, skip=4-5-2, spahn_Hadamitzky=0a5.29 ]
		// ______Graph: AbcDef, end of stroke 1 isAtLeftOf begin of stroke 2, from begin of stroke 2 to the end of stroke 3 lenghtIsSmallerThanHalfGrid

		StringBuilder s = new StringBuilder();

		s.append("Character: " + toString() + "\n");

		s.append("\n\tCodification\n");
		s.append("\t\teuc: " + String.format("%x", getUnicodeRef()) + "\n");

		if (getJisCode() != null)
			s.append("\t\t" + getJisCode().getFirst() + ": " + getJisCode().getSecond() + "\n");

		s.append("\n\tRadical\n");
		s.append("\t\tClassical: " + getClassicRadical().getNumber() + "\n");

		if (getClassicRadical().getVariants().size() > 0) {
			s.append("\t\t\t: " + getClassicRadical() + "\n");
		}

		s.append("\t\tNelson: " + getNelsonRadical() + "\n");

		s.append("\n\tMiscelanea\n");
		s.append("\t\tFrequency: " + getFrequency() + "\n");
		s.append("\t\tGrade: " + getGrade() + "\n");
		s.append("\t\tJPTL level: " + getJLPTLevel() + "\n");
		s.append("\t\tStroke count: " + getStrokeCount() + "\n");

		if (getStrokeMiscounts().size() > 0) {
			s.append("\t\tStroke common miscounts : ");

			for (Integer b : getStrokeMiscounts())
				s.append(b + ",");

			s.deleteCharAt(s.length() - 1);
			s.append('\n');
		}
		if (getVariants().size() > 0) {
			s.append("\t\tVariants: ");

			for (VariantPair vp : getVariants())
				s.append("\n\t\t\t[" + vp.getFirst() + "," + vp.getSecond() + "]");
		}
		if (getDicReferences().size() > 0) {
			s.append("\n\tDictionary References\n");
			for (DicReferencePair p : getDicReferences()) {
				s.append("\t\t" + p.getFirst() + ": " + p.getSecond() + "\n");
			}
		}
		if (getReadings().size() > 0) {
			s.append("\n\tReadings\n");
			for (ReadingEntry de : getReadings()) {
				for (String s1 : de.getElements())
					s.append("\t\t" + de.getKey() + ": " + s1 + "\n");
			}
		}
		if (getMeanings().size() > 0) {
			s.append("\n\tMeanings\n");
			for (MeaningEntry de : getMeanings()) {
				for (String s1 : de.getElements())
					s.append("\t\t" + de.getKey() + ": " + s1 + "\n");
			}
		}

		s.append("\n\tQuery Codes\n");
		s.append("\t\t" + getQueryCodes() + "\n");

		s.append("\n\tGraph: \n");
		s.append("\t\t" + getGraph() + "\n");

		return s.toString();

	}

	public Set<DicReferencePair> getDicReferences() {
		return Collections.unmodifiableSet(dicReferences);
	}

	public Integer getFrequency() {
		return frequency;
	}

	public Integer getGrade() {

		return grade;
	}

	public KanjiGraph getGraph() {
		return graph;
	}

	/**
	 * Returns the jis code information stored in a {@code JISCode} object where
	 * {@code first()} represents the codification (e.g {@code "jis208"}) and
	 * {@code second()} represents the jis code (e.g {@code "16-2"}).
	 * 
	 * @return {@code JISPair} representing a JIS code.
	 */
	public JISPair getJisCode() {
		return jisCode;
	}

	public Integer getJLPTLevel() {
		return JLPTLevel;
	}

	public Set<MeaningEntry> getMeanings() {
		return Collections.unmodifiableSet(meanings);
	}

	public RadicalTag getClassicRadical() {
		return classicRad;
	}

	public Integer getNelsonRadical() {
		return nelsonRadical;
	}

	public KanjiQueryCodes getQueryCodes() {
		return queryCodes;
	}

	public Set<ReadingEntry> getReadings() {
		return Collections.unmodifiableSet(readings);
	}

	public Integer getStrokeCount() {

		return strokeCount;
	}

	public Set<Integer> getStrokeMiscounts() {
		return Collections.unmodifiableSet(strokeMiscounts);
	}

	public Integer getUnicodeRef() {
		return unicode;
	}

	public Set<VariantPair> getVariants() {
		return Collections.unmodifiableSet(variants);
	}

	@Override
	public int hashCode() {
		// I only use unicode value because I consider it the primary key.Two
		// Kanji with same unicode value but discouraged fields should never
		// happen. It's only allowed if the same field equals null and in the
		// other not, what means that the null field's class is not updated

		return unicode.hashCode();
	}

	public char[] toChar() {

		return Character.toChars(unicode);
	}

	/**
	 * Returns a copy of the current kanji in which all kana kanji in readings
	 * has been translated into romaji.
	 * 
	 * @return a copy of the current kanji with romanized text
	 */
	public final KanjiTag toRomaji() {

		KanjiTag r = new KanjiTag();

		r.unicode = unicode;
		r.classicRad = classicRad;
		r.dicReferences = dicReferences;
		r.frequency = frequency;
		r.grade = grade;
		r.jisCode = this.getJisCode();
		r.JLPTLevel = JLPTLevel;
		r.meanings = this.getMeanings();
		r.nelsonRadical = this.nelsonRadical;
		r.queryCodes = this.queryCodes;

		Set<ReadingEntry> res = new HashSet<ReadingEntry>();

		for (ReadingEntry refe : this.getReadings()) {
			// only kana readings
			if (refe.getKey().equals("ja_on") || refe.getKey().equals("ja_kun") || refe.getKey().equals("nanori")) {

				ReadingEntry ret = null;
				List<String> elements = new ArrayList<String>();

				for (String s : refe.getElements())
					elements.add(KanjiTag.kanaToRomaji(s));

				ret = new ReadingEntry(refe.getKey(), elements, refe.getUnicodeRef());

				res.add(ret);
			} else {
				res.add(refe);
			}
		}

		r.readings = res;
		r.strokeCount = strokeCount;
		r.strokeMiscounts = strokeMiscounts;
		r.unicode = this.getUnicodeRef();
		r.variants = this.getVariants();

		return r;

	}

	@Override
	public String toString() {

		return new String(toChar());
	}
	
	// TODO: add to diagram
	// TODO: this functionality has been implemented many times in many places.
	// Find and replace
	/**
	 * Returns a property of this kanji using a defined instance of a {@code
	 * KanjiFieldsEnum}. The type and the content of the returned instance
	 * depends on the specified type by {@code kfe}.
	 * 
	 * @param kfe
	 *            A defined instance of {@code KanjiFieldsEnum} representing a
	 *            kanji field
	 * 
	 * @return Depending on the specified field denoted by {@code kfe}, it
	 *         returns and upcasted {@code Object} from:
	 * 
	 * <br>
	 * <br>
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_CLASSIC_NELSON}
	 *         :</b> An {@code Integer} representing the Nelson radical<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_CLASSIC_RADICAL}
	 *         :</b> An {@code Integer} representing the classic radical number<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_DE_ROO} :</b> A
	 *         {@code String} representing the De Roo query code<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_DIC_NAME} :</b> A
	 *         {@code Set<String>} representing the dictionary names in which
	 *         this kanji is referenced<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_DIC_INDEX} :</b> A
	 *         {@code Set<DicReferencePair>} representing all the references of
	 *         this kanji in dictionaries<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_FOUR_CORNER} :</b>
	 *         A {@code String} representing the four corner query code of this
	 *         kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_FREQUENCY} :</b>
	 *         An {@code Integer} representing the frequency of this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_GRADE} :</b> An
	 *         {@code Integer} representing the grade of this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_GRAPH} :</b> A
	 *         {@code KanjiGraph} representing the graph info of this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_JIS_CHARSET} :</b>
	 *         A {@code String} representing the charset of this kanji JIS code<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_JIS_CODE} :</b> A
	 *         {@code JISPair} representing the JIS code of this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_JLPT_LEVEL} :</b>
	 *         An {@code Integer} representing the JLPT level of this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_LITERAL} :</b> The
	 *         {@code String} representation of this kanji unicode value<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_MEANING} :</b> A
	 *         {@code Set<MeaningEntry>} representing all the meanings of this
	 *         kanji in different languages<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_MEANING_LANGUAGE}
	 *         :</b> A {@code Set<String>} representing all the languages for
	 *         this kanji has registered meanings<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_READING_TYPE}
	 *         :</b> A {@code Set<String>} representing all the readings types
	 *         existing in this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_READING} :</b> A
	 *         {@code Set<ReadingEntry>} representing all the readings of this
	 *         kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_SKIP} :</b> A
	 *         {@code String} representing the SKIP query code of this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_SPAHN_HADAMITZKY}
	 *         :</b> A {@code String} representing the Spahn-Hadamitzky code of
	 *         this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_STROKE_COUNT}
	 *         :</b> An {@code Integer} representing the stroke count of this
	 *         kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_STROKE_MISCOUNT}
	 *         :</b> A {@code Set<Integer>} representing the common stroke
	 *         miscounts of this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_UNICODE_VALUE}
	 *         :</b> An {@code Integer} representing<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_VARIANT_INDEX}
	 *         :</b> A {@code Set<VariantPair>} representing all the variant
	 *         references of this kanji<br>
	 * 
	 *         &nbsp;&nbsp;&nbsp;&nbsp;<b>{@code kfe == KANJI_VARIANT_TYPE}
	 *         :</b> A {@code Set<String>} representing all the variants types
	 *         of this kanji
	 */
	public Object getByEnum(KanjiFieldEnum kfe) {

		switch (kfe) {
			case KANJI_CLASSIC_NELSON :
				return nelsonRadical;
			case KANJI_DE_ROO :
				return queryCodes.getDeRooCode();
			case KANJI_CLASSIC_RADICAL :
				return classicRad.getNumber();
			case KANJI_DIC_NAME :
				Set<String> dics = new HashSet<String>();
				for (DicReferencePair drp : dicReferences)
					dics.add(drp.getFirst());
				return dics;
			case KANJI_DIC_INDEX :
				return dicReferences;
			case KANJI_FOUR_CORNER :
				return queryCodes.getFourCornerCode();
			case KANJI_FREQUENCY :
				return frequency;
			case KANJI_GRADE :
				return grade;
			case KANJI_GRAPH :
				return graph;
			case KANJI_JIS_CHARSET :
				return jisCode.getFirst();
			case KANJI_JIS_CODE :
				return jisCode.getSecond();
			case KANJI_JLPT_LEVEL :
				return JLPTLevel;
			case KANJI_LITERAL:
				return toString();
			case KANJI_MEANING :
				return meanings;
			case KANJI_MEANING_LANGUAGE :
				Set<String> langs = new HashSet<String>();
				for (MeaningEntry me : meanings)
					langs.add(me.getKey());
				return langs;
			case KANJI_READING :
				return readings;
			case KANJI_READING_TYPE :
				Set<String> readTypes = new HashSet<String>();
				for (ReadingEntry re : readings)
					readTypes.add(re.getKey());
				return readTypes;
			case KANJI_SKIP :
				return queryCodes.getSkipCode();
			case KANJI_SPAHN_HADAMITZKY :
				return queryCodes.getSpahnHadamitzkyCode();
			case KANJI_STROKE_COUNT :
				return strokeCount;
			case KANJI_STROKE_MISCOUNT :
				return strokeMiscounts;
			case KANJI_UNICODE_VALUE :
				return unicode;
			case KANJI_VARIANT_INDEX :
				return variants;
			case KANJI_VARIANT_TYPE :
				Set<String> varTypes = new HashSet<String>();
				for (VariantPair vp : variants)
					varTypes.add(vp.getFirst());
				return varTypes;
			case KANJI_FIRST_MEANING :
				throw new IllegalArgumentException("Languaje not selected");
			case KANJI_FIRST_READING :
				throw new IllegalArgumentException("Reading type not selected");				
		}

		throw new IllegalArgumentException();
	}

	public Object getByEnum(KanjiFieldEnum kfe, String value) {

		switch (kfe) {
			case KANJI_DIC_REFERENCE :
				DicReferencePair selDr = null;
				for (DicReferencePair dr : dicReferences) {
					if (dr.getFirst().equals(value.replace("DIC_", ""))) {
						selDr = dr;
						break;
					}
				}

				if (selDr == null)
					return null;
				else
					return selDr.getSecond();
			case KANJI_FIRST_MEANING :
				MeaningEntry selMe = null;
				for (MeaningEntry me : meanings) {
					if (me.getKey().toUpperCase().equals(value)) {
						selMe = me;
						break;
					}
				}
				
				if(selMe==null)
					return null;
				else
					return selMe.getElements().get(0);
			case KANJI_FIRST_READING :
				ReadingEntry selRe = null;
				for (ReadingEntry re : readings) {
					if (re.getKey().equals(value)) {
						selRe = re;
						break;
					}
				}
				
				if (selRe == null)
					return null;
				else
					return selRe.getElements().get(0);
			default:
				return getByEnum(kfe);
		}
	}
}
