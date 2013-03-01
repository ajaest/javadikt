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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

//TODO: JavaDoc
public class KanjiToggleButton extends JToggleButton {

	private Integer listPosition = null;
	private ActionListener al = null;
	private ExportJPanel ejp = null;
	private Integer kanjiRef = null;

	private static final long serialVersionUID = -5237984484676127297L;

	public KanjiToggleButton(Font font, Integer kanjiRef, final Integer listPosition, final ExportJPanel ejp) {
		super(new String(Character.toChars(kanjiRef)));

		this.kanjiRef = kanjiRef;

		setBackground(Color.WHITE);
		setFont(font.deriveFont((float) 31));

		this.listPosition = listPosition;
		this.ejp = ejp;


		al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ejp.buttonToggled(listPosition);
			}
		};

	}

	public Integer getListPosition() {
		return listPosition;
	}

	public void enableSelectKanjiForExportListener(boolean b) {

		if (b && this.getActionListeners().length == 0) {
			this.addActionListener(al);
		} else {
			this.removeActionListener(al);
		}
	}

	public ExportJPanel getExportPanel() {
		return ejp;
	}

	public Integer getKanjiRef() {
		return kanjiRef;
	}

}
