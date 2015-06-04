package com.kido.freedom.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kido.freedom.R;
import com.kido.freedom.model.Cities;

import java.util.ArrayList;

public class CitiesAdapter extends ArrayAdapter<Cities>
{
    private Activity context;
    ArrayList<Cities> data = null;

    public CitiesAdapter(Activity context, int resource, ArrayList<Cities> data)
    {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {   // This view starts when we click the spinner.
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_cities, parent, false);
        }

        Cities item = data.get(position);

        if(item != null)
        {   // Parse the data from each object and set it.
            TextView myCity = (TextView) row.findViewById(R.id.cityName);
            if(myCity != null)
                myCity.setText(item.getNameCity());

        }

        return row;
    }
}