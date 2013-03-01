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

import java.util.Set;

/**
 * Generic interface with basic methods to manage a database.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public interface DatabaseManager<OBJ, EXP extends QueryExpression<?>> {

	/**
	 * @param query
	 *            an object representing a criteria
	 * @return a list of objects that matches the specified criteria.
	 */
	Set<OBJ> executeQuery(EXP query);

	/**
	 * Closes the database
	 */
	void close();

}
