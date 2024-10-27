package test.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.utils.*;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class DoubleDelaunayBugRepro extends ApplicationAdapter {

    ShapeRenderer shape;

    DoubleDelaunayTriangulator delaunay = new DoubleDelaunayTriangulator();
    double[] points;
    IntArray triangles;

    Array<Color> colors;

    long startTime;

    public void create () {
        startTime = TimeUtils.millis();
        colors = Colors.getColors().values().toArray();
        shape = new ShapeRenderer();

        //bad hexagon:
        points = new double[]{
             281.99274,473.00427,
             481.97058,126.60693,
             881.9485,126.5686,
             1081.9927,472.9276,
             882.0813,819.3633,
             482.10352,819.4784
        };

        /*
        //good hexagon: same shape and ratio, but shifted in space (translated)
        points.add(287.99274f, 475.00427f);
        points.add(487.97058f, 128.60693f);
        points.add(887.9485f, 128.5686f);
        points.add(1087.9927f, 474.9276f);
        points.add(888.0813f, 821.3633f);
        points.add(488.10352f, 821.4784f);
        */

        triangles = delaunay.computeTriangles(points, false);
    }

    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // your code here
        shape.begin(ShapeRenderer.ShapeType.Line);

        //draw points
        shape.setColor(Color.WHITE);
        for (int i = 0; i < points.length; i += 2) {
            float x = (float)points[i];
            float y = (float)points[i+1];
            shape.circle(x, y, 3);
        }

        int seconds = (int)(TimeUtils.timeSinceMillis(startTime) / 1000L);

        //draw all triangles
        for (int i = 0, c = 1; i < triangles.size; i += 3, c++) {
            int p1 = triangles.get(i) * 2;
            int p2 = triangles.get(i + 1) * 2;
            int p3 = triangles.get(i + 2) * 2;

            float x1 = (float) points[p1], y1 = (float) points[p1 + 1];
            float x2 = (float) points[p2], y2 = (float) points[p2 + 1];
            float x3 = (float) points[p3], y3 = (float) points[p3 + 1];

            shape.setColor(colors.get(c % colors.size));
            shape.triangle(x1, y1, x2, y2, x3, y3);
        }
//
//        //draw a different triangle per second
//        for (int i = (seconds * 3) % (triangles.size), c = 1; i < triangles.size; i += 3, c++) {
//            int p1 = triangles.get(i) * 2;
//            int p2 = triangles.get(i + 1) * 2;
//            int p3 = triangles.get(i + 2) * 2;
//
//            float x1 = (float) points[p1], y1 = (float) points[p1 + 1];
//            float x2 = (float) points[p2], y2 = (float) points[p2 + 1];
//            float x3 = (float) points[p3], y3 = (float) points[p3 + 1];
//
////            shape.setColor(colors.get(c % colors.size));
//            shape.triangle(x1, y1, x2, y2, x3, y3);
//            break;
//        }

        shape.end();
    }

}
