package test.test;


import com.badlogic.gdx.math.Vector2;

public final class DoubleGeometryUtils {

    /** Computes the area for a convex polygon. */
    static public double polygonArea (double[] polygon, int offset, int count) {
        float area = 0;
        int last = offset + count - 2;
        double x1 = polygon[last], y1 = polygon[last + 1];
        for (int i = offset; i <= last; i += 2) {
            double x2 = polygon[i], y2 = polygon[i + 1];
            area += x1 * y2 - x2 * y1;
            x1 = x2;
            y1 = y2;
        }
        return area * 0.5f;
    }


    /** modified from GeometryUtils.PolygonCentroid
     * Returns the centroid for the specified non-self-intersecting polygon. */
    static public Vector2 polygonCentroid(double[] polygon, int offset, int count, Vector2 centroid) {
        if (count < 6) throw new IllegalArgumentException("A polygon must have 3 or more coordinate pairs.");

        double area = 0, x = 0, y = 0;
        int last = offset + count - 2;
        double x1 = polygon[last], y1 = polygon[last + 1];
        for (int i = offset; i <= last; i += 2) {
            double x2 = polygon[i], y2 = polygon[i + 1];
            double a = x1 * y2 - x2 * y1;
            area += a;
            x += (x1 + x2) * a;
            y += (y1 + y2) * a;
            x1 = x2;
            y1 = y2;
        }
        if (area == 0) {
            centroid.x = 0;
            centroid.y = 0;
        } else {
            area *= 0.5f;
            centroid.x = (float) (x / (6 * area));
            centroid.y = (float) (y / (6 * area));
        }
        return centroid;
    }
}
