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

package net.ajaest.jdk.tools.adders;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.ajaest.jdk.core.main.ExceptionHandler;
import net.ajaest.jdk.core.main.ExceptionHandler.KNOWN_EXCEPTIONS;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Serializer;
import nu.xom.Text;
import nu.xom.Attribute.Type;



//TODO: JavaDoc
public class KanjiBasicBookDicReferenceAdder {

	private String dicName = "kanji_basic_book";
	private BufferedReader bis;

	public void addReferences(String xmlKanjiDicPath, String kbbReferences) {

		Map<Integer, Integer> references = getKBBreferences(kbbReferences);

		addReferences(xmlKanjiDicPath, references);
	}

	private void addReferences(String xmlKanjiDicPath, Map<Integer, Integer> references) {
		
		try{
			Document doc = new Builder().build(xmlKanjiDicPath);

			Element root = doc.getRootElement();
			if (root.getLocalName() != "kanjidic2")
				throw new RuntimeException("cannot parse kanjidic2"); //TODO: create specific exception

			Elements kanjis = root.getChildElements("character");

			Integer codepoint;
			Element codepointElem;
			Element kanji;
			Element dics;
			Attribute atb;
			Element dic;
			for (int i = 0; i < kanjis.size(); i++) {
				kanji = kanjis.get(i);
				codepointElem = kanji.getChildElements("codepoint").get(0).getChildElements().get(0);
				codepoint = Integer.valueOf(codepointElem.getValue(), 16);
				if (references.containsKey(codepoint)) {
					dic = new Element("dic_ref");
					atb = new Attribute("dr_type", dicName);
					atb.setValue(dicName);
					atb.setType(Type.CDATA);
					dic.addAttribute(atb);
					dic.appendChild(new Text(references.get(codepoint).toString()));
					dics = kanji.getChildElements("dic_number").get(0);
					dics.appendChild(dic);
				}
			}

			try {
				Serializer slr = new Serializer(new BufferedOutputStream(new FileOutputStream(xmlKanjiDicPath + "2")), "UTF-16");
				slr.setLineSeparator("\r\n");
				slr.setIndent(4);
				slr.write(doc);
				slr.flush();

			} catch (UnsupportedEncodingException e) {
				ExceptionHandler.handleException(e);
			} catch (FileNotFoundException e) {
				ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.INVALID_CONFIG_FILE_PATH);
			} catch (IOException e) {
				ExceptionHandler.handleException(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private Map<Integer, Integer> getKBBreferences(String kbbReferences) {
		
		Map<Integer, Integer> refMap = new HashMap<Integer, Integer>();

		try{

			bis = new BufferedReader(new FileReader(kbbReferences));

			int codePoint;
			int i = 1;
			while (!EOF()) {
				codePoint = Character.codePointAt(bis.readLine().toCharArray(), 0);
				refMap.put(codePoint, i);
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return refMap;
	}

	public boolean EOF() {

		int read = -1;

		try {
			bis.mark(1);
			read = bis.read();
			bis.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return read == -1;
	}

	public static void main(String... args) {

		KanjiBasicBookDicReferenceAdder kbb = new KanjiBasicBookDicReferenceAdder();

		kbb.addReferences("resources/data/kanjidic22.xml", "resources/data/Kanji Basic Book kanji list.txt");

	}

}

