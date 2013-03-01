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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import net.ajaest.jdk.core.main.JDKGUIEngine;
import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.gui.dialogs.InfoDialog;

//TODO: JavaDoc
public class InfoDialogsEngine {

	private JDKGUIEngine jdkGui;
	private InfoDialog id;

	public InfoDialogsEngine(JDKGUIEngine jdkGui) {
		this.jdkGui = jdkGui;
	}

	public void invokeInfoDialog(String title, String message, Dimension d) {

		id = new InfoDialog(jdkGui.getMainWH().getWindow(), true, this, title, message);

		if (d != null) {
			id.setSize(d);
		}

		Point mwloc = jdkGui.getMainWH().getWindow().getLocation();
		mwloc.setLocation(mwloc.getX() + 200, mwloc.getY() + 100);
		id.setLocation(mwloc);
		id.setVisible(true);

	}

	public void invokeBorderedInfoDialog(String title, String message, Dimension d, String borderText, boolean modal) {

		String bText = borderText;
		if (borderText == null) {
			bText = "";
		}

		id = new InfoDialog(jdkGui.getMainWH().getWindow(), modal, this, title, message);
		
		if (d != null) {
			id.setSize(d);
		}

		id.__getMessageScrollPanel().setBorder(javax.swing.BorderFactory.createTitledBorder(bText));

		Point mwloc = jdkGui.getMainWH().getWindow().getLocation();
		mwloc.setLocation(mwloc.getX() + 200, mwloc.getY() + 100);
		id.setLocation(mwloc);
		id.setVisible(true);
                
	}

	public String getMessage(String key) {

		return Messages.get(key);
	}

    public void AcceptButtonPressed() {
        id.dispose();
        id = null;
    }

	public Font getUnicodeFont() {

		return jdkGui.getUnicodeFont();
	}
}
