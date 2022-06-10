package com.example.kalori.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.kalori.R;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;

    public ViewPagerAdapter(Context context, List<String> mData, ViewPager2 viewPager2) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.makrodetails,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
    TextView myTextView;
    LinearLayout linearLayout;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        myTextView = itemView.findViewById(R.id.staticticsDayText);
        linearLayout = itemView.findViewById(R.id.container);
    }
}
}
