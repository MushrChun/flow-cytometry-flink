package assign2;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.util.Collection;

/**
 * Created by MushrChun on 20/5/17.
 */
public class NearestMapper extends RichMapFunction<Point, Tuple2<Integer, Point>> {

    private Collection<CenterPoint> centerPoints;

    @Override
    public void open(Configuration parameters) throws Exception {
        this.centerPoints = getRuntimeContext().getBroadcastVariable("centerPoints");
    }

    @Override
    public Tuple2<Integer, Point> map(Point o) throws Exception {
        double minDistance = Double.MAX_VALUE;
        int closestCenterPoint = -1;

        for (CenterPoint cp : centerPoints) {
            double distance = cp.euclideanDistance(o);

            if (distance < minDistance) {
                minDistance = distance;
                closestCenterPoint = cp.id;
            }
        }

        return new Tuple2(closestCenterPoint, o);
    }
}


