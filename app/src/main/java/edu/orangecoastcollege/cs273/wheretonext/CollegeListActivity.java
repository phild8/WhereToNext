package edu.orangecoastcollege.cs273.wheretonext;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.List;

/**
 * The controller - Displays a list adapter of colleges. This class also has an option to add a new
 * college to the existing database.
 */
public class CollegeListActivity extends AppCompatActivity {

    private DBHelper db;
    private List<College> collegesList;
    private CollegeListAdapter collegesListAdapter;
    private ListView collegesListView;

    private EditText mName;
    private EditText mPopulation;
    private EditText mTuition;
    private RatingBar mRating;

    private LinearLayout mLayout;

    /**
     * creates the database and displays it to the user. Allows the user to to interact with the
     * database.
     * @param savedInstanceState Loads a saved instance if one exists.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_list);

        //this.deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);

        mName = (EditText) findViewById(R.id.nameEditText);
        mPopulation = (EditText) findViewById(R.id.populationEditText);
        mTuition = (EditText) findViewById(R.id.tuitionEditText);
        mRating = (RatingBar) findViewById(R.id.collegeRatingBar);

        collegesListView = (ListView) findViewById(R.id.collegeListView);

        // Comment this section out once the colleges below have been added to the database,
        // so they are not added multiple times (prevent duplicate entries)
        db.addCollege(new College("UC Berkeley", 42082, 14068, 4.7, "ucb.png"));
        db.addCollege(new College("UC Irvine", 31551, 15026.47, 4.3, "uci.png"));
        db.addCollege(new College("UC Los Angeles", 43301, 25308, 4.5, "ucla.png"));
        db.addCollege(new College("UC San Diego", 33735, 20212, 4.4, "ucsd.png"));
        db.addCollege(new College("CSU Fullerton", 38948, 6437, 4.5, "csuf.png"));
        db.addCollege(new College("CSU Long Beach", 37430, 6452, 4.4, "csulb.png"));

        // Fill the collegesList with all Colleges from the database
        collegesList = db.getAllColleges();

        // Connect the list adapter with the list
        collegesListAdapter = new CollegeListAdapter(this, R.layout.college_list_item, collegesList);

        // Set the list view to use the list adapter
        collegesListView.setAdapter(collegesListAdapter);
    }

    /**
     * Sends a college details to the CollegeDetailsActivity by collecting the information from each
     * college.
     * @param view The button click to view the college details.
     */
    public void viewCollegeDetails(View view) {
        Intent detailsIntent = new Intent(this, CollegeDetailsActivity.class);

        mLayout = (LinearLayout) view;
        College selected = (College) mLayout.getTag();

        detailsIntent.putExtra("Name", selected.getName());
        detailsIntent.putExtra("Population", selected.getPopulation());
        detailsIntent.putExtra("Tuition", selected.getTuition());
        detailsIntent.putExtra("Rating", selected.getRating());
        detailsIntent.putExtra("ImageName", selected.getImageName());

        startActivity(detailsIntent);

        // Implement the view college details using an Intent
    }

    /**
     * Clears all the colleges from the database.
     * @param view The button click to clear it all.
     */
    public void clearAllTasks(View view){
        collegesList.clear();
        db.deleteAllTasks();
        collegesListAdapter.notifyDataSetChanged();
    }

    /**
     * Adds a new college to the database by collecting information from the edit text fields and
     * rating bar.
     * @param view The button click that adds the college to the database.
     */
    public void addCollege(View view) {

        // Implement the code for when the user clicks on the addCollegeButton
        String name = mName.getText().toString();
        String population = mPopulation.getText().toString();
        String tuition = mTuition.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(population) || TextUtils.isEmpty(tuition))
            Toast.makeText(this, "All information about the college must be provided", Toast.LENGTH_SHORT).show();
        else{
            int newPopulation = Integer.parseInt(population);
            double newTuition = Double.parseDouble(tuition);
            float newRating = mRating.getRating();

            College newCollege = new College(name, newPopulation, newTuition, newRating);

            db.addCollege(newCollege);

            collegesList.add(newCollege);

            collegesListAdapter.notifyDataSetChanged();
            }
    }
}
