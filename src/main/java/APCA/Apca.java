package APCA;/*
 * Copyright 2015 Octavian Hasna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implements the Adaptive Piecewise Constant Approximation (APCA) algorithm.
 * <p>
 * Reference:
 * Chakrabarti K., Keogh E., Mehrotra S., Pazzani M. (2002)
 * <i>Locally Adaptive Dimensionality Reduction for Indexing Large Time Series Databases</i>
 * </p>
 *
 * @since 1.0
 */
public class Apca implements GenericTransformer<double[], NewMeanLastPair[]> {
    private static final long serialVersionUID = 5071554004881637993L;
    private final int segments;
    private final boolean approximateError;

    /**
     * Creates a new instance of this class with approximation enabled.
     *
     * @param segments the number of segments
     * @throws NumberIsTooSmallException if segments lower than 1
     */
    public Apca(int segments) {
        this(segments, true);
    }

    /**
     * Creates a new instance of this class with a flag for using approximation.
     *
     * @param segments         the number of segments
     * @param approximateError compute the error of unified segments using approximation
     * @throws NumberIsTooSmallException if segments lower than 1
     */
    public Apca(int segments, boolean approximateError) {
        if (segments < 1) {
            throw new NumberIsTooSmallException(segments, 1, true);
        }

        this.segments = segments;
        this.approximateError = approximateError;
    }

    @Override
    public NewMeanLastPair[] transform(double[] values) {
        int length = values.length;
        if (length < 2 * segments) {
            throw new ArrayLengthIsTooSmallException(length, 2 * segments, true);
        }
        int numberOfSegments = length / 2;

        // create segments with two values
        ApcaSegment first = createSegments(values, length);

        if (numberOfSegments > segments) {
            // compute error by unifying current segment with the next segment
            TreeSet<ApcaSegment> set = createSegmentsSet(values, first);

            // unify concurrent segments with minimum error
            while (numberOfSegments > segments) {
                ApcaSegment minSegment = set.pollFirst();
                minSegment.mean = getUnifiedMean(minSegment, minSegment.next);
                minSegment.error = getUnifiedError(minSegment, minSegment.next, values, minSegment.mean);
                minSegment.end = minSegment.next.end;

                deleteSubsequentSegment(minSegment, set);

                if (minSegment.next != null) {
                    double mean = getUnifiedMean(minSegment, minSegment.next);
                    minSegment.errorWithNext = getUnifiedError(minSegment, minSegment.next, values, mean);
                    set.add(minSegment);
                }

                if (minSegment.prev != null) {
                    set.remove(minSegment.prev);

                    double mean = getUnifiedMean(minSegment.prev, minSegment);
                    minSegment.prev.errorWithNext = getUnifiedError(minSegment.prev, minSegment, values, mean);

                    set.add(minSegment.prev);
                }


                numberOfSegments--;
            }
        }

        return getMeanLastPairs(first, numberOfSegments);
    }




    public ApcaSegment transformSegment(double[] values) {
        int length = values.length;
        if (length < 2 * segments) {
            throw new ArrayLengthIsTooSmallException(length, 2 * segments, true);
        }
        int numberOfSegments = length / 2;

        // create segments with two values
        ApcaSegment first = createSegments(values, length);

        if (numberOfSegments > segments) {
            // compute error by unifying current segment with the next segment
            TreeSet<ApcaSegment> set = createSegmentsSet(values, first);


            // unify concurrent segments with minimum error
            while (numberOfSegments > segments) {
                ApcaSegment minSegment = set.pollFirst();
                minSegment.mean = getUnifiedMean(minSegment, minSegment.next);
                minSegment.error = getUnifiedError(minSegment, minSegment.next, values, minSegment.mean);
                minSegment.end = minSegment.next.end;

                deleteSubsequentSegment(minSegment, set);

                if (minSegment.next != null) {
                    double mean = getUnifiedMean(minSegment, minSegment.next);
                    minSegment.errorWithNext = getUnifiedError(minSegment, minSegment.next, values, mean);
                    set.add(minSegment);
                }

                if (minSegment.prev != null) {
                    set.remove(minSegment.prev);

                    double mean = getUnifiedMean(minSegment.prev, minSegment);
                    minSegment.prev.errorWithNext = getUnifiedError(minSegment.prev, minSegment, values, mean);

                    set.add(minSegment.prev);
                }


                numberOfSegments--;
            }
        }

        return first;
    }


