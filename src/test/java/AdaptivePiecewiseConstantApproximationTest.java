
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.representation.AdaptivePiecewiseConstantApproximation;
import ro.hasna.ts.math.type.MeanLastPair;
import ro.hasna.ts.math.util.TimeSeriesPrecision;


public class AdaptivePiecewiseConstantApproximationTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructor() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");
        new AdaptivePiecewiseConstantApproximation(0);
    }

    @Test
    public void testTransformMoreSegmentsThanValues() throws Exception {
        thrown.expect(ArrayLengthIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (8)");

        AdaptivePiecewiseConstantApproximation apca = new AdaptivePiecewiseConstantApproximation(4);
        double[] v = {1, 2, 3};

        apca.transform(v);
    }

    @Test
    public void testTransform() throws Exception {
        AdaptivePiecewiseConstantApproximation apca = new AdaptivePiecewiseConstantApproximation(3);
        double[] v = {1, 1, 4, 5, 1, 0, 1, 2, 1};

        MeanLastPair[] result = apca.transform(v);

        Assert.assertEquals(1, result[0].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(2, result[0].getLast());
        Assert.assertEquals(4.5, result[1].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(4, result[1].getLast());
        Assert.assertEquals(1, result[2].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(8, result[2].getLast());
    }

    @Test
    public void testTransformWithoutApproximation() throws Exception {
        AdaptivePiecewiseConstantApproximation apca = new AdaptivePiecewiseConstantApproximation(3, false);
        double[] v = {1, 1, 4, 5, 1, 0, 1, 2, 1};

        MeanLastPair[] result = apca.transform(v);

        Assert.assertEquals(1, result[0].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(2, result[0].getLast());
        Assert.assertEquals(4.5, result[1].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(4, result[1].getLast());
        Assert.assertEquals(1, result[2].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(8, result[2].getLast());
    }
}
