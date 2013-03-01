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
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.MeaningEntry;
import net.ajaest.jdk.data.kanji.ReadingEntry;

//TODO: JavaDoc
public class KanjiFlashCardStyler extends Styler<KanjiTag> {

    protected Map<String, String> messages;
    protected JPanel[] panels;

    protected static final Integer ROW_LIMIT = 10;

    private static final long serialVersionUID = -5548727035304598354L;

    public KanjiFlashCardStyler(String language) {
        super(language);

        JPanel noOptionPanel = new JPanel(new BorderLayout());
        noOptionPanel.setName(this.getMessage("PANEL_NAME"));
        noOptionPanel.add(new JLabel(this.getMessage("PANEL_NO_OPTION")));
        noOptionPanel.setSize(200, 100);
        this.panels = new JPanel[1];
        this.panels[0] = noOptionPanel;
    }

    @Override
    protected String getMessage(String message) {
        return this.messages.get(message);
    }

    @Override
    public String getName() {
        return this.messages.get("NAME");
    }

    @Override
    public JPanel[] getStyleOptionsJPanels() {
        // No panels for this version
        return this.panels;
    }

    @Override
    protected void initMessages(String lang) {
        this.messages = new HashMap<String, String>();

        if (lang.equals("es")) {

            this.messages.put("NAME", "Tarjeta de estudio");
            this.messages.put("PANEL_NAME", "Tarjeta de estudio");
            this.messages.put("PANEL_NO_OPTION", "No hay opciones adicionales para este estilo.");

            // Select field panel messages
            this.messages.put("SELECT_PANEL_NAME", "Columnas");
            this.messages.put("PANEL_SELECTED_COLUMN_NAME", "Campos seleccionados");
            this.messages.put("PANEL_NOT_SELECTED_COLUMN_NAME", "Campos");

            this.messages.put("KANJI_FREQUENCY", "Frecuencia");
            this.messages.put("KANJI_GRADE", "Grado");
            this.messages.put("KANJI_STROKE_COUNT", "Trazos");
            this.messages.put("KANJI_CLASSIC_RADICAL", "Radical");
            this.messages.put("KANJI_MEANING", "Significado");

            this.messages.put("READ_JA_ON", "On-yomi");
            this.messages.put("READ_JA_KUN", "Kun-yomi");

        } else { // English default

            super.language = "en";

            this.messages.put("NAME", "Flashcard");
            this.messages.put("PANEL_NAME", "Flashcard");
            this.messages.put("PANEL_NO_OPTION", "There are no additional options for this style.");

            // Select field panel messages
            this.messages.put("SELECT_PANEL_NAME", "Columns");
            this.messages.put("PANEL_SELECTED_COLUMN_NAME", "Selected field");
            this.messages.put("PANEL_NOT_SELECTED_COLUMN_NAME", "Fields");

            this.messages.put("KANJI_CLASSIC_RADICAL", "Radical");
            this.messages.put("KANJI_FREQUENCY", "Frequency");
            this.messages.put("KANJI_GRADE", "Grade");
            this.messages.put("KANJI_MEANING", "Meaning");
            this.messages.put("KANJI_STROKE_COUNT", "Strokes");

            this.messages.put("READ_JA_ON", "On-yomi");
            this.messages.put("READ_JA_KUN", "Kun-yomi");
        }
    }

