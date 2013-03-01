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

package net.ajaest.jdk.data.auxi.database;

import java.util.List;

/**
 * 
 * Class that represents a consistent and finished expression about related
 * fields. Using methods AND and OR will increase this kanji expression, but
 * this won't be consistent until the successive call of processes does not
 * returns a new kanji Expression.<br>
 * Expressions can be used to define an object or a group of objects using
 * almost natural language, or to build a user friendly comprehensible query
 * about an object.
 * 
 * @author Luis Alfonso Arce González
 * 
 * 
 * @param <E>
 *            class which is the beginning step in order to build a consistent
 *            expression
 */
public abstract class QueryExpression<E extends QAbout> extends QAbout {

	public QueryExpression() {
		super();
	}

	protected QueryExpression(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
	}

	/**
	 * Appends a new domain condition using boolean AND operator to this
	 * expression.
	 * 
	 * @return an object inheriting {@code QAbout} representing the beginning
	 *         step in order to append a domain condition to this expression.
	 */
	public E AND() {
		super.previousBConnector.add(ConnectorEnum.AND);
		return createBeginInstance();
	}

	/**
	 * Appends a new domain condition using boolean OR operator to this
	 * expression.
	 * 
	 * @return an object inheriting {@code QAbout} representing the beginning
	 *         step in order to append a domain condition to this expression.
	 */
	public E OR() {
		super.previousBConnector.add(ConnectorEnum.OR);
		return createBeginInstance();
	}

	/**
	 * Creates an instance of the objects which is the first step in order to
	 * build a consistent expression.
	 * 
	 * @return An object of the class that represents the first step in order to
	 *         build a consistent expression
	 */
	protected abstract E createBeginInstance();

}
