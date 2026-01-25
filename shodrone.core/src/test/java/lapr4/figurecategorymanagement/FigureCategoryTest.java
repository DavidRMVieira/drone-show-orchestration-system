package lapr4.figurecategorymanagement;

import lapr4.figurecategorymanagement.domain.FigureCategory;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.general.domain.model.Description;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the FigureCategory class.
 */
class FigureCategoryTest {

    private Designation validName;
    private Description validDescription;

    /**
     * Set up the test environment.
     * This method is called before each test case.
     */
    @BeforeEach
    void setUp() {
        validName = Designation.valueOf("Category1");
        validDescription = Description.valueOf("Description of Category 1");
    }

    /**
     * Test the creation of a FigureCategory object.
     * It should not be null and should have the correct name and description.
     */
    @Test
    void ensureValidFigureCategoryIsCreated() {
        FigureCategory category = new FigureCategory(validName, validDescription);
        assertNotNull(category);
        assertEquals(validName, category.identity());
        assertTrue(category.isActive());
    }

    /**
     * Test the creation of a FigureCategory object with null name or description.
     * It should throw an IllegalArgumentException.
     */
    @Test
    void ensureCreationFailsWithNullNameOrDescription() {
        assertThrows(IllegalArgumentException.class, () -> new FigureCategory(null, validDescription));
        assertThrows(IllegalArgumentException.class, () -> new FigureCategory(validName, null));
    }

    /**
     * Test the creation of a FigureCategory object with empty name or description.
     * It should throw an IllegalArgumentException.
     */
    @Test
    void ensureInactivateWorksAndThrowsIfAlreadyInactive() {
        FigureCategory category = new FigureCategory(validName, validDescription);
        assertTrue(category.isActive());

        category.inactivate();
        assertFalse(category.isActive());

        // inactivate again should throw
        assertThrows(IllegalStateException.class, category::inactivate);
    }

    /**
     * Test the inactivation of a FigureCategory object.
     * It should change the state to inactive and throw an exception if already inactive.
     */
    @Test
    void ensureActivateWorksAndThrowsIfAlreadyActive() {
        FigureCategory category = new FigureCategory(validName, validDescription);
        assertTrue(category.isActive());

        // activate when already active should throw
        assertThrows(IllegalStateException.class, category::activate);

        category.inactivate();
        assertFalse(category.isActive());

        category.activate();
        assertTrue(category.isActive());
    }

    /**
     * Test the inactivation of a FigureCategory object.
     * It should change the state to active and throw an exception if already active.
     */
    @Test
    void ensureChangeNameToWorks() {
        FigureCategory category = new FigureCategory(validName, validDescription);
        Designation newName = Designation.valueOf("NewName");

        category.changeNameTo(newName);
        assertEquals(newName, category.identity());
    }

    /**
     * Test the change of name to a new value.
     * It should update the name of the category.
     */
    @Test
    void ensureChangeDescriptionToWorks() {
        FigureCategory category = new FigureCategory(validName, validDescription);
        Description newDescription = Description.valueOf("New description");

        category.changeDescriptionTo(newDescription);
        // No getter for description, but we can check via toDTO
        assertEquals(newDescription.toString(), category.toDTO().getDescription());
    }

    /**
     * Test the change of description to a new value.
     * It should update the description of the category.
     */
    @Test
    void ensureChangeNameAndDescriptionThrowsOnNull() {
        FigureCategory category = new FigureCategory(validName, validDescription);

        assertThrows(IllegalArgumentException.class, () -> category.changeNameTo(null));
        assertThrows(IllegalArgumentException.class, () -> category.changeDescriptionTo(null));
    }

    /**
     * Test the change of name and description to null values.
     * It should throw an IllegalArgumentException.
     */
    @Test
    void ensureToDTOReturnsCorrectValues() {
        FigureCategory category = new FigureCategory(validName, validDescription);
        FigureCategoryDTO dto = category.toDTO();

        assertEquals(validName.toString(), dto.getName());
        assertEquals(validDescription.toString(), dto.getDescription());
        assertTrue(dto.getClass().equals(FigureCategoryDTO.class));
    }

    /**
     * Test the conversion to DTO.
     * It should return a FigureCategoryDTO object with the correct values.
     */
    @Test
    void ensureEqualsAndHashCodeWork() {
        FigureCategory cat1 = new FigureCategory(validName, validDescription);
        FigureCategory cat2 = new FigureCategory(validName, validDescription);

        assertEquals(cat1, cat2);
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }
}