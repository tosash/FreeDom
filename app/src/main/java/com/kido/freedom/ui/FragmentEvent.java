package com.kido.freedom.ui;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kido.freedom.R;
import com.kido.freedom.imageindicator.network.NetworkImageIndicatorView;
import com.kido.freedom.model.News;
import com.kido.freedom.model.NewsImage;
import com.kido.freedom.model.ServerResponse.ServerNewsResponse;
import com.kido.freedom.utils.CustomSwype;
import com.kido.freedom.utils.GsonRequest;
import com.kido.freedom.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEvent extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static String API_GET_NEWS = "/GetDetailNews";
    private Activity mActivity;
    private Context fContext;
    private View rootView;
    private CardView cardView;
    private News news;
    private CustomSwype mSwipeRefreshLayout;
    private String TAG = FragmentEvent.class.toString();
    private boolean isTaskRunning = false;
    private NetworkImageIndicatorView imageIndicatorView;
    private TextView txtName;
    private TextView txtDesc;
    private long newsId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        newsId = this.getArguments().getLong("NewsId");
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        fContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();
        imageIndicatorView = (NetworkImageIndicatorView) rootView.findViewById(R.id.network_indicate_view);
        txtName = (TextView) rootView.findViewById(R.id.txt_name);
        txtDesc = (TextView) rootView.findViewById(R.id.txt_desc);

        mSwipeRefreshLayout = (CustomSwype) rootView.findViewById(R.id.id_swype);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(255, 155, 30));

        if (isTaskRunning) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (savedInstanceState == null) {
            getNewsFromServer();
        } else {
            Gson gson = new Gson();
            String s = savedInstanceState.getString("News");
            News obj = gson.fromJson(s, News.class);
            if (obj == null) {
                news = new News();
            } else {
                news = obj;
            }

            showNews(news);
        }

    }

    public void showNews(News mNews) {
        final List<String> urlList = new ArrayList<String>();
        for (NewsImage nIm : mNews.getnImages()) {
            urlList.add(nIm.getMiniUrl());
        }
        imageIndicatorView.setupLayoutByImageUrl(fContext, urlList);
        imageIndicatorView.show();
        txtName.setText(mNews.getnName());
        txtDesc.setText(mNews.getnDesc());

    }

    public void getNewsFromServer() {
//        ((MainActivity) getActivity()).showProgressDialog();
        if (!isTaskRunning) {
            isTaskRunning = true;
            mSwipeRefreshLayout.setRefreshing(true);
            Log.d(TAG, "Query: getNewsFromServer: " + Long.toString(System.currentTimeMillis()));
            JSONObject params = new JSONObject();
            try {
                params.put("Id", newsId);
                params.put("ProfileId", ((MainActivity) this.getActivity()).curDevice.getProfileId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VolleySingleton.getInstance(fContext).addToRequestQueue(
                    new GsonRequest<ServerNewsResponse>(Request.Method.POST,
                            API_GET_NEWS,
                            ServerNewsResponse.class,
                            null,
                            new Response.Listener<ServerNewsResponse>() {
                                @Override
                                public void onResponse(ServerNewsResponse response) {
//                                ((MainActivity) getActivity()).hideProgressDialog();
                                    isTaskRunning = false;
                                    mSwipeRefreshLayout.setRefreshing(false);
//                                for (int i = 0; i < response.getValue().size(); i++) {
//                                    response.getValue().
//                                }
                                    if (response.getStatusResponse() == 0) {
                                        news = response.getValue();
                                        if (news == null) {
                                            news = new News();
                                        }
                                        showNews(news);
                                    } else {
                                        Toast.makeText(fContext, response.getMessage(), Toast.LENGTH_SHORT).show();
                                        FragmentManager fm = getFragmentManager();
                                        fm.popBackStack();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                ((MainActivity) getActivity()).hideProgressDialog();
                                    isTaskRunning = false;
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    Log.e(TAG, "err: " + error.toString());
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        Toast.makeText(fContext, "TimeOut Error", Toast.LENGTH_LONG).show();
                                    } else if (error instanceof AuthFailureError) {
                                        Toast.makeText(fContext, "AuthFailureError", Toast.LENGTH_LONG).show();
                                    } else if (error instanceof ServerError) {
                                        Toast.makeText(fContext, "ServerError", Toast.LENGTH_LONG).show();
                                    } else if (error instanceof NetworkError) {
                                        Toast.makeText(fContext, "NetworkError", Toast.LENGTH_LONG).show();
                                    } else if (error instanceof ParseError) {
                                        Toast.makeText(fContext, "ParseError", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            params), TAG);
        }
    }

    @Override
    public void onRefresh() {
//        getNewsFromServer();
    }

    @Override
    public void onDetach() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isShown()) {
            mSwipeRefreshLayout.setRefreshing(false);
            VolleySingleton.getInstance(fContext).cancelPendingRequests(TAG);

        }
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        String s = gson.toJson(news);
        outState.putString("News", s);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Gson gson = new Gson();
        String s = savedInstanceState.getString("News");
        News obj = gson.fromJson(s, News.class);
        if (obj == null) {
            news = new News();
        } else {
            news = obj;
        }
        super.onViewStateRestored(savedInstanceState);

    }
}
