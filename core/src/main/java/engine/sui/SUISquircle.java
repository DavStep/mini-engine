package engine.sui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import engine.Resources;

public class SUISquircle {

    public static Drawable get (int[] cornerRadius, Color color) {
        String region = "squircle/ui-white-squircle";
        for (int radius : cornerRadius) {
            region += "-" + radius;
        }
        return Resources.getDrawable(region, color);
    }

    public static Drawable getBorder (int[] cornerRadius, Color color) {
        String region = "squircle/ui-white-squircle-border";
        for (int radius : cornerRadius) {
            region += "-" + radius;
        }
        return Resources.getDrawable(region, color);
    }

    public static Drawable getBorder (int radius, Color color) {
        return Resources.getDrawable("squircle/ui-white-squircle-border-" + radius, color);
    }
}
