package assign2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MushrChun on 20/5/17.
 */

public class Point implements Serializable {

    public ArrayList<Double> values;

    public Point() {}


    public Point(ArrayList<Double> values) {
        this.values = values;
    }

    public Point add(Point other) {
        for(int i = 0; i< values.size(); i++){
            this.values.set(i, this.values.get(i) + other.values.get(i));
        }
        return this;
    }

    public Point div(long val) {
        for(int i = 0; i< values.size(); i++){
            this.values.set(i, this.values.get(i) / val);
        }
        return this;
    }

    public double euclideanDistance(Point other) {
        Double result = 0.0;

        for(int i = 0; i< values.size(); i++){
            result += (this.values.get(i) - other.values.get(i)) * (this.values.get(i) - other.values.get(i)) ;
        }

        return Math.sqrt(result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.values.get(0));
        for(int i = 1; i< values.size(); i++){
            sb.append("\t");
            sb.append(this.values.get(i));
        }
        return sb.toString();
    }

}