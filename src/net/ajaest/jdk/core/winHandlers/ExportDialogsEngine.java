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

package net.ajaest.jdk.core.winHandlers;

import java.awt.Font;

import net.ajaest.jdk.core.exporters.Exporter;
import net.ajaest.jdk.core.main.JDKGUIEngine;
import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.gui.windows.MainWindow;

//TODO: JavaDoc
public class ExportDialogsEngine {

	private JDKGUIEngine jdkGui;
	private ExportRearrangerWH erwh;
	private ExportSorterAdderWH esawh;

	public ExportDialogsEngine(JDKGUIEngine jdkgui) {

		this.jdkGui = jdkgui;
		this.erwh = new ExportRearrangerWH(this);
		this.esawh = new ExportSorterAdderWH(this);
	}

	public void exportMoreButtonPressed() {

		int selectedExporter = getMainWindow()._getExportTabAs_FormatComboBox().getSelectedIndex();
		Exporter<KanjiTag> exporter = jdkGui.getJavaDikt().getExporters().get(selectedExporter);

		if (exporter.getDialogParent() == null)
			exporter.setDialogParent(getMainWindow());

		exporter.invokeExtraConfigJFrame();

	}

	public String getMessage(String key) {
		return Messages.get(key);
	}

	public MainWindow getMainWindow() {

		return jdkGui.getMainWH().getWindow();
	}

	public void invokeRearrangeDialog() {
		erwh.run();
	}

	public void invokeAddSorterDialog() {
		esawh.run();
	}

	public ExportSorterAdderWH getExportSorterAdderWH() {

		return esawh;
	}

	public ExportRearrangerWH getExportRearrangerWH() {

		return erwh;
	}

	public void setExportWindowModified(boolean b) {
		jdkGui.getMainWH().setSorterListModified(b);

	}

	public JDKGUIEngine getJDKGUIEngine() {

		return jdkGui;
	}

	public Font getUnicodeFont() {

		return jdkGui.getUnicodeFont();
	}

}
