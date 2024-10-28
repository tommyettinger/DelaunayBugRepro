package test.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class DoubleDelaunayBugRepro extends ApplicationAdapter {

    ShapeRenderer shape;

    DoubleDelaunayTriangulator delaunay = new DoubleDelaunayTriangulator();
    double[] points;
    IntArray triangles, tempTriangles;

    Array<Color> colors;

    long startTime;
    int goodLength;

    public void create () {
        MathUtils.random.setSeed(12345);
        startTime = TimeUtils.millis();
        colors = Colors.getColors().values().toArray();
        shape = new ShapeRenderer();

        //bad hexagon:
        points = new double[]{
            0x1.19fe2435696e6p8,  0x1.d90117d6b65aap8,  // 281.99274,473.00427,
            0x1.e1f877ee4e26dp8,  0x1.fa463f141205dp6,  // 481.97058,126.60693,
            0x1.b90a6809d4951p9,  0x1.fa463f141205bp6,  // 881.9485,126.5686,
            0x1.0e7f886594af5p10, 0x1.d90117d6b65a8p8,  // 1081.9927,472.9276,
            0x1.b90a6809d4953p9,  0x1.99ae809d49518p9,  // 882.0813,819.3633,
            0x1.e1f877ee4e26fp8,  0x1.99ae809d4951ap9,   // 482.10352,819.4784
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
        tempTriangles = new IntArray(triangles);
        goodLength = triangles.size;
    }

    public void render () {

        if(goodLength == tempTriangles.size) {
            int idx = MathUtils.random(11);
            // randomly moves a point up or down
            points[idx] = Math.nextAfter(points[idx], MathUtils.randomSign() / 0.0);
            tempTriangles = delaunay.computeTriangles(points, false);
            if(tempTriangles.size != goodLength){
                triangles = tempTriangles;
                for (int i = 0; i < 6; i++) {
                    System.out.println(points[i<<1] +", "+points[i<<1|1]);
                }
                System.out.println();
            }
        }

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
