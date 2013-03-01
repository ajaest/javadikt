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

package net.ajaest.jdk.data.dict.query.kanjiFields;

import java.util.List;

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.query.StringValueQAbout;

/**
 * QAbout class that represents a kanji variant type domain.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class VariantTypeQAbout extends StringValueQAbout {

	protected VariantTypeQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
	}

	@Override
	public String getFieldName() {

		return "Variant type";
	}

	@Override
	public KanjiFieldEnum getFieldEnum() {

		return KanjiFieldEnum.KANJI_VARIANT_TYPE;
	}

}
