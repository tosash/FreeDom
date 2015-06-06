package com.kido.freedom.ui;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import com.kido.freedom.R;
import com.kido.freedom.adapters.StripAdapter;
import com.kido.freedom.model.ServerStripResponse;
import com.kido.freedom.model.Strip;
import com.kido.freedom.utils.GsonRequest;
import com.kido.freedom.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStrip extends Fragment {
    private static String API_GET_STRIP = "/GetTape?profileId=";
    private Activity mActivity;
    private Context fContext;
    private View rootView;
    private RecyclerView recyclerView;
    private CardView cardView;
    private List<Strip> strips;
    private RecyclerView.Adapter mAdapter;
    private String TAG = FragmentStrip.class.toString();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_strip, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();
        strips = new ArrayList<Strip>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(fContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        mAdapter = new StripAdapter(strips);
        recyclerView.setAdapter(mAdapter);
        getStripFromServer();
    }

    public void getStripFromServer() {
        ((MainActivity) getActivity()).showProgressDialog();
        VolleySingleton.getInstance(fContext).addToRequestQueue(
                new GsonRequest<ServerStripResponse>(Request.Method.GET,
                        API_GET_STRIP + ((MainActivity) getActivity()).curDevice.getProfileId(),
                        ServerStripResponse.class,
                        null,
                        new Response.Listener<ServerStripResponse>() {
                            @Override
                            public void onResponse(ServerStripResponse response) {
                                ((MainActivity) getActivity()).hideProgressDialog();
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
                                ((MainActivity) getActivity()).hideProgressDialog();
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
