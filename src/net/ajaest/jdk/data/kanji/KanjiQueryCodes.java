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

package net.ajaest.jdk.data.kanji;

import net.ajaest.jdk.data.auxi.KanjiReference;

/**
 * Class that stores a set kanji description codes useful to find a refered
 * kanji.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiQueryCodes implements KanjiReference {

	private String skip;
	private String spahnHadamitzky;
	private String deRoo;
	private String fourCorner;
	private Integer unicodeRef;

	public KanjiQueryCodes(String skip, String spahnHadamitzky, String deRoo, String fourCorner, Integer unicodeRef) {
		super();
		this.skip = skip;
		this.spahnHadamitzky = spahnHadamitzky;
		this.deRoo = deRoo;
		this.fourCorner = fourCorner;
		this.unicodeRef = unicodeRef;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KanjiQueryCodes other = (KanjiQueryCodes) obj;
		if (deRoo == null) {
			if (other.deRoo != null)
				return false;
		} else if (!deRoo.equals(other.deRoo))
			return false;
		if (fourCorner == null) {
			if (other.fourCorner != null)
				return false;
		} else if (!fourCorner.equals(other.fourCorner))
			return false;
		if (skip == null) {
			if (other.skip != null)
				return false;
		} else if (!skip.equals(other.skip))
			return false;
		if (spahnHadamitzky == null) {
			if (other.spahnHadamitzky != null)
				return false;
		} else if (!spahnHadamitzky.equals(other.spahnHadamitzky))
			return false;
		if (unicodeRef == null) {
			if (other.unicodeRef != null)
				return false;
		} else if (!unicodeRef.equals(other.unicodeRef))
			return false;
		return true;
	}

	public String getDeRooCode() {
		return deRoo;
	}

	public String getFourCornerCode() {
		return fourCorner;
	}

	public String getSkipCode() {
		return skip;
	}

	public String getSpahnHadamitzkyCode() {
		return spahnHadamitzky;
	}

	@Override
	public Integer getUnicodeRef() {
		return unicodeRef;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deRoo == null) ? 0 : deRoo.hashCode());
		result = prime * result + ((fourCorner == null) ? 0 : fourCorner.hashCode());
		result = prime * result + ((skip == null) ? 0 : skip.hashCode());
		result = prime * result + ((spahnHadamitzky == null) ? 0 : spahnHadamitzky.hashCode());
		result = prime * result + ((unicodeRef == null) ? 0 : unicodeRef.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KanjiQueryCodes [");
		if (deRoo != null) {
			builder.append("deRoo=");
			builder.append(deRoo);
			builder.append(", ");
		}
		if (fourCorner != null) {
			builder.append("fourCorner=");
			builder.append(fourCorner);
			builder.append(", ");
		}
		if (skip != null) {
			builder.append("skip=");
			builder.append(skip);
			builder.append(", ");
		}
		if (spahnHadamitzky != null) {
			builder.append("spahnHadamitzky=");
			builder.append(spahnHadamitzky);
		}
		builder.append("]");
		return builder.toString();
	}

}
