package assign2;

import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.SampleInCoordinator;
import org.apache.flink.api.java.functions.SampleInPartition;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.DataSetUtils;
import org.apache.flink.api.java.utils.ParameterTool;

import java.util.ArrayList;


/**
 * Created by MushrChun on 20/5/17.
 */
public class RandomUtil {

    public static DataSet<CenterPoint> sample(int k, DataSet<Point> sample){


        DataSet<Point> sampleResult  = DataSetUtils.sampleWithSize(sample, false, k);
        DataSet<CenterPoint> zipResult = DataSetUtils
                .zipWithIndex(sampleResult)
                .map(tuple -> {
                    Point p = tuple.f1;
                    Integer index = tuple.f0.intValue();
                    return new CenterPoint(index, tuple.f1);
                });

        return zipResult;

    }

}
