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

package net.ajaest.jdk.data.dict.auxi;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic class that stores information about a domain query.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class QAbout {

	/**
	 * It contains the domain query classes. It can't contain itself(risk of
	 * infinite loop lock)
	 */
	protected List<QAbout> queryDomains;

	/**
	 * {@code previusBConector.get(i)} contains relation between {@code
	 * queryAbout.get(i)} and {@code queryAbout.get(i-1)}
	 */
	protected List<ConnectorEnum> previousBConnector;

	public QAbout() {

		queryDomains = new ArrayList<QAbout>();
		previousBConnector = new ArrayList<ConnectorEnum>();
		previousBConnector.add(ConnectorEnum.FIRST);
	}

	/**
	 * Constructor for superclass. It does not check self-containing, so be
	 * careful.
	 * 
	 * @param booleanConnector
	 * @param domains
	 */
	protected QAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super();

		if (booleanConnector == null || domains == null)
			throw new NullPointerException();

		this.queryDomains = domains;
		this.previousBConnector = booleanConnector;
	}

	/**
	 * Adds a domain domain and the boolean operand that append it to the
	 * expression to the current domain query. This class cannot to itself be
	 * added in order to preserve query consistency.
	 * 
	 * @param bool
	 *            boolean operator
	 * @param qa
	 *            domain criteria
	 * @throws RuntimeException
	 *             if {@code domains} contains this class
	 */
	protected void addDomain(ConnectorEnum bool, QAbout qa) {

		if (bool == null || qa == null)
			throw new NullPointerException();
		if (qa.equals(this))
			throw new RuntimeException("QAbout cannot contains this class itself");

		queryDomains.add(qa);

		previousBConnector.add(bool);

	}

	// TODO: transform into unmodifiable
	/**
	 * It returns list of domains that inherits from {@code QAbout}.
	 * 
	 * @return The sorted list of objects containing domain classes
	 */
	public List<QAbout> getDomains() {
		return queryDomains;
	}

	// /**
	// * The sorted list containing the compulsory domain properties for search.
	// * {@code getQuery().get(i)} returns the property of domain {@code
	// * queryAbout.get(i)}
	// *
	// * @return The sorted list containing the domains properties. {@code
	// * queryAbout.get(i)}
	// */
	// public List<QueryCaseEnum> getQueryCase() {
	// return queryCase;
	// }

	// TODO: transform into unmodifiable
	/**
	 * Returns a list of boolean connector where {@code previusBConector.get(i)}
	 * contains relation between {@code queryAbout.get(i)} and {@code
	 * queryAbout.get(i-1)}
	 * 
	 * @return The sorted list of boolean connectors
	 */
	public List<ConnectorEnum> getBooleanCases() {
		return previousBConnector;
	}

	/**
	 * Returns an {@code int} representing the size of the criteria.
	 * 
	 * @return the size of the criteria
	 */
	public int size() {

		int size = getBooleanCases().size() < getDomains().size() ? getBooleanCases().size() : getDomains().size();

		return size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((previousBConnector == null) ? 0 : previousBConnector.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof QAbout)) {
			return false;
		}
		QAbout other = (QAbout) obj;
		if (previousBConnector == null) {
			if (other.previousBConnector != null) {
				return false;
			}
		} else if (!previousBConnector.equals(other.previousBConnector)) {
			return false;
		}
		if (queryDomains == null) {
			if (other.queryDomains != null) {
				return false;
			}
		} else if (!queryDomains.equals(other.queryDomains)) {
			return false;
		}
		return true;
	}

	/**
	 * Inhrited methods should override this.
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		if (size() > 0)
			sb.append(getDomains().get(0));
		for (int i = 1; i < size(); i++) {
			sb.append(' ');
			sb.append(getBooleanCases().get(i));
			sb.append(' ');
			sb.append(getDomains().get(i));
		}
		sb.append(']');
		return sb.toString();
	}

}
