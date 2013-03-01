package net.ajaest.jdk.gui.auxi;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.ajaest.jdk.core.winHandlers.MainWH;
import net.ajaest.jdk.data.kanji.KanjiTag;


public class KanjiButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6719137290347249359L;
	private Integer kanjiRef = null;
	private KanjiTag kanjiInfo = null;
	private ActionListener al = null;
	private Integer listPosition = null;
	private MainWH mwh;
	
	public Integer getListPosition() {
		return listPosition;
	}

	public void setListPosition(Integer listPosition) {
		this.listPosition = listPosition;
	}

	public KanjiTag getKanji() {
		return kanjiInfo;
	}

	
	public KanjiButton(MainWH mwh, Integer kanjiRef, Integer listPosition) throws HeadlessException {
		super(new String(Character.toChars(kanjiRef)));
		this.mwh = mwh;
		this.listPosition = listPosition;
		this.kanjiRef = kanjiRef;
		//TODO:catch
		setBackground(Color.WHITE);
		setFont(mwh.getUnicodeFont().deriveFont((float) 31));
		
		
		al = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				invokeKanjiInfoWindow();				
			}
			
		};
		
		setFocusable(true);
	}
	
	public void invokeKanjiInfoWindow(){
		
		mwh.invokeKanjiInfoWindow(kanjiRef);
	}
	
	public void enableInvokeKanjiInformationWindowActionListener(boolean b){
		
		if(b){
			this.addActionListener(al);
		} else {
			this.removeActionListener(al);
		}
		
	}
}
