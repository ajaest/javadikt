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
import net.ajaest.jdk.data.dict.auxi.QueryExpression;

/**
 * Class that represents a consistent and finished kanji expression about kanji
 * fields. Using methods AND and OR will increase this kanji expression, but
 * this won't be consistent until the successive call of processes does not
 * returns a new kanji Expression.<br>
 * Kanji Expressions can be used to define a Kanji or a group of kanjis using
 * almost natural language, or to build a user friendly comprehensible query
 * about kanji.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiExpression extends QueryExpression<KanjiQuery> {

	protected KanjiExpression(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
	}

	@Override
	protected KanjiQuery createBeginInstance() {

		return new KanjiQuery(super.previousBConnector, super.queryDomains);
	}

}
