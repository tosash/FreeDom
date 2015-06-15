package com.kido.freedom.ui;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
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
import com.kido.freedom.model.Event;
import com.kido.freedom.model.ServerResponse.ServerEventResponse;
import com.kido.freedom.utils.CircularNetworkImageView;
import com.kido.freedom.utils.GsonRequest;
import com.kido.freedom.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEvent extends Fragment {
    public static Event event;
    //        implements SwipeRefreshLayout.OnRefreshListener {
    private static String API_GET_NEWS = "/GetDetailEvent";
    private Activity mActivity;
    private Context fContext;
    private View rootView;
    private CardView cardView;
    //    private CustomSwype mSwipeRefreshLayout;
    private String TAG = FragmentEvent.class.toString();
    private boolean isTaskRunning = false;
    private TextView txtName;
    private TextView txtDesc;
    private CircularNetworkImageView imgEvent;
    private long eventId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event, container, false);
        eventId = this.getArguments().getLong("NewsId");
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        fContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();
        txtName = (TextView) rootView.findViewById(R.id.txtNameEvent);
        txtDesc = (TextView) rootView.findViewById(R.id.txtDescEvent);
        imgEvent = (CircularNetworkImageView) rootView.findViewById(R.id.img_event);
        imgEvent.setCircled(false);

//        mSwipeRefreshLayout = (CustomSwype) rootView.findViewById(R.id.id_swype);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(255, 155, 30));
//
//        if (isTaskRunning) {
//            mSwipeRefreshLayout.setRefreshing(true);
//        }
        if (savedInstanceState == null) {
            getEventFromServer();
        } else {
            Gson gson = new Gson();
            String s = savedInstanceState.getString("Event");
            Event obj = gson.fromJson(s, Event.class);
            if (obj == null) {
                event = new Event();
            } else {
                event = obj;
            }

            showEvent(event);
        }

    }

    public void showEvent(Event mEvent) {
        txtName.setText(mEvent.geteName());
        txtDesc.setText(mEvent.geteDesc());
        imgEvent.setImageUrl(mEvent.geteImage(), VolleySingleton.getInstance(fContext).getImageLoader());

    }

    public void getEventFromServer() {
//        ((MainActivity) getActivity()).showProgressDialog();
        if (!isTaskRunning) {
            isTaskRunning = true;
//            mSwipeRefreshLayout.setRefreshing(true);
            Log.d(TAG, "Query: getEventFromServer: " + Long.toString(System.currentTimeMillis()));
            JSONObject params = new JSONObject();
            try {
                params.put("Id", eventId);
                params.put("ProfileId", MainActivity.getCurDevice().getProfileId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VolleySingleton.getInstance(fContext).addToRequestQueue(
                    new GsonRequest<ServerEventResponse>(Request.Method.POST,
                            API_GET_NEWS,
                            ServerEventResponse.class,
                            null,
                            new Response.Listener<ServerEventResponse>() {
                                @Override
                                public void onResponse(ServerEventResponse response) {
                                    isTaskRunning = false;
//                                    mSwipeRefreshLayout.setRefreshing(false);
                                    if (response.getStatusResponse() == 0) {
                                        event = response.getValue();
                                        if (event == null) {
                                            event = new Event();
                                        }

                                        showEvent(event);
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
//                                    mSwipeRefreshLayout.setRefreshing(false);
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
    public void onDetach() {
//        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isShown()) {
//            mSwipeRefreshLayout.setRefreshing(false);
//            VolleySingleton.getInstance(fContext).cancelPendingRequests(TAG);
//
//        }
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        String s = gson.toJson(event);
        outState.putString("Event", s);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Gson gson = new Gson();
            String s = savedInstanceState.getString("Event");
            Event obj = gson.fromJson(s, Event.class);
            if (obj == null) {
                event = new Event();
            } else {
                event = obj;
            }
        }
        super.onViewStateRestored(savedInstanceState);

    }
}
