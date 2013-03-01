package net.ajaest.jdk.data.dict;

import java.util.Calendar;
import java.util.List;

/**
 * Class that represents practical information about a kanji database. Database
 * must only have one instance of this class.
 * 
 * @author Luis Alfonso Arce González
 * 
 */
public class KanjiDatabaseInfo {

	private Calendar creation = null;
	private Calendar modified = null;
	private List<String> copyrights = null;
	private String licenseName = null;
	private String licenseContent = null;
	private String version = null;
	private String name = null;

	public KanjiDatabaseInfo(String name, Calendar creation, Calendar modificated, List<String> copyrights, String licenseName, String licenseContent, String version) {
		super();
		this.name = name;
		this.creation = creation;
		this.modified = modificated;
		this.copyrights = copyrights;
		this.licenseName = licenseName;
		this.licenseContent = licenseContent;
		this.version = version;
	}

	public Calendar getCreation() {
		return creation;
	}

	public Calendar getModified() {
		return modified;
	}

	public List<String> getCopyrights() {
		return copyrights;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public String getLicenseContent() {
		return licenseContent;
	}

	public String getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

}
