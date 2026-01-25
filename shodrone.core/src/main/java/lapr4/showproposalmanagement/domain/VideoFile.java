package lapr4.showproposalmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class VideoFile implements ValueObject {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String URL_FORMAT = "https?://[^\\s]+";

    private String file;

    public VideoFile(final String file) {
        Preconditions.nonNull(file, "Video file URL must not be null.");
        Preconditions.ensure(file.matches(URL_FORMAT), "Invalid video file URL format. Must start with http:// or https:// and contain no spaces.");

        this.file = file;
    }

    protected VideoFile() {
        // for ORM
    }

    public static VideoFile valueOf(final String file) {
        return new VideoFile(file);
    }

    @Override
    public String toString() {
        return file;
    }

}
