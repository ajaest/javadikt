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

package net.ajaest.jdk.core.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import net.ajaest.jdk.core.stylers.KanjiPlainTextStyler;
import net.ajaest.jdk.data.kanji.KanjiTag;

//TODO: JavaDoc
public class PlainTextExporter extends Exporter<KanjiTag> {


	private static final long serialVersionUID = -8879756140144794244L;
	private Map<String, String> strings;
	private KanjiPlainTextStyler textStyler;
	private boolean activateAutoExtension;

	public PlainTextExporter(String lang) {
		super(lang);
		textStyler = new KanjiPlainTextStyler(lang);
		super.stylers.add(textStyler);
		activateAutoExtension = true;
	}

	@Override
	public Boolean export(List<KanjiTag> list) {

		Boolean success = false;

		String fileString = textStyler.stylerString(list);

		// System.out.println(fileString);

		if (getExportFile() != null && !getExportFile().getName().endsWith(".txt") && activateAutoExtension)
			setExportFile(new File(getExportFile().getAbsolutePath() + ".txt"));

		try {
			// no buffered is needed because the string is written at once
			if (!getExportFile().exists())
				getExportFile().createNewFile();
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getExportFile()), "UTF16"));
			fw.write(fileString);
			fw.flush();
			fw.close();
			success = true;
			super.setExportException(null);
		} catch (Exception e) {
			super.setExportException(e);
		}


		return success;
	}

	@Override
	public String getName() {
		return strings.get("name");
	}

	@Override
	protected void initMessages(String lang) {
		strings = new HashMap<String, String>();

		//Spanish
		if(lang.equals("es")){

			strings.put("name", "Archivo de texto");
			strings.put("fileDes", "Texto plano (.txt)");
			strings.put("WINNAME", "Más...");

		}else {
		
		// English otherwise
			super.languaje = "en";

			strings.put("name", "Text file");
			strings.put("fileDes", "Plain text (.txt)");
			strings.put("WINNAME", "Más...");
		}
	}

	@Override
	public FileFilter getFileChooserFilters() {
		
		return new FileFilter() {
			
			@Override
			public String getDescription() {
				return strings.get("fileDes");
			}
			
			@Override
			public boolean accept(File f) {
				return (f.getName().endsWith(".txt") || f.isDirectory()) && !f.getName().equals(".") && !f.getName().equals("..");
			}
		};
	}

	public KanjiPlainTextStyler getTextStyler() {
		return textStyler;
	}

	@Override
	public String getMessage(String key) {
		
		return strings.get(key);
	}

	public boolean isActivateAutoExtension() {
		return activateAutoExtension;
	}

	public void setActivateAutoExtension(boolean activateAutoExtension) {
		this.activateAutoExtension = activateAutoExtension;
	}
}
