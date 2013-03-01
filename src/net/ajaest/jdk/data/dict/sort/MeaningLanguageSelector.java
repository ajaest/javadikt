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

package net.ajaest.jdk.data.dict.sort;

import java.util.List;

import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.lib.data.interfaces.Selector;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

//TODO: JavaDoc
public class MeaningLanguageSelector implements Selector<ISO639ー1> {

	private ISO639ー1 selected;

	private List<ConnectorEnum> booleanConnector;
	private List<QAbout> domains;

	protected MeaningLanguageSelector(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {

		this.booleanConnector = booleanConnector;
		this.domains = domains;
	}
	
	public MeaningOrderDef in_Spanish() {

		this.setSelected(ISO639ー1.ES);

		return new MeaningOrderDef(this, booleanConnector, domains);
	}

	public MeaningOrderDef in_English() {

		this.setSelected(ISO639ー1.EN);

		return new MeaningOrderDef(this, booleanConnector, domains);
	}

	public MeaningOrderDef in_Portuguese() {

		this.setSelected(ISO639ー1.PT);

		return new MeaningOrderDef(this, booleanConnector, domains);
	}

	public MeaningOrderDef in_French() {

		this.setSelected(ISO639ー1.FR);

		return new MeaningOrderDef(this, booleanConnector, domains);
	}

	@Override
	public ISO639ー1 getSelected() {

		return selected;
	}

	@Override
	public void setSelected(ISO639ー1 selected) {

		this.selected = selected;
	}

	@Override
	public String toString() {

		return selected.toString();
	}

	public MeaningOrderDef languaje_enum_equals(ISO639ー1 lang) {
		this.setSelected(lang);
		return new MeaningOrderDef(this, booleanConnector, domains);
	}

}
