package com.example.metra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.common.CommunicationOrigin;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class CommunicationOriginsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<CommunicationOrigin> communicationOrigins;

    private OnItemClickListener onItemClickListener;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public CommunicationOriginsRecyclerViewAdapter(Context context, ArrayList<CommunicationOrigin> communicationOrigins) {

        this.context = context;
        this.communicationOrigins = communicationOrigins;
    }

    public void updateList(ArrayList<CommunicationOrigin> communicationOrigins) {

        this.communicationOrigins = communicationOrigins;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.communication_origins_item_recycler_view_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {

            final CommunicationOrigin communicationOrigin = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.itemTxtTitle.setText(communicationOrigin.getName());
            genericViewHolder.itemTxtMessage.setText(communicationOrigin.getPhoneNumber());
            genericViewHolder.itemButtonDelete.setOnClickListener(v -> onDeleteButtonClickListener.onDeleteButtonClick(communicationOrigin, position));
        }
    }

    @Override
    public int getItemCount() {

        return communicationOrigins.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    public void SetOnDeleteButtonClickListener(final OnDeleteButtonClickListener onDeleteButtonClickListener) {

        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
    }

    private CommunicationOrigin getItem(int position) {
        return communicationOrigins.get(position);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, CommunicationOrigin communicationOrigin);
    }

    public interface OnDeleteButtonClickListener {

        void onDeleteButtonClick(CommunicationOrigin communicationOrigin, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTxtTitle;
        private TextView itemTxtMessage;
        private ImageButton itemButtonDelete;

        public ViewHolder(final View itemView) {

            super(itemView);

            this.itemTxtTitle = itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = itemView.findViewById(R.id.item_txt_message);
            this.itemButtonDelete = itemView.findViewById(R.id.button_delete);

            itemView.setOnClickListener(view -> onItemClickListener.onItemClick(itemView, getAdapterPosition(), communicationOrigins.get(getAdapterPosition())));
        }
    }
}

