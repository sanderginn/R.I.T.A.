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
package domainapp.application.fixture.scenarios;

import org.apache.isis.applib.fixturescripts.DiscoverableFixtureScript;

import domainapp.application.fixture.teardown.DomainAppTearDown;
import domainapp.modules.rita.fixture.expense.ExpensesForJanModaalAndPeugeot;
import domainapp.modules.rita.fixture.expense.ExpensesForJohnDoeAndPeugeot;
import domainapp.modules.rita.fixture.ride.RidesForJanModaalAndPeugeot;
import domainapp.modules.rita.fixture.ride.RidesForJohnDoeAndPeugeot;

public class RitaAppDemo extends DiscoverableFixtureScript {

    public RitaAppDemo() {
        this(null, "demo");
    }

    public RitaAppDemo(final String friendlyName, final String localName) {
        super(friendlyName, localName);
    }

    @Override
    protected void execute(final ExecutionContext ec) {

        // execute
        ec.executeChild(this, new DomainAppTearDown());
        ec.executeChild(this, new RidesForJohnDoeAndPeugeot());
        ec.executeChild(this, new RidesForJanModaalAndPeugeot());
        ec.executeChild(this, new ExpensesForJohnDoeAndPeugeot());
        ec.executeChild(this, new ExpensesForJanModaalAndPeugeot());
    }
}
