package com.chargebee.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.chargebee.example.R;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<String> mItemList;

    public ItemsAdapter(List<String> mItemList) {
        this.mItemList = mItemList;
    }

    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int position) {
        String itemName = mItemList.get(position);
        holder.myTextView.setText(itemName);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_item);
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}