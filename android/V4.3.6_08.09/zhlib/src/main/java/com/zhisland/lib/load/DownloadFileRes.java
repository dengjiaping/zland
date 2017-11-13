package com.zhisland.lib.load;

import java.io.Serializable;

public class DownloadFileRes implements Serializable {

	private static final long serialVersionUID = 6241097933750821453L;

	public int isfinished;

	public int startIndex;

	public int endIndex;

	public long contentLength;

	public long totalSize;
}
