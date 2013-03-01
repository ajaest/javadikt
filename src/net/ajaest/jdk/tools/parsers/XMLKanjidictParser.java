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

package net.ajaest.jdk.tools.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.JISPair;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiQueryCodes;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.Radical;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.jdk.data.kanji.VariantPair;
import net.ajaest.lib.string.Strings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//TODO: JavaDoc
public class XMLKanjidictParser {

	private static Map<String, Integer> numericDicRefsMaxLength = new HashMap<String, Integer>();
	static {
		numericDicRefsMaxLength.put("kanji_basic_book", 3);
		numericDicRefsMaxLength.put("gakken", 4);
		numericDicRefsMaxLength.put("jf_cards", 4);
		numericDicRefsMaxLength.put("maniette", 4);
		numericDicRefsMaxLength.put("henshall", 4);
		numericDicRefsMaxLength.put("sakade", 3);
		numericDicRefsMaxLength.put("oneill_kk", 4);
		numericDicRefsMaxLength.put("halpern_kkld", 4);
		numericDicRefsMaxLength.put("halpern_njecd", 4);
		numericDicRefsMaxLength.put("tutt_cards", 4);
		numericDicRefsMaxLength.put("sh_kk", 4);
		numericDicRefsMaxLength.put("kodansha_compact", 4);
		numericDicRefsMaxLength.put("nelson_n", 4);
		numericDicRefsMaxLength.put("nelson_c", 4);
		numericDicRefsMaxLength.put("crowley", 3);
		numericDicRefsMaxLength.put("kanji_in_context", 4);
		numericDicRefsMaxLength.put("henshall3", 4);
		numericDicRefsMaxLength.put("heisig", 4);
	}

	private Document doc;

	public XMLKanjidictParser(String s) throws FileNotFoundException, SAXException, IOException {

		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		doc = documentBuilder.parse(new FileInputStream(s));
	}

	public Set<Kanji> parse() {

		Element elementRaiz = doc.getDocumentElement();
		NodeList child = elementRaiz.getChildNodes();

		Set<Kanji> ks = new TreeSet<Kanji>();
		Kanji temp = null;

		// ignorant algorithm: don't know, don't touch
		for (int i = 0, j = 0; i < child.getLength(); i++) {
			Node node = child.item(i);
			if (node instanceof Element && j > -100) {
				j++;

				if (node.getNodeName() == "character") {

					// temp = new
					// Kanji(nodo.getChildNodes().item(1).getTextContent().charAt(0));

					NodeList nl = node.getChildNodes();
					for (int i1 = 0; i1 < nl.getLength(); i1++) {

						if (nl.item(i1) instanceof Element) {
							if (nl.item(i1).getNodeName() == "codepoint") {
								temp = codepoint(nl.item(i1));
							}
							if (nl.item(i1).getNodeName() == "radical") {
								radical(temp, nl.item(i1));
							}
							if (nl.item(i1).getNodeName() == "misc") {
								miscelanea(temp, nl.item(i1));
							}
							if (nl.item(i1).getNodeName() == "dic_number") {
								dicReferences(temp, nl.item(i1));
							}
							if (nl.item(i1).getNodeName() == "query_code") {
								queryCodes(temp, nl.item(i1));
							}
							if (nl.item(i1).getNodeName() == "reading_meaning") {
								readingMeaning(temp, nl.item(i1));
							}
						}
					}
					ks.add(temp);

				}

			}

		}
		return ks;

	}

