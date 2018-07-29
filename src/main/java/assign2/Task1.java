package assign2;

import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;

/**
 * Created by MushrChun on 18/5/17.
 */
public class Task1 {
    public static void main(String[] args) throws Exception {

        final ParameterTool params = ParameterTool.fromArgs(args);
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        String sampleDir = params.getRequired("sample-dir");

        String experimentPath = params.getRequired("experiment-path");


        DataSet<Tuple2<String, Integer>> sample =
                env.readCsvFile(sampleDir)
                        .ignoreFirstLine()
                        .includeFields("11100000000000000")
                        .types(String.class, Integer.class, Integer.class)
                        .filter( tuple -> {
                            if(tuple.f1>=0 && tuple.f1<=150000 && tuple.f2>=0 && tuple.f2<=150000){
                                return true;
                            }else{
                                return false;
                            }
                        })
                        .map( tuple -> new Tuple2<>(tuple.f0, 1));

        DataSet<Tuple2<String, String>> experiment =
                env.readCsvFile(experimentPath)
                        .ignoreFirstLine()
                        .includeFields("10000001")
                        .types(String.class, String.class)
                        .flatMap( (tuple, out) -> {
                            String[] splits = tuple.f1.split(";");
                            for (String s: splits){
                                out.collect(new Tuple2<>(tuple.f0, s.trim()));
                            }
                        });

        DataSet<Tuple2<Integer, String>>joinedData =
                sample.join(experiment)
                .where(0)
                .equalTo(0)
                .projectFirst(1)
                .projectSecond(1);

        DataSet<Tuple2<Integer, String>>result = joinedData.groupBy(1)
                .sum(0);

        DataSet<Tuple2<String, Integer>> finedResult =
                result.map(tuple -> new Tuple2<>(tuple.f1, tuple.f0))
                .sortPartition(1, Order.DESCENDING).setParallelism(1)
                .sortPartition(0, Order.ASCENDING).setParallelism(1);


        // End the program by writing the output!
        if(params.has("output")) {
            finedResult.writeAsCsv(params.get("output"), "\n", "\t", FileSystem.WriteMode.OVERWRITE);
            env.execute();
        } else {
            // Always limit direct printing
            // as it requires pooling all resources into the driver.
            System.err.println("No output location specified; printing first 100.");
            finedResult.first(100).print();
        }
    }
}
