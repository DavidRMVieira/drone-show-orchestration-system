package lapr4.figurecatalogue.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.general.domain.model.Description;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.customermanagement.application.ListCustomerService;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.figurecatalogue.domain.*;
import lapr4.figurecatalogue.dto.FigureDTO;
import lapr4.figurecatalogue.repositories.Catalogue;
import lapr4.figurecategorymanagement.application.ListFigureCategoryService;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.integrations.dslplugin.application.ImportDSLService;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.io.IOException;
import java.util.Set;

@UseCaseController
public class AddFigureCatalogueController {

    private final Catalogue catalogueRepo;
    private final ListCustomerService customerSvc;
    private final ListFigureCategoryService categorySvc;
    private final ImportDSLService dslSvc;
    private final AuthorizationService authz;

    public AddFigureCatalogueController() {
        this.catalogueRepo = PersistenceContext.repositories().catalogue();
        this.customerSvc = new ListCustomerService();
        this.categorySvc = new ListFigureCategoryService();
        this.dslSvc = new ImportDSLService();
        this.authz = AuthzRegistry.authorizationService();
    }

    public FigureDTO addFigureToCatalogue(String descriptionString, String dslDescriptionFile, String dslVersionString, String code,
                                          String figureTypeString, String figureVersionString, CustomerDTO customerDTO, FigureCategoryDTO figureCategoryDTO,Set<String> keywords, boolean validationDSL) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);

        if (validationDSL) {
            dslSvc.validateDSL(dslDescriptionFile, dslVersionString);
        }

        final Description description = Description.valueOf(descriptionString);
        final FigureCode figureCode = FigureCode.valueOf(code);
        final FigureVersion figureVersion = FigureVersion.valueOf(figureVersionString);
        final FigureType figureType = FigureType.valueOf(figureTypeString.toUpperCase());
        final Description dslDescription = Description.valueOf(dslDescriptionFile);
        final DSLVersion dslVersion = DSLVersion.valueOf(dslVersionString);
        Customer customer = null;
        if (customerDTO != null) {
            customer = customerSvc.findCustomerByVatNumber(customerDTO.getVatNumber());
        }
        final FigureCategory figureCategory = categorySvc.findFigureCategoryByName(figureCategoryDTO.getName());
        final DSL dsl = new DSL(dslDescription, dslVersion);
        final Figure figure = new Figure(description, dsl, figureCode, figureType, figureVersion, customer, figureCategory, keywords);

        return catalogueRepo.save(figure).toDTO();
    }

    public Iterable<CustomerDTO> allCustomers() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);
        return customerSvc.allCustomers();
    }

    public Iterable<FigureCategoryDTO> allActiveCategories() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);
        return categorySvc.allActiveFigureCategories();
    }

}
