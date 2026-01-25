package lapr4.integrations.proposaltemplate;

import lapr4.integrations.proposaltemplate.domain.ProposalTemplate;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplateVersion;
import lapr4.integrations.sharedkernel.domain.FQClassName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProposalTemplateTest {

    private static final ProposalTemplateVersion VALID_VERSION = ProposalTemplateVersion.valueOf("1.0");
    private static final FQClassName VALID_CLASS_NAME = FQClassName.valueOf("lapr4.integrations.plugins.proposal.ProposalTemplateFormatImporter");
    private static final ProposalTemplateVersion OTHER_VERSION = ProposalTemplateVersion.valueOf("2.0");
    private static final FQClassName OTHER_CLASS_NAME = FQClassName.valueOf("lapr4.other.domain.OtherClass");

    @Test
    void ensureValidProposalTemplateIsCreated() {
        final var template = new ProposalTemplate(VALID_VERSION, VALID_CLASS_NAME);
        assertNotNull(template);
        assertEquals(VALID_VERSION, template.identity());
        assertEquals(VALID_CLASS_NAME, template.className());
    }

    @Test
    void ensureNullParametersAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProposalTemplate(null, VALID_CLASS_NAME));
        assertThrows(IllegalArgumentException.class,
                () -> new ProposalTemplate(VALID_VERSION, null));
    }

    @Test
    void ensureSameAsWorksCorrectly() {
        final var template1 = new ProposalTemplate(VALID_VERSION, VALID_CLASS_NAME);
        final var template2 = new ProposalTemplate(OTHER_VERSION, OTHER_CLASS_NAME);

        assertTrue(template1.sameAs(template1));
        assertFalse(template1.sameAs(template2));
        assertFalse(template1.sameAs(null));
        assertFalse(template1.sameAs("not a template"));
    }

    @Test
    void ensureToStringContainsRelevantInfo() {
        final var template = new ProposalTemplate(VALID_VERSION, VALID_CLASS_NAME);
        final var toString = template.toString();

        assertTrue(toString.contains(VALID_VERSION.toString()));
        assertTrue(toString.contains(VALID_CLASS_NAME.toString()));
    }

    @Test
    void ensureIdentityReturnsCorrectVersion() {
        final var template = new ProposalTemplate(VALID_VERSION, VALID_CLASS_NAME);
        assertEquals(VALID_VERSION, template.identity());
    }

    @Test
    void ensureHashCodeIsConsistent() {
        final var template = new ProposalTemplate(VALID_VERSION, VALID_CLASS_NAME);
        final var hashCode1 = template.hashCode();
        final var hashCode2 = template.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void ensureClassNameIsCorrectlyReturned() {
        final var template = new ProposalTemplate(VALID_VERSION, VALID_CLASS_NAME);
        assertEquals(VALID_CLASS_NAME, template.className());
    }
}
