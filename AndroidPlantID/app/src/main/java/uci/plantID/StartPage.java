package uci.plantID;

import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;


public class StartPage extends AppCompatActivity
{
    //This plant is the plant we are building from the users answers and is visible in every class
    public static plant queryPlant;
    public static plantDatabase db;
    private ViewController control;
/*
    private static int viewTracker = 0;
    private static int[] question_views = {
            R.layout.activity_start_page,
            R.layout.plant_group_question,
            R.layout.leaf_shape_question,
            R.layout.leaf_arrangement_question,
            R.layout.plant_growth_form_question,
            R.layout.is_flower
    };

    private static int[] question_flower_views = {
            R.layout.flower_color_question,
            R.layout.flower_symmetry_question
    };
    private static ArrayList<ArrayList<String>> queries = new ArrayList<>();
    private static boolean has_flowers = false;
    private static int checkbox_counter = 0;
    private final int numResults = 5;*/


    //TODO: DONE
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();

        setContentView(R.layout.activity_start_page);
    }

    private void init(){
        try {
            db = new plantDatabase( new InputStreamReader( this.getAssets().open("plants.JSON") ));
        }catch( Exception e )
        {
            Log.d( "----ERROR----",  e.getMessage() );
        }
        queryPlant = new plant();
        db = null;
        control = new ViewController(this);
    }
    static int counter = 0;
    public void respond(View view){
        //control.respond(this, view);
        View test = getLayoutInflater().inflate(R.layout.plant_standard_linear_scroll, null);
        LinearLayout a = test.findViewById(R.id.container);
        CardView b = new CardView(this);
        ConstraintLayout c = new ConstraintLayout(this);
        ImageButton d = new ImageButton(this);
        b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        c.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        d.setLayoutParams(new LinearLayout.LayoutParams(250, 250));

        final ViewStub stub = new ViewStub(this);
        stub.setLayoutResource(R.layout.test_stub);


        a.addView(b);
        a.addView(stub);

        b.addView(c);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stub.getParent() != null) {
                    stub.inflate();
                    Log.d(stub.toString(), "inflate");
                }
                else if (stub.isShown()){
                    stub.setVisibility(View.VISIBLE);
                    Log.d(stub.toString(), "visible");
                }
                else{
                    stub.setVisibility(View.GONE);
                    Log.d(stub.toString(),"gone");
                }
            }
        });

        c.addView(d);

        View test2 = getLayoutInflater().inflate(R.layout.plant_standard_linear_scroll, null);


        if (counter == 0)
            setContentView(test);
        else
            setContentView(test2);
        ++counter;
    }
