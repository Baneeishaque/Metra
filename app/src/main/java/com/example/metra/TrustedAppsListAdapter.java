package com.example.metra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class TrustedAppsListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<TrustedApp> trustedApps;
    private Context context;

    public TrustedAppsListAdapter(ArrayList<TrustedApp> trustedApps, Context context) {
        this.trustedApps = trustedApps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trustedApps.size();
    }

    @Override
    public Object getItem(int position) {
        return trustedApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trustedApps.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = Objects.requireNonNull(inflater).inflate(R.layout.trusted_apps_list_item, null);
        }

        //Handle TextView and display string from your trustedApps
        TextView listItemText = view.findViewById(R.id.list_item_string);
        listItemText.setText(trustedApps.get(position).getApp_name());

        //Handle buttons and add onClickListeners
        Button deleteBtn = view.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO : Confirmation
                TrustedAppsDatabaseHelper trustedAppsDatabaseHelper = new TrustedAppsDatabaseHelper(context);
                trustedAppsDatabaseHelper.deleteTrustedApp(trustedApps.get(position));
                trustedApps.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
