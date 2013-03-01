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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import net.ajaest.jdk.core.main.ExceptionHandler.KNOWN_EXCEPTIONS;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;

//TODO: JavaDoc
public class JDKOptions implements Serializable{

	private static final long serialVersionUID = -774134435227882060L;

	private File configFile = null;
	private File kanjiFile = null;
	private File treeFile = null;

	private String language = null;
	// include extended information in kanji info dialog
	private Boolean extendedInfo = null;
	// find in extended kanji list
	private Boolean findInExtended = null;
	// show kana texts in romaji
	private Boolean kanaInRomaji = null;
	
	public JDKOptions(){
		this("config" + File.separator + "JDKconfig.xml");
	}

	public JDKOptions(String path) {
		this(new File(path));
	}

	public JDKOptions(File configFile) {

		this.configFile = configFile;

		if (configFile.exists()) {
			parseConfigInfo();
		} else {
			defaultConfig();
		}

	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public File getKanjiFile() {
		return kanjiFile;
	}

	public void setKanjiFile(File kanjiFile) {
		this.kanjiFile = kanjiFile;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String languaje) {
		this.language = languaje;
	}

	public Boolean getExtendedInfo() {
		return extendedInfo;
	}

	public void setExtendedInfo(Boolean extendedInfo) {
		this.extendedInfo = extendedInfo;
	}

	public Boolean getExtendedIKanjiList() {
		return findInExtended;
	}

	public void setFindInExtended(Boolean findInExtended) {
		this.findInExtended = findInExtended;
	}

	public File getTreeFile() {
		return treeFile;
	}

	public void setTreeFile(File treeFile) {
		this.treeFile = treeFile;
	}

	public void setRomanized(Boolean romaji) {

		this.kanaInRomaji = romaji;
	}

	public Boolean getRomanized() {

		return kanaInRomaji;
	}

	private void defaultConfig() {
		Messages.localePrintln("**JDKOptions: building default config file");
		
		kanjiFile = new File("dict" + File.separator + "kanjidict.jdk");

		treeFile = new File("dict" + File.separator + "trees.zobj");

		language = System.getProperty("user.language");

		extendedInfo = true;
		findInExtended = false;
		kanaInRomaji = false;

		saveCurrentConfig();
		Messages.localePrintln("**JDKOptions: default config file built");
	}

	private void parseConfigInfo() {
		
		Messages.localePrintln("**JDKOptions: parsing config file");

		try {
			Document doc = new Builder().build(configFile);
			Element root = doc.getRootElement();
			if (root.getLocalName() != "config")
				throw new ParsingException("config");

			language = root.getFirstChildElement("lang").getValue();
			if (language == null || language == "")
				throw new ParsingException("lang");

			String kanjiFilePath = root.getFirstChildElement("kanji_file").getValue();
			if (kanjiFilePath == null || kanjiFilePath == "")
				throw new ParsingException("kanji_file");
			kanjiFile = new File(kanjiFilePath);

			String strokeFilePath = root.getFirstChildElement("tree_file").getValue();
			if (strokeFilePath == null || strokeFilePath == "")
				throw new ParsingException("tree_file");
			treeFile = new File(strokeFilePath);

			String showExtInfo = root.getFirstChildElement("show_extended_info").getValue();
			if (showExtInfo == null || showExtInfo == "")
				throw new ParsingException("show_extended_info");
			extendedInfo = new Boolean(showExtInfo);

			String kana_in_romaji = root.getFirstChildElement("kana_in_romaji").getValue();
			if (kana_in_romaji == null || kana_in_romaji == "")
				throw new ParsingException("kana_in_romaji");
			kanaInRomaji = new Boolean(kana_in_romaji);

			String findExtkanji = root.getFirstChildElement("find_extended_Kanji_list").getValue();
			if (findExtkanji == null || findExtkanji == "")
				throw new ParsingException("find_extended_Kanji_list");
			findInExtended = new Boolean(findExtkanji);

		} catch (ValidityException e) {
			ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.MALFORMED_CONFIG_FILE);
		} catch (ParsingException e) {
			ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.MALFORMED_CONFIG_FILE);
		} catch (FileNotFoundException e){
			ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.INVALID_CONFIG_FILE_PATH);
		} catch (IOException e) {
			ExceptionHandler.handleException(e);
		}

		Messages.localePrintln("**JDKOptions: config file parsed");
		Messages.localePrintln(toString());

	}

	public void saveCurrentConfig() {
		
		Messages.localePrintln("**JDKOptions: saving current config file");


		Element config = new Element("config");

		{
			Element langElement = new Element("lang");
				langElement.appendChild(language);
			config.appendChild(langElement);

			Element kanjiFileElement = new Element("kanji_file");
				kanjiFileElement.appendChild(kanjiFile.getPath());
			config.appendChild(kanjiFileElement);

			Element strokeFileElement = new Element("tree_file");
			strokeFileElement.appendChild(treeFile.getPath());
			config.appendChild(strokeFileElement);

			Element extInfoElement = new Element("show_extended_info");
				extInfoElement.appendChild(extendedInfo.toString());
			config.appendChild(extInfoElement);
			
			Element extKanjiListElement = new Element("find_extended_Kanji_list");
				extKanjiListElement.appendChild(findInExtended.toString());
			config.appendChild(extKanjiListElement);
			
			Element kanaInRomajiElement = new Element("kana_in_romaji");
				kanaInRomajiElement.appendChild(kanaInRomaji.toString());
			config.appendChild(kanaInRomajiElement);
		}

		Document configDoc = new Document(config);

		try {
			Serializer slr = new Serializer(new BufferedOutputStream(new FileOutputStream(configFile)), "UTF-16");
			slr.setLineSeparator("\r\n");
			slr.setIndent(4);
			slr.write(configDoc);
			slr.flush();

		} catch (UnsupportedEncodingException e) {
			ExceptionHandler.handleException(e);
		} catch (FileNotFoundException e) {
			ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.INVALID_CONFIG_FILE_PATH);
		} catch (IOException e) {
			ExceptionHandler.handleException(e);
		}

		Messages.localePrintln("**JDKOptions: current config file saved");

	}

	@Override
	public String toString() {
		return "**JDKOptions: [configFile=" + configFile + ", extendedInfo=" + extendedInfo + ", findInExtended=" + findInExtended + ", kanaInRomaji=" + kanaInRomaji + ", kanjiFile=" + kanjiFile + ", language=" + language + ", treeFile=" + treeFile + "]";
	}


}
