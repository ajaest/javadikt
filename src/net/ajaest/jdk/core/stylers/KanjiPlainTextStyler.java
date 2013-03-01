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

package net.ajaest.jdk.core.stylers;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.jdk.data.kanji.VariantPair;

//TODO: JavaDoc
public class KanjiPlainTextStyler extends Styler<KanjiTag> {

    private static final long serialVersionUID = -971872742987257019L;
    private Map<String, HashMap<String, String>> strings = null;
    private final OptionPanel op;
    private final JPanel panels[];

    public KanjiPlainTextStyler(String lang) {
        super(lang);
        this.initMessages(this.getLanguaje());
        this.op = new OptionPanel();
        this.panels = new JPanel[]{this.op};

    }

    @Override
    public byte[] styler(List<KanjiTag> list) {

        return this.stylerString(list).getBytes(Charset.forName("UTF-16"));
    }

    public String stylerString(List<KanjiTag> list) {

        String ret = null;

        if (this.op.getCheckBoxChecked())
            ret = this.constructCompleteFileString(list);
        else
            ret = this.constructSimpleFileString(list);

        return ret;
    }

    private String constructSimpleFileString(List<KanjiTag> list) {

        Map<String, String> locStrings = this.strings.get("lang");
        // System.out.println(locStrings.values());

        StringBuilder s = new StringBuilder();
        for (KanjiTag kt : list) {
            s.append(String.format(locStrings.get("CHARACTER"), new String(kt.toChar())));

            if (kt.getClassicRadical() != null)
                s.append(locStrings.get("RADICAL"));
            s.append(String.format(locStrings.get("RADICAL_CLASSICAL"), kt
                    .getClassicRadical().getNumber()));

            if ((kt.getFrequency() != null) || (kt.getGrade() != null) || (kt.getJLPTLevel() != null) || (kt.getStrokeCount() != null) || (kt.getStrokeMiscounts().size() > 0) || (kt.getVariants().size() > 0)) {
                s.append(locStrings.get("MISCELANEA"));
                if (kt.getGrade() != null)
                    s.append(String.format(locStrings.get("MISCELANEA_GRADE"), kt.getGrade()));
                if (kt.getJLPTLevel() != null)
                    s.append(String.format(locStrings.get("MISCELANEA_JPTL"), kt.getJLPTLevel()));
                if (kt.getStrokeCount() != null)
                    s.append(String.format(locStrings.get("MISCELANEA_STROKE_C"), kt.getStrokeCount()));
            }

            if (kt.getDicReferences().size() > 0) {
                s.append(locStrings.get("DICREF"));

                // sort dics

                SortedSet<String> sortedDicRefs = new TreeSet<String>();

                for (DicReferencePair drp : kt.getDicReferences())
                    sortedDicRefs.add(String.format(locStrings.get("DICREF_INS"), this.strings.get("dicNames").get(drp.getFirst()), drp.getSecond()));

                for (String dicref : sortedDicRefs)
                    s.append(dicref);
            }

            if (kt.getReadings().size() > 0) {
                s.append(locStrings.get("READINGS"));
                for (ReadingEntry re : kt.getReadings()) {
                    if (re.getKey().equals("ja_on") || re.getKey().equals("ja_kun")) {
                        s.append(String.format(locStrings.get("READINGS_TYPE"), this.strings.get("readingType").get(re.getKey())));
                        for (String reading : re.getElements()) {
                            s.append(String.format(locStrings.get("READINGS_INS"), reading));
                        }
                    }
                }
            }

            if (kt.getMeanings().size() > 0) {
                MeaningEntry localizedEntry = null;
                MeaningEntry englisEntry = null;
                for (MeaningEntry re : kt.getMeanings()) {
                    if (re.getKey().equals(this.getLanguaje()))
                        localizedEntry = re;
                    else if (re.getKey().equals("en"))
                        englisEntry = re;
                }

                if (localizedEntry == null) {
                    if (englisEntry != null)
                        localizedEntry = englisEntry;
                    else
                        localizedEntry = kt.getMeanings().iterator().next();
                }

                s.append(String.format(locStrings.get("MEANINGS"), localizedEntry.getKey()));
                for (String mean : localizedEntry.getElements())
                    s.append(String.format(locStrings.get("MEANINGS_INS"), mean));

            }

            if (kt.getQueryCodes().getSkipCode() != null) {
                s.append(locStrings.get("QUERYCODES"));
                if (kt.getQueryCodes().getSkipCode() != null)
                    s.append(String.format(locStrings.get("QUERYCODES_SKIP"), kt.getQueryCodes().getSkipCode()));
            }
            // TODO: graph info
            // s.append(String.format(locStrings.get(""), ));
            s.append("\r\n\r\n************************\r\n\r\n");
        }

        return s.toString();
    }

