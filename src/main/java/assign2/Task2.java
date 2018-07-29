package assign2;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.tuple.*;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;



/**
 * Created by MushrChun on 20/5/17.
 */
public class Task2 {

    public static void main(String[] args) throws Exception {

        final ParameterTool params = ParameterTool.fromArgs(args);
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();


        String sampleDir = params.getRequired("sample-dir");
        String mask = params.get("mask", "00110001000000");
        int iteratorTimes = params.getInt("iterator", 10);
        int k = params.getInt("k", 10);


        DataSet<Point> sample =
                env.readTextFile(sampleDir)
                        .flatMap(new PointsMapper(mask))
                        .filter( tuple -> {
                            if(tuple.f0>=0 && tuple.f0<=150000 && tuple.f1>=0 && tuple.f1<=150000){
                                return true;
                            }else{
                                return false;
                            }
                        })
                        .map(new MapFunction<Tuple3<Integer, Integer, Point>, Point>() {
                            public Point map(Tuple3<Integer, Integer, Point> value) { return value.f2; }
                        });



        DataSet<CenterPoint> centerPoints = RandomUtil.sample(k, sample);
        IterativeDataSet<CenterPoint> loop = centerPoints.iterate(iteratorTimes);

        DataSet<CenterPoint> newCenterPoints = sample
                .map(new NearestMapper()).withBroadcastSet(loop, "centerPoints")
                .map( tuple -> {
                    return new Tuple3<Integer, Point, Long>(tuple.f0, tuple.f1, 1L);
                })
                .groupBy(0)
                .reduce( (tuple1, tuple2) -> {
                    return new Tuple3<Integer, Point, Long>(tuple1.f0, tuple1.f1.add(tuple2.f1), tuple1.f2+ tuple2.f2);
                })
                .map( tuple -> {
                    return new CenterPoint(tuple.f0, tuple.f1.div(tuple.f2));
                });

        DataSet<CenterPoint> finalCenterPoints = loop.closeWith(newCenterPoints);

        DataSet<Tuple3<Integer, Long, Point>> clusteredPoints = sample
                .map(new NearestMapper()).withBroadcastSet(finalCenterPoints, "centerPoints")
                .map( tuple -> {
                    return new Tuple3<Integer, Point, Long>(tuple.f0, tuple.f1, 1L);
                })
                .groupBy(0)
                .reduce( (tuple1, tuple2) -> {
                    return new Tuple3<Integer, Point, Long>(tuple1.f0, tuple1.f1.add(tuple2.f1), tuple1.f2+ tuple2.f2);
                })
                .map( tuple -> {
                    return new Tuple3<Integer, Long, Point>(tuple.f0, tuple.f2, tuple.f1.div(tuple.f2));
                })
                .sortPartition(0, Order.ASCENDING).setParallelism(1);



        // End the program by writing the output!
        if(params.has("output")) {
            clusteredPoints.writeAsCsv(params.get("output"),  "\n", "\t", FileSystem.WriteMode.OVERWRITE);
//            sample.writeAsCsv(params.get("output"),  "\n", "\t", FileSystem.WriteMode.OVERWRITE);

            env.execute();
        } else {
            // Always limit direct printing
            // as it requires pooling all resources into the driver.
            System.err.println("No output location specified; printing first 100.");
            clusteredPoints.first(100).print();
//            sample.first(100).print();
        }

    }

}
