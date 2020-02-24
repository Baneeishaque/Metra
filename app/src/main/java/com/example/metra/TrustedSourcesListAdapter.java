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

public class TrustedSourcesListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<TrustedSource> trustedSources;
    private Context context;

    public TrustedSourcesListAdapter(ArrayList<TrustedSource> trustedSources, Context context) {
        this.trustedSources = trustedSources;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trustedSources.size();
    }

    @Override
    public Object getItem(int position) {
        return trustedSources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trustedSources.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = Objects.requireNonNull(inflater).inflate(R.layout.trusted_source_list_item, null);
        }

        //Handle TextView and display string from your trustedSources
        TextView listItemText = view.findViewById(R.id.list_item_string);
        listItemText.setText(trustedSources.get(position).getPhone_number());

        //Handle buttons and add onClickListeners
        Button deleteBtn = view.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO : Confirmation
                DatabaseHelper trustedSourcesDatabaseHelper = new DatabaseHelper(context);
                trustedSourcesDatabaseHelper.deleteTrustedSource(trustedSources.get(position));
                trustedSources.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
