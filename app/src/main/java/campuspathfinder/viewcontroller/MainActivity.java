package campuspathfinder.viewcontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import campuspathfinder.model.*;
import android.widget.Toast;

/**
 * Main driver of the CampusPathFinder app
 * Can show path between two selected buildings
 *
 *
 * Not an ADT
 */
public class MainActivity extends AppCompatActivity {

    DrawView view;
    ListView buildingsStartList;
    ListView buildingsEndList;
    Campus campus;
    String startBuildingShortName;
    String endBuildingShortName;
    TextView startText;
    TextView endText;

    /**
     * Sets up the interface and widgets
     *
     * @param savedInstanceState Saved state from previous session
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream pathsInputStream = this.getResources().openRawResource(R.raw.campus_paths);
        InputStream buildingsInputStream =
                this.getResources().openRawResource(R.raw.campus_buildings_new);
        //creates the two lists of buildings
        buildingsStartList = findViewById(R.id.BuildingsStart);
        buildingsEndList = findViewById(R.id.BuildingsEnd);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        campus = new Campus();
        try {
            //loads the campus
            campus.loadCampus(buildingsInputStream, pathsInputStream);
            Map<String, String> names = campus.getBuildings();
            for (String s : names.keySet()) {
                adapter.add(s);
            }
        } catch (IOException e) {
            //display error
            System.out.println("IOError in reading files");
            System.exit(0);
        }
        //fills the buildings lists
        buildingsStartList.setAdapter(adapter);
        buildingsEndList.setAdapter(adapter);
        //initializes buttons
        view = findViewById(R.id.CampusMap);
        Button showPathButton = findViewById(R.id.ShowPathButton);
        showPathButton.setOnClickListener(showPathButtonClick);

        Button resetButton = findViewById(R.id.ResetButton);
        resetButton.setOnClickListener(resetButtonClick);

        buildingsStartList.setOnItemClickListener(buildingsStartListItemClick);
        buildingsEndList.setOnItemClickListener(buildingsEndListItemClick);
        //initializes display of starting and ending buildings
        startText = findViewById(R.id.StartBuildingText);
        endText = findViewById(R.id.EndBuildingText);
    }

    /*
     * Button click for the Show Path button
     * Displays the path on the map
     */
    private View.OnClickListener showPathButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            Location src = campus.getLocation(startBuildingShortName);
            Location dest = campus.getLocation(endBuildingShortName);
            List<double[]> path = campus.findPath(startBuildingShortName, endBuildingShortName);
            view.drawPath(src, dest, path);
        }
    };

    /*
     * Button click for the Reset button
     * Resets the path on the map
     */
    private View.OnClickListener resetButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            view.resetPath();
        }
    };

    /*
     * Button click for the starting buildings list
     * Displays the building selected on screen
     */
    private ListView.OnItemClickListener buildingsStartListItemClick =
            new ListView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
            startBuildingShortName = (String) buildingsStartList.getItemAtPosition(position);
            Toast.makeText(getApplicationContext(), startBuildingShortName, Toast.LENGTH_SHORT).show();
            startText.setText(startBuildingShortName);
        }
    };

    /*
     * Button click for the ending buildings list
     * Displays the building selected on screen
     */
    private ListView.OnItemClickListener buildingsEndListItemClick =
            new ListView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
            endBuildingShortName = (String) buildingsEndList.getItemAtPosition(position);
            Toast.makeText(getApplicationContext(), endBuildingShortName, Toast.LENGTH_SHORT).show();
            endText.setText(endBuildingShortName);
        }
    };
}
