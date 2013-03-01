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

package net.ajaest.jdk.core.auxi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import net.ajaest.jdk.core.main.Messages;

//TODO: JavaDoc
public class LastVersionChecker {

	private URL versionFileUrl;
	private File lastVersionFile;
	private Runnable versionCheker;
	
	private volatile boolean isChecking = false;
	private boolean writeToFile = false;

	private boolean writtenToFile = false;
	private Boolean readSuccessFull = null;

	private Exception checkException;
	private Exception writeException;

	private String version = null;
	
	public LastVersionChecker(URL versionFile) {
		this.versionFileUrl = versionFile;
	}

	public boolean getWriteToFile() {
		return writeToFile;
	}

	public void writeToFile(boolean writeToFile) {
		this.writeToFile = writeToFile;
	}

	public boolean isWrittenInFile() {
		return writtenToFile;
	}

	public boolean isChecking(){
		return isChecking;
	}
	
	public Boolean readSuccessfull() {
		return readSuccessFull;
	}

	public String getVersion() {
		return version;
	}
	
	public void setLastVersionFile(File f) {
		this.lastVersionFile = f;
	}


	public void checkVersion() {

		isChecking = true;
		Messages.localePrintln("**VersionChecker: checking last version");

		if (versionCheker == null)
			versionCheker = new Runnable() {
				@Override
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(versionFileUrl.openStream()));
						version = reader.readLine();
						reader.close();
						readSuccessFull = true;
					} catch (Exception e) {
						readSuccessFull = false;
						checkException = e;
					}

					isChecking = false;

					if (readSuccessFull)
						Messages.localePrintln("**VersionChecker: last version retrieved from server \"" + versionFileUrl + "\", last version is \"" + version + "\"");
					else
						Messages.localePrintln("**VersionChecker: last version check failed, the exception throwed is \"" + checkException + "\"");

					if (writeToFile && readSuccessFull) {
						try {
							BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lastVersionFile)));
							writer.write(version);
							writer.flush();
							writer.close();
							writtenToFile = true;

						} catch (Exception e) {
							writeException = e;
						}
					}

					if (writeToFile && writtenToFile) {
						Messages.localePrintln("**VersionChecker: version written to file \"" + lastVersionFile.getAbsoluteFile() + "\"");
					} else if (writeToFile && !writtenToFile) {
						Messages.localePrintln("**VersionChecker: write to file failed, the exception throwed is \"" + writeException + "\"");
					}
				}
			};

		new Thread(versionCheker, "version-checker").start();

	}

	public static void main(String... args) throws MalformedURLException, InterruptedException {

		URL versionFile = new URL("http://ajaest.net/javadikt/lastVersion.txt");

		LastVersionChecker lvc = new LastVersionChecker(versionFile);



		long bgTime = System.currentTimeMillis();
		lvc.checkVersion();

		long edTime;
		long elpsTime;



		while (lvc.isChecking) {
			// System.out.println(lvc.isChecking);
			edTime = System.currentTimeMillis();
			elpsTime = edTime - bgTime;
			if (elpsTime > 5000) {
				lvc.setLastVersionFile(new File("versionFile.txt"));
				lvc.writeToFile(true);
				System.out.println("Version thread killed");
				break;
			}
		}

		System.out.println("Is checking:" + lvc.isChecking());
		System.out.println("Read successful:" + lvc.readSuccessfull());
		if (!lvc.isChecking && lvc.readSuccessfull() && !lvc.isWrittenInFile()) {
			System.out.println(lvc.getVersion());
		}
	}
}
