package lapr4.figurecatalogue;

import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.CustomerState;
import lapr4.customermanagement.domain.CustomerType;
import lapr4.customermanagement.util.CustomerTestUtil;
import lapr4.figurecatalogue.domain.*;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {

    private static final Description VALID_DESCRIPTION = Description.valueOf("A valid figure description");
    private static final DSL VALID_DSL = new DSL(Description.valueOf("dsl"), DSLVersion.valueOf("1.1"));
    private static final FigureCode VALID_CODE = FigureCode.valueOf("FigA1");
    private static final FigureVersion VALID_VERSION = FigureVersion.valueOf("1.1");
    private static final Customer VALID_CUSTOMER = CustomerTestUtil.dummyCustomer("AA111111111", "Igor", "Coutinho", "Rua Porto", "Porto", "4444-111", "Portugal", CustomerState.CREATED, CustomerType.REGULAR);
    private static final FigureCategory VALID_CATEGORY = new FigureCategory(Designation.valueOf("Categoria1"), Description.valueOf("categoria de desenhos"));
    private static final Set<String> VALID_KEYWORDS = Set.of("keyword1", "keyword2");
    private static final FigureType VALID_TYPE = FigureType.STATIC;

    @Test
    void ensureValidFigureIsCreated() {
        final var figure = new Figure(VALID_DESCRIPTION, VALID_DSL, VALID_CODE, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS);
        assertNotNull(figure);
        assertEquals(VALID_DESCRIPTION.toString(), figure.toDTO().getDescription().toString());
        assertEquals(VALID_CODE.toString(), figure.toDTO().getCode().toString());
        assertEquals(VALID_VERSION.toString(), figure.toDTO().getFigureVersion().toString());
        assertEquals(VALID_CUSTOMER.identity().toString(), figure.toDTO().getCustomer().toString());
        assertEquals(VALID_CATEGORY.identity().toString(), figure.toDTO().getFigureCategory().toString());
        assertEquals(VALID_KEYWORDS.toString(), figure.toDTO().getKeywords().toString());
        assertEquals(VALID_TYPE.toString(), figure.toDTO().getType().toString());
        assertTrue(figure.isExclusive());
    }

    @Test
    void ensureIdentityReturnsCode() {
        final var figure = new Figure(VALID_DESCRIPTION, VALID_DSL, VALID_CODE, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS);
        assertEquals(VALID_CODE, figure.identity());
    }

    @Test
    void ensureFigureWithoutClientIsNotExclusive() {
        final var figureWithoutClient = new Figure(
                VALID_DESCRIPTION,
                VALID_DSL,
                VALID_CODE,
                VALID_TYPE,
                VALID_VERSION,
                null,
                VALID_CATEGORY,
                VALID_KEYWORDS
        );
        assertFalse(figureWithoutClient.isExclusive());
    }


    @Test
    void ensureAllParametersAreRequired() {
        assertThrows(IllegalArgumentException.class, () -> new Figure(null, VALID_DSL, VALID_CODE, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS));
        assertThrows(IllegalArgumentException.class, () -> new Figure(VALID_DESCRIPTION, null, VALID_CODE, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS));
        assertThrows(IllegalArgumentException.class, () -> new Figure(VALID_DESCRIPTION, VALID_DSL, null, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS));
        assertThrows(IllegalArgumentException.class, () -> new Figure(VALID_DESCRIPTION, VALID_DSL, VALID_CODE, null, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS));
        assertThrows(IllegalArgumentException.class, () -> new Figure(VALID_DESCRIPTION, VALID_DSL, VALID_CODE, VALID_TYPE, null, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS));
        assertThrows(IllegalArgumentException.class, () -> new Figure(VALID_DESCRIPTION, VALID_DSL, VALID_CODE, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, null, VALID_KEYWORDS));
        assertThrows(IllegalArgumentException.class, () -> new Figure(VALID_DESCRIPTION, VALID_DSL, VALID_CODE, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, null));
    }

    @Test
    void ensureFigureCanBeDecommissioned() {
        final var figure = new Figure(VALID_DESCRIPTION, VALID_DSL, VALID_CODE, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS);
        figure.decommission();
        assertThrows(IllegalStateException.class, figure::decommission);
    }


    @Test
    void ensureInactiveFigureCannotBeDecommissioned() {
        final var figure = new Figure(VALID_DESCRIPTION, VALID_DSL, VALID_CODE, VALID_TYPE, VALID_VERSION, VALID_CUSTOMER, VALID_CATEGORY, VALID_KEYWORDS);
        figure.decommission();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            figure.decommission();
        });

        assertEquals("Cannot decommission an inactive figure", exception.getMessage());
    }
}