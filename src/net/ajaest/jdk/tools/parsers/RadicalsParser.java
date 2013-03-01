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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.ajaest.jdk.data.auxi.KanjiEnums.RadicalTypeEnum;
import net.ajaest.jdk.data.kanji.Radical;
import net.ajaest.jdk.data.kanji.RadicalVariant;
import net.ajaest.jdk.data.kanji.RadicalVariantTag;

//TODO: JavaDoc
public class RadicalsParser {

	private BufferedReader radicalFile;

	public RadicalsParser(File f) throws FileNotFoundException {
		
		radicalFile = new BufferedReader(new FileReader(f));
	}

	public Map<Integer, Radical> parse() throws IOException {

		Map<Integer, Radical> radicals = new HashMap<Integer, Radical>();

		Radical rad;
		RadicalVariant radVar;
		while (!EOF()) {

			radVar = parseLine(radicalFile.readLine());
			rad = radicals.get(radVar.getNumber());

			if (rad == null) {
				rad = new Radical(radVar.getNumber());
				radicals.put(rad.getNumber(), rad);
			}

			rad.addRadicalVariant(radVar);
		}

		return radicals;
	}

	public RadicalVariant parseLine(String s) throws IOException {
		
		RadicalVariant rad = null;

		String[] parsed = s.split("[\\s\" \"]");
		List<String> elements = new ArrayList<String>();
		Integer stringOffset = 0;

		for (String element : parsed) {
			if (element.length() != 0)
				elements.add(element);
		}

		// init numbers

		Integer radNumber;

		if (elements.get(0).contains(".")) {
			String[] numbers = elements.get(0).split("[.]");

			radNumber = Integer.decode(numbers[0]);
			rad = new RadicalVariant(radNumber);
			rad.setAuxNumber(Integer.decode(numbers[1]));
		} else {

			radNumber = Integer.decode(elements.get(0));
			rad = new RadicalVariant(radNumber);
			rad.setAuxNumber(1);
		}

		// init names

		String namesStrings;
		Integer firstChar = (int) elements.get(1).toCharArray()[0];
		Integer hiragana_first = 0x3041;
		Integer hiragana_last = 0x30FF;
		
		if (firstChar >= hiragana_first && firstChar <= hiragana_last) {
			namesStrings = elements.get(1);
		} else {
			// to jump the radical literal
			stringOffset++;
			namesStrings = elements.get(2);
		}

		String[] names = namesStrings.split("[,]");

		for (String name : names) {
			rad.getNames().add(name);
		}

		// init type

		rad.setRadicalType(identifyType(elements.get(3 + stringOffset)));

		// init unicode
		// only lines with more than 4 elements
		if (elements.size() > 4) {
			rad.setUnicode(Integer.valueOf(elements.get(5), 16));

			if (elements.size() > 6) {
				String unicodeName = "";
				for (int i = 6; i < elements.size() - 1; i++) {
					unicodeName += elements.get(i) + " ";
				}

				rad.setUnicodeName(unicodeName.substring(0, unicodeName.length() - 1));
				rad.setKangxiUnicode(Integer.valueOf(elements.get(elements.size() - 1), 16));
			}
		}
		return rad;
	}

	private boolean EOF() {

		int read = -1;
		// FIXME: chapuza
		try {
			radicalFile.mark(1);
			read = radicalFile.read();
			radicalFile.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return read == -1;
	}

	private RadicalTypeEnum identifyType(String s) {

		if (s.equals("O"))
			return RadicalTypeEnum.original;
		if (s.equals("E"))
			return RadicalTypeEnum.enclosure;
		if (s.equals("L"))
			return RadicalTypeEnum.left;
		if (s.equals("B"))
			return RadicalTypeEnum.bottom;
		if (s.equals("R"))
			return RadicalTypeEnum.right;
		if (s.equals("V"))
			return RadicalTypeEnum.variable;
		if (s.equals("T"))
			return RadicalTypeEnum.top;

		throw new IllegalArgumentException("type not supported:" + s);
	}

	public static void main(String... args) throws IOException {

		Map<Integer, Radical> asd = new RadicalsParser(new File("resources/data/extra/radicals")).parse();

		for (Radical r : new TreeSet<Radical>(asd.values())) {
			for (RadicalVariantTag var : new TreeSet<RadicalVariantTag>(r.getVariants()))
				System.out.println(var);
		}
	}
}