    private String constructCompleteFileString(List<KanjiTag> list) {

        Map<String, String> locStrings = this.strings.get("lang");
        // System.out.println(locStrings.values());

        StringBuilder s = new StringBuilder();
        for (KanjiTag kt : list) {
            s.append(String.format(locStrings.get("CHARACTER"), new String(kt.toChar())));

            s.append(locStrings.get("CODIFICATION"));
            s.append(String.format(locStrings.get("CODIFICATION_EUC"), kt.getUnicodeRef()));
            if (kt.getJisCode() != null)
                s.append(String.format(locStrings.get("CODIFICATION_JIS"), kt.getJisCode().getFirst(), kt.getJisCode().getSecond()));

            if ((kt.getClassicRadical() != null) || (kt.getNelsonRadical() != null)) {
                s.append(locStrings.get("RADICAL"));
                if (kt.getClassicRadical() != null)
                    s.append(String.format(locStrings.get("RADICAL_CLASSICAL"),
                            kt.getClassicRadical().getNumber()));
                if (kt.getNelsonRadical() != null)
                    s.append(String.format(locStrings.get("RADICAL_NELSON"), kt.getNelsonRadical()));
            }
            if ((kt.getFrequency() != null) || (kt.getGrade() != null) || (kt.getJLPTLevel() != null) || (kt.getStrokeCount() != null) || (kt.getStrokeMiscounts().size() > 0) || (kt.getVariants().size() > 0)) {
                s.append(locStrings.get("MISCELANEA"));
                if (kt.getFrequency() != null)
                    s.append(String.format(locStrings.get("MISCELANEA_FREQ"), kt.getFrequency()));
                if (kt.getGrade() != null)
                    s.append(String.format(locStrings.get("MISCELANEA_GRADE"), kt.getGrade()));
                if (kt.getJLPTLevel() != null)
                    s.append(String.format(locStrings.get("MISCELANEA_JPTL"), kt.getJLPTLevel()));
                if (kt.getStrokeCount() != null)
                    s.append(String.format(locStrings.get("MISCELANEA_STROKE_C"), kt.getStrokeCount()));

                if (kt.getStrokeMiscounts().size() > 0) {
                    String strokeMisco = "";
                    for (Integer i : kt.getStrokeMiscounts()) {
                        strokeMisco += i + ", ";
                    }
                    strokeMisco = strokeMisco.substring(0, strokeMisco.length() - 2);
                    s.append(String.format(locStrings.get("MISCELANEA_STROKE_MISCO"), strokeMisco));
                }

                if (kt.getVariants().size() > 0) {
                    s.append(locStrings.get("VARIANT"));
                    for (VariantPair vp : kt.getVariants())
                        s.append(String.format(locStrings.get("VARIANT_INS"), vp.getFirst(), vp.getSecond()));
                }
            }

            if (kt.getDicReferences().size() > 0) {
                s.append(locStrings.get("DICREF"));
                // sort dics

                SortedSet<String> sortedDicRefs = new TreeSet<String>();

                for (DicReferencePair drp : kt.getDicReferences())
                    sortedDicRefs.add(String.format(locStrings.get("DICREF_INS"), this.strings.get("dicNames").get(drp.getFirst()), drp.getSecond()));

                for (String dicref : sortedDicRefs)
                    s.append(dicref);
            }

            if (kt.getReadings().size() > 0) {
                s.append(locStrings.get("READINGS"));
                for (ReadingEntry re : kt.getReadings()) {
                    s.append(String.format(locStrings.get("READINGS_TYPE"), this.strings.get("readingType").get(re.getKey())));
                    for (String reading : re.getElements()) {
                        s.append(String.format(locStrings.get("READINGS_INS"), reading));
                    }
                }
            }

            if (kt.getMeanings().size() > 0) {
                MeaningEntry localizedEntry = null;
                MeaningEntry englisEntry = null;
                for (MeaningEntry re : kt.getMeanings()) {
                    if (re.getKey().equals(this.getLanguaje()))
                        localizedEntry = re;
                    else if (re.getKey().equals("en"))
                        englisEntry = re;
                }

                if (localizedEntry == null) {
                    if (englisEntry != null)
                        localizedEntry = englisEntry;
                    else
                        localizedEntry = kt.getMeanings().iterator().next();
                }

                s.append(String.format(locStrings.get("MEANINGS"), localizedEntry.getKey()));
                for (String mean : localizedEntry.getElements())
                    s.append(String.format(locStrings.get("MEANINGS_INS"), mean));

            }

            if ((kt.getQueryCodes().getDeRooCode() != null) || (kt.getQueryCodes().getFourCornerCode() != null) || (kt.getQueryCodes().getSkipCode() != null) || (kt.getQueryCodes().getSpahnHadamitzkyCode() != null)) {
                s.append(locStrings.get("QUERYCODES"));
                if (kt.getQueryCodes().getDeRooCode() != null)
                    s.append(String.format(locStrings.get("QUERYCODES_DEROO"), kt.getQueryCodes().getDeRooCode()));
                if (kt.getQueryCodes().getFourCornerCode() != null)
                    s.append(String.format(locStrings.get("QUERYCODES_FOUR"), kt.getQueryCodes().getFourCornerCode()));
                if (kt.getQueryCodes().getSkipCode() != null)
                    s.append(String.format(locStrings.get("QUERYCODES_SKIP"), kt.getQueryCodes().getSkipCode()));
                if (kt.getQueryCodes().getSpahnHadamitzkyCode() != null)
                    s.append(String.format(locStrings.get("QUERYCODES_SPAHN"), kt.getQueryCodes().getSpahnHadamitzkyCode()));
            }
            // TODO: graph info
            // s.append(String.format(locStrings.get(""), ));
            s.append("\r\n\r\n************************\r\n\r\n");
        }

        return s.toString();
    }

