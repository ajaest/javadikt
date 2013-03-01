/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ajaest.jdk.core.winHandlers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ajaest.jdk.core.main.ExceptionHandler;
import net.ajaest.jdk.core.main.JDKGUIEngine;
import net.ajaest.jdk.core.main.JDKOptions;
import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.jdk.data.kanji.RadicalTag;
import net.ajaest.jdk.data.kanji.RadicalVariantTag;
import net.ajaest.jdk.gui.auxi.KanjiInfoWindow;
import net.ajaest.jdk.gui.windows.KanjiInfoExtWindow;
import net.ajaest.jdk.gui.windows.KanjiInfoSimpleWindow;
import net.ajaest.jdk.gui.windows.RadicalInfoWindow;
import net.ajaest.jdk.gui.windows.StrokeOrderFontWindow;

/**
 * 
 * @author ajaest10
 */
public class KanjiInfoWH implements WinHandler {

	private JDKGUIEngine jdkGui;

	private List<KanjiInfoWindow> kanjiDisplaying;

	private Map<Integer, KanjiInfoWindow> kanjiWindows;
	private Map<Integer, RadicalInfoWindow> radicalWindows;
	private Map<Integer, StrokeOrderFontWindow> strokeWindows;

	private Font strokeOrderFont;

	private final Dimension screen;
	// Subscreen dimension(from upper left corner of the screen) that can be
	// used to put a windows
	private final Dimension positionableScreen;

	/**
	 * Vertical offset for first line windows, makes them to appear under the
	 * menu bar in mac systems
	 */
	private final int Y_OFFSET;
	/**
	 * Horizontal offset for new windows
	 */
	private static final int X_SHIFT = 140;
	/** Vertical offset for new windows */
	private static final int Y_SHIFT = 140;
	/**
	 * Default window width used to calculate positionable screen size- Must be
	 * multiple of X_SHIFT
	 */
	private static final int WINDOW_WIDTH = 584;
	/**
	 * Default window height used to calculate positionable screen size- Must be
	 * multibple of Y_SHIFT
	 */
	private static final int WINDOW_HEIGHT = 224;

	public KanjiInfoWH(JDKGUIEngine jdkgui) {

		this.jdkGui = jdkgui;

		kanjiDisplaying = new ArrayList<KanjiInfoWindow>();
		kanjiWindows = new HashMap<Integer, KanjiInfoWindow>();
		radicalWindows = new HashMap<Integer, RadicalInfoWindow>();
		strokeWindows = new HashMap<Integer, StrokeOrderFontWindow>();

		screen = Toolkit.getDefaultToolkit().getScreenSize();

		if (System.getProperty("os.name").toLowerCase().contains("mac"))
			Y_OFFSET = 20;
		else
			Y_OFFSET = 0;

		int positionableWidth = ((int) (screen.getWidth() - WINDOW_WIDTH) / X_SHIFT) * X_SHIFT;
		int positionableHeigth = ((int) (screen.getHeight() - WINDOW_HEIGHT) / Y_SHIFT) * X_SHIFT;
		positionableScreen = new Dimension(positionableWidth, positionableHeigth);

		try {
			strokeOrderFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/font/KanjiStrokeOrders_v2.015.ttf")).deriveFont((float) 250);
			Messages.localePrintln("**KanjiInfoWH: Stroke order font retrieved");
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
		}
	}

	public void invokeWindow(Integer kanjiRef) {

		KanjiInfoWindow kif = kanjiWindows.get(kanjiRef);

		if (kif == null) {

			KanjiTag kt = jdkGui.getJavaDikt().getKanjiByRef(kanjiRef);

			if (jdkGui.getOptions().getExtendedInfo())
				kif = new KanjiInfoExtWindow(this, kt, jdkGui.getOptions().getRomanized());
			else
				kif = new KanjiInfoSimpleWindow(this, kt, jdkGui.getOptions().getRomanized());

			Integer locIndex = getFirstIndexOfAvaibleSpace();

			kif.setLocation(getScreenPositionOfIndex(locIndex));

			kif.setVisible(true);

			if (locIndex < kanjiDisplaying.size())
				kanjiDisplaying.set(locIndex, kif);
			else
				kanjiDisplaying.add(kif);

			kanjiWindows.put(kanjiRef, kif);
		} else {
			kif.toFront();
		}

		jdkGui.getMainWH().getWindow().toFront();
	}

