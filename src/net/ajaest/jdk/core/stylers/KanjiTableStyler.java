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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.ajaest.jdk.core.main.Launcher;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.dict.query.KanjiQuery;
import net.ajaest.jdk.data.kanji.DicReferencePair;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.jdk.data.kanji.VariantPair;
import net.ajaest.lib.swing.panel.SelectPanel;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceRoyale;

//TODO: JavaDoc
public class KanjiTableStyler extends Styler<KanjiTag> {

    private static final long serialVersionUID = -3464022163399026329L;
    private Map<String, String> messages;

    private final JPanel[] optionPanels;
    private final SelectPanel selectionFieldsPanel;

    public KanjiTableStyler(String language) {
        super(language);

        // Init selection panel
        this.optionPanels = new JPanel[1];
        Set<String> fields = this.initFieldNames();
        this.selectionFieldsPanel = new SelectPanel(this.getMessage("PANEL_SELECTED_COLUMN_NAME"), this.getMessage("PANEL_NOT_SELECTED_COLUMN_NAME"), fields);
        this.selectionFieldsPanel.setPreferredSize(new Dimension(500, 300));
        this.selectionFieldsPanel.setName(this.getMessage("NAME"));
        this.optionPanels[0] = this.selectionFieldsPanel;
    }

    @Override
    public String getName() {

        return this.getMessage("NAME");
    }

