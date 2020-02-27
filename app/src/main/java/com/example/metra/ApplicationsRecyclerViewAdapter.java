package com.example.metra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class ApplicationsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> applications;

    private OnItemClickListener mItemClickListener;
//    private OnForwardButtonClickListener mForwardButtonClickListener;

    public ApplicationsRecyclerViewAdapter(Context context, ArrayList<String> applications) {

        this.mContext = context;
        this.applications = applications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.applications_item_recycler_view_list, viewGroup, false);
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

//            //Adding click listener on CardView to open clicked application directly from here .
//            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(ApplicationPackageName);
//                    if (intent != null) {
//                        mContext.startActivity(intent);
//                    } else {
//                        Toast.makeText(mContext, ApplicationPackageName + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
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

        this.mItemClickListener = mItemClickListener;
    }

    private String getItem(int position) {
        return applications.get(position);
    }

//    public void SetOnForwardButtonClickListener(final OnForwardButtonClickListener mForwardButtonClickListener) {
//
//        this.mForwardButtonClickListener = mForwardButtonClickListener;
//    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, String model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView textViewAppName;
        TextView textViewAppPackageName;

        public ViewHolder(View itemView) {

            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            imageView = itemView.findViewById(R.id.imageView);
            textViewAppName = itemView.findViewById(R.id.Apk_Name);
            textViewAppPackageName = itemView.findViewById(R.id.Apk_Package_Name);

            itemView.setOnClickListener(view -> mItemClickListener.onItemClick(itemView, getAdapterPosition(), applications.get(getAdapterPosition())));
        }
    }

//    public interface OnForwardButtonClickListener {
//
//        void onForwardButtonClick(Sms model);
//    }
}

