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

package net.ajaest.jdk.core.stylers;

import java.io.Serializable;
import java.util.List;

import javax.swing.JPanel;

//TODO: JavaDoc
public abstract class Styler<E> implements Comparable<Styler<?>>, Serializable {

	private static final long serialVersionUID = -5832895298374453438L;
	protected String language;
	
	public abstract byte[] styler(List<E> style);
	public abstract String getName();
	protected abstract void initMessages(String lang);
	protected abstract String getMessage(String lang);
	
	public Styler(String language) {
		this.language = language;
		initMessages(language.toLowerCase());
	}

	public String getLanguaje() {
		return language;
	}

	public void setLanguaje(String lang) {
		this.language = lang.toLowerCase();
		initMessages(language);
	}

	public abstract JPanel[] getStyleOptionsJPanels();
	
	@Override
	public int compareTo(Styler<?> style) {
		return getName().compareTo(style.getName());
	}
}