    @Override
    protected void initMessages(String lang) {
        this.messages = new HashMap<String, String>();

        if (lang.equals("es")) {

            this.messages.put("NAME", "Tabla");

            // Select field panel messages
            this.messages.put("SELECT_PANEL_NAME", "Columnas");
            this.messages.put("PANEL_SELECTED_COLUMN_NAME", "Campos seleccionados");
            this.messages.put("PANEL_NOT_SELECTED_COLUMN_NAME", "Campos");
            this.messages.put("ACCEPT_BUTTON", "Aceptar");

            this.messages.put("KANJI_LITERAL", "Kanji");
            this.messages.put("KANJI_FOUR_CORNER", "Código cuatro esquinas");
            this.messages.put("KANJI_DE_ROO", "Código de Roo");
            this.messages.put("KANJI_SKIP", "Código SKIP");
            this.messages.put("KANJI_SPAHN_HADAMITZKY", "Código Spahn-Hadamitzky");
            this.messages.put("KANJI_FREQUENCY", "Frecuencia");
            this.messages.put("KANJI_GRADE", "Grado");
            this.messages.put("KANJI_GRAPH", "Grafo asterisco");
            this.messages.put("KANJI_DIC_NAME", "Nombre del diccionario");
            this.messages.put("KANJI_DIC_INDEX", "Referencia del diccionario");
            this.messages.put("KANJI_JIS_CHARSET", "Juego de caracteres JIS");
            this.messages.put("KANJI_JIS_CODE", "Código JIS kuten");
            this.messages.put("KANJI_JLPT_LEVEL", "Nivel JLPT");
            this.messages.put("KANJI_STROKE_COUNT", "Número de trazos");
            this.messages.put("KANJI_CLASSIC_RADICAL", "Radical clásico");
            this.messages.put("KANJI_MEANING", "Significado");
            this.messages.put("KANJI_READING_TYPE", "Tipo de lectura");
            this.messages.put("KANJI_VARIANT_TYPE", "Tipo de variante");
            this.messages.put("KANJI_VARIANT_INDEX", "Referencia de la variante");
            this.messages.put("KANJI_READING", "Lectura");
            this.messages.put("KANJI_UNICODE_VALUE", "Unicode");

            this.messages.put("DIC_nelson_c", "Diccionario \"Modern Reader’s Japanese-English Character Dictionary\"");
            this.messages.put("DIC_nelson_n", "Diccionario \"The New Nelson Japanese-English Character Dictionary\"");
            this.messages.put("DIC_halpern_njecd", "Diccionario \"New Japanese-English Character Dictionary\"");
            this.messages.put("DIC_halpern_kkld", "Diccionario \"Kanji Learners Dictionary\"");
            this.messages.put("DIC_heisig", "Diccionario \"Remembering The  Kanji\"");
            this.messages.put("DIC_gakken", "Diccionario \"New Dictionary of Kanji Usage\"");
            this.messages.put("DIC_oneill_names", "Diccionario \"Japanese Names\"");
            this.messages.put("DIC_oneill_kk", "Diccionario \"Essential Kanji\"");
            this.messages.put("DIC_moro", "Diccionario \"Daikanwajiten\"");
            this.messages.put("DIC_henshall", "Diccionario \"A Guide To Remembering Japanese Characters\"");
            this.messages.put("DIC_sh_kk", "Diccionario \"Kanji and Kana\"");
            this.messages.put("DIC_sakade", "Diccionario \"Sakade's A Guide To Reading and Writing Japanese\"");
            this.messages.put("DIC_jf_cards", "Diccionario \"Japanese Kanji Flashcards\"");
            this.messages.put("DIC_henshall3", "Diccionario \"Henshall's A Guide To Reading and Writing Japanese\"");
            this.messages.put("DIC_tutt_cards", "Diccionario \"Tuttle Kanji Cards\"");
            this.messages.put("DIC_crowley", "Diccionario \"The Kanji Way to Japanese Language Power\"");
            this.messages.put("DIC_kanji_in_context", "Diccionario \"Kanji in Context\"");
            this.messages.put("DIC_busy_people", "Diccionario \"Japanese For Busy People\"");
            this.messages.put("DIC_kodansha_compact", "Diccionario \"Kodansha Compact Kanji Guide\"");
            this.messages.put("DIC_maniette", "Diccionario \"Les Kanjis dans la tete\"");
            this.messages.put("DIC_kanji_basic_book", "Diccionario \"Kanji Basic Book\"");

            this.messages.put("READ_JA_ON", "Lectura \"on-yomi\"");
            this.messages.put("READ_JA_KUN", "Lectura \"kun-yomi\"");
            this.messages.put("READ_NANORI", "Lectura \"nanori\"");

        } else { // English default

            super.language = "en";

            this.messages.put("NAME", "Table");

            // Select field panel messages
            this.messages.put("SELECT_PANEL_NAME", "Columns");
            this.messages.put("PANEL_SELECTED_COLUMN_NAME", "Selected field");
            this.messages.put("PANEL_NOT_SELECTED_COLUMN_NAME", "Fields");
            this.messages.put("ACCEPT_BUTTON", "Accept");

            this.messages.put("KANJI_LITERAL", "Kanji");
            this.messages.put("KANJI_CLASSIC_RADICAL", "Classic radical");
            this.messages.put("KANJI_DE_ROO", "De Roo code");
            this.messages.put("KANJI_DIC_NAME", "Dictionary name");
            this.messages.put("KANJI_DIC_INDEX", "Dictionary index");
            this.messages.put("KANJI_FOUR_CORNER", "Four corner code");
            this.messages.put("KANJI_FREQUENCY", "Frequency");
            this.messages.put("KANJI_GRADE", "Grade");
            this.messages.put("KANJI_GRAPH", "Asterisk graph");
            this.messages.put("KANJI_JIS_CHARSET", "JIS charset");
            this.messages.put("KANJI_JIS_CODE", "JIS kuten code");
            this.messages.put("KANJI_JLPT_LEVEL", "JLPT level");
            this.messages.put("KANJI_MEANING", "Meaning");
            this.messages.put("KANJI_READING_TYPE", "Reading type");
            this.messages.put("KANJI_READING", "Reading");
            this.messages.put("KANJI_SKIP", "Skip code");
            this.messages.put("KANJI_STROKE_COUNT", "Stroke count");
            this.messages.put("KANJI_SPAHN_HADAMITZKY", "Spahn-Hadamitky code");
            this.messages.put("KANJI_UNICODE_VALUE", "Unicode");
            this.messages.put("KANJI_VARIANT_INDEX", "Variant reference");

            this.messages.put("DIC_nelson_c", "Dictionary \"Modern Reader’s Japanese-English Character Dictionary\"");
            this.messages.put("DIC_nelson_n", "Dictionary \"The New Nelson Japanese-English Character Dictionary\"");
            this.messages.put("DIC_halpern_njecd", "Dictionary \"New Japanese-English Character Dictionary\"");
            this.messages.put("DIC_halpern_kkld", "Dictionary \"Kanji Learners Dictionary\"");
            this.messages.put("DIC_heisig", "Dictionary \"Remembering The  Kanji\"");
            this.messages.put("DIC_gakken", "Dictionary \"New Dictionary of Kanji Usage\"");
            this.messages.put("DIC_oneill_names", "Dictionary \"Japanese Names\"");
            this.messages.put("DIC_oneill_kk", "Dictionary \"Essential Kanji\"");
            this.messages.put("DIC_moro", "Dictionary \"Daikanwajiten\"");
            this.messages.put("DIC_henshall", "Dictionary \"A Guide To Remembering Japanese Characters\"");
            this.messages.put("DIC_sh_kk", "Dictionary \"Kanji and Kana\"");
            this.messages.put("DIC_sakade", "Dictionary \"Sakade's A Guide To Reading and Writing Japanese\"");
            this.messages.put("DIC_jf_cards", "Dictionary \"Japanese Kanji Flashcards\"");
            this.messages.put("DIC_henshall3", "Dictionary \"Henshall's A Guide To Reading and Writing Japanese\"");
            this.messages.put("DIC_tutt_cards", "Dictionary \"Tuttle Kanji Cards\"");
            this.messages.put("DIC_crowley", "Dictionary \"The Kanji Way to Japanese Language Power\"");
            this.messages.put("DIC_kanji_in_context", "Dictionary \"Kanji in Context\"");
            this.messages.put("DIC_busy_people", "Dictionary \"Japanese For Busy People\"");
            this.messages.put("DIC_kodansha_compact", "Dictionary \"Kodansha Compact Kanji Guide\"");
            this.messages.put("DIC_maniette", "Dictionary \"Les Kanjis dans la tete\"");
            this.messages.put("DIC_kanji_basic_book", "Dictionary \"Kanji Basic Book\"");

            this.messages.put("READ_JA_ON", "Reading \"on-yomi\"");
            this.messages.put("READ_JA_KUN", "Reading \"kun-yomi\"");
            this.messages.put("READ_NANORI", "Reading \"nanori\"");
        }
    }

