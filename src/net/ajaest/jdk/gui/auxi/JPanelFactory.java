package net.ajaest.jdk.gui.auxi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.core.winHandlers.MainWH;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.ReadingEntry;
import net.ajaest.lib.locale.SwingMessagesEnums.ContextMenuEnums.EditMenu;
import net.ajaest.lib.swing.util.EditContextMenuEngine;
import net.ajaest.lib.swing.util.action.JLabelMultiCopyAction;

public class JPanelFactory {

	public static JScrollPane createToggleKanjiGrid(MainWH mwh, List<Integer> kl) {

		ExportJPanel jp = new ExportJPanel(kl, mwh);
		jp.setLayout(new GridLayout(0, 4));

		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		JScrollPane jsp = new JScrollPane(v, h);
		jsp.setViewportView(jp);
		jsp.getVerticalScrollBar().setUnitIncrement(16);

		return jsp;

	}

	public static JScrollPane createKanjiGrid(MainWH mwh, List<Integer> lk) {

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 4));

		int listPosition = 0;
		for (Integer k : lk) {

			KanjiButton b = new KanjiButton(mwh, k, listPosition);
			listPosition++;
			b.enableInvokeKanjiInformationWindowActionListener(true);
			panel.add(b);
		}
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		JScrollPane jsp = new JScrollPane(v, h);
		jsp.setViewportView(panel);
		jsp.getVerticalScrollBar().setUnitIncrement(16);

		return jsp;
	}

	public static JScrollPane createKanjiReadingsList(Font font, KanjiTag kt, boolean extended, boolean romanized, EditContextMenuEngine eCtxMenE) {

		if (romanized) {
			kt = kt.toRomaji();
			font = new JPanel().getFont();
		}

		JPanel panel = new JPanel();
		List<JLabel> addedLabels;
		JLabelMultiCopyAction readingCopyAct;

		panel.setFont(font);
		panel.setLayout(new GridLayout(0, 1));

		Map<String, List<String>> readingsMap = new HashMap<String, List<String>>();
		for (ReadingEntry re : kt.getReadings()) {
			readingsMap.put(re.getKey(), re.getElements());
		}

		JPanel onYomiPanel = new JPanel();
		onYomiPanel.setLayout(new BorderLayout());
		onYomiPanel.setBackground(Color.GREEN);
		JLabel readingTitle = new JLabel(Messages.get(Messages.READ_JA_ON));
		readingTitle.setVerticalAlignment(JLabel.CENTER);
		readingTitle.setHorizontalAlignment(JLabel.CENTER);
		onYomiPanel.add(readingTitle, BorderLayout.CENTER);
		panel.add(onYomiPanel);

		List<String> readings = readingsMap.get("ja_on");
		if (readings == null) {
			readings = new ArrayList<String>();
			readings.add("---");
		}

		addedLabels = addLabelsRowly(panel, readings, font, eCtxMenE);
		// Adds context menu to reading title panel
		readingCopyAct = new JLabelMultiCopyAction(addedLabels, "\r\n");
		eCtxMenE.addContextMenu(readingTitle, EditMenu.COPY, readingCopyAct);

		JPanel kunYomiPanel = new JPanel();
		kunYomiPanel.setLayout(new BorderLayout());
		kunYomiPanel.setBackground(Color.ORANGE);
		readingTitle = new JLabel(Messages.get(Messages.READ_JA_KUN));
		readingTitle.setVerticalAlignment(JLabel.CENTER);
		readingTitle.setHorizontalAlignment(JLabel.CENTER);
		kunYomiPanel.add(readingTitle, BorderLayout.CENTER);
		panel.add(kunYomiPanel);

		readings = readingsMap.get("ja_kun");
		if (readings == null) {
			readings = new ArrayList<String>();
			readings.add("---");
		}

		addedLabels = addLabelsRowly(panel, readings, font, eCtxMenE);
		// Adds context menu to reading title panel
		readingCopyAct = new JLabelMultiCopyAction(addedLabels, "\r\n");
		eCtxMenE.addContextMenu(readingTitle, EditMenu.COPY, readingCopyAct);

		if (extended) {

			JPanel nanori = new JPanel();
			nanori.setLayout(new BorderLayout());
			nanori.setBackground(Color.PINK);
			readingTitle = new JLabel(Messages.get(Messages.READ_NANORI));
			readingTitle.setVerticalAlignment(JLabel.CENTER);
			readingTitle.setHorizontalAlignment(JLabel.CENTER);
			nanori.add(readingTitle, BorderLayout.CENTER);
			panel.add(nanori);

			readings = readingsMap.get("nanori");
			if (readings == null) {
				readings = new ArrayList<String>();
				readings.add("---");
			}

			addedLabels = addLabelsRowly(panel, readings, font, eCtxMenE);
			// Adds context menu to reading title panel
			readingCopyAct = new JLabelMultiCopyAction(addedLabels, "\r\n");
			eCtxMenE.addContextMenu(readingTitle, EditMenu.COPY, readingCopyAct);
		}

		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		JScrollPane jsp = new JScrollPane(v, h);
		jsp.setViewportView(panel);
		jsp.getVerticalScrollBar().setUnitIncrement(16);

		return jsp;
	}
	private static List<JLabel> addLabelsRowly(JPanel panel, List<String> lines, Font font, EditContextMenuEngine eCtxMenE) {

		List<JLabel> labels = new ArrayList<JLabel>();

		JLabel tempJL;
		JPanel tempJP;

		for (String s : lines) {

			tempJL = new JLabel(s);
			tempJL.setFont(font);
			// tempJL.setVerticalAlignment(JLabel.CENTER);
			tempJL.setHorizontalAlignment(JLabel.CENTER);

			if (eCtxMenE != null)
				eCtxMenE.addContextMenu(tempJL);

			labels.add(tempJL);

			tempJP = new JPanel();
			tempJP.add(tempJL);
			panel.add(tempJP);
		}


		return labels;
	}

}