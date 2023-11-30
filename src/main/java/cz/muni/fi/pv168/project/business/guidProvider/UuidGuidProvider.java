package cz.muni.fi.pv168.project.business.guidProvider;

import java.util.UUID;

public class UuidGuidProvider implements GuidProvider {

    @Override
    public String newGuid() {
        return UUID.randomUUID().toString();
    }

    public static String newGuidStatic() {
        return UUID.randomUUID().toString();
    }
}