    @Override
    public byte[] styler(List<KanjiTag> style) {

        List<String> selectedFields = this.selectionFieldsPanel.getSelectedFields();

        StringBuilder htmlTableSnippet = new StringBuilder();
        // write snippet header(includes last part of the HEAD
        htmlTableSnippet.append("\t<title></title>\n");
        htmlTableSnippet.append("</head>\n");
        htmlTableSnippet.append("<body>\n");

        htmlTableSnippet.append("\t<table border=\"1\">\r\n");

        // Headers
        htmlTableSnippet.append("\t\t<tr>\n");
        for (String s : selectedFields) {
            htmlTableSnippet.append("\t\t\t<th align=\"center\">" + s + "</th>\n");
        }
        htmlTableSnippet.append("\t\t</tr>\n");
        // Rows
        // TODO: very very slow, chapuza
        for (KanjiTag kt : style) {
            htmlTableSnippet.append("\t\t<tr>\n");
            for (String fieldMessage : selectedFields) {
                htmlTableSnippet.append("\t\t\t<td align=\"center\">");

                String key = this.getKey(fieldMessage);

                if (key.contains("DIC_")) {
                    String dicName = key.replace("DIC_", "");
                    for (DicReferencePair drp : kt.getDicReferences()) {
                        if (drp.getFirst().equals(dicName))
                            htmlTableSnippet.append(drp.getSecond());
                    }

                } else if (key.contains("KANJI_")) {
                    KanjiFieldEnum kfe = KanjiFieldEnum.valueOf(key);
                    switch (kfe) {
                    case KANJI_CLASSIC_RADICAL :
                        htmlTableSnippet.append(kt.getClassicRadical().getNumber() + " - " + kt.getClassicRadical().toString());
                        break;
                    case KANJI_GRAPH :
                        KanjiGraph kg = ((KanjiGraph) kt.getByEnum(kfe));
                        if (kg != null)
                            htmlTableSnippet.append(kg.toAsteriskString());
                        else
                            htmlTableSnippet.append("");
                        break;
                    case KANJI_DE_ROO :
                    case KANJI_JIS_CHARSET :
                    case KANJI_FOUR_CORNER:
                    case KANJI_LITERAL :
                    case KANJI_SKIP :
                    case KANJI_SPAHN_HADAMITZKY :
                    case KANJI_FREQUENCY :
                    case KANJI_GRADE :
                    case KANJI_JLPT_LEVEL :
                    case KANJI_STROKE_COUNT :
                        Object value = kt.getByEnum(kfe);
                        if (value != null)
                            htmlTableSnippet.append(value.toString());
                        else
                            htmlTableSnippet.append("");
                        break;
                    case KANJI_UNICODE_VALUE :
                        Integer unicode = (Integer) kt.getByEnum(kfe);
                        htmlTableSnippet.append(String.format("%x", unicode).toUpperCase());
                        break;
                    case KANJI_MEANING :
                        MeaningEntry me = null;

                        for (MeaningEntry met : kt.getMeanings()) {
                            if (met.getKey().equals(this.getLanguaje())) {
                                me = met;
                                break;
                            }
                        }

                        if (me != null) {
                            StringBuilder tableIntoTable = new StringBuilder("\n\t\t\t\t<table>\n");
                            for (String st : me.getElements())
                                tableIntoTable.append("\t\t\t\t\t<tr>\n\t\t\t\t\t\t<td align=\"center\">" + st + "</td>\n\t\t\t\t\t</tr>\n");
                            tableIntoTable.append("\t\t\t\t</table>\n\t\t\t");
                            htmlTableSnippet.append(tableIntoTable);
                        } else {
                            htmlTableSnippet.append("");
                        }
                        break;
                    case KANJI_JIS_CODE :
                        htmlTableSnippet.append(kt.getJisCode().getSecond());
                        break;
                    case KANJI_VARIANT_INDEX :{
                        if (kt.getVariants().size() != 0) {
                            StringBuilder tableIntoTable = new StringBuilder("\n\t\t\t\t<table>\n");
                            for (VariantPair vp : kt.getVariants())
                                tableIntoTable.append("\t\t\t\t\t<tr>\n\t\t\t\t\t\t<td align=\"center\">" + vp.getFirst() + ": " + vp.getSecond() + "</td>\n\t\t\t\t\t</tr>\n");
                            tableIntoTable.append("\t\t\t\t</table>\n\t\t\t");
                            htmlTableSnippet.append(tableIntoTable);
                        } else {
                            htmlTableSnippet.append("");
                        }
                        break;
                    }
                    }
                } else {

                    ReadingEntry re = null;

                    String readingType = key.replace("READ_", "").toLowerCase();

                    for (ReadingEntry ret : kt.getReadings()) {
                        if (ret.getKey().equals(readingType)) {
                            re = ret;
                            break;
                        }
                    }

                    if (re != null) {
                        StringBuilder tableIntoTable = new StringBuilder("\n\t\t\t\t<table>\n");
                        for (String st : re.getElements())
                            tableIntoTable.append("\t\t\t\t\t<tr>\n\t\t\t\t\t\t<td align=\"center\">" + st + "</td>\n\t\t\t\t\t</tr>\n");
                        tableIntoTable.append("\t\t\t\t</table>\n\t\t\t");
                        htmlTableSnippet.append(tableIntoTable);
                    } else {
                        htmlTableSnippet.append("");
                    }
                }

                htmlTableSnippet.append("</td>\n");
            }
            htmlTableSnippet.append("\t\t</tr>\n");
        }

        htmlTableSnippet.append("</table>\n");

        return htmlTableSnippet.toString().getBytes(Charset.forName("UTF-16"));
    }

