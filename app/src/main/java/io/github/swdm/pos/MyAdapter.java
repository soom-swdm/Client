package io.github.swdm.pos;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Product> mDataset;

    public MyAdapter() {
        this.mDataset = new ArrayList<>();
    }

    public void addData(Product product) {
        mDataset.add(product);
        notifyDataSetChanged();
    }

    public void clearData(){
        mDataset.clear();
        notifyDataSetChanged();
    }

    public List<Product> getItems(){
        return mDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_texct_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mDataset.get(position).name);
        holder.category.setText(mDataset.get(position).category);
        holder.price.setText(mDataset.get(position).price+"");
        holder.percent.setText(mDataset.get(position).persent+"");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup root;
        private TextView name;
        private TextView category;
        private TextView price;
        private TextView percent;

        public MyViewHolder(View v) {
            super(v);
            root = (ViewGroup) v;
            name = v.findViewById(R.id.name);
            category = v.findViewById(R.id.category);
            price = v.findViewById(R.id.price);
            percent = v.findViewById(R.id.percent);
        }
    }


}
