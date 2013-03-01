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

import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.auxi.ConnectorEnum;
import net.ajaest.jdk.data.dict.auxi.QAbout;
import net.ajaest.jdk.data.dict.auxi.QueryCaseEnum;

/**
 * Class that represents a kanji query expression building step in which a
 * condition about the domain {@code getFieldName} related to an object value is
 * going to be added. At this point, a kanji Expression is not consistent.
 * 
 * @author Luis Alfonso Arce González
 * 
 * @param <E>
 */
public abstract class ValueQAbout<E> extends QAbout implements KanjiFieldDescriptor {

	protected E value;
	protected QueryCaseEnum qc;

	protected ValueQAbout(List<ConnectorEnum> booleanConnector, List<QAbout> domains) {
		super(booleanConnector, domains);
	}

	@Override
	public abstract String getFieldName();
	@Override
	public abstract KanjiFieldEnum getFieldEnum();

	public KanjiExpression notEquals(E value) {
		this.value = value;
		this.qc = QueryCaseEnum.NOT_EQUALS;
		super.queryDomains.add(this);

		return new KanjiExpression(super.previousBConnector, super.queryDomains);
	}

	public KanjiExpression equal(E value) {
		this.value = value;
		this.qc = QueryCaseEnum.EQUALS;
		super.queryDomains.add(this);

		return new KanjiExpression(super.previousBConnector, super.queryDomains);

	}

	public KanjiExpression isNull() {
		this.qc = QueryCaseEnum.NULL;
		super.queryDomains.add(this);

		return new KanjiExpression(super.previousBConnector, super.queryDomains);

	}

	public KanjiExpression greatherThan(E value) {
		this.value = value;
		this.qc = QueryCaseEnum.GREATER_THAN;
		super.queryDomains.add(this);

		return new KanjiExpression(super.previousBConnector, super.queryDomains);

	}

	public KanjiExpression lessThan(E value) {
		this.value = value;
		this.qc = QueryCaseEnum.LESS_THAN;
		super.queryDomains.add(this);

		return new KanjiExpression(super.previousBConnector, super.queryDomains);

	}

	public KanjiExpression equaslOrGreatherThan(E value) {
		this.value = value;
		this.qc = QueryCaseEnum.GREATER_OR_EQUALS_THAN;
		super.queryDomains.add(this);

		return new KanjiExpression(super.previousBConnector, super.queryDomains);

	}

	public KanjiExpression equalsOrLessThan(E value) {
		this.value = value;
		this.qc = QueryCaseEnum.LESS_OR_EQUALS_THAN;
		super.queryDomains.add(this);

		return new KanjiExpression(super.previousBConnector, super.queryDomains);

	}

	public E getValue() {
		return value;
	}

	public QueryCaseEnum getQueryCase() {
		return qc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((qc == null) ? 0 : qc.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof ValueQAbout<?>)) {
			return false;
		}
		ValueQAbout<?> other = (ValueQAbout<?>) obj;
		if (qc == null) {
			if (other.qc != null) {
				return false;
			}
		} else if (!qc.equals(other.qc)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(getFieldName());
		sb.append(' ');
		sb.append(qc);
		sb.append(' ');
		sb.append(value);
		sb.append(']');

		return sb.toString();
	}
}
