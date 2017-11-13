package com.zhisland.lib.load;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.zhisland.lib.util.MLog;

import de.greenrobot.event.EventBus;

public abstract class BaseLoadMgr<T extends BaseLoadInfo> {

	protected final String TAG;

	/**可同时执行的任务数,最小为1*/
	protected int concurrent = 1;
	
	/**当前正在执行的任务的token集合*/
	protected ArrayList<Long> curLoads;
	
	protected Context context;

	public BaseLoadMgr() {
		this.TAG = this.getClass().getSimpleName();
		concurrent = 1;
		this.curLoads = new ArrayList<Long>();
	}

	protected void init(Context context) {
		this.context = context;
	}

	/**
	 * 当loadService启动时，执行该方法。注册EventBus。
	 * 
	 */
	public void onStart() {
		EventBus.getDefault().register(this);
	}

	/**
	 * 收到Event消息，重新抓取任务执行。
	 * */
	public void onEvent(T loadInfo) {
		if (loadInfo != null && loadInfo.status != LoadStatus.LOADING) {
			tryFetchLoad();
		}
	}

	public void onStop() {
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 如果当前执行的任务数，小于可同时执行的任务数，重新取任务添加到当前任务list。
	 * */
	void tryFetchLoad() {
		if (curLoads.size() < concurrent) {
			List<Long> loads = fetchLoad(concurrent - curLoads.size(), curLoads);
			if (loads != null) {
				for (Long token : loads) {
					curLoads.add(token);
					tryLoadFileByToken(token);
					MLog.d(TAG, "start upload " + token);
				}
			}
		}
	}

	/**
	 * 尝试开始某个任务
	 * 
	 * @param token
	 * @return
	 */
	void tryLoadFileByToken(long token) {
		final T info = getLoadInfo(token);
		if (info == null) {
			curLoads.remove(token);
			tryFetchLoad();
			return;
		}
		switch (info.status) {
		case LoadStatus.WAIT: {
			updateStatusByToken(token, LoadStatus.LOADING);
			loadFile(info);
			break;
		}
		case LoadStatus.ERROR:
		case LoadStatus.ERROR_FILE_MISS:
		case LoadStatus.ERROR_NETWORK: {
			curLoads.remove(token);
			tryFetchLoad();
			return;
		}
		}

	}

	/**
	 * 尝试开始某个任务
	 * 
	 * @param context
	 *            the context to start service
	 * @param info
	 */
	public T startLoad(Context context, T info) {
		if (info == null)
			return info;

		Intent intent = new Intent(context, LoadService.class);
		context.startService(intent);

		info.status = LoadStatus.WAIT;
		long token = insert(info);
		if (token >= 0) {
			info.token = token;
			postEvent(info);
		}
		return info;
	}

	/**
	 * 尝试开始某个任务
	 * 
	 * @param context
	 *            the context to start service
	 * @param token
	 * @return
	 */
	public int startByToken(Context context, long token) {
		if (context != null) {
			Intent intent = new Intent(context, LoadService.class);
			context.startService(intent);
		}

		int i = updateStatusByToken(token, LoadStatus.WAIT);
		postEvent(token);
		return i;
	}

	/**
	 * 暂停某一个任务
	 * 
	 * @param token
	 */
	public int stopByToken(long token) {
		int i = updateStatusByToken(token, LoadStatus.PAUSE);
		postEvent(token);
		return i;
	}

	/**
	 * 任务失败
	 * 
	 * @param token
	 * @param status
	 * */
	public int failLoad(long token, int status) {
		curLoads.remove(token);
		int i = updateStatusByToken(token, status);
		postEvent(token);
		return i;
	}

	/**
	 * 任务完成
	 * 
	 * @param token
	 * */
	public int finishLoad(long token, Object obj) {
		curLoads.remove(token);
		int i = updateStatusByToken(token, LoadStatus.FINISH);
		if (i > 0) {
			postEvent(token);
		}
		return i;
	}

	/**
	 * 暂停某人的服务
	 * 
	 * @param ownerId
	 * @return
	 */
	public int stopByOwnerId(long ownerId) {
		return updateStatusByOwnerId(ownerId, LoadStatus.ERROR);
	}

	/**
	 * 判断指定任务是否完成。
	 * 1: finish; 0: not existed; -1 not finished
	 * 
	 * @param token
	 * @return
	 */
	public int getFinishStatus(long token) {
		BaseLoadInfo info = this.getLoadInfo(token);
		if (info == null) {
			return 0;
		} else if (info.status == LoadStatus.FINISH) {
			return 1;
		} else {
			return -1;
		}

	}

	/**
	 * post notify by event bus
	 * 
	 * @param info
	 */
	protected void postEvent(T info) {
		if (info != null) {
			EventBus.getDefault().post(info);
		}
	}

	protected void postEvent(long token) {
		T info = getLoadInfo(token);
		postEvent(info);
	}

	// ==============abstract methods===============

	/**
	 * 获取新的加载任务
	 * 
	 * @param count
	 * @param curLoads
	 *            当前的加载任务token，新的任务列表不应该包含这里的任意一个任务
	 * @return
	 */
	public abstract List<Long> fetchLoad(int count, ArrayList<Long> curLoads);

	/**
	 * 执行一次任务
	 * 
	 * @param token
	 */
	public abstract void loadFile(T info);

	/**
	 * 根据token获取任务的信息
	 * 
	 * @param token
	 * @return
	 */
	public abstract T getLoadInfo(long token);

	/**
	 * 
	 * @param info
	 * @return new token
	 */
	public abstract long insert(T info);

	/**
	 * 删除默认的所有任务
	 * 
	 * @param ownerId
	 * @return
	 */
	public abstract int deleteByOwnerId(long ownerId);

	/**
	 * 删除指定的load任务
	 * 
	 * @param token
	 * @return
	 */
	public abstract int deleteByToken(long token);

	/**
	 * 更新指定任务的状态
	 * @param token
	 * @param status
	 *            see {@link LoadStatus}
	 * @return
	 */
	public abstract int updateStatusByToken(long token, int status);

	/**
	 * 更新指定owner的所有任务的状态
	 * */
	public abstract int updateStatusByOwnerId(long ownerId, int status);

}
