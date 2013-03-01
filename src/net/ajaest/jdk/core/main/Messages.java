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

package net.ajaest.jdk.core.main;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.ajaest.jdk.core.main.ExceptionHandler.KNOWN_EXCEPTIONS;
import net.ajaest.lib.exceptions.NoSuchMessage;
import net.ajaest.lib.utils.SystemEnums.ISO639ー1;

//TODO: translate into english
/**
 *
 * Clase que comprende todos los métodos de entrada-salida y de tratamiento de
 * cadenas localizadas. Para usar los métodos de recuperación de cadenas la
 * clase primero ha de ser inicializada mediante el método estático {@code
 * initMessages()}. No está permitida la instanciación.
 *
 */
public class Messages {

    private static Map<String, String> messages;
    private static Map<String, Map<String, String>> listMessages;

    private static Map<String, List<String>> keyMap;

    private static boolean initialized = false;

    public final static String[] SUPPORTED = {"EN","ES"};
    public final static String DEFAULT_LENGUAGE = SUPPORTED[0]; //EN
    public static String SYSTEM_LENGUAGE = System.getProperty("user.language").toUpperCase();
    public static String LANGUAGE;

    public static boolean printMessages = true;

    // Generic
    public final static String GEN_DIR_FILES_READ_ERROR_C = "DIR_FILES_READ_ERROR_C";
    public final static String GEN_GENERIC_READ_ERROR_C_colon = "GENERIC_READ_ERROR_C_colon";
    public final static String GEN_EXIT_SUCESS_C = "EXIT_SUCESS_C";
    public final static String GEN_EXIT_SUCCES_TIME_C = "EXIT_SUCCES_TIME_C";
    public final static String GEN_INDEXING = "INDEXING";
    public final static String GEN_LENGUAJE_DOES_NOT_EXISTS_C = "LENGUAJE_DOES_NOT_EXISTS";
    public final static String GEN_LOCAL_LABEL = "LOCAL_LABEL";
    public final static String GEN_NAME_ERROR = "NAME_ERROR";
    public final static String GEN_NAME_OR_PATH_ERROR = "NAME_OR_PATH_ERROR";
    public final static String GEN_NULL_POINTER = "NULL_POINTER";
    public final static String GEN_NOT_ISO_639_1 = "NO_ISO-639-1";
    public final static String GEN_NOT_MD5_HASH = "NOT_A_MD5_HASH";
    public final static String GEN_PATH_DOES_NOT_EXISTS = "PATH_DOES_NOT_EXISTS";
    public final static String GEN_PATH_ERROR = "PATH_ERROR";
    public final static String GEN_READ_ERROR_C = "READ_ERROR_C";
    public final static String GEN_RECOVERING_FILE_SYSTEM = "RECOVERING_FILE_SYSTEM";
    public final static String GEN_UNKNOWN_ERROR = "UNKNOWN_ERROR";
    public final static String GEN_WRITING = "WRITING";

    // LOCALIZED LANGUAGE NAMES

    public final static String LANGUAJE_EN = "LANGUAJE_EN";
    public final static String LANGUAJE_ES = "LANGUAJE_ES";

    // FILE DESCRIPTIONS

    public final static String FILE_DESCRIPTION_JDK_KANJI_FILE = "FILE_DESCRIPTION_JDK_KANJI_FILE";
    public final static String FILE_DESCRIPTION_ZOBJ_STROKE_FILE = "FILE_DESCRIPTION_TXT_STROKE_FILE";

    // KANJI PROPERTIES

    public final static String KANJI_CLASSIC_NELSON = "KANJI_CLASSIC_NELSON";
    public final static String KANJI_CLASSIC_RADICAL = "KANJI_CLASSIC_RADICAL";
    public final static String KANJI_DE_ROO = "KANJI_DE_ROO";
    public final static String KANJI_DIC_NAME = "KANJI_DIC_NAME";
    public final static String KANJI_DIC_INDEX = "KANJI_DIC_INDEX";
    public final static String KANJI_FOUR_CORNER = "KANJI_FOUR_CORNER";
    public final static String KANJI_FREQUENCY = "KANJI_FREQUENCY";
    public final static String KANJI_GRADE = "KANJI_GRADE";
    public final static String KANJI_GRAPH = "KANJI_GRAPH";
    public final static String KANJI_JIS_CHARSET = "KANJI_JIS_CHARSET";
    public final static String KANJI_JIS_CODE = "KANJI_JIS_CODE";
    public final static String KANJI_JLPT_LEVEL = "KANJI_JLPT_LEVEL";
    public final static String KANJI_LITERAL = "KANJI_LITERAL";
    public final static String KANJI_MEANING = "KANJI_MEANING";
    public final static String KANJI_READING_TYPE = "KANJI_READING_TYPE";
    public final static String KANJI_READING = "KANJI_READING";
    public final static String KANJI_SKIP = "KANJI_SKIP";
    public final static String KANJI_SPAHN_HADAMITZKY = "KANJI_SPAHN_HADAMITZKY";
    public final static String KANJI_STROKE_COUNT = "KANJI_STROKE_COUNT";
    public final static String KANJI_UNICODE_VALUE = "KANJI_UNICODE_VALUE";
    public final static String KANJI_VARIANT_INDEX = "KANJI_VARIANT_INDEX";
    public final static String KANJI_VARIANT_TYPE = "KANJI_VARIANT_TYPE";
    public final static String KANJI_FIRST_MEANING = "KANJI_FIRST_MEANING";
    public final static String KANJI_FIRST_READING = "KANJI_FIRST_READING";
    public final static String KANJI_DIC_REFERENCE = "KANJI_DIC_REFERENCE";

    //RADICAL TYPES

    public final static String RADICAL_TYPE_original = "RADICAL_TYPE_original";
    public final static String RADICAL_TYPE_top = "RADICAL_TYPE_top";
    public final static String RADICAL_TYPE_bottom = "RADICAL_TYPE_bottom";
    public final static String RADICAL_TYPE_left = "RADICAL_TYPE_left";
    public final static String RADICAL_TYPE_right = "RADICAL_TYPE_right";
    public final static String RADICAL_TYPE_enclosure = "RADICAL_TYPE_enclosure";
    public final static String RADICAL_TYPE_variable = "RADICAL_TYPE_variable";

    // ALLOWED CASES

    public final static String CASE_EQUALS = "CASE_EQUALS";
    public final static String CASE_NOT_EQUALS = "CASE_NOT_EQUALS";
    public final static String CASE_GREATHER = "CASE_GREATHER";
    public final static String CASE_LESS = "CASE_LESS";
    public final static String CASE_EQUALS_OR_GREATHER = "CASE_EQUALS_OR_GREATHER";
    public final static String CASE_EQUALS_OR_LESS = "CASE_EQUALS_OR_LESS";
    public final static String CASE_NULL = "CASE_NULL";

    // JIS CHARSET

    public final static String JIS_208 = "JIS_208";
    public final static String JIS_212 = "JIS_212";
    public final static String JIS_213 = "JIS_213";

    // PROGRAM LISTS

    public final static String LIST_CRITERIA_FIELD_BASIC = "LIST_CRITERIA_FIELD_BASIC";
    public final static String LIST_CRITERIA_FIELD_EXTENDED = "LIST_CRITERIA_FIELD_EXTENDED";
    public final static String LIST_CRITERIA_CASES_STRING = "LIST_CRITERIA_CASES_STRING";
    public final static String LIST_CRITERIA_CASES_INT = "LIST_CRITERIA_CASES_INT";
    public final static String LIST_CRITERIA_VALUES_JIS = "LIST_CRITERIA_VALUES_JIS";
    public final static String LIST_CRITERIA_VALUES_RADICALS = "LIST_CRITERIA_VALUES_RADICALS";
    public final static String LIST_CRITERIA_VALUES_DIC = "LIST_CRITERIA_VALUES_DIC";
    public final static String LIST_CRITERIA_VALUES_READING_BASIC = "LIST_CRITERIA_VALUES_READING_BASIC";
    public final static String LIST_CRITERIA_VALUES_READING_EXTENDED = "LIST_CRITERIA_VALUES_READING_EXTENDED";
    public final static String LIST_LANGUAGES_SUPPORTED = "LIST_LANGUAGES_SUPPORTED";
    public final static String LIST_ORDENABLE_FIELDS = "LIST_ORDENABLE_FIELDS";
    public final static String LIST_ORDENABLE_METHODS_STRING = "LIST_ORDENABLE_METHODS_STRING";
    public final static String LIST_ORDENABLE_METHODS_INTEGER = "LIST_ORDENABLE_METHODS_INTEGER";

    // DICTIONARIES

    public final static String DIC_nelson_c = "DIC_nelson_c";
    public final static String DIC_nelson_n = "DIC_nelson_n";
    public final static String DIC_halpern_njecd = "DIC_halpern_njecd";
    public final static String DIC_halpern_kkld = "DIC_halpern_kkld";
    public final static String DIC_heisig = "DIC_heisig";
    public final static String DIC_gakken = "DIC_gakken";
    public final static String DIC_kanji_basic_book = "DIC_kanji_basic_book";
    public final static String DIC_oneill_names = "DIC_oneill_names";
    public final static String DIC_oneill_kk = "DIC_oneill_kk";
    public final static String DIC_moro = "DIC_moro";
    public final static String DIC_henshall = "DIC_henshall";
    public final static String DIC_sh_kk = "DIC_sh_kk";
    public final static String DIC_sakade = "DIC_sakade";
    public final static String DIC_jf_cards = "DIC_jf_cards";
    public final static String DIC_henshall3 = "DIC_henshall3";
    public final static String DIC_tutt_cards = "DIC_tutt_cards";
    public final static String DIC_crowley = "DIC_crowley";
    public final static String DIC_kanji_in_context = "DIC_kanji_in_context";
    public final static String DIC_busy_people = "DIC_busy_people";
    public final static String DIC_kodansha_compact = "DIC_kodansha_compact";
    public final static String DIC_maniette = "DIC_maniette";

    // READINGS

    public final static String READ_JA_ON = "READ_JA_ON";
    public final static String READ_JA_KUN = "READ_JA_KUN";
    public final static String READ_NANORI = "READ_NANORI";
    public final static String READ_PINYIN = "READ_PINYIN";
    public final static String READ_KOREAN_H = "READ_KOREAN_H";
    public final static String READ_KOREAN_R = "READ_KOREAN_R";

    // ORDENABLE PROPERTIES

    public final static String ORDER_UNICODE_VALUE = "ORDER_UNICODE_VALUE";
    public final static String ORDER_JIS_CHARSET = "ORDER_JIS_CHARSET";
    public final static String ORDER_JIS_CODE = "ORDER_JIS_CODE";
    public final static String ORDER_CLASSIC_NELSON = "ORDER_CLASSIC_NELSON";
    public final static String ORDER_CLASSIC_RADICAL = "ORDER_CLASSIC_RADICAL";
    public final static String ORDER_GRADE = "ORDER_GRADE";
    public final static String ORDER_STROKE_COUNT = "ORDER_STROKE_COUNT";
    public final static String ORDER_FREQUENCY = "ORDER_FREQUENCY";
    public final static String ORDER_SKIP = "ORDER_SKIP";
    public final static String ORDER_SPAHN_HADAMITZKY = "ORDER_SPAHN_HADAMITZKY";
    public final static String ORDER_FOUR_CORNER = "ORDER_FOUR_CORNER";
    public final static String ORDER_DE_ROO = "ORDER_DE_ROO";
    public final static String ORDER_GRAPH = "ORDER_GRAPH";
    public final static String ORDER_DIC_INDEX = "ORDER_DIC_INDEX";
    public final static String ORDER_MEANING = "ORDER_MEANING";
    public final static String ORDER_READING = "ORDER_READING";

    // ORDENABLE METHODS

    public final static String ORDERMETHODSSTR_ALPHABETICALLY = "ORDERMETHODSSTR_ALPHABETICALLY";
    public final static String ORDERMETHODSSTR_ALPHABETICALLY_INVERSE = "ORDERMETHODSSTR_ALPHABETICALLY_INVERSE";
    public final static String ORDERMETHODSINT_FROM_LEAST_TO_GREATEST = "ORDERMETHODSINT_FROM_LEAST_TO_GREATEST";
    public final static String ORDERMETHODSINT_FROM_GREATEST_TO_LEAST = "ORDERMETHODSINT_FROM_GREATEST_TO_LEAST";
    public final static String ORDERMETHODSINT_RAMDOM = "ORDERMETHODSINT_RAMDOM";
    public final static String ORDERMETHODSSTR_RAMDOM = "ORDERMETHODSSTR_RAMDOM";

    // WINMAIN

    // Window name

    public final static String WINMAIN_name = "WINMAIN_name";

    // Option tab names
    public final static String WINMAIN_TAB_CONTROL_About = "WINMAIN_TAB_CONTROL_About";
    public final static String WINMAIN_TAB_CONTROL_Criteria = "WINMAIN_TAB_CONTROL_Criteria";
    public final static String WINMAIN_TAB_CONTROL_Drawing = "WINMAIN_TAB_CONTROL_Drawing";
    public final static String WINMAIN_TAB_CONTROL_Export = "WINMAIN_TAB_CONTROL_Export";
    public final static String WINMAIN_TAB_CONTROL_Config = "WINMAIN_TAB_CONTROL_Config";

    // Border texts
    public final static String WINMAIN_CRITERIA_BorderTextQueryData = "WINMAIN_CRITERIA_BorderTextQueryData";
    public final static String WINMAIN_CRITERIA_BorderTextQueryMaker = "WINMAIN_CRITERIA_BorderTextQueryMaker";
    public final static String WINMAIN_CRITERIA_BorderTextQueryControls = "WINMAIN_CRITERIA_BorderTextQueryControls";
    public final static String WINMAIN_RESULTS_BorderTextResults = "WINMAIN_RESULTS_BorderTextResults";
    public final static String WINMAIN_RESULTS_EXPORT_BorderTextControls = "WINMAIN_RESULTS_EXPORT_BorderTextControls";
    public final static String WINMAIN_RESULTS_BorderTextControls = "WINMAIN_RESULTS_BorderTextControls";
    public final static String WINMAIN_STROKES_BorderTextControls = "WINMAIN_STROKES_BorderTextControls";
    public final static String WINMAIN_STROKES_BorderTextAndOr = "WINMAIN_STROKES_BorderTextAndOr";
    public final static String WINMAIN_EXPORT_BorderTextExportAs = "WINMAIN_EXPORT_BorderTextExportAs";
    public final static String WINMAIN_EXPORT_BorderTextExportSorting = "WINMAIN_EXPORT_BorderTextExportSorting";
    public final static String WINMAIN_EXPORT_BorderTextExportExtra = "WINMAIN_EXPORT_BorderTextExportExtra";

    // Query table column names
    public final static String WINMAIN_QUERY_DATA_columnAndOr = "WINMAIN_QUERY_DATA_columnAndOr";
    public final static String WINMAIN_QUERY_DATA_columnField = "WINMAIN_QUERY_DATA_columnField";
    public final static String WINMAIN_QUERY_DATA_columnThatFulfilsThat = "WINMAIN_QUERY_DATA_columnThatFulfilsThat";
    public final static String WINMAIN_QUERY_DATA_columnValue = "WINMAIN_QUERY_DATA_columnValue";
    public final static String WINMAIN_EXPORT_SORTING_columnLevel = "WINMAIN_EXPORT_SORTING_columnLevel";
    public final static String WINMAIN_EXPORT_SORTING_columnOrderBy = "WINMAIN_EXPORT_SORTING_columnOrderBy";
    public final static String WINMAIN_EXPORT_SORTING_columnField = "WINMAIN_EXPORT_SORTING_columnField";

    //Query table cell string

    public final static String WINMAIN_QUERY_DATA_cellAndString = "WINMAIN_QUERY_DATA_cellAndString";
    public final static String WINMAIN_QUERY_DATA_cellOrString = "WINMAIN_QUERY_DATA_cellOrString";
    public final static String WINMAIN_QUERY_DATA_cellFirstString = "WINMAIN_QUERY_DATA_cellFirstString";

    // Text Label
    public final static String WINMAIN_CRITERIA_LabelFulfilsComboBox = "WINMAIN_CRITERIA_LabelFulfilsComboBox";
    public final static String WINMAIN_CRITERIA_LabelValueComboBox = "WINMAIN_CRITERIA_LabelValueComboBox";
    public final static String WINMAIN_EXPORT_LabelFormatComboBox = "WINMAIN_EXPORT_LabelFormatComboBox";
    public final static String WINMAIN_EXPORT_LabelStyleComboBox = "WINMAIN_EXPORT_LabelStyleComboBox";
    public final static String WINMAIN_EXPORT_LabelDirButton = "WINMAIN_EXPORT_LabelDirButton";
    public final static String WINMAIN_CRITERIA_LabelFieldComboBox = "WINMAIN_CRITERIA_LabelFieldComboBox";
    public final static String WINMAIN_OPTIONS_LabelLanguajeComboBox = "WINMAIN_OPTIONS_LabelLanguajeComboBox";
    public final static String WINMAIN_OPTIONS_LabelKanjiFileTextField = "WINMAIN_OPTIONS_LabelKanjiFileTextField";
    public final static String WINMAIN_OPTIONS_LabelStrokeFileTextField = "WINMAIN_OPTIONS_LabelStrokeFileTextField";
    public final static String WINMAIN_OPTIONS_LabelExtendedInfoCheckBox = "WINMAIN_OPTIONS_LabelExtendedInfoCheckBox";
    public final static String WINMAIN_OPTIONS_LabelExtendedKanjiListCheckBox = "WINMAIN_OPTIONS_LabelExtendedKanjiListCheckBox";
    public final static String WINMAIN_OPTIONS_LabelRomajiCheckBox = "WINMAIN_OPTIONS_LabelRomajiCheckBox";

    // Button text

    public final static String WINMAIN_CRITERIA_ButtonQueryMakerAndText = "WINMAIN_CRITERIA_ButtonQueryMakerAndText";
    public final static String WINMAIN_CRITERIA_ButtonQueryMakerAddText = "WINMAIN_CRITERIA_ButtonQueryMakerAddText";
    public final static String WINMAIN_CRITERIA_ButtonQueryMakerChangeText = "WINMAIN_CRITERIA_ButtonQueryMakerChangeText";
    public final static String WINMAIN_CRITERIA_ButtonQueryMakerOrText = "WINMAIN_CRITERIA_ButtonQueryMakerOrText";
    public final static String WINMAIN_CRITERIA_ButtonQueryControlResetText = "WINMAIN_CRITERIA_ButtonQueryControlResetText";
    public final static String WINMAIN_CRITERIA_ButtonQueryControlRemoveText = "WINMAIN_CRITERIA_ButtonQueryControlRemoveText";
    public final static String WINMAIN_CRITERIA_ButtonQueryControlModifyText = "WINMAIN_CRITERIA_ButtonQueryControlModifyText";
    public final static String WINMAIN_CRITERIA_ButtonQueryControlExecuteText = "WINMAIN_CRITERIA_ButtonQueryControlExecuteText";
    public final static String WINMAIN_STROKES_ButtonDrawPanelReset = "WINMAIN_STROKES_ButtonDrawPanelReset";
    public final static String WINMAIN_STROKES_ButtonDrawPanelRemoveLastStroke = "WINMAIN_STROKES_ButtonDrawPanelRemoveLastStroke";
    public final static String WINMAIN_STROKES_ButtonDrawPanelRemoveLastLine = "WINMAIN_STROKES_ButtonDrawPanelRemoveLastLine";
    public final static String WINMAIN_RESULTS_ButtonExportAll = "WINMAIN_RESULTS_ButtonExportAll";
    public final static String WINMAIN_RESULTS_ButtonCloseAllWindows = "WINMAIN_RESULTS_ButtonCloseAllWindows";
    public final static String WINMAIN_RESULTS_ButtonExportInvert = "WINMAIN_RESULTS_ButtonExportInvert";
    public final static String WINMAIN_RESULTS_ButtonExportReset = "WINMAIN_RESULTS_ButtonExportReset";
    public final static String WINMAIN_EXPORT_ButtonExportExtraOptions = "WINMAIN_EXPORT_ButtonExportExtraOptions";
    public final static String WINMAIN_EXPORT_ButtonSortingAdd = "WINMAIN_EXPORT_ButtonSortingAdd";
    public final static String WINMAIN_EXPORT_ButtonSortingReset = "WINMAIN_EXPORT_ButtonSortingReset";
    public final static String WINMAIN_EXPORT_ButtonSortingRemove = "WINMAIN_EXPORT_ButtonSortingRemove";
    public final static String WINMAIN_EXPORT_ButtonSortingModify = "WINMAIN_EXPORT_ButtonSortingModify";
    public final static String WINMAIN_EXPORT_ButtonExtraRearrange = "WINMAIN_EXPORT_ButtonExtraRearrange";
    public final static String WINMAIN_EXPORT_ButtonExtraOptions = "WINMAIN_EXPORT_ButtonExtraOptions";
    public final static String WINMAIN_EXPORT_ButtonExport = "WINMAIN_EXPORT_ButtonExport";
    public final static String WINMAIN_OPTIONS_ButtonSave = "WINMAIN_OPTIONS_ButtonSave";
    public final static String WINMAIN_OPTIONS_ButtonAbout = "WINMAIN_OPTIONS_ButtonAbout";

