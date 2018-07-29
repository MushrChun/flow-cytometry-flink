package assign2;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

/**
 * Created by MushrChun on 25/5/17.
 */
public class CenterPointsMapper implements FlatMapFunction<String, Tuple2<CenterPoint, Long>> {



    public void flatMap(String line, Collector<Tuple2<CenterPoint, Long>> out) {
        String[] splits = line.split("\t");

        ArrayList<Double> values = new ArrayList<>();

        for (int i = 2, n = splits.length; i < n; i++) {
            values.add(Double.parseDouble(splits[i].trim()));
        }
        out.collect(new Tuple2<CenterPoint, Long>(new CenterPoint(Integer.parseInt(splits[0]), new Point(values)), Long.parseLong(splits[1])));

    }
}
