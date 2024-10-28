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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.badlogic.gdx.utils.TimeUtils;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class Delaunay2BugRepro extends ApplicationAdapter {

    ShapeRenderer shape;

    DelaunayTriangulator2 delaunay = new DelaunayTriangulator2();
    FloatArray points = new FloatArray();
    ShortArray triangles, tempTriangles;

    Array<Color> colors;

    long startTime;
    int goodLength;

    public void create () {
        MathUtils.random.setSeed(12345);
        startTime = TimeUtils.millis();
        colors = Colors.getColors().values().toArray();
        shape = new ShapeRenderer();

        //bad hexagon:
        // 281.99274,473.00427,
        // 481.97058,126.60693,
        // 881.9485,126.5686,
        // 1081.9927,472.9276,
        // 882.0813,819.3633,
        // 482.10352,819.4784
//        points.add(281.99274f,473.00427f);
//        points.add(481.97058f, 126.60693f);
//        points.add(881.9485f,126.5686f);
//        points.add(1081.9927f,472.9276f);
//        points.add(882.0813f,819.3633f);
//        points.add(482.10352f,819.4784f);


        //good hexagon: same shape and ratio, but shifted in space (translated)
        points.add(287.99274f, 475.00427f);
        points.add(487.97058f, 128.60693f);
        points.add(887.9485f, 128.5686f);
        points.add(1087.9927f, 474.9276f);
        points.add(888.0813f, 821.3633f);
        points.add(488.10352f, 821.4784f);

        triangles = delaunay.computeTriangles(points, false);
        tempTriangles = new ShortArray(triangles);
        goodLength = triangles.size;
    }

    public void render () {

//        if(goodLength == tempTriangles.size) {
            int idx = MathUtils.random(11);
            // randomly moves a point up or down
            points.set(idx, Math.nextAfter(points.get(idx), MathUtils.randomSign() / 0f));
            tempTriangles = delaunay.computeTriangles(points, false);
            if(tempTriangles.size != goodLength){
                triangles = tempTriangles;
                for (int i = 0; i < 6; i++) {
                    System.out.println("points.add("+points.get(i<<1) +"f,"+points.get(i<<1|1) +"f);");
                }
                System.out.println("//Above produces " + (triangles.size/3) + " triangles.\n");
            }
//        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // your code here
        shape.begin(ShapeRenderer.ShapeType.Line);

        //draw points
        shape.setColor(Color.WHITE);
        for (int i = 0; i < points.size; i += 2) {
            float x = points.get(i);
            float y = points.get(i + 1);
            shape.circle(x, y, 3);
        }

        //draw all triangles
        for (int i = 0, c = 1; i < triangles.size; i += 3, c++) {
            int p1 = triangles.get(i) * 2;
            int p2 = triangles.get(i + 1) * 2;
            int p3 = triangles.get(i + 2) * 2;

            float x1 = points.get(p1), y1 = points.get(p1 + 1);
            float x2 = points.get(p2), y2 = points.get(p2 + 1);
            float x3 = points.get(p3), y3 = points.get(p3 + 1);

            shape.setColor(colors.get(c % colors.size));
            shape.triangle(x1, y1, x2, y2, x3, y3);
        }
//
//        //draw a different triangle per second
//        int seconds = (int)(TimeUtils.timeSinceMillis(startTime) / 1000L);
//        for (int i = (seconds * 3) % (triangles.size - 3), c = 1; i < triangles.size; i += 3, c++) {
//            int p1 = triangles.get(i) * 2;
//            int p2 = triangles.get(i + 1) * 2;
//            int p3 = triangles.get(i + 2) * 2;
//
//            float x1 = points.get(p1), y1 = points.get(p1 + 1);
//            float x2 = points.get(p2), y2 = points.get(p2 + 1);
//            float x3 = points.get(p3), y3 = points.get(p3 + 1);
//
////            shape.setColor(colors.get(c % colors.size));
//            shape.triangle(x1, y1, x2, y2, x3, y3);
//            break;
//        }

        shape.end();
    }

}
