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

package net.ajaest.jdk.core.main;

import java.awt.Font;
import java.awt.Image;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.ajaest.jdk.core.winHandlers.AboutWH;
import net.ajaest.jdk.core.winHandlers.ExportDialogsEngine;
import net.ajaest.jdk.core.winHandlers.InfoDialogsEngine;
import net.ajaest.jdk.core.winHandlers.KanjiInfoWH;
import net.ajaest.jdk.core.winHandlers.MainWH;
import net.ajaest.jdk.data.dict.query.IntegerValueQAbout;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.KanjiQuery;
import net.ajaest.jdk.data.dict.query.StringValueQAbout;
import net.ajaest.jdk.data.dict.query.ValueQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.KanjiGraphQAbout;
import net.ajaest.jdk.data.kanji.JISPair;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.lib.string.Strings;
import net.ajaest.lib.swing.util.EditContextMenuEngine;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

//TODO: JavaDoc
public class JDKGUIEngine implements Runnable {

	private JavaDiKt jdk = null;

	private EditContextMenuEngine ecmE;

	private MainWH mWH = null;
	private ExportDialogsEngine edWH = null;
	private KanjiInfoWH kiWH = null;
	private InfoDialogsEngine idE;
	private AboutWH aWH;

	private KanjiExpression currentKE = null;

	private List<KanjiTag> currentResult = null;
	private List<Integer> currentRefResult = null;

	private List<Image> icons;
	private Font unicodeFont;

	public JDKGUIEngine(JavaDiKt jdk) {
		this.jdk = jdk;

		icons = new ArrayList<Image>();
		icons.add(new ImageIcon(this.getClass().getResource("/images/3.0_16.png")).getImage());
		icons.add(new ImageIcon(this.getClass().getResource("/images/3.0_24.png")).getImage());
		icons.add(new ImageIcon(this.getClass().getResource("/images/3.0_32.png")).getImage());
		icons.add(new ImageIcon(this.getClass().getResource("/images/3.0_48.png")).getImage());
		icons.add(new ImageIcon(this.getClass().getResource("/images/3.0_256.png")).getImage());

		try {
			// retrieves unicode font
			unicodeFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("font/Sazanami-Hanazono-Mincho.ttf")).deriveFont((float) 14);
			Messages.localePrintln("**JDKGui, Unicode font retrieved: " + unicodeFont.getName());
		} catch (Exception e) {
			// retrieves default font
			unicodeFont = new JPanel().getFont().deriveFont(14);
		}

		currentResult = new ArrayList<KanjiTag>();

