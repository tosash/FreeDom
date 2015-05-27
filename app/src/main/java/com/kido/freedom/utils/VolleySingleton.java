package com.kido.freedom.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
	private static VolleySingleton mInstance = null;
	private static Context mContext;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	public static final String TAG = VolleySingleton.class
			.getSimpleName();

	private VolleySingleton(Context context) {
		mContext = context;
		mRequestQueue = getRequestQueue();
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
	}

	public static synchronized VolleySingleton getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new VolleySingleton(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new BitmapLruCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		req.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		req.setShouldCache(true);
		getRequestQueue().add(req);

	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		req.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		req.setShouldCache(true);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}
