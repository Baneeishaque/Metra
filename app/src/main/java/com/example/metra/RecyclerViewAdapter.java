package com.example.metra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Sms> modelList;

    private OnItemClickListener mItemClickListener;
    private OnForwardButtonClickListener mForwardButtonClickListener;

    public RecyclerViewAdapter(Context context, ArrayList<Sms> modelList) {

        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<Sms> modelList) {

        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {

            final Sms model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.itemTxtTitle.setText(model.getAddress());
            genericViewHolder.itemTxtMessage.setText(model.getMessage());
            genericViewHolder.itemButtonForward.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    mForwardButtonClickListener.onForwardButtonClick(model);
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnForwardButtonClickListener(final OnForwardButtonClickListener mForwardButtonClickListener) {

        this.mForwardButtonClickListener = mForwardButtonClickListener;
    }

    private Sms getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, Sms model);
    }

    public interface OnForwardButtonClickListener {

        void onForwardButtonClick(Sms model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTxtTitle;
        private TextView itemTxtMessage;
        private Button itemButtonForward;

        public ViewHolder(final View itemView) {

            super(itemView);

            this.itemTxtTitle = itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = itemView.findViewById(R.id.item_txt_message);
            this.itemButtonForward = itemView.findViewById(R.id.button_forward);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }
}