	private void readingMeaning(Kanji temp, Node n) {

		NodeList nl = n.getChildNodes().item(1).getChildNodes();

		Map<String, List<String>> meanings = new HashMap<String, List<String>>();
		Map<String, List<String>> readings = new HashMap<String, List<String>>();
		String s1;
		String s2;
		Node aux;
		
		for (int i1 = 0; i1 < nl.getLength(); i1++) {
			if (nl.item(i1) instanceof Element) {
				if (nl.item(i1).getNodeName().equals("meaning")) {
					if (nl.item(i1).getAttributes().getLength() == 0) {
						s1 = "en";
						s2 = nl.item(i1).getTextContent();
					} else {
						s1 = nl.item(i1).getAttributes().getNamedItem("m_lang").getNodeValue();
						s2 = nl.item(i1).getTextContent();
					}

					if (meanings.get(s1) == null) {
						List<String> entries = new ArrayList<String>();
						entries.add(s2);
						meanings.put(s1, entries);
					} else {
						meanings.get(s1).add(s2);
					}

				} else if (nl.item(i1).getNodeName().equals("reading")) {
					s1 = nl.item(i1).getAttributes().getNamedItem("r_type").getNodeValue();
					s2 = nl.item(i1).getTextContent();
					aux = nl.item(i1).getAttributes().getNamedItem("r_status");
					if (aux != null)
						System.out.println(aux.getNodeValue());
					if (readings.get(s1) == null) {
						List<String> entries = new ArrayList<String>();
						entries.add(s2);
						readings.put(s1, entries);
					} else {
						readings.get(s1).add(s2);
					}
				}
			}
		}
		nl = n.getChildNodes();
		for (int i1 = 0; i1 < nl.getLength(); i1++) {
			if (nl.item(i1).getNodeName().equals("nanori")) {
				s1 = "nanori";
				s2 = nl.item(i1).getTextContent();
				if (readings.get(s1) == null) {
					List<String> entries = new ArrayList<String>();
					entries.add(s2);
					readings.put(s1, entries);
				} else {
					readings.get(s1).add(s2);
				}
			}
		}

		Set<MeaningEntry> me = new HashSet<MeaningEntry>();
		Set<ReadingEntry> re = new HashSet<ReadingEntry>();

		for (String s : meanings.keySet()) {
			me.add(new MeaningEntry(s, meanings.get(s), temp.getUnicodeRef()));
		}
		for (String s : readings.keySet()) {
			re.add(new ReadingEntry(s, readings.get(s), temp.getUnicodeRef()));
		}

		temp.setMeanings(me);
		temp.setReadings(re);

	}

	private void queryCodes(Kanji temp, Node n) {

		NodeList nl = n.getChildNodes();

		String s1, s2;

		for (int i1 = 0; i1 < nl.getLength(); i1++) {

			if (nl.item(i1) instanceof Element) {

				s1 = nl.item(i1).getAttributes().getNamedItem("qc_type").getNodeValue();
				s2 = nl.item(i1).getTextContent();

				if (s1.equals("skip"))
					temp.setQueryCodes(new KanjiQueryCodes(s2, temp.getQueryCodes().getSpahnHadamitzkyCode(), temp.getQueryCodes().getDeRooCode(), temp.getQueryCodes().getFourCornerCode(), temp.getUnicodeRef()));
				else if (s1.equals("sh_desc"))
					temp.setQueryCodes(new KanjiQueryCodes(temp.getQueryCodes().getSkipCode(), s2, temp.getQueryCodes().getDeRooCode(), temp.getQueryCodes().getFourCornerCode(), temp.getUnicodeRef()));
				else if (s1.equals("four_corner"))
					temp.setQueryCodes(new KanjiQueryCodes(temp.getQueryCodes().getSkipCode(), temp.getQueryCodes().getSpahnHadamitzkyCode(), temp.getQueryCodes().getDeRooCode(), s2, temp.getUnicodeRef()));
				else if (s1.equals("deroo"))
					temp.setQueryCodes(new KanjiQueryCodes(temp.getQueryCodes().getSkipCode(), temp.getQueryCodes().getSpahnHadamitzkyCode(), s2, temp.getQueryCodes().getFourCornerCode(), temp.getUnicodeRef()));

			}
		}

	}

	private void dicReferences(Kanji temp, Node n) {

		NodeList nl = n.getChildNodes();

		String s1, s2, numRef;

		for (int i1 = 0; i1 < nl.getLength(); i1++) {
			if (nl.item(i1) instanceof Element) {
				if (nl.item(i1).getAttributes().getLength() > 1) { //only for "moro" dic

					s1 = nl.item(i1).getAttributes().getNamedItem("dr_type").getNodeValue();

					String page, vol, ref;
					vol = nl.item(i1).getAttributes().getNamedItem("m_vol").getNodeValue();
					page = nl.item(i1).getAttributes().getNamedItem("m_page").getNodeValue();
					ref = nl.item(i1).getTextContent();
					numRef = ref;

					// repair moro references in order to have same size in all
					// ref strings
					vol = Strings.fillHead(vol, '0', 2 - vol.length());
					page = Strings.fillHead(page, '0', 4 - page.length());
					ref = repairMoroDicRef(ref);

					s2 = ref + "|";
					s2 += " vol " + vol;
					s2 += ", page " + page;


				} else {
					s1 = nl.item(i1).getAttributes().getNamedItem("dr_type").getNodeValue();
					s2 = nl.item(i1).getTextContent();
					numRef = s2;

					// repair non numeric dic references sizes
					if (s1.equals("moro")) {
						s2 = repairMoroDicRef(s2) + "| vol ??, page ????";
					} else if (s1.equals("busy_people")) {
						s2 = Strings.fillTail(s2, '0', 4 - s2.length());
					} else if (s1.equals("oneill_names")) {
						s2 = repairOneill_NamesRef(s2);
					} else { // repair numeric
						s2 = numericDicRefsMaxLength.get(s1) != null ? Strings.fillHead(s2, '0', numericDicRefsMaxLength.get(s1) - s2.length()) : s2;
					}
				 }

				temp.getDicReferences().add(new DicReferencePair(s1, s2, Integer.valueOf(Strings.trimToDecimalNumeric(numRef)), temp.getUnicodeRef()));
			}
		}

	}

