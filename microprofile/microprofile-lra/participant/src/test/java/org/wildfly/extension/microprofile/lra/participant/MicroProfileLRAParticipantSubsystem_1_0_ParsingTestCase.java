/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2023, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.extension.microprofile.lra.participant;

import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.as.subsystem.test.KernelServicesBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class MicroProfileLRAParticipantSubsystem_1_0_ParsingTestCase extends AbstractSubsystemBaseTest {

    public MicroProfileLRAParticipantSubsystem_1_0_ParsingTestCase() {
        super(MicroProfileLRAParticipantExtension.SUBSYSTEM_NAME, new MicroProfileLRAParticipantExtension());
    }

    @Override
    protected String getSubsystemXml() throws IOException {
        return readResource("lra_participant_subsystem_1_0.xml");
    }

    @Override
    protected String getSubsystemXsdPath() {
        return "schema/wildfly-microprofile-lra-participant_1_0.xsd";
    }

    @Test
    public void testRuntime() throws Exception {
        KernelServicesBuilder builder = createKernelServicesBuilder(createAdditionalInitialization())
            .setSubsystemXmlResource("lra_participant_subsystem_1_0.xml");
        KernelServices mainServices = builder.build();
        if (!mainServices.isSuccessfulBoot()) {
            Assert.fail(mainServices.getBootError().toString());
        }

        ModelNode model = mainServices.readWholeModel();
        ModelNode subsystem = model.get("subsystem", "microprofile-lra-participant");
        ModelNode urlAttribute = subsystem.get("url");

        Assert.assertSame(urlAttribute.getType(), ModelType.EXPRESSION);
        Assert.assertEquals("http://some-different-url:8080/lra-coordinator/lra-coordinator",
            urlAttribute.asExpression().resolveString());
    }

    @Test
    public void testRuntimeOverrideExpressions() throws Exception {
        System.setProperty("lra.coordinator.url", "overridden-expression-value");
        KernelServicesBuilder builder = createKernelServicesBuilder(createAdditionalInitialization())
            .setSubsystemXmlResource("lra_participant_subsystem_1_0.xml");
        KernelServices mainServices = builder.build();
        if (!mainServices.isSuccessfulBoot()) {
            Assert.fail(mainServices.getBootError().toString());
        }

        ModelNode model = mainServices.readWholeModel();
        ModelNode subsystem = model.get("subsystem", "microprofile-lra-participant");
        ModelNode urlAttribute = subsystem.get("url");

        Assert.assertSame(urlAttribute.getType(), ModelType.EXPRESSION);
        Assert.assertEquals("overridden-expression-value",
            urlAttribute.asExpression().resolveString());

        System.clearProperty("lra.coordinator.url");
    }
}
