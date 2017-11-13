package com.zhisland.lib.async.http.task;

/**
 * the asynchronous call from http layer, you can work with response by
 * overriding {@link #onStart()}, {@link #onFinish()},
 * {@link #onSuccess(Object)},{@link #onFailure(Object)}.
 * 
 * <p>
 * in addition, if want to parse response in background thread, including error
 * message. please override {@link #handleSuccessMessageInBackGround(String)}
 * and {@link #handleFailureMessageInBackGround(Throwable, String)}.
 * 
 * @author arthur
 * 
 * @param <S>
 * @param <F>
 */
public abstract class TaskCallback<S> {

	public void onStart() {
	}

	public void onFinish() {
	}

	public abstract void onSuccess(S content);

	public abstract void onFailure(Throwable error);

}
