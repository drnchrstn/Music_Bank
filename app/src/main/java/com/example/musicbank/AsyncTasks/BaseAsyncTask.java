package com.example.musicbank.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import com.example.musicbank.AsyncTasks.Response.Listener;

import java.lang.ref.WeakReference;

public abstract class BaseAsyncTask<Params, Progress, T> extends AsyncTask<Params, Progress, Response<T>> {
	private WeakReference<Listener<T>> listenerWrapper;
	private WeakReference<Context> contextWrapper;

	public BaseAsyncTask(Listener<T> listener, Context context) {
		listenerWrapper = new WeakReference<Listener<T>>(listener);
		contextWrapper = new WeakReference<Context>(context);
	}

	protected Listener<T> getListener() {
		return listenerWrapper.get();
	}

	protected Context getContext() {
		return contextWrapper.get();
	}

	@Override
	protected void onPostExecute(Response<T> response) {
		Listener<T> listener = getListener();

		if (listener != null && response != null) {
			if (response.isSuccess()) {
				listener.onResponse(response.getResult());
			} else {
				listener.onErrorResponse(response.getException());
			}
		}
	}
}