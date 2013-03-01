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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.ajaest.jdk.data.auxi.KanjiReference;
import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.dict.auxi.DatabaseManager;
import net.ajaest.jdk.data.dict.auxi.KanjiDatabaseInfo;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.query.IntegerValueQAbout;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.StringValueQAbout;
import net.ajaest.jdk.data.dict.query.ValueQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ClassicNelsonQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ClassicRadicalQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.CommonStrokeMiscountQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DeRooQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DicNameQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.DicReferenceQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.FourCornerQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.FrequencyQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.GradeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JISCharsetQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JISCodeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.JLPTLevelQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.KanjiGraphQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.MeaningLanguageQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.MeaningQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ReadingQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.ReadingTypeQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.SkipQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.SpahnHadamitzkyQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.StrokeCountQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.UnicodeValueQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.VariantReferenceQAbout;
import net.ajaest.jdk.data.dict.query.kanjiFields.VariantTypeQAbout;
import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.RadicalVariantTag;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.jdk.data.kanji.VariantPair;
import net.ajaest.jdk.tools.parsers.StrokeOrderParser;
import net.ajaest.lib.data.SequenceTree;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

import org.neodatis.odb.CorruptedDatabaseException;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.query.criteria.And;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Or;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

/**
 * This class provides methods to manage a kanji database.
 * 
 * @author Luis Alfonso Arce González
 * @deprecated It was substituted by {@link TreeDatabaseManager}. Backward
 *             compatibility with .jdk files which version is higher than 1.24
 *             is not guaranteed
 */
// I've created a monster!
@Deprecated
public class NeodatisKanjiDatabaseManager implements DatabaseManager<KanjiTag, KanjiExpression> {

	private final ODB kanjiDb;
	private final KanjiDatabaseInfo kdbi;
	private SequenceTree<AllowedStrokeLineEnum, KanjiGraph> graphTree;
	private Map<Integer, SortedSet<RadicalVariantTag>> radicals;

	private static final Map<Class<?>, String> fieldNamesTranslations = new HashMap<Class<?>, String>();
	static {
		// Object with a unique instance per kanji, query from nested value in
		// kanji
		fieldNamesTranslations.put(UnicodeValueQAbout.class, "unicode");

		fieldNamesTranslations.put(JISCharsetQAbout.class, "jisCode.first");
		fieldNamesTranslations.put(JISCodeQAbout.class, "jisCode.second");

		fieldNamesTranslations.put(ClassicRadicalQAbout.class, "classicRadical");

		fieldNamesTranslations.put(ClassicNelsonQAbout.class, "nelsonRadical");

		fieldNamesTranslations.put(GradeQAbout.class, "grade");

		fieldNamesTranslations.put(StrokeCountQAbout.class, "strokeCount");

		fieldNamesTranslations.put(CommonStrokeMiscountQAbout.class, "strokeMiscounts");

		fieldNamesTranslations.put(FrequencyQAbout.class, "frequency");

		fieldNamesTranslations.put(JLPTLevelQAbout.class, "JLPTLevel");

		fieldNamesTranslations.put(SkipQAbout.class, "queryCodes.skip");
		fieldNamesTranslations.put(SpahnHadamitzkyQAbout.class, "queryCodes.spahnHadamitzky");
		fieldNamesTranslations.put(DeRooQAbout.class, "queryCodes.deRoo");
		fieldNamesTranslations.put(FourCornerQAbout.class, "queryCodes.fourCorner");

		// List value object, query from StringPair/DicEntry specific inherited
		// class

		fieldNamesTranslations.put(VariantReferenceQAbout.class, "second");
		fieldNamesTranslations.put(VariantTypeQAbout.class, "first");

		fieldNamesTranslations.put(DicNameQAbout.class, "first");
		fieldNamesTranslations.put(DicReferenceQAbout.class, "numRef");

		fieldNamesTranslations.put(ReadingTypeQAbout.class, "key");
		fieldNamesTranslations.put(ReadingQAbout.class, "elements");

		fieldNamesTranslations.put(MeaningLanguageQAbout.class, "key");
		fieldNamesTranslations.put(MeaningQAbout.class, "elements");;
	}

