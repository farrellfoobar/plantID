package uci.plantID;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlantCardCompact {
    private ImageButton trait_image;
    private TextView trait_name;
    private Activity main_activity;
    private View root_layout;
    private ViewGroup parent_layout;
    PlantCardCompact(Activity new_main_activity, View new_root_layout, ViewGroup new_parent_layout, String name){
        trait_image = new ImageButton(new_main_activity);
        trait_name = new TextView(new_main_activity);
        main_activity = new_main_activity;
        root_layout = new_root_layout;
        parent_layout = new_parent_layout;
        setName(name);
        setImage(name);
        setViews();
    }

    public void setName(String name){
        trait_name.setText(name);
    }

    public void setImage(String name){
        trait_image.setImageDrawable(chooseImage(name));
        trait_image.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
    }

    private void setViews(){
        CardView card = new CardView(main_activity);
        ConstraintLayout card_layout = new ConstraintLayout(main_activity);

        card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        card_layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        trait_name.setId(View.generateViewId());
        trait_image.setId(View.generateViewId());

        parent_layout.addView(card);
        card.addView(card_layout);
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

    public Drawable chooseImage(String name){
        Drawable result;
        int drawable_id;
        switch(name) {
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
                drawable_id = R.drawable.compond_deeply_divided;
                break;
            }
            case ("blade"): {
                drawable_id = R.drawable.ic_launcher_background;
                break;
            }
            case ("N.A."): {
                drawable_id = R.drawable.ic_launcher_background;
                break;
            }
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
            case ("basal"): {
                drawable_id = R.drawable.basalrosette;
                break;
            }
            case ("rosette"): {
                drawable_id = R.drawable.basalrosette;
                break;
            }
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
                drawable_id = R.drawable.ic_launcher_background;
                break;
            }
            case ("clump-forming"): {
                drawable_id = R.drawable.ic_launcher_background;
                break;
            }
            case ("vine"): {
                drawable_id = R.drawable.ic_launcher_background;
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
        // IMAGES ARE TOO LARGE
        Drawable original_image = main_activity.getResources().getDrawable(drawable_id);
        if (original_image instanceof VectorDrawable){
            result = main_activity.getResources().getDrawable(R.drawable.ic_launcher_background);
        }
        else {
            Bitmap image = ((BitmapDrawable) original_image).getBitmap();
            Bitmap bitmapResized = Bitmap.createScaledBitmap(image, 100, 100, false);

            result = new BitmapDrawable(main_activity.getResources(), bitmapResized);
        }
        //result = main_activity.getResources().getDrawable(R.drawable.simple);



        return result;
    }

}