    @Override
    public String getName() {
        return this.strings.get("localized").get("name");
    }

    @Override
    protected void initMessages(String lang) {
        this.strings = new HashMap<String, HashMap<String, String>>();


        // Language common tokens

        HashMap<String, String> dicNames = new HashMap<String, String>();
        dicNames.put("nelson_c", "Modern Reader’s Japanese-English Character Dictionary");
        dicNames.put("nelson_n", "The New Nelson Japanese-English Character Dictionary");
        dicNames.put("halpern_njecd", "New Japanese-English Character Dictionary");
        dicNames.put("halpern_kkld", "Kanji Learners Dictionary");
        dicNames.put("heisig", "Remembering The  Kanji");
        dicNames.put("gakken", "New Dictionary of Kanji Usage");
        dicNames.put("oneill_names", "Japanese Names");
        dicNames.put("oneill_kk", "Essential Kanji");
        dicNames.put("moro", "Daikanwajiten");
        dicNames.put("henshall", "A Guide To Remembering Japanese Characters");
        dicNames.put("sh_kk", "Kanji and Kana");
        dicNames.put("sakade", "A Guide To Reading and Writing Japanese");
        dicNames.put("jf_cards", "Japanese Kanji Flashcards");
        dicNames.put("henshall3", "A Guide To Reading and Writing Japanese");
        dicNames.put("tutt_cards", "Tuttle Kanji Cards");
        dicNames.put("crowley", "The Kanji Way to Japanese Language Power");
        dicNames.put("kanji_in_context", "Kanji in Context");
        dicNames.put("busy_people", "Japanese For Busy People");
        dicNames.put("kodansha_compact", "Kodansha Compact Kanji Guide");
        dicNames.put("maniette", "Les Kanjis dans la tete");
        dicNames.put("kanji_basic_book", "Kanji Basic Book");
        this.strings.put("dicNames", dicNames);

        // Spanish
        if (lang.equals("es")) {

            HashMap<String, String> localized = new HashMap<String, String>();
            localized.put("name", "Texto plano");
            localized.put("checkboxtext", "Mostrar información extendida");
            localized.put("panelName", "Opciones");
            this.strings.put("localized", localized);

            HashMap<String, String> lang_es = new HashMap<String, String>();
            lang_es.put("CHARACTER", "Caracter: %s \r\n");
            lang_es.put("CODIFICATION", "\r\n\tCodificación\r\n");
            lang_es.put("CODIFICATION_EUC", "\t\teuc: %x\r\n");
            lang_es.put("CODIFICATION_JIS", "\t\t%s: %s\r\n");
            lang_es.put("RADICAL", "\r\n\tRadical\r\n");
            lang_es.put("RADICAL_CLASSICAL", "\t\tClásico: %s\r\n");
            lang_es.put("RADICAL_NELSON", "\t\tNélson: %s\r\n");
            lang_es.put("MISCELANEA", "\r\n\tMiscelánea\r\n");
            lang_es.put("MISCELANEA_FREQ", "\t\tFrecuencia: %s\r\n");
            lang_es.put("MISCELANEA_GRADE", "\t\tGrado: %s\r\n");
            lang_es.put("MISCELANEA_JPTL", "\t\tNivel JPTL: %s\r\n");
            lang_es.put("MISCELANEA_STROKE_C", "\t\tNúmero de trazos: %s\r\n");
            lang_es.put("MISCELANEA_STROKE_MISCO", "\t\tErrores comunes en el número de trazos: %s\r\n");
            lang_es.put("VARIANT", "\t\tVariantes:\r\n");
            lang_es.put("VARIANT_INS", "\t\t\t[%s, %s]\r\n");
            lang_es.put("DICREF", "\r\n\tReferencias en Diccionarios\r\n\r\n");
            lang_es.put("DICREF_INS", "\t\t-%s: [%s]\r\n");
            lang_es.put("READINGS", "\r\n\tLecturas\r\n\r\n");
            lang_es.put("READINGS_TYPE", "\t\t-%s\r\n");
            lang_es.put("READINGS_INS", "\t\t\t[%s]\r\n");
            lang_es.put("MEANINGS", "\r\n\tSignificados, [%s]\r\n\r\n");
            lang_es.put("MEANINGS_INS", "\t\t\t[%s]\r\n");
            lang_es.put("QUERYCODES", "\r\n\tCódigos de búsqueda\r\n\r\n");
            lang_es.put("QUERYCODES_SKIP", "\t\tSKIP: [%s]\r\n");
            lang_es.put("QUERYCODES_SPAHN", "\t\tSpahn-Hadamitzky: [%s]\r\n");
            lang_es.put("QUERYCODES_FOUR", "\t\tCuatro esquinas: [%s]\r\n");
            lang_es.put("QUERYCODES_DEROO", "\t\tCódigo De Roo: [%s]\r\n");
            lang_es.put("GRAPH", "\r\n\tGrafo: \r\n");
            lang_es.put("GRAPH_STROKES", "\r\n\t\tTrazos: %s\r\n");
            lang_es.put("GRAPH_CLUES", "\r\n\t\tPistas: %s\r\n");
            this.strings.put("lang", lang_es);

            HashMap<String, String> readingType = new HashMap<String, String>();
            readingType.put("ja_on", "on-yomi");
            readingType.put("ja_kun", "kun-yomi");
            readingType.put("pinyin", "pinyin");
            readingType.put("korean_h", "korean hangul");
            readingType.put("korean_r", "korean romanized");
            readingType.put("nanori", "nanori");
            this.strings.put("readingType", readingType);

        } else {

            super.language = "en";

            // English otherwise
            HashMap<String, String> localized = new HashMap<String, String>();
            localized.put("name", "Plain text");
            localized.put("checkboxtext", "Show extended info");
            localized.put("panelName", "Options");
            this.strings.put("localized", localized);

            HashMap<String, String> lang_en = new HashMap<String, String>();
            lang_en.put("CHARACTER", "Character: %s \r\n");
            lang_en.put("CODIFICATION", "\r\n\tCodification\r\n");
            lang_en.put("CODIFICATION_EUC", "\t\teuc: %x\r\n");
            lang_en.put("CODIFICATION_JIS", "\t\t%s: %s\r\n");
            lang_en.put("RADICAL", "\r\n\tRadical\r\n");
            lang_en.put("RADICAL_CLASSICAL", "\t\tClassical: %s\r\n");
            lang_en.put("RADICAL_NELSON", "\t\tNelson: %s\r\n");
            lang_en.put("MISCELANEA", "\r\n\tMiscelanea\r\n");
            lang_en.put("MISCELANEA_FREQ", "\t\tFrequency: %s\r\n");
            lang_en.put("MISCELANEA_GRADE", "\t\tGrade: %s\r\n");
            lang_en.put("MISCELANEA_JPTL", "\t\tJPTL level: %s\r\n");
            lang_en.put("MISCELANEA_STROKE_C", "\t\tStroke count: %s\r\n");
            lang_en.put("MISCELANEA_STROKE_MISCO", "\t\tStroke common miscounts : %s\r\n");
            lang_en.put("VARIANT", "\t\tVariants:\r\n");
            lang_en.put("VARIANT_INS", "\t\t\t[%s, %s]\r\n");
            lang_en.put("DICREF", "\r\n\tDictionary References\r\n\r\n");
            lang_en.put("DICREF_INS", "\t\t-%s: [%s]\r\n");
            lang_en.put("READINGS", "\r\n\tReadings\r\n\r\n");
            lang_en.put("READINGS_TYPE", "\t\t-%s\r\n");
            lang_en.put("READINGS_INS", "\t\t\t[%s]\r\n");
            lang_en.put("MEANINGS", "\r\n\tMeanings, [%s]\r\n\r\n");
            lang_en.put("MEANINGS_INS", "\t\t\t[%s]\r\n");
            lang_en.put("QUERYCODES", "\r\n\tQuery Codes\r\n\r\n");
            lang_en.put("QUERYCODES_SKIP", "\t\tSKIP: [%s]\r\n");
            lang_en.put("QUERYCODES_SPAHN", "\t\tSpahn-Hadamitzky: [%s]\r\n");
            lang_en.put("QUERYCODES_FOUR", "\t\tFour corner: [%s]\r\n");
            lang_en.put("QUERYCODES_DEROO", "\t\tDe Roo code: [%s]\r\n");
            lang_en.put("GRAPH", "\r\n\tGraph: \r\n");
            lang_en.put("GRAPH_STROKES", "\r\n\t\tStrokes: %s\r\n");
            lang_en.put("GRAPH_CLUES", "\r\n\t\tClues: %s\r\n");
            this.strings.put("lang", lang_en);

            HashMap<String, String> readingType = new HashMap<String, String>();
            readingType.put("ja_on", "on-yomi");
            readingType.put("ja_kun", "kun-yomi");
            readingType.put("pinyin", "pinyin");
            readingType.put("korean_h", "coreano hangul");
            readingType.put("korean_r", "coreano romanizado");
            readingType.put("nanori", "nanori");
            this.strings.put("readingType", readingType);
        }
    }

    @Override
    public JPanel[] getStyleOptionsJPanels() {

        return this.panels;
    }

    @Override
    protected String getMessage(String key) {
        throw new RuntimeException("Not implemented");
    }


    private class OptionPanel extends JPanel{

        private static final long serialVersionUID = 3891130224197344400L;

        private final JCheckBox showExtended;

        private boolean getCheckBoxChecked() {
            return this.showExtended.isSelected();
        }

        private OptionPanel() {
            super();

            this.setName(KanjiPlainTextStyler.this.strings.get("localized").get("panelName"));

            this.showExtended = new JCheckBox();

            this.showExtended.setSelected(true);

            this.showExtended.setText(KanjiPlainTextStyler.this.strings.get("localized").get("checkboxtext"));

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(this);
            this.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(this.showExtended)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(this.showExtended)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }
    }
}
