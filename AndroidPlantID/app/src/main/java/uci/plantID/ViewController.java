package uci.plantID;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

class ViewController {
    private static int index = 0;
    private static final int ACTIVITY_START_PAGE = index++;
    private static final int QUESTION_PLANT_GROUP = index++;
    private static final int QUESTION_LEAF_SHAPE = index++;
    private static final int QUESTION_LEAF_ARRANGEMENT = index++;
    private static final int QUESTION_PLANT_GROWTH_FORM = index++;
    private static final int QUESTION_HAS_FLOWER = index++;
    private static final int QUESTION_FLOWER_COLOR = index++;
    private static final int QUESTION_FLOWER_SYMMETRY = index++;
    private static final int ACTIVITY_RESULTS = index++;
    private View[] layouts = new View[index];
    private int layout_counter = 0;



    ViewController(AppCompatActivity activity){
        init(activity);
    }

    public void nextButton(Activity activity, View view, plant plant_query, plantDatabase db){
        buildQuery(activity, view, plant_query);
        if (layout_counter == QUESTION_HAS_FLOWER &&
                !plant_query.getFlowerColor().isEmpty() &&
                plant_query.getFlowerColor().get(0).equals(plant.validFlowerColors.get(plant.validFlowerColors.size() - 1))){
            layout_counter = ACTIVITY_RESULTS;
        }
        if (layout_counter != ACTIVITY_RESULTS) {
            ++layout_counter;
        }
        Log.d("counter", String.valueOf(layout_counter));
        activity.setContentView(layouts[layout_counter]);


    }

    public void backButton(Activity activity, View view, plant plant_query, plantDatabase db){
        revertQuery(activity, view, plant_query);
        if (layout_counter == ACTIVITY_RESULTS &&
                !plant_query.getFlowerColor().isEmpty() &&
                plant_query.getFlowerColor().get(0).equals(plant.validFlowerColors.get(plant.validFlowerColors.size() - 1))){
            layout_counter = QUESTION_HAS_FLOWER;
        }
        else {
            --layout_counter;
        }
        Log.d("counter", String.valueOf(layout_counter));
        activity.setContentView(layouts[layout_counter]);

    }


    public void homeButton(Activity activity, View view){
        activity.setContentView(R.layout.activity_start_page);
        layout_counter = ACTIVITY_START_PAGE;
    }

    private void buildQuery(Activity activity, View view, plant plant_query){

    }

    private void revertQuery(Activity activity, View view, plant plant_query){

    }


    private void init(AppCompatActivity activity){
        layouts[ACTIVITY_START_PAGE] = buildLayout(activity, ACTIVITY_START_PAGE, R.layout.activity_start_page);
        layouts[QUESTION_PLANT_GROUP] = buildLayoutFromTemplate(activity, QUESTION_PLANT_GROUP, R.layout.plant_standard_linear_scroll);
        layouts[QUESTION_LEAF_SHAPE] = buildLayoutFromTemplate(activity, QUESTION_LEAF_SHAPE, R.layout.plant_standard_linear_scroll);
        layouts[QUESTION_LEAF_ARRANGEMENT] = buildLayoutFromTemplate(activity, QUESTION_LEAF_ARRANGEMENT, R.layout.plant_standard_linear_scroll);
        layouts[QUESTION_PLANT_GROWTH_FORM] = buildLayoutFromTemplate(activity, QUESTION_PLANT_GROWTH_FORM, R.layout.plant_standard_linear_scroll);
        layouts[QUESTION_HAS_FLOWER] = buildLayout(activity, QUESTION_HAS_FLOWER, R.layout.has_flower_question);
        layouts[QUESTION_FLOWER_COLOR] = buildLayout(activity, QUESTION_FLOWER_COLOR, R.layout.flower_color_question);
        layouts[QUESTION_FLOWER_SYMMETRY] = buildLayoutFromTemplate(activity, QUESTION_FLOWER_SYMMETRY, R.layout.plant_standard_linear_scroll);
        layouts[ACTIVITY_RESULTS] = buildLayout(activity, ACTIVITY_RESULTS, R.layout.activity_results);
    }

    private View buildLayout(Activity activity, int layout_index, int layout_id){
        View result = activity.getLayoutInflater().inflate(layout_id, null);
        TextView title = result.findViewById(R.id.question_title);
        if (title != null){
            title.setText(chooseQuestionTitle(layout_index));
        }
        else {
            Log.d(String.valueOf(layout_index), "ViewController::buildLayout_ question title not found");
        }
        return result;
    }

    private View buildLayoutFromTemplate(Activity activity, int layout_index, int layout_id){
        // create a new ConstraintLayout object from the provided layout
        ConstraintLayout result = (ConstraintLayout) activity.getLayoutInflater().inflate(layout_id, null);
        TextView title = result.findViewById(R.id.question_title);
        if (title != null){
            title.setText(chooseQuestionTitle(layout_index));
        }
        else {
            Log.d(String.valueOf(layout_index), "ViewController::buildLayout_ question title not found");
        }
        buildCards(activity, layout_index, result);
        return result;
    }

    private void buildCards(Activity activity, int layout_index, View layout){
        List<String> traitsToIterate;
        if (layout_index == QUESTION_PLANT_GROUP){
            traitsToIterate = plant.validPlantGroups;
        }
        else if (layout_index == QUESTION_LEAF_SHAPE){
            traitsToIterate = plant.validLeafTypes;
        }
        else if (layout_index == QUESTION_LEAF_ARRANGEMENT){
            traitsToIterate = plant.validLeafArrangements;
        }
        else if (layout_index == QUESTION_PLANT_GROWTH_FORM){
            traitsToIterate = plant.validGrowthForms;
        }
        else if (layout_index == QUESTION_FLOWER_SYMMETRY){
            traitsToIterate = plant.validFlowerSymetrys;
        }
        else{
            return;
        }
        for (String trait : traitsToIterate){
            buildCard(activity, layout_index, layout, trait);
        }
    }
    private PlantCardCompact buildCard(Activity activity, int layout_index, View layout, String trait){
        ViewGroup container = layout.findViewById(R.id.container);
        PlantCardCompact card;
        if (container != null) {
            card = new PlantCardCompact(activity, layout, container, trait);
        }
        else {
            Log.d("", "ViewController::buildCard_ container not found");
            return null;
        }
        return card;
    }

    private String chooseQuestionTitle(int layout_index){
        String result;
        if (layout_index == QUESTION_PLANT_GROUP){
            result = "Plant Group";
        }
        else if (layout_index == QUESTION_LEAF_SHAPE){
            result = "Leaf Shape";
        }
        else if (layout_index == QUESTION_LEAF_ARRANGEMENT){
            result = "Leaf Arrangement";
        }
        else if (layout_index == QUESTION_PLANT_GROWTH_FORM){
            result = "Plant Growth Form";
        }
        else if (layout_index == QUESTION_HAS_FLOWER){
            result = "Does the Plant Have a Flower?";
        }
        else if (layout_index == QUESTION_FLOWER_COLOR){
            result = "Flower Color";
        }
        else if (layout_index == QUESTION_FLOWER_SYMMETRY){
            result = "Flower Symmetry";
        }
        else{
            result = "";
        }
        return result;
    }






}
