package uci.plantID;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

class ViewController {
    private static final String activity_start_page = "activity_start_page";
    private static final String question_plant_group = "question_plant_group";
    private static final String question_leaf_shape = "question_leaf_shape";
    private static final String question_leaf_arrangement = "question_leaf_arrangement";
    private static final String question_plant_growth = "question_plant_growth";
    private static final String question_has_flower = "question_has_flower";
    private static final String question_flower_color = "question_flower_color";
    private static final String question_flower_symmetry = "question_flower_symmetry";
    private static final String activity_results = "activity_results";

    private static final String home = "home";
    private static final String next = "next";
    private static final String next_flower_true = "next_flower_true";
    private static final String next_flower_false = "next_flower_false";
    private static final String back = "back";
    private static final String back_flower_true = "back_flower_true";
    private static final String back_flower_false = "back_flower_false";


    public static String head_view = activity_start_page;
    // a map of layouts and their transitions to other layouts
    public static HashMap<String, HashMap<String, String>> layouts_transitions = new HashMap<>();
    public static HashMap<String, Integer> layouts = new HashMap<>();

    ViewController(AppCompatActivity activity){
        init(activity);
    }

    public void respond(AppCompatActivity activity, View view){
        int id = view.getId();
        switch(head_view){
            case activity_start_page: {
                if (id == R.id.next){
                    activity.setContentView(layouts.get(head_view));
                    //head_view = layouts_transitions.get(head_view).get(next);
                    Log.d(view.getTag().toString(), head_view);
                }
                break;
            }
            case question_plant_group: {
                break;
            }
            case question_leaf_shape: {
                break;
            }
            case question_leaf_arrangement: {
                break;
            }
            case question_plant_growth: {
                break;
            }
            case question_has_flower: {
                break;
            }
            case question_flower_color: {
                break;
            }
            case question_flower_symmetry: {
                break;
            }
            case activity_results: {
                break;
            }
        }
    }

    private void init(AppCompatActivity activity){
        createAllLayouts(activity);
        addAllTransitions();

    }

    private void createAllLayouts(AppCompatActivity activity){
        // add start page layout
        buildLayout(activity_start_page, R.layout.activity_start_page);
        // add question layouts based on given template
        buildLayoutFromTemplate(activity, question_plant_group, R.layout.plant_standard_linear_scroll);
        buildLayoutFromTemplate(activity, question_leaf_shape, R.layout.plant_standard_linear_scroll);
        buildLayoutFromTemplate(activity, question_leaf_arrangement, R.layout.plant_standard_linear_scroll);
        buildLayoutFromTemplate(activity, question_plant_growth, R.layout.plant_standard_linear_scroll);
        buildLayoutFromTemplate(activity, question_flower_symmetry, R.layout.plant_standard_linear_scroll);
        // add flower_color layout
        buildLayout(question_flower_color, R.layout.flower_color_question);
        // add has_flower layout
        buildLayout(question_has_flower, R.layout.has_flower_question);
        // add results layout
        buildLayout(activity_results, R.layout.activity_results);

    }

    private void buildLayout(String layout_name, int layout_id){
        layouts.put(layout_name, layout_id);
        layouts_transitions.put(layout_name, new HashMap<String, String>());
    }

    private void buildLayoutFromTemplate(AppCompatActivity activity, String layout_name, int layout_id){
        // create a new ConstraintLayout object from the provided layout
        ConstraintLayout new_layout = (ConstraintLayout) activity.getLayoutInflater().inflate(layout_id, null);


        layouts.put(layout_name, new_layout.getId());
        layouts_transitions.put(layout_name, new HashMap<String, String>());
    }


    private void addTransition(String src_layout, String transition_name, String dest_layout){
        layouts_transitions.get(src_layout).put(transition_name, dest_layout);
    }

    private void addAllTransitions(){
        // start page layout transitions
        addTransition(activity_start_page, next, question_plant_group);

        // plant group layout transitions
        addTransition(question_plant_group, next, question_leaf_shape);
        addTransition(question_plant_group, back, activity_start_page);
        addTransition(question_plant_group, home, activity_start_page);


        // leaf shape layout transitions
        addTransition(question_leaf_shape, next, question_leaf_arrangement);
        addTransition(question_leaf_shape, back, question_plant_group);
        addTransition(question_leaf_shape, home, activity_start_page);


        // leaf arrangement layout transitions
        addTransition(question_leaf_arrangement, next, question_plant_growth);
        addTransition(question_leaf_arrangement, back, question_leaf_shape);
        addTransition(question_leaf_arrangement, home, activity_start_page);


        // plant growth layout transitions
        addTransition(question_plant_growth, next, question_has_flower);
        addTransition(question_plant_growth, back, question_leaf_arrangement);
        addTransition(question_plant_growth, home, activity_start_page);


        // has flower layout transition
        addTransition(question_has_flower, next_flower_true, question_flower_color);
        addTransition(question_has_flower, next_flower_false, activity_results);
        addTransition(question_has_flower, back, question_plant_growth);
        addTransition(question_has_flower, home, activity_start_page);


        // flower color layout transition
        addTransition(question_flower_color, next, question_flower_symmetry);
        addTransition(question_flower_color, back, question_has_flower);
        addTransition(question_flower_color, home, activity_start_page);


        // flower symmetry layout transition
        addTransition(question_flower_symmetry, next, activity_results);
        addTransition(question_flower_symmetry, back, question_flower_color);
        addTransition(question_flower_symmetry, home, activity_start_page);


        // results layout transition
        addTransition(activity_results, back_flower_true, question_flower_symmetry);
        addTransition(activity_results, back_flower_false, question_has_flower);
        addTransition(activity_results, home, activity_start_page);

    }

}
