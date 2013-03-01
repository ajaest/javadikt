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

package net.ajaest.jdk.data.dict;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.ajaest.jdk.data.auxi.KanjiComparator;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiDicsEnum;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiReadingTypesEnum;
import net.ajaest.jdk.data.dict.auxi.KanjiDatabaseInfo;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.kanjiFields.ClassicNelsonQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ClassicRadicalQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DeRooQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DicNameQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.FourCornerQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.FrequencyQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.GradeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JISCharsetQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JISCodeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.KanjiGraphQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.MeaningLanguageQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.MeaningQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ReadingTypeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.SkipQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.SpahnHadamitzkyQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.StrokeCountQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.UnicodeValueQAbout;
import net.ajaest.jdk.data.dict.sort.KanjiOrdering;
import net.ajaest.jdk.data.dict.sort.KanjiSortExpression;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

/**
 * Class that provides methods to query and retrieve information about kanjis.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiDict {

	private TreeDatabaseManager tree;
	private List<KanjiTag> previousSearch = null;

	/**
	 * 
	 * @param kanjiDatabase
	 *            a {@code File} class representing a database file
	 * @param trees
	 *            a {@code File} class representing a packaged tree file
	 * @throws IOException
	 */
	public KanjiDict(File kanjiDatabase, File trees) throws IOException {

		this(kanjiDatabase.getPath(), trees.getPath());
	}

	/**
	 * 
	 * @param kanjiDatabasePath
	 *            the kanji database path string
	 * @param treesPath
	 *            packaged tree file path string
	 * @throws IOException
	 */
	public KanjiDict(String kanjiDatabasePath, String treesPath) throws IOException {

		tree = new TreeDatabaseManager(treesPath, kanjiDatabasePath);
	}

	public TreeDatabaseManager getKanjiDatabaseManager() {
		return tree;
	}

	/**
	 * Returns the kanji specified by it's unicode value.
	 * 
	 * @param unicodeValue
	 *            The unicode value of the queried kanji
	 * @return the kanji which unicode value is the specified in {@code
	 *         unicodeValue}, or null if the unicode value does not represents a
	 *         kanji
	 */
	public KanjiTag getKanjiByUnicode(Integer unicodeValue) {

		KanjiTag kt = tree.getKanji(unicodeValue);

		if (kt == null && Kanji.isKanji(unicodeValue))
			// if kanji is not in the database, returns void kanji
			kt = new Kanji(unicodeValue);

		return kt;
	}

	/**
	 * Returns a set of kanji from database that matches all the conditions
	 * defined in the specified kanji expression.
	 * 
	 * @param ke
	 *            a kanji expression defining a kanji or a group of kanjis
	 * @return a list of kanji unicode values wich kanjis matches all the
	 *         conditions specified in {code ke} kanji expression
	 */
	public List<Integer> executeRefQuery(KanjiExpression ke) {

		return new LinkedList<Integer>(tree.executeRefQuery(ke));
	}

	/**
	 * Returns a set of kanji from database that matches all the conditions
	 * defined in the specified kanji expression.
	 * 
	 * @param ke
	 *            a kanji expression defining a kanji or a group of kanjis
	 * @return a set of kanjis that matches all the conditions specified in
	 *         {code ke} kanji expression
	 */
	public List<KanjiTag> executeQuery(KanjiExpression ke) {

		previousSearch = getSortedKanjis(ke);
		return previousSearch;
	}

	/**
	 * Returns a set of kanji from database that matches all the conditions
	 * defined in the specified kanji expression.
	 * 
	 * @param ke
	 *            a kanji expression defining a kanji or a group of kanjis
	 * @return a set of kanjis that matches all the conditions specified in
	 *         {code ke} kanji expression
	 */
	public List<KanjiTag> executeQuery(KanjiExpression ke, KanjiSortExpression kse) {

		previousSearch = sortKanjis(new LinkedList<KanjiTag>(tree.executeQuery(ke)), kse);
		return previousSearch;
	}

	/**
	 * Returns a set of kanji from the previous query that matches all the
	 * conditions defined in the specified kanji expression. This method should
	 * be faster than {@code executeQuery}.
	 * 
	 * @param ke
	 *            a kanji expression defining a kanji or a group of kanjis
	 * @return a set of kanjis that matches all the conditions specified in
	 *         {code ke} kanji expression
	 */
	public List<KanjiTag> executeFromPrevious(KanjiExpression ke) {

		// TODO: implement from memory, not using database. It must be quick!
		if (previousSearch == null)
			previousSearch = getSortedKanjis(ke);
		else
			previousSearch.retainAll(getSortedKanjis(ke));

		return sortKanjis(previousSearch, getSorterFromExpression(ke));
	}

	private List<KanjiTag> getSortedKanjis(KanjiExpression ke) {


		return sortKanjis(new LinkedList<KanjiTag>(tree.executeQuery(ke)), getSorterFromExpression(ke));
	}

	/**
	 * 
	 * @return the previous result of the last call to {@code executeQuery} or
	 *         {@code executeQueryFromPrevious} methods, null if those methods
	 *         has never been called in this object.
	 */
	public List<KanjiTag> getPreviousSearch() {
		return previousSearch;
	}

	public KanjiDatabaseInfo getDatabaseInfo() {
		return tree.getKanjiDatabaseInformation();
	}

	/**
	 * Returns a specified set of kanjis sorted by defined a {@code
	 * KanjiSortExpression}
	 * 
	 * @param kanjis
	 *            Kanji set to be sorted
	 * @param kse
	 *            Expression of ordering criteria
	 * @return a sorted set of kanjis
	 */
	public List<KanjiTag> sortKanjis(List<KanjiTag> kanjis, KanjiSortExpression kse) {

		KanjiComparator kc = new KanjiComparator(kse);
		
		Collections.sort(kanjis, kc);

		return kanjis;
	}

	/**
	 * Returns a specified set of kanjis sorted by defined a {@code
	 * KanjiSortExpression}
	 * 
	 * @param refs
	 *            Kanji refs to be sorted
	 * @param kse
	 *            Expression of ordering criteria
	 */
	public void sortKanjiRefs(List<Integer> refs, KanjiSortExpression kse) {

		tree.sortRefs(kse, refs);
	}

	/**
	 * 
	 * Creates a {@code KanjiSortExpression} that describes an ordering where
	 * the elements are arranged firstly by the first domain of the kanji
	 * expression. For example: <br>
	 * <br> {@code new
	 * KanjiQuery().unicode_Value().less_Than("2f2f").and().Skip_code
	 * ().greather_than(5)} <br>
	 * <br>
	 * Will return the equivalent of the following kanji ordering expression <br>
	 * <br> {@code new
	 * KanjiQuery().sort_by_unicode().from_lower_to_greather().and_if_equals
	 * ().sort_by_Skip_code().alphabetically()}
	 * 
	 * @param ke
	 *            a well-formed {@code KanjiExpression}
	 * @return the default kanji sorter of the kanji expression
	 */
	public KanjiSortExpression getSorterFromExpression(KanjiExpression ke) {

		KanjiSortExpression kse = null;

		ISO639ー1 auxLang = ISO639ー1.EN;
		// First we must detect if exists any language statement
		for (QAbout vqa : ke.getDomains()) {
			if (vqa.getClass().equals(MeaningLanguageQAbout.class)) {
				MeaningLanguageQAbout mlqa = (MeaningLanguageQAbout) vqa;
				auxLang = ISO639ー1.valueOf(mlqa.getValue().toUpperCase());
			}
		}

		for (QAbout vqa : ke.getDomains()) {

			Class<?> vqaClass = vqa.getClass();

			if (vqaClass.equals(KanjiExpression.class)) {
				if (kse == null)
					kse = getSorterFromExpression((KanjiExpression) vqa);
				// TODO:
			}
			if (vqaClass.equals(UnicodeValueQAbout.class)) {
				if(kse ==null)
					kse = new KanjiOrdering().sort_by_Unicode().from_least_to_greatest();
				else
					kse = kse.and_if_equals().sort_by_Unicode().from_least_to_greatest();
			} else if(vqaClass.equals(JISCharsetQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_JIS_charset().alphabetically();
				else
					kse = kse.and_if_equals().sort_by_JIS_charset().alphabetically();		
			} else if(vqaClass.equals(JISCodeQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_JIS_code().alphabetically();
				else
					kse = kse.and_if_equals().sort_by_JIS_code().alphabetically();		
			} else if(vqaClass.equals(ClassicNelsonQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_Nelson_radical().from_least_to_greatest();
				else
					kse = kse.and_if_equals().sort_by_Nelson_radical().from_least_to_greatest();		
			} else if(vqaClass.equals(ClassicRadicalQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_classic_radical().from_least_to_greatest();
				else
					kse = kse.and_if_equals().sort_by_classic_radical().from_least_to_greatest();		
			} else if(vqaClass.equals(GradeQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_grade().from_least_to_greatest();
				else
					kse = kse.and_if_equals().sort_by_grade().from_least_to_greatest();		
			} else if(vqaClass.equals(StrokeCountQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_stroke_count().from_least_to_greatest();
				else
					kse = kse.and_if_equals().sort_by_stroke_count().from_least_to_greatest();		
			} else if(vqaClass.equals(FrequencyQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_frequency().from_least_to_greatest();
				else
					kse = kse.and_if_equals().sort_by_frequency().from_least_to_greatest();		
			} else if(vqaClass.equals(SkipQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_SKIP_code().alphabetically();
				else
					kse = kse.and_if_equals().sort_by_SKIP_code().alphabetically();		
			} else if(vqaClass.equals(SpahnHadamitzkyQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_SpahnーHadamitzky_code().alphabetically();
				else
					kse = kse.and_if_equals().sort_by_SpahnーHadamitzky_code().alphabetically();		
			} else if(vqaClass.equals(FourCornerQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_four_corner_code().alphabetically();
				else
					kse = kse.and_if_equals().sort_by_four_corner_code().alphabetically();		
			} else if(vqaClass.equals(DeRooQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_De_Roo_code().alphabetically();
				else
					kse = kse.and_if_equals().sort_by_De_Roo_code().alphabetically();		
			} else if(vqaClass.equals(KanjiGraphQAbout.class)){
				if(kse == null)
					kse = new KanjiOrdering().sort_by_graph().in_similar_order(((KanjiGraphQAbout) vqa).getValue());
				else
					kse = kse.and_if_equals().sort_by_graph().in_similar_order(((KanjiGraphQAbout) vqa).getValue());
			} else if (vqaClass.equals(DicNameQAbout.class)) {
				DicNameQAbout dnqa = (DicNameQAbout) vqa;
				KanjiDicsEnum dic = KanjiDicsEnum.valueOf("DIC_" + dnqa.getValue().toString());

				if (kse == null)
					kse = new KanjiOrdering().sort_by_dictionary_reference().dic_enum_equals(dic).from_least_to_greatest();
				else
					kse = kse.and_if_equals().sort_by_dictionary_reference().dic_enum_equals(dic).from_least_to_greatest();
			} else if (vqaClass.equals(MeaningQAbout.class)) {
				if (kse == null)
					kse = new KanjiOrdering().sort_by_first_meaning().languaje_enum_equals(auxLang).alphabetically();
				else
					kse = kse.and_if_equals().sort_by_first_meaning().languaje_enum_equals(auxLang).alphabetically();
			} else if (vqaClass.equals(ReadingTypeQAbout.class)) {
				ReadingTypeQAbout rtqa = (ReadingTypeQAbout) vqa;
				KanjiReadingTypesEnum kre = KanjiReadingTypesEnum.valueOf("READ_" + rtqa.getValue().toUpperCase());

				if (kse == null)
					kse = new KanjiOrdering().sort_by_first_reading().reading_enum_equals(kre).alphabetically();
				else
					kse = kse.and_if_equals().sort_by_first_reading().reading_enum_equals(kre).alphabetically();
			}
		}

		if (kse == null)
			kse = new KanjiOrdering().sort_by_frequency().from_least_to_greatest();
		else
			kse.and_if_equals().sort_by_frequency().from_least_to_greatest();

		return kse;
	}

	public void close() {

		tree.close();
	}

}