    private ApcaSegment createSegments(double[] values, int length) {
        ApcaSegment first = null, last = null;
        for (int i = 0; i < length - 1; i += 2) {
            double mean = (values[i] + values[i + 1]) / 2;
            ApcaSegment segment = new ApcaSegment(i, i + 2, mean, 2 * FastMath.abs(values[i] - mean));
            if (first == null) {
                first = segment;
                last = first;
            } else {
                last.next = segment;
                segment.prev = last;
                last = last.next;
            }
        }
        return first;
    }

    /**
     * 将当前segment和下一个segment合并，并且从小到大排序。
     * @param values
     * @param first
     * @return
     */
    private TreeSet<ApcaSegment> createSegmentsSet(double[] values, ApcaSegment first) {
        TreeSet<ApcaSegment> map = new TreeSet<ApcaSegment>(new Comparator<ApcaSegment>() {
            @Override
            public int compare(ApcaSegment s1, ApcaSegment s2) {
                return Precision.compareTo(s1.errorWithNext, s2.errorWithNext, TimeSeriesPrecision.EPSILON);
            }
        });

        ApcaSegment current = first;
        while (current.next != null) {
            double mean = getUnifiedMean(current, current.next);
            current.errorWithNext = getUnifiedError(current, current.next, values, mean);
            map.add(current);
            current = current.next;
        }
        return map;
    }

    private void deleteSubsequentSegment(ApcaSegment segment, Set<ApcaSegment> set) {
        ApcaSegment toBeDeleted = segment.next;
        segment.next = toBeDeleted.next;
        if (toBeDeleted.next != null) {
            toBeDeleted.next.prev = segment;
        }
        set.remove(toBeDeleted);
    }

    private NewMeanLastPair[] getMeanLastPairs(ApcaSegment segments, int numberOfSegments) {
        NewMeanLastPair[] result = new NewMeanLastPair[numberOfSegments];
        int i = 0;
        while (segments != null) {
            result[i] = new NewMeanLastPair(segments.mean, segments.end);
            segments = segments.next;
            i++;
        }
        return result;
    }

    private double getUnifiedApproximatedError(ApcaSegment first, ApcaSegment second, double mean) {
        return first.error + second.error + 2 * FastMath.abs(first.mean - mean) * (first.end - first.start);
    }

    private double getUnifiedError(ApcaSegment first, ApcaSegment second, double[] values, double mean) {
        if (Precision.equals(mean, first.mean, TimeSeriesPrecision.EPSILON)) {
            return first.error + second.error;
        }

        if (approximateError) {
            return getUnifiedApproximatedError(first, second, mean);
        }

        double error = 0.0;
        for (int i = first.start; i < second.end; i++) {
            error += FastMath.abs(values[i] - mean);
        }
        return error;
    }

    private double getUnifiedMean(ApcaSegment first, ApcaSegment second) {
        return (first.mean * (first.end - first.start) + second.mean * (second.end - second.start))
                / (second.end - first.start);
    }

    public ArrayList<ArrayList<Double>> ComputeMaxandMin(double[] value,ApcaSegment segmentItem) {
        ArrayList<ArrayList<Double>> listMaxandMin=new ArrayList<ArrayList<Double>>();
        ArrayList<Double> listMax=new ArrayList<Double>();
        ArrayList<Double> listMin=new ArrayList<>();
        int start=0;
        int end=0;
        double Max=0;
        double Min=0;
        while (segmentItem.next!=null)
        {
            start=segmentItem.start;
            end=segmentItem.end;
            for(int i=start;i<end;i++)
            {
                    Max=ComputeMax(value[start],Max);
                    Min=ComputeMin(value[start],Min);
            }
            listMax.add(Max);
            listMax.add(Min);
        }
        listMaxandMin.add(listMax);
        listMaxandMin.add(listMin);
        return listMaxandMin;
    }

    double  ComputeMax(double value,double max)
    {
        return value> max ? value:max;
    }

    double  ComputeMin(double value,double min)
    {
        return value< min ? value:min;
    }


}