    @Override
    public JPanel[] getStyleOptionsJPanels() {
        return this.optionPanels;
    }

    @Override
    protected String getMessage(String key) {
        return this.messages.get(key);
    }

    // TODO: Slow, chapuza
    protected String getKey(String message) {

        for (String k : this.messages.keySet()) {
            if (this.messages.get(k).equals(message))
                return k;
        }
        return null;
    }

    private Set<String> initFieldNames() {

        Set<String> fields = new TreeSet<String>();

        fields.add(this.getMessage("KANJI_CLASSIC_RADICAL"));
        fields.add(this.getMessage("KANJI_DE_ROO"));
        fields.add(this.getMessage("DIC_busy_people"));
        fields.add(this.getMessage("DIC_crowley"));
        fields.add(this.getMessage("DIC_gakken"));
        fields.add(this.getMessage("DIC_halpern_kkld"));
        fields.add(this.getMessage("DIC_halpern_njecd"));
        fields.add(this.getMessage("DIC_heisig"));
        fields.add(this.getMessage("DIC_henshall"));
        fields.add(this.getMessage("DIC_henshall3"));
        fields.add(this.getMessage("DIC_jf_cards"));
        fields.add(this.getMessage("DIC_kanji_basic_book"));
        fields.add(this.getMessage("DIC_kanji_in_context"));
        fields.add(this.getMessage("DIC_kodansha_compact"));
        fields.add(this.getMessage("DIC_maniette"));
        fields.add(this.getMessage("DIC_moro"));
        fields.add(this.getMessage("DIC_nelson_c"));
        fields.add(this.getMessage("DIC_nelson_n"));
        fields.add(this.getMessage("DIC_oneill_kk"));
        fields.add(this.getMessage("DIC_oneill_names"));
        fields.add(this.getMessage("DIC_sakade"));
        fields.add(this.getMessage("DIC_sh_kk"));
        fields.add(this.getMessage("DIC_tutt_cards"));


        fields.add(this.getMessage("KANJI_FOUR_CORNER"));
        fields.add(this.getMessage("KANJI_FREQUENCY"));
        fields.add(this.getMessage("KANJI_GRADE"));
        fields.add(this.getMessage("KANJI_GRAPH"));
        fields.add(this.getMessage("KANJI_JIS_CHARSET"));
        fields.add(this.getMessage("KANJI_JIS_CODE"));
        fields.add(this.getMessage("KANJI_JLPT_LEVEL"));
        fields.add(this.getMessage("KANJI_LITERAL"));
        fields.add(this.getMessage("KANJI_MEANING"));
        fields.add(this.getMessage("READ_JA_KUN"));
        fields.add(this.getMessage("READ_JA_ON"));
        fields.add(this.getMessage("READ_NANORI"));
        fields.add(this.getMessage("KANJI_SKIP"));
        fields.add(this.getMessage("KANJI_SPAHN_HADAMITZKY"));
        fields.add(this.getMessage("KANJI_STROKE_COUNT"));
        fields.add(this.getMessage("KANJI_UNICODE_VALUE"));
        fields.add(this.getMessage("KANJI_VARIANT_INDEX"));

        return fields;
    }

