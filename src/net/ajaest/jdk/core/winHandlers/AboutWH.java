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

import net.ajaest.jdk.core.main.JDKGUIEngine;
import net.ajaest.jdk.core.main.JavaDiKt;
import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.data.dict.auxi.KanjiDatabaseInfo;
import net.ajaest.jdk.gui.windows.AboutWindow;


//TODO: JavaDoc
public class AboutWH implements WinHandler {

	private JDKGUIEngine jdkGui;
	private AboutWindow aw;

	public AboutWH(JDKGUIEngine jdkgui) {
		this.jdkGui = jdkgui;
	}
	
	public String getJDKInfoString(){
	
		String version = JavaDiKt.VERSION;
		String project = JavaDiKt.PROJECT;
		String dicName;
		String dicVersion;

		KanjiDatabaseInfo kdi = jdkGui.getJavaDikt().getKanjiDatabaseInfo();
		if (kdi != null) {
			dicName = kdi.getName();
			dicVersion = kdi.getVersion();
		} else {
			dicName = "--";
			dicVersion = "--";
		}

		String jdkString = String.format(Messages.get(Messages.ABOUTWIN_javadicText), version, project, dicName, dicVersion);

		return jdkString;
	}

	public String getTitleString() {

		return Messages.get(Messages.ABOUTWIN_title);
	}

	public String getLicenseBorderString() {

		return Messages.get(Messages.ABOUTWIN_borderTextLicense);
	}

	public String getLicenseInfoString() {
		
		return Messages.get(Messages.ABOUTWIN_licenseText);
	}

	public void invokeAboutWindow() {
		if (aw != null) {
			if(aw.isShowing())
				aw.toFront();
			else
				aw.setVisible(true);
		} else {
			aw = new AboutWindow(this, jdkGui.getMainWH().getWindow(), false);
			aw.setVisible(true);
		}

	}

	public Font getUnicodeFont() {

		return jdkGui.getUnicodeFont();
	}

}