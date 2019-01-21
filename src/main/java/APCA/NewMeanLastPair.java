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


import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;


/**
 * @since 1.0
 */
public class NewMeanLastPair extends Pair<Double, Integer> {

    /**
     * Create an entry of type (mean, last).
     *
     * @param mean the mean value for the segment
     * @param last the position from the last element of the segment
     */
    public NewMeanLastPair(double mean, int last) {
        super(mean, last);
    }

    public double getMean() {
        return getFirst();
    }

    public int getLast() {
        return getSecond();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewMeanLastPair)) {
            return false;
        } else {
            NewMeanLastPair mlp = (NewMeanLastPair) o;
            return Precision.equals(getFirst(), mlp.getMean(), TimeSeriesPrecision.EPSILON) &&
                    getSecond() == mlp.getLast();
        }
    }
}