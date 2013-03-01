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

package net.ajaest.jdk.data.dict.query;

import java.util.List;

import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;

/**
 * QAbout class that represents an integer domain.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public abstract class IntegerValueQAbout extends ValueQAbout<Integer> {

	protected IntegerValueQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
	}

	public KanjiExpression notEquals(String value) {

		return notEquals(Integer.valueOf(value));
	}

	public KanjiExpression equalz(String value) {

		return equal(Integer.valueOf(value));

	}

	public KanjiExpression greatherThan(String value) {

		return greatherThan(Integer.valueOf(value));

	}

	public KanjiExpression lessThan(String value) {

		return lessThan(Integer.valueOf(value));

	}

	public KanjiExpression equaslOrGreatherThan(String value) {

		return equaslOrGreatherThan(Integer.valueOf(value));

	}

	public KanjiExpression equalsOrLessThan(String value) {

		return equalsOrLessThan(Integer.valueOf(value));

	}

	@Override
	public abstract String getFieldName();

}