	/**
	 * Constructs a kanji database manager interface and connects it to the
	 * kanji database in {@code path}
	 * 
	 * @param kanjiDatabase
	 *            a {@code File} class representing a database file
	 * @param strokeOrder
	 *            a {@code File} class representing a stroke order file
	 * @throws IOException
	 */
	public NeodatisKanjiDatabaseManager(File kanjiDatabase, File strokeOrder) throws IOException {
		this(kanjiDatabase.getAbsolutePath(), strokeOrder.getAbsolutePath());
	}

	/**
	 * Constructs a kanji database manager interface and connects it to the
	 * kanji database in {@code path}
	 * 
	 * @param kanjiDatabasePath
	 *            Kanji database path string
	 * @param strokeOrderPath
	 *            stroke order file path string
	 * @throws IOException
	 */
	public NeodatisKanjiDatabaseManager(String kanjiDatabasePath, String strokeOrderPath) throws IOException {

		this.kanjiDb = ODBFactory.open(kanjiDatabasePath);

		Objects<KanjiDatabaseInfo> kdbi = this.kanjiDb.getObjects(KanjiDatabaseInfo.class);

		this.buildGraphTree(strokeOrderPath);

		this.buildRadicalMap();

		if (kdbi.size() != 1)
			throw new CorruptedDatabaseException(NeoDatisError.UNEXPECTED_SITUATION, "There are multiples data headers or does not exists anyone.");

		this.kdbi = kdbi.getFirst();
	}

	private void buildGraphTree(String path) throws IOException {

		StrokeOrderParser sop = new StrokeOrderParser(path, false);
		sop.parse();
		sop.close();

		this.graphTree = new SequenceTree<AllowedStrokeLineEnum, KanjiGraph>(false);

		for (Integer i : sop.getRetrieved().keySet()) {
			this.graphTree.add(this.getLineSequence(sop.getRetrieved().get(i)), sop.getRetrieved().get(i));
		}

		for (Integer i : sop.getRepeated().keySet()) {
			this.graphTree.add(this.getLineSequence(sop.getRepeated().get(i)), sop.getRetrieved().get(i));
		}

	}

	private void buildRadicalMap() {
		this.radicals = new HashMap<Integer, SortedSet<RadicalVariantTag>>();

		Objects<RadicalVariantTag> retRadicals = this.kanjiDb.getObjects(RadicalVariantTag.class);

		SortedSet<RadicalVariantTag> temp;
		for (RadicalVariantTag r : retRadicals) {
			temp = this.radicals.get(r.getNumber());
			if (temp == null) {
				temp = new TreeSet<RadicalVariantTag>();
				this.radicals.put(r.getNumber(), temp);
			}
			temp.add(r);
		}
	}

	/**
	 * Closes the connection between the database manager interface and the
	 * kanji database.
	 */
	@Override
	public void close() {
		this.kanjiDb.close();
		this.graphTree = null;
	}

	private Set<KanjiTag> executeNeodatisQuery(Class<?> c, And and, Set<KanjiTag> previousQuery) {

		And quickAnd = this.nonKanjiQueryCriteriaShortener(previousQuery, and);
		CriteriaQuery tempQuery = new CriteriaQuery(c, quickAnd);

		Set<KanjiReference> partialResult = this.executeNeodatisQuery(tempQuery);

		Set<KanjiTag> result = partialResult.size() > 0 ? this.getKanjis(partialResult) : new HashSet<KanjiTag>();

		return result;
	}

	private <E> Set<E> executeNeodatisQuery(CriteriaQuery qc) {

		Objects<E> obs = this.kanjiDb.getObjects(qc);

		return new HashSet<E>(obs);
	}

