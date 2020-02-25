package com.example.metra;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class ListMessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private RecyclerViewAdapter mAdapter;
    private ArrayList<AbstractModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_messages);
        findViews();
        setSupportActionBar(toolbar);
        setAdapter();
    }

    private void findViews() {

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(this.getComponentName()));

        //changing edittext color
        EditText searchEdit = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!Character.isLetterOrDigit(source.charAt(i)))
                        return "";
                }
                return null;
            }
        };
        searchEdit.setFilters(fArray);

        View v = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<AbstractModel> filterList = new ArrayList<AbstractModel>();

                if (s.length() > 0) {

                    for (int i = 0; i < modelList.size(); i++) {

                        if (modelList.get(i).getTitle().toLowerCase().contains(s.toLowerCase())) {

                            filterList.add(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }

                } else {

                    mAdapter.updateList(modelList);
                }
                return false;
            }
        });
        return true;
    }

    private void setAdapter() {

        modelList.add(new AbstractModel("Android", "Hello " + " Android"));
        modelList.add(new AbstractModel("Beta", "Hello " + " Beta"));
        modelList.add(new AbstractModel("Cupcake", "Hello " + " Cupcake"));
        modelList.add(new AbstractModel("Donut", "Hello " + " Donut"));
        modelList.add(new AbstractModel("Eclair", "Hello " + " Eclair"));
        modelList.add(new AbstractModel("Froyo", "Hello " + " Froyo"));
        modelList.add(new AbstractModel("Gingerbread", "Hello " + " Gingerbread"));
        modelList.add(new AbstractModel("Honeycomb", "Hello " + " Honeycomb"));
        modelList.add(new AbstractModel("Ice Cream Sandwich", "Hello " + " Ice Cream Sandwich"));
        modelList.add(new AbstractModel("Jelly Bean", "Hello " + " Jelly Bean"));
        modelList.add(new AbstractModel("KitKat", "Hello " + " KitKat"));
        modelList.add(new AbstractModel("Lollipop", "Hello " + " Lollipop"));
        modelList.add(new AbstractModel("Marshmallow", "Hello " + " Marshmallow"));
        modelList.add(new AbstractModel("Nougat", "Hello " + " Nougat"));
        modelList.add(new AbstractModel("Android O", "Hello " + " Android O"));

        mAdapter = new RecyclerViewAdapter(ListMessagesActivity.this, modelList);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, AbstractModel model) {

                //handle item click events here
                Toast.makeText(ListMessagesActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.SetOnCheckedListener(new RecyclerViewAdapter.OnCheckedListener() {

            @Override
            public void onChecked(View view, boolean isChecked, int position, AbstractModel model) {

                Toast.makeText(ListMessagesActivity.this, (isChecked ? "Checked " : "Unchecked ") + model.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