    // Export sorter adder
    //Window name
    public final static String WINEXPORTRESORTERADDER_name = "WINEXPORTRESORTERADDER_name";
    //Buttons
    public final static String WINEXPORTRESORTERADDER_ButtonAdd = "WINEXPORTRESORTERADDER_ButtonAdd";
    public final static String WINEXPORTRESORTERADDER_ButtonCancel = "WINEXPORTRESORTERADDER_ButtonCancel";
    // Labels
    public final static String WINEXPORTRESORTERADDER_LabelLevel = "WINEXPORTRESORTERADDER_LabelLevel";
    public final static String WINEXPORTRESORTERADDER_LabelOrderBy = "WINEXPORTRESORTERADDER_LabelOrderBy";
    public final static String WINEXPORTRESORTERADDER_LabelField = "WINEXPORTRESORTERADDER_LabelField";

    // Export rearranger Dialog
    // Window name
    public final static String WINEXPORTREARRANGER_name = "WINEXPORTREARRANGER_name";
    // Buttons
    public final static String WINEXPORTREARRANGER_acceptButton = "WINEXPORTREARRANGER_acceptButton";
    public final static String WINEXPORTREARRANGER_cancelButton = "WINEXPORTREARRANGER_cancelButton";
    // table column names
    public final static String WINEXPORTREARRANGER_kanjiLiteral = "WINEXPORTREARRANGER_kanjiLiteral";

    // Kanji info window

    public final static String WINKANJIINFO_name = "WINKANJIINFO_name";

    // Panel titles
    public final static String WINKANJIINFO_PanelTitle_Radical = "WINKANJIINFO_PanelTitle_Radical";
    public final static String WINKANJIINFO_PanelTitle_StrokeCount = "WINKANJIINFO_PanelTitle_StrokeCount";
    public final static String WINKANJIINFO_PanelTitle_Grade = "WINKANJIINFO_PanelTitle_Grade";
    public final static String WINKANJIINFO_PanelTitle_JLPTLevel = "WINKANJIINFO_PanelTitle_JLPTLevel";
    public final static String WINKANJIINFO_PanelTitle_Frequency = "WINKANJIINFO_PanelTitle_Frequency";
    public final static String WINKANJIINFO_PanelTitle_NelsonRadical = "WINKANJIINFO_PanelTitle_NelsonRadical";
    public final static String WINKANJIINFO_PanelTitle_Unicode = "WINKANJIINFO_PanelTitle_Unicode";
    public final static String WINKANJIINFO_PanelTitle_JIS = "WINKANJIINFO_PanelTitle_JIS";
    public final static String WINKANJIINFO_PanelTitle_DicReferences = "WINKANJIINFO_PanelTitle_DicReferences";
    public final static String WINKANJIINFO_PanelTitle_Graph = "WINKANJIINFO_PanelTitle_Graph";
    public final static String WINKANJIINFO_PanelTitle_Meanings = "WINKANJIINFO_PanelTitle_Meanings";
    public final static String WINKANJIINFO_PanelTitle_Readings = "WINKANJIINFO_PanelTitle_Readings";
    public final static String WINKANJIINFO_PanelTitle_Variants = "WINKANJIINFO_PanelTitle_Variants";
    public final static String WINKANJIINFO_PanelTitle_QueryCodes = "WINKANJIINFO_PanelTitle_QueryCodes";
    public final static String WINKANJIINFO_PanelTitle_QueryCodes_SKIP = "WINKANJIINFO_PanelTitle_QueryCodes_SKIP";
    public final static String WINKANJIINFO_PanelTitle_QueryCodes_FourCorner = "WINKANJIINFO_PanelTitle_QueryCodes_FourCorner";
    public final static String WINKANJIINFO_PanelTitle_QueryCodes_SpahnHadamitzky = "WINKANJIINFO_PanelTitle_QueryCodes_SpahnHadamitzky";
    public final static String WINKANJIINFO_PanelTitle_QueryCodes_DeRoo = "WINKANJIINFO_PanelTitle_QueryCodes_DeRoo";

    // Radical Info window
    // Window name
    public final static String PANELRADICALINFO_name = "PANELRADICALINFO_name";
    // Panel titles
    public final static String PANELRADICALINFO_radicalNumber = "PANELRADICALINFO_radicalNumber";
    public final static String PANELRADICALINFO_radicalType = "PANELRADICALINFO_radicalType";
    public final static String PANELRADICALINFO_radicalNames = "PANELRADICALINFO_radicalNames";
    public final static String PANELRADICALINFO_radicalUnicode = "PANELRADICALINFO_radicalUnicode";
    public final static String PANELRADICALINFO_radicalUnicodeName = "PANELRADICALINFO_radicalUnicodeName";
    public final static String PANELRADICALINFO_radicalKangxi = "PANELRADICALINFO_radicalKangxi";

    // Stroke order info window
    // Window name
    public final static String STROKEORDERINFO_name = "STROKEORDERINFO_name";

    // INFO DIALOG
    // title
    public final static String INFODIALOG_title_error = "INFODIALOG_title_error";
    public final static String INFODIALOG_title_info = "INFODIALOG_title_info";
    public final static String INFODIALOG_title_newVersion = "INFODIALOG_title_newVersion";
    public final static String INFODIALOG_title_pleaseWait = "INFODIALOG_title_pleaseWait";
    // Messages
    public final static String INFODIALOG_buttonAccept = "INFODIALOG_buttonAccept";
    public final static String INFODIALOG_errorMessage_exportFail = "INFODIALOG_errorMessage_exportFail";
    public final static String INFODIALOG_errorMessage_integerCastException = "INFODIALOG_errorMessage_integerCastException";
    public final static String INFODIALOG_errorMessage_isNotHex = "INFODIALOG_errorMessage_isNotHex";
    public final static String INFODIALOG_errorMessage_cannotOpenFile = "INFODIALOG_errorMessage_cannotOpenFile";

    public final static String INFODIALOG_infoMessage_exportSuccess = "INFODIALOG_infoMessage_exportSuccess";
    public final static String INFODIALOG_infoMessage_graphCastException = "INFODIALOG_infoMessage_graphCastException";
    public final static String INFODIALOG_infoMessage_newVersion = "INFODIALOG_infoMessage_newVersion";
    public final static String INFODIALOG_infoMessage_kanjiCastException = "INFODIALOG_infoMessage_kanjiCastException";
    public final static String INFODIALOG_infoMessage_exporting = "INFODIALOG_infoMessage_exporting";
    // Button
    public final static String INFODIALOG_infoMessage_openFile = "INFODIALOG_infoMessage_openFile";

    // Border
    public final static String INFODIALOG_infoBorderText = "INFODIALOG_infoBorderText";
    // Info
    public final static String INFO_KANJI_CLASSIC_NELSON = "INFO_KANJI_CLASSIC_NELSON";
    public final static String INFO_KANJI_CLASSIC_RADICAL = "INFO_KANJI_CLASSIC_RADICAL";
    public final static String INFO_KANJI_DE_ROO = "INFO_KANJI_DE_ROO";
    public final static String INFO_KANJI_DIC_NAME = "INFO_KANJI_DIC_NAME";
    public final static String INFO_KANJI_DIC_INDEX = "INFO_KANJI_DIC_INDEX";
    public final static String INFO_KANJI_FOUR_CORNER = "INFO_KANJI_FOUR_CORNER";
    public final static String INFO_KANJI_FREQUENCY = "INFO_KANJI_FREQUENCY";
    public final static String INFO_KANJI_GRADE = "INFO_KANJI_GRADE";
    public final static String INFO_KANJI_GRAPH = "INFO_KANJI_GRAPH";
    public final static String INFO_KANJI_JIS_CHARSET = "INFO_KANJI_JIS_CHARSET";
    public final static String INFO_KANJI_JIS_CODE = "INFO_KANJI_JIS_CODE";
    public final static String INFO_KANJI_JLPT_LEVEL = "INFO_KANJI_JLPT_LEVEL";
    public final static String INFO_KANJI_LITERAL = "INFO_KANJI_LITERAL";
    public final static String INFO_KANJI_MEANING = "INFO_KANJI_MEANING";
    public final static String INFO_KANJI_READING_TYPE = "INFO_KANJI_READING_TYPE";
    public final static String INFO_KANJI_READING = "INFO_KANJI_READING";
    public final static String INFO_KANJI_SKIP = "INFO_KANJI_SKIP";
    public final static String INFO_KANJI_SPAHN_HADAMITZKY = "INFO_KANJI_SPAHN_HADAMITZKY";
    public final static String INFO_KANJI_STROKE_COUNT = "INFO_KANJI_STROKE_COUNT";
    public final static String INFO_KANJI_UNICODE_VALUE = "INFO_KANJI_UNICODE_VALUE";
    public final static String INFO_KANJI_VARIANT_INDEX = "INFO_KANJI_VARIANT_INDEX";
    public final static String INFO_KANJI_VARIANT_TYPE = "INFO_KANJI_VARIANT_TYPE";
    public final static String INFO_KANJI_FIRST_MEANING = "INFO_KANJI_FIRST_MEANING";
    public final static String INFO_KANJI_FIRST_READING = "INFO_KANJI_FIRST_READING";
    public final static String INFO_KANJI_DIC_REFERENCE = "INFO_KANJI_DIC_REFERENCE";


    // ABOUT WINDOW
    // Title
    public final static String ABOUTWIN_title = "ABOUTWIN_title";
    public final static String ABOUTWIN_javadicText = "ABOUTWIN_javadicText";
    public final static String ABOUTWIN_borderTextLicense = "ABOUTWIN_borderTextLicense";
    public final static String ABOUTWIN_licenseText = "ABOUTWIN_licenseText";

    //-------------------
    private Messages(){}//impide inicializar objetos de tipo Messages

    /**
     *
     * <br>
     * <b> initMessages</b>
     *
     * <br>
     * <br>{@code public final static void initMessages()}
     *
     * <br>
     * <br>
     * Método que inicializa la clase Messages en el idioma del sistema,
     * permitiendo obtener desde get las cadenas almacenadas. Si el idioma del
     * sistema no está soportado, inicia el idioma por defecto señalado en
     * DEFAULT_LENGUAGE.<br>
     * <br>
     */
    public final static void initMessages() {

        //No se implementa ningún método para evitar llamadas recursivas infinitas
        // en caso de que el idioma por defecto no tenga el formato correcto, o
        // por si no existe el idioma por defecto, eso deberá estar siempre
        // solucionado en tiempo de compilación.

        initMessages(SYSTEM_LENGUAGE);
    }