	/**
	 * Executes a kanji expression representing a kanji query in order to
	 * retrieve the kanjis in database that fulfills the specified conditions.
	 * 
	 * @param query
	 *            The {@code KanjiExpression} representing a kanji query.
	 * @return A set of kanjis as a result of executing the query.
	 */
	@Override
	public Set<KanjiTag> executeQuery(KanjiExpression query) {

		KanjiExpression optimized = this.optimizeQuery(query);

		// Object with a unique instance per kanji, query from nested value in
		// kanji
		List<And> aboutKanji = new ArrayList<And>();
		// List value object, query from StringPair/DicEntry specific inherited
		// class
		List<And> aboutDicReference = new ArrayList<And>();
		List<And> aboutReadings = new ArrayList<And>();
		List<And> aboutMeaning = new ArrayList<And>();
		List<And> aboutVariant = new ArrayList<And>();
		List<List<KanjiGraph>> aboutGraph = new ArrayList<List<KanjiGraph>>();
		List<List<KanjiExpression>> aboutQueryIntoQuery = new ArrayList<List<KanjiExpression>>();

		int size = optimized.size();

		for (int i = 0; i < size; i++) {

			switch (optimized.getBooleanCases().get(i)) {
			case AND :
				this.translateCriteria(optimized.getDomains().get(i), aboutKanji, aboutDicReference, aboutReadings, aboutMeaning, aboutVariant, aboutGraph, aboutQueryIntoQuery);
				break;
			case OR :
			case FIRST :

				aboutKanji.add(Where.and());
				aboutDicReference.add(Where.and());
				aboutReadings.add(Where.and());
				aboutMeaning.add(Where.and());
				aboutVariant.add(Where.and());
				aboutGraph.add(new ArrayList<KanjiGraph>());
				aboutQueryIntoQuery.add(new ArrayList<KanjiExpression>());

				this.translateCriteria(optimized.getDomains().get(i), aboutKanji, aboutDicReference, aboutReadings, aboutMeaning, aboutVariant, aboutGraph, aboutQueryIntoQuery);
				break;

			default :
				throw new RuntimeException("Boolean operator not supported.");
			}
		}

		return this.executeQuery(aboutKanji, aboutDicReference, aboutReadings, aboutMeaning, aboutVariant, aboutGraph, aboutQueryIntoQuery);
	}

	private Set<KanjiTag> executeQuery(List<And> aboutKanji, List<And> aboutDicReference, List<And> aboutReadings, List<And> aboutMeaning, List<And> aboutVariant, List<List<KanjiGraph>> aboutGraph, List<List<KanjiExpression>> aboutQueryIntoQuery) {

		// partial current and results
		Set<KanjiTag> andResult = null;
		//partial current graph result
		Set<KanjiReference> graphResult = null;
		//final or result
		Set<KanjiTag> orResult = new HashSet<KanjiTag>();
		// stores non kanji results


		CriteriaQuery tempQuery = null;
		// all lists must have the same size
		int size = aboutDicReference.size();
		boolean execute = size>0;

		// A lot of improvements could be done here. For now, we only want it to
		// work.
		for (int i = 0; i < size /* && execute */; i++) {

			if (!aboutKanji.get(i).isEmpty() && execute) {
				tempQuery = new CriteriaQuery(Kanji.class, aboutKanji.get(i));
				andResult = this.executeNeodatisQuery(tempQuery);
				execute = andResult.size() > 0;
			}

			if (!aboutDicReference.get(i).isEmpty() && execute) {
				andResult = this.executeNeodatisQuery(DicReferencePair.class, aboutDicReference.get(i), andResult);
				execute = andResult.size() > 0;
			}

			if (!aboutReadings.get(i).isEmpty() && execute) {
				andResult = this.executeNeodatisQuery(ReadingEntry.class, aboutReadings.get(i), andResult);
				execute = andResult.size() > 0;
			}

			if (!aboutMeaning.get(i).isEmpty() && execute) {
				andResult = this.executeNeodatisQuery(MeaningEntry.class, aboutMeaning.get(i), andResult);
				execute = andResult.size() > 0;
			}

			if (!aboutVariant.get(i).isEmpty() && execute) {
				andResult = this.executeNeodatisQuery(VariantPair.class, aboutVariant.get(i), andResult);
				execute = andResult.size() > 0;
			}

			if (!aboutGraph.get(i).isEmpty() && execute) {

				List<AllowedStrokeLineEnum> tempAsl = null;
				for (int j = 0; (j < aboutGraph.get(i).size()) && execute; j++) {
					KanjiGraph kg = aboutGraph.get(i).get(j);
					tempAsl = this.getLineSequence(kg);
					graphResult = new HashSet<KanjiReference>(this.graphTree.search(tempAsl));

					if (andResult == null)
						andResult = this.getKanjis(graphResult);
					else
						andResult.retainAll(this.getKanjis(graphResult));
					execute = andResult.size() > 0;
				}
			}

			if (!aboutQueryIntoQuery.get(i).isEmpty() && execute) {

				for (int j = 0; (j < aboutQueryIntoQuery.get(i).size()) && execute; j++) {
					KanjiExpression ke = aboutQueryIntoQuery.get(i).get(j);

					if (andResult == null)
						andResult = this.executeQuery(ke);
					else
						andResult.retainAll(this.executeQuery(ke));

					execute = andResult.size() > 0;
				}
			}

			orResult.addAll(andResult);
			andResult = null;
			// TODO: remove
			execute = true;
			// --
		}



		return orResult;
	}

