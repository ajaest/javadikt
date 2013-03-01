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

import javax.swing.UIManager;

//TODO: JavaDoc
public class ExceptionHandler {

    private static JavaDiKt jdk = null;

    public static enum KNOWN_EXCEPTIONS {

        // System
        LOOK_AND_FEEL_DOES_NOT_EXISTS,

        // System.messages
        MESSAGES_NULL,
        MESSAGES_MESSAGE_DOES_NOT_EXISTS,
        MESSAGES_KEY_DOES_NOT_EXISTS,
        MESSAGES_LIST_NULL,
        MESSAGES_LIST_DOES_NOT_EXISTS,

        // Config file
        INVALID_CONFIG_FILE_PATH,
        MALFORMED_CONFIG_FILE,

        // Kanji/stroke File

        INVALID_KANJI_OR_STROKE_FILE_PATH,

        // Casts exception

        NUMBER_CAST_EXCEPTION,
        GRAPH_CAST_EXCEPTION,
        IS_NOT_HEX,
        NOT_A_KANJI,

        // IO exception

        EXPORT_IOEXCEPTION,
        TREE_IOEXCEPTION,
        CORRUPTED_TREE,
        KANJIDB_CORRUPTED,
        KANJIDB_IOEXCEPTION,
        CANNOT_OPEN_FILE_INSPECTOR,

        // Query execution

        INEXISTENT_QABOUT,

        // yet not implemented

    }

    public static void setJavaDiKt(JavaDiKt jdk) {
        ExceptionHandler.jdk = jdk;
    }

    public static boolean handleException(Exception e) {

        boolean exit;

        if (e instanceof RuntimeException) {
            exit = handleRuntimeException((RuntimeException) e);
        } else {
            exit = handleNoRuntimeException(e);
        }

        return exit;
    }
    /**
     * 
     * @param e
     * @return true if the exception causes the program to exit, false otherwise
     */
    public static boolean handleException(Exception e, KNOWN_EXCEPTIONS info) {

        boolean exit;

        if (e instanceof RuntimeException) {
            exit = handleRuntimeException((RuntimeException) e, info);
        } else {
            exit = handleNoRuntimeException(e, info);
        }

        return exit;
    }

    /**
     * 
     * @param e
     * @return true if the exception causes the program to exit, false otherwise
     */
    private static boolean handleRuntimeException(RuntimeException e, KNOWN_EXCEPTIONS info) {

        boolean exceptionHandled;

        switch (info) {
        case NUMBER_CAST_EXCEPTION :
            jdk.getJDKGuiEngine().getInfoDialgosEngine().invokeInfoDialog(Messages.get(Messages.INFODIALOG_title_error), Messages.get(Messages.INFODIALOG_errorMessage_integerCastException), null);
            exceptionHandled = true;
            break;
        case IS_NOT_HEX :
            jdk.getJDKGuiEngine().getInfoDialgosEngine().invokeInfoDialog(Messages.get(Messages.INFODIALOG_title_error), Messages.get(Messages.INFODIALOG_errorMessage_isNotHex), null);
            exceptionHandled = true;
            break;
        case GRAPH_CAST_EXCEPTION :
            jdk.getJDKGuiEngine().getInfoDialgosEngine().invokeInfoDialog(Messages.get(Messages.INFODIALOG_title_error), Messages.get(Messages.INFODIALOG_infoMessage_graphCastException), null);
            exceptionHandled = true;
            break;
        case NOT_A_KANJI :
            jdk.getJDKGuiEngine().getInfoDialgosEngine().invokeInfoDialog(Messages.get(Messages.INFODIALOG_title_error), Messages.get(Messages.INFODIALOG_infoMessage_kanjiCastException), null);
            exceptionHandled = true;
            break;
        default :
            exceptionHandled = false;
            e.printStackTrace();
            break;
        }

        return exceptionHandled;
    }

    /**
     * 
     * @param e
     * @return true if the exception causes the program to exit, false otherwise
     */
    private static boolean handleNoRuntimeException(Exception e, KNOWN_EXCEPTIONS info) {

        boolean exceptionHandled;

        switch (info) {
        case EXPORT_IOEXCEPTION :
            //jdk.getJDKGuiEngine().getInfoDialgosEngine().invokeInfoDialog(Messages.get(Messages.INFODIALOG_title_error), String.format(Messages.get(Messages.INFODIALOG_errorMessage_exportFail), e.getLocalizedMessage()), null);
            //Nothing can be done. Information are given from MainWH.ExportTask
            exceptionHandled = true;
            break;
        case CANNOT_OPEN_FILE_INSPECTOR:
            jdk.getJDKGuiEngine().getInfoDialgosEngine().invokeInfoDialog(Messages.get(Messages.INFODIALOG_title_error), String.format(Messages.get(Messages.INFODIALOG_errorMessage_cannotOpenFile), e.getLocalizedMessage()), null);
            exceptionHandled = true;
            break;
        case LOOK_AND_FEEL_DOES_NOT_EXISTS :
            Messages.localePrintln("\t**ExceptionHandler: initializing system default look&feel, may contain visual errors");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                exceptionHandled = true;
                Messages.localePrintln("\t**ExceptionHandler: system default look&feel initialized");
            } catch (Exception e2) {
                Messages.localePrintln("\t**ExceptionHandler: couldn't initialize system default look&feel");
                exceptionHandled = false;
                e2.printStackTrace();
            }
            break;
        default :
            exceptionHandled = false;
            e.printStackTrace();
            break;
        }

        return exceptionHandled;
    }
    /**
     * 
     * @param e
     * @return true if the exception causes the program to exit, false otherwise
     */
    private static boolean handleNoRuntimeException(Exception e) {
        e.printStackTrace();
        return true;
    }

    private static boolean handleRuntimeException(RuntimeException e) {

        e.printStackTrace();
        return true;
    }

}
