package uci.plantID;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Button;


import java.util.List;

public class StartPage extends AppCompatActivity
{
    //This plant is the plant we are building from the users answers and is visible in every class
    public static plant queryPlant;

    private static int viewTracker = 0;
    private static int[] views = {
            R.layout.activity_start_page,
            R.layout.plant_group_question,
            R.layout.leaf_shape_question,
            R.layout.leaf_arrangement_question,
            R.layout.plant_growth_form_question,
            R.layout.is_flower
    };
    private static int[] flower_views = {
            R.layout.flower_color_question,
            R.layout.flower_symmetry_question
    };
    private static int checkbox_counter = 0;
    private final int checkbox_limit = 3;
    //I havent gotten to asking if there is a flower and then conditionally asking the next two questions

    //TODO: DONE
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
    }

    //this method is called when the next button on the activity_main page is pressed, it must take View view as a parameter, and view give access to certain ui elements
    //TODO: DONE
    public void respondToNextButton(View view)
    {
        ++viewTracker;
        reset();
        setView(view);

    }

    //the previous button doesn't exists on the layouts but this is the method it should call
    public void respondToPrevButton(View view)
    {
        --viewTracker;
        reset();
        setView(view);
    }

    public void setView(View view) {
        // Temporary view flow control

        if (viewTracker < views.length) {
            setContentView( views[viewTracker] );
        }
        else if (viewTracker < (views.length + flower_views.length)) {
            setContentView(flower_views[viewTracker - views.length]);
        }
        else{
            setContentView(R.layout.results);
        }
    }

    private void buildQuestionLayout(){

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
        */

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
        boolean checked = isChecked(view);
        Button nextButton = findViewById(R.id.is_flower_template).findViewById(R.id.question_next);

        // require minimum API 16 for getParentForAccessibility
        // get parent view of the checkbox
        ViewGroup parent = (ViewGroup) view.getParent();

        // count how many checkboxes have been click
        setCheckbox_counter(checked);

        enableNextButton(view, nextButton);

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

    private boolean isChecked(View view){
        return ((CheckBox) view).isChecked();
    }

    private void limitCheckbox(ViewGroup parent, boolean isLimited, int limit){
        // limit how many checkboxes are allowed to be checked if true
        if (checkbox_counter >= limit) {
            disableAllCheckboxes(parent);
        }
        else{
            enableAllView(parent);
        }
    }

    private void enableNextButton(View view, Button nextButton){
        if (isChecked(view)) {
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

    private void reset(){
        checkbox_counter = 0;
    }
}