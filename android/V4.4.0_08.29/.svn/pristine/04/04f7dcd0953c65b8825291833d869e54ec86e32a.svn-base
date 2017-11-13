package com.zhisland.lib.load;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class LoadDBConfigUtil extends OrmLiteConfigUtil {

	private static final String CONFIG_NAME = "ormlite_config_load_new.txt";

	public static void main(String[] args) throws SQLException, IOException {
		File rawDir = findRawDir(new File("./res/raw"));
		File configFile = new File(rawDir, CONFIG_NAME);
		Class<?> cls = LoadDBConfigUtil.class;
		String clsName = cls.getSimpleName();
		String fullName = cls.getCanonicalName();
		String pkgName = fullName.substring(0,
				fullName.length() - clsName.length() - 1);
		String file = ".";
		pkgName = pkgName.replace(".", "/");
		file = "./src/" + pkgName;
		writeConfigFile(configFile, new File(file));
	}
}
