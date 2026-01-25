package lapr4.showproposalmanagement;

import lapr4.showproposalmanagement.domain.VideoFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoFileTest {

    private static final String VALID_HTTP_URL = "http://example.com/video.mp4";
    private static final String VALID_HTTPS_URL = "https://example.com/video.mp4";
    private static final String INVALID_URL_NO_PROTOCOL = "example.com/video.mp4";
    private static final String INVALID_URL_WITH_SPACE = "http://example.com/video with space.mp4";
    private static final String EMPTY_URL = "";
    private static final String NULL_URL = null;

    @Test
    void ensureValidHttpUrlIsAccepted() {
        final var video = new VideoFile(VALID_HTTP_URL);
        assertNotNull(video);
        assertEquals(VALID_HTTP_URL, video.toString());
    }

    @Test
    void ensureValidHttpsUrlIsAccepted() {
        final var video = new VideoFile(VALID_HTTPS_URL);
        assertNotNull(video);
        assertEquals(VALID_HTTPS_URL, video.toString());
    }

    @Test
    void ensureInvalidUrlsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new VideoFile(INVALID_URL_NO_PROTOCOL));
        assertThrows(IllegalArgumentException.class, () -> new VideoFile(INVALID_URL_WITH_SPACE));
        assertThrows(IllegalArgumentException.class, () -> new VideoFile(EMPTY_URL));
        assertThrows(IllegalArgumentException.class, () -> new VideoFile(NULL_URL));
    }

    @Test
    void ensureValueOfCreatesEquivalentInstance() {
        final var video1 = new VideoFile(VALID_HTTPS_URL);
        final var video2 = VideoFile.valueOf(VALID_HTTPS_URL);
        assertEquals(video1, video2);
    }

    @Test
    void ensureToStringReturnsCorrectValue() {
        final var video = new VideoFile(VALID_HTTP_URL);
        assertEquals(VALID_HTTP_URL, video.toString());
    }
}
