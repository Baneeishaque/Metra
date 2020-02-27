package com.example.metra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class ApplicationsRecyclerViewAdapterWithDelete extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> applications;

    private OnItemClickListener onItemClickListener;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public ApplicationsRecyclerViewAdapterWithDelete(Context context, ArrayList<String> applications) {

        this.mContext = context;
        this.applications = applications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.applications_with_delete_item_recycler_view_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {

            ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(mContext);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            final String ApplicationPackageName = applications.get(position);
            genericViewHolder.textViewAppName.setText(apkInfoExtractor.GetAppName(ApplicationPackageName));
            genericViewHolder.textViewAppPackageName.setText(ApplicationPackageName);
            genericViewHolder.imageView.setImageDrawable(apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName));

            genericViewHolder.imageButtonDelete.setOnClickListener(v -> onDeleteButtonClickListener.onDeleteButtonClick(position, applications.get(position)));
        }
    }

    @Override
    public int getItemCount() {

        return applications.size();
    }

    public void updateList(ArrayList<String> applications) {

        this.applications = applications;
        notifyDataSetChanged();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.onItemClickListener = mItemClickListener;
    }

    private String getItem(int position) {
        return applications.get(position);
    }

    public void SetOnDeleteButtonClickListener(final OnDeleteButtonClickListener onDeleteButtonClickListener) {

        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, String model);
    }

    public interface OnDeleteButtonClickListener {

        void onDeleteButtonClick(int position, String trustedApp);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView textViewAppName;
        TextView textViewAppPackageName;
        ImageButton imageButtonDelete;

        public ViewHolder(View itemView) {

            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            imageView = itemView.findViewById(R.id.imageView);
            textViewAppName = itemView.findViewById(R.id.Apk_Name);
            textViewAppPackageName = itemView.findViewById(R.id.Apk_Package_Name);
            imageButtonDelete = itemView.findViewById(R.id.button_delete);

            itemView.setOnClickListener(view -> onItemClickListener.onItemClick(itemView, getAdapterPosition(), applications.get(getAdapterPosition())));
        }
    }
}

