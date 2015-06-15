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
import com.kido.freedom.adapters.MissionsTaskAdapter;
import com.kido.freedom.model.Mission;
import com.kido.freedom.model.MissionTaskStatus;
import com.kido.freedom.model.ServerResponse.ServerMissionsResponse;
import com.kido.freedom.utils.CustomSwype;
import com.kido.freedom.utils.GsonRequest;
import com.kido.freedom.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMissions extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static String API_GET_Missions = "/GetMission?profileId=";
    private static List<MissionTaskStatus> missionsTask;
    private static Mission mission;
    private Activity mActivity;
    private Context fContext;
    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private CustomSwype mSwipeRefreshLayout;
    private String TAG = FragmentMissions.class.toString();
    private boolean isTaskRunning = false;
    private TextView txtName;
    private TextView txtPoints;
    private CardView crdMission;
    private TextView txtNameNext;
    private TextView txtPointsNext;
    private CardView crdMissionNext;

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

        if (mission == null) {
            mission = new Mission();
        }

        txtName = (TextView) rootView.findViewById(R.id.txt_name);
        txtPoints = (TextView) rootView.findViewById(R.id.txt_points);
        crdMission = (CardView) rootView.findViewById(R.id.crd_mission);
        crdMission.setCardBackgroundColor(Color.parseColor("#4A6472"));
        crdMission = (CardView) rootView.findViewById(R.id.crd_header);
        crdMission.setCardBackgroundColor(Color.parseColor("#FAC770"));

        txtNameNext = (TextView) rootView.findViewById(R.id.txt_nameNext);
        txtPointsNext = (TextView) rootView.findViewById(R.id.txt_pointsNext);
        crdMission = (CardView) rootView.findViewById(R.id.crd_Next);
        crdMission.setCardBackgroundColor(Color.parseColor("#4A6472"));
        crdMission = (CardView) rootView.findViewById(R.id.crd_headerNext);
        crdMission.setCardBackgroundColor(Color.parseColor("#FAC770"));

        recyclerView = (RecyclerView) rootView.findViewById(R.id.tasksList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(fContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        if (missionsTask == null) {
            missionsTask = new ArrayList<MissionTaskStatus>();
        }
        mAdapter = new MissionsTaskAdapter(missionsTask);
        recyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = (CustomSwype) rootView.findViewById(R.id.id_swype);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(255, 155, 30));
        mSwipeRefreshLayout.setEnabled(false);

        if (isTaskRunning) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (savedInstanceState == null) {
            getMissionsFromServer();
        } else {
            Gson gson = new Gson();
            String s = savedInstanceState.getString("Mission");
            Mission obj = gson.fromJson(s, Mission.class);

            List<MissionTaskStatus> sm;
            if (obj == null) {
                sm = new ArrayList<MissionTaskStatus>();
            } else {
                sm = obj.getmTasks();
            }
            if (missionsTask == null) {
                missionsTask = new ArrayList<MissionTaskStatus>();
            } else {
                missionsTask.clear();
            }
            missionsTask.addAll(sm);
            mAdapter.notifyDataSetChanged();
            setMissionValues();
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
                            API_GET_Missions + MainActivity.getCurDevice().getProfileId(),
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
                                    mission = response.getValue();
                                    if (missionsTask == null) {
                                        missionsTask = new ArrayList<MissionTaskStatus>();
                                    } else {
                                        missionsTask.clear();
                                    }
                                    missionsTask.addAll(mission.getmTasks());//ADDALL FOR LIST
                                    mAdapter.notifyDataSetChanged();
                                    setMissionValues();
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
        String s = gson.toJson(mission);
        outState.putString("Mission", s);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Gson gson = new Gson();
            String s = savedInstanceState.getString("Mission");
            Mission obj = gson.fromJson(s, Mission.class);
            if ((obj == null) || (missionsTask == null)) {
                missionsTask = new ArrayList<MissionTaskStatus>();
            } else {
                missionsTask.clear();
                missionsTask.addAll(obj.getmTasks());
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    public void setMissionValues() {
        txtName.setText("Миссия #" + Long.toString(mission.getmNumber()));
        txtPoints.setText(Long.toString(mission.getmPoints()));
        txtPointsNext.setText(Long.toString(mission.getmPointMax()));
        txtNameNext.setText("Миссия #" + Long.toString(mission.getmNumber() + 1));
    }
}
