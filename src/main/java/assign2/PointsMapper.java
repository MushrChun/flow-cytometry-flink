package assign2;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

/**
 * Created by MushrChun on 25/5/17.
 */
public class PointsMapper implements FlatMapFunction<String, Tuple3<Integer, Integer, Point>> {

    public String mask;

    PointsMapper(String mask){
        this.mask = mask;
    }


    public void flatMap(String line, Collector<Tuple3<Integer, Integer, Point>> out) {
        String[] splits = line.split(",");

        String firstCol = splits[0];
        if (firstCol.equals("sample")) {
            return;
        }

        int fsc = Integer.parseInt(splits[1].trim());
        int ssc = Integer.parseInt(splits[2].trim());

        ArrayList<Double> values = new ArrayList<>();

        for (int i = 0, n = mask.length(); i < n; i++) {
            char c = mask.charAt(i);
            if (c == '1') {
                values.add(Double.parseDouble(splits[i + 3].trim()));
            }
        }
        out.collect(new Tuple3<Integer, Integer, Point>(fsc, ssc, new Point(values)));

    }
}
