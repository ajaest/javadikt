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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import net.ajaest.jdk.core.auxi.LastVersionChecker;
import net.ajaest.jdk.core.exporters.Exporter;
import net.ajaest.jdk.core.exporters.HTMLExporter;
import net.ajaest.jdk.core.exporters.PDFExporter;
import net.ajaest.jdk.core.exporters.PlainTextExporter;
import net.ajaest.jdk.core.main.ExceptionHandler.KNOWN_EXCEPTIONS;
import net.ajaest.jdk.data.auxi.SimilarKanjiStrokeDemuxer;
import net.ajaest.jdk.data.dict.KanjiDict;
import net.ajaest.jdk.data.dict.auxi.KanjiDatabaseInfo;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.QueryAdaptor;
import net.ajaest.jdk.data.dict.sort.KanjiOrdering;
import net.ajaest.jdk.data.dict.sort.KanjiSortExpression;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

//TODO: JavaDoc
public class JavaDiKt implements Runnable {

	// Program engine
	private LastVersionChecker lvc;
	private final JDKOptions jdkOpt;
	private final JDKGUIEngine jdkGui;
	private KanjiDict kd;
	private List<Exporter<KanjiTag>> exporters;
	private SimilarKanjiStrokeDemuxer sksdmx;

	// Program fields
	public static final String VERSION = "1.1.6 beta";
	public static final String PROJECT = "Mirai";

	private String lastVersion = null;



	public JavaDiKt(JDKOptions jdkOpt) {

		try {
			this.lvc = new LastVersionChecker(new URL("http://ajaest.net/javadikt/lastVersion.txt"));
			this.lvc.checkVersion();
		} catch (MalformedURLException e1) {
		}

		this.jdkOpt = jdkOpt;

		try {
			this.kd = new KanjiDict(jdkOpt.getKanjiFile(), jdkOpt.getTreeFile());

		} catch (FileNotFoundException e) {
			ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.INVALID_KANJI_OR_STROKE_FILE_PATH);
		} catch (IOException e) {
			// TODO:
		}

		this.exporters = new ArrayList<Exporter<KanjiTag>>();
		this.exporters.add(new PlainTextExporter(jdkOpt.getLanguage().toLowerCase()));
		this.exporters.add(new HTMLExporter(jdkOpt.getLanguage().toLowerCase()));
		this.exporters.add(new PDFExporter(jdkOpt.getLanguage().toLowerCase()));

		this.jdkGui = new JDKGUIEngine(this);

		for (Exporter<KanjiTag> exporter : this.exporters)
			exporter.setDialogParent(this.jdkGui.getMainWH().getWindow());

	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(this.jdkGui);
	}

	public List<KanjiTag> executeQuery(KanjiExpression currentKE) {

		List<KanjiTag> results;

		if(currentKE==null){
			results = new ArrayList<KanjiTag>();
			Messages.localePrintln("**JavaDiKt: WARINING - Null query");
		}
		else
			results = this.kd.executeQuery(this.adaptQuery(currentKE));

		Messages.localePrintln("**JavaDiKt: Query Executed: " + currentKE);
		return results;
	}

	public List<KanjiTag> executeQuery(KanjiExpression currentKE, KanjiSortExpression kse) {

		List<KanjiTag> results;

		if (currentKE == null) {
			results = new ArrayList<KanjiTag>();
			Messages.localePrintln("**JavaDiKt: WARINING - Null query");
		} else
			results = this.kd.executeQuery(this.adaptQuery(currentKE), kse);

		Messages.localePrintln("**JavaDiKt: Query Executed: " + currentKE);
		return results;
	}

	@SuppressWarnings("static-access")
	private KanjiExpression adaptQuery(KanjiExpression ke) {

		QueryAdaptor qa = QueryAdaptor.groupPairs().demuxReadings().demuxGraphs(this.sksdmx, 5).meaningsInLanguaje(ISO639ー1.valueOf(this.jdkOpt.getLanguage().toUpperCase()));

		if (!this.jdkOpt.getExtendedIKanjiList()) {
			qa = qa.limitToJôyô();
		}

		return qa.adapt(ke);
	}

	public List<Integer> executeRefQuery(KanjiExpression ke) {

		ke = this.adaptQuery(ke);

		List<Integer> result = this.kd.executeRefQuery(ke);
        Messages.localePrintln("**JavaDiKt: Reference Query Executed: " + ke);
		this.kd.sortKanjiRefs(result,new KanjiOrdering().sort_by_frequency().from_least_to_greatest());

		return result;
	}

	public JDKOptions getOptions() {
		return this.jdkOpt;
	}

	public void saveOptions(String lang, String kanjiDictpath, String strokeDictPath, Boolean extInfo, Boolean extKanji, Boolean romaji) {

		this.jdkOpt.setLanguage(lang);
		this.jdkOpt.setKanjiFile(new File(kanjiDictpath));
		this.jdkOpt.setTreeFile(new File(strokeDictPath));
		this.jdkOpt.setExtendedInfo(extInfo);
		this.jdkOpt.setFindInExtended(extKanji);
		this.jdkOpt.setRomanized(romaji);
		this.jdkOpt.saveCurrentConfig();
	}

	public void setExporters(List<Exporter<KanjiTag>> exporters) {

		for (Exporter<KanjiTag> exp : exporters) {
			exp.setLanguaje(this.jdkOpt.getLanguage());
		}

		this.exporters = exporters;
	}

	public List<Exporter<KanjiTag>> getExporters() {
		return this.exporters;
	}

	public JDKGUIEngine getJDKGuiEngine() {
		return this.jdkGui;
	}
	public KanjiDatabaseInfo getKanjiDatabaseInfo() {
		return this.kd.getDatabaseInfo();
	}

	public SimilarKanjiStrokeDemuxer getSimilarGraphDemuxer() {
		return this.sksdmx;
	}

	public void setSimilarStrokeDemuxer(SimilarKanjiStrokeDemuxer sksdmx) {
		this.sksdmx = sksdmx;
	}

	public boolean isLastVersion() {

		if (this.lastVersion == null)
			this.lastVersion = this.getLastVersion();

		return this.lastVersion.equals(VERSION);
	}

	public String getLastVersion() {

		if (this.lastVersion == null) {
			if (this.lvc.isChecking()) {
				// order the version checker to write the last version info in
				// an file. The file version will be read the next time the
				// program is launched
				this.lvc.setLastVersionFile(new File("config/ver.txt"));
				this.lvc.writeToFile(true);
				// reads version info from a previous session
				this.lastVersion = this.getVersionFromFile("config/ver.txt");
			} else {
				if (this.lvc.readSuccessfull())
					this.lastVersion = this.lvc.getVersion();
				else {
					// reads version info from a previous session
					this.lastVersion = this.getVersionFromFile("config/ver.txt");
				}
			}
		}

		return this.lastVersion;
	}

	private String getVersionFromFile(String path) {

		String version;

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			version = br.readLine();
			br.close();
		} catch (Exception e) {
			version = VERSION;
		}

		return version;
	}

	public KanjiTag getKanjiByRef(Integer kanjiRef) {

		System.out.println("**JavaDiKt: Retrieving kanji by unicode value: " + kanjiRef);
		return this.kd.getKanjiByUnicode(kanjiRef);
	}
}
