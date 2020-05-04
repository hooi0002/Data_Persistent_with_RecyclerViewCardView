package com.example.data_persistent_with_recyclerviewcardview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

//IN ESSENCE, RECYCLER VIEW NEEDS AN ADAPTER AND VIEW HOLDER

public class MainActivity extends AppCompatActivity {

    //In order for our RecyclerView to work, we will need 3 parts
    //And we create variables for them

    //This variable contains the recycler view we created in our xml layout
    private RecyclerView mRecyclerView;

    //The Adapter is basically a bridge between our data which is the ArrayList and the Recycler View
    //Because we can't just load all our items at once into the Recycler View
    //If we had a really big ArrayList and we load it all at once into the recycler view
    //it would have really bad performance
    //The Adapter basically provides only as many items as we need and fit into the screen
    //and prepare the next item in line as we scroll, so doesn't waste memory
    private ExampleAdapter mAdapter;

    //The Layout manager as the name implies will be responsible
    //for aligning the single items in our list
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<ExampleItem> mExampleList;

    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //res>layout>new>layout resource file

        createExampleList();
        buildRecyclerView();

        buttonInsert = findViewById(R.id.button_insert);
        buttonRemove = findViewById(R.id.button_remove);
        editTextInsert = findViewById(R.id.edittext_insert);
        editTextRemove = findViewById(R.id.edittext_remove);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                insertItem(position);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });

    }

    //But now, App will crash if we input an invalid number for the position, haven't fixed yet
    private void insertItem(int position) {
        mExampleList.add(position, new ExampleItem(R.drawable.ic_android, "New Item At Position" + position, "This is Line 2"));
        mAdapter.notifyItemInserted(position); //This is to notify the Adapter that changes have occured, This will give animation also as we insert or remove items into the Recycler View
    }

    private void removeItem(int position) {
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String text) {
        mExampleList.get(position).changeText1(text);
        //after getting the position of the ExampleList, we want to change the name of the ExampleList
        //for this, go to ExampleItem.java and add a method there
        mAdapter.notifyItemChanged(position);
    }

    public void createExampleList() {
        mExampleList = new ArrayList<>();
        mExampleList.add(new ExampleItem(R.drawable.ic_android, "Line 1", "Line 2"));
        mExampleList.add(new ExampleItem(R.drawable.ic_radio, "Line 3", "Line 4"));
        mExampleList.add(new ExampleItem(R.drawable.ic_alarm, "Line 5", "Line 6"));
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true); //increase the performance of app if you know the adapter won't change in size
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");
            }
        });
        
        //Swipe left or right to delete item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mExampleList.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    //Now we are going to create an OnItemClickListener interface to handle click events like we would do it in a ListView.
    //In a ListView, it is pretty easy to implement because a ListView provided the OnItemClick Interface which also gives us the position we click on
    //Now a RECYCLER DOESN'T HAVE THAT INTERFACE. BUT We will create our own by setting our MainActivity as the listener to this interface

    //For this, we go to ExampleAdapter.java class and make some changes there
    //Item Touch Helper

}
