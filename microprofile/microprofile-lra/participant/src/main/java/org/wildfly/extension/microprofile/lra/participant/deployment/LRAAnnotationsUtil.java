package org.wildfly.extension.microprofile.lra.participant.deployment;

import org.eclipse.microprofile.lra.annotation.AfterLRA;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.Forget;
import org.eclipse.microprofile.lra.annotation.Status;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.eclipse.microprofile.lra.annotation.ws.rs.Leave;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.jandex.DotName;

public class LRAAnnotationsUtil {

    private static final Class[] LRA_ANNOTATIONS = {
        LRA.class,
        Complete.class,
        Compensate.class,
        Status.class,
        Forget.class,
        Leave.class,
        AfterLRA.class
    };


    public static boolean isLRADeployment(DeploymentUnit deploymentUnit) {
        final CompositeIndex compositeIndex = deploymentUnit.getAttachment(Attachments.COMPOSITE_ANNOTATION_INDEX);
        if (compositeIndex == null) {
            return false;
        }

        return isLRAAnnotationsPresent(compositeIndex);
    }

    private static boolean isLRAAnnotationsPresent(CompositeIndex compositeIndex) {
        for (Class<?> annotation : LRA_ANNOTATIONS) {
            if (compositeIndex.getAnnotations(DotName.createSimple(annotation.getName())).size() > 0) {
                return true;
            }
        }
        return false;
    }
}
