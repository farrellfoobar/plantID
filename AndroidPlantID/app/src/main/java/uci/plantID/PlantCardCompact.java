package uci.plantID;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlantCardCompact extends CardView{
    private ImageButton trait_image;
    private TextView trait_name;
    private Activity main_activity;
    private View root_layout;
    private ViewGroup parent_layout;
    private ViewController controller;
    //private CardView card;
    private ConstraintLayout card_layout;
    private boolean is_selected;
    private boolean is_selected_image;

    private static final BitmapFactory.Options options = new BitmapFactory.Options();
    PlantCardCompact(Activity new_main_activity, View new_root_layout, ViewGroup new_parent_layout, ViewController new_controller, String name){
        super(new_main_activity);
        trait_image = new ImageButton(new_main_activity);
        trait_name = new TextView(new_main_activity);
        main_activity = new_main_activity;
        root_layout = new_root_layout;
        parent_layout = new_parent_layout;
        controller = new_controller;
        is_selected = false;
        is_selected_image = false;

        //card = new CardView(main_activity);
        //card.setClickable(true);
        card_layout = new ConstraintLayout(main_activity);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // if image is selected, set to true, else false and vice versa.
                is_selected = !is_selected;

                Button next_button = main_activity.findViewById(R.id.question_next_button);
                if (is_selected) {
                    v.setBackgroundColor(main_activity.getResources().getColor(R.color.lightgreen));
                    controller.incContainerCounter();
                }
                else{
                    v.setBackgroundColor(main_activity.getResources().getColor(R.color.white));
                    controller.decContainerCounter();
                }
                if (next_button != null){
                    if (controller.getContainerCounter() > 0){
                        next_button.setEnabled(true);
                    }
                    else {
                        next_button.setEnabled(false);
                    }
                }
                else{
                    Log.d("PlantCard:setImage", "next button not found");
                }
            }
        });
        setName(name);
        setImage(name);
        setViews();
    }


    @Override
    public boolean isSelected(){
        return is_selected;
    }

    public void setName(String name){
        trait_name.setText(name);
        trait_name.setTextSize(dpToPx(8));
        trait_name.setTextColor( getResources().getColor(R.color.black) );
    }

    public View getTextView(){
        return trait_name;
    }

    public String getTraitName()
    {
        return trait_name.getText().toString();
    }

    public void setImage(String name){
        trait_image.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(80), dpToPx(80)));
        trait_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        trait_image.setAdjustViewBounds(true);
        //trait_image.setImageDrawable(chooseImage(name));
        trait_image.setImageResource(chooseImage(name));
        trait_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = v.getLayoutParams();
                is_selected_image = !is_selected_image;
                if (is_selected_image){
                    params.width *= 3;
                    params.height *= 3;
                }
                else {
                    params.width /= 3;
                    params.height /= 3;
                }
                v.setLayoutParams(params);

            }
        });
    }

    private void setViews(){

        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        card_layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        trait_name.setId(View.generateViewId());
        trait_image.setId(View.generateViewId());

        parent_layout.addView(this);
        this.addView(card_layout);
        card_layout.addView(trait_image);
        card_layout.addView(trait_name);

        ConstraintSet constraint_set = new ConstraintSet();
        constraint_set.clone(card_layout);
        constraint_set.connect(trait_image.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 10);
        constraint_set.connect(trait_image.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);

        constraint_set.connect(trait_name.getId(), ConstraintSet.LEFT, trait_image.getId(), ConstraintSet.RIGHT, 20);
        constraint_set.connect(trait_name.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 10);

        constraint_set.applyTo(card_layout);

    }

    private int chooseImage(String name){
        int drawable_id;
        switch(name) {
            //Plant Group
            case ("tree"): {
                drawable_id = R.drawable.tree;
                break;
            }
            case ("grass"): {
                drawable_id = R.drawable.grass;
                break;
            }
            case ("shrub"): {
                drawable_id = R.drawable.shrub;
                break;
            }
            case ("forb"): {
                drawable_id = R.drawable.forb;
                break;
            }
            case ("succulent"): {
                drawable_id = R.drawable.succulent;
                break;
            }
            //LeafShape
            case ("simple"): {
                drawable_id = R.drawable.simple;
                break;
            }
            case ("lobed"): {
                drawable_id = R.drawable.lobed;
                break;
            }
            case ("pinnate"): {
                drawable_id = R.drawable.pinnate;
                break;
            }
            case ("compound/ deeply divided"): {
                drawable_id = R.drawable.deeplydivided;
                break;
            }
            case ("blade"): {
                drawable_id = R.drawable.blade;
                break;
            }
            case ("N.A."): {
                drawable_id = R.drawable.na;
                break;
            }
            //Leaf Arrangement
            case ("alternate"): {
                drawable_id = R.drawable.alternate;
                break;
            }
            case ("opposite"): {
                drawable_id = R.drawable.opposite;
                break;
            }
            case ("bundled"): {
                drawable_id = R.drawable.bundled;
                break;
            }
            case ("whorled"): {
                drawable_id = R.drawable.whorled;
                break;
            }
            case ("basal/ rosette"): {
                drawable_id = R.drawable.basalrosette;
                break;
            }
            case ("basal"): {
                drawable_id = R.drawable.basalrosette;      //should not be needed
                break;
            }
            case ("rosette"): {
                drawable_id = R.drawable.basalrosette;      //should not be needed
                break;
            }
            //Growth Form
            case ("prostrate"): {
                drawable_id = R.drawable.prostrate;
                break;
            }
            case ("decumbent"): {
                drawable_id = R.drawable.decumbent;
                break;
            }
            case ("ascending"): {
                drawable_id = R.drawable.ascending;
                break;
            }
            case ("erect"): {
                drawable_id = R.drawable.erect;
                break;
            }
            case ("mat"): {
                drawable_id = R.drawable.mat;
                break;
            }
            case ("clump-forming"): {
                drawable_id = R.drawable.clump;
                break;
            }
            case ("vine"): {
                drawable_id = R.drawable.vine;
                break;
            } case ("radial"): {
                drawable_id = R.drawable.radial;
                break;
            } case ("bilateral"): {
                drawable_id = R.drawable.bilateral;
                break;
            }
            case ("asymmertical"): {
                drawable_id = R.drawable.assymetrical;
                break;
            }
            default: {
                drawable_id = R.drawable.ic_launcher_background;
                break;
            }
        }

        return drawable_id;
    }

    public void reset(){
        if (is_selected){
            this.setBackgroundColor(main_activity.getResources().getColor(R.color.white));
            is_selected = false;
        }
        if (is_selected_image) {
            ViewGroup.LayoutParams params = trait_image.getLayoutParams();
            is_selected_image = !is_selected_image;

            params.width /= 3;
            params.height /= 3;

            trait_image.setLayoutParams(params);
            is_selected_image = false;

        }
        Button next_button = parent_layout.findViewById(R.id.question_next_button);
        //TODO: fix next_button null reference on home button
        if (next_button != null)
            next_button.setEnabled(false);

    }


    private int pxToDp(int px){
        return (int) (px / main_activity.getResources().getDisplayMetrics().density);
    }

    private int dpToPx(int dp){
        return (int) (dp * main_activity.getResources().getDisplayMetrics().density);
    }
}
