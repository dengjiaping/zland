package com.zhisland.android.blog.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class DeleteResForLint {
	public static String workSpacePath = "";

	public static final String separator = File.separator;
	/**
	 * android sdk 的路径,注意 '/'位置
	 */
	public static String sdkPath = "/Users/zhangxiang/Downloads/adt-bundle-mac-x86_64-20131030/sdk/";
	
	/**
	 * android sdk下的tools路径
	 */
	public static String sdkToolsPath = "/Users/zhangxiang/Downloads/adt-bundle-mac-x86_64-20131030/sdk/tools/";
	
	/**
	 * android sdk,tools下 lint执行文件的路径
	 */
	public static String sdkToolsLintPath = "/Users/zhangxiang/Downloads/adt-bundle-mac-x86_64-20131030/sdk/tools/lint";
	
	//以下是工程的相对路径
	public static String zhislandPath = "/zhisland";
	public static String ZHImPath = "/ZHIm";
	public static String ZhislandLibPath = "/ZhislandLib";
	public static String PullToRefreshPath = "/PullToRefresh";
	
	
	public static String resultPath = "";
	public static boolean TEST = true;
	public static HashMap<String, ArrayList<Integer>> fileMap = new HashMap<String, ArrayList<Integer>>();

	/**
	 * 初始化工作空间的绝对路径
	 */
	public static void setWorkSpacePath() {
		workSpacePath = DeleteResForLint.class.getResource("/").getPath();
		workSpacePath = workSpacePath.substring(0, workSpacePath.length() - 5);
		workSpacePath = workSpacePath.substring(0,
				workSpacePath.lastIndexOf('/'));
		zhislandPath = workSpacePath + "/zhisland/";
		ZHImPath = workSpacePath + "/ZHIm/";
		ZhislandLibPath = workSpacePath + "/ZhislandLib/";
		PullToRefreshPath = workSpacePath + "/PullToRefresh/";
	}

	

	/**
	 * 删除无用的R.string或R.dimen等文件内的内容，但是只能删除一行，慎用
	 */
	public static void delAll() {
		Set<String> set = fileMap.keySet();
		BufferedReader srcBR = null;
		BufferedWriter toWR = null;
		for (String path : set) {
			File tmp = new File(path + "tmp");
			File src = new File(path);

			if (!src.exists()) {
				continue;
			}
			try {
				srcBR = new BufferedReader(new FileReader(new File(path)));
				toWR = new BufferedWriter(new FileWriter(tmp));
				String line = "";
				int lineNo = 0;
				while ((line = srcBR.readLine()) != null) {
					lineNo++;
					if (isContain(fileMap.get(path), lineNo)) {
						continue;
					}
					toWR.write(line + "\n");
					toWR.flush();
				}
				srcBR.close();
				toWR.close();
				src.delete();
				tmp.renameTo(src);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			srcBR.close();
			toWR.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 统计无用的R.string或R.dimen等资源文件中的内容，然后用delAll()方法删掉
	 * @param projectPath
	 * @param deleteResultFile
	 */
	public static void perDelFileInLine(String projectPath,
			boolean deleteResultFile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(sdkPath +"/"+ resultPath));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("UnusedResources")
						&& line.contains("res/value")
						&& !line.contains("appcompat")) {
					System.out.println(line+"\n\n\n\n\n");
					String[] unit = line.split(":");
					if (unit.length <= 1) {
						continue;
					}
					String file = projectPath + unit[0];
					int lineNo = -1;
					try {
						lineNo = Integer.valueOf(unit[1]);
						System.out.println(lineNo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!fileMap.containsKey(file)) {
						fileMap.put(file, new ArrayList<Integer>());
					}
					if (lineNo != -1) {
						fileMap.get(file).add(lineNo);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (deleteResultFile) {
				new File(sdkPath +"/"+ resultPath).delete();
			}
		}
	}

	/**
	 * 删除无用的文件
	 * @param autoDelete
	 * @param lintPath
	 * @param projectPath
	 * @param dirArray
	 */
	public static void clearRes(boolean autoDelete, String lintPath,
			String projectPath, String[] dirArray) {
		Process process = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		resultPath = "result"+System.currentTimeMillis()+"_"+new Random().nextInt(10000)+".txt";		
		String cmd = lintPath + " --check UnusedResources " + projectPath;
		int fileCount = 0;
		long fileSize = 0;
		String line;
		try {
			File tempfile = new File(sdkPath +"/"+ resultPath);
			tempfile.createNewFile();
			System.out.println(resultPath);
			fw = new FileWriter(sdkPath +"/"+ resultPath);
			Runtime runtime = Runtime.getRuntime();
			line = null;

			process = runtime.exec(cmd);
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {
				fw.write(line+"\n");
				fw.flush();
				for (String dir : dirArray) {
					if (line.startsWith("res" + separator + dir)) {
						int index = line.indexOf(":");
						if (index > 0) {
							String filePath = projectPath + separator
									+ line.substring(0, index);
							++fileCount;
							File file = new File(filePath);
							fileSize += file.length();
							System.out.println("unused file: " + filePath);
							if (autoDelete) {
								boolean success = file.delete();
								System.out.println(filePath + " " + success);
							}
						}
						break;
					}
				}
			}

			String result = "delete file " + fileCount + ",save space "
					+ fileSize / 1024 + "KB.";
			System.out.println(result);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (process != null) {
				process.destroy();
			}
		}
	}

	public static boolean isContain(ArrayList<Integer> list, int no) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == no) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		setWorkSpacePath();
		String[] dirArray = { "drawable", "layout", "anim", "color", "string",
				"array" };
		String projectPath[] = { zhislandPath, ZHImPath, ZhislandLibPath,
				PullToRefreshPath };
		for (String path : projectPath) {
			DeleteResForLint.clearRes(true, sdkToolsLintPath, path, dirArray);
			perDelFileInLine(path, false);
			delAll();
			System.out.println(projectPath);
		}

		System.out.print("done");
	}
}