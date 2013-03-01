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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.UIManager;

import net.ajaest.jdk.core.main.ExceptionHandler.KNOWN_EXCEPTIONS;
import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.auxi.SimilarKanjiStrokeDemuxer;
import net.ajaest.jdk.data.kanji.KanjiGraph;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceRoyale;

//TODO: JavaDoc
public class Launcher {

	public static JavaDiKt jdk;

	/*
	 * FIXME: WARNING! i-bus and OpenJDK doesn't get along very well, it's a
	 * bug(I don't know whether is a OpenJDK bug or IBUS bug). Button + dead
	 * keys won't return, for example, accented chars in Spanish's or French's
	 * keyboards. This problem is solved if i-bus input method is deactivated or
	 * if the jvm is initialized with 「XMODIFIERS=''」 parameter, but you
	 * probably won't be able to write in Japanese.
	 */

	/*
	 * FIXME: WARNING! in some linux systems clipboard's data from jvm
	 * applications cannot be retrieved from non java apps.
	 */

	public static void main(String... args) {

		Messages.verbose(false);

		for (String arg : args) {
			if (arg.equals("-verbose"))
				Messages.verbose(true);
			else if (arg.equals("-version")) {
				Messages.verbose(true);
				Messages.localePrintln("JavaDiKt " + JavaDiKt.PROJECT + " version " + JavaDiKt.VERSION);
				System.exit(0);
			} else {
				System.err.println("Options:\n\t-verbose\n\t-version");
				System.exit(-1);
			}
		}

		// Sets system's look and feel
		Messages.localePrintln("**Launcher: initializing user look&feel");
		try {
			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("mac")) {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JavaDiKt " + JavaDiKt.PROJECT + " " + JavaDiKt.VERSION);
			}

			Plastic3DLookAndFeel.setPlasticTheme(new ExperienceRoyale());

			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());

		} catch (Exception e) {
			Messages.localePrintln("**Error: user default look&feel cannot be loaded");
			ExceptionHandler.handleException(e, KNOWN_EXCEPTIONS.LOOK_AND_FEEL_DOES_NOT_EXISTS);
		}

		Messages.localePrintln("**Launcher: system look and feel initialized");

		// default config file
		JDKOptions jdkOpt = new JDKOptions();

		// Now we have info about language, initializing
		Messages.localePrintln("**Launcher: initializing messages system");
		Locale.setDefault(new Locale(jdkOpt.getLanguage()));
		Messages.initMessages(jdkOpt.getLanguage());
		Messages.localePrintln("**Launcher: messages system initialized");

		// initializing javadikt but not running it
		Messages.localePrintln("**Launcher: initializing JavaDiKt core");
		JavaDiKt jdk = new JavaDiKt(jdkOpt);
		Launcher.jdk = jdk;
		try {
			jdk.setSimilarStrokeDemuxer(createSimilarKanjiStrokeDemuxer());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Messages.localePrintln("**Launcher: JavaDiKt core initialized");

		// now exception handler can catch javadikt core exceptions
		Messages.localePrintln("**Launcher: initializing exception handler");
		ExceptionHandler.setJavaDiKt(jdk);
		Messages.localePrintln("**Launcher: exception handler initialized");

		// running javadikt
		Messages.localePrintln("**Launcher: running javadikt");
		jdk.isLastVersion();
		jdk.run();
	}

	@SuppressWarnings("unchecked")
	private static SimilarKanjiStrokeDemuxer createSimilarKanjiStrokeDemuxer()
			throws FileNotFoundException, IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				"dict/subgraphset.obj"));
		Set<List<KanjiStroke>> allowedsubGraphs = (Set<List<KanjiStroke>>) ois
				.readObject();
		ois.close();

		ois = new ObjectInputStream(new FileInputStream("dict/strokemap.obj"));
		Map<KanjiStroke, Set<KanjiStroke>> similarityMap = (Map<KanjiStroke, Set<KanjiStroke>>) ois
				.readObject();
		ois.close();

		ois = new ObjectInputStream(new FileInputStream("dict/graphset.obj"));
		Set<KanjiGraph> allowedGraph = (Set<KanjiGraph>) ois.readObject();
		ois.close();

		SimilarKanjiStrokeDemuxer sksd = new SimilarKanjiStrokeDemuxer(
				similarityMap, allowedsubGraphs, allowedGraph);

		return sksd;

	}
}
