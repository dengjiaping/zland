package com.zhisland.im.data;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	private static final String CONFIG_NAME = "ormlite_im.txt";

	public static void main(String[] args) throws SQLException, IOException {

		File rawDir = findRawDir(new File("./zhim_lib/src/main/res/raw"));
		File configFile = new File(rawDir, CONFIG_NAME);
		Class<?> cls = DatabaseConfigUtil.class;
		String clsName = cls.getSimpleName();
		String fullName = cls.getCanonicalName();
		String pkgName = fullName.substring(0,
				fullName.length() - clsName.length() - 1);
		String file = ".";
		pkgName = pkgName.replace(".", "/");
		file = "./zhim_lib/src/main/java/" + pkgName;
		writeConfigFile(configFile, new File(file));
	}
}