    public static void main(String... args) throws UnsupportedLookAndFeelException {

        Plastic3DLookAndFeel.setPlasticTheme(new ExperienceRoyale());

        UIManager.setLookAndFeel(new Plastic3DLookAndFeel());

        final KanjiTableStyler ts = new KanjiTableStyler("es");

        JFrame jf = new JFrame("Test");

        jf.add(ts.selectionFieldsPanel, BorderLayout.NORTH);

        Launcher.main();

        final List<KanjiTag> retKt = Launcher.jdk.executeQuery(new KanjiQuery().unicode_value().greatherThan(0));

        JButton jb = new JButton("Convertir");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jf2 = new JFrame("Result");
                jf2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jf2.setSize(400, 300);
                JEditorPane je = new JEditorPane();
                je.setContentType("text/html");
                je.putClientProperty(javax.swing.JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
                je.setFont(Launcher.jdk.getJDKGuiEngine().getUnicodeFont());
                String s = new String(ts.styler(retKt));
                je.setText(s);
                jf2.add(new JScrollPane(je));
                jf2.setVisible(true);

                JFrame jf3 = new JFrame("Result");
                jf3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jf3.setSize(400, 300);
                JEditorPane je1 = new JEditorPane();

                je1.setText(s);
                jf3.add(new JScrollPane(je1));
                jf3.setVisible(true);

            }
        });

        jf.add(jb, BorderLayout.SOUTH);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(600, 400);

        jf.setVisible(true);
    }
}
