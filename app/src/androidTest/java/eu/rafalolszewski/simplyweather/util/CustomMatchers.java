package eu.rafalolszewski.simplyweather.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Created by Rafał Olszewski on 29.03.16.
 */
public class CustomMatchers {

    public static Matcher<View> isImageTheSame(final Drawable drawable) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("is image the same as: ");
                description.appendValue(drawable);
            }

            @Override
            protected boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable().getConstantState()
                        .equals(drawable.getConstantState());
            }
        };
    }

}
