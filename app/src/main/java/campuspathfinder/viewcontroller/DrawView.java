package campuspathfinder.viewcontroller;

import android.support.v7.widget.AppCompatImageView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import java.util.List;

import campuspathfinder.model.*;

/**
 * Draws the path for two selected buildings on the campus map
 *
 *
 * Not an ADT
 */
public class DrawView extends AppCompatImageView {

    private boolean draw = false;
    private Location src;
    private Location dest;
    private List<double[]> path;
    private final double SCALE_FACTOR = 0.25;

    /**
     * Creates a new instance of DrawView
     *
     * @param context The context
     * @spec.effects Creates a new instance of DrawView
     */
    public DrawView(Context context) {
        super(context);
    }

    /**
     * Creates a new instance of DrawView
     *
     * @param context The Context
     * @param attrs The AttributeSet
     * @spec.effects Creates a new instance of DrawView
     */
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Creates a new instance of DrawView
     * @param context The Context
     * @param attrs The AttributeSet
     * @param defStyle The defStyle
     * @spec.effects Creates a new instance of DrawView
     */
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Draws the path
     *
     * @param canvas The Canvas to be drawn on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        if (draw) {
            float startX = (float) (src.getLocation().getX() * SCALE_FACTOR);
            float startY = (float) (src.getLocation().getY() * SCALE_FACTOR);
            float endX = (float) (dest.getLocation().getX() * SCALE_FACTOR);
            float endY = (float) (dest.getLocation().getY() * SCALE_FACTOR);
            paint.setColor(Color.BLUE);
            canvas.drawCircle(startX, startY, 10.f, paint);
            canvas.drawCircle(endX, endY, 10.f, paint);
            paint.setColor(Color.RED);
            // {{ Inv: for each double[] p in path that we have seen so far,
            //      it has been drawn on the map as a path from the previous ending location
            //      to the coordinates specified by p[0] and p[1] }}
            for (double[] p : path) {
                endX = (float) (p[0] * SCALE_FACTOR);
                endY = (float) (p[1] * SCALE_FACTOR);
                canvas.drawLine(startX, startY, endX, endY, paint);
                startX = endX;
                startY = endY;
            }
        }
    }

    /**
     * Draws the path from src to dest on the map
     *
     * @param src The starting Location
     * @param dest The ending Location
     * @param path The path to take
     * @spec.effects Draws the path on the map
     */
    public void drawPath(Location src, Location dest, List<double[]> path) {
        draw = true;
        this.src = src;
        this.dest = dest;
        this.path = path;
        this.invalidate();
    }

    /**
     * Resets the path on the map
     * @spec.effects Resets the path on the map
     */
    public void resetPath() {
        draw = false;
        this.invalidate();
    }
}
