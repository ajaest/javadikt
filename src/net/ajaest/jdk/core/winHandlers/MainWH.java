//Copyright (C) 2010  Luis Alfonso Arce González, ajaest[@]gmail.com
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

package net.ajaest.jdk.core.winHandlers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import net.ajaest.jdk.core.exporters.Exporter;
import net.ajaest.jdk.core.main.ExceptionHandler;
import net.ajaest.jdk.core.main.ExceptionHandler.KNOWN_EXCEPTIONS;
import net.ajaest.jdk.core.main.JDKGUIEngine;
import net.ajaest.jdk.core.main.Launcher;
import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.core.stylers.Styler;
import net.ajaest.jdk.data.auxi.KanjiComparator;
import net.ajaest.jdk.data.auxi.KanjiEnums;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiDicsEnum;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiFieldEnum;
import net.ajaest.jdk.data.auxi.KanjiEnums.KanjiReadingTypesEnum;
import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.dict.query.KanjiExpression;
import net.ajaest.jdk.data.dict.query.KanjiQuery;
import net.ajaest.jdk.data.dict.sort.KanjiOrdering;
import net.ajaest.jdk.data.dict.sort.KanjiSortExpression;
import net.ajaest.jdk.data.dict.sort.NumberOrderDef;
import net.ajaest.jdk.data.dict.sort.StringOrderDef;
import net.ajaest.jdk.data.kanji.Kanji;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.gui.auxi.ExportJPanel;
import net.ajaest.jdk.gui.auxi.JPanelFactory;
import net.ajaest.jdk.gui.dialogs.ExportSorterAdderDialog;
import net.ajaest.jdk.gui.dialogs.FileExplorerDialog;
import net.ajaest.jdk.gui.windows.MainWindow;
import net.ajaest.lib.data.Pair;
import net.ajaest.lib.swing.panel.JDrawPanel;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;
import net.ajaest.lib.swing.util.FixedTableModel;
import net.ajaest.lib.swing.util.JLineDraw;
import net.ajaest.lib.swing.window.LoadingDialog;
import net.ajaest.lib.utils.RowColorCellRenderer;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

//TODO: JavaDoc
public class MainWH implements WinHandler {

    public static void main(String... args) {

        Launcher.main();
    }
    // GUI engine
    private final JDKGUIEngine jdkGui;

    private MainWindow mw = null;
    // Flags and state
    private Boolean showingKanjiInfo = false;
    private Boolean modifingQuery = false;

    private Boolean sortersModified = true;
    private Integer selectedTab = null;
    private static final Integer TAB_DRAW = 0;
    private static final Integer TAB_CRITERIA = 1;
    @SuppressWarnings("unused")
    private static final Integer TAB_EXPORT = 2;

    @SuppressWarnings("unused")
    private static final Integer TAB_OPTIONS = 3;
    // Win panels
    private JPanel resultPanel = null;
    private JPanel exportResultPanel = null;

    private ExportJPanel exportGridExportJPanel = null;
    // Win properties
    private static final Color TABLE_PRIMARY_BACKGROUNG_COLOR = Color.WHITE;
    private static final Color TABLE_SECONDARY_BACKGROUNG_COLOR = Color.LIGHT_GRAY;

    private static final Dimension fieldInfoDialogDimension = new Dimension(700, 400);
    private FixedTableModel queryTableModel = null;

    private RowColorCellRenderer queryTableCellRenderer = null;

    private FixedTableModel exportSortingTableModel = null;
    private List<KanjiTag> exportSortedList;
    private List<KanjiFieldEnum> exportInvolvedFields;

    private List<Object> exportInvolvedValues;

    public MainWH(JDKGUIEngine jdgGui) {
        this.jdkGui = jdgGui;

        this.preAssignInitialization();
        this.mw = new MainWindow(this);
        this.posAssignInitialization();
    }

    public void _criteriaTabQueryMaker_InfoButtonPressed() {

        String selectedComboBoxMessage = this.getSelectedMessageFromFieldComboBox();
        String infoMessageKey = "INFO_" + Messages.getKeyInList(selectedComboBoxMessage, Messages.LIST_CRITERIA_FIELD_EXTENDED);
        String infoMessage = Messages.get(infoMessageKey);
        Messages.localePrintln("**MainWH: retrieving info message ,\"%s\"", infoMessageKey);
        this.jdkGui.getInfoDialgosEngine().invokeBorderedInfoDialog(selectedComboBoxMessage, infoMessage, fieldInfoDialogDimension, Messages.get(Messages.INFODIALOG_infoBorderText), true);
    }
    public void _exportTabAs_formatComboBoxSelectionModified() {
        this.mw._getExportTabAs_StyleComboBox().setModel(this.getExportStyleModel());
        this.mw._getExportTabAs_StyleComboBox().setSelectedIndex(this.getSelectedExporter().getSelectedStylerIndex());
    }

    public void _exportTabAs_styleComboBoxSelectionModified() {

        String selectedStyleName = (String) this.mw._getExportTabAs_StyleComboBox().getSelectedItem();

        Exporter<KanjiTag> selectedExporter = this.getSelectedExporter();

        // TODO: chapuza
        for (int i = 0; i < selectedExporter.getStylers().size(); i++)
            if (selectedExporter.getStylers().get(i).getName().equals(selectedStyleName)) {
                selectedExporter.setSelectedStyler(i);
                break;
            }
    }

    public void addAndQueryButtonPressed() {

        String field = null;
        String fulfils = null;
        String value = null;
        String andOrString = null;

        field = this.getSelectedMessageFromFieldComboBox();
        fulfils = this.getSelectedMessageFromFulfilsComboBox();
        value = this.getSelectedMessageFromValueComboBox();

        if ((value != null) && !value.equals("") && this.checkField(field, value)) {
            if (!this.modifingQuery) {

                Integer rowCount = this.queryTableModel.getRowCount();

                if (rowCount == 0) {
                    this.mw._getCriteriaTabQueryMaker_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAndText));
                    this.mw._getCriteriaTabQueryMaker_OrButton().setEnabled(true);
                    this.mw._getDrawTabAndOr_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAndText));
                    this.mw._getDrawTabAndOr_OrButton().setEnabled(true);

