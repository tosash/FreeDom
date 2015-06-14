package com.kido.freedom.ui;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kido.freedom.adapters.StripAdapter;
import com.kido.freedom.model.ServerResponse.ServerStripResponse;
import com.kido.freedom.model.Strip;
import com.kido.freedom.utils.CustomSwype;
import com.kido.freedom.utils.GsonRequest;
import com.kido.freedom.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStrip extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static String API_GET_STRIP = "/GetTape?profileId=";
    private Activity mActivity;
    private Context fContext;
    private View rootView;
    private RecyclerView recyclerView;
    private CardView cardView;
    private List<Strip> strips;
    private RecyclerView.Adapter mAdapter;
    private CustomSwype mSwipeRefreshLayout;
    private String TAG = FragmentStrip.class.toString();
    private boolean mMeasured = false;
    private boolean mPreMeasureRefreshing = false;
    private boolean isTaskRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_strip, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        fContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();

        if (strips == null) {
            strips = new ArrayList<Strip>();
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(fContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        mAdapter = new StripAdapter(this.getFragmentManager(), strips);
        recyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = (CustomSwype) rootView.findViewById(R.id.id_swype);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(255, 155, 30));

        if (isTaskRunning) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (savedInstanceState == null) {
            getStripFromServer();
        } else {
            List<Strip> sstrips;
            Gson gson = new Gson();
            String s = savedInstanceState.getString("Strips");
            Strip[] obj = gson.fromJson(s, Strip[].class);
            if (obj == null) {
                sstrips = new ArrayList<Strip>();
            } else {
                sstrips = Arrays.asList(obj);
            }
            strips.addAll(sstrips);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void getStripFromServer() {
//        ((MainActivity) getActivity()).showProgressDialog();
        if (!isTaskRunning) {
            isTaskRunning = true;
            mSwipeRefreshLayout.setRefreshing(true);
            Log.d(TAG, "Query: getStripFromServer: " + Long.toString(System.currentTimeMillis()));
            VolleySingleton.getInstance(fContext).addToRequestQueue(
                    new GsonRequest<ServerStripResponse>(Request.Method.GET,
                            API_GET_STRIP + ((MainActivity) getActivity()).curDevice.getProfileId(),
                            ServerStripResponse.class,
                            null,
                            new Response.Listener<ServerStripResponse>() {
                                @Override
                                public void onResponse(ServerStripResponse response) {
//                                ((MainActivity) getActivity()).hideProgressDialog();
                                    isTaskRunning = false;
                                    mSwipeRefreshLayout.setRefreshing(false);
//                                for (int i = 0; i < response.getValue().size(); i++) {
//                                    response.getValue().
//                                }
                                    strips.clear();
                                    strips.addAll(response.getValue());
                                    mAdapter.notifyDataSetChanged();
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
                            null), TAG);
        }
    }

    @Override
    public void onRefresh() {
        getStripFromServer();
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
        String s = gson.toJson(strips);
        outState.putString("Strips", s);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Gson gson = new Gson();
        String s = savedInstanceState.getString("Strips");
        Strip[] obj = gson.fromJson(s, Strip[].class);
        if (obj == null) {
            strips = new ArrayList<Strip>();
        } else {
            strips = Arrays.asList(obj);
        }
        super.onViewStateRestored(savedInstanceState);

    }
}