	@Override
	protected void finalize() throws Throwable {
		this.kanjiDb.close();
		super.finalize();
	}

	/**
	 * Returns the current kanji database information object.
	 * 
	 * @return a {@code KanjiDatabaseInformation} object.
	 */
	public KanjiDatabaseInfo getKanjiDatabaseInformation() {
		return this.kdbi;
	}

	private Set<KanjiTag> getKanjis(Set<KanjiReference> kr) {

		Or or = Where.or();

		for (KanjiReference obj : kr) {
			or.add(Where.equal("unicode", obj.getUnicodeRef()));
		}

		Objects<KanjiTag> obs = this.kanjiDb.getObjects(new CriteriaQuery(Kanji.class, or));

		return new HashSet<KanjiTag>(obs);

	}

	private List<AllowedStrokeLineEnum> getLineSequence(KanjiGraph kg) {

		List<AllowedStrokeLineEnum> sequence = new ArrayList<AllowedStrokeLineEnum>();

		for (KanjiStroke ks : kg.getStrokes()) {
			for (AllowedStrokeLineEnum asl : ks.getStrokeList()) {

				sequence.add(asl);
			}
		}

		return sequence;
	}

	public ODB getLowLevelDatabaseManager() {
		return this.kanjiDb;
	}

	public SortedSet<RadicalVariantTag> getRadicalInfos(Integer radNumber) {

		return this.radicals.get(radNumber);
	}

	/**
	 * Returns the current kanji database stroke order sequence tree.
	 * 
	 * @return the kanji stroke order tree
	 */
	public SequenceTree<AllowedStrokeLineEnum, KanjiGraph> getStrokeTree() {
		return this.graphTree;
	}

	private And nonKanjiQueryCriteriaShortener(Set<KanjiTag> previousQuery, And and) {

		Or or = null;
		And ret = null;

		if ((previousQuery == null) || (previousQuery.size() == 0)) {
			ret = and;

		} else {
			or = Where.or();
			ret = Where.and();
			// makes retain all operation form previous query using unicode
			// references
			for (KanjiTag kt : previousQuery)
				or.add(Where.equal("unicodeRef", kt.getUnicodeRef()));

			ret.add(or);
			ret.add(and);
		}

		return ret;
	}

	private KanjiExpression optimizeQuery(KanjiExpression ke) {

		// TODO:Dummy, implement. I need performance tests before going on it.
		return ke;
	}

	private ICriterion translateCaseToNeodatis(IntegerValueQAbout qa) {

		switch (qa.getQueryCase()) {
		case EQUALS :
			return Where.equal(this.translateFieldName(qa), qa.getValue());
		case GREATER_OR_EQUALS_THAN :
			return Where.ge(this.translateFieldName(qa), qa.getValue());
		case GREATER_THAN :
			return Where.gt(this.translateFieldName(qa), qa.getValue());
		case LESS_OR_EQUALS_THAN :
			return Where.le(this.translateFieldName(qa), qa.getValue());
		case LESS_THAN :
			return Where.lt(this.translateFieldName(qa), qa.getValue());
		case NOT_EQUALS :
			return Where.not(Where.equal(this.translateFieldName(qa), qa.getValue()));
		case NULL :
			return Where.isNull(this.translateFieldName(qa));

		default :
			throw new IllegalArgumentException("Query case not suportted");
		}
	}

	private ICriterion translateCaseToNeodatis(StringValueQAbout qa) {

		switch (qa.getQueryCase()) {
		case EQUALS :
			return Where.equal(this.translateFieldName(qa), qa.getValue());
		case GREATER_OR_EQUALS_THAN :
			return Where.ge(this.translateFieldName(qa), qa.getValue());
		case GREATER_THAN :
			return Where.gt(this.translateFieldName(qa), qa.getValue());
		case LESS_OR_EQUALS_THAN :
			return Where.le(this.translateFieldName(qa), qa.getValue());
		case LESS_THAN :
			return Where.lt(this.translateFieldName(qa), qa.getValue());
		case NOT_EQUALS :
			return Where.not(Where.equal(this.translateFieldName(qa), qa.getValue()));
		case NULL :
			return Where.isNull(this.translateFieldName(qa));

		default :
			throw new IllegalArgumentException("Query case not suportted");
		}
	}

