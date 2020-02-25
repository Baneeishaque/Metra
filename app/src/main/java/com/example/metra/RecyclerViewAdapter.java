package com.example.metra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<AbstractModel> modelList;

    private OnItemClickListener mItemClickListener;
//    private OnCheckedListener mOnCheckedListener;

//    private Set<Integer> checkSet = new HashSet<>();

    public RecyclerViewAdapter(Context context, ArrayList<AbstractModel> modelList) {

        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<AbstractModel> modelList) {

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

            final AbstractModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.itemTxtTitle.setText(model.getTitle());
            genericViewHolder.itemTxtMessage.setText(model.getMessage());

//            //in some cases, it will prevent unwanted situations
//            genericViewHolder.itemCheckList.setOnCheckedChangeListener(null);
//            //if true, your checkbox will be selected, else unselected
//            genericViewHolder.itemCheckList.setChecked(checkSet.contains(position));
//            genericViewHolder.itemCheckList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if (isChecked) {
//                        checkSet.add(position);
//                    } else {
//                        checkSet.remove(position);
//                    }
//                    mOnCheckedListener.onChecked(buttonView, isChecked, position, model);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.mItemClickListener = mItemClickListener;
    }

//    public void SetOnCheckedListener(final OnCheckedListener onCheckedListener) {
//
//        this.mOnCheckedListener = onCheckedListener;
//    }

    private AbstractModel getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, AbstractModel model);
    }


//    public interface OnCheckedListener {
//
//        void onChecked(View view, boolean isChecked, int position, AbstractModel model);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTxtTitle;
        private TextView itemTxtMessage;

        public ViewHolder(final View itemView) {

            super(itemView);

            this.itemTxtTitle = itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = itemView.findViewById(R.id.item_txt_message);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }
}

