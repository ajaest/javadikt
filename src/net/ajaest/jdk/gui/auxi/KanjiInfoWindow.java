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

package net.ajaest.jdk.gui.auxi;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.ListModel;

import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.core.winHandlers.KanjiInfoWH;
import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.JISPair;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.VariantPair;

//TODO: JavaDoc
public class KanjiInfoWindow extends JFrame {

	private static final long serialVersionUID = -321076763217458412L;

         protected KanjiInfoWH kimwh;

    protected KanjiTag kt;
    protected boolean romanized;

	public KanjiInfoWindow(KanjiInfoWH kimwh, KanjiTag kt, boolean romanized) {

        this.kt = kt;
        this.romanized = romanized;
        this.kimwh = kimwh;

	}

	protected String getStrokeCountString() {

        return getIntegerString(kt.getStrokeCount());
    }


    protected String getGradeString(){

		return getIntegerString(kt.getGrade());

    }

    protected AbstractListModel getMeaningListModel(){

        AbstractListModel alm = new AbstractListModel() {

			private static final long serialVersionUID = -8232381397689439504L;
			List<String> strings = new ArrayList<String>();

            {
                String languaje = kimwh.getOptions().getLanguage();

                for(MeaningEntry me : kt.getMeanings()){
					if (me.getKey().equals(languaje.toLowerCase())) {
                        strings.addAll(me.getElements());
                        break;
                    }
                }
            }

            @Override
            public int getSize() {
                return strings.size();
            }

            @Override
            public Object getElementAt(int i) {
				return "  " + strings.get(i);
            }
        };

        return alm;

    }

    protected List<Image> getIcons(){
		return kimwh.getIcons();
    }

	public KanjiTag getKanjiTag() {
		return kt;
	}

	protected ListModel getVariantsListModel() {

		AbstractListModel alm = new AbstractListModel() {

			private static final long serialVersionUID = -8232381397689439504L;
			List<VariantPair> elements = new ArrayList<VariantPair>(kt.getVariants());

			@Override
			public int getSize() {
				return elements.size();
			}

			@Override
			public Object getElementAt(int i) {

				String variant = "  " + elements.get(i).getFirst() + ", " + elements.get(i).getSecond();
				return variant;
			}
		};

		return alm;
	}

	protected String getSkipCodeString() {

		return getStringString(kt.getQueryCodes().getSkipCode());
	}

	public String getSpahnHadamitzkyCodeString() {

		return getStringString(kt.getQueryCodes().getSpahnHadamitzkyCode());
	}

	public String getFourCornerString() {

		return getStringString(kt.getQueryCodes().getFourCornerCode());
	}

	public String getDeRooCodeString() {

		return getStringString(kt.getQueryCodes().getDeRooCode());
	}

	public String getJLPTLevelString() {

		return getIntegerString(kt.getJLPTLevel());
	}

	public String getFrequencyString() {

		return getIntegerString(kt.getFrequency()) + "º";
	}

	public String getNelsonRadicalString() {

		return getIntegerString(kt.getNelsonRadical());
	}

	public String getUnicodeString() {

		String ret;
		Integer unicode = kt.getUnicodeRef();

		if (unicode != null)
			ret = String.format("%x", unicode).toUpperCase();
		else
			ret = "--";

		return ret;
	}

	public String getJisCharset() {

		String ret;
		JISPair jp = kt.getJisCode();

		if (jp != null) {

			if (jp.getFirst().equals(JISPair.JIS_X_208))
				ret = Messages.get(Messages.JIS_208);
			else if (jp.getFirst().equals(JISPair.JIS_X_212))
				ret = Messages.get(Messages.JIS_212);
			else
				ret = Messages.get(Messages.JIS_212);
		} else {
			ret = "--";
		}

		return ret;
	}

	public String getJisCodeString() {

		String ret;

		if (kt.getJisCode() != null)
			ret = getStringString(kt.getJisCode().getSecond());
		else
			ret = "--";

		return ret;
	}

	public ListModel getDicReferencesListModel() {

		// Sorts dictionary list
		//chapuza!
		final SortedSet<String> localizedDicReferences = new TreeSet<String>();

		for (DicReferencePair drp : kt.getDicReferences()) {
			String dicName = Messages.get("DIC_" + drp.getFirst());
			localizedDicReferences.add("  " + dicName + ": " + drp.getSecond());
		}

		AbstractListModel alm = new AbstractListModel() {

			private static final long serialVersionUID = -8232381397689439504L;
			List<String> elements = new ArrayList<String>(localizedDicReferences);

			@Override
			public int getSize() {
				return elements.size();
			}

			@Override
			public Object getElementAt(int i) {

				return elements.get(i);
			}
		};

		return alm;
	}

	public String getGraphString() {

		String ret;
		KanjiGraph kf = kt.getGraph();

		// TODO: improve graph string representation
		if (kf != null)
			ret = kf.toAsteriskString();
		else
			ret = "--";

		return ret;
	}

	private String getIntegerString(Integer i) {

		String ret = null;

		if (i != null)
			ret = i.toString();
		else
			ret = "--";

        return ret;
	}

	private String getStringString(String s) {

		String ret = s;

		if (s == null)
			ret = "--";

		return ret;
	}

}