    /**
     *
     * <br>
     * <b> initMessages</b>
     *
     * <br>
     * <br>{@code public final static void initMessages()}
     *
     * <br>
     * <br>
     * Método que inicializa la clase Messages en el idioma elegido de la lista
     * ISO-639-1 permitiendo obtener desde get las cadenas almacenadas.<br>
     * <br>
     *
     * @param languaje
     *            - Cadena con el código del lenguaje en formato ISO-639-1.
     * @throws IllegalArgumentException
     *             - Si la cadena es nula.
     */
    public final static void initMessages(String languaje) {
        // Nombre de error termminado en:
        // "~colon": Prosigue un mensaje después de la descripción del error.
        // "~_C": El mensaje está formateado según el estándar de printf de C

        if(languaje == null)
            throw new IllegalArgumentException(get("NULL_POINTER")); //no inicializa el idioma por defecto,un fallo de puntero nulo debe ser resuelto en tiempo de compilación

        if( (languaje.length()!=2) ||
                !Character.isLetter(languaje.charAt(0)) ||
                !Character.isLetter(languaje.charAt(1))) {

            initMessages(); //inicializa el idioma por defecto, no es realmente necesario

            localePrintln(get("NO_ISO-639-1"));

            //BLOQUES DE IDIOMAS

        } else if (languaje.toUpperCase().equals("EN")) {

            LANGUAGE = "EN";

            if (initialized = true) {
                messages = new HashMap<String, String>();
                listMessages = new HashMap<String, Map<String, String>>();
            }
            // Generic

            messages.put(GEN_DIR_FILES_READ_ERROR_C, "**Could not access to files in folder \"%s\", maybe the file does not exist, it is locked or you do not have enough read privileges.");
            messages.put(GEN_GENERIC_READ_ERROR_C_colon, "**The file or folder \"%s\" could not be read: ");
            messages.put(GEN_EXIT_SUCESS_C, "The synchronization file was sucessfuly built in \"%s\".");
            messages.put(GEN_EXIT_SUCCES_TIME_C, "Runtime:[%s:%s:%s]");
            messages.put(GEN_INDEXING, "[Indexing...]\n");
            messages.put(GEN_LENGUAJE_DOES_NOT_EXISTS_C, "**The requested lenguage \"%s\" does not exist or is not defined, initializing default lenguage settings.");
            messages.put(GEN_LOCAL_LABEL, "After the program name, type the directory path wich will be used to build the synchronization file and following the synchronization file name.");
            messages.put(GEN_NAME_ERROR, "**The typed filename is not valid.");
            messages.put(GEN_NAME_OR_PATH_ERROR, "**The typed file name or directory path is not valid.");
            messages.put(GEN_NOT_ISO_639_1, "**The lenguage string has not ISO-639-1 format, initializing default lenguaje settings.");
            messages.put(GEN_NOT_MD5_HASH, "Not a MD5 hash.");
            messages.put(GEN_NULL_POINTER, "**Null pointer error.");
            messages.put(GEN_PATH_DOES_NOT_EXISTS, "**The typed diectory path do not exists.");
            messages.put(GEN_PATH_ERROR, "**The typed directory path name is not valid.");
            messages.put(GEN_READ_ERROR_C, "**The file or folder %s can not be read, maybe the file does not exists or you do not have enough read privileges.");
            messages.put(GEN_RECOVERING_FILE_SYSTEM, "\n[Recovering file system...]\n");
            messages.put(GEN_UNKNOWN_ERROR, "**Unknown error.");
            messages.put(GEN_WRITING, "[Writing synchronization file...]\n");

            // FILE DESCRIPTIONS

            messages.put(FILE_DESCRIPTION_JDK_KANJI_FILE, "JavaDiKt .jdk kanji file");
            messages.put(FILE_DESCRIPTION_ZOBJ_STROKE_FILE, "JavaDiKt .zobj index file");

            // PROGRAM LISTS

            Map<String, String> criteriaFieldsBasic = new HashMap<String, String>();
            criteriaFieldsBasic.put(KANJI_LITERAL, "Kanji");
            criteriaFieldsBasic.put(KANJI_FREQUENCY, "Frequency");
            criteriaFieldsBasic.put(KANJI_DIC_NAME, "Dictionary name");
            criteriaFieldsBasic.put(KANJI_DIC_INDEX, "Dictionary index");
            criteriaFieldsBasic.put(KANJI_GRADE, "Grade");
            criteriaFieldsBasic.put(KANJI_JLPT_LEVEL, "JLPT level");
            criteriaFieldsBasic.put(KANJI_MEANING, "Meaning");
            criteriaFieldsBasic.put(KANJI_CLASSIC_RADICAL, "Classic radical");
            criteriaFieldsBasic.put(KANJI_READING_TYPE, "Reading type");
            criteriaFieldsBasic.put(KANJI_READING, "Reading");
            criteriaFieldsBasic.put(KANJI_SKIP, "Skip code");
            criteriaFieldsBasic.put(KANJI_STROKE_COUNT, "Stroke count");
            listMessages.put(LIST_CRITERIA_FIELD_BASIC, criteriaFieldsBasic);

            Map<String, String> criteriaFieldsExt = new HashMap<String, String>();
            criteriaFieldsExt.put(KANJI_LITERAL, "Kanji");
            criteriaFieldsExt.put(KANJI_CLASSIC_RADICAL, "Classic radical");
            // criteriaFieldsExt.put(KANJI_CLASSIC_NELSON, "Nelson radical");
            criteriaFieldsExt.put(KANJI_DE_ROO, "De Roo code");
            criteriaFieldsExt.put(KANJI_DIC_NAME, "Dictionary name");
            criteriaFieldsExt.put(KANJI_DIC_INDEX, "Dictionary index");
            criteriaFieldsExt.put(KANJI_FOUR_CORNER, "Four corner code");
            criteriaFieldsExt.put(KANJI_FREQUENCY, "Frequency");
            criteriaFieldsExt.put(KANJI_GRADE, "Grade");
            criteriaFieldsExt.put(KANJI_GRAPH, "Asterisk graph");
            criteriaFieldsExt.put(KANJI_JIS_CHARSET, "JIS charset");
            criteriaFieldsExt.put(KANJI_JIS_CODE, "JIS kuten code");
            criteriaFieldsExt.put(KANJI_JLPT_LEVEL, "JLPT level");
            criteriaFieldsExt.put(KANJI_MEANING, "Meaning");
            criteriaFieldsExt.put(KANJI_READING_TYPE, "Reading type");
            criteriaFieldsExt.put(KANJI_READING, "Reading");
            criteriaFieldsExt.put(KANJI_SKIP, "Skip code");
            criteriaFieldsExt.put(KANJI_STROKE_COUNT, "Stroke count");
            criteriaFieldsExt.put(KANJI_SPAHN_HADAMITZKY, "Spahn-Hadamitky code");
            criteriaFieldsExt.put(KANJI_UNICODE_VALUE, "Unicode");
            criteriaFieldsExt.put(KANJI_VARIANT_TYPE, "Variant type");
            criteriaFieldsExt.put(KANJI_VARIANT_INDEX, "Variant reference");
            listMessages.put(LIST_CRITERIA_FIELD_EXTENDED, criteriaFieldsExt);

            Map<String, String> criteriaCasesString = new HashMap<String, String>();
            criteriaCasesString.put(CASE_EQUALS, "equal to");
            criteriaCasesString.put(CASE_NOT_EQUALS, "not equal to");
            // criteriaCasesString.put(CASE_NULL, "null");
            listMessages.put(LIST_CRITERIA_CASES_STRING, criteriaCasesString);

            Map<String, String> criteriaCasesInteger = new HashMap<String, String>();
            criteriaCasesInteger.put(CASE_EQUALS, "equal to");
            // criteriaCasesInteger.put(CASE_GREATHER, "greather than");
            criteriaCasesInteger.put(CASE_EQUALS_OR_GREATHER, "equal or greather than");
            // criteriaCasesInteger.put(CASE_LESS, "less than");
            criteriaCasesInteger.put(CASE_EQUALS_OR_LESS, "equal or less than");
            criteriaCasesInteger.put(CASE_NOT_EQUALS, "not equal to");
            // criteriaCasesInteger.put(CASE_NULL, "null");
            listMessages.put(LIST_CRITERIA_CASES_INT, criteriaCasesInteger);

            Map<String, String> criteriaValuesJis = new HashMap<String, String>();
            criteriaValuesJis.put(JIS_208, "JIS X 208");
            criteriaValuesJis.put(JIS_212, "JIS X 212");
            criteriaValuesJis.put(JIS_213, "JIS X 213");
            listMessages.put(LIST_CRITERIA_VALUES_JIS, criteriaValuesJis);

            Map<String, String> criteriaValuesDic = new HashMap<String, String>();
            criteriaValuesDic.put(DIC_nelson_c,"Modern Reader’s Japanese-English Character Dictionary");
            criteriaValuesDic.put(DIC_nelson_n,"The New Nelson Japanese-English Character Dictionary");
            criteriaValuesDic.put(DIC_halpern_njecd,"New Japanese-English Character Dictionary");
            criteriaValuesDic.put(DIC_halpern_kkld,"Kanji Learners Dictionary");
            criteriaValuesDic.put(DIC_heisig,"Remembering The  Kanji");
            criteriaValuesDic.put(DIC_gakken, "New Dictionary of Kanji Usage");
            criteriaValuesDic.put(DIC_oneill_names, "Japanese Names");
            criteriaValuesDic.put(DIC_oneill_kk, "Essential Kanji");
            criteriaValuesDic.put(DIC_moro, "Daikanwajiten");
            criteriaValuesDic.put(DIC_henshall, "A Guide To Remembering Japanese Characters");
            criteriaValuesDic.put(DIC_sh_kk, "Kanji and Kana");
            criteriaValuesDic.put(DIC_sakade, "Sakade's A Guide To Reading and Writing Japanese");
            criteriaValuesDic.put(DIC_jf_cards, "Japanese Kanji Flashcards");
            criteriaValuesDic.put(DIC_henshall3, "Henshall's A Guide To Reading and Writing Japanese");
            criteriaValuesDic.put(DIC_tutt_cards, "Tuttle Kanji Cards");
            criteriaValuesDic.put(DIC_crowley, "The Kanji Way to Japanese Language Power");
            criteriaValuesDic.put(DIC_kanji_in_context, "Kanji in Context");
            criteriaValuesDic.put(DIC_busy_people, "Japanese For Busy People");
            criteriaValuesDic.put(DIC_kodansha_compact, "Kodansha Compact Kanji Guide");
            criteriaValuesDic.put(DIC_maniette, "Les Kanjis dans la tete");
            criteriaValuesDic.put(DIC_kanji_basic_book, "Kanji Basic Book");
            listMessages.put(LIST_CRITERIA_VALUES_DIC,criteriaValuesDic);

            Map<String, String> criteriaValuesReadingTypeBasic = new HashMap<String, String>();
            criteriaValuesReadingTypeBasic.put(READ_JA_ON, "on-yomi");
            criteriaValuesReadingTypeBasic.put(READ_JA_KUN, "kun-yomi");
            listMessages.put(LIST_CRITERIA_VALUES_READING_BASIC, criteriaValuesReadingTypeBasic);

            Map<String, String> criteriaValuesReadingTypeExt = new HashMap<String, String>();
            criteriaValuesReadingTypeExt.put(READ_JA_ON, "on-yomi");
            criteriaValuesReadingTypeExt.put(READ_JA_KUN, "kun-yomi");
            criteriaValuesReadingTypeExt.put(READ_NANORI, "nanori");
            // criteriaValuesReadingTypeExt.put(READ_PINYIN, "pinyin");
            // criteriaValuesReadingTypeExt.put(READ_KOREAN_H, "korean hangul");
            // criteriaValuesReadingTypeExt.put(READ_KOREAN_R, "korean romanized");
            listMessages.put(LIST_CRITERIA_VALUES_READING_EXTENDED, criteriaValuesReadingTypeExt);

            Map<String, String> languagesSupported = new HashMap<String, String>();
            languagesSupported.put(LANGUAJE_EN, "English");
            languagesSupported.put(LANGUAJE_ES, "Spanish");
            listMessages.put(LIST_LANGUAGES_SUPPORTED, languagesSupported);

            // ORDENABLE PROPERTIES

            Map<String, String> ordenableFields = new HashMap<String, String>();

            ordenableFields.put(ORDER_GRAPH, "Asterisk Graph");
            ordenableFields.put(ORDER_CLASSIC_RADICAL, "Classic Radical");
            ordenableFields.put(ORDER_DE_ROO, "De Roo Code");
            ordenableFields.put(ORDER_DIC_INDEX, "Dictionary Reference");
            ordenableFields.put(ORDER_MEANING, "First Meaning");
            ordenableFields.put(ORDER_READING, "First Reading");
            ordenableFields.put(ORDER_FREQUENCY, "Frequency");
            ordenableFields.put(ORDER_FOUR_CORNER, "Four Corner Code");
            ordenableFields.put(ORDER_GRADE, "Grade");
            ordenableFields.put(ORDER_JIS_CHARSET, "JIS charset");
            ordenableFields.put(ORDER_JIS_CODE, "JIS code");
            // ordenableFields.put(ORDER_CLASSIC_NELSON, "Nelson Radical");
            ordenableFields.put(ORDER_SKIP, "SKIP Code");
            ordenableFields.put(ORDER_STROKE_COUNT, "Stroke number");
            ordenableFields.put(ORDER_SPAHN_HADAMITZKY, "Spahn-Hadamitzky code");
            ordenableFields.put(ORDER_UNICODE_VALUE, "Unicode");
            listMessages.put(LIST_ORDENABLE_FIELDS, ordenableFields);

            Map<String, String> ordenableMethodsStr = new HashMap<String, String>();
            ordenableMethodsStr.put(ORDERMETHODSSTR_ALPHABETICALLY, "Alphabetically");
            ordenableMethodsStr.put(ORDERMETHODSSTR_ALPHABETICALLY_INVERSE, "Alphabetically inverse");
            ordenableMethodsStr.put(ORDERMETHODSSTR_RAMDOM, "Ramdom");
            listMessages.put(LIST_ORDENABLE_METHODS_STRING, ordenableMethodsStr);

            Map<String, String> ordenableMethodsInt = new HashMap<String, String>();
            ordenableMethodsInt.put(ORDERMETHODSINT_FROM_LEAST_TO_GREATEST, "From least to greatest");
            ordenableMethodsInt.put(ORDERMETHODSINT_FROM_GREATEST_TO_LEAST, "From greatest to least");
            ordenableMethodsInt.put(ORDERMETHODSINT_RAMDOM, "Ramdom");
            listMessages.put(LIST_ORDENABLE_METHODS_INTEGER, ordenableMethodsInt);

            Map<String, String> radicals = new HashMap<String, String>();
            radicals.put(KANJI_CLASSIC_RADICAL + "_001", "001 - 一");
            radicals.put(KANJI_CLASSIC_RADICAL + "_002", "002 - 丨");
            radicals.put(KANJI_CLASSIC_RADICAL + "_003", "003 - 丶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_004", "004 - 丿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_005", "005 - 乙");
            radicals.put(KANJI_CLASSIC_RADICAL + "_006", "006 - 亅");
            radicals.put(KANJI_CLASSIC_RADICAL + "_007", "007 - 二");
            radicals.put(KANJI_CLASSIC_RADICAL + "_008", "008 - 亠");
            radicals.put(KANJI_CLASSIC_RADICAL + "_009", "009 - 人");
            radicals.put(KANJI_CLASSIC_RADICAL + "_010", "010 - 儿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_011", "011 - 入");
            radicals.put(KANJI_CLASSIC_RADICAL + "_012", "012 - 八");
            radicals.put(KANJI_CLASSIC_RADICAL + "_013", "013 - 冂");
            radicals.put(KANJI_CLASSIC_RADICAL + "_014", "014 - 冖");
            radicals.put(KANJI_CLASSIC_RADICAL + "_015", "015 - 冫");
            radicals.put(KANJI_CLASSIC_RADICAL + "_016", "016 - 几");
            radicals.put(KANJI_CLASSIC_RADICAL + "_017", "017 - 凵");
            radicals.put(KANJI_CLASSIC_RADICAL + "_018", "018 - 刀");
            radicals.put(KANJI_CLASSIC_RADICAL + "_019", "019 - 力");
            radicals.put(KANJI_CLASSIC_RADICAL + "_020", "020 - 勺");
            radicals.put(KANJI_CLASSIC_RADICAL + "_021", "021 - 匕");
            radicals.put(KANJI_CLASSIC_RADICAL + "_022", "022 - 匚");
            radicals.put(KANJI_CLASSIC_RADICAL + "_023", "023 - 匸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_024", "024 - 十");
            radicals.put(KANJI_CLASSIC_RADICAL + "_025", "025 - 卜");
            radicals.put(KANJI_CLASSIC_RADICAL + "_026", "026 - 卩");
            radicals.put(KANJI_CLASSIC_RADICAL + "_027", "027 - 厂");
            radicals.put(KANJI_CLASSIC_RADICAL + "_028", "028 - 厶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_029", "029 - 又");
            radicals.put(KANJI_CLASSIC_RADICAL + "_030", "030 - 口");
            radicals.put(KANJI_CLASSIC_RADICAL + "_031", "031 - 囗");
            radicals.put(KANJI_CLASSIC_RADICAL + "_032", "032 - 土");
            radicals.put(KANJI_CLASSIC_RADICAL + "_033", "033 - 士");
            radicals.put(KANJI_CLASSIC_RADICAL + "_034", "034 - 夂");
            radicals.put(KANJI_CLASSIC_RADICAL + "_035", "035 - 夊");
            radicals.put(KANJI_CLASSIC_RADICAL + "_036", "036 - 夕");
            radicals.put(KANJI_CLASSIC_RADICAL + "_037", "037 - 大");
            radicals.put(KANJI_CLASSIC_RADICAL + "_038", "038 - 女");
            radicals.put(KANJI_CLASSIC_RADICAL + "_039", "039 - 子");
            radicals.put(KANJI_CLASSIC_RADICAL + "_040", "040 - 宀");
            radicals.put(KANJI_CLASSIC_RADICAL + "_041", "041 - 寸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_042", "042 - 小");
            radicals.put(KANJI_CLASSIC_RADICAL + "_043", "043 - 尢");
            radicals.put(KANJI_CLASSIC_RADICAL + "_044", "044 - 尸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_045", "045 - 屮");
            radicals.put(KANJI_CLASSIC_RADICAL + "_046", "046 - 山");
            radicals.put(KANJI_CLASSIC_RADICAL + "_047", "047 - 巛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_048", "048 - 工");
            radicals.put(KANJI_CLASSIC_RADICAL + "_049", "049 - 己");
            radicals.put(KANJI_CLASSIC_RADICAL + "_050", "050 - 巾");
            radicals.put(KANJI_CLASSIC_RADICAL + "_051", "051 - 干");
            radicals.put(KANJI_CLASSIC_RADICAL + "_052", "052 - 幺");
            radicals.put(KANJI_CLASSIC_RADICAL + "_053", "053 - 广");
            radicals.put(KANJI_CLASSIC_RADICAL + "_054", "054 - 廴");
            radicals.put(KANJI_CLASSIC_RADICAL + "_055", "055 - 廾");
            radicals.put(KANJI_CLASSIC_RADICAL + "_056", "056 - 弋");
            radicals.put(KANJI_CLASSIC_RADICAL + "_057", "057 - 弓");
            radicals.put(KANJI_CLASSIC_RADICAL + "_058", "058 - 彐");
            radicals.put(KANJI_CLASSIC_RADICAL + "_059", "059 - 彡");
            radicals.put(KANJI_CLASSIC_RADICAL + "_060", "060 - 彳");
            radicals.put(KANJI_CLASSIC_RADICAL + "_061", "061 - 心");
            radicals.put(KANJI_CLASSIC_RADICAL + "_062", "062 - 戈");
            radicals.put(KANJI_CLASSIC_RADICAL + "_063", "063 - 戶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_064", "064 - 手");
            radicals.put(KANJI_CLASSIC_RADICAL + "_065", "065 - ⺙");
            radicals.put(KANJI_CLASSIC_RADICAL + "_066", "066 - 攴");
            radicals.put(KANJI_CLASSIC_RADICAL + "_067", "067 - 文");
            radicals.put(KANJI_CLASSIC_RADICAL + "_068", "068 - 斗");
            radicals.put(KANJI_CLASSIC_RADICAL + "_069", "069 - 斤");
            radicals.put(KANJI_CLASSIC_RADICAL + "_070", "070 - 方");
            radicals.put(KANJI_CLASSIC_RADICAL + "_071", "071 - 无");
            radicals.put(KANJI_CLASSIC_RADICAL + "_072", "072 - 日");
            radicals.put(KANJI_CLASSIC_RADICAL + "_073", "073 - 曰");
            radicals.put(KANJI_CLASSIC_RADICAL + "_074", "074 - 月");
            radicals.put(KANJI_CLASSIC_RADICAL + "_075", "075 - 木");
            radicals.put(KANJI_CLASSIC_RADICAL + "_076", "076 - 欠");
            radicals.put(KANJI_CLASSIC_RADICAL + "_077", "077 - 止");
            radicals.put(KANJI_CLASSIC_RADICAL + "_078", "078 - 歹");
            radicals.put(KANJI_CLASSIC_RADICAL + "_079", "079 - 殳");
            radicals.put(KANJI_CLASSIC_RADICAL + "_080", "080 - 毋");
            radicals.put(KANJI_CLASSIC_RADICAL + "_081", "081 - 比");
            radicals.put(KANJI_CLASSIC_RADICAL + "_082", "082 - 毛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_083", "083 - 氏");
            radicals.put(KANJI_CLASSIC_RADICAL + "_084", "084 - 气");
            radicals.put(KANJI_CLASSIC_RADICAL + "_085", "085 - 水");
            radicals.put(KANJI_CLASSIC_RADICAL + "_086", "086 - 火");
            radicals.put(KANJI_CLASSIC_RADICAL + "_087", "087 - 爪");
            radicals.put(KANJI_CLASSIC_RADICAL + "_088", "088 - 父");
            radicals.put(KANJI_CLASSIC_RADICAL + "_089", "089 - 爻");
            radicals.put(KANJI_CLASSIC_RADICAL + "_090", "090 - 爿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_091", "091 - 片");
            radicals.put(KANJI_CLASSIC_RADICAL + "_092", "092 - 牙");
            radicals.put(KANJI_CLASSIC_RADICAL + "_093", "093 - 牛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_094", "094 - 犬");
            radicals.put(KANJI_CLASSIC_RADICAL + "_095", "095 - 玄");
            radicals.put(KANJI_CLASSIC_RADICAL + "_096", "096 - 玉");
            radicals.put(KANJI_CLASSIC_RADICAL + "_097", "097 - 瓜");
            radicals.put(KANJI_CLASSIC_RADICAL + "_098", "098 - 瓦");
            radicals.put(KANJI_CLASSIC_RADICAL + "_099", "099 - 甘");
            radicals.put(KANJI_CLASSIC_RADICAL + "_100", "100 - 生");
            radicals.put(KANJI_CLASSIC_RADICAL + "_101", "101 - 用");
            radicals.put(KANJI_CLASSIC_RADICAL + "_102", "102 - 田");
            radicals.put(KANJI_CLASSIC_RADICAL + "_103", "103 - 疋");
            radicals.put(KANJI_CLASSIC_RADICAL + "_104", "104 - 疒");
            radicals.put(KANJI_CLASSIC_RADICAL + "_105", "105 - 癶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_106", "106 - 白");
            radicals.put(KANJI_CLASSIC_RADICAL + "_107", "107 - 皮");
            radicals.put(KANJI_CLASSIC_RADICAL + "_108", "108 - 皿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_109", "109 - 目");
            radicals.put(KANJI_CLASSIC_RADICAL + "_110", "110 - 矛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_111", "111 - 矢");
            radicals.put(KANJI_CLASSIC_RADICAL + "_112", "112 - 石");
            radicals.put(KANJI_CLASSIC_RADICAL + "_113", "113 - 示");
            radicals.put(KANJI_CLASSIC_RADICAL + "_114", "114 - 禸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_115", "115 - 禾");
            radicals.put(KANJI_CLASSIC_RADICAL + "_116", "116 - 穴");
            radicals.put(KANJI_CLASSIC_RADICAL + "_117", "117 - 立");
            radicals.put(KANJI_CLASSIC_RADICAL + "_118", "118 - 竹");
            radicals.put(KANJI_CLASSIC_RADICAL + "_119", "119 - 米");
            radicals.put(KANJI_CLASSIC_RADICAL + "_120", "120 - 糸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_121", "121 - 缶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_122", "122 - 网");
            radicals.put(KANJI_CLASSIC_RADICAL + "_123", "123 - 羊");
            radicals.put(KANJI_CLASSIC_RADICAL + "_124", "124 - 羽");
            radicals.put(KANJI_CLASSIC_RADICAL + "_125", "125 - 老");
            radicals.put(KANJI_CLASSIC_RADICAL + "_126", "126 - 而");
            radicals.put(KANJI_CLASSIC_RADICAL + "_127", "127 - 耒");
            radicals.put(KANJI_CLASSIC_RADICAL + "_128", "128 - 耳");
            radicals.put(KANJI_CLASSIC_RADICAL + "_129", "129 - 聿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_130", "130 - 肉");
            radicals.put(KANJI_CLASSIC_RADICAL + "_131", "131 - 臣");
            radicals.put(KANJI_CLASSIC_RADICAL + "_132", "132 - 自");
            radicals.put(KANJI_CLASSIC_RADICAL + "_133", "133 - 至");
            radicals.put(KANJI_CLASSIC_RADICAL + "_134", "134 - 臼");
            radicals.put(KANJI_CLASSIC_RADICAL + "_135", "135 - 舌");
            radicals.put(KANJI_CLASSIC_RADICAL + "_136", "136 - 舛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_137", "137 - 舟");
            radicals.put(KANJI_CLASSIC_RADICAL + "_138", "138 - 艮");
            radicals.put(KANJI_CLASSIC_RADICAL + "_139", "139 - 色");
            radicals.put(KANJI_CLASSIC_RADICAL + "_140", "140 - 艸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_141", "141 - 虍");
            radicals.put(KANJI_CLASSIC_RADICAL + "_142", "142 - 虫");
            radicals.put(KANJI_CLASSIC_RADICAL + "_143", "143 - 血");
            radicals.put(KANJI_CLASSIC_RADICAL + "_144", "144 - 行");
            radicals.put(KANJI_CLASSIC_RADICAL + "_145", "145 - 衣");
            radicals.put(KANJI_CLASSIC_RADICAL + "_146", "146 - 西");
            radicals.put(KANJI_CLASSIC_RADICAL + "_147", "147 - 見");
            radicals.put(KANJI_CLASSIC_RADICAL + "_148", "148 - 角");
            radicals.put(KANJI_CLASSIC_RADICAL + "_149", "149 - 言");
            radicals.put(KANJI_CLASSIC_RADICAL + "_150", "150 - 谷");
            radicals.put(KANJI_CLASSIC_RADICAL + "_151", "151 - 豆");
            radicals.put(KANJI_CLASSIC_RADICAL + "_152", "152 - 豕");
            radicals.put(KANJI_CLASSIC_RADICAL + "_153", "153 - 豸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_154", "154 - 貝");
            radicals.put(KANJI_CLASSIC_RADICAL + "_155", "155 - 赤");
            radicals.put(KANJI_CLASSIC_RADICAL + "_156", "156 - 走");
            radicals.put(KANJI_CLASSIC_RADICAL + "_157", "157 - 足");
            radicals.put(KANJI_CLASSIC_RADICAL + "_158", "158 - 身");
            radicals.put(KANJI_CLASSIC_RADICAL + "_159", "159 - 車");
            radicals.put(KANJI_CLASSIC_RADICAL + "_160", "160 - 辛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_161", "161 - 辰");
            radicals.put(KANJI_CLASSIC_RADICAL + "_162", "162 - 辵");
            radicals.put(KANJI_CLASSIC_RADICAL + "_163", "163 - 邑");
            radicals.put(KANJI_CLASSIC_RADICAL + "_164", "164 - 酉");
            radicals.put(KANJI_CLASSIC_RADICAL + "_165", "165 - 釆");
            radicals.put(KANJI_CLASSIC_RADICAL + "_166", "166 - 里");
            radicals.put(KANJI_CLASSIC_RADICAL + "_167", "167 - 金");
            radicals.put(KANJI_CLASSIC_RADICAL + "_168", "168 - 長");
            radicals.put(KANJI_CLASSIC_RADICAL + "_169", "169 - 門");
            radicals.put(KANJI_CLASSIC_RADICAL + "_170", "170 - 阜");
            radicals.put(KANJI_CLASSIC_RADICAL + "_171", "171 - 隶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_172", "172 - 隹");
            radicals.put(KANJI_CLASSIC_RADICAL + "_173", "173 - 雨");
            radicals.put(KANJI_CLASSIC_RADICAL + "_174", "174 - 靑");
            radicals.put(KANJI_CLASSIC_RADICAL + "_175", "175 - 非");
            radicals.put(KANJI_CLASSIC_RADICAL + "_176", "176 - 面");
            radicals.put(KANJI_CLASSIC_RADICAL + "_177", "177 - 革");
            radicals.put(KANJI_CLASSIC_RADICAL + "_178", "178 - 韋");
            radicals.put(KANJI_CLASSIC_RADICAL + "_179", "179 - 韭");
            radicals.put(KANJI_CLASSIC_RADICAL + "_180", "180 - 音");
            radicals.put(KANJI_CLASSIC_RADICAL + "_181", "181 - 頁");
            radicals.put(KANJI_CLASSIC_RADICAL + "_182", "182 - 風");
            radicals.put(KANJI_CLASSIC_RADICAL + "_183", "183 - 飛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_184", "184 - 食");
            radicals.put(KANJI_CLASSIC_RADICAL + "_185", "185 - 首");
            radicals.put(KANJI_CLASSIC_RADICAL + "_186", "186 - 香");
            radicals.put(KANJI_CLASSIC_RADICAL + "_187", "187 - 馬");
            radicals.put(KANJI_CLASSIC_RADICAL + "_188", "188 - 骨");
            radicals.put(KANJI_CLASSIC_RADICAL + "_189", "189 - 高");
            radicals.put(KANJI_CLASSIC_RADICAL + "_190", "190 - 髟");
            radicals.put(KANJI_CLASSIC_RADICAL + "_191", "191 - 鬥");
            radicals.put(KANJI_CLASSIC_RADICAL + "_192", "192 - 鬯");
            radicals.put(KANJI_CLASSIC_RADICAL + "_193", "193 - 鬲");
            radicals.put(KANJI_CLASSIC_RADICAL + "_194", "194 - 鬼");
            radicals.put(KANJI_CLASSIC_RADICAL + "_195", "195 - 魚");
            radicals.put(KANJI_CLASSIC_RADICAL + "_196", "196 - 鳥");
            radicals.put(KANJI_CLASSIC_RADICAL + "_197", "197 - 鹵");
            radicals.put(KANJI_CLASSIC_RADICAL + "_198", "198 - 鹿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_199", "199 - 麥");
            radicals.put(KANJI_CLASSIC_RADICAL + "_200", "200 - 麻");
            radicals.put(KANJI_CLASSIC_RADICAL + "_201", "201 - 黄");
            radicals.put(KANJI_CLASSIC_RADICAL + "_202", "202 - 黍");
            radicals.put(KANJI_CLASSIC_RADICAL + "_203", "203 - 黑");
            radicals.put(KANJI_CLASSIC_RADICAL + "_204", "204 - 黹");
            radicals.put(KANJI_CLASSIC_RADICAL + "_205", "205 - 黽");
            radicals.put(KANJI_CLASSIC_RADICAL + "_206", "206 - 鼎");
            radicals.put(KANJI_CLASSIC_RADICAL + "_207", "207 - 鼓");
            radicals.put(KANJI_CLASSIC_RADICAL + "_208", "208 - 鼠");
            radicals.put(KANJI_CLASSIC_RADICAL + "_209", "209 - 鼻");
            radicals.put(KANJI_CLASSIC_RADICAL + "_210", "210 - 齊");
            radicals.put(KANJI_CLASSIC_RADICAL + "_211", "211 - 齒");
            radicals.put(KANJI_CLASSIC_RADICAL + "_212", "212 - 龍");
            radicals.put(KANJI_CLASSIC_RADICAL + "_213", "213 - 龜");
            radicals.put(KANJI_CLASSIC_RADICAL + "_214", "214 - 龠");
            listMessages.put(LIST_CRITERIA_VALUES_RADICALS, radicals);

            // RADICAL TYPES

            messages.put(RADICAL_TYPE_original, "Original");
            messages.put(RADICAL_TYPE_top, "Top");
            messages.put(RADICAL_TYPE_bottom, "Bottom");
            messages.put(RADICAL_TYPE_left, "Left");
            messages.put(RADICAL_TYPE_right, "Right");
            messages.put(RADICAL_TYPE_enclosure, "Enclosure");
            messages.put(RADICAL_TYPE_variable, "Variable");

            // MainWindow
            // Window name

            messages.put(WINMAIN_name, "JavaDiKt, the definitive Kanji dictionary");

            // Option tab names
            messages.put(WINMAIN_TAB_CONTROL_About, "More");
            messages.put(WINMAIN_TAB_CONTROL_Criteria, "Criteria");
            messages.put(WINMAIN_TAB_CONTROL_Drawing, "Strokes");
            messages.put(WINMAIN_TAB_CONTROL_Export, "Export");
            messages.put(WINMAIN_TAB_CONTROL_Config, "Options");

            // Border texts
            messages.put(WINMAIN_CRITERIA_BorderTextQueryData, "Search list");
            messages.put(WINMAIN_CRITERIA_BorderTextQueryMaker, "Find kanji where it's");
            messages.put(WINMAIN_CRITERIA_BorderTextQueryControls, "Modify current search");
            messages.put(WINMAIN_RESULTS_BorderTextResults, "Results");
            messages.put(WINMAIN_RESULTS_BorderTextControls, "Windows");
            messages.put(WINMAIN_STROKES_BorderTextControls, "Modify");
            messages.put(WINMAIN_STROKES_BorderTextAndOr, "Add stroke search");
            messages.put(WINMAIN_RESULTS_EXPORT_BorderTextControls, "Modify selection");
            messages.put(WINMAIN_EXPORT_BorderTextExportAs, "1.- Export as");
            messages.put(WINMAIN_EXPORT_BorderTextExportSorting, "2.- Sort as");
            messages.put(WINMAIN_EXPORT_BorderTextExportExtra, "3.- Extra config.");

            // Query column names
            messages.put(WINMAIN_QUERY_DATA_columnAndOr, "And/Or");
            messages.put(WINMAIN_QUERY_DATA_columnField, "Field");
            messages.put(WINMAIN_QUERY_DATA_columnThatFulfilsThat, "Fulfils");
            messages.put(WINMAIN_QUERY_DATA_columnValue, "Value");
            messages.put(WINMAIN_EXPORT_SORTING_columnLevel, "Level");
            messages.put(WINMAIN_EXPORT_SORTING_columnOrderBy, "Order by");
            messages.put(WINMAIN_EXPORT_SORTING_columnField, "Mode");

            //Query table cell string

            messages.put(WINMAIN_QUERY_DATA_cellAndString, "And");
            messages.put(WINMAIN_QUERY_DATA_cellOrString, "Or");
            messages.put(WINMAIN_QUERY_DATA_cellFirstString, "-");

            // Text Label
            messages.put(WINMAIN_CRITERIA_LabelFieldComboBox, "Where");
            messages.put(WINMAIN_CRITERIA_LabelFulfilsComboBox, "is");
            messages.put(WINMAIN_CRITERIA_LabelValueComboBox, "");
            messages.put(WINMAIN_EXPORT_LabelFormatComboBox, "Format:");
            messages.put(WINMAIN_EXPORT_LabelStyleComboBox, "Style:");
            messages.put(WINMAIN_EXPORT_LabelDirButton, "Path:");
            messages.put(WINMAIN_OPTIONS_LabelLanguajeComboBox, "Languaje:");
            messages.put(WINMAIN_OPTIONS_LabelKanjiFileTextField, "Kanji file path:");
            messages.put(WINMAIN_OPTIONS_LabelStrokeFileTextField, "Index file path:");
            messages.put(WINMAIN_OPTIONS_LabelExtendedInfoCheckBox, "<html>Include extended information in kanji info dialog");
            messages.put(WINMAIN_OPTIONS_LabelExtendedKanjiListCheckBox, "<html>Find in extended kanji list (>13000 kanjis)");
            messages.put(WINMAIN_OPTIONS_LabelRomajiCheckBox, "<html>Kana texts in romaji");

            // Button text
            messages.put(WINMAIN_CRITERIA_ButtonQueryMakerAndText, "And");
            messages.put(WINMAIN_CRITERIA_ButtonQueryMakerAddText, "Add");
            messages.put(WINMAIN_CRITERIA_ButtonQueryMakerChangeText, "Change");
            messages.put(WINMAIN_CRITERIA_ButtonQueryMakerOrText, "Or");
            messages.put(WINMAIN_CRITERIA_ButtonQueryControlResetText,"Reset");
            messages.put(WINMAIN_CRITERIA_ButtonQueryControlRemoveText, "Remove entries");
            messages.put(WINMAIN_CRITERIA_ButtonQueryControlModifyText, "Modify");
            messages.put(WINMAIN_CRITERIA_ButtonQueryControlExecuteText, "Find");
            messages.put(WINMAIN_STROKES_ButtonDrawPanelReset, "Reset draw");
            messages.put(WINMAIN_STROKES_ButtonDrawPanelRemoveLastStroke, "Remove last stroke");
            messages.put(WINMAIN_STROKES_ButtonDrawPanelRemoveLastLine, "Remove last line");
            messages.put(WINMAIN_RESULTS_ButtonExportAll,"All");
            messages.put(WINMAIN_RESULTS_ButtonCloseAllWindows, "Close all windows");
            messages.put(WINMAIN_RESULTS_ButtonExportInvert, "Invert");
            messages.put(WINMAIN_RESULTS_ButtonExportReset, "Reset");
            messages.put(WINMAIN_EXPORT_ButtonExportExtraOptions, "Extra Options");
            messages.put(WINMAIN_EXPORT_ButtonSortingAdd, "Add");
            messages.put(WINMAIN_EXPORT_ButtonSortingReset, "Reset");
            messages.put(WINMAIN_EXPORT_ButtonSortingRemove, "Remove");
            messages.put(WINMAIN_EXPORT_ButtonSortingModify, "Modify");
            messages.put(WINMAIN_EXPORT_ButtonExtraRearrange, "Rearrange");
            messages.put(WINMAIN_EXPORT_ButtonExtraOptions, "More...");
            messages.put(WINMAIN_EXPORT_ButtonExport, "Export");
            messages.put(WINMAIN_OPTIONS_ButtonSave, "Save");;
            messages.put(WINMAIN_OPTIONS_ButtonAbout, "About");

            //Export Sorter Adder Dialog
            //Window name
            messages.put(WINEXPORTRESORTERADDER_name, "Add automatic sorter");
            //Buttons
            messages.put(WINEXPORTRESORTERADDER_ButtonAdd, "Add/Change");
            messages.put(WINEXPORTRESORTERADDER_ButtonCancel, "Finish");
            // Labels
            messages.put(WINEXPORTRESORTERADDER_LabelLevel, "Level");
            messages.put(WINEXPORTRESORTERADDER_LabelOrderBy, "Order by");
            messages.put(WINEXPORTRESORTERADDER_LabelField, "Field");

            // Export Rearranger Dialog
            // Window name
            messages.put(WINEXPORTREARRANGER_name, "Rearrange selection");
            // Button
            messages.put(WINEXPORTREARRANGER_acceptButton, "Accept");
            messages.put(WINEXPORTREARRANGER_cancelButton, "Cancel");
            // table column names
            messages.put(WINEXPORTREARRANGER_kanjiLiteral, "Kanji");
            // Special kanji field names
            messages.put(KANJI_FIRST_MEANING, "First meaning");
            messages.put(KANJI_FIRST_READING, "First %s reading");
            messages.put(KANJI_DIC_REFERENCE, "\"%s\" dictionary reference ");

            // Kanji info window
            // Window name
            messages.put(WINKANJIINFO_name, "Kanji information - %s");

            // Panel titles
            messages.put(WINKANJIINFO_PanelTitle_Radical, "Radical");
            messages.put(WINKANJIINFO_PanelTitle_StrokeCount, "Stroke count");
            messages.put(WINKANJIINFO_PanelTitle_Grade, "Gradde");
            messages.put(WINKANJIINFO_PanelTitle_JLPTLevel, "JLPT Level");
            messages.put(WINKANJIINFO_PanelTitle_Frequency, "Frequency");
            messages.put(WINKANJIINFO_PanelTitle_NelsonRadical, "Nelson Rad.");
            messages.put(WINKANJIINFO_PanelTitle_Unicode, "Unicode");
            messages.put(WINKANJIINFO_PanelTitle_JIS, "JIS kuten");
            messages.put(WINKANJIINFO_PanelTitle_DicReferences, "Dictionaire references of this kanji");
            messages.put(WINKANJIINFO_PanelTitle_Graph, "Asterisk graph info");
            messages.put(WINKANJIINFO_PanelTitle_Meanings, "Meanings");
            messages.put(WINKANJIINFO_PanelTitle_Readings, "Readings");
            messages.put(WINKANJIINFO_PanelTitle_Variants, "Variants");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes, "Query codes");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes_SKIP, "SKIP code");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes_FourCorner, "Four corner code");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes_SpahnHadamitzky, "Spahn-Hadamiztky code");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes_DeRoo, "De Roo code");

            // Radical Info window
            // Window name
            messages.put(PANELRADICALINFO_name, "Radical information - %s");
            // Panel names
            messages.put(PANELRADICALINFO_radicalNumber, "Number");
            messages.put(PANELRADICALINFO_radicalType, "Type");
            messages.put(PANELRADICALINFO_radicalNames, "Names");
            messages.put(PANELRADICALINFO_radicalUnicode, "Unicode");
            messages.put(PANELRADICALINFO_radicalUnicodeName, "Unicode name");
            messages.put(PANELRADICALINFO_radicalKangxi, "Kangxi unicode");

            // Stroke order info window
            // Window name
            messages.put(STROKEORDERINFO_name, "Stroke order - %s");

            // INFO DIALOG
            // title
            messages.put(INFODIALOG_title_error, "Error");
            messages.put(INFODIALOG_title_info, "Info");
            messages.put(INFODIALOG_title_newVersion, "New version");
            messages.put(INFODIALOG_title_pleaseWait, "Please wait...");
            // Messages
            messages.put(INFODIALOG_buttonAccept, "Accept");
            messages.put(INFODIALOG_errorMessage_integerCastException, "Please, insert a number.");
            messages.put(INFODIALOG_errorMessage_isNotHex, "Please, insert an hexadecimal number.");
            messages.put(INFODIALOG_infoMessage_kanjiCastException, "The typed character is not a kanji or multiple characters were indroduced.");
            messages.put(INFODIALOG_errorMessage_exportFail, "Error when exporting: %");
            messages.put(INFODIALOG_infoMessage_exportSuccess, "Export finished succesfully.");
            messages.put(INFODIALOG_infoMessage_graphCastException, "<html>Please insert a valid asterisk stroke string. An asterisk stroke string must start with a letter in uppercase and can only contains the following characters: A,B,C,D,E,F,G,H,I,a,b,c,d,e,f,g,h,i");
            messages.put(INFODIALOG_infoMessage_newVersion, "<html>A new version of JavaDiKt(%s) has been released. You can download it <a href=\"http://www.ajaest.net/javadikt/download.html\"> here</a>");
            messages.put(INFODIALOG_infoBorderText, "Kanji Information");
            messages.put(INFODIALOG_errorMessage_cannotOpenFile, "Error when trying to open a file: %s");
            messages.put(INFODIALOG_infoMessage_exporting, "Exporting...");
            // Button
            messages.put(INFODIALOG_infoMessage_openFile, "Open File");

            // ABOUT WINDOW
            // Title
            messages.put(ABOUTWIN_title, "About JavaDiKt");
            messages.put(ABOUTWIN_javadicText, "<html><b>JavaDiKt %s</b> <i>%s</i> with %s %s<br>©2011, Luis Alfonso Arce Gonzalez<br><br>Licensed under <a href=\"http://www.gnu.org/licenses/gpl.html\">GNU General Public License version 3<a>.<br><br>You can find tips, guides and information about newer versions in JavaDiKt's webpage <a href=\"http://www.javadikt.net\">JavaDiKt.net</a></html>");
            messages.put(ABOUTWIN_borderTextLicense, "License");
            messages.put(ABOUTWIN_licenseText,"<html><font size=4>JavaDiKt wouldn't be possible without the efforts and kindness of people who developed the following resources and libraries,people who grants without any cost use permission of their work under the licences below:<br><br><b><a href=\"http://www.csse.monash.edu.au/~jwb/kanjidic.html\">KANJIDIC</a></b>, by Jim Breen of the <i><a href=\"http://www.edrdg.org/\">ELECTRONIC DICTIONARY RESEARCH AND DEVELOPMENT GROUP</a></i>, under <i><a href=\"http://creativecommons.org/licenses/by-sa/3.0/\">Creative Commons Attribution-ShareAlike3.0 Unported</a></i> license .<br><br><b><a href=\"http://www.kanji.org/kanji/dictionaries/skip_permission.htm\">SYSTEM OF KANJI INDEXING BY PATTERNS(SKIP)</a></b>, by Jack Halpern of the <i><a href=\"http://www.kanji.org\">Kanji Dictionary Publishing Society</a></i> under <i><a href=\"http://creativecommons.org/licenses/by-nc-sa/3.0/\">Creative Commons Attribution-Noncommercial-Share Alike 3.0 Unported</a></i> license.<br><br><b><a href=\"http://www.neodatis.org/\">Neodatis ODB</a></b>, library under <a href=\"http://www.gnu.org/licenses/lgpl-2.1.html\">LGPL 2.1</a> license.<br><br><b><a href=\"http://www.xom.nu/\">XOM</a></b>, library under <a href=\"http://www.gnu.org/licenses/lgpl-2.1.html\">LGPL 2.1</a> license. <br><br><b><a href=\"http://www.jgoodies.com/downloads/libraries.html\">JGoodies Looks and JGoodies Common</a></b>, library under the <a href=\"http://www.opensource.org/licenses/bsd-license.html\">BSD</a> license.</font></html>");
            initialized = true;
            buildKeyMap();

        } else if(languaje.toUpperCase().equals("ES")){

            LANGUAGE = "ES";

            if (initialized = true) {
                messages = new HashMap<String, String>();
                listMessages = new HashMap<String, Map<String, String>>();
            }

            // generic

            messages.put(GEN_DIR_FILES_READ_ERROR_C, "**Error al acceder a los archivos en la carpeta \"%s\", puede que no exista, esté bloqueado o no tenga privilegios de lectura suficientes.");
            messages.put(GEN_GENERIC_READ_ERROR_C_colon, "**No se ha podido leer el archivo o carpeta \"%s\": ");
            messages.put(GEN_EXIT_SUCESS_C, "El archivo de sincronización se creó satisfactoriaente en \"%s\".");
            messages.put(GEN_EXIT_SUCCES_TIME_C, "Tiempo de ejecución:[%s:%s:%s]");
            messages.put(GEN_INDEXING, "[Indexando...]\n");
            messages.put(GEN_LENGUAJE_DOES_NOT_EXISTS_C, "**El lenguaje especificado %s no existe o no está definido, inicializando configuración de idioma por defecto.");
            messages.put(GEN_LOCAL_LABEL, "Introduzca la ruta completa del directorio al que se le realizará el archivo local de sincronización y la ruta completa y el nombre del archivo de sincronización.");
            messages.put(GEN_NAME_ERROR, "**El nombre de archivo introducido no es válido.");
            messages.put(GEN_NAME_OR_PATH_ERROR, "**El nombre o ruta de archivo introducido no es válido.");
            messages.put(GEN_NOT_ISO_639_1, "**El lenguaje introducido no tiene formato ISO-639-1, inicializando configuración de idioma por defecto.");
            messages.put(GEN_NOT_MD5_HASH, "No es un hash MD5.");
            messages.put(GEN_NULL_POINTER, "**Error de puntero nulo.");
            messages.put(GEN_PATH_DOES_NOT_EXISTS, "**El directorio introducido no existe.");
            messages.put(GEN_PATH_ERROR, "**El nombre de directorio introducido no es válido.");
            messages.put(GEN_READ_ERROR_C, "**No se puede leer el archivo o carpeta \"%s\", puede que no exista, esté bloqueado o no tenga privilegios de lectura suficientes.");
            messages.put(GEN_RECOVERING_FILE_SYSTEM, "\n[Recuperando sistema de archivos...]\n");
            messages.put(GEN_UNKNOWN_ERROR, "**Se ha producido un error inesperado.");
            messages.put(GEN_WRITING, "\n[Escribiendo archivo de sincronización...]");

            // FILE DESCRIPTIONS

            messages.put(FILE_DESCRIPTION_JDK_KANJI_FILE, "Archivo .jdk de kanjis de JavaDiKt ");
            messages.put(FILE_DESCRIPTION_ZOBJ_STROKE_FILE, "Archivo .zobj de índices de JavaDiKt ");

            // PROGRAM LISTS

            Map<String, String> criteriaFieldsBasic = new HashMap<String, String>();
            criteriaFieldsBasic.put(KANJI_LITERAL, "Kanji");
            criteriaFieldsBasic.put(KANJI_SKIP, "Código SKIP");
            criteriaFieldsBasic.put(KANJI_DIC_INDEX, "Referencia del diccionario");
            criteriaFieldsBasic.put(KANJI_FREQUENCY, "Frecuencia");
            criteriaFieldsBasic.put(KANJI_GRADE, "Grado");
            criteriaFieldsBasic.put(KANJI_JLPT_LEVEL, "Nivel JLPT");
            criteriaFieldsBasic.put(KANJI_DIC_NAME, "Nombre del diccionario");
            criteriaFieldsBasic.put(KANJI_STROKE_COUNT, "Número de trazos");
            criteriaFieldsBasic.put(KANJI_MEANING, "Significado");
            criteriaFieldsBasic.put(KANJI_READING_TYPE, "Tipo de lectura");
            criteriaFieldsBasic.put(KANJI_READING, "Lectura");
            criteriaFieldsBasic.put(KANJI_CLASSIC_RADICAL, "Radical clásico");
            listMessages.put(LIST_CRITERIA_FIELD_BASIC, criteriaFieldsBasic);

            Map<String, String> criteriaFieldsExt = new HashMap<String, String>();
            criteriaFieldsExt.put(KANJI_LITERAL, "Kanji");
            criteriaFieldsExt.put(KANJI_FOUR_CORNER, "Código cuatro esquinas");
            criteriaFieldsExt.put(KANJI_DE_ROO, "Código de Roo");
            criteriaFieldsExt.put(KANJI_SKIP, "Código SKIP");
            criteriaFieldsExt.put(KANJI_SPAHN_HADAMITZKY, "Código Spahn-Hadamitzky");
            criteriaFieldsExt.put(KANJI_FREQUENCY, "Frecuencia");
            criteriaFieldsExt.put(KANJI_GRADE, "Grado");
            criteriaFieldsExt.put(KANJI_GRAPH, "Grafo asterisco");
            criteriaFieldsExt.put(KANJI_DIC_NAME, "Nombre del diccionario");
            criteriaFieldsExt.put(KANJI_DIC_INDEX, "Referencia del diccionario");
            criteriaFieldsExt.put(KANJI_JIS_CHARSET, "Juego de caracteres JIS");
            criteriaFieldsExt.put(KANJI_JIS_CODE, "Código JIS kuten");
            criteriaFieldsExt.put(KANJI_JLPT_LEVEL, "Nivel JLPT");
            criteriaFieldsExt.put(KANJI_STROKE_COUNT, "Número de trazos");
            criteriaFieldsExt.put(KANJI_CLASSIC_RADICAL, "Radical clásico");
            // criteriaFieldsExt.put(KANJI_CLASSIC_NELSON, "Radical Nelson");
            criteriaFieldsExt.put(KANJI_MEANING, "Significado");
            criteriaFieldsExt.put(KANJI_READING_TYPE, "Tipo de lectura");
            criteriaFieldsExt.put(KANJI_VARIANT_TYPE, "Tipo de variante");
            criteriaFieldsExt.put(KANJI_VARIANT_INDEX, "Referencia de la variante");
            criteriaFieldsExt.put(KANJI_READING, "Lectura");
            criteriaFieldsExt.put(KANJI_UNICODE_VALUE, "Unicode");
            listMessages.put(LIST_CRITERIA_FIELD_EXTENDED, criteriaFieldsExt);

            Map<String, String> criteriaCasesString = new HashMap<String, String>();
            criteriaCasesString.put(CASE_EQUALS, "igual que");
            criteriaCasesString.put(CASE_NOT_EQUALS, "no igual que");
            // criteriaCasesString.put(CASE_NULL, "nulo");
            listMessages.put(LIST_CRITERIA_CASES_STRING, criteriaCasesString);

            Map<String, String> criteriaCasesInteger = new HashMap<String, String>();
            criteriaCasesInteger.put(CASE_EQUALS, "igual que");
            // criteriaCasesInteger.put(CASE_GREATHER, "mayor que");
            criteriaCasesInteger.put(CASE_EQUALS_OR_GREATHER, "mayor o igual que");
            // criteriaCasesInteger.put(CASE_LESS, "menor que");
            criteriaCasesInteger.put(CASE_EQUALS_OR_LESS, "menor o igual que");
            criteriaCasesInteger.put(CASE_NOT_EQUALS, "no igual que");
            // criteriaCasesInteger.put(CASE_NULL, "nulo");
            listMessages.put(LIST_CRITERIA_CASES_INT, criteriaCasesInteger);

            Map<String, String> criteriaValuesJis = new HashMap<String, String>();
            criteriaValuesJis.put(JIS_208, "JIS X 208");
            criteriaValuesJis.put(JIS_212, "JIS X 212");
            criteriaValuesJis.put(JIS_213, "JIS X 213");
            listMessages.put(LIST_CRITERIA_VALUES_JIS, criteriaValuesJis);

            Map<String, String> criteriaValuesDic = new HashMap<String, String>();
            criteriaValuesDic.put(DIC_nelson_c, "Modern Reader’s Japanese-English Character Dictionary");
            criteriaValuesDic.put(DIC_nelson_n, "The New Nelson Japanese-English Character Dictionary");
            criteriaValuesDic.put(DIC_halpern_njecd, "New Japanese-English Character Dictionary");
            criteriaValuesDic.put(DIC_halpern_kkld, "Kanji Learners Dictionary");
            criteriaValuesDic.put(DIC_heisig, "Remembering The  Kanji");
            criteriaValuesDic.put(DIC_gakken, "New Dictionary of Kanji Usage");
            criteriaValuesDic.put(DIC_oneill_names, "Japanese Names");
            criteriaValuesDic.put(DIC_oneill_kk, "Essential Kanji");
            criteriaValuesDic.put(DIC_moro, "Daikanwajiten");
            criteriaValuesDic.put(DIC_henshall, "A Guide To Remembering Japanese Characters");
            criteriaValuesDic.put(DIC_sh_kk, "Kanji and Kana");
            criteriaValuesDic.put(DIC_sakade, "Sakade's A Guide To Reading and Writing Japanese");
            criteriaValuesDic.put(DIC_jf_cards, "Japanese Kanji Flashcards");
            criteriaValuesDic.put(DIC_henshall3, "Henshall's A Guide To Reading and Writing Japanese");
            criteriaValuesDic.put(DIC_tutt_cards, "Tuttle Kanji Cards");
            criteriaValuesDic.put(DIC_crowley, "The Kanji Way to Japanese Language Power");
            criteriaValuesDic.put(DIC_kanji_in_context, "Kanji in Context");
            criteriaValuesDic.put(DIC_busy_people, "Japanese For Busy People");
            criteriaValuesDic.put(DIC_kodansha_compact, "Kodansha Compact Kanji Guide");
            criteriaValuesDic.put(DIC_maniette, "Les Kanjis dans la tete");
            criteriaValuesDic.put(DIC_kanji_basic_book, "Kanji Basic Book");
            listMessages.put(LIST_CRITERIA_VALUES_DIC, criteriaValuesDic);

            Map<String, String> criteriaValuesReadingTypeBasic = new HashMap<String, String>();
            criteriaValuesReadingTypeBasic.put(READ_JA_ON, "on-yomi");
            criteriaValuesReadingTypeBasic.put(READ_JA_KUN, "kun-yomi");
            listMessages.put(LIST_CRITERIA_VALUES_READING_BASIC, criteriaValuesReadingTypeBasic);

            Map<String, String> criteriaValuesReadingTypeExt = new HashMap<String, String>();
            criteriaValuesReadingTypeExt.put(READ_JA_ON, "on-yomi");
            criteriaValuesReadingTypeExt.put(READ_JA_KUN, "kun-yomi");
            // criteriaValuesReadingTypeExt.put(READ_PINYIN, "pinyin");
            criteriaValuesReadingTypeExt.put(READ_NANORI, "nanori");
            // criteriaValuesReadingTypeExt.put(READ_KOREAN_H, "coreano hangul");
            // criteriaValuesReadingTypeExt.put(READ_KOREAN_R, "coreano romanizado");
            listMessages.put(LIST_CRITERIA_VALUES_READING_EXTENDED, criteriaValuesReadingTypeExt);

            Map<String, String> languagesSupported = new HashMap<String, String>();
            languagesSupported.put(LANGUAJE_EN, "Inglés");
            languagesSupported.put(LANGUAJE_ES, "Español");
            listMessages.put(LIST_LANGUAGES_SUPPORTED, languagesSupported);

            Map<String, String> ordenableFields = new HashMap<String, String>();

            ordenableFields.put(ORDER_UNICODE_VALUE, "Código unicode");
            ordenableFields.put(ORDER_JIS_CHARSET, "Juego de caracteres JIS");
            ordenableFields.put(ORDER_JIS_CODE, "Código JIS");
            ordenableFields.put(ORDER_DE_ROO, "Código de Roo");
            ordenableFields.put(ORDER_FOUR_CORNER, "Código cuatro esquinas");
            ordenableFields.put(ORDER_SKIP, "Código SKIP");
            ordenableFields.put(ORDER_SPAHN_HADAMITZKY, "Código Spahn-Hadamitzky");
            ordenableFields.put(ORDER_FREQUENCY, "Frecuencia");
            ordenableFields.put(ORDER_GRADE, "Grado");
            ordenableFields.put(ORDER_GRAPH, "Grafo asterisco");
            ordenableFields.put(ORDER_STROKE_COUNT, "Número de trazos");
            ordenableFields.put(ORDER_MEANING, "Primer significado");
            ordenableFields.put(ORDER_READING, "Primera lectura");
            // ordenableFields.put(ORDER_CLASSIC_NELSON, "Radical Nelson");
            ordenableFields.put(ORDER_CLASSIC_RADICAL, "Radical clásico");
            ordenableFields.put(ORDER_DIC_INDEX, "Referencia en diccionario");
            listMessages.put(LIST_ORDENABLE_FIELDS, ordenableFields);

            Map<String, String> ordenableMethodsStr = new HashMap<String, String>();
            ordenableMethodsStr.put(ORDERMETHODSSTR_ALPHABETICALLY, "Alfabético");
            ordenableMethodsStr.put(ORDERMETHODSSTR_ALPHABETICALLY_INVERSE, "Alfabéticamente inverso");
            ordenableMethodsStr.put(ORDERMETHODSSTR_RAMDOM, "Aleatorio");
            listMessages.put(LIST_ORDENABLE_METHODS_STRING, ordenableMethodsStr);

            Map<String, String> ordenableMethodsInt = new HashMap<String, String>();
            ordenableMethodsInt.put(ORDERMETHODSINT_FROM_LEAST_TO_GREATEST, "De menor a mayor");
            ordenableMethodsInt.put(ORDERMETHODSINT_FROM_GREATEST_TO_LEAST, "De mayor a menor");
            ordenableMethodsInt.put(ORDERMETHODSINT_RAMDOM, "Aleatorio");
            listMessages.put(LIST_ORDENABLE_METHODS_INTEGER, ordenableMethodsInt);

            Map<String, String> radicals = new HashMap<String, String>();
            radicals.put(KANJI_CLASSIC_RADICAL + "_001", "001 - 一");
            radicals.put(KANJI_CLASSIC_RADICAL + "_002", "002 - 丨");
            radicals.put(KANJI_CLASSIC_RADICAL + "_003", "003 - 丶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_004", "004 - 丿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_005", "005 - 乙");
            radicals.put(KANJI_CLASSIC_RADICAL + "_006", "006 - 亅");
            radicals.put(KANJI_CLASSIC_RADICAL + "_007", "007 - 二");
            radicals.put(KANJI_CLASSIC_RADICAL + "_008", "008 - 亠");
            radicals.put(KANJI_CLASSIC_RADICAL + "_009", "009 - 人");
            radicals.put(KANJI_CLASSIC_RADICAL + "_010", "010 - 儿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_011", "011 - 入");
            radicals.put(KANJI_CLASSIC_RADICAL + "_012", "012 - 八");
            radicals.put(KANJI_CLASSIC_RADICAL + "_013", "013 - 冂");
            radicals.put(KANJI_CLASSIC_RADICAL + "_014", "014 - 冖");
            radicals.put(KANJI_CLASSIC_RADICAL + "_015", "015 - 冫");
            radicals.put(KANJI_CLASSIC_RADICAL + "_016", "016 - 几");
            radicals.put(KANJI_CLASSIC_RADICAL + "_017", "017 - 凵");
            radicals.put(KANJI_CLASSIC_RADICAL + "_018", "018 - 刀");
            radicals.put(KANJI_CLASSIC_RADICAL + "_019", "019 - 力");
            radicals.put(KANJI_CLASSIC_RADICAL + "_020", "020 - 勺");
            radicals.put(KANJI_CLASSIC_RADICAL + "_021", "021 - 匕");
            radicals.put(KANJI_CLASSIC_RADICAL + "_022", "022 - 匚");
            radicals.put(KANJI_CLASSIC_RADICAL + "_023", "023 - 匸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_024", "024 - 十");
            radicals.put(KANJI_CLASSIC_RADICAL + "_025", "025 - 卜");
            radicals.put(KANJI_CLASSIC_RADICAL + "_026", "026 - 卩");
            radicals.put(KANJI_CLASSIC_RADICAL + "_027", "027 - 厂");
            radicals.put(KANJI_CLASSIC_RADICAL + "_028", "028 - 厶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_029", "029 - 又");
            radicals.put(KANJI_CLASSIC_RADICAL + "_030", "030 - 口");
            radicals.put(KANJI_CLASSIC_RADICAL + "_031", "031 - 囗");
            radicals.put(KANJI_CLASSIC_RADICAL + "_032", "032 - 土");
            radicals.put(KANJI_CLASSIC_RADICAL + "_033", "033 - 士");
            radicals.put(KANJI_CLASSIC_RADICAL + "_034", "034 - 夂");
            radicals.put(KANJI_CLASSIC_RADICAL + "_035", "035 - 夊");
            radicals.put(KANJI_CLASSIC_RADICAL + "_036", "036 - 夕");
            radicals.put(KANJI_CLASSIC_RADICAL + "_037", "037 - 大");
            radicals.put(KANJI_CLASSIC_RADICAL + "_038", "038 - 女");
            radicals.put(KANJI_CLASSIC_RADICAL + "_039", "039 - 子");
            radicals.put(KANJI_CLASSIC_RADICAL + "_040", "040 - 宀");
            radicals.put(KANJI_CLASSIC_RADICAL + "_041", "041 - 寸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_042", "042 - 小");
            radicals.put(KANJI_CLASSIC_RADICAL + "_043", "043 - 尢");
            radicals.put(KANJI_CLASSIC_RADICAL + "_044", "044 - 尸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_045", "045 - 屮");
            radicals.put(KANJI_CLASSIC_RADICAL + "_046", "046 - 山");
            radicals.put(KANJI_CLASSIC_RADICAL + "_047", "047 - 巛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_048", "048 - 工");
            radicals.put(KANJI_CLASSIC_RADICAL + "_049", "049 - 己");
            radicals.put(KANJI_CLASSIC_RADICAL + "_050", "050 - 巾");
            radicals.put(KANJI_CLASSIC_RADICAL + "_051", "051 - 干");
            radicals.put(KANJI_CLASSIC_RADICAL + "_052", "052 - 幺");
            radicals.put(KANJI_CLASSIC_RADICAL + "_053", "053 - 广");
            radicals.put(KANJI_CLASSIC_RADICAL + "_054", "054 - 廴");
            radicals.put(KANJI_CLASSIC_RADICAL + "_055", "055 - 廾");
            radicals.put(KANJI_CLASSIC_RADICAL + "_056", "056 - 弋");
            radicals.put(KANJI_CLASSIC_RADICAL + "_057", "057 - 弓");
            radicals.put(KANJI_CLASSIC_RADICAL + "_058", "058 - 彐");
            radicals.put(KANJI_CLASSIC_RADICAL + "_059", "059 - 彡");
            radicals.put(KANJI_CLASSIC_RADICAL + "_060", "060 - 彳");
            radicals.put(KANJI_CLASSIC_RADICAL + "_061", "061 - 心");
            radicals.put(KANJI_CLASSIC_RADICAL + "_062", "062 - 戈");
            radicals.put(KANJI_CLASSIC_RADICAL + "_063", "063 - 戶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_064", "064 - 手");
            radicals.put(KANJI_CLASSIC_RADICAL + "_065", "065 - ⺙");
            radicals.put(KANJI_CLASSIC_RADICAL + "_066", "066 - 攴");
            radicals.put(KANJI_CLASSIC_RADICAL + "_067", "067 - 文");
            radicals.put(KANJI_CLASSIC_RADICAL + "_068", "068 - 斗");
            radicals.put(KANJI_CLASSIC_RADICAL + "_069", "069 - 斤");
            radicals.put(KANJI_CLASSIC_RADICAL + "_070", "070 - 方");
            radicals.put(KANJI_CLASSIC_RADICAL + "_071", "071 - 无");
            radicals.put(KANJI_CLASSIC_RADICAL + "_072", "072 - 日");
            radicals.put(KANJI_CLASSIC_RADICAL + "_073", "073 - 曰");
            radicals.put(KANJI_CLASSIC_RADICAL + "_074", "074 - 月");
            radicals.put(KANJI_CLASSIC_RADICAL + "_075", "075 - 木");
            radicals.put(KANJI_CLASSIC_RADICAL + "_076", "076 - 欠");
            radicals.put(KANJI_CLASSIC_RADICAL + "_077", "077 - 止");
            radicals.put(KANJI_CLASSIC_RADICAL + "_078", "078 - 歹");
            radicals.put(KANJI_CLASSIC_RADICAL + "_079", "079 - 殳");
            radicals.put(KANJI_CLASSIC_RADICAL + "_080", "080 - 毋");
            radicals.put(KANJI_CLASSIC_RADICAL + "_081", "081 - 比");
            radicals.put(KANJI_CLASSIC_RADICAL + "_082", "082 - 毛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_083", "083 - 氏");
            radicals.put(KANJI_CLASSIC_RADICAL + "_084", "084 - 气");
            radicals.put(KANJI_CLASSIC_RADICAL + "_085", "085 - 水");
            radicals.put(KANJI_CLASSIC_RADICAL + "_086", "086 - 火");
            radicals.put(KANJI_CLASSIC_RADICAL + "_087", "087 - 爪");
            radicals.put(KANJI_CLASSIC_RADICAL + "_088", "088 - 父");
            radicals.put(KANJI_CLASSIC_RADICAL + "_089", "089 - 爻");
            radicals.put(KANJI_CLASSIC_RADICAL + "_090", "090 - 爿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_091", "091 - 片");
            radicals.put(KANJI_CLASSIC_RADICAL + "_092", "092 - 牙");
            radicals.put(KANJI_CLASSIC_RADICAL + "_093", "093 - 牛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_094", "094 - 犬");
            radicals.put(KANJI_CLASSIC_RADICAL + "_095", "095 - 玄");
            radicals.put(KANJI_CLASSIC_RADICAL + "_096", "096 - 玉");
            radicals.put(KANJI_CLASSIC_RADICAL + "_097", "097 - 瓜");
            radicals.put(KANJI_CLASSIC_RADICAL + "_098", "098 - 瓦");
            radicals.put(KANJI_CLASSIC_RADICAL + "_099", "099 - 甘");
            radicals.put(KANJI_CLASSIC_RADICAL + "_100", "100 - 生");
            radicals.put(KANJI_CLASSIC_RADICAL + "_101", "101 - 用");
            radicals.put(KANJI_CLASSIC_RADICAL + "_102", "102 - 田");
            radicals.put(KANJI_CLASSIC_RADICAL + "_103", "103 - 疋");
            radicals.put(KANJI_CLASSIC_RADICAL + "_104", "104 - 疒");
            radicals.put(KANJI_CLASSIC_RADICAL + "_105", "105 - 癶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_106", "106 - 白");
            radicals.put(KANJI_CLASSIC_RADICAL + "_107", "107 - 皮");
            radicals.put(KANJI_CLASSIC_RADICAL + "_108", "108 - 皿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_109", "109 - 目");
            radicals.put(KANJI_CLASSIC_RADICAL + "_110", "110 - 矛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_111", "111 - 矢");
            radicals.put(KANJI_CLASSIC_RADICAL + "_112", "112 - 石");
            radicals.put(KANJI_CLASSIC_RADICAL + "_113", "113 - 示");
            radicals.put(KANJI_CLASSIC_RADICAL + "_114", "114 - 禸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_115", "115 - 禾");
            radicals.put(KANJI_CLASSIC_RADICAL + "_116", "116 - 穴");
            radicals.put(KANJI_CLASSIC_RADICAL + "_117", "117 - 立");
            radicals.put(KANJI_CLASSIC_RADICAL + "_118", "118 - 竹");
            radicals.put(KANJI_CLASSIC_RADICAL + "_119", "119 - 米");
            radicals.put(KANJI_CLASSIC_RADICAL + "_120", "120 - 糸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_121", "121 - 缶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_122", "122 - 网");
            radicals.put(KANJI_CLASSIC_RADICAL + "_123", "123 - 羊");
            radicals.put(KANJI_CLASSIC_RADICAL + "_124", "124 - 羽");
            radicals.put(KANJI_CLASSIC_RADICAL + "_125", "125 - 老");
            radicals.put(KANJI_CLASSIC_RADICAL + "_126", "126 - 而");
            radicals.put(KANJI_CLASSIC_RADICAL + "_127", "127 - 耒");
            radicals.put(KANJI_CLASSIC_RADICAL + "_128", "128 - 耳");
            radicals.put(KANJI_CLASSIC_RADICAL + "_129", "129 - 聿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_130", "130 - 肉");
            radicals.put(KANJI_CLASSIC_RADICAL + "_131", "131 - 臣");
            radicals.put(KANJI_CLASSIC_RADICAL + "_132", "132 - 自");
            radicals.put(KANJI_CLASSIC_RADICAL + "_133", "133 - 至");
            radicals.put(KANJI_CLASSIC_RADICAL + "_134", "134 - 臼");
            radicals.put(KANJI_CLASSIC_RADICAL + "_135", "135 - 舌");
            radicals.put(KANJI_CLASSIC_RADICAL + "_136", "136 - 舛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_137", "137 - 舟");
            radicals.put(KANJI_CLASSIC_RADICAL + "_138", "138 - 艮");
            radicals.put(KANJI_CLASSIC_RADICAL + "_139", "139 - 色");
            radicals.put(KANJI_CLASSIC_RADICAL + "_140", "140 - 艸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_141", "141 - 虍");
            radicals.put(KANJI_CLASSIC_RADICAL + "_142", "142 - 虫");
            radicals.put(KANJI_CLASSIC_RADICAL + "_143", "143 - 血");
            radicals.put(KANJI_CLASSIC_RADICAL + "_144", "144 - 行");
            radicals.put(KANJI_CLASSIC_RADICAL + "_145", "145 - 衣");
            radicals.put(KANJI_CLASSIC_RADICAL + "_146", "146 - 西");
            radicals.put(KANJI_CLASSIC_RADICAL + "_147", "147 - 見");
            radicals.put(KANJI_CLASSIC_RADICAL + "_148", "148 - 角");
            radicals.put(KANJI_CLASSIC_RADICAL + "_149", "149 - 言");
            radicals.put(KANJI_CLASSIC_RADICAL + "_150", "150 - 谷");
            radicals.put(KANJI_CLASSIC_RADICAL + "_151", "151 - 豆");
            radicals.put(KANJI_CLASSIC_RADICAL + "_152", "152 - 豕");
            radicals.put(KANJI_CLASSIC_RADICAL + "_153", "153 - 豸");
            radicals.put(KANJI_CLASSIC_RADICAL + "_154", "154 - 貝");
            radicals.put(KANJI_CLASSIC_RADICAL + "_155", "155 - 赤");
            radicals.put(KANJI_CLASSIC_RADICAL + "_156", "156 - 走");
            radicals.put(KANJI_CLASSIC_RADICAL + "_157", "157 - 足");
            radicals.put(KANJI_CLASSIC_RADICAL + "_158", "158 - 身");
            radicals.put(KANJI_CLASSIC_RADICAL + "_159", "159 - 車");
            radicals.put(KANJI_CLASSIC_RADICAL + "_160", "160 - 辛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_161", "161 - 辰");
            radicals.put(KANJI_CLASSIC_RADICAL + "_162", "162 - 辵");
            radicals.put(KANJI_CLASSIC_RADICAL + "_163", "163 - 邑");
            radicals.put(KANJI_CLASSIC_RADICAL + "_164", "164 - 酉");
            radicals.put(KANJI_CLASSIC_RADICAL + "_165", "165 - 釆");
            radicals.put(KANJI_CLASSIC_RADICAL + "_166", "166 - 里");
            radicals.put(KANJI_CLASSIC_RADICAL + "_167", "167 - 金");
            radicals.put(KANJI_CLASSIC_RADICAL + "_168", "168 - 長");
            radicals.put(KANJI_CLASSIC_RADICAL + "_169", "169 - 門");
            radicals.put(KANJI_CLASSIC_RADICAL + "_170", "170 - 阜");
            radicals.put(KANJI_CLASSIC_RADICAL + "_171", "171 - 隶");
            radicals.put(KANJI_CLASSIC_RADICAL + "_172", "172 - 隹");
            radicals.put(KANJI_CLASSIC_RADICAL + "_173", "173 - 雨");
            radicals.put(KANJI_CLASSIC_RADICAL + "_174", "174 - 靑");
            radicals.put(KANJI_CLASSIC_RADICAL + "_175", "175 - 非");
            radicals.put(KANJI_CLASSIC_RADICAL + "_176", "176 - 面");
            radicals.put(KANJI_CLASSIC_RADICAL + "_177", "177 - 革");
            radicals.put(KANJI_CLASSIC_RADICAL + "_178", "178 - 韋");
            radicals.put(KANJI_CLASSIC_RADICAL + "_179", "179 - 韭");
            radicals.put(KANJI_CLASSIC_RADICAL + "_180", "180 - 音");
            radicals.put(KANJI_CLASSIC_RADICAL + "_181", "181 - 頁");
            radicals.put(KANJI_CLASSIC_RADICAL + "_182", "182 - 風");
            radicals.put(KANJI_CLASSIC_RADICAL + "_183", "183 - 飛");
            radicals.put(KANJI_CLASSIC_RADICAL + "_184", "184 - 食");
            radicals.put(KANJI_CLASSIC_RADICAL + "_185", "185 - 首");
            radicals.put(KANJI_CLASSIC_RADICAL + "_186", "186 - 香");
            radicals.put(KANJI_CLASSIC_RADICAL + "_187", "187 - 馬");
            radicals.put(KANJI_CLASSIC_RADICAL + "_188", "188 - 骨");
            radicals.put(KANJI_CLASSIC_RADICAL + "_189", "189 - 高");
            radicals.put(KANJI_CLASSIC_RADICAL + "_190", "190 - 髟");
            radicals.put(KANJI_CLASSIC_RADICAL + "_191", "191 - 鬥");
            radicals.put(KANJI_CLASSIC_RADICAL + "_192", "192 - 鬯");
            radicals.put(KANJI_CLASSIC_RADICAL + "_193", "193 - 鬲");
            radicals.put(KANJI_CLASSIC_RADICAL + "_194", "194 - 鬼");
            radicals.put(KANJI_CLASSIC_RADICAL + "_195", "195 - 魚");
            radicals.put(KANJI_CLASSIC_RADICAL + "_196", "196 - 鳥");
            radicals.put(KANJI_CLASSIC_RADICAL + "_197", "197 - 鹵");
            radicals.put(KANJI_CLASSIC_RADICAL + "_198", "198 - 鹿");
            radicals.put(KANJI_CLASSIC_RADICAL + "_199", "199 - 麥");
            radicals.put(KANJI_CLASSIC_RADICAL + "_200", "200 - 麻");
            radicals.put(KANJI_CLASSIC_RADICAL + "_201", "201 - 黄");
            radicals.put(KANJI_CLASSIC_RADICAL + "_202", "202 - 黍");
            radicals.put(KANJI_CLASSIC_RADICAL + "_203", "203 - 黑");
            radicals.put(KANJI_CLASSIC_RADICAL + "_204", "204 - 黹");
            radicals.put(KANJI_CLASSIC_RADICAL + "_205", "205 - 黽");
            radicals.put(KANJI_CLASSIC_RADICAL + "_206", "206 - 鼎");
            radicals.put(KANJI_CLASSIC_RADICAL + "_207", "207 - 鼓");
            radicals.put(KANJI_CLASSIC_RADICAL + "_208", "208 - 鼠");
            radicals.put(KANJI_CLASSIC_RADICAL + "_209", "209 - 鼻");
            radicals.put(KANJI_CLASSIC_RADICAL + "_210", "210 - 齊");
            radicals.put(KANJI_CLASSIC_RADICAL + "_211", "211 - 齒");
            radicals.put(KANJI_CLASSIC_RADICAL + "_212", "212 - 龍");
            radicals.put(KANJI_CLASSIC_RADICAL + "_213", "213 - 龜");
            radicals.put(KANJI_CLASSIC_RADICAL + "_214", "214 - 龠");
            listMessages.put(LIST_CRITERIA_VALUES_RADICALS, radicals);

            // RADICAL TYPES

            messages.put(RADICAL_TYPE_original, "Original");
            messages.put(RADICAL_TYPE_top, "Arriba");
            messages.put(RADICAL_TYPE_bottom, "Abajo");
            messages.put(RADICAL_TYPE_left, "Izquierda");
            messages.put(RADICAL_TYPE_right, "Derecha");
            messages.put(RADICAL_TYPE_enclosure, "Alrededor");
            messages.put(RADICAL_TYPE_variable, "Variable");

            // MainWinfow
            // Window name
            messages.put(WINMAIN_name, "JavaDiKt, el diccionario de kanjis definitivo");

            // Option tab names
            messages.put(WINMAIN_TAB_CONTROL_About, "Info");
            messages.put(WINMAIN_TAB_CONTROL_Criteria, "Criterio");
            messages.put(WINMAIN_TAB_CONTROL_Drawing, "Trazos");
            messages.put(WINMAIN_TAB_CONTROL_Export, "Exportar");
            messages.put(WINMAIN_TAB_CONTROL_Config, "Opciones");

            // Border texts
            messages.put(WINMAIN_CRITERIA_BorderTextQueryData, "Lista de búsqueda");
            messages.put(WINMAIN_CRITERIA_BorderTextQueryMaker, "Buscar kanji tal que su");
            messages.put(WINMAIN_CRITERIA_BorderTextQueryControls, "Modificar la búsqueda actual");
            messages.put(WINMAIN_RESULTS_BorderTextResults, "Resultados");
            messages.put(WINMAIN_RESULTS_BorderTextControls, "Ventanas");
            messages.put(WINMAIN_STROKES_BorderTextControls, "Modificar");
            messages.put(WINMAIN_STROKES_BorderTextAndOr, "Añadir búsqueda");
            messages.put(WINMAIN_RESULTS_EXPORT_BorderTextControls, "Modificar selección");
            messages.put(WINMAIN_EXPORT_BorderTextExportAs, "1.- Exportar como");
            messages.put(WINMAIN_EXPORT_BorderTextExportSorting, "2.- Ordenar por");
            messages.put(WINMAIN_EXPORT_BorderTextExportExtra, "3.- Config. extra");

            // Query table column names
            messages.put(WINMAIN_QUERY_DATA_columnAndOr, "Y/Ó");
            messages.put(WINMAIN_QUERY_DATA_columnField, "Campo");
            messages.put(WINMAIN_QUERY_DATA_columnThatFulfilsThat, "Cumple");
            messages.put(WINMAIN_QUERY_DATA_columnValue, "Valor");
            messages.put(WINMAIN_EXPORT_SORTING_columnLevel, "Nivel");
            messages.put(WINMAIN_EXPORT_SORTING_columnOrderBy, "Ordenar por");
            messages.put(WINMAIN_EXPORT_SORTING_columnField, "Modo");

            // Query table cell string
            messages.put(WINMAIN_QUERY_DATA_cellAndString, "Y");
            messages.put(WINMAIN_QUERY_DATA_cellOrString, "Ó");
            messages.put(WINMAIN_QUERY_DATA_cellFirstString, "-");

            // Text Label
            messages.put(WINMAIN_CRITERIA_LabelFieldComboBox, "donde");
            messages.put(WINMAIN_CRITERIA_LabelFulfilsComboBox, "sea");
            messages.put(WINMAIN_CRITERIA_LabelValueComboBox, "");
            messages.put(WINMAIN_EXPORT_LabelFormatComboBox, "Formato:");
            messages.put(WINMAIN_EXPORT_LabelStyleComboBox, "Estilo:");
            messages.put(WINMAIN_EXPORT_LabelDirButton, "Ruta:");
            messages.put(WINMAIN_OPTIONS_LabelLanguajeComboBox, "Idioma:");
            messages.put(WINMAIN_OPTIONS_LabelKanjiFileTextField, "Ruta del archivo de kanjis:");
            messages.put(WINMAIN_OPTIONS_LabelStrokeFileTextField, "Ruta del archivo de índices:");
            messages.put(WINMAIN_OPTIONS_LabelExtendedInfoCheckBox, "<html>Incluir información avanzada en el<br>diálogo de información de kanjis");
            messages.put(WINMAIN_OPTIONS_LabelExtendedKanjiListCheckBox, "<html>Buscar en la lista extendida de<br>kanjis (>13000 kanjis)");
            messages.put(WINMAIN_OPTIONS_LabelRomajiCheckBox, "Texto kana romanizado");

            // Button text
            messages.put(WINMAIN_CRITERIA_ButtonQueryMakerAndText, "Y");
            messages.put(WINMAIN_CRITERIA_ButtonQueryMakerAddText, "Añadir");
            messages.put(WINMAIN_CRITERIA_ButtonQueryMakerChangeText, "Cambiar");
            messages.put(WINMAIN_CRITERIA_ButtonQueryMakerOrText, "Ó");
            messages.put(WINMAIN_CRITERIA_ButtonQueryControlResetText, "Reset");
            messages.put(WINMAIN_CRITERIA_ButtonQueryControlRemoveText, "Borrar entrada/s");
            messages.put(WINMAIN_CRITERIA_ButtonQueryControlModifyText, "Modificar");
            messages.put(WINMAIN_CRITERIA_ButtonQueryControlExecuteText, "Buscar");
            messages.put(WINMAIN_STROKES_ButtonDrawPanelReset, "Borrar dibujo");
            messages.put(WINMAIN_STROKES_ButtonDrawPanelRemoveLastStroke, "Borrar último trazo");
            messages.put(WINMAIN_STROKES_ButtonDrawPanelRemoveLastLine, "Borrar última línea");
            messages.put(WINMAIN_RESULTS_ButtonExportAll, "Todos");
            messages.put(WINMAIN_RESULTS_ButtonCloseAllWindows, "Cerrar todas las ventanas");
            messages.put(WINMAIN_RESULTS_ButtonExportInvert, "Invertir");
            messages.put(WINMAIN_RESULTS_ButtonExportReset, "Reset");
            messages.put(WINMAIN_EXPORT_ButtonExportExtraOptions, "Opciones Adicionales");
            messages.put(WINMAIN_EXPORT_ButtonSortingAdd, "Añadir");
            messages.put(WINMAIN_EXPORT_ButtonSortingReset, "Reset");
            messages.put(WINMAIN_EXPORT_ButtonSortingRemove, "Borrar");
            messages.put(WINMAIN_EXPORT_ButtonSortingModify, "Modificar");
            messages.put(WINMAIN_EXPORT_ButtonExtraRearrange, "Reordenar");
            messages.put(WINMAIN_EXPORT_ButtonExtraOptions, "Más...");
            messages.put(WINMAIN_EXPORT_ButtonExport, "Exportar");
            messages.put(WINMAIN_OPTIONS_ButtonSave, "Guardar");;
            messages.put(WINMAIN_OPTIONS_ButtonAbout, "Acerca de");

            // Export Sorter Adder Dialog
            // Window name
            messages.put(WINEXPORTRESORTERADDER_name, "Añadir ordenador automático");
            // Buttons
            messages.put(WINEXPORTRESORTERADDER_ButtonAdd, "Añadir/Cambiar");
            messages.put(WINEXPORTRESORTERADDER_ButtonCancel, "Terminar");
            // Labels
            messages.put(WINEXPORTRESORTERADDER_LabelLevel, "Nivel");
            messages.put(WINEXPORTRESORTERADDER_LabelOrderBy, "Ordenar según");
            messages.put(WINEXPORTRESORTERADDER_LabelField, "Propiedad");

            // Export Rearranger Dialog
            // Window name
            messages.put(WINEXPORTREARRANGER_name, "Reordenar selección");
            // Button
            messages.put(WINEXPORTREARRANGER_acceptButton, "Aceptar");
            messages.put(WINEXPORTREARRANGER_cancelButton, "Cancelar");
            // table column names
            messages.put(WINEXPORTREARRANGER_kanjiLiteral, "Kanji");
            // Special kanji field names
            messages.put(KANJI_FIRST_MEANING, "Primer significado");
            messages.put(KANJI_FIRST_READING, "Primera lectura %s");
            messages.put(KANJI_DIC_REFERENCE, "Referencia en diccionario \"%s\"");

            // Kanji info window
            // Window name
            messages.put(WINKANJIINFO_name, "Información de kanji - %s");
            // Panel titles
            messages.put(WINKANJIINFO_PanelTitle_Radical, "Radical");
            messages.put(WINKANJIINFO_PanelTitle_StrokeCount, "Nº de trazos");
            messages.put(WINKANJIINFO_PanelTitle_Grade, "Grado");
            messages.put(WINKANJIINFO_PanelTitle_JLPTLevel, "Nivel JLPT");
            messages.put(WINKANJIINFO_PanelTitle_Frequency, "Frecuencia");
            messages.put(WINKANJIINFO_PanelTitle_NelsonRadical, "Rad. Nelson");
            messages.put(WINKANJIINFO_PanelTitle_Unicode, "Unicode");
            messages.put(WINKANJIINFO_PanelTitle_JIS, "JIS kuten");
            messages.put(WINKANJIINFO_PanelTitle_DicReferences, "Referencias de este kanji en diccionarios");
            messages.put(WINKANJIINFO_PanelTitle_Graph, "Grafo asterisco");
            messages.put(WINKANJIINFO_PanelTitle_Meanings, "Significados");
            messages.put(WINKANJIINFO_PanelTitle_Readings, "Lecturas");
            messages.put(WINKANJIINFO_PanelTitle_Variants, "Variantes");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes, "Códigos");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes_SKIP, "SKIP");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes_FourCorner, "Cuatro esquinas");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes_SpahnHadamitzky, "Spahn-Hadamiztky");
            messages.put(WINKANJIINFO_PanelTitle_QueryCodes_DeRoo, "De Roo");

            // Radical Info window
            // Window name
            messages.put(PANELRADICALINFO_name, "Información del radical - %s");
            // Panel names
            messages.put(PANELRADICALINFO_radicalNumber, "Número");
            messages.put(PANELRADICALINFO_radicalType, "Tipo");
            messages.put(PANELRADICALINFO_radicalNames, "Nombres");
            messages.put(PANELRADICALINFO_radicalUnicode, "Unicode");
            messages.put(PANELRADICALINFO_radicalUnicodeName, "Nombre unicode");
            messages.put(PANELRADICALINFO_radicalKangxi, "Unicode kangxi");

            // Stroke order info window
            // Window name
            messages.put(STROKEORDERINFO_name, "Orden de trazos - %s");

            // INFO DIALOG
            // title
            messages.put(INFODIALOG_title_error, "Error");
            messages.put(INFODIALOG_title_info, "Info");
            messages.put(INFODIALOG_title_newVersion, "Nueva versión");
            messages.put(INFODIALOG_title_pleaseWait, "Espere...");
            // Messages
            messages.put(INFODIALOG_buttonAccept, "Aceptar");
            messages.put(INFODIALOG_errorMessage_integerCastException, "Inserte un número.");
            messages.put(INFODIALOG_errorMessage_isNotHex, "Inserte un número en hexadecimal.");
            messages.put(INFODIALOG_errorMessage_exportFail, "Error al exportar: %s");
            messages.put(INFODIALOG_infoMessage_exportSuccess, "Exportación finalizada con éxito");
            messages.put(INFODIALOG_infoMessage_kanjiCastException, "El caracter introducido no es un kanji o fueron introducidos más de un caracter.");
            messages.put(INFODIALOG_infoMessage_graphCastException, "<html>Inserte una cadena de trazos-asterisco válida. Una cadena grafo-asterisco debe empezar por minúscula y solo puede contener los caracteres A,B,C,D,E,F,G,H,I,a,b,c,d,e,f,g,h,i");
            messages.put(INFODIALOG_infoMessage_newVersion, "<html>Ha sido lanzada una nueva versión de JavaDiKt(%s). Puede descargarla desde <a href=\"http://www.ajaest.net/javadikt/download.html\">aquí</a>.");
            messages.put(INFODIALOG_errorMessage_cannotOpenFile, "Error al intentar abrir el archivo: %s");
            messages.put(INFODIALOG_infoMessage_exporting, "Exportando...");
            // Button
            messages.put(INFODIALOG_infoMessage_openFile, "Abrir archivo");
            //Border
            messages.put(INFODIALOG_infoBorderText, "Información sobre kanji");
            // Info
            //messages.put(INFO_KANJI_CLASSIC_NELSON, "INFO_KANJI_CLASSIC_NELSON");
            messages.put(INFO_KANJI_CLASSIC_RADICAL, "<font size=\"6\"><b>Radical clásico</b></font><br><br>El radical clásico o radical principal de un kanji se define como <b>aquel de los kanjis o grupo significativo de trazos que forma parte de un carácter y que tiene más peso dentro de éste</b>. Si el carácter está compuesto por un único kanji o grupo significativo de trazos, el radical principal de dicho kanji es el propio kanji, como “月” de 月.<br><br>Reconocer a un kanji por su radical es evidente más complicado que reconocerlo por su número de trazos dos motivos:<br><br>Primero, es difícil definir la expresión “que tiene más peso”. En muchas ocasiones, el elemento de más peso puede considerarse el que más espacio ocupa en un cuadrado imaginario que rodea al kanji, el que está en la posición más privilegiada, el que aporta mayor significado, o el que, en competencia con otros radicales, tiene “prioridad” en la <a href=\"http://nuthatch.com/kanji/demo/radicals.html\" target=\"_blank\">lista oficial</a>.<br><br>Y segundo, porque definir “conjunto significativo de trazos” también es complicado. Gran parte de los radicales tienen más de una forma de escribirse, en la mayoría de los casos debido a la simplificación por causa de la escritura rápida.<br><br>Aunque reconocer un kanji por su radical no sea necesariamente una tarea sencilla, podemos acotar mucho una búsqueda si logramos averiguarlo. La lista oficial de radicales clásicos contiene 214 radicales, lo que convierte la media de kanjis obtenida al consultar sobre un radical en el diccionario de cuatro mil kanjis a diecinueve por radical aproximadamente (aunque el número de kanjis por radical está muy poco equilibrado).<br><br>Por ejemplo, “山” es radical de 岩. Sin embargo es difícil saber por qué “山” es el radical y no lo es “石”. Puede que “山” tenga prioridad sobre “石” por varias razones, como que “山” esté mejor posicionado en la lista oficial, que su significado de “montaña” gane al de “piedra” o que se encuentra más arriba espacialmente que el otro elemento. Este ejemplo deja patente que para diferenciar el radical de un kanji, hay que tener cierta intuición y destreza que se adquiere con la práctica.<br><br>");
            messages.put(INFO_KANJI_DE_ROO, "<font size=\"6\"><b>Código \"de Roo\"</b></font><br><br>El código <b>de Roo</b> fue presentado por Joshep de Roo en el diccionario <a href=\"http://books.google.es/books?id=AM_jPwAACAAJ&dq=%22de+roo%22+kanji&hl=es&ei=OYqWTPzSFMfGswaXrphb&sa=X&oi=book_result&ct=result&\" target=\"_blank\"><i>2001 kanji</i></a> y tiene el objetivo de facilitar la búsqueda de kanjis a principiantes. Tiene la forma <b>NNMM</b> donde <b>NN</b> representa al radical más alto o el más alto a la izquierda y <b>MM</b> representa el radical más bajo o el más bajo a la derecha, ignorando los radicales envolventes más externos. Ambos números han de ser consultados en <a href=\"http://www.whiteknightlogic.net/kanjidb/search/searchderoohelp.php\" target=\"_blank\">esta tabla</a> dividida en subgrupos de manera similar al método de las cuatro esquinas. Algunos ejemplos:<br><br>生　2472　囚 1262　個 2177　道 979　縫 2755");
            messages.put(INFO_KANJI_DIC_NAME, "<font size=\"6\"><b>Nombre del diccionario</b></font><br><br>Ver \"Referencia del diccionario\".");
            messages.put(INFO_KANJI_FOUR_CORNER, "<font size=\"6\"><b>Código de las cuatro esquinas</b></font><br><br>El <b>método de las cuatro esquinas</b> fue desarrollado por Wang Yun-Wuu en los años 20 y como su propio nombre indica se basa en el contenido de las esquinas del kanji. El código es de la forma ABCD.n, donde A representa la esquina superior izquierda, B la superior derecha, C la inferior izquierda, D la inferior derecha y .n un punto junto a un número de desambiguación opcionales que hace al código unívoco, similar al del código Spahn-Hadamitzky.Para conseguir la clave, tenemos que identificar el radical o trazo más cercano a cada una de las esquinas. El número de la esquina es el del elemento de la tabla a continuación que más se parezca a al elemento de la esquina:<br><br><p style=\"margin-left: 1.25cm;\">0　Sombrero　 <font color=\"#ff0000\">亠</font><br>1　Línea horizontal　 <font color=\"#ff0000\">一</font><br>2　Línea vertical　 <font color=\"#ff0000\">｜</font><br>3　Punto　 <font color=\"#ff0000\">丶</font><br>4　Cruz　 <font color=\"#ff0000\">十</font><br>5　Brocheta　 <font color=\"#ff0000\">キ</font><br>6　Caja　 <font color=\"#ff0000\">口</font><br>7　Ángulo　<font color=\"#ff0000\"> 厂</font><br>8　Hachi　 <font color=\"#ff0000\">八</font><br>9　Chiisai　<font color=\"#ff0000\"> 小</font><br></p><br><br>En realidad el método es complicado, tiene más reglas, puedes verlas <a href=\"http://www.whiteknightlogic.net/kanjidb/search/searchfourcornerhelp.php\" target=\"_blank\">aquí</a>. Algunos ejemplos:<br><br>仕 2421 行 2122 歴 7121 魚 2733 詞 0762 同 7722 橋 4292");
            messages.put(INFO_KANJI_FREQUENCY, "<font size=\"6\"><b>Frecuencia</b></font><br><br>Los <b>2500 kanjis más usados</b> en japonés moderno están ordenados unívocamente(es decir, dos kanjis no pueden tener el mismo número de frecuencia) en el campo frecuencia por su posición en el ranking de kanjis más usados según un sondeo realizado entre los periódicos japoneses. ");
            messages.put(INFO_KANJI_GRADE, "<font size=\"6\"><b>Grado</b></font><br><br>El campo “grado” hace referencia a la lista a la que el kanji pertenece de las distintas listas oficiales publicadas por distintas administraciones de Japón. Pueden ser del 1 al 10, menos el 7, que no referencia a ningún kanji. Así pues, las equivalencias son:<br><br><table border=\"1\" align=\"center\"><tr>		<td width=\"239\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\"><br>			</p>		</td>		<td width=\"153\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\"><br>			</p>		</td>		<td width=\"104\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\"><br>			</p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">Grado</p>		</td>	</tr>	<tr>		<td rowspan=\"7\" width=\"239\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\"><a href=\"http://es.wikipedia.org/wiki/J%C5%8Dy%C5%8D_kanji\" target=\"_blank\">Jōyō			kanji</a></p>		</td>		<td rowspan=\"6\" width=\"153\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\"><a href=\"http://en.wikipedia.org/wiki/Ky%C5%8Diku_kanji\" target=\"_blank\">Kyōiku			kanji</a></p>		</td>		<td width=\"104\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">Nivel 1</p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">1</p>		</td>	</tr>	<tr>		<td width=\"104\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">Nivel 2</p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">2</p>		</td>	</tr>	<tr>		<td width=\"104\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">Nivel 3</p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">3</p>		</td>	</tr>	<tr>		<td width=\"104\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">Nivel 4</p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">4</p>		</td>	</tr>	<tr>		<td width=\"104\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">Nivel 5</p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">5</p>		</td>	</tr>	<tr>		<td width=\"104\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">Nivel 6</p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">6</p>		</td>	</tr>	<tr>		<td colspan=\"2\" width=\"271\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\"><a href=\"http://es.wikipedia.org/wiki/J%C5%8Dy%C5%8D_kanji\" target=\"_blank\"><span lang=\"es-ES\">Resto de Jōyō kanji</a></p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">8</p>		</td>	</tr>	<tr>		<td rowspan=\"2\" colspan=\"3\" width=\"524\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\"><a href=\"http://es.wikipedia.org/wiki/Jinmei_kanji\" target=\"_blank\">Jinmeiyō kanji</a></p>		</td>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">9</p>		</td>	</tr>	<tr>		<td width=\"76\">			<p style=\"margin-top: 0cm;\" align=\"CENTER\">10</p>		</td>	</tr></table><br><br><b>Aclaración</b>: Existen principalmente dos listas:<ul>     <li> La lista <a href=\"http://es.wikipedia.org/wiki/J%C5%8Dy%C5%8D_kanji\" target=\"_blank\"><b>Jōyō kanji</b></a> es la lista oficial de kanjis de uso común redactada por el ministerio japonés de educación. Redactada y modificada en varias ocasiones, la última, en 2009, comprende los 2131 caracteres más comunes de la vida diaria, los cuales deben ser aprendidos por cualquier japonés que termina la educación secundaria. Cualquier palabra en un texto cuyo alguno de sus kanjis no esté en la jōyō kanji deberá llevar un indicador de pronunciación, también llamado <a href=\"http://es.wikipedia.org/wiki/Furigana\" target=\"_blank\">furigana</a>. Existe también la <a href=\"http://en.wikipedia.org/wiki/Ky%C5%8Diku_kanji\" target=\"_blank\"><b>Kyōiku kanji</b></a>, un subconjunto de la jōyō kanji que divide a 1006 caracteres seleccionados en seis grupos, uno para cada año de la educación primaria.</li>          <li>La otra lista, la <a href=\"http://es.wikipedia.org/wiki/Jinmei_kanji\" target=\"_blank\"><b>Jinmeiyō kanji</b></a>, fue redactada por el departamento de asuntos civiles dependiente del ministerio de justicia de Japón, y añade 985 caracteres extra además de los de la lista jōyō kanji que podrán ser usados a la hora de registrar un nombre en el registro civil.</li></ul>Sobre estas listas de kanji, decir que aunque contienen la mayoría de los kanjis que un japonés podría usar a lo largo de toda su vida, aún hay muchos que perteneciendo a contextos muy específicos como pueden ser la ciencia o la literatura, no están oficialmente registrados.");
            messages.put(INFO_KANJI_GRAPH, "<font size=\"6\"><b>Grafo asterisco</b></font><br><br><p align=\"CENTER\"><img src=\"jar:file:javadikt.jar!/images/Grafo_asterisco.png\" width=\"192\" height=\"204\" align=\"BOTTOM\"><br><font size=\"2\">Lineas posibles en una representación grafo-asterisco</font></p><br><br>El modelo grafo-asterisco es una forma de representar un kanji según el orden de dibujo de sus líneas y trazos. En él, se divide a cualquier tipo de trazo en un conjunto posible de 8 tipos de líneas según las direcciones de un asterisco, asignándole a cada una una letra de la A a la H. Por ejemplo, la línea tipo G empieza va desde la parte inferior derecha hasta la parte superior izquierda, mientras que la de tipo C va de la parte superior izquierda hasta la parte inferior derecha. El tipo E representa un punto y generalmente no se usa.<br><br>Así pues el primer paso para representar un kanji mediante el modelo grafo-asterisco es separarlo ordenadamente por líneas que se trazarían sin levantar la mano del papel, y luego convertir cada trazo en un conjunto de líneas asterisco. La primera línea de cada trazo se pone en mayúsculas y el resto en minúsculas.<br><br>La representación grafo-asterisco obvia la posición relativa de los trazos entre sí, pero hace necesario conocer su orden.<br><br>Por ejemplo, consideremos el kanji de cuatro, 四, la forma de obtener su cadena grafo-asterisco sería:<br><p align=\"CENTER\"><img src=\"jar:file:javadikt.jar!/images/ejemploGrafo.png\" width=\"457\" height=\"169\" align=\"BOTTOM\"></p>");
            messages.put(INFO_KANJI_JIS_CHARSET, "<font size=\"6\"><b>Juego de caracteres JIS</b></font><br><br>Ver sección “Código JIS Kuten”.");
            messages.put(INFO_KANJI_JIS_CODE, "<font size=\"6\"><b>Código JIS</b></font><br><br>La agencia japonesa de estandarización(<a href=\"http://en.wikipedia.org/wiki/Japanese_Industrial_Standards\" target=\"_blank\">JIS</a>) mediante su especificación <a href=\"http://en.wikipedia.org/wiki/JIS_X_0208\" target=\"_blank\">JIS X 208:1997</a> define una lista de 6879 kanji, caracteres katakana, caracteres hiragana y algunos caracteres de otros idiomas como inglés, ruso o griego . La especificación define una tabla de referencias, de forma que cada carácter se identifica por su fila y columna dentro de esa tabla, usando para cada una dos dígitos de la forma “ff-cc”, siendo “ff” la fila y “cc” la columna.<br><br>La lista fue ampliada con el estándar <a href=\"http://en.wikipedia.org/wiki/JIS_X_0212\" target=\"_blank\">JIS X 212:1990</a>, que incluía 6067 caracteres más y que hoy en día está prácticamente en desuso en favor del estándar <a href=\"http://en.wikipedia.org/wiki/JIS_X_0213\" target=\"_blank\">JIS X 213:2004</a>, que extiende también al estándar JIS X 208 con todos los caracteres contenidos en el JIS X 212 más 952, lo que suman 11233.<br><br>El estándar JIS X 213 codifica los caracteres de otra manera, pues divide el conjunto completo en dos planos, donde cada plano es una tabla. Así, la codificación queda de la forma “p-ff-cc”. Dado que los caracteres de JIS X 208 están contenidos en el plano uno, convertir de JIS X 208 a JIS X 212 es tan fácil como añadir un uno delante, quedando “1-ff-cc”.<br><br><b>Aclaración</b>: el estándar definitivo japonés sobre la codificación de kanjis es JIS 213, que incluye en el primer plano, entre otros, todos los caracteres del JIS208, algunos kanjis del JIS212 y los nuevos del propio JIS213 distribuidos entre ambos planos. Así pues, la referencia de código JIS ha de ser interpretada de la siguiente manera:<br><br><ul>    	<li>Formato “<b>ff-cc</b>”, juego de caracteres <b>JIS208</b>: El kanji está tanto en los estándares JIS208 como JIS213. Su forma para el estándar JIS213 es “1-ff-cc”.</li>      <li>Formato “<b>p-ff-cc</b>”, juego de caracteres <b>JIS213</b>: El kanji está en el estándar JIS213, no pertenece a JIS208 y quizás también forma parte del JIS212.</li>      <li>Formato “<b>ff-cc</b>”, juego de caracteres JIS212</b>: El kanji pertenece al estándar obsoleto JIS212, pero no fue incluido en el estándar JIS213.</li></ul><br><br><b>Uso</b>: para consultas sobre el estándar JIS213 no será necesario, pero si se consulta sobre el estándar JIS208 o JIS212 habrá que añadir necesariamente el criterio “Juego de caracteres JIS” correspondiente.<br><br><b>Ejemplos</b>:<br><br>-Buscar un kanji del JIS208 o del JIS213 que también pertenezca a JIS 208<br><br><table border=\"1\" align=\"center\"><tr><td>-</td><td>Juego de caracteres JIS</td><td>igual que</td><td>JIS X 208</td></tr><tr><td>Y</td><td>Código JIS kuten</td><td>igual que </td><td>38-17</td></tr></table><br><br> -Buscar un kanji del JIS212<br><br><table border=\"1\" align=\"center\"><tr><td>-</td><td>Juego de caracteres JIS</td><td>igual que</td><td>JIS X 212</td></tr><tr><td>Y</td><td>Código JIS kuten</td><td>igual que </td><td>38-17</td></tr></table><br><br> -Buscar un kanji del estandar JIS213 (y que no pertenece al JIS208)<br><br><table border=\"1\" align=\"center\"><tr><td>-</td><td>Código JIS kuten</td><td>igual que </td><td>2-38-17</td></tr></table><br><br> -Buscar todos los kanjis del estandar JIS213<br><br><table border=\"1\" align=\"center\"><tr><td>-</td><td>Juego de caracteres JIS</td><td>igual que</td><td>JIS X 208</td></tr><tr><td>Y</td><td>Juego de caracteres JIS</td><td>igual que</td><td>JIS X 213</td></tr></table>");
            messages.put(INFO_KANJI_JLPT_LEVEL, "<font size=\"6\"><b>Nivel JLPT</b></font><br><br>Algunos kanjis tienen información sobre el nivel del examen <a href=\"http://es.wikipedia.org/wiki/Nihongo_n%C5%8Dryoku_shiken\" target=\"_blank\">JLPT</a>(Japanese Language Proficiency Test), también llamado Nôken, en el que ese kanji es requerido. En total son cuatro niveles, que tienen las siguientes equivalencias en el <a href=\"http://es.wikipedia.org/wiki/Marco_com%C3%BAn_europeo_de_referencia_para_las_lenguas\" target=\"_blank\">Marco común europeo de referencia para las lenguas</a>(MECR):<br><br><table border=\"1\" align=\"center\">	<tr>		<td>Nivel Nôken</td>		<td>MCER</td>	</tr>	<tr>		<td>1</td>		<td>C2</td>	</tr>	<tr>		<td>2</td>		<td>B2</td>	</tr>	<tr>		<td>3</td>		<td>B1</td>	</tr>	<tr>		<td>4</td>		<td>A1</td>	</tr></table><br><br><b>Aclaración</b>: hace poco se añadió un nuevo nivel al JLPT entre los niveles 2 y3, pero la información de los kanjis aún no ha sido actualizada en la base de datos KANJIDIC, lo que significa que esta información se refiere al formato antiguo que ya no existe.");
            messages.put(INFO_KANJI_LITERAL, "<font size=\"6\"><b>Kanji</b></font><br><br><b>Uso</b>: Desde el campo kanji, pueden introducirse a mano el literal del kanji para obtener uno a uno su información.<br><br><b>Ejemplos</b>:<br><br>-Buscar los kanjis 行 y 買<br><br>	<table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Kanji</td>		<td>igual que</td>		<td>行</td>	</tr>	<tr>		<td>Ó</td>		<td>Kanji</td>		<td>igual que </td>		<td>買</td>	</tr></table>");
            messages.put(INFO_KANJI_MEANING, "<font size=\"6\"><b>Significado</b></font><br><br>A cada kanji se le suele atribuir uno o varios significados que se podrían considerar “generales”. Los significados son independientes de los kanjis a los que acompañe en otras palabras o los significados de las palabras a las que represente en las lecturas kun y on,, aunque evidentemente relacionadas. Por ejemplo, el kanji “開” significa el concepto de “abierto”, “desplegado” o “no cerrado”, independientemente de que la palabra “開ける” o “akeru” signifique el verbo “abrir”, o “開店” o “kaiten”  signifique “inaugurar un negocio” o “abrir una tienda”. Esto nos ayuda, por ejemplo, a intuir el significado de un kanji o una palabra en función de los kanjis que los componen, posibilitando en muchas ocasiones leer un texto sin saber pronunciarlo todo.<br><br>El ordenamiento por significado suele estar implementado solo en diccionarios electrónicos, y no suele ser muy eficiente por los problemas de sinonimia. Si queremos buscar una palabra pensando en un significado cuando realmente en el diccionario está registrado un sinónimo de ésta, nunca obtendremos resultados satisfactorios a pesar de que el resultado sí está registrado. Sin embargo, puede ser útil para sustantivos y palabras muy concretas.<br><br><b>Aclaración</b>: la base de datos de JavaDiKt está basada en un proyecto libre colaborativo en varios idiomas. Eso significa que los campos de significado pueden variar considerablemente de idioma en idioma, e incluso no existir de un lenguaje a otro.<br><br><b>Uso</b>: por culpa de la sinonimia de las palabras y el polimorfismo de las palabras escritas a máquina (por ejemplo, “casa” es igual que “CaSa” para una persona pero no para un ordenador), buscar kanjis por significado puede ser algo engorroso. Por eso, es recomendable escribir siempre en minúsculas (excepto la primera letra de topónimos y nombre comunes) e intentar combinar búsquedas por significado con varios sinónimos.<br><br><b>Ejemplos</b>:<br><br>-Buscar el kanji que significa algo relacionado con “niño”<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Significado</td>		<td>igual que</td>		<td>niño</td>	</tr>	<tr>		<td>Ó</td>		<td>Significado</td>		<td>igual que</td>		<td>chaval</td>	</tr>	<tr>		<td>Ó</td>		<td>Tipo de lectura</td>		<td>igual que </td>		<td>chiquillo</td>	</tr></table>");
            messages.put(INFO_KANJI_READING, "<font size=\"6\"><b>Lectura</b></font><br><br>Los kanjis japoneses tienen principalmente dos clasificaciones para sus lecturas: las <b>lecturas “kun”</b> y las <b>lecturas “on”</b>. Estos nombres categorizan a las lecturas según sus fonemas procedan del japonés o del chino respectivamente.<br><br>Cuando la literatura, la ciencia y la tecnología china entraron en Japón (lo cual incluye a los kanjis), también lo hicieron cientos de palabras de origen chino que en japonés simplemente no existían. Así que lo que hicieron los japoneses fue elegir kanjis para representar sus propias palabras, conservando sus lecturas chinas para palabras que en su idioma no existían. Por ejemplo, en chino Shanghái, la ciudad, se escribe 上海, que se pronuncia “shan-hai”, cuyos kanjis significan tanto en chino como en japonés “encima” y “mar” respectivamente. Los japoneses conservaron la pronunciación “Shanhai” para el nombre de la ciudad, convirtieron la pronunciación de “hai” del segundo kanji en “kai” para otras palabras (como 日本海, “ni-hon-kai” o “mar de Japón”) y asignaron además al kanji la palabra japonesa para mar, ”umi”. Por tanto, las lecturas “on” o chinas de este kanji son “hai” y “kai”, y la lectura “kun” o japonesa es “umi”.<br><br>En la mayoría de los casos, aunque con muchísimas excepciones, las palabras de origen japonés suelen estar representadas por un solo kanji y las de origen chino por una composición de éstos. Por lo general es más fácil aprender las pronunciaciones kun de los kanjis porque los vocablos japoneses son más largos y variados que los chinos. Éstos últimos tienden a ser monosílabos y de sonidos alargados, como “shou”, “ryou” o “sei”, de forma que se repiten mucho entre los kanjis. Mientras tanto, los japoneses tienden a ser de dos sílabas con algún fonema fuerte, como “umi”, “sakura”, “kome” o “mado”.<br><br>Ocurre muy comúnmente que leemos un kanji del cual conocemos su lectura kun, pero no recordamos su lectura “on”, o no sabemos cuál de sus lecturas on se utiliza en esa palabra en concreto. Buscar en este tipo de casos un kanji por su pronunciación (sobre todo si es kun) suele ser bastante efectivo, aún teniendo en cuenta que el número de palabras homónimas en japonés es enorme.<br><br>Otra cosa interesante sobre las lecturas es que, en muchas ocasiones, kanjis compuestos por otros kanjis suelen heredar entera o parcialmente las lecturas “on” de sus radicales. Este hecho es especialmente útil cuando sobre la marcha tenemos que hacer suposiciones sobre cómo se lee una palabra que no entendamos.<br><br>Cabe resaltar también que existe otro tipo de lecturas, <b>las lecturas “nanori”</b>, que son pronunciaciones de kanjis generalmente procedentes del japonés y ya en desuso que siguen usándose para leer nombres. La curiosidad en torno a este tipo de lecturas es que en un registro estatal japonés solo está registrado como se escribe tu nombre, no que pronunciación de cada kanji usa. Esto da libertad a los japoneses de elegir según las lecturas de los kanjis que conforman su nombre como quieren que se pronuncien, posibilitando cambios de significado y juegos de palabras.<br><br><b>Aclaración</b>: es común en muchas publicaciones escribir las lecturas “On” de un kanji en katakana y las lecturas “Kun” y “Nanori” en hiragana. Así es como están almacenadas las lecturas en JavaDiKt, lo que significa que cualquier búsqueda de, por ejemplo, una lectura “On” escribiéndola en hiragana será infructífera.<br><br><b>Uso</b>: es conveniente que siempre se añada a un criterio de lectura mediante “Y” un criterio de “Tipo de lectura”, especialmente si queremos diferenciar las lecturas kun de las lecturas nanori. Si no, la búsqueda podría devolver todos los kanjis que tienen cierta lectura, sea nanori, kun u on.<br><br><b>Ejemplos</b>:<br><br>-Buscar kanjis cuya lectura kun sea “かう”<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Lectura</td>		<td>igual que</td>		<td>かう</td>	</tr>	<tr>		<td>Y</td>		<td>Tipo de lectura</td>		<td>igual que </td>		<td>kun-yomi</td>	</tr></table><br><br>-Buscar kanjis cuya lectura on sea “ショウ”<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Lectura</td>		<td>igual que</td>		<td>ショウ</td>	</tr>	<tr>		<td>Y</td>		<td>Tipo de lectura</td>		<td>igual que </td>		<td>on-yomi</td>	</tr></table><br><br>-Buscar kanjis cuya lectura sea “かう”, sin importar si es nanori, on, o kun<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Lectura</td>		<td>igual que</td>		<td>かう</td>	</tr></table><br><br>-Buscar kanjis del grado 1 que tengan lectura on<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Grado</td>		<td>igual que</td>		<td>1</td>	</tr>	<tr>		<td>Y</td>		<td>Tipo de lectura</td>		<td>igual que</td>		<td>On-yomi</td>	</tr></table>");
            messages.put(INFO_KANJI_READING_TYPE, "<font size=\"6\"><b>Tipo de lectura</b></font><br><br>Ver la sección \"Lectura\".");
            messages.put(INFO_KANJI_SKIP, "<font size=\"6\"><b>Código SKIP</b></font><br><br>\"<b>S</b>ystem of <b>K</b>anji <b>I</b>ndexing by <b>P</b>atterns\", o sistema de indexación de kanjis por patrones es un método de catalogación expuesto por Jack Halpern, en su <a href=\"http://books.google.es/books?id=8b65Dog7a_8C&printsec=frontcover&dq=New+Japanese-English+Character+Dictionary&hl=es&ei=unWWTJi5#v=onepage&q&f=false\" target=\"_blank\"><i>New Japanese-English Character Dictionary</i></a>. En él, cada kanji queda representado de forma no unívoca(es decir, el código puede ser igual en varios kanjis) por un código de tipo “<b>i-j-k</b>”.\"<br><br>El código “i” viene dado por la forma del kanji. La mayoría de ellos suelen tener lo que podemos considerar “<u>un centro de gravedad</u>” que diferencia a dos conjuntos de trazos/radicales espacialmente y que, generalmente, son <b>no conexos o no se cruzan</b>. Si la diferencia es <b>izquierda-derecha</b>, i=1, si es <b>arriba-abajo</b>, i=2; y si es <b>núcleo-envoltura</b>, i=3. En caso de que, por ejemplo, todos los trazos/radicales sean conexos o se crucen, o el kanji sea de simetría tal que <b>no permita diferenciar ninguno de los casos anteriores</b>, i=4.\"<br><br>Si <b>i es igual a 1,2 o 3</b>, “j” es el número de trazos de la parte izquierda/superior/envoltura y “k” es el número de trazos de la parte derecha/inferior/núcleo.\"<br><br>Por el contrario, si <b>i es igual a 4</b>, “j” será el número total de trazos del kanji y “k” dependerá de si tiene una línea horizontal en la parte superior (k=1), si la tiene en la parte inferior (k=2), si tiene una línea vertical en el centro (k=3) o ninguna de las anteriores (k=4).\"<br><br>Lo bueno de este método es que no requiere conocimientos sobre radicales, solo saber contar trazos y reconocer el centro de gravedad, que suele ser fácil. \"Algunos ejemplos:\"<br><br>行　1-3-3　引　1-3-1　相　1-4-5　時　1-4-6　八　1-1-1　噛　1-3-12\"<br>学　2-5-3　見　2-5-2　京　2-2-6　分　2-2-2　二　2-1-1　字　2-3-3　春　2-5-4.\"<br>気　3-4-2　週　3-3-8　広　3-3-2　問　3-8-3　国　3-3-5　月　3-2-2　通　3-3-7\"<br>下　4-3-1　西　4-6-1　士　4-3-2　自　4-6-2　中　4-4-3　事　4-8-3　火　4-4-4　女　4-3-4</font>");
            messages.put(INFO_KANJI_SPAHN_HADAMITZKY, "<font size=\"6\"><b>Código Spahn-Hadamitzky</b></font><br><br>El código <b>Spahn-Hadamitzky</b> es un método de búsqueda de kanjis propuesto en el diccionario <a href=\"http://books.google.es/books?id=D5bIdSRcADwC&printsec=frontcover&dq=spahn+Hadamitzky&hl=es&ei=nHWWTLiEHoLHswagubBk&sa=X&oi=book#v=onepage&q&f=false\" target=\"_blank\"><i>The Kanji Dictionary</i></a>, de Mark Spahn y Wolfgang Hadamitzky. Se basa en la búsqueda del radical y la referenciación, por lo que requiere más preparación que otros métodos como SKIP.<br><br>Dado un kanji, identificamos su radical principal y contamos su número de trazos, que será el primer elemento de la clave. Luego vamos a <a href=\"http://www.hadamitzky.de/images/79radicals-variants.jpg\" target=\"_blank\">esta tabla</a>(sin variantes) o a <a href=\"http://www.hadamitzky.de/images/79radicals+variants.jpg\" target=\"_blank\">ésta</a>(con variantes) e identificamos el kanji por el número de trazos, obteniendo una letra, que será el segundo elemento de la clave. Finalmente, contamos el número de trazos no pertenecientes al radical y lo añadimos al final, obteniendo así el código no unívoco que identifica el kanji.<br><br>En el diccionario The Kanji Dictionary, el código se convierte en unívoco al recibir secuencialmente sin usar ningún criterio en particular números a partir del 1 todos los kanjis pertenecientes al mismo grupo. Algunos ejemplos:<br><br>十　2k0.1　遅　2q9.17　師　3f7.2　則　7b2.1　貨　7b4.5");
            messages.put(INFO_KANJI_STROKE_COUNT, "<font size=\"6\"><b>Número de trazos</b></font><br><br>El número de trazos es el número de líneas que tiene un kanji, entendiendo como línea cualquier elemento del kanji que sea dibujado sin levantar el lápiz del papel.<br><br>La lista de trazos posibles para un kanji es finita, conocida y lo suficientemente abarcable como para que cualquier estudiante de primer grado no tenga excesivos problemas en contar los que cualquier carácter contiene. Las excepciones son escasas y es un método seguro pero poco eficiente para acotar una búsqueda.<br><br>Uso: el número de trazos suele ser extremadamente útil en complemento con otros criterios para acortar una búsqueda, igual que también puede ser útil para hacer búsquedas sobre kanjis borrosos o mal escritos en el que no podamos distinguir exactamente el número de trazos, pero podamos acotarlo.<br><br><b>Ejemplos</b>:<br><br>-Buscar kanjis con un número de trazos mayor que 13 y menor que 15 cuyo radical sea “女”<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Número de trazos</td>		<td>mayor o igual que</td>		<td>13</td>	</tr>	<tr>		<td>Y</td>		<td>Número de trazos</td>		<td>menor o igual que</td>		<td>15</td>	</tr>	<tr>		<td>Y</td>		<td>Radical clásico</td>		<td>igual que</td>		<td>038 – 女</td>	</tr></table>");
            messages.put(INFO_KANJI_UNICODE_VALUE, "<font size=\"6\"><b>Unicode</b></font><br><br>El número Unicode de un kanji es el índice del carácter en el estándar <a href=\"http://es.wikipedia.org/wiki/ISO_10646\" target=\"_blank\">ISO 10646-1</a> que es parecida a la especificación <a href=\"http://es.wikipedia.org/wiki/Unicode\" target=\"_blank\">Unicode</a> con la diferencia fundamental de que éste último solo permite codificaciones de 16 bits, es decir, valores numéricos para los caracteres desde el 0 al 65536, mientras que el ISO 10646-1 permite codificaciones desde 0 a 4294967296 usando 32 bits. Absolutamente todos los kanjis de JavaDiKt tienen un código Unicode y todos son distintos. Cada índice Unicode reconoce al kanji de manera clara, limpia y unívoca y por eso pueden ser bastante útiles a la hora de de intercambiar rápidamente referencias sobre kanjis.<br><br><b>Aclaración</b>: todos los códigos Unicode de JavaDiKt están presentados en hexadecimal.<br><br>");
            messages.put(INFO_KANJI_VARIANT_INDEX, "<font size=\"6\"><b>Referencia de la variante</b></font><br><br>Para ciertos kanjis existen otras versiones relacionadas y parecidas en forma, significado y/o lectura llamadas “variantes”. Generalmente suelen ser representaciones más antiguas del mismo kanji que con el paso del tiempo han ido simplificándose o cambiando de forma de alguna manera. Debido a esto, es bastante común que la relevancia de un kanji en el idioma moderno difiera mucho de la de sus variantes. Por ejemplo, el kanji 四 que tiene una frecuencia tan alta como 47 tiene como variante 亖, que ni siquiera está en el estándar JIS213.<br><br><b>Aclaración</b>: la función de búsqueda por referencia en JavaDiKt está en proceso de desarrollo y por lo tanto no funciona todo lo bien que uno cabría esperar.<br><br><b>Uso</b>: al igual que otros criterios, es necesaria la combinación de este tipo de criterio con el criterio “Tipo de variante”. Sin embargo, aquí el tipo de variante tiene que ser escrito explícitamente en el código interno que usa el programa, por lo que la calidad de la búsqueda es muy deficiente. Los tipos de variantes posibles son los siguientes:<br><br><p style=\"margin-left: 1.25cm;\">jis208jis212jis213ucss_hnelson_cderoonjecdoneill</p><br><br>La utilidad principal de este tipo de información radica en encontrar las variantes del kanji usando otro criterio. Si por ejemplo un kanji especifica que tiene una variante con código JIS212 igual a 18-88, podemos usar los criterios “Código JIS” y “Juego de caracteres JIS” para encontrar dicho kanji.<br><br><b>Ejemplos</b>:<br><br>-Buscar kanjis que tengan como variante un kanji cuyo código JIS212 sea 18-88<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Referencia de la variante</td>		<td>igual que</td>		<td>18-88</td>	</tr>	<tr>		<td>Y</td>		<td>Tipo de variante</td>		<td>igual que</td>		<td>jis212</td>	</tr></table><br><br>");
            messages.put(INFO_KANJI_VARIANT_TYPE, "<font size=\"6\"><b>Tipo de variante</b></font><br><br>Ver sección \"Referencia de la variante\".");
            messages.put(INFO_KANJI_DIC_INDEX, "<font size=\"6\"><b>Referencia del diccionario</b></font><br><br>Muchos autores que escriben diccionarios y guías de estudio sobre kanjis los ordenan por infinidad de criterios, muchas veces prometiendo que es la mejor forma de estudiarlos con nombre sugerentes y pocas consiguiendo su objetivo. La falta de consenso a la hora de ordenar los kanjis para su estudio es grande y prácticamente cada autor que no se atiene a las listas oficiales lo hace de una manera distinta, aunque bien es cierto que en ocasiones ciertos kanjis suelen ocupar posiciones similares en distintas listas, como los numerales o los pertenecientes a días de la semana.<br><br>En JavaDiKt pueden hacerse consultas sobre kanjis referentes a los siguientes diccionarios donde han sido recogidos:<br><br><p style=\"margin-left: 1.25cm;\"><i>A Guide To Reading and Writing Japanese<br>A Guide To Remembering Japanese Characters<br>Daikanwajiten<br>Essential Kanji<br>Japanese For Busy People<br>Japanese Kanji Flashcards<br>Japanese Names<br>Kanji and Kana<br>Kanji Basic Book 1&2<br>Kanji in Context<br>Kanji Learners Dictionary<br>Kodansha Compact Kanji Guide<br>Les Kanjis dans la tete<br>Modern Reader’s Japanese-English Character Dictionary<br>New Dictionary of Kanji Usage”<br>New Japanese-English Character Dictionary<br>The New Nelson Japanese-English Character Dictionary<br>Remembering The  Kanji<br>The Kanji Way to Japanese Language Power<br>Tuttle Kanji Cards<br></i></p><br><br><b>Uso</b>: en la mayoría de los casos, el criterio por referencia del diccionario necesita de ir acompañado por otro criterio del tipo “Nombre del diccionario” para ser útil.<br><br><b>Ejemplos</b>:<br><br>-Buscar el kanji 234 del Kanji Basic Book<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Nombre del diccionario</td>		<td>igual que</td>		<td>Kanji Basic Book</td>	</tr>	<tr>		<td>Y</td>		<td>Referencia del diccionario</td>		<td>igual que </td>		<td>234</td>	</tr></table><br><br>-Buscar todos los kanjis del Kanji Basic Book<br><br><table border=\"1\" align=\"center\">	<tr>		<td>-</td>		<td>Nombre del diccionario</td>		<td>igual que</td>		<td>Kanji Basic Book</td>	</tr></table>");

            // ABOUT WINDOW
            // Title
            messages.put(ABOUTWIN_title, "Acerca de JavaDiKt");
            messages.put(ABOUTWIN_javadicText, "<html><b>JavaDiKt %s</b> <i>%s</i> con %s %s<br>©2011, Luis Alfonso Arce Gonzalez<br><br>Licenciado bajo <a href=\"http://www.gnu.org/licenses/gpl.html\">GNU General Public License version 3<a>.<br><br>Encuentre trucos, manuales e información sobre nuevas versiones en <a href=\"http://www.javadikt.net\">JavaDiKt.net</a></html>");
            messages.put(ABOUTWIN_borderTextLicense, "Licencias");
            messages.put(ABOUTWIN_licenseText,"<html><font size=4>JavaDiKt no seria posible sin el esfuerzo y la generosidad de las personas que elaboraron los siguientes recursos y bibliotecas, que otorgan ademas sin coste alguno permisos de uso de sus trabajos bajo las licencias detalladas a continuación:<br><br><b><a href=\"http://www.csse.monash.edu.au/~jwb/kanjidic.html\">KANJIDIC</a></b>, por Jim Breen del <i><a href=\"http://www.edrdg.org/\">ELECTRONIC DICTIONARY RESEARCH AND DEVELOPMENT GROUP</a></i>, bajo licencia<i><a href=\"http://creativecommons.org/licenses/by-sa/3.0/\"> Creative Commons Attribution-ShareAlike3.0 Unported</a></i>.<br><br><b><a href=\"http://www.kanji.org/kanji/dictionaries/skip_permission.htm\">SYSTEM OF KANJI INDEXING BY PATTERNS(SKIP)</a></b>, por Jack Halpern de la <i><a href=\"http://www.kanji.org\">Kanji Dictionary Publishing Society</a></i> bajo licencia <i><a href=\"http://creativecommons.org/licenses/by-nc-sa/3.0/\">Creative Commons Attribution-Noncommercial-Share Alike 3.0 Unported</a></i>.<br><br><b><a href=\"http://www.neodatis.org/\">Neodatis ODB</a></b>, biblioteca bajo licencia <a href=\"http://www.gnu.org/licenses/lgpl-2.1.html\">LGPL 2.1</a>.<br><br><b><a href=\"http://www.xom.nu/\">XOM</a></b>, biblioteca bajo licencia <a href=\"http://www.gnu.org/licenses/lgpl-2.1.html\">LGPL 2.1</a>. <br><br><b><a href=\"http://www.jgoodies.com/downloads/libraries.html\">JGoodies Looks y JGoodies Common</a></b>, licenciadas bajo la licencia <a href=\"http://www.opensource.org/licenses/bsd-license.html\">BSD</a>.</font></html>");

            initialized = true;
            buildKeyMap();

            //FIN DE IDIOMAS
        } else {

            initMessages(DEFAULT_LENGUAGE);

            localePrintln(messages.get(GEN_LENGUAJE_DOES_NOT_EXISTS_C), languaje);
        }
    }

    // TODO: javadoc
    public static String[] getList(String list) {

        String[] retrievedArray = null;
        // return sorted
        SortedSet<String> retrievedList = null;

        if (!initialized)
            initMessages();
        if (list == null)
            ExceptionHandler.handleException(new NullPointerException(), KNOWN_EXCEPTIONS.MESSAGES_LIST_NULL);

        if (listMessages.get(list) != null)
            retrievedList = new TreeSet<String>(listMessages.get(list).values());

        if (retrievedList == null)
            ExceptionHandler.handleException(new NoSuchMessage("Failed to find the message, the requested list does not exists:" + list), KNOWN_EXCEPTIONS.MESSAGES_LIST_DOES_NOT_EXISTS);

        retrievedArray = new String[retrievedList.size()];
        retrievedList.toArray(retrievedArray);

        return retrievedArray;
    }

    /**
     *
     * <br>
     * <b> initMessages</b>
     *
     * <br>
     * <br>{@code public final static void initMessages()}
     *
     * <br>
     * <br>
     * Método que devuelve los mensajes solicitados en función de una clave de
     * tipo String solo si antes se ha llamado al método {@code initMessages()},
     * en caso contrario dvolverá los mensajes en el idioma por defecto. Las
     * claves y las cadenas devueltas solo pueden modificarse en el código
     * fuente de la clase.<br>
     * <br>
     *
     * @param key
     *            - Cadena clave para solicitar el mensaje.
     * @return El mensaje solicitado por la clave message.
     * @throws NoSuchMessage
     *             - Si el mensaje solicitado no existe.
     */
    public final static String get(String key){

        Map<String,String> retrieveFrom;
        String retrievedMessage;

        if(!initialized)
            initMessages();
        if(key == null)
            ExceptionHandler.handleException(new NullPointerException(), KNOWN_EXCEPTIONS.MESSAGES_NULL);

        retrievedMessage = messages.get(key);

        if (retrievedMessage == null) {
            // TODO: warning!
            if (key.startsWith("CASE_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_CASES_INT);
            else if (key.startsWith("KANJI_CLASSIC_RADICAL_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_VALUES_RADICALS);
            else if (key.startsWith("KANJI_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_FIELD_EXTENDED);
            else if (key.startsWith("DIC_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_VALUES_DIC);
            else if (key.startsWith("JIS_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_VALUES_JIS);
            else if (key.startsWith("READ_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_VALUES_READING_EXTENDED);
            else if (key.startsWith("LANGUAJE_"))
                retrieveFrom = listMessages.get(LIST_LANGUAGES_SUPPORTED);
            else if (key.startsWith("ORDERMETHODSINT_"))
                retrieveFrom = listMessages.get(LIST_ORDENABLE_METHODS_INTEGER);
            else if (key.startsWith("ORDERMETHODSSTR_"))
                retrieveFrom = listMessages.get(LIST_ORDENABLE_METHODS_STRING);
            else if (key.startsWith("ORDER_"))
                retrieveFrom = listMessages.get(LIST_ORDENABLE_FIELDS);
            else
                retrieveFrom = messages;

            retrievedMessage = retrieveFrom.get(key);

            // chapuza!
            if (retrievedMessage == null) {
                retrieveFrom = listMessages.get(LIST_CRITERIA_FIELD_BASIC);
                retrievedMessage = retrieveFrom.get(key);
            }

        }
        if (retrievedMessage == null)
            ExceptionHandler.handleException(new NoSuchMessage("Failed to find the message, the requested message does not exists:" + key), KNOWN_EXCEPTIONS.MESSAGES_MESSAGE_DOES_NOT_EXISTS);

        return retrievedMessage;
    }

    public static String getKey(String message) {

        return getKeys(message).get(0);
    }

    public static List<String> getKeys(String message){

        List<String> keyList = null;

        if (!initialized)
            initMessages();
        if (message == null)
            ExceptionHandler.handleException(new NullPointerException(), KNOWN_EXCEPTIONS.MESSAGES_NULL);

        keyList = keyMap.get(message);

        if ((keyList == null) || (keyList.size() == 0))
            ExceptionHandler.handleException(new NoSuchMessage("Failed to find the key, the requested key does not exists:" + message), KNOWN_EXCEPTIONS.MESSAGES_KEY_DOES_NOT_EXISTS);

        return keyList;
    }



    private static void buildKeyMap() {

        keyMap = new HashMap<String, List<String>>();
        List<String> currentList = null;

        // add simple messages keys

        for (String key : messages.keySet()) {

            currentList = keyMap.get(get(key));

            if (currentList == null) {
                currentList = new ArrayList<String>();
                keyMap.put(get(key), currentList);
            }

            currentList.add(key);
        }

        // add listed messages keys

        for (String listKey : listMessages.keySet()) {
            for (String key : listMessages.get(listKey).keySet()) {

                currentList = keyMap.get(get(key));

                if (currentList == null) {
                    currentList = new ArrayList<String>();
                    keyMap.put(get(key), currentList);
                }

                if (!currentList.contains(key))
                    currentList.add(key);
            }
        }
    }

    public static String[] getKeyListOwners(String key) {

        List<String> owners = new ArrayList<String>();

        for (String listKey : listMessages.keySet()) {
            if (listMessages.get(listKey).containsKey(key))
                owners.add(listKey);
        }

        String[] ownersArray = new String[owners.size()];

        owners.toArray(ownersArray);

        return ownersArray;
    }

    /**
     * Returns the key of a message in a specified list
     *
     * @param message
     * @param listKey
     * @return the key of the specified message in the specified list, null if
     *         the message is not in the specified list
     */
    public static String getKeyInList(String message, String listKey) {

        List<String> keys = getKeys(message);

        // TODO:improve, slow
        for (String key : keys) {
            for (String retListKey : getKeyListOwners(key)) {
                if (retListKey.equals(listKey)) {
                    return key;

                }

            }
        }

        return null;
    }

    public static boolean hasMessage(String message) {

        // TODO: chapuza
        Map<String, String> retrieveFrom;
        String retrievedMessage;

        if (!initialized)
            initMessages();
        if (message == null)
            ExceptionHandler.handleException(new NullPointerException(), KNOWN_EXCEPTIONS.MESSAGES_NULL);

        retrievedMessage = messages.get(message);

        if (retrievedMessage == null) {
            // TODO: warning!
            if (message.startsWith("CASE_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_CASES_INT);
            else if (message.startsWith("KANJI_CLASSIC_RADICAL_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_VALUES_RADICALS);
            else if (message.startsWith("KANJI_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_FIELD_EXTENDED);
            else if (message.startsWith("DIC_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_VALUES_DIC);
            else if (message.startsWith("JIS_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_VALUES_JIS);
            else if (message.startsWith("READ_"))
                retrieveFrom = listMessages.get(LIST_CRITERIA_VALUES_READING_EXTENDED);
            else if (message.startsWith("LANGUAJE_"))
                retrieveFrom = listMessages.get(LIST_LANGUAGES_SUPPORTED);
            else if (message.startsWith("ORDERMETHODSINT_"))
                retrieveFrom = listMessages.get(LIST_ORDENABLE_METHODS_INTEGER);
            else if (message.startsWith("ORDERMETHODSSTR_"))
                retrieveFrom = listMessages.get(LIST_ORDENABLE_METHODS_STRING);
            else if (message.startsWith("ORDER_"))
                retrieveFrom = listMessages.get(LIST_ORDENABLE_FIELDS);
            else
                retrieveFrom = messages;

            retrievedMessage = retrieveFrom.get(message);

            // chapuza!
            if (retrievedMessage == null) {
                retrieveFrom = listMessages.get(LIST_CRITERIA_FIELD_BASIC);
                retrievedMessage = retrieveFrom.get(message);
            }

        }
        return retrievedMessage != null;
    }

    /**
     * Sets whether {@linkplain Messages#localePrintln} methods will print
     * messages at standard output or not
     */
    public static void verbose(boolean verbose) {
        printMessages = verbose;
    }

    /**
     * Prints a return value of the object's {@code toString()} method to the
     * system's standard output device. If the return value is <code>null</code>
     * then the string <code>"null"</code> is printed. Otherwise, the string's
     * characters are converted into bytes according to the platform's default
     * character encoding, and these bytes are written in exactly the manner of
     * the <code> java.io.PrintStream.write(int)</code> method.
     *
     * @param o
     *            The <code>Object</code> to be printed
     */
    public static synchronized final void localePrintln(Object o) {
        String s = o.toString();

        if (printMessages) {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {

                byte[] message = new byte[s.length()];

                try {
                    message = s.getBytes("IBM850");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    System.out.write(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println();

            } else {

                System.out.println(s);
            }
        }
    }

    /**
     * A convenience method to write a formatted string to the system's
     * standardoutput stream using the specified format string and arguments.
     *
     * @param format
     *            A format string as described in <a
     *            href="../util/Formatter.html#syntax">Format string syntax</a>
     *
     * @param args
     *            Arguments referenced by the format specifiers in the format
     *            string. If there are more arguments than format specifiers,
     *            the extra arguments are ignored. The number of arguments is
     *            variable and may be zero. The maximum number of arguments is
     *            limited by the maximum dimension of a Java array as defined by
     *            the <a href="http://java.sun.com/docs/books/vmspec/">Java
     *            Virtual Machine Specification</a>. The behaviour on a
     *            <tt>null</tt> argument depends on the <a
     *            href="../util/Formatter.html#syntax">conversion</a>.
     *
     * @throws IllegalFormatException
     *             If a format string contains an illegal syntax, a format
     *             specifier that is incompatible with the given arguments,
     *             insufficient arguments given the format string, or other
     *             illegal conditions. For specification of all possible
     *             formatting errors, see the <a
     *             href="../util/Formatter.html#detail">Details</a> section of
     *             the formatter class specification.
     *
     * @throws NullPointerException
     *             If the <tt>format</tt> is <tt>null</tt>
     *
     */
    public static synchronized final void localePrintln(String format, Object... args) {
        localePrintln(String.format(format, args));
    }

    public static ISO639ー1 getISO639ー1Language() {
        // FIXME: use ISO639-1 in messages
        return ISO639ー1.parse(LANGUAGE);
    }
}