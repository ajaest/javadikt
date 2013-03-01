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

package net.ajaest.jdk.gui.auxi;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import net.ajaest.jdk.core.winHandlers.MainWH;

//TODO: JavaDoc
public class ExportJPanel extends JPanel {

	private boolean shift_pressed = false;
	// The last pressed button
	private Integer previousPressed = -1;
	// must have same size as lkt
	private List<List<Boolean>> currentResultsSelectedForExport = null;
	private List<List<KanjiToggleButton>> lktb = null;
	private KeyAdapter mainKeyAdapter = null;
	private MainWH mwh = null;

	private static final long serialVersionUID = 9149391564458347013L;

	public ExportJPanel(List<Integer> lkt, MainWH mwh) {

		this.mwh = mwh;

		// controls if shift key is pressed
		mainKeyAdapter = new ExportJPanelKeyAdapter(this);
		this.addKeyListener(mainKeyAdapter);
		
		lktb = new ArrayList<List<KanjiToggleButton>>();
		currentResultsSelectedForExport = new ArrayList<List<Boolean>>();

		KanjiToggleButton ktb = null;
		for (int i=0; i<lkt.size(); i++) {

			if (i % 4 == 0) {// rows of 4 columns
				lktb.add(new ArrayList<KanjiToggleButton>(4));
				currentResultsSelectedForExport.add(new ArrayList<Boolean>(4));
			}

			ktb = new KanjiToggleButton(mwh.getUnicodeFont(),lkt.get(i), i, this);

			ktb.addKeyListener(mainKeyAdapter);
			ktb.enableSelectKanjiForExportListener(true);

			lktb.get(i / 4).add(ktb);
			currentResultsSelectedForExport.get(i / 4).add(false);

			this.add(ktb);
		}
	}

	public void buttonToggled(Integer listPosition) {


		int currentRow = listPosition / 4;
		int currentColumn = listPosition % 4;
		
		int previousRow = previousPressed / 4;
		int previousColumn = previousPressed % 4;
		

		if (!shift_pressed) {
			// System.out.println("button pressed");
			currentResultsSelectedForExport.get(currentRow).set(currentColumn, !currentResultsSelectedForExport.get(currentRow).get(currentColumn));

			if (currentResultsSelectedForExport.get(currentRow).get(currentColumn))
				previousPressed = listPosition;
			else
				previousPressed = -1;

		} else {
			
			
			if (previousPressed != -1 && currentResultsSelectedForExport.get(previousRow).get(previousColumn)) {
				toggleButtons(previousPressed, listPosition);
				this.previousPressed = -1;
			} else {
				currentResultsSelectedForExport.get(currentRow).set(currentColumn, !currentResultsSelectedForExport.get(currentRow).get(currentColumn));
				this.previousPressed = listPosition;
			}

		}

		setSelectionModified();
	}

	private void toggleButtons(Integer previousPressed, Integer listPosition) {
		
		int previousRow = previousPressed / 4;
		int previousColumn = previousPressed % 4;
		
		int currentRow = listPosition / 4;
		int currentColumn = listPosition % 4;
		
		int beginRow = currentRow < previousRow ? currentRow : previousRow;
		int beginColumn = currentColumn < previousColumn ? currentColumn : previousColumn;

		int distanceRow = Math.abs(previousRow - currentRow);
		int distanceColumn = Math.abs(previousColumn - currentColumn);
		
		int endRow = beginRow + distanceRow;
		int endColumn = beginColumn + distanceColumn;

		int countRow = 0;
		int countColumn = 0;

		KanjiToggleButton ktb = null;
		boolean tempPreviousState;
		for (countRow = beginRow; countRow < lktb.size(); countRow++) {
			for (countColumn = beginColumn; countColumn < lktb.get(countRow).size(); countColumn++) {
				if (countRow >= beginRow && countRow <= endRow &&
					countColumn >= beginColumn && countColumn <= endColumn) {
					tempPreviousState = currentResultsSelectedForExport.get(countRow).get(countColumn);
					currentResultsSelectedForExport.get(countRow).set(countColumn, !tempPreviousState);
					ktb = lktb.get(countRow).get(countColumn);
					ktb.setSelected(!tempPreviousState);

				}
			}
		}

		tempPreviousState = currentResultsSelectedForExport.get(previousRow).get(previousColumn);
		currentResultsSelectedForExport.get(previousRow).set(previousColumn, !tempPreviousState);
		ktb = lktb.get(previousRow).get(previousColumn);
		ktb.setSelected(!tempPreviousState);

		setSelectionModified();

	}

	@SuppressWarnings("unused")
	private void printActiveElements(){
		System.out.println("____________________________________");
		for (List<Boolean> lb : currentResultsSelectedForExport) {
			System.out.println(lb);
		}
	}

	public void setAll(Boolean b) {

		int row, column;
		for (row = 0; row < currentResultsSelectedForExport.size(); row++) {
			for (column = 0; column < currentResultsSelectedForExport.get(row).size(); column++) {
				currentResultsSelectedForExport.get(row).set(column, b);
				lktb.get(row).get(column).setSelected(b);
			}
		}

		setSelectionModified();
	}

	public void invertSelection() {

		int row, column;
		boolean previousValue;
		for (row = 0; row < currentResultsSelectedForExport.size(); row++) {
			for (column = 0; column < currentResultsSelectedForExport.get(row).size(); column++) {
				previousValue = currentResultsSelectedForExport.get(row).get(column);
				currentResultsSelectedForExport.get(row).set(column, !previousValue);
				lktb.get(row).get(column).setSelected(!previousValue);
			}
		}

		setSelectionModified();
	}

	private class ExportJPanelKeyAdapter extends KeyAdapter {

		private ExportJPanel ejp;

		public ExportJPanelKeyAdapter(ExportJPanel ejp) {
			this.ejp = ejp;
		}

		@Override
		public void keyPressed(KeyEvent ke) {
			// System.out.println(ke.getKeyCode());
			// 16 == shift
			if (ke.getKeyCode() == 16) {
				// System.out.println(KeyEvent.getKeyText(ke.getKeyCode()));
				ejp.shift_pressed = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent ke) {
			// 16 == shift
			if (ke.getKeyCode() == 16) {
				ejp.shift_pressed = false;
			}
		}
	}

	public List<Integer> getSelectedKanji() {

		List<Integer> selected = new ArrayList<Integer>();

		for (int i = 0; i < currentResultsSelectedForExport.size(); i++) {
			for (int j = 0; j < currentResultsSelectedForExport.get(i).size(); j++) {
				if (currentResultsSelectedForExport.get(i).get(j)) {
					selected.add(lktb.get(i).get(j).getKanjiRef());
				}
			}
		}

		return selected;
	}

	private void setSelectionModified() {
		mwh.setSorterListModified(true);
	}
}
