package com.zhisland.android.blog.common.dto;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final String CONFIG_NAME = "ormlite_config.txt";

    public static void main(String[] args) throws SQLException, IOException {
        String pathRoot = new File("").getAbsolutePath();
        File rawDir = findRawDir(new File(pathRoot + "/zhisland/src/main/res/raw"));
        File configFile = new File(rawDir, CONFIG_NAME);
        String pathSearch = pathRoot + "/zhisland/src/main/java/com/zhisland/android/blog";
        writeConfigFile(configFile, new File(pathSearch));
    }
}
