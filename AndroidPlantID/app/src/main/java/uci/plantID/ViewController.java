package uci.plantID;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
    private static final String[] questions = {"What is the Plant Group?", "What is the Leaf Shape?", "What is the Leaf Arrangement?", "What is the Growth Form?", "Is there a Flower?", "What Color is the Flower?", "What is the Flower Symmetry?"};
    private View[] layouts = new View[index];
    private Vector<ArrayList<View>> queries = new Vector<>(index);
    private int layout_counter = 0;
    private int container_counter = 0;
    private static int num_ranked_plants = 5;



    ViewController(AppCompatActivity activity){
        init(activity);
    }

    public void nextButton(Activity activity, View view, plant plant_query, plantDatabase db){
        Switch has_flowers = (Switch) queries.get(QUESTION_HAS_FLOWER).get(0);
        if (layout_counter == QUESTION_HAS_FLOWER &&
                !has_flowers.isChecked()){
            layout_counter = ACTIVITY_RESULTS;
        }
        else {
            ++layout_counter;
        }
        activity.setContentView(layouts[layout_counter]);

        if (layout_counter == ACTIVITY_RESULTS) {
            plant_query = new plant();
            buildQuery(activity, view, plant_query);
            displayResults(activity, plant_query, db);
        }

        Log.d("counter", String.valueOf(layout_counter));

        reset();
    }

    public void backButton(Activity activity, View view, plant plant_query, plantDatabase db){
        revertQuery(activity, view, plant_query);
        Switch has_flowers = (Switch) queries.get(QUESTION_HAS_FLOWER).get(0);
        if (layout_counter == ACTIVITY_RESULTS &&
                !has_flowers.isChecked()){
            layout_counter = QUESTION_HAS_FLOWER;
        }
        else {
            --layout_counter;
        }
        Log.d("counter", String.valueOf(layout_counter));
        activity.setContentView(layouts[layout_counter]);
        reset();
    }


    public void homeButton(Activity activity, View view){
        resetQueries();
        layout_counter = ACTIVITY_START_PAGE;
        activity.setContentView(layouts[layout_counter]);
        reset();

    }

    public void respondToButton(Activity activity, View view){
        if (layout_counter == QUESTION_FLOWER_COLOR){
            if (((CheckBox) view).isChecked()){
                incContainerCounter();
            }
            else{
                decContainerCounter();
            }
            if (container_counter > 0){
                activity.findViewById(R.id.question_next_button).setEnabled(true);
            }
            else{
                activity.findViewById(R.id.question_next_button).setEnabled(false);
            }
        }
    }

    private void displayResults(Activity activity, plant plant_query, plantDatabase db){
        LinearLayout root_container = activity.findViewById(R.id.results_container);
        if (root_container == null){
            throw new Error("container id not found");
        }
        if (root_container.getChildCount() > 0){
            root_container.removeAllViews();
        }
        LinearLayout plant_container;
        ScrollView plant_image_scroll;
        HorizontalScrollView plant_image_scroll_horizonal;
        LinearLayout plant_image_scroll_container;
        ImageView plant_image;
        TextView plant_name;
        TextView plant_rank;
        ArrayList<rankedPlant> results = db.getGreatestMatch(plant_query, num_ranked_plants);
        for (rankedPlant plant : results)
        {
            plant_container = new LinearLayout(activity);
            plant_name = new TextView(activity);
            plant_rank = new TextView(activity);
            plant_image_scroll = new ScrollView(activity);
            plant_image_scroll_horizonal = new HorizontalScrollView(activity);
            plant_image_scroll_container = new LinearLayout(activity);
            plant_image_scroll_container.setHorizontalScrollBarEnabled( true );

            plant_container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            plant_image_scroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            plant_container.setOrientation(LinearLayout.VERTICAL);
            plant_image_scroll_container.setOrientation(LinearLayout.HORIZONTAL);

            // some plants dont have a common name
            if( !plant.getPlant().getCommonName().equals("") )
                plant_name.setText(plant.getPlant().getScientificName() + " A.K.A " + plant.getPlant().getCommonName());
            else
                plant_name.setText(plant.getPlant().getScientificName() );

            plant_name.setTextColor( ContextCompat.getColor( activity, R.color.black ) );
            plant_rank.setText( String.valueOf( String.format( "%.2f", plant.getRank() ) ) + "% match" + "\n\n");
            plant_rank.setTextColor( ContextCompat.getColor( activity, R.color.black ) );
            plant_image = new ImageView(activity);
            plant_image.setLayoutParams(new LinearLayout.LayoutParams(400,400));
            plant_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            plant_image.setAdjustViewBounds(true);

            for (Drawable image : plant.getPlant().getImage()){
                plant_image = new ImageView(activity);
                plant_image.setLayoutParams(new LinearLayout.LayoutParams(400,400));
                plant_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                plant_image.setAdjustViewBounds(true);
                plant_image.setImageDrawable(image);
                plant_image_scroll_container.addView(plant_image);
            }

            plant_image_scroll_horizonal.addView(plant_image_scroll_container);
            plant_image_scroll.addView(plant_image_scroll_horizonal);
            plant_container.addView(plant_image_scroll);
            plant_container.addView(plant_name);
            plant_container.addView(plant_rank);
            root_container.addView(plant_container);
        }
    }

    private void buildQuery(Activity activity, View view, plant plant_query){
        Switch has_flowers = (Switch) queries.get(QUESTION_HAS_FLOWER).get(0);
        for (int index = 0; index < queries.size(); ++index){
            if (index != ACTIVITY_START_PAGE &&
                    index != ACTIVITY_RESULTS){
                for (View v : queries.get(index)){

                    if (index == QUESTION_PLANT_GROUP){
                        if (v.isSelected()){
                            plant_query.addPlantGroup(((PlantCardCompact) v).getTraitName());
                        }
                    }
                    else if (index == QUESTION_LEAF_SHAPE){
                        if (v.isSelected()){
                            plant_query.addLeafType(((PlantCardCompact) v).getTraitName());
                        }
                    }
                    else if (index == QUESTION_LEAF_ARRANGEMENT){
                        if (v.isSelected()){
                            plant_query.addLeafArrangement(((PlantCardCompact) v).getTraitName());
                        }
                    }
                    else if (index == QUESTION_PLANT_GROWTH_FORM){
                        if (v.isSelected()){
                            plant_query.addGrowthForm(((PlantCardCompact) v).getTraitName());
                        }
                    }
                    else if (index == QUESTION_HAS_FLOWER){
                        if (((Switch) v).isChecked()){
                            plant_query.addFlowerColor("N.A.");
                        }
                    }
                    else if (index == QUESTION_FLOWER_COLOR){
                        if (((CheckBox) v).isChecked() && has_flowers.isChecked()){
                            plant_query.addFlowerColor(((CheckBox) v).getTag().toString());
                        }
                    }
                    else if (index == QUESTION_FLOWER_SYMMETRY){
                        if (v.isSelected() && has_flowers.isChecked()){
                            plant_query.addFlowerSymetry(((PlantCardCompact) v).getTraitName());
                        }
                    }

                }
            }
        }
    }

    private void revertQuery(Activity activity, View view, plant plant_query){
        if (layout_counter == QUESTION_PLANT_GROUP ||
                layout_counter == QUESTION_LEAF_SHAPE ||
                layout_counter == QUESTION_LEAF_ARRANGEMENT ||
                layout_counter == QUESTION_PLANT_GROWTH_FORM ||
                layout_counter == QUESTION_FLOWER_SYMMETRY){
            for (View v : queries.get(layout_counter)){
                ((PlantCardCompact) v).reset();
            }
        }
        else if (layout_counter == QUESTION_HAS_FLOWER){
            Switch has_flower = (Switch) queries.get(layout_counter).get(0);
            if (has_flower.isChecked()){
                has_flower.setChecked(false);
            }
        }
        else if (layout_counter == QUESTION_FLOWER_COLOR){
            for (View v : queries.get(layout_counter)){
                if (((CheckBox) v).isChecked()){
                    ((CheckBox) v).setChecked(false);
                }
            }

        }
    }


    private void init(AppCompatActivity activity){
        layouts[ACTIVITY_START_PAGE] = buildLayout(activity, ACTIVITY_START_PAGE, R.layout.activity_start_page);
        layouts[QUESTION_PLANT_GROUP] = buildLayoutFromTemplate(activity, QUESTION_PLANT_GROUP, R.layout.plant_standard_linear_scroll);
        layouts[QUESTION_LEAF_SHAPE] = buildLayoutFromTemplate(activity, QUESTION_LEAF_SHAPE, R.layout.plant_standard_linear_scroll);
        layouts[QUESTION_LEAF_ARRANGEMENT] = buildLayoutFromTemplate(activity, QUESTION_LEAF_ARRANGEMENT, R.layout.plant_standard_linear_scroll);
        layouts[QUESTION_PLANT_GROWTH_FORM] = buildLayoutFromTemplate(activity, QUESTION_PLANT_GROWTH_FORM, R.layout.plant_standard_linear_scroll);

        layouts[QUESTION_HAS_FLOWER] = buildLayout(activity, QUESTION_HAS_FLOWER, R.layout.has_flower_question);
        layouts[QUESTION_HAS_FLOWER].findViewById(R.id.question_next_button).setEnabled(true);

        layouts[QUESTION_FLOWER_COLOR] = buildLayout(activity, QUESTION_FLOWER_COLOR, R.layout.flower_color_question);
        layouts[QUESTION_FLOWER_SYMMETRY] = buildLayoutFromTemplate(activity, QUESTION_FLOWER_SYMMETRY, R.layout.plant_standard_linear_scroll);
        layouts[ACTIVITY_RESULTS] = buildLayout(activity, ACTIVITY_RESULTS, R.layout.activity_results);
    }

    private View buildLayout(Activity activity, int layout_index, int layout_id){
        View result = activity.getLayoutInflater().inflate(layout_id, null);
        TextView title = result.findViewById(R.id.question_title);
        ViewGroup container;
        if (title != null){
            title.setText(chooseQuestionTitle(layout_index));
        }
        else {
            Log.d(String.valueOf(layout_index), "ViewController::buildLayout_ question title not found");
        }
        if (layout_index != QUESTION_HAS_FLOWER &&
                layout_index != QUESTION_FLOWER_COLOR){
            queries.add(layout_index, null);
        }
        else {
            queries.add(layout_index, new ArrayList<View>());
            container = result.findViewById(R.id.container);
            if (container != null){
                for (int index = 0; index < container.getChildCount(); ++index){
                    queries.get(layout_index).add(container.getChildAt(index));
                }
            }
            else {
                throw new Error("container not found");
            }
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
        queries.add(layout_index, new ArrayList<View>());
        PlantCardCompact card;
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
            card = buildCard(activity, layout_index, layout, trait);

            queries.get(layout_index).add(card);
        }
    }
    private PlantCardCompact buildCard(Activity activity, int layout_index, View layout, String trait){
        ViewGroup container = layout.findViewById(R.id.container);
        PlantCardCompact card;
        if (container != null) {
            card = new PlantCardCompact(activity, layout, container, this, trait);
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
            result = questions[0];
        }
        else if (layout_index == QUESTION_LEAF_SHAPE){
            result = questions[1];
        }
        else if (layout_index == QUESTION_LEAF_ARRANGEMENT){
            result = questions[2];
        }
        else if (layout_index == QUESTION_PLANT_GROWTH_FORM){
            result = questions[3];
        }
        else if (layout_index == QUESTION_HAS_FLOWER){
            result = questions[4];
        }
        else if (layout_index == QUESTION_FLOWER_COLOR){
            result = questions[5];
        }
        else if (layout_index == QUESTION_FLOWER_SYMMETRY){
            result = questions[6];
        }
        else{
            result = "";
        }
        return result;
    }

    private void reset(){
        container_counter = 0;
        if (layout_counter == QUESTION_PLANT_GROUP ||
                layout_counter == QUESTION_LEAF_SHAPE ||
                layout_counter == QUESTION_LEAF_ARRANGEMENT ||
                layout_counter == QUESTION_PLANT_GROWTH_FORM ||
                layout_counter == QUESTION_FLOWER_SYMMETRY){
            for (View v : queries.get(layout_counter)){
                if (v.isSelected()){
                    ++container_counter;
                }
            }
        }
        else if (layout_counter == QUESTION_FLOWER_COLOR){
            for (View v : queries.get(layout_counter)){
                if (((CheckBox) v).isChecked()){
                    ++container_counter;
                }
            }

        }


    }

    private void resetQueries(){
        for (int index = 0; index < queries.size(); ++index){
            if (index == QUESTION_PLANT_GROUP ||
                    index == QUESTION_LEAF_SHAPE ||
                    index == QUESTION_LEAF_ARRANGEMENT ||
                    index == QUESTION_PLANT_GROWTH_FORM ||
                    index == QUESTION_FLOWER_SYMMETRY){
                for (View v : queries.get(index)){
                    ((PlantCardCompact) v).reset();
                }
            }
            else if (index == QUESTION_HAS_FLOWER){
                Switch has_flower = (Switch) queries.get(index).get(0);
                if (has_flower.isChecked()){
                    has_flower.setChecked(false);
                }
            }
            else if (index == QUESTION_FLOWER_COLOR){
                for (View v : queries.get(index)){
                    if (((CheckBox) v).isChecked()){
                        ((CheckBox) v).setChecked(false);
                    }
                }

            }
        }
    }

    public void incContainerCounter(){
        ++container_counter;
    }

    public void decContainerCounter(){
        --container_counter;
        if (container_counter < 0){
            throw new Error("container counter out of bounds");
        }
    }

    public int getContainerCounter(){
        return container_counter;
    }

}
