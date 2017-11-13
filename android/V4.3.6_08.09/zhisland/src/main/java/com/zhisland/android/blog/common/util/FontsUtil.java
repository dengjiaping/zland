package com.zhisland.android.blog.common.util;

import android.graphics.Typeface;

import com.zhisland.lib.async.PriorityFutureTask;
import com.zhisland.lib.async.ThreadManager;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.file.FileMgr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Mr.Tui on 2016/5/31.
 */
public class FontsUtil {

    public static final String PATH_FONTS_DIR = "/FONTS";
    public static final String FONT_FILE_NAME_fzltsjw_0 = "/fzltsjw_0.ttf";
    public static final String ASSET_NAME_fzltsjw_0 = "fzltsjw_0.zip";

    public static Typeface getFzltsjwTypeface() {
        String path = FileMgr.Instance().getRootFolder() + PATH_FONTS_DIR + FONT_FILE_NAME_fzltsjw_0;
        Typeface result = null;
        try {
            result = Typeface.createFromFile(path);
        } catch (Exception e) {
        }
        return result;
    }

    public static void unZipFzltsjwAsync() {
        ThreadManager.instance().execute(
                new PriorityFutureTask<>(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            FontsUtil.unZip(FontsUtil.ASSET_NAME_fzltsjw_0, FileMgr.Instance().getRootFolder() + FontsUtil.PATH_FONTS_DIR, true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, null, ThreadManager.THREAD_PRIORITY_LOWER), null);
    }

    /**
     * 解压assets的zip压缩文件到指定目录
     *
     * @param assetName       压缩文件名
     * @param outputDirectory 输出目录
     * @param isReWrite       是否覆盖
     * @throws IOException
     */
    public static void unZip(String assetName, String outputDirectory, boolean isReWrite) throws IOException {
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 打开压缩文件
        InputStream inputStream = ZHApplication.APP_CONTEXT.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        inputStream.close();
    }

}
