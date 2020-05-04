package com.example.data_persistent_with_recyclerviewcardview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private ArrayList<ExampleItem> mExampleList;

    //Choose the one with our package name and .ExampleAdapter
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    //This adapter needs something called ViewHolder
    //Thus need to create an public static inner class for that
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        //create variables for our widgets in example_item.xml
        //Then assign the variables at the constructor
        public ImageView mImageView;
        public TextView mTextView1, mTextView2;

        //click on red light bulb, create constructor matching super
        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Problem is since this is a static class, we can't access our variable mListener here
                    //Since mListener is not static, we can't make it static because it needs to be changed for every different instance
                    //And it is also recommended to make our ExampleViewHolder class static because it is an inner class of the Adapter
                    //So what we do is that we add arguments in the constructor
                    //then in the onCreateViewHolder() method where we create instance of ExampleViewHolder, also need to paste in the mListener argument
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position); //we get our AdaterPosition, save as position, as we paste it in our OnItemClickListener interface's method onItemClick()
                            // And then we get the click and the position from our Adapter to the main activity
                            // Next, go to MainActivity.java
                            //first thing we need to change is private RecyclerView.Adapter mAdapter;
                            //must be more specific, change to private ExampleAdapter mAdapter;
                            //because only this way we can assess the custom method we created, in this case the public void setOnItemClickListener(OnItemClickListener listener)
                            //in the buildRecyclerView() method, call the public void setOnItemClickListener(OnItemClickListener listener) method in this class
                        }
                    }
                }
            });
        }

    }

    //This is where we get our data out of our ArrayList into the Adapter
    //We paste it in the constructor of our ExampleAdapter
    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