	private void translateCriteria(QAbout qa, List<And> aboutKanji, List<And> aboutDicReference, List<And> aboutReadings, List<And> aboutMeaning, List<And> aboutVariant, List<List<KanjiGraph>> aboutGraph, List<List<KanjiExpression>> aboutQueryIntoQuery) {
		// TODO: repair
		int last = aboutGraph.size() - 1;// all list must have the same size

		// query about Kanji class/kanji nested class, IntegerValueQAbout, not
		// list
		if ((qa instanceof ClassicNelsonQAbout) || (qa instanceof ClassicRadicalQAbout) || (qa instanceof FrequencyQAbout) || (qa instanceof GradeQAbout) || (qa instanceof JLPTLevelQAbout) || (qa instanceof StrokeCountQAbout) || (qa instanceof UnicodeValueQAbout)) {

			aboutKanji.get(last).add(this.translateCaseToNeodatis((IntegerValueQAbout) qa));

		}
		// query about Kanji class/kanji nested class, StringValueQAbout, not
		// list
		else if ((qa instanceof DeRooQAbout) || (qa instanceof FourCornerQAbout) || (qa instanceof SkipQAbout) || (qa instanceof SpahnHadamitzkyQAbout)) {

			aboutKanji.get(last).add(this.translateCaseToNeodatis((StringValueQAbout) qa));

		}
		// query about Kanji class/kanji nested class, StringValueQAbout, not
		// list
		else if ((qa instanceof JISCharsetQAbout) || (qa instanceof JISCodeQAbout)) {

			aboutKanji.get(last).add(this.translateCaseToNeodatis((StringValueQAbout) qa));

		}
		// query about Kanji class, IntegerValueQAbout, list
		else if (qa instanceof CommonStrokeMiscountQAbout) {

			aboutKanji.get(last).add(Where.contain(this.translateFieldName((IntegerValueQAbout) qa), ((IntegerValueQAbout) qa).getValue()));
			// TODO: WARNING: this means that always a case about
			// miscounts will be "equals"
		}
		// Query about graph
		else if (qa instanceof KanjiGraphQAbout) {
			aboutGraph.get(last).add(((KanjiGraphQAbout) qa).getValue());
		}
		// query about StringEntry classes, meaning
		else if (qa instanceof MeaningLanguageQAbout) {

			aboutMeaning.get(last).add(this.translateCaseToNeodatis((StringValueQAbout) qa));
		}
		// query about StringEntry classes, meaning entry
		else if (qa instanceof MeaningQAbout) {

			aboutMeaning.get(last).add(Where.contain(this.translateFieldName((StringValueQAbout) qa), ((StringValueQAbout) qa).getValue()));
			// TODO: WARNING: this means that always a case about
			// miscounts will be "equals"
		}
		// query about StringEntry classes, reading key
		else if (qa instanceof ReadingQAbout) {

			aboutReadings.get(last).add(Where.contain(this.translateFieldName((StringValueQAbout) qa), ((StringValueQAbout) qa).getValue()));
			// TODO: WARNING: this means that always a case about
			// miscounts will be "equals"

		}
		// query about StringEntry classes, reading entry
		else if (qa instanceof ReadingTypeQAbout) {

			aboutReadings.get(last).add(this.translateCaseToNeodatis((StringValueQAbout) qa));

		}
		// query about StringPair classes, variant
		else if ((qa instanceof VariantReferenceQAbout) || (qa instanceof VariantTypeQAbout)) {

			aboutVariant.get(last).add(this.translateCaseToNeodatis((StringValueQAbout) qa));
		}
		// query about StringPair classes, dictionary reference
		else if (qa instanceof DicNameQAbout) {

			aboutDicReference.get(last).add(this.translateCaseToNeodatis((StringValueQAbout) qa));

		}
		// query about DicReferencePair classes, dictionary reference
		else if (qa instanceof DicReferenceQAbout) {

			aboutDicReference.get(last).add(this.translateCaseToNeodatis((IntegerValueQAbout) qa));
		}
		// query into query
		else if (qa instanceof KanjiExpression) {
			aboutQueryIntoQuery.get(last).add((KanjiExpression) qa);
		}

		else {
			throw new RuntimeException("Domain not supported.");
		}
	}

	private String translateFieldName(ValueQAbout<?> qa) {

		return fieldNamesTranslations.get(qa.getClass());
	}

}
