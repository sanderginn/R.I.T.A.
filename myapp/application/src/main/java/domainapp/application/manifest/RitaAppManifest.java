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
package domainapp.application.manifest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.isis.applib.AppManifest;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.application.fixture.RitaApplicationModuleFixtureSubmodule;
import domainapp.application.services.DomainAppApplicationModuleServicesSubmodule;
import domainapp.modules.rita.dom.RitaModuleDomSubmodule;

/**
 * Bootstrap the application.
 */
public class RitaAppManifest implements AppManifest {

    @Override
    public List<Class<?>> getModules() {
        List<Class<?>> modules = Lists.newArrayList();
        modules.addAll(Arrays.asList(
                RitaModuleDomSubmodule.class,
                RitaApplicationModuleFixtureSubmodule.class,
                DomainAppApplicationModuleServicesSubmodule.class
        ));

        return modules;
    }

    @Override
    public List<Class<?>> getAdditionalServices() {
        return null;
    }

    /**
     * Use shiro for authentication.
     */
    @Override
    public String getAuthenticationMechanism() {
        return "shiro";
    }

    /**
     * Use shiro for authorization.
     */
    @Override
    public String getAuthorizationMechanism() {
        return "shiro";
    }

    /**
     * No fixtures.
     */
    @Override
    public List<Class<? extends FixtureScript>> getFixtures() {
        return null;
    }

    /**
     * No overrides.
     */
    @Override
    public Map<String, String> getConfigurationProperties() {
        final Map<String, String> props = Maps.newHashMap();

//        loadPropsInto(props, "isis.properties");

        return props;
    }

    static void loadPropsInto(final Map<String, String> props, final String propertiesFile) {
        final Properties properties = new Properties();
        try {
            try (final InputStream stream =
                    RitaAppManifest.class.getResourceAsStream(propertiesFile)) {
                properties.load(stream);
                for (Object key : properties.keySet()) {
                    final Object value = properties.get(key);
                    if (key instanceof String && value instanceof String) {
                        props.put((String) key, (String) value);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Failed to load '%s' file ", propertiesFile), e);
        }
    }

    protected static Map<String, String> withInstallFixtures(Map<String, String> props) {
        props.put("isis.persistor.datanucleus.install-fixtures", "true");

        loadPropsInto(props, "isis.properties");

        return props;
    }

}