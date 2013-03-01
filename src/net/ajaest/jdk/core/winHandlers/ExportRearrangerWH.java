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
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.gui.dialogs.ExportRearrangerDialog;
import net.ajaest.lib.swing.util.MoveRowTableModel;

//TODO: JavaDoc
public class ExportRearrangerWH implements WinHandler, Runnable {

	private MoveRowTableModel rearrangeTableModel;
	private ExportRearrangerDialog erd;
	
	private List<KanjiTag> sortedList;
	private List<KanjiFieldEnum> fieldSorters;
	private List<Object> fieldValues;
	
	private ExportDialogsEngine edE;

	public ExportRearrangerWH(ExportDialogsEngine edE) {

		this.edE = edE;
		sortedList = new ArrayList<KanjiTag>();
		fieldValues = new ArrayList<Object>();
	}

	public void setSortedList(List<KanjiTag> sortedList, List<KanjiFieldEnum> fieldSorters, List<Object> fieldValues) {

		if (fieldSorters.size() != fieldValues.size()) {
			throw new IllegalArgumentException("fieldSorters.size()!= fieldValues.size()");
		}

		this.sortedList = sortedList;
		this.fieldSorters = fieldSorters;
		this.fieldValues = fieldValues;

	}
	
	public List<KanjiTag> getSortedList() {

		return sortedList;
	}

	public void setFieldSorters(List<KanjiFieldEnum> fieldSorters) {

		this.fieldSorters = fieldSorters;
	}

	public String getMessage(String key) {

		return Messages.get(key);
	}

	public TableModel getRearrangeTableModel() {
		
		List<String> columnNames = new ArrayList<String>();

		columnNames.add("");
		columnNames.add(getMessage(Messages.WINEXPORTREARRANGER_kanjiLiteral));
		if (fieldSorters != null) {

			for (int i = 0; i < fieldSorters.size(); i++) {
				
				if (fieldValues.get(i) != null) {
					String formattedValueKey = fieldValues.get(i).toString();
					switch (fieldSorters.get(i)) {
						case KANJI_DIC_REFERENCE :
							formattedValueKey = "DIC_" + formattedValueKey;
							break;
						case KANJI_FIRST_READING :
							formattedValueKey = "READ_" + formattedValueKey.toUpperCase();
						case KANJI_FIRST_MEANING :
							formattedValueKey = Messages.LANGUAJE_EN;
						default :
							break;
					}

					columnNames.add(String.format(Messages.get(fieldSorters.get(i).toString()), Messages.get(formattedValueKey)));
				} else
					columnNames.add(Messages.get(fieldSorters.get(i).toString()));
			}
		}


		rearrangeTableModel = new MoveRowTableModel(columnNames);

		Object temp;
		List<Object> templ;
		int number = 1;
		for (KanjiTag kt : sortedList) {
			templ = new ArrayList<Object>();
			templ.add(number);
			number++;
			templ.add(kt);
			for (int i = 0; i < fieldSorters.size(); i++) {
				temp = kt.getByEnum(fieldSorters.get(i), (String) fieldValues.get(i));
				if (temp != null)
					templ.add(temp.toString());
				else
					templ.add("");
			}

			rearrangeTableModel.addRow(templ);
		}

		rearrangeTableModel.setCellEditable(false);

		return rearrangeTableModel;
	}

    public void goTopButtonPressed() {
    	
    	int selRows[] = erd.getKanjiRearrangeTable().getSelectedRows();

		if (selRows.length > 0) {
			selRows = rearrangeTableModel.moveRowsToTop(selRows);
			if (selRows.length > 0)
				erd.getKanjiRearrangeTable().getSelectionModel().setSelectionInterval(selRows[0], selRows[selRows.length - 1]);
		}
    }

    public void goUpButtonPressed() {

		int selRows[] = erd.getKanjiRearrangeTable().getSelectedRows();

		if (selRows.length > 0) {
			selRows = rearrangeTableModel.moveUp(selRows);
			if (selRows.length > 0)
				erd.getKanjiRearrangeTable().getSelectionModel().setSelectionInterval(selRows[0], selRows[selRows.length - 1]);
		}
    }

    public void goDownButtonPressed() {

		int selRows[] = erd.getKanjiRearrangeTable().getSelectedRows();

		if (selRows.length > 0) {
			selRows = rearrangeTableModel.moveDown(selRows);
			if (selRows.length > 0)
				erd.getKanjiRearrangeTable().getSelectionModel().setSelectionInterval(selRows[0], selRows[selRows.length - 1]);
		}
    }

    public void goBottomButtonPressed() {

		int selRows[] = erd.getKanjiRearrangeTable().getSelectedRows();

		if (selRows.length > 0) {
			selRows = rearrangeTableModel.moveRowsToBottom(selRows);
			if (selRows.length > 0)
				erd.getKanjiRearrangeTable().getSelectionModel().setSelectionInterval(selRows[0], selRows[selRows.length - 1]);
		}
    }

	@SuppressWarnings("unchecked")
	public void acceptButtonPressed() {

		sortedList = (List<KanjiTag>) rearrangeTableModel.getColumn(1);

		edE.getJDKGUIEngine().getMainWH().setExportSortedList(sortedList);

		erd.dispose();
    }

    public void cancelButtonPressed() {

		erd.dispose();
    }

	public void setExportRearranger(ExportRearrangerDialog erd) {

		this.erd = erd;
	}

	@Override
	public void run() {
		erd = new ExportRearrangerDialog(edE.getMainWindow(), true, this);
		// TODO: remove, implement font system
		erd.setVisible(true);
	}

	public Font getUnicodeFont() {

		return edE.getUnicodeFont();
	}
}
