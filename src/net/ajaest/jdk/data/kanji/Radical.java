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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Mutable class that stores information about a radical and provides methods to
 * build radical information objects. Because the only attribute shared between
 * radical's variants are the radical number, further information about the
 * radical must be searched in the specific variant.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class Radical extends RadicalTag {

	public Radical(Integer number) {
		super(number);
	}

	/**
	 * Adds the specified variant to this radical variant list. Only radical
	 * variants in which main number equals this radical's main number or in
	 * which main number is null will be added, otherwise and exception will be
	 * throw.
	 * 
	 * @param rvt
	 *            the radical variant to be added
	 * @throws IllegalArgumentException
	 *             if the variant's main number is not null and it's main number
	 *             differs from this radical main number
	 * 
	 * @see RadicalVariant#getAuxNumber()
	 * @see RadicalVariant#getNumber()
	 */
	public void addRadicalVariant(RadicalVariant rvt) {

		if (rvt != null && !(rvt.getNumber().equals(super.radicalNumber)))
			throw new IllegalArgumentException("main radical number mismatch");

		// Loop fills the list until radical variant position is reached.
		// Radical variants must be able to be retrieved from variant list using
		// a get(auxNumber-1) operation
		int i = super.radsVars.size();
		for (; i < rvt.getAuxNumber(); i++)
			super.radsVars.add(null);

		rvt.setNumber(super.radicalNumber);
		
		super.radsVars.set(rvt.getAuxNumber() - 1, rvt);

		


		Set<RadicalVariantTag> vars;
		vars = super.radsTypes.get(rvt.getRadicalType());

		if (vars == null) {
			vars = new HashSet<RadicalVariantTag>();
			super.radsTypes.put(rvt.getRadicalType(), vars);
		}
		vars.add(rvt);

		
	}

	/**
	 * Adds the specified variants stored in a collection to this radical
	 * variant list. Only radical variants in which main number equals this
	 * radical's main number or in which main number is {@code null} will be
	 * added, otherwise and exception will be throw.
	 * 
	 * @param rads
	 *            a collection containing the radical variants to be added
	 * @throws IllegalArgumentException
	 *             if any of the variant's main number is not null and it's main
	 *             number differs from this radical main number
	 * 
	 * @see RadicalVariant#getAuxNumber()
	 * @see RadicalVariant#getNumber()
	 */
	public void addRadicalVariants(Collection<RadicalVariant> rads) {

		// Linear order[O(3n)], but this complication isn't really necessary
		// because radical variant list wont be longer than 4. Just fun :)
		Iterator<RadicalVariant> it = rads.iterator();
			
		if (it.hasNext()) {

			Integer maxRadMainNumber = Integer.MIN_VALUE;
			
			RadicalVariant next;
			while (it.hasNext()) {
				next = it.next();
				if (next != null && !(next.getNumber().equals(super.radicalNumber)))
					throw new IllegalArgumentException("main radical number mismatch: " + next);
				
				maxRadMainNumber = next.getAuxNumber() > maxRadMainNumber ? 
						next.getAuxNumber()	: maxRadMainNumber;
			}
			
			int i = super.radsVars.size();
			for (; i < maxRadMainNumber; i++)
				super.radsVars.add(null);
			
			Set<RadicalVariantTag> vars;
			for (RadicalVariant rad : rads) {
				super.radsVars.set(rad.getAuxNumber() - 1, rad);

				vars = super.radsTypes.get(rad.getRadicalType());

				if (vars == null) {
					vars = new HashSet<RadicalVariantTag>();
					super.radsTypes.put(rad.getRadicalType(), vars);
				}

				vars.add(rad);
			}
		}
	}
}
