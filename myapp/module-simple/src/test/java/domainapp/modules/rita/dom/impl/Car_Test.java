/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package domainapp.modules.rita.dom.impl;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import domainapp.modules.rita.dom.car.Car;

public class Car_Test {

    @Before
    public void setUp() throws Exception {
    }

    public static class NewCar extends Car_Test {

        @Test
        public void happyCase() throws Exception {
            Car car = new Car("4TFL24", BigInteger.TEN);
            System.out.println(car);
        }
    }

}
