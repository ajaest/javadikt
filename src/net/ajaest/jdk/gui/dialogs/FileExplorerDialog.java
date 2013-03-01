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

package net.ajaest.jdk.gui.dialogs;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

//TODO: javadoc
public class FileExplorerDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = -4884696102093133058L;

	private JFileChooser jFileChooser;
	private FileFilter fileFilter;

    public FileExplorerDialog(FileFilter fileFilter,java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        
        this.fileFilter = fileFilter;
        
        initComponents();
    }


    private void initComponents() {

        jFileChooser = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jFileChooser.setDialogType(javax.swing.JFileChooser.CUSTOM_DIALOG);
        jFileChooser.setFileFilter(fileFilter);
        getContentPane().add(jFileChooser, java.awt.BorderLayout.CENTER);

        pack();
	}

	public JFileChooser getFileChooser() {
		return jFileChooser;
	}

	public int showOpenDialog() {

		return jFileChooser.showOpenDialog(this);
	}
}
