package lapr4.showproposalmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class Location implements ValueObject {

    @Serial
    private static final long serialVersionUID = 1L;

    private double latitude;
    private double longitude;

    public Location(final double latitude, final double longitude) {
        Preconditions.ensure(latitude >= -90 && latitude <= 90, "Latitude must be between -90 and 90.");
        Preconditions.ensure(longitude >= -180 && longitude <= 180, "Longitude must be between -180 and 180.");

        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Location() {
        // for ORM
    }

    public static Location valueOf (final double latitude, final double longitude) {
        return new Location(latitude, longitude);
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("latitude=%.6f, longitude=%.6f", latitude, longitude);
    }
}
