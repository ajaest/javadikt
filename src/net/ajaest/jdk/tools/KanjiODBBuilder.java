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

package net.ajaest.jdk.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.ajaest.jdk.data.dict.KanjiDatabaseInfo;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.Radical;
import net.ajaest.jdk.tools.parsers.RadicalsParser;
import net.ajaest.jdk.tools.parsers.StrokeOrderParser;
import net.ajaest.jdk.tools.parsers.XMLKanjidictParser;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.OdbConfiguration;
import org.xml.sax.SAXException;

/**
 * 
 * @author Luis Alfonso Arce González
 * @deprecated Now the class used to build JavaDiKt's database is
 *             {@link TreeNeodatisKanjiODBBuilder}
 */
public class KanjiODBBuilder {

	private long t1, t2, total1, total2;
	private Set<KanjiTag> kanjis;
	private SortedSet<Radical> radicals;
	private KanjiDatabaseInfo kdinfo = null;

	public void construct(String kanjidict, String strokeOrder, String radical,String kanjiODB) throws FileNotFoundException, SAXException, IOException {

		total1 = System.currentTimeMillis();


		System.out.println("--Retrieving kanji info ");

		// ------------------

		System.out.println("\t*KANJIDIC ");

		XMLKanjidictParser kdp = new XMLKanjidictParser(kanjidict);
		Set<Kanji> sk = null;
		t1 = System.currentTimeMillis();
		sk = kdp.parse();
		t2 = System.currentTimeMillis();

		System.out.println("\t\tFinished. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");
		System.out.println("\t\tStatics: parsed=" + sk.size());

		// ------------------

		System.out.println("\t*Stroke order ");

		StrokeOrderParser sop = new StrokeOrderParser(strokeOrder, false);
		Map<Integer, KanjiGraph> skg = null;
		t1 = System.currentTimeMillis();
		skg = sop.parse();
		t2 = System.currentTimeMillis();

		System.out.println("\t\tFinished. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");
		System.out.println("\t\tStatics: parsed=" + skg.size() + ", fails=" + sop.getNoKanjiReads() + ", repeated = " + sop.getRepeatedInfo());

		// ------------------
		
		System.out.println("\t*Kanji radicals ");
		
		RadicalsParser rp = new RadicalsParser(new File(radical));
		t1 = System.currentTimeMillis();
		radicals = new TreeSet<Radical>(rp.parse().values());
		t2 = System.currentTimeMillis();
		System.out.println("\t\tFinished. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");
		
		// ------------------

		System.out.println("--Processing kanji info");

		// ------------------

		System.out.println("\t*Mixing kanji info ");

		// TODO: include radical info in kanji
		t1 = System.currentTimeMillis();
		for (Kanji k : sk) {
			KanjiGraph kg = skg.get(k.getUnicodeRef());
			if (kg != null)
				k.setGraph(kg);
		}
		t2 = System.currentTimeMillis();
		System.out.println("\t\tFinished. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

		// ------------------

		System.out.println("\t*Translating kanji into kanjiTag ");

		t1 = System.currentTimeMillis();
		kanjis = new HashSet<KanjiTag>(sk);
		t2 = System.currentTimeMillis();
		System.out.println("\t\tFinished. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

		// ------------------

		System.out.println("--Building Kanji Object Database");

		// ------------------

		File f = new File(kanjiODB);
		if (f.exists()) {
			System.out.println("\t*Deleting previous database");
			f.delete();
			System.out.println("\t\tdeleted");
		}

		// ------------------

		System.out.println("\t*Creating database");

		OdbConfiguration.setDatabaseCharacterEncoding("UTF-8");
		ODB od = ODBFactory.open(kanjiODB);
		String[] kanjiFieldNames = {"unicode"};
		od.getClassRepresentation(Kanji.class).addIndexOn("kanji-index", kanjiFieldNames, true);

		// ------------------

		System.out.println("\t*Storing database info");

		kdinfo = createDataBaseInformation();
		od.store(kdinfo);


		// ------------------

		System.out.println("\t*Storing kanjis");

		t1 = System.currentTimeMillis();
		for (KanjiTag kt : kanjis)
			od.store(kt);
		t2 = System.currentTimeMillis();
		System.out.println("\t\tFinished. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

		// ------------------

		System.out.println("\t*Storing radical info");

		t1 = System.currentTimeMillis();
		for (Radical rad : radicals)
			od.store(rad);
		t2 = System.currentTimeMillis();
		System.out.println("\t\tFinished. Time elapsed: " + ((float) (t2 - t1)) / 1000 + "s");

		// ------------------

		System.out.println("\t*Closing database");
		od.commit();
		od.close();
		System.out.println("\t\tclosed");

		// ------------------

		total2 = System.currentTimeMillis();
		System.out.println("All tasks finished. Time elapsed: " + ((float) (total2 - total1)) / 1000 + "s");

	}


	private static KanjiDatabaseInfo createDataBaseInformation() {

		Calendar inicio = new GregorianCalendar();
		String name = "Java Object KANJIDIC";

		List<String> copyrights = new ArrayList<String>();
		copyrights.add("©KanjiDict, Jim Breen");
		copyrights.add("©SKIP codes in KANJIDIC, Jack Halpern");
		copyrights.add("©Pinyin information in KANJIDIC, Christian Wittern and Koichi Yasuoka");
		copyrights.add("©Four Corner codes and Morohashi information in KANJIDIC, Urs App");
		copyrights.add("©Kanji descriptors from Spahn and Hadamitzky's dictionary in KANJIDIC, Mark Spahn and Wolfgang Hadamitzky");
		copyrights.add("©Romanized Korean readingsin KANJIDIC, Charles Muller");
		copyrights.add("©De Roo codes in KANJIDIC, Joseph de Roo");

		StringBuilder licensesNamed = new StringBuilder();
		licensesNamed.append("ELECTRONIC DICTIONARY RESEARCH AND DEVELOPMENT GROUP, GENERAL DICTIONARY LICENCE STATEMENT" + "; ");
		licensesNamed.append("Creative Commons Attribution-ShareAlike Licence (V3.0)");

		String licensecontent = "<P ALIGN=\"JUSTIFY\"><B>1. Introduction</B></P><P ALIGN=\"JUSTIFY\">In March 2000, James William Breen assigned ownership of the copyrightof the dictionary files assembled, coordinated and edited by him to theThe Electronic Dictionary Research and Development Group, then atat Monash University (hereafter \"the Group\"), on the understanding thatthe Groupwill foster the development of the dictionary files, and will utilize allmonies received for use of the files for the furtherdevelopment of the files, and for research into computer lexicography andelectronic dictionaries.</P><P ALIGN=\"JUSTIFY\">This document outlines the licence arrangement put in place by TheGroup for usage of the files. It replaces all previous copyright andlicence statements applying to the files.</P><P ALIGN=\"JUSTIFY\"><B>2. Application</B></P><P ALIGN=\"JUSTIFY\">This licence statement and copyright notice applies to thefollowing dictionary files, the associated documentation files,and any data files which are derived from them.</P><P ALIGN=\"JUSTIFY\"></P><UL><LI><P ALIGN=\"JUSTIFY\"><B>JMDICT</B>- Japanese-Multilingual Dictionary File - the Japanese and Englishcomponents (the German, French and Russian translational equivalentsare covered by separate copyright held by the compilers of that material.)</P></LI><LI><P ALIGN=\"JUSTIFY\"><B>EDICT</B>- Japanese-English Electronic DICTionary File</P></LI><LI><P ALIGN=\"JUSTIFY\"><B>ENAMDICT</B>- Japanese Names File</P></LI><LI><P ALIGN=\"JUSTIFY\"><B>COMPDIC</B>- Japanese-English Computing and Telecommunications Terminology File</P></LI><LI><P ALIGN=\"JUSTIFY\"><B>KANJIDIC2</B>- File of Information about the Kanji in JIS X 0208, JIS X 0212 and JIS X0213 in XML format.</P></LI><LI><P ALIGN=\"JUSTIFY\"><B>KANJIDIC</B>- File of Information about the 6,355 Kanji in the JIS X 0208 Standard<I>(special conditions apply)</I></P></LI><LI><P ALIGN=\"JUSTIFY\"><B>KANJD212</B>- File of Information about the 5,801 Supplementary Kanji in theJIS X 0212 Standard</P></LI><LI><P ALIGN=\"JUSTIFY\"><B>EDICT-R</B>- romanized version of the EDICT file. (NB: this file has been withdrawnfrom circulation, and all sites carrying it are requested to removetheir copies.)</P></LI><LI><P ALIGN=\"JUSTIFY\"><B>RADKFILE/KRADFILE</B>- files relating to the decomposition of the 6,355 kanji in JIS X 0208into their visible components.</P><P ALIGN=\"JUSTIFY\"></P></LI></UL><P ALIGN=\"JUSTIFY\">Copyright over the documents covered by this statement is held by JamesWilliam BREEN and The Electronic Dictionary Research andDevelopment Group.</P><P ALIGN=\"JUSTIFY\"><B>3. Licence Conditions</B></P><P ALIGN=\"JUSTIFY\"><a href=\"http://creativecommons.org/licenses/by-sa/3.0/\"><imgsrc=\"http://creativecommons.org/images/public/somerights20.gif\"></a></P><P ALIGN=\"JUSTIFY\">The dictionary files are made available under a Creative CommonsAttribution-ShareAlike Licence (V3.0). The Licence Deed can be viewed <a HREF=\"http://creativecommons.org/licenses/by-sa/3.0/\">here, </a>and the full Licence Code is <a HREF=\"http://creativecommons.org/licenses/by-sa/3.0/legalcode\">here. </a></P><P ALIGN=\"JUSTIFY\">In summary (extract from the Licence Deed):<center><table border width=\"80%\"><tr><td>You are free:<ul><li>to Share - to copy, distribute and transmit the work<li>to Remix - to adapt the work</ul>Under the following conditions:<ul><li>Attribution. You must attribute the work in the manner specified by the author or licensor (but not in any way that suggests that they endorse you or your use of the work).<li>Share Alike. If you alter, transform, or build upon this work, you may distribute the resulting work only under the same, similar or a compatible licence.</ul></td></tr></table></center></P><P ALIGN=\"JUSTIFY\">For attribution of these files, you must:</P><OL type=\"a\"><LI><P ALIGN=\"JUSTIFY\">in the case of publishing significant extracts of the files, ormaterial based on significant extracts of the files, e.g. in a publisheddictionary or invocabulary lists, clearly acknowledge that you used the files for this purpose;</P></LI><LI><P ALIGN=\"JUSTIFY\">in the case of a software package or WWW server, etc. which uses thefiles or incorporates data from the files, you must:</P><OL type=\"i\"><LI><P ALIGN=\"JUSTIFY\">acknowledge the usage and source of the files in the documentation,publicity material, WWW site of the package/server, etc.;</P></LI><LI><P ALIGN=\"JUSTIFY\">provide copies of the documentation and licence files (in the caseof software packages). Where the application packaging does not providefor the inclusion of such files (e.g. with iPhone applications), it issufficient to provide links, as per the next point;</P></LI><LI><P ALIGN=\"JUSTIFY\">provide links to either local copies ofthe documentation and licence files or to the locations of the filesat Monash University or at the EDRDG site.</P><P ALIGN=\"JUSTIFY\">If a WWW server is providing a dictionary function or an on-screendisplay of words from the files, the acknowledgement must be made on eachscreen display, e.g. in the form of a message at the foot of the screenor page. If, however, material from the files is mixed with information fromother sources, it is sufficient to provide a general acknowledgement of thesources as described above.</P><P ALIGN=\"JUSTIFY\">For the EDICT, JMdict and KANJIDIC files, thefollowing URLs may  be used or quoted:</P><UL><LI><P ALIGN=\"JUSTIFY\"> <a HREF=\"http://www.csse.monash.edu.au/~jwb/edict.html\">http://www.csse.monash.edu.au/~jwb/edict.html </a></P></LI><LI><P ALIGN=\"JUSTIFY\"> <a HREF=\"http://www.csse.monash.edu.au/~jwb/jmdict.html\">http://www.csse.monash.edu.au/~jwb/jmdict.html </a></P></LI><LI><P ALIGN=\"JUSTIFY\"> <a HREF=\"http://www.csse.monash.edu.au/~jwb/kanjidic.html\">http://www.csse.monash.edu.au/~jwb/kanjidic.html </a></P></LI></UL></LI></OL></LI></OL>See <a HREF=\"sample.html\">this page </a>for samples of possible acknowledgement text.<P ALIGN=\"JUSTIFY\">Note that in all cases, the addition of material to the files or toextracts from the files does not remove or in any way diminish theGroup's copyright over the files.</P><P ALIGN=\"JUSTIFY\">Note also that provided the conditions above are met,there is NO restriction placed on commercial use of the files.The files can be bundled with software and sold for whateverthe developer wants to charge.Software using these files does not have to be under any form ofopen-source licence.</P><P ALIGN=\"JUSTIFY\">Where use of the files results in a financial return to the user, it issuggested that the user make a donation to the Group to assist with thecontinued development of the files. There areseveral ways of donating:</P><UL><LI><P ALIGN=\"JUSTIFY\"> send a cheque (check) in any currency made outto \"Monash University\". The address to use is:</P><DL><DD><pre>Jim BreenElectronic Dictionary Research GroupClayton School of Information TechnologyMonash UniversityClayton, Vic, 3800Australia</pre></DL></LI><LI><P ALIGN=\"JUSTIFY\">make a donation via PayPal using a credit or debit card. Simply click onthe following button and follow the instructions.<form action=\"https://www.paypal.com/cgi-bin/webscr\" method=\"post\"><input type=\"hidden\" name=\"cmd\" value=\"_xclick\"><input type=\"hidden\" name=\"business\" value=\"Jim.Breen@infotech.monash.edu.au\"><input type=\"hidden\" name=\"item_name\" value=\"Electronic Dictionary Research Group\"><input type=\"hidden\" name=\"item_number\" value=\"EDRGDON001\"><input type=\"hidden\" name=\"no_shipping\" value=\"2\"><input type=\"hidden\" name=\"no_note\" value=\"1\"><input type=\"hidden\" name=\"currency_code\" value=\"AUD\"><input type=\"hidden\" name=\"tax\" value=\"0\"><input type=\"hidden\" name=\"bn\" value=\"PP-DonationsBF\"><input type=\"image\" src=\"https://www.paypal.com/en_US/i/btn/x-click-but21.gif\" border=\"0\" name=\"submit\" alt=\"Make payments with PayPal - it's fast, free and secure!\"><img alt=\"\" border=\"0\" src=\"https://www.paypal.com/en_AU/i/scr/pixel.gif\" width=\"1\" height=\"1\"></form></P></LI><LI><P ALIGN=\"JUSTIFY\">make a funds transfer from your bank using a mechanism such asSWIFT. Email<a href=\"mailto://Jim.Breen@infotech.monash.edu.au\">Jim</a>for the University's banking details.</P></LI></UL><P ALIGN=\"JUSTIFY\">NB: No contract or agreement needs to be signed in order to use the files.By using the files, the user implicitly undertakes to abide by theconditions of this licence.</P><P ALIGN=\"JUSTIFY\"><B>4. Warranty and Liability</B></P><P ALIGN=\"JUSTIFY\">While every effort has been made to ensure the accuracy of the informationin the files, it is possible that errors may still be included. The filesare made available without any warranty whatsoever as to their accuracy orsuitability for a particular application.</P><P ALIGN=\"JUSTIFY\">Any individual or organization making use of the files must agree:</P><P ALIGN=\"JUSTIFY\"></P><OL type=\"a\"><P ALIGN=\"JUSTIFY\"></P><LI><P ALIGN=\"JUSTIFY\">to assume all liability for the use or misuse of the files, and mustagree not to hold the Group liable for any actions orevents resulting from use of the files.</P><P ALIGN=\"JUSTIFY\"></P></LI><LI><P ALIGN=\"JUSTIFY\">to  refrain from bringing action or suit or claim againstthe Group or any of the Group's members on the basis of the use ofthe files, or any information included in the files.</P><P ALIGN=\"JUSTIFY\"></P></LI><LI><P ALIGN=\"JUSTIFY\">to indemnify the Group or its members in the case of action by athird party on the basis of the use of the files, or anyinformation included in the files.</P><P ALIGN=\"JUSTIFY\"></P></LI></OL><P ALIGN=\"JUSTIFY\"><B>5. Copyright</B></P><P ALIGN=\"JUSTIFY\">Every effort has been made in the compilation of these files to ensurethat the copyright of other compilers of dictionaries and lexicographicmaterialhas not been infringed. The Group asserts its intention to rectify immediatelyany breach of copyright brought to its attention.</P><P ALIGN=\"JUSTIFY\">Any individual or organization in possession of copies of the files, upon becomingaware that a possible copyright infringement may be present in the files,must undertake to contact the Group immediately with details of thepossible infringement.</P><P ALIGN=\"JUSTIFY\"><B>6. Prior Permission</B></P><P ALIGN=\"JUSTIFY\">All permissions for use of the files granted by James William Breenprior to March 2000 will be honoured and maintained, however the placingof the KANJD212 and EDICTH files under the GNU GPL has been withdrawn asof 25 March 2000.</P><P ALIGN=\"JUSTIFY\"><B>7. Special Conditions for the KANJIDIC, KANJD212 and KANJIDIC2 Files</B></P><P ALIGN=\"JUSTIFY\">In addition to licensing arrangements described above, the followingadditional conditions apply to the KANJIDIC, KANJD212 and KANJIDIC2 files.</P><P ALIGN=\"JUSTIFY\">The following people have granted permission for material for which they holdcopyright to be included in the files, and distributed under the aboveconditions, while retaining their copyright over that material:</P><P ALIGN=\"JUSTIFY\">Jack HALPERN: The SKIP codes.</P><P ALIGN=\"JUSTIFY\">Note that the SKIP codes are under their own similar Creative Commonlicence. See Jack Halpern's <a HREF=\"http://www.kanji.org/kanji/dictionaries/skip_permission.htm\">conditions of use </a>page. Note that commercial applications using the SKIP codes musthave prior permission from Jack Halpern.</P><P ALIGN=\"JUSTIFY\">Christian WITTERN and Koichi YASUOKA: The Pinyin information.</P><P ALIGN=\"JUSTIFY\">Urs APP: the Four Corner codes and the Morohashi information.</P><P ALIGN=\"JUSTIFY\">Mark SPAHN and Wolfgang HADAMITZKY: the kanji descriptors from theirdictionary.</P><P ALIGN=\"JUSTIFY\">Charles MULLER: the Korean readings.</P><P ALIGN=\"JUSTIFY\">Joseph DE ROO: the De Roo codes.</P><P ALIGN=\"JUSTIFY\"></P></OL><B>8. Enquiries</B><P ALIGN=\"JUSTIFY\">All enquiries to:</P><P ALIGN=\"JUSTIFY\">The Electronic Dictionary Research and Development Group<BR>(Attn: Assoc. Prof. Jim Breen)<BR>Clayton School of Information Technology<BR>Monash University<BR>CLAYTON VIC 3168<BR>AUSTRALIA<BR>";

		String version = "1.21beta";

		return new KanjiDatabaseInfo(name, inicio, inicio, copyrights, licensesNamed.toString(), licensecontent, version);
	}

	public static void main(String... args) {

		KanjiODBBuilder kb = new KanjiODBBuilder();
		try {
			kb.construct("resources/data/kanjidic23.xml", "resources/data/strokedata.txt", "resources/data/radicals", "resources/data/kanjidict.jdk");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// List<AllowedStrokeLineEnum> asl = new
		// ArrayList<AllowedStrokeLineEnum>();
		// asl.add(AllowedStrokeLineEnum.B);
		// asl.add(AllowedStrokeLineEnum.B);
		// asl.add(AllowedStrokeLineEnum.F);
		// asl.add(AllowedStrokeLineEnum.A);
		// asl.add(AllowedStrokeLineEnum.A);
		// asl.add(AllowedStrokeLineEnum.C);
		// asl.add(AllowedStrokeLineEnum.B);
		// asl.add(AllowedStrokeLineEnum.A);
		// asl.add(AllowedStrokeLineEnum.F);
		// asl.add(AllowedStrokeLineEnum.F);
		// asl.add(AllowedStrokeLineEnum.B);
		// asl.add(AllowedStrokeLineEnum.A);
		// asl.add(AllowedStrokeLineEnum.C);
		// System.out.println((char)
		// kb.strokeTree.search(asl).get(0).intValue());
	}

}