    @Override
    public byte[] styler(List<KanjiTag> style) {

        StringBuilder htmlFCSnippet = new StringBuilder();
        // write snippet header(includes last part of the HEAD
        htmlFCSnippet.append("\t<title></title>\n");
        // write css
        htmlFCSnippet.append("  <style type=\"text/css\">\n");
        htmlFCSnippet.append("\n");
        htmlFCSnippet.append("                .kanji          {font-family:Sazanami-Hanazono Mincho; font-size:45pt;}\n");
        htmlFCSnippet.append("                .radical        {background-color: #EDE275; color: black;font-weight:bold;}\n");
        htmlFCSnippet.append("                .stroke         {background-color: #ddffff; color: black;font-weight:bold;}\n");
        htmlFCSnippet.append("                .grade          {background-color: #dfdfe1; color: black;font-weight:bold;}\n");
        htmlFCSnippet.append("                .frequency      {background-color: #FBBBB9; color: black;font-weight:bold;}\n");
        htmlFCSnippet.append("                .meaning        {background-color: #F62217; color: black;font-weight:bold;}\n");
        htmlFCSnippet.append("                .onYomi         {background-color: #00FF00; color: black;font-weight:bold;}\n");
        htmlFCSnippet.append("                .kunYomi        {background-color: #FFC800; color: black;font-weight:bold;}\n");
        htmlFCSnippet.append("                .nanori         {background-color: #FFAFAF; color: black;font-weight:bold;}\n");
        htmlFCSnippet.append("                .font           {font-family:Sazanami-Hanazono Mincho}");
        htmlFCSnippet.append("\n");
        htmlFCSnippet.append("                .borderedTable  {border: 3px solid black;}\n");
        htmlFCSnippet.append("                .tableHeight    {height:250px;}\n");
        htmlFCSnippet.append("                .tableWidht     {width:440px;}\n");
        htmlFCSnippet.append("\n");
        htmlFCSnippet.append("                div             {margin:10px; page-break-after: auto ;}\n");
        htmlFCSnippet.append("                table           {margin-left: auto;margin-right: auto; }\n");
        htmlFCSnippet.append("                th              {text-align:center; font-weight:bold;width:120px; height:19px;}\n");
        htmlFCSnippet.append("                td              {text-align:center;}\n");
        htmlFCSnippet.append("\n");
        htmlFCSnippet.append("  </style>\n");
        htmlFCSnippet.append("</head>\n");
        htmlFCSnippet.append("<body>\n");

        for (KanjiTag kt : style) {
            htmlFCSnippet.append("  <div>\n");
            htmlFCSnippet.append("    <table class=\"borderedTable tableHeight tableWidht\">\n");
            htmlFCSnippet.append("      <tr>\n");
            htmlFCSnippet.append("        <td>\n");
            htmlFCSnippet.append("          <table class=\"tableHeight\">\n");
            htmlFCSnippet.append("            <tr>\n");
            htmlFCSnippet.append("              <td class=\"kanji font\">" + kt.toString() + "</td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("            <tr>\n");
            htmlFCSnippet.append("              <td class=\"radical\">" + this.getMessage("KANJI_CLASSIC_RADICAL") + "</td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("            <tr>\n");
            if (kt.getClassicRadical() != null)
                htmlFCSnippet.append("              <td class=\"font\">" + kt.getClassicRadical().getNumber() + " - " + kt.getClassicRadical().toString() + "</td>\n");
            else
                htmlFCSnippet.append("              <td></td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("            <tr>\n");
            htmlFCSnippet.append("              <td class=\"stroke\">" + this.getMessage("KANJI_STROKE_COUNT") + "</td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("            <tr>\n");
            if (kt.getStrokeCount() != null)
                htmlFCSnippet.append("              <td>" + kt.getStrokeCount() + "</td>\n");
            else
                htmlFCSnippet.append("              <td></td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("            <tr>\n");
            htmlFCSnippet.append("              <td class=\"grade\">" + this.getMessage("KANJI_GRADE") + "</td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("            <tr>\n");
            if (kt.getGrade() != null)
                htmlFCSnippet.append("              <td>" + kt.getGrade() + "</td>\n");
            else
                htmlFCSnippet.append("              <td></td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("            <tr>\n");
            htmlFCSnippet.append("              <td class=\"frequency\"><b>" + this.getMessage("KANJI_FREQUENCY") + "</b></td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("            <tr>\n");
            if (kt.getFrequency() != null)
                htmlFCSnippet.append("              <td>" + kt.getFrequency() + "</td>\n");
            else
                htmlFCSnippet.append("              <td></td>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("          </table>\n");
            htmlFCSnippet.append("        </td>\n");
            htmlFCSnippet.append("\n");
            htmlFCSnippet.append("        <td>\n");
            htmlFCSnippet.append("          <table class=\"tableHeight\">\n");
            htmlFCSnippet.append("            <tr>\n");
            htmlFCSnippet.append("              <th class=\"meaning\">" + this.getMessage("KANJI_MEANING") + "</th>\n");
            htmlFCSnippet.append("\n");
            htmlFCSnippet.append("              <th class=\"kunYomi\">" + this.getMessage("READ_JA_ON") + "</th>\n");
            htmlFCSnippet.append("\n");
            htmlFCSnippet.append("              <th class=\"onYomi\">" + this.getMessage("READ_JA_KUN") + "</th>\n");
            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("\n");
            htmlFCSnippet.append("            <tr>\n");

            // Meaning column
            htmlFCSnippet.append("              <td>\n");
            htmlFCSnippet.append("                <table>\n");
            MeaningEntry localMeaning = null;
            for (MeaningEntry me : kt.getMeanings()) {
                if (me.getKey().toLowerCase().equals(this.getLanguaje())) {
                    localMeaning = me;
                    break;
                }
            }

            if (localMeaning != null) {
                Iterator<String> meaningIt = localMeaning.getElements().iterator();
                int i = 0;
                // TODO: No truncation
                while (meaningIt.hasNext() && (i < ROW_LIMIT)) {
                    htmlFCSnippet.append("                  <tr>\n");
                    htmlFCSnippet.append("                    <td>" + meaningIt.next() + "</td>\n");
                    htmlFCSnippet.append("                  </tr>\n");
                    i++;
                }
            }
            htmlFCSnippet.append("                </table>\n");
            htmlFCSnippet.append("              </td>\n");

            // kun-yomi column
            htmlFCSnippet.append("              <td>\n");
            htmlFCSnippet.append("                <table class=\"font\">\n");
            ReadingEntry kunRead = null;
            for (ReadingEntry re : kt.getReadings()) {
                if (re.getKey().toLowerCase().equals("ja_kun")) {
                    kunRead = re;
                    break;
                }
            }

            if (kunRead != null) {
                Iterator<String> readIt = kunRead.getElements().iterator();
                int i = 0;
                // TODO: No truncation
                while (readIt.hasNext() && (i < ROW_LIMIT)) {
                    htmlFCSnippet.append("                  <tr>\n");
                    htmlFCSnippet.append("                    <td>" + readIt.next() + "</td>\n");
                    htmlFCSnippet.append("                  </tr>\n");
                    i++;
                }
            }
            htmlFCSnippet.append("                </table>\n");
            htmlFCSnippet.append("              </td>\n");

            // on-yomi column
            htmlFCSnippet.append("              <td>\n");
            htmlFCSnippet.append("                <table class=\"font\">\n");
            ReadingEntry onRead = null;
            for (ReadingEntry re : kt.getReadings()) {
                if (re.getKey().toLowerCase().equals("ja_on")) {
                    onRead = re;
                    break;
                }
            }

            if (onRead != null) {
                Iterator<String> readIt = onRead.getElements().iterator();
                int i = 0;
                // TODO: No truncation
                while (readIt.hasNext() && (i < ROW_LIMIT)) {
                    htmlFCSnippet.append("                  <tr>\n");
                    htmlFCSnippet.append("                    <td>" + readIt.next() + "</td>\n");
                    htmlFCSnippet.append("                  </tr>\n");
                    i++;
                }
            }
            htmlFCSnippet.append("                </table>\n");
            htmlFCSnippet.append("              </td>\n");

            htmlFCSnippet.append("            </tr>\n");
            htmlFCSnippet.append("          </table>\n");
            htmlFCSnippet.append("        </td>\n");
            htmlFCSnippet.append("      </tr>\n");
            htmlFCSnippet.append("    </table>\n");
            htmlFCSnippet.append("  </div>\n");
        }

        return htmlFCSnippet.toString().getBytes(Charset.forName("UTF-16"));
    }

}