                    andOrString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellFirstString);
                } else {
                    andOrString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellAndString);
                }

                this.queryTableModel.addRow(new Object[]{andOrString, field, fulfils, value});

            } else if (this.modifingQuery) {

                Integer selectedRow = null;
                JTable currentTable = null;

                if (this.selectedTab == TAB_DRAW)
                    currentTable = this.mw._getDrawTab_QueryTable();
                else if (this.selectedTab == TAB_CRITERIA)
                    currentTable = this.mw._getCriteriaTabQuery_QueryTable();

                selectedRow = currentTable.getSelectedRow();

                if (selectedRow == 0) {

                    andOrString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellFirstString);

                    if (this.selectedTab == TAB_DRAW) {
                        this.mw._getDrawTabAndOr_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAndText));
                        this.mw._getDrawTabAndOr_OrButton().setEnabled(true);
                    } else if (this.selectedTab == TAB_CRITERIA) {
                        this.mw._getCriteriaTabQueryMaker_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAndText));
                        this.mw._getCriteriaTabQueryMaker_OrButton().setEnabled(true);
                    }
                } else
                    andOrString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellAndString);;

                    this.queryTableModel.setValueAt(andOrString, selectedRow, 0);
                    this.queryTableModel.setValueAt(field, selectedRow, 1);
                    this.queryTableModel.setValueAt(fulfils, selectedRow, 2);
                    this.queryTableModel.setValueAt(value, selectedRow, 3);

                    this.modifingQuery = false;
                    this.setDisableForModifing(true);
            }

            this.mw._getCriteriaTabQuery_QueryTable().repaint();
            this.mw._getDrawTab_QueryTable().repaint();
        }
    }

    public void changeExportPath() {
        // Maybe this functionality must be handled by exports dialogs engine
        int selectedExporter = this.mw._getExportTabAs_FormatComboBox().getSelectedIndex();
        Exporter<KanjiTag> exporter = this.jdkGui.getJavaDikt().getExporters().get(selectedExporter);

        FileFilter exporterFilters = exporter.getFileChooserFilters();

        FileExplorerDialog fed = new FileExplorerDialog(exporterFilters, this.mw, true);
        fed.setLocale(new Locale(this.jdkGui.getOptions().getLanguage()));

        int retValue = fed.showOpenDialog();

        if (retValue == JFileChooser.APPROVE_OPTION) {
            File exportTo = fed.getFileChooser().getSelectedFile();
            this.mw._getExportTabAs_PathTextField().setText(exportTo.getPath());
            this.mw._getExportTabPanel().updateUI();
        }

    }

    private boolean checkField(String message, String value) {

        // TODO: replace using TextComponent validators

        boolean passCheck = true;

        String key = Messages.getKeyInList(message, Messages.LIST_CRITERIA_FIELD_EXTENDED);

        switch (KanjiEnums.getFieldType(key)) {
        case INTEGER :
        case INTEGER_LIST :

            if (key.equals("KANJI_CLASSIC_RADICAL")) {

                passCheck = true;

            } else if (key.equals(Messages.KANJI_LITERAL)) {

                int codePoint = Character.codePointAt(value.toCharArray(), 0);
                passCheck = Kanji.isKanji(codePoint) && (value.length() == 1);

                if (!passCheck)
                    ExceptionHandler.handleException(new IllegalArgumentException(), KNOWN_EXCEPTIONS.NOT_A_KANJI);
            } else if (key.equals("KANJI_UNICODE_VALUE")) {

                try {
                    Integer.valueOf(value, 16);
                } catch (NumberFormatException e) {
                    passCheck = false;
                    ExceptionHandler.handleException(new NumberFormatException(), KNOWN_EXCEPTIONS.IS_NOT_HEX);
                }

            } else {

                try {
                    new Integer(value);
                } catch (NumberFormatException e) {
                    passCheck = false;
                    ExceptionHandler.handleException(new NumberFormatException(), KNOWN_EXCEPTIONS.NUMBER_CAST_EXCEPTION);
                }
            }
            break;
        case STRING :
            if (key.equals(Messages.KANJI_GRAPH)) {
                String astGraphStr = new String(value);
                passCheck = Character.isUpperCase(astGraphStr.toCharArray()[0]);
                astGraphStr = astGraphStr.toLowerCase();
                astGraphStr = astGraphStr.replaceAll("[abcdefghi]", "");
                passCheck = passCheck && (astGraphStr.length() == 0);
                if (!passCheck) {
                    ExceptionHandler.handleException(new IllegalArgumentException(), KNOWN_EXCEPTIONS.GRAPH_CAST_EXCEPTION);
                }

            } else {
                passCheck = true;
            }
            break;
        default :
            passCheck = true;
            break;
        }

        return passCheck;
    }
    private KanjiSortExpression createExportSortExpression() {

        KanjiSortExpression kse = null;

        if (this.exportSortingTableModel.getRowCount() == 0) {
            kse = new KanjiOrdering().sort_by_frequency().from_least_to_greatest();
        } else {

            KanjiOrdering ko = new KanjiOrdering();

            String fieldKey;
            String value = null;
            String valueKey;
            String orderByKey;
            for (int i = 0; i < this.exportSortingTableModel.getRowCount(); i++) {
                // _____
                fieldKey = (String) this.exportSortingTableModel.getValueAt(i, 2);
                String[] splitValue = fieldKey.split(" - ");
                if (splitValue.length == 2) {
                    fieldKey.replace("ORDER_", "KANJI_");
                    value = splitValue[1];
                }

                fieldKey = Messages.getKeyInList(splitValue[0], Messages.LIST_ORDENABLE_FIELDS);
                // _____

                orderByKey = (String) this.exportSortingTableModel.getValueAt(i, 1);
                switch (KanjiEnums.getFieldType(fieldKey.replace("ORDER_", "KANJI_"))) {
                case INTEGER :
                    orderByKey = Messages.getKeyInList(orderByKey, Messages.LIST_ORDENABLE_METHODS_INTEGER);
                    break;
                case STRING :
                    orderByKey = Messages.getKeyInList(orderByKey, Messages.LIST_ORDENABLE_METHODS_STRING);
                    break;
                }
                // _____

                if (fieldKey.equals(Messages.ORDER_CLASSIC_NELSON)) {
                    kse = this.selectOrderBy(ko.sort_by_Nelson_radical(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_CLASSIC_RADICAL)) {
                    kse = this.selectOrderBy(ko.sort_by_classic_radical(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_DE_ROO)) {
                    kse = this.selectOrderBy(ko.sort_by_De_Roo_code(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_DIC_INDEX)) {
                    valueKey = Messages.getKeyInList(value, Messages.LIST_CRITERIA_VALUES_DIC);
                    KanjiDicsEnum kde = KanjiDicsEnum.valueOf(valueKey);
                    kse = this.selectOrderBy(ko.sort_by_dictionary_reference().dic_enum_equals(kde), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_FOUR_CORNER)) {
                    kse = this.selectOrderBy(ko.sort_by_four_corner_code(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_FREQUENCY)) {
                    kse = this.selectOrderBy(ko.sort_by_frequency(), orderByKey);
                } else if (fieldKey.equals(Messages.ORDER_GRADE)) {
                    kse = this.selectOrderBy(ko.sort_by_grade(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_GRAPH)) {
                    kse = this.selectOrderBy(ko.sort_by_graph(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_JIS_CHARSET)) {
                    kse = this.selectOrderBy(ko.sort_by_JIS_charset(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_JIS_CODE)) {
                    kse = this.selectOrderBy(ko.sort_by_JIS_code(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_MEANING)) {
                    ISO639ー1 lang = ISO639ー1.valueOf(this.jdkGui.getOptions().getLanguage().toUpperCase());
                    kse = this.selectOrderBy(ko.sort_by_first_meaning().languaje_enum_equals(lang), orderByKey);
                } else if (fieldKey.equals(Messages.ORDER_READING)) {
                    valueKey = Messages.getKeyInList(value, Messages.LIST_CRITERIA_VALUES_READING_EXTENDED);
                    KanjiReadingTypesEnum kre = KanjiReadingTypesEnum.valueOf(valueKey);
                    kse = this.selectOrderBy(ko.sort_by_first_reading().reading_enum_equals(kre), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_SKIP)) {
                    kse = this.selectOrderBy(ko.sort_by_SKIP_code(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_SPAHN_HADAMITZKY)) {
                    kse = this.selectOrderBy(ko.sort_by_SpahnーHadamitzky_code(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_STROKE_COUNT)) {
                    kse = this.selectOrderBy(ko.sort_by_stroke_count(), orderByKey);
                    ko = kse.and_if_equals();
                } else if (fieldKey.equals(Messages.ORDER_UNICODE_VALUE)) {
                    kse = this.selectOrderBy(ko.sort_by_Unicode(), orderByKey);
                    ko = kse.and_if_equals();
                }

                // _____
            }
        }

        return kse;
    }

    public void createResultPanels(List<Integer> kt) {

        JScrollPane resultPanelScroll = JPanelFactory.createKanjiGrid(this, kt);
        this.resultPanel = new JPanel();
        this.resultPanel.setLayout(new BorderLayout());
        this.resultPanel.add(this.mw._getResultControlPanel(), BorderLayout.SOUTH);
        this.resultPanel.add(resultPanelScroll, BorderLayout.CENTER);

        JScrollPane exportScroll = JPanelFactory.createToggleKanjiGrid(this, kt);
        this.exportGridExportJPanel = (ExportJPanel) exportScroll.getViewport().getComponent(0);
        this.exportResultPanel = new JPanel();
        this.exportResultPanel.setLayout(new BorderLayout());
        this.exportResultPanel.add(this.mw._getResultExportControlPanel(), BorderLayout.SOUTH);
        this.exportResultPanel.add(exportScroll, BorderLayout.CENTER);
    }

    /**
     * Contains workarond for bug <a href="https://forja.rediris.es/tracker/index.php?func=detail&aid=639&group_id=818&atid=3308">661</a>.
     * @see JDrawPanel
     */
    public void drawAndButtonPressed() {

        if (!this.mw._getDrawTabDrawer_KanjiDrawPanel().isMouseClicked()) {

            String field = null;
            String fulfils = null;
            String value = null;
            String andOrString = null;

            KanjiGraph kg;
            List<KanjiStroke> ks = new ArrayList<KanjiStroke>();
            JLineDraw draw = this.mw._getDrawTabDrawer_KanjiDrawPanel().getJLineDraw();

            if (draw != null) {
                List<AllowedStrokeLineEnum> drawnAsterisk = this.mw._getDrawTabDrawer_KanjiDrawPanel().getJLineDraw().getAsteriskModel();

                // TODO: implement stroke clues
                int order = 1;
                List<AllowedStrokeLineEnum> strokeList;
                for (Pair<Integer, Integer> stroke : draw.getStrokes()) {

                    strokeList = new ArrayList<AllowedStrokeLineEnum>();

                    for (int i = stroke.getFirst(); i <= stroke.getSecond(); i++) {

                        /**
                         * FIXME: WORKAROUND, removed E strokes from drawn
                         * asterisk. E strokes aren't really used in database
                         */
                        if(!drawnAsterisk.get(i).equals(AllowedStrokeLineEnum.E))
                            strokeList.add(drawnAsterisk.get(i));
                    }

                    ks.add(new KanjiStroke(strokeList, order, -1));
                    order++;
                }

                kg = new KanjiGraph(ks, null, -1);

                field = this.getMessage(Messages.KANJI_GRAPH);
                // only equals is allowed
                fulfils = this.getMessage(Messages.CASE_EQUALS);
                value = kg.toString();

                Integer rowCount = this.queryTableModel.getRowCount();

                if (rowCount == 0) {
                    this.mw._getCriteriaTabQueryMaker_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAndText));
                    this.mw._getCriteriaTabQueryMaker_OrButton().setEnabled(true);
                    this.mw._getDrawTabAndOr_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAndText));
                    this.mw._getDrawTabAndOr_OrButton().setEnabled(true);

                    andOrString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellFirstString);
                } else {
                    andOrString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellAndString);
                }

                this.queryTableModel.addRow(new Object[]{andOrString, field, fulfils, value});

                this.mw._getCriteriaTabQuery_QueryTable().repaint();
                this.mw._getDrawTab_QueryTable().repaint();
            }
        }
    }

    public void drawOrButtonPressed() {

        if (!this.mw._getDrawTabDrawer_KanjiDrawPanel().isMouseClicked()) {

            String field = null;
            String fulfils = null;
            String value = null;
            String andOrString = null;

            KanjiGraph kg;
            List<KanjiStroke> ks = new ArrayList<KanjiStroke>();
            JLineDraw draw = this.mw._getDrawTabDrawer_KanjiDrawPanel().getJLineDraw();
            if (draw != null) {
                List<AllowedStrokeLineEnum> drawnAsterisk = this.mw._getDrawTabDrawer_KanjiDrawPanel().getJLineDraw().getAsteriskModel();

                // TODO: implement stroke clues
                int order = 1;
                List<AllowedStrokeLineEnum> strokeList;
                for (Pair<Integer, Integer> stroke : draw.getStrokes()) {

                    strokeList = new ArrayList<AllowedStrokeLineEnum>();

                    for (int i = stroke.getFirst(); i <= stroke.getSecond(); i++) {
                        strokeList.add(drawnAsterisk.get(i));
                    }

                    ks.add(new KanjiStroke(strokeList, order, -1));
                    order++;
                }

                kg = new KanjiGraph(ks, null, -1);

                field = this.getMessage(Messages.KANJI_GRAPH);
                // only equals is allowed
                fulfils = this.getMessage(Messages.CASE_EQUALS);
                andOrString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellOrString);
                value = kg.toString();

                this.queryTableModel.addRow(new Object[]{andOrString, field, fulfils, value});

                this.mw._getCriteriaTabQuery_QueryTable().repaint();
                this.mw._getDrawTab_QueryTable().repaint();
            }
        }
    }

    public void drawRemoveLastLineButtonPressed() {

        if (this.mw._getDrawTabDrawer_KanjiDrawPanel().isMouseClicked())
            this.mw._getDrawTabDrawer_KanjiDrawPanel().unclickMouse();
        else
            this.mw._getDrawTabDrawer_KanjiDrawPanel().removeLastLine();
    }

    public void drawRemoveLastStrokeButtonPressed() {

        if (this.mw._getDrawTabDrawer_KanjiDrawPanel().isMouseClicked())
            this.mw._getDrawTabDrawer_KanjiDrawPanel().unclickMouse();

        this.mw._getDrawTabDrawer_KanjiDrawPanel().removeLastStroke();
    }

    public void drawResetButtonPressed() {

        if (this.mw._getDrawTabDrawer_KanjiDrawPanel().isMouseClicked())
            this.mw._getDrawTabDrawer_KanjiDrawPanel().unclickMouse();
        this.mw._getDrawTabDrawer_KanjiDrawPanel().reset();

        this.mw._getCriteriaTabQuery_QueryTable().repaint();
        this.mw._getDrawTab_QueryTable().repaint();
    }


    public void executeDrawQuery() {

        if (this.mw._getDrawTabDrawer_KanjiDrawPanel().isMouseClicked())
            this.mw._getDrawTabDrawer_KanjiDrawPanel().unclickMouse();

        this.setSorterListModified(true);

        this.executeQuery();
    }

    public void executeQuery() {

        if (this.queryTableModel.getRowCount() > 0) {
            // elaborates key string values. jdkgui translates it to query
            List<String> boolKeys = new ArrayList<String>();
            List<String> fieldKeys = new ArrayList<String>();
            List<String> caseKeys = new ArrayList<String>();
            List<Object> valueKeys = new ArrayList<Object>();

            List<String> tempList = null;
            Integer rowCount = this.queryTableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {

                // and/or column messages can have multiple keys
                tempList = Messages.getKeys((String) this.queryTableModel.getValueAt(i, 0));
                if (tempList.contains(Messages.WINMAIN_QUERY_DATA_cellFirstString) || tempList.contains(Messages.WINMAIN_QUERY_DATA_cellAndString))
                    boolKeys.add(Messages.WINMAIN_QUERY_DATA_cellAndString);
                else
                    boolKeys.add(Messages.WINMAIN_QUERY_DATA_cellOrString);

                fieldKeys.add(this.getSelectedKeyFromFieldComboBox((String) this.queryTableModel.getValueAt(i, 1)));
                caseKeys.add(Messages.getKey((String) this.queryTableModel.getValueAt(i, 2)));

                // jdkgui decodes the value literal
                valueKeys.add(this.queryTableModel.getValueAt(i, 3));

            }

            try {
                List<Integer> currentResults = this.jdkGui.executeRefQuery(boolKeys, fieldKeys, caseKeys, valueKeys);

                this.createResultPanels(currentResults);

                this.setResultPanelBordertText(this.getMessage(Messages.WINMAIN_RESULTS_BorderTextResults) + ": " + currentResults.size());

                this.setResultPanelToKanjiToInfoGrid();

            } catch (NumberFormatException e) {
                ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.NUMBER_CAST_EXCEPTION);

            }
        }
    }

    public void exportExportButtonPressed() {

        String path = this.getWindow()._getExportTabAs_PathTextField().getText();
        if ((path != null) && !path.equals("")) {

            final File exportFile = new File(path);

            ExportTask et = new ExportTask(this, exportFile);

            LoadingDialog ld = new LoadingDialog(this.getMessage(Messages.INFODIALOG_infoMessage_exporting),
                                                 this.getMessage(Messages.INFODIALOG_infoMessage_exportSuccess),
                                                 this.getMessage(Messages.INFODIALOG_infoMessage_openFile),
                                                 et,
                                                 this.getWindow());
            ld.setTitle(this.getMessage(Messages.INFODIALOG_title_info));
            ld.setUserImpatienceTitle(this.getMessage(Messages.INFODIALOG_title_pleaseWait));
            et.setLoadingDialog(ld);

            SwingUtilities.invokeLater(ld);
        }

    }

    private class ExportTask implements Runnable {

        private final MainWH mwh;
        private LoadingDialog ld;
        private final File f;

        private ExportTask(MainWH mwh,  File f){
            this.mwh = mwh;
            this.ld = null;
            this.f = f;
        }

        private void setLoadingDialog(LoadingDialog ld) {
            this.ld = ld;

        }

        @Override
        public void run() {

            final Exporter<KanjiTag> exporter;

            int exporterIdx = this.mwh.mw._getExportTabAs_FormatComboBox().getSelectedIndex();
            exporter = this.mwh.jdkGui.getJavaDikt().getExporters().get(exporterIdx);
            int stylerIdx = this.mwh.mw._getExportTabAs_StyleComboBox().getSelectedIndex();
            exporter.setSelectedStyler(stylerIdx);

            exporter.setExportFile(this.f);

            if (this.mwh.exportSortedList == null) {
                this.mwh.exportSortedList = new ArrayList<KanjiTag>();

                KanjiQuery q = new KanjiQuery();
                KanjiExpression ke = null;
                for (Integer kanjiRef : this.mwh.exportGridExportJPanel.getSelectedKanji()) {
                    ke = q.unicode_value().equal(kanjiRef);
                    q = ke.OR();
                }

                this.mwh.exportSortedList = this.mwh.jdkGui.getJavaDikt().executeQuery(ke,
                        this.mwh.createExportSortExpression());
            }

            exporter.export(this.mwh.exportSortedList);

            if (exporter.getExportException() != null) {
                this.ld.setFinishedText(String.format(Messages
                        .get(Messages.INFODIALOG_errorMessage_exportFail), exporter.getExportException()
                        .getLocalizedMessage()));
                this.ld.setFinishedTitle(Messages.get(Messages.INFODIALOG_title_error));
                this.ld.setAcceptButtonText(Messages.get(Messages.INFODIALOG_buttonAccept));
                ExceptionHandler.handleException(exporter.getExportException(),
                        KNOWN_EXCEPTIONS.EXPORT_IOEXCEPTION);
            } else {
                // If all right, register file opener
                this.ld.setAcceptAction(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Desktop.getDesktop().open(exporter.getExportFile());
                        } catch (Exception e) {
                            ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.CANNOT_OPEN_FILE_INSPECTOR);
                        }
                    }
                });
            }

            this.mwh.exportSortedList = null;
            this.mwh.getWindow()._getExportTabAs_PathTextField().setText(exporter.getExportFile().getAbsolutePath());


        }
    }

    public void exportMoreButtonPressed() {

        this.jdkGui.getExporDialogsWH().exportMoreButtonPressed();
    }

    public void exportRearrangeButtonPressed() {

        List<KanjiTag> selected = null;

        if (this.exportGridExportJPanel != null) {
            selected = new ArrayList<KanjiTag>();

            KanjiQuery q = new KanjiQuery();
            KanjiExpression ke = null;
            for (Integer kanjiRef : this.exportGridExportJPanel.getSelectedKanji()) {
                ke = q.unicode_value().equal(kanjiRef);
                q = ke.OR();
            }

            selected = this.jdkGui.getJavaDikt().executeQuery(ke);
        }

        if ((selected != null) && (selected.size() > 0)) {
            if (this.sortersModified) {

                this.exportSortedList = new LinkedList<KanjiTag>(selected);

                KanjiSortExpression kse = this.createExportSortExpression();

                if (kse != null) {
                    Collections.sort(this.exportSortedList, new KanjiComparator(kse));
                    this.exportSortedList = new ArrayList<KanjiTag>(this.exportSortedList);

                    this.exportInvolvedFields = kse.getInvolvedFieldsInOrdering();
                    this.exportInvolvedValues = kse.getInvolvedValuesInOrdering();
                } else {
                    this.exportInvolvedFields = new ArrayList<KanjiFieldEnum>();
                    this.exportInvolvedValues = new ArrayList<Object>();
                }

                this.jdkGui.getExporDialogsWH().getExportRearrangerWH().setSortedList(this.exportSortedList, this.exportInvolvedFields, this.exportInvolvedValues);

                this.sortersModified = false;
            }

            this.jdkGui.getExporDialogsWH().invokeRearrangeDialog();
        }
    }

    public void exportResultDisposeAllWindowsButtonPressed() {
        this.jdkGui.getKanjiInfoWinEngine().disposeAllWindows();
    }

    public void exportResultPanelInvertButtonPressed() {

        if (this.exportGridExportJPanel != null)
            this.exportGridExportJPanel.invertSelection();
        this.setSorterListModified(true);
    }

    public void exportResultPanelResetButtonPressed() {

        if (this.exportGridExportJPanel != null)
            this.exportGridExportJPanel.setAll(false);
        this.setSorterListModified(true);
    }

    public void exportResultPanelSelectAllButtonPressed() {

        if (this.exportGridExportJPanel != null)
            this.exportGridExportJPanel.setAll(true);
        this.setSorterListModified(true);
    }

    public void exportSorterAddButtonPressed() {
        this.jdkGui.getExporDialogsWH().invokeAddSorterDialog();
        this.setSorterListModified(true);
    }

    public void exportSorterRemoveButtonPressed() {

        int[] selectedRow = this.mw._getExportTabSorting_SortingTable().getSelectedRows();

        int lastRemovedRow = 0;

        for (int i = 0; i < selectedRow.length; i++) {
            // -i because when a row is deleted the next rows change their
            // position
            this.exportSortingTableModel.removeRow(selectedRow[i] - i);
            // We cannot expected that the rows are sorted
            if (selectedRow[i] > lastRemovedRow)
                lastRemovedRow = selectedRow[i] - i;
        }

        if (this.exportSortingTableModel.getRowCount() > lastRemovedRow)
            this.mw._getExportTabSorting_SortingTable().changeSelection(lastRemovedRow, 1, false, false);
        else
            this.mw._getExportTabSorting_SortingTable().changeSelection(this.exportSortingTableModel.getRowCount() - 1, 1, false, false);

        // level literals must be re-written

        for (int i = 0; i < this.exportSortingTableModel.getRowCount(); i++) {
            this.exportSortingTableModel.setValueAt(new Integer(i + 1).toString(), i, 0);
        }

        // if add sorter is visible level combo box must be modified

        ExportSorterAdderDialog esaD = this.jdkGui.getExporDialogsWH().getExportSorterAdderWH().getExportSorterAdderDialog();

        // if (esaD.isVisible()) {
        esaD.getLevelComboBox().setModel(this.jdkGui.getExporDialogsWH().getExportSorterAdderWH().getLevelComboBoxModel());
        // }

        esaD.getLevelComboBox().setSelectedIndex(this.exportSortingTableModel.getRowCount());

        this.mw._getExportTabSorting_SortingTable().repaint();
        this.setSorterListModified(true);
    }

    public void exportSorterResetButtonPressed() {
        this.mw._getExportTabSorting_SortingTable().setModel(this.getExportSorterTableModel());
        this.setSorterListModified(true);

    }

    public ComboBoxModel getCriteriaFieldComboBoxModel() {

        String[] comboMessages;
        if (this.jdkGui.getOptions().getExtendedInfo()) {
            comboMessages = Messages.getList(Messages.LIST_CRITERIA_FIELD_EXTENDED);
        } else
            comboMessages = Messages.getList(Messages.LIST_CRITERIA_FIELD_BASIC);

        return new DefaultComboBoxModel(comboMessages);
    }

    public ComboBoxModel getExportFormatModel() {

        String[] exporterNames = new String[this.jdkGui.getJavaDikt().getExporters().size()];

        for (int i = 0; i < this.jdkGui.getJavaDikt().getExporters().size(); i++) {

            exporterNames[i] = this.jdkGui.getJavaDikt().getExporters().get(i).getName();
        }

        ComboBoxModel cbm = new DefaultComboBoxModel(exporterNames);
        if (this.jdkGui.getJavaDikt().getExporters().size() > 0)
            cbm.setSelectedItem(this.jdkGui.getJavaDikt().getExporters().get(0).getName());

        return cbm;
    }

    public TableModel getExportSorterTableModel() {

        Object[] columnNames = new Object[3];
        columnNames[0] = this.getMessage(Messages.WINMAIN_EXPORT_SORTING_columnLevel);
        columnNames[1] = this.getMessage(Messages.WINMAIN_EXPORT_SORTING_columnOrderBy);
        columnNames[2] = this.getMessage(Messages.WINMAIN_EXPORT_SORTING_columnField);

        int rowCount = 0;

        this.exportSortingTableModel = new FixedTableModel(columnNames, rowCount);

        return this.exportSortingTableModel;
    }

    public ComboBoxModel getExportStyleModel() {

        String selectedExporterName = (String) this.mw._getExportTabAs_FormatComboBox().getSelectedItem();

        Exporter<KanjiTag> selectedExporter = null;
        for (Exporter<KanjiTag> exporter : this.jdkGui.getJavaDikt().getExporters()) {
            if (selectedExporterName.equals(exporter.getName())) {
                selectedExporter = exporter;
                break;
            }
        }

        String[] stylerNames = new String[selectedExporter.getStylers().size()];

        int i = 0;
        for (Styler<KanjiTag> kt : selectedExporter.getStylers()) {
            stylerNames[i] = kt.getName();
            i++;
        }

        return new DefaultComboBoxModel(stylerNames);
    }

    public List<? extends Image> getIcons() {

        return this.jdkGui.getIcons();
    }

    public JDKGUIEngine getJDKGuiEngine() {

        return this.jdkGui;
    }

    public String getMessage(String message) {

        return Messages.get(message);
    }

    public ComboBoxModel getOptionLanguageComboBoxModel() {

        return new DefaultComboBoxModel(Messages.getList(Messages.LIST_LANGUAGES_SUPPORTED));
    }

    public TableCellRenderer getQueryTableCellRenderer() {

        return this.queryTableCellRenderer;
    }

    public TableModel getQueryTableModel() {

        return this.queryTableModel;
    }


    private Exporter<KanjiTag> getSelectedExporter() {
        String selectedExporterName = (String) this.mw._getExportTabAs_FormatComboBox().getSelectedItem();

        Exporter<KanjiTag> selectedExporter = null;

        // TODO: chapuza
        for (Exporter<KanjiTag> exp : this.jdkGui.getJavaDikt().getExporters())
            if (exp.getName().equals(selectedExporterName)) {
                selectedExporter = exp;
                break;
            }
        return selectedExporter;
    }

    private String getSelectedKeyFromFieldComboBox(String selectedMessage) {

        List<String> keys = Messages.getKeys(selectedMessage);
        String selectedKey = "";


        for (String key : keys) {
            if (key.contains("KANJI_")) {
                selectedKey = key;
                break;
            }
        }

        return selectedKey;
    }

    private String getSelectedMessageFromFieldComboBox() {

        int selectedItemIndex = this.mw._getCriteriaTabQueryMaker_FieldComboBox().getSelectedIndex();

        String[] currentBoxList;
        if (this.jdkGui.getOptions().getExtendedInfo())
            currentBoxList = Messages.getList(Messages.LIST_CRITERIA_FIELD_EXTENDED);
        else
            currentBoxList = Messages.getList(Messages.LIST_CRITERIA_FIELD_BASIC);

        return currentBoxList[selectedItemIndex];
    }

    private String getSelectedMessageFromFulfilsComboBox() {

        String fieldSelectedMessage = this.getSelectedMessageFromFieldComboBox();
        String fieldKey = this.getSelectedKeyFromFieldComboBox(fieldSelectedMessage);

        String[] currentBoxList = null;
        switch (KanjiEnums.getFieldType(fieldKey)) {
        case INTEGER :
        case ORDENABLE_STRING :
            currentBoxList = Messages.getList(Messages.LIST_CRITERIA_CASES_INT);
            break;
        case STRING :
            currentBoxList = Messages.getList(Messages.LIST_CRITERIA_CASES_STRING);
            break;
        }

        return currentBoxList[this.mw._getCriteriaTabQueryMaker_FulfilsComboBox().getSelectedIndex()];
    }

    private String getSelectedMessageFromValueComboBox() {

        String valueSelectedMessage = null;

        if (this.mw._getCriteriaTabQueryMaker_ValueComboBox().isEditable()) {

            valueSelectedMessage = (String) this.mw._getCriteriaTabQueryMaker_ValueComboBox().getSelectedItem();
        } else {


            int selectedValueIndex = this.mw._getCriteriaTabQueryMaker_ValueComboBox().getSelectedIndex();

            String selectedKey;

            if (this.jdkGui.getOptions().getExtendedInfo()) {
                selectedKey = Messages.getKeyInList(this.getSelectedMessageFromFieldComboBox(), Messages.LIST_CRITERIA_FIELD_EXTENDED);
            } else {
                selectedKey = Messages.getKeyInList(this.getSelectedMessageFromFieldComboBox(), Messages.LIST_CRITERIA_FIELD_BASIC);
            }

            if (selectedKey.equals(Messages.KANJI_DIC_NAME))
                valueSelectedMessage = Messages.getList(Messages.LIST_CRITERIA_VALUES_DIC)[selectedValueIndex];
            else if (selectedKey.startsWith("KANJI_CLASSIC_RADICAL"))
                valueSelectedMessage = Messages.getList(Messages.LIST_CRITERIA_VALUES_RADICALS)[selectedValueIndex];
            else if (selectedKey.equals(Messages.KANJI_JIS_CHARSET))
                valueSelectedMessage = Messages.getList(Messages.LIST_CRITERIA_VALUES_JIS)[selectedValueIndex];
            else if (selectedKey.equals(Messages.KANJI_READING_TYPE)) {
                if (this.jdkGui.getOptions().getExtendedInfo())
                    valueSelectedMessage = Messages.getList(Messages.LIST_CRITERIA_VALUES_READING_EXTENDED)[selectedValueIndex];
                else
                    valueSelectedMessage = Messages.getList(Messages.LIST_CRITERIA_VALUES_READING_BASIC)[selectedValueIndex];
            }
        }

        return valueSelectedMessage;
    }

    @Override
    public Font getUnicodeFont() {

        return this.jdkGui.getUnicodeFont();
    }

    public MainWindow getWindow() {
        return this.mw;
    }

    public void invokeKanjiInfoWindow(Integer kanjiRef) {
        this.jdkGui.getKanjiInfoWinEngine().invokeWindow(kanjiRef);
    }

    public void kanjiFileChoose() {

        FileFilter kanjiDictFileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {

                return f.isDirectory() || (f.isFile() && f.getName().endsWith(".jdk"));
            }
            @Override
            public String getDescription() {
                return Messages.get(Messages.FILE_DESCRIPTION_JDK_KANJI_FILE);
            }
        };

        FileExplorerDialog fed = new FileExplorerDialog(kanjiDictFileFilter, this.mw, true);
        fed.setLocale(new Locale(this.jdkGui.getOptions().getLanguage()));

        int retValue = fed.showOpenDialog();

        if (retValue == JFileChooser.APPROVE_OPTION) {
            File kanjiDict = fed.getFileChooser().getSelectedFile();
            this.mw._getOptionsTab_KanjiDictPathTextField().setText(kanjiDict.getAbsolutePath());
            this.mw._getOptionsTabPanel().updateUI();
        }
    }

    public void optionsAboutButtonPressed() {
        this.jdkGui.getAboutWH().invokeAboutWindow();
    }

    public void orQueryButtonPressed() {

        String field = null;
        String fulfils = null;
        String value = null;
        String andOrString = null;

        andOrString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellOrString);
        field = this.getSelectedMessageFromFieldComboBox();
        fulfils = this.getSelectedMessageFromFulfilsComboBox();
        value = this.getSelectedMessageFromValueComboBox();

        if ((value != null) && !value.equals("") && this.checkField(field, value)) {
            if (!this.modifingQuery) {
                this.queryTableModel.addRow(new Object[]{andOrString, field, fulfils, value});
            } else if (this.modifingQuery) {

                Integer selectedRow = null;
                JTable currentTable = null;

                if (this.selectedTab == TAB_DRAW)
                    currentTable = this.mw._getDrawTab_QueryTable();
                else if (this.selectedTab == TAB_CRITERIA)
                    currentTable = this.mw._getCriteriaTabQuery_QueryTable();

                selectedRow = currentTable.getSelectedRow();

                this.queryTableModel.setValueAt(andOrString, selectedRow, 0);
                this.queryTableModel.setValueAt(field, selectedRow, 1);
                this.queryTableModel.setValueAt(fulfils, selectedRow, 2);
                this.queryTableModel.setValueAt(value, selectedRow, 3);

                this.modifingQuery = false;
                this.setDisableForModifing(true);
            }

            this.mw._getCriteriaTabQuery_QueryTable().repaint();
            this.mw._getDrawTab_QueryTable().repaint();
        }
    }

    private void posAssignInitialization() {

        // pos window asignation init: query comboboxes
        this.mw._getCriteriaTabQueryMaker_FieldComboBox().setSelectedIndex(0);
    }

    private void preAssignInitialization() {
        this.selectedTab = 0;

        Object[] columnNames = new Object[4];
        columnNames[0] = this.getMessage(Messages.WINMAIN_QUERY_DATA_columnAndOr);
        columnNames[1] = this.getMessage(Messages.WINMAIN_QUERY_DATA_columnField);
        columnNames[2] = this.getMessage(Messages.WINMAIN_QUERY_DATA_columnThatFulfilsThat);
        columnNames[3] = this.getMessage(Messages.WINMAIN_QUERY_DATA_columnValue);

        int rowCount = 0;
        this.queryTableModel = new FixedTableModel(columnNames, rowCount);

        String orString = this.getMessage(Messages.WINMAIN_QUERY_DATA_cellOrString);
        this.queryTableCellRenderer = new RowColorCellRenderer(this.queryTableModel, TABLE_PRIMARY_BACKGROUNG_COLOR, TABLE_SECONDARY_BACKGROUNG_COLOR, 0, orString);
    }

    public void QueryModifierModifyButtonPressed() {

        Integer selectedRow = null;
        JTable currentTable = null;

        if (this.selectedTab == TAB_DRAW)
            currentTable = this.mw._getDrawTab_QueryTable();
        else if (this.selectedTab == TAB_CRITERIA)
            currentTable = this.mw._getCriteriaTabQuery_QueryTable();


        selectedRow = currentTable.getSelectedRow();

        // Changes the selection in order to mark only the row that is being
        // modified
        currentTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
        currentTable.updateUI();
        // TODO: you can do much better!
        if (selectedRow != -1) {

            if (selectedRow == 0) {
                if (this.selectedTab == TAB_DRAW) {
                    // No "and" or "or" for first column is allowed
                    this.mw._getDrawTabAndOr_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerChangeText));
                    this.mw._getDrawTabAndOr_OrButton().setEnabled(false);

                } else if (this.selectedTab == TAB_CRITERIA) {
                    // No "and" or "or" for first column is allowed
                    this.mw._getCriteriaTabQueryMaker_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerChangeText));
                    this.mw._getCriteriaTabQueryMaker_OrButton().setEnabled(false);
                }
            }

            if (this.modifingQuery) {
                this.setDisableForModifing(true);
                // restores and/or buttons
                if (selectedRow == 0) {
                    if (this.selectedTab == TAB_DRAW) {
                        // No "and" or "or" for first column is allowed
                        this.mw._getDrawTabAndOr_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAndText));
                        this.mw._getDrawTabAndOr_OrButton().setEnabled(true);

                    } else if (this.selectedTab == TAB_CRITERIA) {
                        // No "and" or "or" for first column is allowed
                        this.mw._getCriteriaTabQueryMaker_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAndText));
                        this.mw._getCriteriaTabQueryMaker_OrButton().setEnabled(true);
                    }
                }
            } else
                this.setDisableForModifing(false);

            this.modifingQuery = !this.modifingQuery;
        }

        this.mw._getCriteriaTabQuery_QueryTable().repaint();
        this.mw._getDrawTab_QueryTable().repaint();
    }

    public void QueryModifierRemoveButtonPressed() {

        int[] selectedRow = null;
        JTable currentTable = null;

        if (this.selectedTab == TAB_DRAW)
            currentTable = this.mw._getDrawTab_QueryTable();
        else if (this.selectedTab == TAB_CRITERIA)
            currentTable = this.mw._getCriteriaTabQuery_QueryTable();

        selectedRow = currentTable.getSelectedRows();

        int lastRemovedRow = 0;

        for(int i=0; i<selectedRow.length; i++){
            // -i because when a row is deleted the next rows change their
            // position
            this.queryTableModel.removeRow(selectedRow[i] - i);
            // We cannot expected that the rows are sorted
            if (selectedRow[i] > lastRemovedRow)
                lastRemovedRow = selectedRow[i]-i;
        }

        if (this.queryTableModel.getRowCount() > lastRemovedRow)
            currentTable.changeSelection(lastRemovedRow, 1, false, false);
        else
            currentTable.changeSelection(currentTable.getRowCount() - 1, 1, false, false);

        if (this.queryTableModel.getRowCount() == 0) {
            this.mw._getCriteriaTabQueryMaker_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAddText));
            this.mw._getCriteriaTabQueryMaker_OrButton().setEnabled(false);
            this.mw._getDrawTabAndOr_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAddText));
            this.mw._getDrawTabAndOr_OrButton().setEnabled(false);
        } else
            // if the first row is deleted, the new first row and/or attribute
            // must be "-"
            this.queryTableModel.setValueAt(this.getMessage(Messages.WINMAIN_QUERY_DATA_cellFirstString), 0, 0);

        this.mw._getCriteriaTabQuery_QueryTable().repaint();
        this.mw._getDrawTab_QueryTable().repaint();
    }

    public void QueryModifierResetButtonPressed() {

        int size = this.queryTableModel.getRowCount();

        for (int i = 0; i < size; i++) {
            this.queryTableModel.removeRow(this.queryTableModel.getRowCount() - 1);

        }

        this.mw._getCriteriaTabQueryMaker_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAddText));
        this.mw._getCriteriaTabQueryMaker_OrButton().setEnabled(false);
        this.mw._getDrawTabAndOr_AndAddButton().setText(this.getMessage(Messages.WINMAIN_CRITERIA_ButtonQueryMakerAddText));
        this.mw._getDrawTabAndOr_OrButton().setEnabled(false);
    }

    public void saveConfig() {

        // TODO: not restart needed to apply changes

        String lang = Messages.getKey((String) this.mw._getOptionsTab_LanguajeComboBox().getSelectedItem());
        lang = lang.substring("LANGUAJE_".length());
        String kanjipath = this.mw._getOptionsTab_KanjiDictPathTextField().getText();
        String strokepath = this.mw._getOptionsTab_StrokesPathTextField().getText();
        Boolean extInfo = this.mw._getOptionsTab_ExtendedInfoCheckBox().isSelected();
        Boolean extKanji = this.mw._getOptionsTab_ExtendedIKanjiListCheckBox().isSelected();
        Boolean romaji = this.mw._getOptionsTab_KanaToRomajiCheckBox().isSelected();

        this.jdkGui.getJavaDikt().saveOptions(lang, kanjipath, strokepath, extInfo, extKanji, romaji);

        this.jdkGui.reinitEngine();
    }

    private KanjiSortExpression selectOrderBy(NumberOrderDef sod, String orderByKey) {

        KanjiSortExpression kse = null;

        if (orderByKey.equals(Messages.ORDERMETHODSINT_FROM_GREATEST_TO_LEAST)) {
            kse = sod.from_greatest_to_least();
        } else if (orderByKey.equals(Messages.ORDERMETHODSINT_FROM_LEAST_TO_GREATEST)) {
            kse = sod.from_least_to_greatest();
        } else if (orderByKey.equals(Messages.ORDERMETHODSINT_RAMDOM)) {
            kse = sod.ramdomly();
        } else if (orderByKey.equals(Messages.ORDERMETHODSSTR_RAMDOM)) {
            kse = sod.ramdomly();
        }

        return kse;
    }

    private KanjiSortExpression selectOrderBy(StringOrderDef sod, String orderByKey) {

        KanjiSortExpression kse = null;

        if (orderByKey.equals(Messages.ORDERMETHODSSTR_ALPHABETICALLY)) {
            kse = sod.alphabetically();
        } else if (orderByKey.equals(Messages.ORDERMETHODSSTR_ALPHABETICALLY_INVERSE)) {
            kse = sod.alphabetically_inverse();
        } else if (orderByKey.equals(Messages.ORDERMETHODSINT_RAMDOM)) {
            kse = sod.ramdomly();
        } else if (orderByKey.equals(Messages.ORDERMETHODSSTR_RAMDOM)) {
            kse = sod.ramdomly();
        }

        return kse;
    }

    public void setDisableForModifing(Boolean disable) {

        this.mw._getControlsTabbedPane().setEnabled(disable);

        if (this.selectedTab == TAB_CRITERIA) {
            this.mw._getCriteriaQueryControlModify_RemoveButton().setEnabled(disable);
            this.mw._getCriteriaQueryControlModify_ResetButton().setEnabled(disable);
            this.mw._getCriteriaTabQueryControl_ExecuteButton().setEnabled(disable);
            this.mw._getCriteriaTabQuery_QueryTable().setEnabled(disable);
            this.mw._getCriteriaTabQueryMaker_InfoButton().setEnabled(disable);
            // I don't know why renderer dissapears when disabled
            this.mw._getCriteriaTabQuery_QueryTable().setDefaultRenderer(Object.class, this.getQueryTableCellRenderer());

        } else if (this.selectedTab == TAB_DRAW) {
            this.mw._getDrawTabQueryControl_RemoveButton().setEnabled(disable);
            this.mw._getDrawTabQueryControl_ResetButton().setEnabled(disable);
            this.mw._getDrawTab_ExecuteButton().setEnabled(disable);
            this.mw._getDrawTab_QueryTable().setEnabled(disable);
            this.mw._getCriteriaTabQueryMaker_InfoButton().setEnabled(disable);
            // I don't know why renderer dissapears when disabled
            this.mw._getDrawTab_QueryTable().setDefaultRenderer(Object.class, this.getQueryTableCellRenderer());
        }

        this.mw._getCriteriaTabQuery_QueryTable().repaint();
        this.mw._getDrawTab_QueryTable().repaint();
    }

    public void setExportSortedList(List<KanjiTag> sortedList) {

        this.exportSortedList = sortedList;
    }

    public void setResultPanelBordertText(String text) {

        this.mw._getResultsPanel().setBorder(BorderFactory.createTitledBorder(text));
    }

    private void setResultPanelToKanjiExportGrid() {

        JPanel results = this.mw._getResultsPanel();

        // remove void panel if first or previous panel
        results.remove(0);

        this.showingKanjiInfo = false;

        results.add(this.exportResultPanel, BorderLayout.CENTER);

        results.updateUI();
    }

    private void setResultPanelToKanjiToInfoGrid() {

        JPanel results = this.mw._getResultsPanel();

        // remove void panel if first or previous panel
        results.remove(0);

        this.showingKanjiInfo = true;

        results.add(this.resultPanel, BorderLayout.CENTER);

        results.updateUI();
    }

    public void setSorterListModified(boolean b) {
        this.exportSortedList = null;
        this.sortersModified = b;
    }

    public void setWindow(MainWindow mainWindow) {
        this.mw = mainWindow;
    }

    public void strokeFileChoose() {
        FileFilter kanjiDictFileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {

                return f.isDirectory() || (f.isFile() && f.getName().endsWith(".zobj"));
            }
            @Override
            public String getDescription() {
                return Messages.get(Messages.FILE_DESCRIPTION_ZOBJ_STROKE_FILE);
            }
        };

        FileExplorerDialog fed = new FileExplorerDialog(kanjiDictFileFilter, this.mw, true);
        fed.setLocale(new Locale(this.jdkGui.getOptions().getLanguage()));

        int retValue = fed.showOpenDialog();

        if (retValue == JFileChooser.APPROVE_OPTION) {
            File kanjiDict = fed.getFileChooser().getSelectedFile();
            this.mw._getOptionsTab_KanjiDictPathTextField().setText(kanjiDict.getAbsolutePath());
            this.mw._getOptionsTabPanel().updateUI();
        }
    }

    public void tabChangedActionPerformed(int index) {

        this.selectedTab = index;

        if (this.exportResultPanel != null) {

            switch (this.selectedTab) {
            // stroke and criteria tab
            case 0 :
            case 1 :
                // if getBufferedKanjiInfoGrid() exists but another panel is
                // being displayed in results panel
                if (!this.showingKanjiInfo)
                    this.setResultPanelToKanjiToInfoGrid();

                break;
                // export tab
            case 2 :
                this.setResultPanelToKanjiExportGrid();
                break;
                // options tab
            case 3 :
                this.updateOptionPanel();
                break;
            }

        } else {
            switch (this.selectedTab) {
            case 3 :
                this.updateOptionPanel();
                break;
            }

        }
    }

    public void updateChoosableQueryComboBoxes() {

        String selectedMessage = this.getSelectedMessageFromFieldComboBox();
        String selectedKey = this.getSelectedKeyFromFieldComboBox(selectedMessage);

        //Activates info button
        this.mw._getCriteriaTabQueryMaker_InfoButton().setEnabled(Messages.hasMessage("INFO_" + selectedKey));

        ComboBoxModel fullfilModel = null;
        ComboBoxModel valueModel = null;

        //Selects fulfills combo box model
        switch (KanjiEnums.getFieldType(selectedKey)) {
        case INTEGER:
        case ORDENABLE_STRING :
            fullfilModel = new DefaultComboBoxModel(Messages.getList(Messages.LIST_CRITERIA_CASES_INT));
            break;
        case STRING:
            fullfilModel = new DefaultComboBoxModel(Messages.getList(Messages.LIST_CRITERIA_CASES_STRING));
            break;
        case INTEGER_LIST:
            fullfilModel = new DefaultComboBoxModel();
            break;
        }

        this.mw._getCriteriaTabQueryMaker_FulfilsComboBox().setModel(fullfilModel);
        this.mw._getCriteriaTabQueryMaker_FulfilsComboBox().setSelectedIndex(0);
        this.mw._getCriteriaTabQueryMaker_FulfilsComboBox().setEnabled(true);


        //Selects value combo box model
        if (selectedKey.equals(Messages.KANJI_DIC_NAME)) {

            valueModel = new DefaultComboBoxModel(Messages.getList(Messages.LIST_CRITERIA_VALUES_DIC));
            this.mw._getCriteriaTabQueryMaker_ValueComboBox().setEditable(false);

        } else if (selectedKey.endsWith(Messages.KANJI_JIS_CHARSET)) {

            valueModel = new DefaultComboBoxModel(Messages.getList(Messages.LIST_CRITERIA_VALUES_JIS));
            this.mw._getCriteriaTabQueryMaker_ValueComboBox().setEditable(false);

        } else if (selectedKey.equals(Messages.KANJI_READING_TYPE)) {

            if (this.jdkGui.getOptions().getExtendedInfo())
                valueModel = new DefaultComboBoxModel(Messages.getList(Messages.LIST_CRITERIA_VALUES_READING_EXTENDED));
            else
                valueModel = new DefaultComboBoxModel(Messages.getList(Messages.LIST_CRITERIA_VALUES_READING_BASIC));

            this.mw._getCriteriaTabQueryMaker_ValueComboBox().setEditable(false);
        } else if (selectedKey.equals(Messages.KANJI_CLASSIC_RADICAL)) {

            valueModel = new DefaultComboBoxModel(Messages.getList(Messages.LIST_CRITERIA_VALUES_RADICALS));
            this.mw._getCriteriaTabQueryMaker_ValueComboBox().setEditable(false);

        } else {

            valueModel = new DefaultComboBoxModel();

            this.mw._getCriteriaTabQueryMaker_ValueComboBox().setEditable(true);
        }

        this.mw._getCriteriaTabQueryMaker_ValueComboBox().setModel(valueModel);
        this.mw._getCriteriaTabQueryMaker_ValueComboBox().setEnabled(true);

    }

    public void updateOptionPanel() {

        this.mw._getOptionsTab_LanguajeComboBox().setSelectedItem(this.jdkGui.getOptions().getLanguage());
        this.mw._getOptionsTab_KanjiDictPathTextField().setText(this.jdkGui.getOptions().getKanjiFile().getAbsolutePath());
        this.mw._getOptionsTab_StrokesPathTextField().setText(this.jdkGui.getOptions().getTreeFile().getAbsolutePath());
        this.mw._getOptionsTab_ExtendedInfoCheckBox().setSelected(this.jdkGui.getOptions().getExtendedInfo());
        this.mw._getOptionsTab_ExtendedIKanjiListCheckBox().setSelected(this.jdkGui.getOptions().getExtendedIKanjiList());
        this.mw._getOptionsTab_KanaToRomajiCheckBox().setSelected(this.jdkGui.getOptions().getRomanized());

        this.mw._getOptionsTabPanel().updateUI();
    }
}
