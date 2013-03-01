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

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.data.auxi.KanjiEnums;
import net.ajaest.jdk.data.auxi.KanjiEnums.FieldTypes;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.gui.dialogs.ExportSorterAdderDialog;

public class ExportSorterAdderWH implements WinHandler, Runnable {

	private ExportDialogsEngine edE;
	private ExportSorterAdderDialog esaD;
	private Boolean running = false;

	public ExportSorterAdderWH(ExportDialogsEngine edE) {
		this.edE = edE;
	}

        public String getMessage(String key){

            return edE.getMessage(key);
        }

	@Override
	public void run() {

		if (!running) {
			esaD = new ExportSorterAdderDialog(edE.getMainWindow(), true, this);
			esaD.getLevelComboBox().setSelectedIndex(edE.getMainWindow()._getExportTabSorting_SortingTable().getRowCount());
			esaD.setVisible(true);
			running = true;
		} else {
			esaD.setVisible(true);
			esaD.toFront();
		}
	}

	public ComboBoxModel getLevelComboBoxModel() {

		DefaultTableModel exptm = (DefaultTableModel) edE.getMainWindow()._getExportTabSorting_SortingTable().getModel();

		int levels = exptm.getRowCount();
		// begins in zero +1
		levels += 1;

		String[] levelsStr = new String[levels];

		for (int i = 1; i <= levels; i++) {
			levelsStr[i - 1] = new Integer(i).toString();
		}

		return new DefaultComboBoxModel(levelsStr);
	}

	public ComboBoxModel getOrderByComboBoxModel() {

		String sel = getSelectedField().replace("ORDER_", "KANJI_");
		
		FieldTypes kfeStr = KanjiEnums.getFieldType(sel);
		

		// System.out.println(sel);
		// System.out.println(kfeStr);

		String[] orderby;
		switch (kfeStr) {
			case INTEGER :
				orderby = Messages.getList(Messages.LIST_ORDENABLE_METHODS_INTEGER);
				break;
			case STRING :
				orderby = Messages.getList(Messages.LIST_ORDENABLE_METHODS_STRING);
				break;
			default :
				orderby = new String[0];
		}

		return new DefaultComboBoxModel(orderby);
	}

	public ComboBoxModel getFieldComboBoxModel() {
		String[] ordenableFields = Messages.getList(Messages.LIST_ORDENABLE_FIELDS);

		DefaultComboBoxModel dcbm = new DefaultComboBoxModel(ordenableFields);
		return dcbm;
	}

	public ComboBoxModel getFieldValueComboBoxModel() {

		String sel = getSelectedField();

		String[] values = null;

		if (sel.equals(Messages.ORDER_DIC_INDEX))
			values = Messages.getList(Messages.LIST_CRITERIA_VALUES_DIC);

		else if (sel.equals(Messages.ORDER_READING))
			values = Messages.getList(Messages.LIST_CRITERIA_VALUES_READING_EXTENDED);
		else
			values = new String[0];

		if (values.length != 0)
			esaD.getFieldValueComboBox().setEnabled(true);
		else
			esaD.getFieldValueComboBox().setEnabled(false);

		return new DefaultComboBoxModel(values);
	}

	public void addButtonPressed() {

		DefaultTableModel exptm = (DefaultTableModel) edE.getMainWindow()._getExportTabSorting_SortingTable().getModel();

		String selField;
		String selFieldValue;
		String selLevel;
		String selOrderBy;
		
		int idx = esaD.getFieldComboBox().getSelectedIndex();
		selField = Messages.getList(Messages.LIST_ORDENABLE_FIELDS)[idx];
		
		String selFieldKey = getSelectedField();
		idx = esaD.getFieldValueComboBox().getSelectedIndex();
		if (selFieldKey.equals(Messages.ORDER_JIS_CHARSET)) {
			selFieldValue = Messages.getList(Messages.LIST_CRITERIA_VALUES_JIS)[idx];

		} else if (selFieldKey.equals(Messages.ORDER_DIC_INDEX)) {
			selFieldValue = Messages.getList(Messages.LIST_CRITERIA_VALUES_DIC)[idx];
		} else if (selFieldKey.equals(Messages.ORDER_READING)) {
			selFieldValue = Messages.getList(Messages.LIST_CRITERIA_VALUES_READING_EXTENDED)[idx];
		} else {
			selFieldValue = null;
		}

		if (selFieldValue != null)
			selField += " - " + selFieldValue;

		selLevel = new Integer(esaD.getLevelComboBox().getSelectedIndex() + 1).toString();

		KanjiFieldEnum selKanjiKey = KanjiFieldEnum.valueOf(getSelectedField().replace("ORDER", "KANJI"));
		idx = esaD.getOrderByComboBox().getSelectedIndex();
		switch (KanjiEnums.getFieldType(selKanjiKey)) {
			case INTEGER :
				selOrderBy = Messages.getList(Messages.LIST_ORDENABLE_METHODS_INTEGER)[idx];
				break;
			case STRING :
				selOrderBy = Messages.getList(Messages.LIST_ORDENABLE_METHODS_STRING)[idx];
				break;
			case ORDENABLE_STRING :
				selOrderBy = Messages.getList(Messages.LIST_ORDENABLE_METHODS_INTEGER)[idx];
				break;
			default :
				selOrderBy = "";
				break;
		}
		
		int level = esaD.getLevelComboBox().getSelectedIndex() + 1;
		if ((level - 1) == exptm.getRowCount())
			exptm.addRow(new Object[]{selLevel, selOrderBy, selField});
		else{
			exptm.setValueAt(selLevel, level - 1, 0);
			exptm.setValueAt(selOrderBy, level - 1, 1);
			exptm.setValueAt(selField, level - 1, 2);
		}
		// refresh level selector
		ComboBoxModel levMod = getLevelComboBoxModel();
		esaD.getLevelComboBox().setModel(levMod);
		esaD.getLevelComboBox().setSelectedIndex(edE.getMainWindow()._getExportTabSorting_SortingTable().getRowCount());

		edE.setExportWindowModified(true);
		//TODO: remove from field combo box previously selected items
	}

	public void cancelButtonPressed() {
		running = false;
		esaD.dispose();

	}

	public void fieldComboBoxChanged() {

		ComboBoxModel fcbm = getFieldValueComboBoxModel();
		esaD.getFieldValueComboBox().setModel(fcbm);

		ComboBoxModel ocbm = getOrderByComboBoxModel();
		esaD.getOrderByComboBox().setModel(ocbm);
	}

	public ExportSorterAdderDialog getExportSorterAdderDialog() {
		return esaD;
	}

	private String getSelectedField() {

		int selMesIdx;
		if (esaD != null)
			selMesIdx = esaD.getFieldComboBox().getSelectedIndex();
		else
			// not yet initialized
			selMesIdx = 0;
		
		String selMessage = Messages.getList(Messages.LIST_ORDENABLE_FIELDS)[selMesIdx];

		String key = Messages.getKeyInList(selMessage, Messages.LIST_ORDENABLE_FIELDS);

		return key;
	}

	public Font getUnicodeFont() {

		return edE.getUnicodeFont();
	}

}