		initEngine();
	}

	@Override
	public void run() {
		mWH.getWindow().setVisible(true);

		if (!jdk.isLastVersion())
			this.displayNewVersionNotice(jdk.getLastVersion());
	}

	public void initEngine() {
		Messages.localePrintln("**JDKGUIEngine: initializing GUI engine");

		ecmE = new EditContextMenuEngine(ISO639ー1.parse(getOptions().getLanguage().toUpperCase()));

		idE = new InfoDialogsEngine(this);
		mWH = new MainWH(this);
		edWH = new ExportDialogsEngine(this);
		kiWH = new KanjiInfoWH(this);
		aWH = new AboutWH(this);
	}

	public List<Integer> executeRefQuery(List<String> boolKeys, List<String> fieldKeys, List<String> caseKeys, List<Object> valueKeys) {

		currentKE = translateToQuery(boolKeys, fieldKeys, caseKeys, valueKeys);

		currentRefResult = jdk.executeRefQuery(currentKE);

		return currentRefResult;
	}

	private KanjiExpression translateToQuery(List<String> boolKeys, List<String> fieldKeys, List<String> caseKeys, List<Object> valueKeys) {

		KanjiQuery tempKQ = new KanjiQuery();
		ValueQAbout<?> tempVQA = null;
		KanjiExpression tempKE;
		
		// first iteration
		tempVQA = selectField(tempKQ, fieldKeys.get(0));
		
		tempKE = selectCase(tempVQA, fieldKeys.get(0), caseKeys.get(0), valueKeys.get(0));

		if (boolKeys.size() > 1) {
			for (int i = 1; i < boolKeys.size(); i++) {
				tempKQ = selectBooleanConector(tempKE, boolKeys.get(i));
				tempVQA = selectField(tempKQ, fieldKeys.get(i));

				tempKE = selectCase(tempVQA, fieldKeys.get(i), caseKeys.get(i), valueKeys.get(i));
			}
		}
		
		return tempKE;
	}

	private Object translateValue(String fieldKey, Object value) {

		String key;

		// Special cases
		if (fieldKey.equals(Messages.KANJI_DIC_NAME)) {

			// Dic name stored in database is dic name message key without
			// "DIC_" beginning
			return Messages.getKey((String) value).substring("DIC_".length());
		} else if (fieldKey.equals(Messages.KANJI_DIC_INDEX)) {
			return Strings.trimToDecimalNumeric((String) value);
		} else if (fieldKey.equals(Messages.KANJI_LITERAL)) {
			
			String strValue = (String) value;
			
			return new Integer(Character.codePointAt(strValue.toCharArray(), 0)).toString();
		} else if (fieldKey.equals(Messages.KANJI_JIS_CHARSET)) {

			key = Messages.getKey((String) value);

			if (key.equals(Messages.JIS_208))
				return JISPair.JIS_X_208;
			if (key.equals(Messages.JIS_212))
				return JISPair.JIS_X_212;
			if (key.endsWith(Messages.JIS_213))
				return JISPair.JIS_X_213;
			else
				throw new IllegalArgumentException("String not supported");
		} else if (fieldKey.equals(Messages.KANJI_CLASSIC_RADICAL)) {

			String strValue = (String) value;

			return strValue.substring(0, 3);

		} else if (fieldKey.equals(Messages.KANJI_READING_TYPE)) {

			key = Messages.getKey((String) value);

			if (key.equals(Messages.READ_KOREAN_H))
				return "korean_h";
			else if (key.equals(Messages.READ_KOREAN_R))
				return "korean_r";
			else if (key.equals(Messages.READ_JA_KUN))
				return "ja_kun";
			else if (key.equals(Messages.READ_JA_ON))
				return "ja_on";
			else if (key.equals(Messages.READ_NANORI))
				return "nanori";
			else if (key.equals(Messages.READ_PINYIN))
				return "pinyin";
			else
				throw new IllegalArgumentException("String not supported");

			// String values
		} else if (	fieldKey.equals(Messages.KANJI_DE_ROO) ||
					fieldKey.equals(Messages.KANJI_DIC_INDEX) ||
					fieldKey.equals(Messages.KANJI_FOUR_CORNER) ||
					fieldKey.equals(Messages.KANJI_JIS_CODE) ||
					fieldKey.equals(Messages.KANJI_MEANING) ||
					fieldKey.equals(Messages.KANJI_READING) ||
					fieldKey.equals(Messages.KANJI_SKIP) ||
					fieldKey.equals(Messages.KANJI_SPAHN_HADAMITZKY) ||
					fieldKey.equals(Messages.KANJI_VARIANT_INDEX) || 
					fieldKey.equals(Messages.KANJI_VARIANT_TYPE)){
			return value;
			// Integer values
		} else if( 	fieldKey.equals(Messages.KANJI_CLASSIC_NELSON) ||
					fieldKey.equals(Messages.KANJI_FREQUENCY) ||
					fieldKey.equals(Messages.KANJI_GRADE) ||
					fieldKey.equals(Messages.KANJI_JLPT_LEVEL) ||
					fieldKey.equals(Messages.KANJI_STROKE_COUNT)){
			return value;
		} else if (fieldKey.equals(Messages.KANJI_UNICODE_VALUE)) {
			return Integer.valueOf((String) value, 16).toString();
		} else if (fieldKey.equals(Messages.KANJI_GRAPH)) {
			return new KanjiGraph((String) value, null, -1);
		}
		else
			throw new IllegalArgumentException("String not supported");
		
	}

	private KanjiQuery selectBooleanConector(KanjiExpression tempKE, String booleanConnector) {

		if (booleanConnector.equals(Messages.WINMAIN_QUERY_DATA_cellAndString))
			return tempKE.AND();
		else if (booleanConnector.equals(Messages.WINMAIN_QUERY_DATA_cellOrString))
			return tempKE.OR();
		else
			throw new IllegalArgumentException("String not supported");
	}

	private KanjiExpression selectCase(ValueQAbout<?> tempVQA, String fieldKey, String caseKey, Object value) {
		
		if(tempVQA instanceof StringValueQAbout){

			StringValueQAbout svqa = (StringValueQAbout) tempVQA;
			
			if(caseKey.equals(Messages.CASE_EQUALS))
				return svqa.equal((String)translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_NOT_EQUALS))
				return svqa.notEquals((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_NULL))
				return svqa.isNull();
			else if (caseKey.equals(Messages.CASE_EQUALS_OR_GREATHER))
				return svqa.equaslOrGreatherThan((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_EQUALS_OR_LESS))
				return svqa.equalsOrLessThan((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_GREATHER))
				return svqa.greatherThan((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_LESS))
				return svqa.lessThan((String) translateValue(fieldKey, value));
			else
				throw new IllegalArgumentException("String not supported");
			
		} else if (tempVQA instanceof IntegerValueQAbout) {

			IntegerValueQAbout ivqa = (IntegerValueQAbout) tempVQA;
			
			if(caseKey.equals(Messages.CASE_EQUALS))
				return ivqa.equalz((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_EQUALS_OR_GREATHER))
				return ivqa.equaslOrGreatherThan((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_EQUALS_OR_LESS))
				return ivqa.equalsOrLessThan((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_GREATHER))
				return ivqa.greatherThan((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_LESS))
				return ivqa.lessThan((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_NOT_EQUALS))
				return ivqa.notEquals((String) translateValue(fieldKey, value));
			else if (caseKey.equals(Messages.CASE_NULL))
				return ivqa.isNull();
			else
				throw new IllegalArgumentException("String not supported");
		} else if (tempVQA instanceof KanjiGraphQAbout) {

			KanjiGraphQAbout kgqa = (KanjiGraphQAbout) tempVQA;

			return kgqa.equal(new KanjiGraph((String) value, null, -1));
		}
		throw new IllegalArgumentException("String not supported");
	}

	private ValueQAbout<?> selectField(KanjiQuery tempKQ, String fieldKey) {

		if(fieldKey.equals(Messages.KANJI_CLASSIC_NELSON))
			return tempKQ.classic_nelson();
		else if (fieldKey.equals(Messages.KANJI_LITERAL))
			return tempKQ.unicode_value();
		else if(fieldKey.equals(Messages.KANJI_CLASSIC_RADICAL))
			return tempKQ.classic_radical();
		else if(fieldKey.equals(Messages.KANJI_DE_ROO))
			return tempKQ.De_Roo_code();
		else if(fieldKey.equals(Messages.KANJI_DIC_INDEX))
			return tempKQ.dictionary_reference();
		else if(fieldKey.equals(Messages.KANJI_DIC_NAME))
			return tempKQ.dictionary_name();
		else if(fieldKey.equals(Messages.KANJI_FOUR_CORNER))
			return tempKQ.four_corner_code();
		else if(fieldKey.equals(Messages.KANJI_FREQUENCY))
			return tempKQ.frequency();
		else if(fieldKey.equals(Messages.KANJI_GRADE))
			return tempKQ.grade();
		else if (fieldKey.equals(Messages.KANJI_GRAPH))
			return tempKQ.kanji_graph();
		else if(fieldKey.equals(Messages.KANJI_JIS_CHARSET))
			return tempKQ.JIS_charset();
		else if(fieldKey.equals(Messages.KANJI_JIS_CODE))
			return tempKQ.JIS_code_value();
		else if(fieldKey.equals(Messages.KANJI_JLPT_LEVEL))
			return tempKQ.JLPT_level();
		else if(fieldKey.equals(Messages.KANJI_MEANING))
			return tempKQ.meaning();
		else if(fieldKey.equals(Messages.KANJI_READING))
			return tempKQ.reading();
		else if(fieldKey.equals(Messages.KANJI_READING_TYPE))
			return tempKQ.reading_type();
		else if(fieldKey.equals(Messages.KANJI_SKIP))
			return tempKQ.skip_code();
		else if(fieldKey.equals(Messages.KANJI_SPAHN_HADAMITZKY))
			return tempKQ.SpahnーHadamizky_code();
		else if(fieldKey.equals(Messages.KANJI_STROKE_COUNT))
			return tempKQ.stroke_count();
		else if(fieldKey.equals(Messages.KANJI_UNICODE_VALUE))
			return tempKQ.unicode_value();
		else if(fieldKey.equals(Messages.KANJI_VARIANT_INDEX))
			return tempKQ.variant_reference();
		else if(fieldKey.equals(Messages.KANJI_VARIANT_TYPE))
			return tempKQ.variant_type();
		else
			throw new IllegalArgumentException("String not supported");
	}

	public JDKOptions getOptions() {

		return jdk.getOptions();
	}
	
	public ExportDialogsEngine getExporDialogsWH() {
		return edWH;
	}

	public static void main(String... args) {
		Launcher.main();
	}

	public List<Image> getIcons() {

		return icons;
	}

	public EditContextMenuEngine getEditContextMenuEngine() {

		return ecmE;
	}

	public MainWH getMainWH(){
		
		return mWH;
	}

	public KanjiInfoWH getKanjiInfoWinEngine() {

		return kiWH;
	}

	public JavaDiKt getJavaDikt() {

		return jdk;
	}

	public List<KanjiTag> getCurrentResult() {
		return currentResult;
	}

	public InfoDialogsEngine getInfoDialgosEngine() {
		return idE;
	}

	public AboutWH getAboutWH() {
		return aWH;
	}

	public void reinitEngine() {

		mWH.getWindow().dispose();
		kiWH.disposeAllWindows();

		String lang = jdk.getOptions().getLanguage().toUpperCase();

		Messages.initMessages(lang);

		ecmE = new EditContextMenuEngine(ISO639ー1.parse(lang));

		idE = new InfoDialogsEngine(this);
		mWH = new MainWH(this);
		edWH = new ExportDialogsEngine(this);
		kiWH = new KanjiInfoWH(this);
		aWH = new AboutWH(this);

		mWH.getWindow().setVisible(true);
	}

	public void setUnicodeFont(Font font) {

		this.unicodeFont = font;
	}

	public Font getUnicodeFont() {

		return unicodeFont;
	}

	public void displayNewVersionNotice(String version) {
		
		String title = Messages.get(Messages.INFODIALOG_title_newVersion);
		String message = String.format(Messages.get(Messages.INFODIALOG_infoMessage_newVersion), version);
		idE.invokeInfoDialog(title, message, null);
	}
}