/*
    //this method is called when the next button on the activity_main page is pressed, it must take View view as a parameter, and view give access to certain ui elements
    //TODO: DONE
    public void respondToNextButton(View view)
    {
        buildQuery(view);
        ++viewTracker;
        reset();
        setView(view);
    }

    public void buildQuery(View view) {
        int parentID = ((ViewGroup) view.getParent()).getId();
        View parent = findViewById(parentID);
        CheckBox box;

        queries.add(new ArrayList<String>());

        //TODO: fix temp bad query building control flow
        if (isQuestionTemplate()) {
            ViewGroup container = findViewById(R.id.question_container);

            queries.add(new ArrayList<String>());
            for (int i = 0; i < container.getChildCount(); ++i){
                // gc should take care of problem with back button and redoing answers
                box = (CheckBox) container.getChildAt(i);
                if (isChecked(box)){
                    queries.get(viewTracker - 1).add(box.getText().toString());
                }
            }
            //Log.d("queries", String.valueOf(Arrays.toString(queries[viewTracker-1])));
        }
        else if (viewTracker == (question_views.length - 1)){
            CheckBox flower_checkbox = findViewById(R.id.is_flower_checkbox1);
            has_flowers = isChecked(flower_checkbox);
        }
        else if (viewTracker == question_views.length){
            ViewGroup table = findViewById(R.id.question_container);
            Log.d("# children: ", String.valueOf(table.getChildCount()));
            queries.add(new ArrayList<String>());
            for (int i = 0; i < table.getChildCount(); ++i){
                ViewGroup tableRow = ((ViewGroup) table.getChildAt(i));
                for (int j = 0; j < tableRow.getChildCount(); ++j){
                    box = (CheckBox) tableRow.getChildAt(j);
                    if (isChecked(box)){
                        queries.get(viewTracker - 1).add(box.getTag().toString());
                    }
                }
            }
            //Log.d("queries", String.valueOf(Arrays.toString(queries[viewTracker-2])));

        }
    }
    private boolean isQuestionTemplate(){
        // TODO: fix temp method for distinguishing template questions

        return viewTracker != 0 &&
                viewTracker != (question_views.length - 1) &&
                viewTracker != question_views.length &&
                viewTracker < (question_views.length + question_flower_views.length);
    }

    private void buildPlant(){
        // TODO: optimize
        print2DArray(queries);
        /*
        String[] test = queries.get(0).toArray(new String[0]);
        for (String s : test){
            Log.d("", s);
        }
        Log.d("1:", test.toString());
        Log.d("bool:", String.valueOf(queries.get(0).toArray(new String[0]) == null));
        queryPlant.addPlantGroup(queries.get(0).toArray(new String[0]));
        queryPlant.addLeafType(queries.get(1).toArray(new String[0]));
        queryPlant.addLeafArrangement(queries.get(2).toArray(new String[0]));
        queryPlant.addGrowthForm(queries.get(3).toArray(new String[0]));
        if (has_flowers){
            queryPlant.addFlowerColor("N.A.");
        }
        else {
            queryPlant.addFlowerColor(queries.get(4).toArray(new String[0]));
            queryPlant.addFlowerSymetry(queries.get(5).toArray(new String[0]));
        }
        Log.d("plant:", queryPlant.toString());
    }

    //the previous button doesn't exists on the layouts but this is the method it should call
    public void respondToPrevButton(View view)
    {
        if (viewTracker > 0 && viewTracker < question_views.length + question_flower_views.length) {
            queries.remove(queries.size() - 1);
        }
        --viewTracker;
        reset();
        setView(view);
    }

    public void setView(View view) {
        // TODO: Temporary view flow control

        if (viewTracker < question_views.length) {
            setContentView(question_views[viewTracker]);
            if (viewTracker == (question_views.length - 1)) {
                // set has flower layout next button to true

                Button nextButton = findViewById(R.id.is_flower_template).findViewById(R.id.question_next);
                nextButton.setEnabled(true);
            }

        }
        else if (has_flowers && viewTracker < (question_views.length + question_flower_views.length)) {
            setContentView(question_flower_views[viewTracker - question_views.length]);
        }
        else{
            setContentView(R.layout.results);
            buildPlant();
            ArrayList<rankedPlant> results = db.getGreatestMatch(queryPlant, numResults);
            Log.d("results", Arrays.toString(results.toArray()));
            Log.d("plant", results.get(0).getPlant().toString());
            buildResultsPage(results);
        }
    }

    private void buildResultsPage(ArrayList<rankedPlant> results){
        ViewGroup container = findViewById(R.id.results_container);
        String text = "";
        Log.d("", container.toString());
        TextView[] plants = new TextView[results.size()];
        for (int i = 0; i < results.size(); ++i){
            plants[i] = new TextView(this);
            text = results.get(i).getPlant().getCommonName() + "\n      Rank: " + results.get(i).getRank();
            plants[i].setText(text);
            container.addView(plants[i]);
        }

    }

    public void respondToButton(View view)
    {

        switch ( viewTracker )
        {
            case 0: //start page
                handleStartPageButton(view);
                break;
            case 1: //plant group question
                handlePlantGroupButton(view);
                break;
            case 2: //leaf shape question
                handleLeafShapeButtonButton(view);
                break;
            case 3: //leaf arrangement question
                handleLeafArrangementButton(view);
                break;
            case 4: //plant growth form question
                handleGrowthFormButton(view);
                break;
            case 5: //is flower question
                handleIsFlowerButton(view);
                break;
            case 6: //flower color question
                handleFlowerColorButton(view);
                break;
            case 7: //flower symmetry question
                handleFlowerSymmetryButton(view);
                break;

        }
    }

    public void handleStartPageButton( View view)
    {
        //This is mostly to keep the format the same:
        // The only button on the start page is the next button, so theoretically this method
        //should never be called, unless we add more button at the start
    }

    public void handlePlantGroupButton(View view)
    {
        boolean checked = isChecked(view);
        Button nextButton = findViewById(R.id.plant_group_template).findViewById(R.id.question_next);

        // require minimum API 16 for getParentForAccessibility
        // get parent view of the checkbox
        ViewGroup parent = (ViewGroup) view.getParent();

        // count how many checkboxes have been click
        setCheckbox_counter(checked);

        // diagnostics for debugging
        /*
        Log.d("checked", String.valueOf(checked));
        Log.d("counter", String.valueOf(checkbox_counter));
        Log.d("view id", String.valueOf(view.getId()));
        if (parent != null){
            Log.d("number of children", String.valueOf(((ViewGroup) view.getParent()).getChildCount()));
        }


        enableNextButton(view, nextButton);

        limitCheckbox(parent, true, 5);

    }


    //TODO: Copy the switch from handlePlantGroupButton with appropriate button names and modify the query plant accordingly
    public void handleLeafShapeButtonButton( View view)
    {
        boolean checked = isChecked(view);
        Button nextButton = findViewById(R.id.leaf_shape_template).findViewById(R.id.question_next);

        // require minimum API 16 for getParentForAccessibility
        // get parent view of the checkbox
        ViewGroup parent = (ViewGroup) view.getParent();

        // count how many checkboxes have been click
        setCheckbox_counter(checked);


        enableNextButton(view, nextButton);

        limitCheckbox(parent, true, 6);
    }

    //TODO: Copy the switch from handlePlantGroupButton with appropriate button names and modify the query plant accordingly
    public void handleLeafArrangementButton( View view)
    {
        boolean checked = isChecked(view);
        Button nextButton = findViewById(R.id.leaf_arrangement_template).findViewById(R.id.question_next);

        // require minimum API 16 for getParentForAccessibility
        // get parent view of the checkbox
        ViewGroup parent = (ViewGroup) view.getParent();

        // count how many checkboxes have been click
        setCheckbox_counter(checked);


        enableNextButton(view, nextButton);

        limitCheckbox(parent, true, 6);
    }

    //TODO: Copy the switch from handlePlantGroupButton with appropriate button names and modify the query plant accordingly
    public void handleGrowthFormButton( View view)
    {
        boolean checked = isChecked(view);
        Button nextButton = findViewById(R.id.growth_form_template).findViewById(R.id.question_next);
        // require minimum API 16 for getParentForAccessibility
        // get parent view of the checkbox
        ViewGroup parent = (ViewGroup) view.getParent();

        // count how many checkboxes have been click
        setCheckbox_counter(checked);


        enableNextButton(view, nextButton);

        limitCheckbox(parent, true, 9);

    }

    public void handleIsFlowerButton(View view){
        //boolean checked = isChecked(view);
        //Button nextButton = findViewById(R.id.is_flower_template).findViewById(R.id.question_next);

        // require minimum API 16 for getParentForAccessibility
        // get parent view of the checkbox
        //ViewGroup parent = (ViewGroup) view.getParent();

        // count how many checkboxes have been click
        //setCheckbox_counter(checked);

        //nextButton.setEnabled(true);
        //limitCheckbox(parent, true, 1);
    }

    public void handleFlowerColorButton(View view){
        Button nextButton = findViewById(R.id.flower_color_template).findViewById(R.id.question_next);
        nextButton.setEnabled(true);

    }

    public void handleFlowerSymmetryButton(View view){
        boolean checked = isChecked(view);
        Button nextButton = findViewById(R.id.flower_symmetry_template).findViewById(R.id.question_next);

        // require minimum API 16 for getParentForAccessibility
        // get parent view of the checkbox
        ViewGroup parent = (ViewGroup) view.getParent();

        // count how many checkboxes have been click
        setCheckbox_counter(checked);


        enableNextButton(view, nextButton);

        limitCheckbox(parent, true, 3);
    }

    private boolean isChecked(View view) {
        return ((CheckBox) view).isChecked();
    }

    private void limitCheckbox(ViewGroup parent, boolean isLimited, int limit) {
        // limit how many checkboxes are allowed to be checked if true
        if (checkbox_counter >= limit) {
            disableAllCheckboxes(parent);
        }
        else{
            enableAllView(parent);
        }
    }

    private void enableNextButton(View view, Button nextButton){
        if (checkbox_counter >= 1) {
            nextButton.setEnabled(true);
        }
        else {
            nextButton.setEnabled(false);
        }

    }

    private void disableAllCheckboxes(ViewGroup parent){
        View child;
        Log.d("check disabled", "disable other checkboxes");
        for (int i = 0; i < parent.getChildCount(); ++i){
            child = parent.getChildAt(i);
            if (!((CheckBox) parent.getChildAt(i)).isChecked()){
                child.setEnabled(false);
            }
        }
    }

    private void enableAllView(ViewGroup parent){
        View child;
        for (int i = 0; i < parent.getChildCount(); ++i){
            child = parent.getChildAt(i);
            if (!child.isEnabled()){
                child.setEnabled(true);
            }
        }
    }

    private void setCheckbox_counter(boolean isChecked){
        if (isChecked){
            ++checkbox_counter;
        }
        else{
            --checkbox_counter;
        }
    }

    private void print2DArray(ArrayList<ArrayList<String>> array){
        String log = "";
        for (ArrayList<String> a : array){
            log += a.toString() + "\n";
        }
        Log.d("2D Query:", log);
    }

    private void reset(){
        checkbox_counter = 0;
    }*/
}