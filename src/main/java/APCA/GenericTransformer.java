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


import java.io.Serializable;

/**
 * Interface for generic vector transformation.
 *
 * @since 1.0
 */
public interface GenericTransformer<I, O> extends Serializable {
    /**
     * Transform the input vector from type I into type O.
     *
     * @param input the input vector
     * @return the output vector
     */
    O transform(I input);
}
