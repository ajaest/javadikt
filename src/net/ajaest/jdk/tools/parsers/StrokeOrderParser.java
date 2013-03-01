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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ajaest.jdk.data.auxi.AllowedStrokeClueEnum;
import net.ajaest.jdk.data.auxi.AllowedStrokePointEnum;
import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.auxi.KanjiStrokeClue;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

//TODO: JavaDoc
public class StrokeOrderParser {

	public class ExporterDialog extends javax.swing.JDialog {


		private static final long serialVersionUID = 321976699961363310L;


		private javax.swing.JButton jButton1;

		private javax.swing.JButton jButton2;
		private javax.swing.JCheckBox jCheckBox1;
		private javax.swing.JLabel jLabel1;
		public ExporterDialog(java.awt.Frame parent, boolean modal) {
			super(parent, modal);
			this.initComponents();
		}
		private void initComponents() {

			this.jCheckBox1 = new javax.swing.JCheckBox();
			this.jLabel1 = new javax.swing.JLabel();
			this.jButton1 = new javax.swing.JButton();
			this.jButton2 = new javax.swing.JButton();

			this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

			this.jLabel1.setText("jLabel1");

			this.jButton1.setText("jButton1");

			this.jButton2.setText("jButton2");

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this.getContentPane());
			this.getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 304, Short.MAX_VALUE).addComponent(this.jCheckBox1)).addGroup(layout.createSequentialGroup().addComponent(this.jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(this.jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))).addContainerGap()));
			layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(this.jCheckBox1).addComponent(this.jLabel1)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(this.jButton1).addComponent(this.jButton2)).addContainerGap(20, Short.MAX_VALUE)));

			this.pack();
		}

	}

	public static void main(String... args) throws IOException {

		StrokeOrderParser sop = new StrokeOrderParser("resources/data/strokedata.txt", true);
		Map<Integer, KanjiGraph> map = sop.parse();


		KanjiGraph temp = null;
		Character kLit = null;
		for(Integer i: map.keySet()){
			kLit = new Character((char) i.intValue());
			temp = map.get(i);
			System.out.println(kLit + ": " + temp);
		}


		System.out.println("[[[[NOKANJI= " + sop.getNoKanjiReads() + "]]]]");
		System.out.println("[[[[SIZE = " + map.size() + "]]]]");
		System.out.println("[[[[REPEATED = " + sop.getRepeatedInfo() + "]]]]");

		for (Integer i : sop.getRepeated().keySet()) {
			kLit = new Character((char) i.intValue());
			temp = sop.getRepeated().get(i);
			System.out.println(kLit + ": " + temp);
		}

		char[] ichi = new char[1];
		ichi[0] = '干';
		int ichiUCS = Character.codePointAt(ichi, 0);

		KanjiGraph kg = map.get(ichiUCS);
		System.out.println(kg);
	}

	private Map<Integer, KanjiGraph> retrieved = null;

	private Map<Integer, KanjiGraph> repeated = null;
	private BufferedReader fReader = null;

	private Integer noKanjiReads = 0;

	private Integer repeatedInfo = 0;

	private boolean verbose;

	public StrokeOrderParser(String path, boolean verbose) throws FileNotFoundException, UnsupportedEncodingException {

		this.fReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF16"));
		this.retrieved = new HashMap<Integer, KanjiGraph>();
		this.repeated = new HashMap<Integer, KanjiGraph>();
	}

	public void addExceptions() {
		//This is the only char that this parser cannot parse. That's because is the only
		//kanji which has a stroke clue whose point order is greater than nine, so it has
		//an extra char
		//M漢CCIFBBBFbFFFBaC|y10-j7
		char[] c = new char[1];
		c[0] = '漢';
		Integer unicodeRef = Character.codePointAt(c, 0);

		List<KanjiStroke> lks = null;
		try {
			lks = this.parseStrokes("CCIFBBBFbFFFBaC", unicodeRef);
		} catch (CorruptedFormatException e) {
			e.printStackTrace();
		}

		Set<KanjiStrokeClue> sksc = new HashSet<KanjiStrokeClue>();
		KanjiStrokeClue ksc1 = new KanjiStrokeClue(10, 7, AllowedStrokePointEnum.begin, AllowedStrokePointEnum.end, AllowedStrokeClueEnum.isBelowOf, unicodeRef);
		sksc.add(ksc1);

		KanjiGraph kg1 = new KanjiGraph(lks, sksc, unicodeRef);

		this.retrieved.put(kg1.getUnicodeRef(), kg1);
	}

	public void close() throws IOException {
		this.fReader.close();
	}

	public boolean EOF() {

		int read = -1;

		try {
			this.fReader.mark(1);
			read = this.fReader.read();
			this.fReader.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return read == -1;
	}

	@Override
	public void finalize() throws IOException {
		this.fReader.close();
	}

	public Integer getNoKanjiReads() {
		return this.noKanjiReads;
	}
	public Map<Integer, KanjiGraph> getRepeated() {
		return this.repeated;
	}


	public Integer getRepeatedInfo() {
		return this.repeatedInfo;
	}

	public Map<Integer, KanjiGraph> getRetrieved() {
		return this.retrieved;
	}

	public Map<Integer, KanjiGraph> parse() throws IOException {

		this.addExceptions();

		KanjiGraph temp = null;
		while (!this.EOF()) {
			try {
				temp = this.readKanji();
				if (this.retrieved.get(temp.getUnicodeRef()) != null) {
					this.repeated.put(temp.getUnicodeRef(), temp);
					this.repeatedInfo++;
				}

				this.retrieved.put(temp.getUnicodeRef(), temp);
			} catch (CorruptedFormatException e) {
				if (this.verbose) {
					System.out.println(e.getClass().getName() + ":" + e.getMessage());
					for (StackTraceElement st : e.getStackTrace()) {
						System.out.println("\t" + st.toString());
					}
				}
				this.noKanjiReads++;
				this.fReader.readLine();// if nk-kanji character is found, skip
				// line

			}
		}

		return this.retrieved;
	}

	private Set<KanjiStrokeClue> parseClues(String trim, Integer unicodeRef) throws CorruptedFormatException {

		Set<KanjiStrokeClue> ksc = new HashSet<KanjiStrokeClue>();
		String[] splitted = this.removeVoidString(trim.split("[! ]"));

		Integer OrderFirst;
		AllowedStrokePointEnum pointFirst;
		Integer OrderSecond;
		AllowedStrokePointEnum pointSecond;
		AllowedStrokeClueEnum clue;
		String[] points;
		String delimiter;
		for (String s : splitted) {
			points = s.trim().split("[<=-]");// trim cannot be used
			int delimiterOffset = s.indexOf(points[0]) + points[0].length();
			delimiter = s.substring(delimiterOffset, delimiterOffset + 1);

			if (points.length != 2)
				throw new CorruptedFormatException("Unknown stroke clue format: " + s);

			pointFirst = this.solveStrokePoint(points[0]);
			OrderFirst = this.solveStrokeOrder(points[0]);

			clue = this.solveStrokeClue(points[0], delimiter, points[1]);

			pointSecond = this.solveStrokePoint(points[1]);
			OrderSecond = this.solveStrokeOrder(points[1]);

			ksc.add(new KanjiStrokeClue(OrderFirst, OrderSecond, pointFirst, pointSecond, clue, unicodeRef));
		}

		return ksc;
	}

	private List<KanjiStroke> parseStrokes(String s, Integer unicodeRef) throws CorruptedFormatException {

		List<KanjiStroke> kss = new ArrayList<KanjiStroke>();
		List<AllowedStrokeLineEnum> tempAsl = null;
		int count = 0;

		for (char c : s.toCharArray()) {
			if (Character.isUpperCase(c)) {// Beginning of a stroke
				if (tempAsl != null) {
					kss.add(new KanjiStroke(tempAsl, count, unicodeRef));
					count++;
				}
				tempAsl = new ArrayList<AllowedStrokeLineEnum>();
			}

			tempAsl.add(this.solveStrokeType(c));
		}
		kss.add(new KanjiStroke(tempAsl, count, unicodeRef));
		return kss;
	}

	private KanjiGraph readKanji() throws IOException, CorruptedFormatException {

		this.skipStrokeCountChar();
		Integer unicodeRef = this.readKanjiLiteral();

		String kanjiStrokeInfo = this.fReader.readLine();

		// split strokes and stroke clues
		String[] splitted = this.removeVoidString(kanjiStrokeInfo.split("[|]"));
		if ((splitted.length != 2) && (splitted.length != 1))
			throw new CorruptedFormatException("Unknown stroke info format: " + kanjiStrokeInfo);
		// process strokes

		List<KanjiStroke> ksl = this.parseStrokes(splitted[0].trim(), unicodeRef);
		Set<KanjiStrokeClue> kscl = null;
		if (splitted.length > 1)
			kscl = this.parseClues(splitted[1].trim(), unicodeRef);

		return new KanjiGraph(ksl, kscl, unicodeRef);
	}

	private int readKanjiLiteral() throws CorruptedFormatException, IOException {

		int read = this.fReader.read();

		if (Character.isHighSurrogate((char) read)) {
			char[] surrogate = new char[2];
			surrogate[0] = (char) read;
			read = this.fReader.read(); // reads high surrogate
			surrogate[1] = (char) read;
			read = Character.codePointAt(surrogate, 0);
		}

		if(!Kanji.isKanji(read)){
			char[] readChar;
			readChar = Character.toChars(read);

			StringBuilder s = new StringBuilder();
			s.append(readChar[0]);
			if (readChar.length > 1)
				s.append(readChar[1]);

			throw new CorruptedFormatException("Kanji literal was expected, 「" + s.toString() + "」 was read instead");
		}

		return read;
	}

	private String[] removeVoidString(String[] ss) {

		int realSize = 0;

		for (String s : ss) {
			if (!s.equals(""))
				realSize++;
		}

		String[] withoutVoid = new String[realSize];
		int count = 0;

		for (String s : ss) {
			if (!s.equals("")) {
				withoutVoid[count] = s;
				count++;
			}

		}

		return withoutVoid;
	}
	private void skipStrokeCountChar() throws CorruptedFormatException, IOException {

		int read = this.fReader.read();

		//first character can be from upper case "A" to upper case "W"
		if ((read < 65) || (read > 87))
			throw new CorruptedFormatException("Stroke count marker was expected, 「" + (char) read + "」 was read instead");

	}

	private AllowedStrokeClueEnum solveStrokeClue(String first, String clue, String second) throws CorruptedFormatException {
		if ((clue.length() != 1) || (first.length()!=2) || (second.length()!=2))
			throw new CorruptedFormatException("Kanji stroke clue definition was expected, 「" + first+clue+second + "」 was recieved instead");

		switch (clue.charAt(0)) {
		case '-' :
			if(	first.startsWith("i") || first.startsWith("a") || (first.startsWith("x") &&
					second.startsWith("i")) || second.startsWith("a") || second.startsWith("x"))
				return AllowedStrokeClueEnum.isAtLeftOf;
			else if (	first.startsWith("y") || first.startsWith("j") || (first.startsWith("b") &&
					second.startsWith("y")) || second.startsWith("j") || second.startsWith("b"))
				return AllowedStrokeClueEnum.isBelowOf;

			else if( first.startsWith("l") && second.startsWith("l"))
				return AllowedStrokeClueEnum.isLongerThan;

			else
				throw new CorruptedFormatException("Kanji stroke clue literal('<','-' or '=') was expected, 「" + clue + "」 was recieved instead");

		case '<' :
			return AllowedStrokeClueEnum.fromToSmallerThanHalfGridSize;
		case '=' :
			return AllowedStrokeClueEnum.areIntheSamePoint;
		default :
			throw new CorruptedFormatException("Kanji stroke clue literal('<','-' or '=') was expected, 「" + clue + "」 was recieved instead");
		}
	}

	private Integer solveStrokeOrder(String string) throws CorruptedFormatException {

		if (string.length() != 2)
			throw new CorruptedFormatException("Kanji stroke point definition was expected, 「" + string + "」 was recieved instead");

		return Integer.parseInt(string.trim().substring(1, 2));
	}

	private AllowedStrokePointEnum solveStrokePoint(String string) throws CorruptedFormatException {
		if (string.length() != 2)
			throw new CorruptedFormatException("Kanji stroke point definition was expected, 「" + string + "」 was recieved instead");

		switch (string.trim().charAt(0)) {
		case 'i' :
		case 'j' :
			return AllowedStrokePointEnum.end;
		case 'a' :
		case 'b' :
			return AllowedStrokePointEnum.ave;
		case 'x' :
		case 'y' :
			return AllowedStrokePointEnum.begin;
		case 'l' :
			return AllowedStrokePointEnum.length;
		default :
			throw new CorruptedFormatException("Kanji stroke point marker(i,j,a,b,x,y or l) was expected, 「" + string.trim().charAt(0) + "」 was recieved instead");
		}
	}

	private AllowedStrokeLineEnum solveStrokeType(char c) throws CorruptedFormatException {

		switch (Character.toLowerCase(c)) {
		case 'a' :
			return AllowedStrokeLineEnum.A;
		case 'b' :
			return AllowedStrokeLineEnum.B;
		case 'c' :
			return AllowedStrokeLineEnum.C;
		case 'd' :
			return AllowedStrokeLineEnum.D;
		case 'e' :
			return AllowedStrokeLineEnum.E;
		case 'f' :
			return AllowedStrokeLineEnum.F;
		case 'g' :
			return AllowedStrokeLineEnum.G;
		case 'h' :
			return AllowedStrokeLineEnum.H;
		case 'i' :
			return AllowedStrokeLineEnum.I;
		default :
			throw new CorruptedFormatException("Kanji stroke type char was expected, 「" + c + "」 was received instead");

		}
	}

}
