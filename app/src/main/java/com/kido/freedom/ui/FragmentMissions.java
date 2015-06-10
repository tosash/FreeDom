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
import com.kido.freedom.adapters.MissionsAdapter;
import com.kido.freedom.model.Mission;
import com.kido.freedom.model.ServerResponse.ServerMissionsResponse;
import com.kido.freedom.utils.CustomSwype;
import com.kido.freedom.utils.GsonRequest;
import com.kido.freedom.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMissions extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static String API_GET_Missions = "/GetMission?profileId=";
    private Activity mActivity;
    private Context fContext;
    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView tasksRecyclerView;
    private CardView cardView;
    private CardView taskCardView;
    private List<Mission> missions;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mTasksAdapter;
    private CustomSwype mSwipeRefreshLayout;
    private String TAG = FragmentMissions.class.toString();
    private boolean isTaskRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_missions, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        fContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();

        if (missions == null) {
            missions = new ArrayList<Mission>();
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(fContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        mAdapter = new MissionsAdapter(missions);
        recyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = (CustomSwype) rootView.findViewById(R.id.id_swype);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(255, 155, 30));

        if (isTaskRunning) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (savedInstanceState == null) {
            getMissionsFromServer();
        } else {
            Gson gson = new Gson();
            String s = savedInstanceState.getString("Missions");
            Mission[] obj = gson.fromJson(s, Mission[].class);
            List<Mission> sm = Arrays.asList(obj);
            missions.addAll(sm);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void getMissionsFromServer() {
//        ((MainActivity) getActivity()).showProgressDialog();
        if (!isTaskRunning) {
            isTaskRunning = true;
            mSwipeRefreshLayout.setRefreshing(true);
            Log.d(TAG, "Query: getMissionsFromServer: " + Long.toString(System.currentTimeMillis()));
            VolleySingleton.getInstance(fContext).addToRequestQueue(
                    new GsonRequest<ServerMissionsResponse>(Request.Method.GET,
                            API_GET_Missions + ((MainActivity) getActivity()).curDevice.getProfileId(),
                            ServerMissionsResponse.class,
                            null,
                            new Response.Listener<ServerMissionsResponse>() {
                                @Override
                                public void onResponse(ServerMissionsResponse response) {
//                                ((MainActivity) getActivity()).hideProgressDialog();
                                    isTaskRunning = false;
                                    mSwipeRefreshLayout.setRefreshing(false);
//                                for (int i = 0; i < response.getValue().size(); i++) {
//                                    response.getValue().
//                                }
                                    missions.clear();
                                    missions.add(response.getValue());//ADDALL FOR LIST
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
        getMissionsFromServer();
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
        String s = gson.toJson(missions);
        outState.putString("Missions", s);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Gson gson = new Gson();
        String s = savedInstanceState.getString("Missions");
        Mission[] obj = gson.fromJson(s, Mission[].class);
        missions = Arrays.asList(obj);
        super.onViewStateRestored(savedInstanceState);
    }
}
