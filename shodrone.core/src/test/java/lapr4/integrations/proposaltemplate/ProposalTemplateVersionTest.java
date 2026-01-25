package lapr4.integrations.proposaltemplate;

import lapr4.integrations.proposaltemplate.domain.ProposalTemplateVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProposalTemplateVersionTest {

    private static final String VALID_VERSION = "1.0";
    private static final String ANOTHER_VALID_VERSION = "2.0";
    private static final String EMPTY_VERSION = "";
    private static final String NULL_VERSION = null;

    @Test
    void ensureValidProposalTemplateVersionIsCreated() {
        final var version = new ProposalTemplateVersion(VALID_VERSION);
        assertNotNull(version);
        assertEquals(VALID_VERSION, version.toString());
    }

    @Test
    void ensureVersionCannotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new ProposalTemplateVersion(NULL_VERSION));
        assertThrows(IllegalArgumentException.class, () -> new ProposalTemplateVersion(EMPTY_VERSION));
    }

    @Test
    void ensureValueOfCreatesEquivalentInstance() {
        final var version1 = new ProposalTemplateVersion(VALID_VERSION);
        final var version2 = ProposalTemplateVersion.valueOf(VALID_VERSION);
        assertEquals(version1, version2);
    }

    @Test
    void ensureCompareToWorksCorrectly() {
        final var version1 = new ProposalTemplateVersion(VALID_VERSION);
        final var version2 = new ProposalTemplateVersion(ANOTHER_VALID_VERSION);

        assertTrue(version1.compareTo(version2) < 0);
        assertTrue(version2.compareTo(version1) > 0);
        assertEquals(0, version1.compareTo(new ProposalTemplateVersion(VALID_VERSION)));
    }

    @Test
    void ensureToStringReturnsCorrectValue() {
        final var version = new ProposalTemplateVersion(VALID_VERSION);
        assertEquals(VALID_VERSION, version.toString());
    }
}
