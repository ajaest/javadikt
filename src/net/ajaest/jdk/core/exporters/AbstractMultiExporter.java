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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import net.ajaest.jdk.core.stylers.Styler;

//TODO: JavaDoc
public abstract class AbstractMultiExporter<E> extends Exporter<E> {

	private static final long serialVersionUID = -6242914721999420349L;

	protected List<Exporter<E>> subExporters;

	private Map<Integer, Exporter<E>> exporterMap;
	private Map<Integer, Integer> exporterIndex;

	public AbstractMultiExporter(String lang, List<Exporter<E>> subExporters) {
		super(lang);
		
		this.subExporters = subExporters;

		exporterMap = new HashMap<Integer, Exporter<E>>();
		exporterIndex = new HashMap<Integer, Integer>();

		int index = 0;
		for (Exporter<E> exp : subExporters) {
			for (Styler<E> sty : exp.getStylers()) {
				super.stylers.add(sty);
				exporterMap.put(index, exp);
				exporterIndex.put(index, exp.getStylers().indexOf(sty));
				index++;
			}
		}
	}

	@Override
	public Boolean export(List<E> list) {

		Exporter<E> selectedExporter = getSelectedExporter();
		File tempExportFile = null;
		Boolean success = false;

		try {
			tempExportFile = File.createTempFile(getExportFile().getName(), ".temp");
			tempExportFile.deleteOnExit();
		} catch (IOException e1) {
			super.setExportException(e1);
		}

		if (tempExportFile != null) {
			selectedExporter.setExportFile(tempExportFile);
			success = getSelectedExporter().export(list);
			super.setExportException(selectedExporter.getExportException());

			if (success) {
				try {
					if (!getExportFile().exists())
						getExportFile().createNewFile();

					success = transformSubExportation(tempExportFile);

					super.setExportException(null);
				} catch (Exception e) {
					super.setExportException(e);
				}
			}
		}

		tempExportFile.delete();

		return success;
	}

	@Override
	public abstract FileFilter getFileChooserFilters();

	@Override
	public abstract String getMessage(String key);

	@Override
	public abstract String getName();

	@Override
	protected abstract void initMessages(String lang);

	protected abstract Boolean transformSubExportation(File f) throws Exception;

	@Override
	public void setSelectedStyler(Integer selectedStyler) {
		super.setSelectedStyler(selectedStyler);
		
		exporterMap.get(selectedStyler).setSelectedStyler(exporterIndex.get(selectedStyler));
	}

	public Exporter<E> getSelectedExporter() {
		return exporterMap.get(getSelectedStylerIndex());
	}
	
	public List<Exporter<E>> getExporters() {

		return Collections.unmodifiableList(subExporters);
	}
}
