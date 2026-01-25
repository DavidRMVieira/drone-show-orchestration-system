package lapr4.integrations.dronelanguageplugin.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class DroneLanguageVersion implements ValueObject, Comparable<DroneLanguageVersion> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String droneLanguageVersion;

    public DroneLanguageVersion(String droneLanguageVersion) {
        Preconditions.nonEmpty(droneLanguageVersion, "Version should neither be null nor empty");

        this.droneLanguageVersion = droneLanguageVersion;
    }

    protected DroneLanguageVersion() {
        // for ORM only
    }

    public static DroneLanguageVersion valueOf(final String version) {
        return new DroneLanguageVersion(version);
    }

    @Override
    public String toString() {
        return droneLanguageVersion;
    }

    @Override
    public int compareTo(final DroneLanguageVersion other) {
        return droneLanguageVersion.compareTo(other.droneLanguageVersion);
    }
}