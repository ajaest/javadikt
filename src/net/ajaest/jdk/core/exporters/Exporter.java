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

import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

import net.ajaest.jdk.core.stylers.Styler;

//TODO: JavaDoc
/**
 * WARNING: exporters must be runtime-independent. They must be able to work
 * alone, what means, for example, that export windows and messages must be
 * stored in the exporter itself. That's because they are intended to use they
 * as plugins.
 */
public abstract class Exporter<E> implements Comparable<Exporter<?>>, Serializable {


	private static final long serialVersionUID = 6912511348890433716L;

	private JFrame dialogParent = null;
	private Exception exportException;
	private File f;
	private Integer selectedStylerIndex;

	protected String languaje;
	protected List<JDialog> stylerDialogs;
	protected List<Styler<E>> stylers;
	
	public abstract FileFilter getFileChooserFilters();
	public abstract Boolean export(List<E> list);
	public abstract String getMessage(String key);
	public abstract String getName();
	protected abstract void initMessages(String lang);

	public Exporter(String lang) {
		this.languaje = lang;
		initMessages(lang);
		stylers = new ArrayList<Styler<E>>();
		stylerDialogs = null;
		selectedStylerIndex = 0;
	}
	@Override
	public int compareTo(Exporter<?> exporter) {

		return getName().compareTo(exporter.getName());
	}

	public JDialog extraConfigJFrame() {

		if (stylerDialogs == null)
			initDialog();

		return stylerDialogs.get(getSelectedStylerIndex());
	}

	public JFrame getDialogParent() {
		return dialogParent;
	}

	public Exception getExportException() {
		return exportException;
	}

	public File getExportFile() {

		return f;
	}

	public String getLanguaje(){
		return languaje;
	}

	public Integer getSelectedStylerIndex() {
		return selectedStylerIndex;
	}

	public List<Styler<E>> getStylers() {
		return Collections.unmodifiableList(stylers);
	}

	protected void initDialog() {

		stylerDialogs = new ArrayList<JDialog>();

		for (Styler<E> st : getStylers()) {
			JDialog jd = new JDialog(getDialogParent(), true);
			jd.setLocationRelativeTo(getDialogParent());

			if (getMessage("WINNAME") != null)
				jd.setTitle(getMessage("WINNAME"));

			JTabbedPane jtp = new JTabbedPane();
			double xsize = 300d;
			double ysize = 200d;
			for (JPanel jp : st.getStyleOptionsJPanels()) {
				jtp.addTab(jp.getName(), jp);
				if (jp.getPreferredSize().getWidth() > xsize)
					xsize = jtp.getPreferredSize().getWidth();
				if (jp.getPreferredSize().getHeight() > ysize)
					ysize = jtp.getPreferredSize().getHeight();
			}
			jd.setModalityType(ModalityType.DOCUMENT_MODAL);
			jd.setSize((int) xsize, (int) ysize + 30);

			if (getMessage("BUTTON_ACCEPT") != null) {
				jtp.setLayout(new GridLayout(2, 1));
				JButton acceptButton = new JButton(getMessage("BUTTON_ACCEPT"));
				acceptButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						((JButton) e.getSource()).getParent().getParent().setVisible(false);
						// JBUTTON->JTABBEDPANE->JFRAME
					}
				});
				jtp.add(acceptButton);
			}

			jd.add(jtp);
			jd.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					e.getWindow().setVisible(false);
				}
			});
			stylerDialogs.add(jd);
		}
	}

	public void invokeExtraConfigJFrame() {
		if (stylerDialogs == null)
			initDialog();
		stylerDialogs.get(getSelectedStylerIndex()).setVisible(true);
	}

	public void setDialogParent(JFrame dialogParent) {
		this.dialogParent = dialogParent;
	}

	protected void setExportException(Exception exportException) {
		this.exportException = exportException;
	}

	public void setExportFile(File f) {

		this.f = f;
	}

	public void setExportPath(File f) {
		this.f = f;
	}

	public void setLanguaje(String lang) {
		this.languaje = lang.toLowerCase();
		initMessages(languaje);
		for (Styler<?> sty : stylers)
			sty.setLanguaje(languaje);
	}

	public void setSelectedStyler(Integer selectedStyler) {
		this.selectedStylerIndex = selectedStyler;
	}

	public void setStylers(List<Styler<E>> stylers) {
		this.stylers = stylers;
	}

	public Styler<E> getSelectedStyler() {

		Styler<E> selectedStyler = null;

		if (stylers.size() > selectedStylerIndex)
			selectedStyler = stylers.get(selectedStylerIndex);

		return selectedStyler;
	}

}