	private void miscelanea(Kanji temp, Node n) {

		NodeList nl = n.getChildNodes();

		String s1, s2;

		for (int i1 = 0; i1 < nl.getLength(); i1++) {
			if (nl.item(i1) instanceof Element) {

				if (nl.item(i1).getNodeName().equals("grade"))
					temp.setGrade(Integer.valueOf(nl.item(i1).getTextContent()));

				if (nl.item(i1).getNodeName().equals("stroke_count")) {
					if (temp.getStrokeCount() == null)
						temp.setStrokeCount(Integer.valueOf(nl.item(i1).getTextContent()));
					else {
						temp.getStrokeMiscounts().add(Integer.valueOf(nl.item(i1).getTextContent()));
					}
				}

				if (nl.item(i1).getNodeName().equals("variant")) {
					s1 = nl.item(i1).getAttributes().getNamedItem("var_type").getNodeValue();
					s2 = nl.item(i1).getTextContent();
					temp.getVariants().add(new VariantPair(s1, s2, temp.getUnicodeRef()));
				}

				if (nl.item(i1).getNodeName().equals("freq"))
					temp.setFrequency(Integer.valueOf(nl.item(i1).getTextContent()));

				if (nl.item(i1).getNodeName().equals("jlpt"))
					temp.setJLPTLevel(Integer.valueOf(nl.item(i1).getTextContent()));
			}
		}

	}

	private void radical(Kanji temp, Node n) {

		NodeList nl = n.getChildNodes();

		String s1, s2;

		for (int i1 = 0; i1 < nl.getLength(); i1++) {
			if (nl.item(i1) instanceof Element) {

				s1 = nl.item(i1).getAttributes().getNamedItem("rad_type").getNodeValue();
				s2 = nl.item(i1).getTextContent();

				if (s1.equals("classical"))
					temp.setClassicRadical(new Radical(Integer.valueOf(s2)));
				else if (s1.equals("nelson_c"))
					temp.setNelsonRadical(Integer.valueOf(s2));

			}
		}

	}

	private Kanji codepoint(Node n) {

		Kanji temp = null;
		String s1, s2;

		NodeList nl = n.getChildNodes();

		for (int i1 = 0; i1 < nl.getLength(); i1++) {
			if (nl.item(i1) instanceof Element) {

				s1 = nl.item(i1).getAttributes().getNamedItem("cp_type").getNodeValue();
				s2 = nl.item(i1).getTextContent();

				if (s1.contains("ucs"))
					temp = new Kanji(Integer.parseInt(s2, 16));

				if (s1.contains("jis"))
					temp.setJisCode(new JISPair(s1, s2, temp.getUnicodeRef()));
			}
		}

		return temp;
	}



	private static String repairMoroDicRef(String ref) {
		try {
			Integer.valueOf(ref, 10);
			ref = Strings.fillHead(ref, '0', 5 - ref.length());
			ref = Strings.fillTail(ref, ' ', 2);
		} catch (NumberFormatException e) {
			if (ref.length() < 6)
				ref = Strings.fillHead(ref, '0', 6 - ref.length());
			ref = Strings.fillTail(ref, ' ', 7 - ref.length());
		}

		return ref;
	}

	private static String repairOneill_NamesRef(String ref) {
		try {
			Integer.valueOf(ref, 10);
			ref = Strings.fillHead(ref, '0', 4 - ref.length());
			ref = Strings.fillTail(ref, ' ', 1);
		} catch (NumberFormatException e) {
			if (ref.length() < 6)
				ref = Strings.fillHead(ref, '0', 5 - ref.length());
		}
		return ref;
	}

	public static void main(String... args) {
		XMLKanjidictParser kp = null;

		try {
			kp = new XMLKanjidictParser("resources/data/kanjidic22.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Set<Kanji> k = kp.parse();
		Iterator<Kanji> it = k.iterator();
		for (int i = 0; i < 1000; i++) {
			System.out.println(it.next().formattedDescriptionString());
		}
		System.out.println(k.size());
	}
}