	public void radicalInfoButtonPressed(RadicalTag classicRadical,	KanjiInfoWindow kiw) {

		if (classicRadical != null) {
			RadicalInfoWindow riw = radicalWindows.get(classicRadical);

			if (riw == null) {

				Point pos = kiw.getLocation();
				pos.setLocation(pos.getX() + X_SHIFT, pos.getY());

				riw = new RadicalInfoWindow(this, classicRadical);
				riw.setLocation(pos);
				riw.setVisible(true);

				radicalWindows.put(classicRadical.getNumber(), riw);
			} else {
				riw.toFront();
			}
		}
	}

	public String getRadicalString(RadicalTag radicalTag) {

		String radString;
		if (radicalTag != null) {
			RadicalVariantTag retRad = null;

			for (RadicalVariantTag r : radicalTag.getVariants()) {
				if (r.getUnicode() != null) {
					retRad = r;
					break;
				}
			}

			if (retRad != null)
				radString = new String(Character.toChars(retRad.getUnicode()));
			else
				radString = radicalTag.toString();
		} else
			radString = "--";

		return radString;
	}

	public void windowClosed(KanjiInfoWindow kif) {

		int closedIndex = kanjiDisplaying.indexOf(kif);

		if ((closedIndex + 1) == kanjiDisplaying.size())
			kanjiDisplaying.remove(closedIndex);
		else
			kanjiDisplaying.set(closedIndex, null);

		kanjiWindows.remove(kif.getKanjiTag().getUnicodeRef());
	}

	public void windowClosed(RadicalInfoWindow riw) {

		radicalWindows.remove(riw.getRadical().getNumber());
	}

	public void windowClosed(StrokeOrderFontWindow riw) {
		strokeWindows.remove(riw.getKanjiTag().getUnicodeRef());
	}
	public JDKOptions getOptions() {
		return jdkGui.getOptions();
	}

	public String getMessage(String message) {
		return Messages.get(message);
	}

	public List<Image> getIcons() {

		return jdkGui.getIcons();
	}

	public Font getUnicodeFont() {

		return jdkGui.getUnicodeFont();
	}

	public Point getScreenPositionOfIndex(Integer index) {

		int xpos = (X_SHIFT * index) % ((int) positionableScreen.getWidth());
		int ypos = (((X_SHIFT * index) / ((int) positionableScreen.getWidth())) * Y_SHIFT) % ((int) positionableScreen.getHeight()) + Y_OFFSET;

		Point position = new Point(xpos, ypos);

		return position;
	}

	public Integer getFirstIndexOfAvaibleSpace() {

		Integer index = kanjiDisplaying.indexOf(null);

		if (index == -1)
			index = kanjiDisplaying.size();

		return index;
	}

	public void disposeAllWindows() {

		for (RadicalInfoWindow riw : radicalWindows.values())
			riw.dispose();

		for (KanjiInfoWindow kiw : kanjiWindows.values())
			kiw.dispose();

		for (StrokeOrderFontWindow sofw : strokeWindows.values())
			sofw.dispose();
	}

	public JDKGUIEngine getJDKGuiEngine() {
		return jdkGui;
	}

	@Override
	protected void finalize() {
		disposeAllWindows();
	}

	public boolean hasStrokeOrder(Integer unicode) {

		return strokeOrderFont.canDisplay(unicode);
	}

	public Font getStrokeOrderFont() {

		return strokeOrderFont;
	}

	public void InvokeStrokeOrderFont(KanjiInfoWindow kiw) {

		StrokeOrderFontWindow sto = strokeWindows.get(kiw.getKanjiTag().getUnicodeRef());
		if (sto == null) {
			sto = new StrokeOrderFontWindow(this, kiw.getKanjiTag());
			Point pos = kiw.getLocation();
			pos.setLocation(pos.getX() + X_SHIFT, pos.getY());
			sto.setLocation(pos);
			strokeWindows.put(sto.getKanjiTag().getUnicodeRef(), sto);

			sto.setVisible(true);
		} else {
			sto.toFront();
		}
	}
}
