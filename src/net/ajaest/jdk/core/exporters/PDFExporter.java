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

package net.ajaest.jdk.core.exporters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import net.ajaest.jdk.core.stylers.KanjiFlashCardStyler;
import net.ajaest.jdk.core.stylers.KanjiTableStyler;
import net.ajaest.jdk.core.stylers.Styler;
import net.ajaest.jdk.data.kanji.KanjiTag;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

//TODO: JavaDoc
public class PDFExporter extends AbstractMultiExporter<KanjiTag> {

    private static final long serialVersionUID = 8208798748431453055L;

    protected Map<String, String> messages;

    public PDFExporter(String lang) {
        super(lang, initExporters(lang));
    }

    private static List<Exporter<KanjiTag>> initExporters(String lang) {

        List<Exporter<KanjiTag>> exporters = new ArrayList<Exporter<KanjiTag>>();

        HTMLExporter htmlExp = new HTMLExporter(lang);
        htmlExp.setXhtml(true);
        htmlExp.setActivateAutoExtension(false);
        exporters.add(htmlExp);

        PlainTextExporter textExp = new PlainTextExporter(lang);
        textExp.setActivateAutoExtension(false);
        exporters.add(textExp);

        return exporters;
    }

    @Override
    public Boolean export(List<KanjiTag> kts) {
        if ((this.getExportFile() != null) && !this.getExportFile().getName().endsWith(".pdf"))
            this.setExportFile(new File(this.getExportFile().getAbsolutePath() + ".pdf"));

        return super.export(kts);
    }

    @Override
    public FileFilter getFileChooserFilters() {

        return new FileFilter() {

            @Override
            public String getDescription() {
                return PDFExporter.this.messages.get("FILE_DES");
            }

            @Override
            public boolean accept(File f) {
                return (f.getName().endsWith(".pdf") || f.isDirectory()) && !f.getName().equals(".") && !f.getName().equals("..");
            }
        };
    }

    @Override
    public String getMessage(String key) {

        return this.messages.get(key);
    }

    @Override
    public String getName() {

        return this.getMessage("NAME");
    }

    @Override
    protected void initMessages(String lang) {

        this.messages = new HashMap<String, String>();
        if (lang.equals("es")) {
            this.messages.put("NAME", "PDF");
            this.messages.put("FILE_DES", "Archivo PDF");
            this.messages.put("WINNAME", "Exportar como PDF - Más...");
        } else {
            super.languaje = "en";
            this.messages.put("NAME", "PDF");
            this.messages.put("FILE_DES", "PDF File");
            this.messages.put("WINNAME", "Export as PDF - More...");
        }
    }

    @Override
    protected Boolean transformSubExportation(File f) throws Exception {

        Boolean succes = true;

        OutputStream os = null;
        try {
            Styler<KanjiTag> selectedStyler = this.getStylers().get(this.getSelectedStylerIndex());

            String adaptedDoc;

            if (selectedStyler instanceof KanjiFlashCardStyler)
                adaptedDoc = this.adaptFlashCardDocument(f);
            else if (selectedStyler instanceof KanjiTableStyler)
                adaptedDoc = this.adaptTableDocument(f);
            else
                adaptedDoc = this.adaptPlainTextDocument(f);

            ITextRenderer renderer = new ITextRenderer();

            renderer.getFontResolver().addFont("font/Sazanami-Hanazono-Mincho.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            renderer.setDocumentFromString(adaptedDoc);

            renderer.layout();

            os = new FileOutputStream(this.getExportFile());

            renderer.createPDF(os);
            os.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (os != null)
                os.close();
            succes = false;
        }

        return succes;
    }

    private String adaptPlainTextDocument(File f) throws Exception {

        StringBuilder document = new StringBuilder();

        document.append("<?xml version=\"1.0\" encoding=\"utf-16\"?>");
        document.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\" >");
        document.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        document.append("<head>\n");
        document.append("  <style type=\"text/css\">\n");
        document.append("                .kanji          {font-family:Sazanami-Hanazono Mincho;}\n");
        document.append("  </style>\n");
        document.append("</head>\n");
        document.append("<body>\n");
        document.append("  <div xml:space=\"preserve\"><span class=\"kanji\">\n");
        String fileText = this.getText(f);
        fileText = fileText.replace(" ", "&nbsp;");
        fileText = fileText.replace("\r\n", "<br />");
        fileText = fileText.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        document.append(fileText);
        document.append("</span></div>\n");
        document.append("</body>\n");
        document.append("</html>\n");

        return document.toString();
    }

    private String adaptTableDocument(File f) throws Exception {

        String adapted = this.getText(f);
        // Table styler does no generates CSS block
        adapted = adapted.replace("<head>", "<head><style type=\"text/css\"> table {font-family:Sazanami-Hanazono Mincho;}</style>");

        return adapted.replace("\r\n", "");
    }

    private String adaptFlashCardDocument(File f) throws Exception {

        String adapted = this.getText(f);

        // Replaces table auto height-spacing
        // adapted = adapted.replace(" class=\"tableHeight\">", ">");
        adapted = adapted.replace("class=\"tableHeight font\"", "class=\"font\"");

        // Adds page break every three cards
        String div[] = adapted.split("<div[^>]*>");

        StringBuilder adapted1 = new StringBuilder(div[0]);
        if (div.length > 1) {
            adapted1.append("  <div>");
            adapted1.append(div[1]);
        }

        for (int i = 2; i < div.length; i++) {
            if ((i - 1) % 3 == 0)
                adapted1.append("  <div style=\"page-break-before: always;\">\n");
            else
                adapted1.append("  <div>");

            adapted1.append(div[i]);

        }

        adapted1.replace(0, 2, "");// removes first \r\n

        return adapted1.toString();
    }

    private String getText(File f) throws Exception {

        StringBuilder sb = new StringBuilder();

        BufferedReader bis = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-16"));

        String line = "";
        boolean eof;
        do {
            sb.append(line);
            sb.append("\r\n");
            line = bis.readLine();
            eof = line == null;
        } while (!eof);

        bis.close();

        return sb.toString();
    }

}
