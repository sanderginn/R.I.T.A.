/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.modules.rita.integtests.tests;

import java.math.BigInteger;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.wrapper.InvalidException;
import org.apache.isis.applib.services.xactn.TransactionService;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.car.CarRepository;
import domainapp.modules.rita.dom.driver.Driver;
import domainapp.modules.rita.dom.driver.DriverRepository;
import domainapp.modules.rita.fixture.car.CarForPeugeotAndJohnAndJan;
import domainapp.modules.rita.fixture.driver.DriverForJanModaal;
import domainapp.modules.rita.fixture.driver.DriverForJohnDoe;
import domainapp.modules.rita.fixture.teardown.RitaModuleTearDown;
import domainapp.modules.rita.integtests.RitaModuleIntegTestAbstract;
import static org.assertj.core.api.Assertions.assertThat;

public class Ride_IntegTest extends RitaModuleIntegTestAbstract {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    TransactionService transactionService;

    Car car;
    Driver driverForJanModaal;

    @Before
    public void setUp() throws Exception {
        // given
        fixtureScripts.runFixtureScript(new RitaModuleTearDown(), null);
        fixtureScripts.runFixtureScript(new CarForPeugeotAndJohnAndJan(), null);
        transactionService.nextTransaction();

        car = carRepository.findByLicensePlate(CarForPeugeotAndJohnAndJan.LICENSE_PLATE);
        driverForJanModaal = driverRepository.findByLastName(DriverForJanModaal.LAST_NAME).get(0);

        assertThat(car).isNotNull();
        assertThat(car.getDrivers()).hasSize(2);
        assertThat(car.getDrivers())
                .extracting(Driver::getLastName)
                .containsExactlyInAnyOrder(DriverForJohnDoe.LAST_NAME, DriverForJanModaal.LAST_NAME);

        assertThat(driverForJanModaal).isNotNull();
    }

    public static class AddRide extends Ride_IntegTest {

        @Test
        public void happyCase() throws Exception {
            // when
            wrap(car).addRide("Commute to work", new LocalDate(2017, 1, 1), BigInteger.valueOf(1100), driverForJanModaal);
            transactionService.nextTransaction();

            // then
            assertThat(car.getMileage()).isEqualTo(BigInteger.valueOf(1100));
            assertThat(car.getRides().first().getDistanceTraveled()).isEqualTo(BigInteger.valueOf(100));
        }

        @Test
        public void sadCase_newMileageLowerThanCurrent() throws Exception {
            // expect
            expectedExceptions.expect(InvalidException.class);
            expectedExceptions.expectMessage("New mileage must be more than current mileage (1000)");

            // when
            wrap(car).addRide("Negative mileage?!", new LocalDate(2017, 1, 1), BigInteger.valueOf(900), driverForJanModaal);
        }

    }

    @Inject
    CarRepository carRepository;

    @Inject
    DriverRepository driverRepository;
}