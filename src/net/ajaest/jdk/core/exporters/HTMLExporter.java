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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import net.ajaest.jdk.core.stylers.KanjiFlashCardStyler;
import net.ajaest.jdk.core.stylers.KanjiTableStyler;
import net.ajaest.jdk.core.stylers.Styler;
import net.ajaest.jdk.data.kanji.KanjiTag;

//TODO: JavaDoc
public class HTMLExporter extends Exporter<KanjiTag> {

    private static final long serialVersionUID = -5614957347001414429L;
    private Map<String, String> messages;
    private boolean xhtml;
    private boolean activateAutoExtension;

    public HTMLExporter(String lang) {
        super(lang);
        super.stylers.add(new KanjiTableStyler(lang));
        super.stylers.add(new KanjiFlashCardStyler(lang));
        this.xhtml = false;
        this.activateAutoExtension = true;
    }

    @Override
    public Boolean export(List<KanjiTag> list) {

        boolean success = true;
        StringBuilder document = new StringBuilder("");

        try{
            if (this.getExportFile() != null) {
                // Building document
                Styler<KanjiTag> selectedStyler = this.getStylers().get(this.getSelectedStylerIndex());


                if (!this.xhtml) {
                    if ((this.getExportFile() != null) && !this.getExportFile().getName().endsWith(".html") && this.activateAutoExtension)
                        this.setExportFile(new File(this.getExportFile().getAbsolutePath() + ".html"));

                    document.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n");
                    document.append("<html>\n");
                    document.append("<head>\n");
                    document.append("\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\">\n");
                    document.append("\t<meta name=\"generator\" content=\"JavaDiKt 1.1.5 or above, www.javadikt.net\">\n");
                    document.append("\t<meta name=\"description\" content=\"" + selectedStyler.getName() + "\">\n");
                    document.append("\t<meta name=\"language\" content=\"" + this.getLanguaje() + "\">\n");
                } else {
                    if ((this.getExportFile() != null) && !this.getExportFile().getName().endsWith(".xhtml") && this.activateAutoExtension)
                        this.setExportFile(new File(this.getExportFile().getAbsolutePath() + ".xhtml"));

                    document.append("<?xml version=\"1.0\" encoding=\"utf-16\"?>");
                    document.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\" >");
                    document.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
                    document.append("<head>\n");
                    document.append("\t<meta name=\"generator\" content=\"JavaDiKt 1.1.5 or above, www.javadikt.net\" />\n");
                    document.append("\t<meta name=\"description\" content=\"" + selectedStyler.getName() + "\" />\n");
                    document.append("\t<meta name=\"language\" content=\"" + this.getLanguaje() + "\" />\n");
                }

                String snnipet = new String(selectedStyler.styler(list), Charset.forName("UTF-16"));
                document.append(snnipet);
                document.append("</body>\n");
                document.append("</html>\n");

                // Exporting

                BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getExportFile()), "UTF-16"));
                fw.write(document.toString());// FIXME: Can cause heap/stack memory errors if the document is big
                fw.flush();
                fw.close();

            } else
                throw new FileNotFoundException("export File was not specified");

        } catch (Exception e) {
            success = false;
            this.setExportException(e);
        }

        return success;
    }

    @Override
    public FileFilter getFileChooserFilters() {

        return new FileFilter() {

            @Override
            public String getDescription() {
                return HTMLExporter.this.messages.get("FILE_DES");
            }

            @Override
            public boolean accept(File f) {

                boolean accept;

                if (!HTMLExporter.this.xhtml)
                    accept = f.getName().endsWith(".html");
                else
                    accept = f.getName().endsWith(".xhtml");

                accept = (accept || f.isDirectory()) && !f.getName().equals(".") && !f.getName().equals("..");

                return accept;
            }
        };
    }

    @Override
    public String getName() {

        return this.getMessage("NAME");
    }

    @Override
    protected void initMessages(String lang) {
        this.messages = new HashMap<String, String>();
        if (lang.equals("es")) {
            this.messages.put("NAME", "HTML");
            this.messages.put("FILE_DES", "Archivo HTML");
            this.messages.put("WINNAME", "Exportar como HTML - Más...");
        } else {
            super.languaje = "en";
            this.messages.put("NAME", "HTML");
            this.messages.put("FILE_DES", "HTML File");
            this.messages.put("WINNAME", "Export as HTML - More...");
        }
    }



    @Override
    public String getMessage(String key) {
        return this.messages.get(key);
    }

    @Override
    public void setDialogParent(JFrame dialogParent) {
        super.setDialogParent(dialogParent);
        this.initDialog();
    }

    public boolean isXhtml() {
        return this.xhtml;
    }

    public void setXhtml(boolean xhtml) {
        this.xhtml = xhtml;
    }

    public boolean isActivateAutoExtension() {
        return this.activateAutoExtension;
    }

    public void setActivateAutoExtension(boolean activateAutoExtension) {
        this.activateAutoExtension = activateAutoExtension;
    }
}
