package assign2;

/**
 * Created by MushrChun on 20/5/17.
 */

public class CenterPoint extends Point {

    public int id;

    public CenterPoint() {}



    public CenterPoint(int id, Point p) {
        super(p.values);
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " " + super.toString();
    }


}