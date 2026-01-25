package lapr4.showproposalmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.figurecatalogue.application.FigureDTOParser;
import lapr4.figurecatalogue.domain.Figure;
import lapr4.figurecatalogue.domain.FigureCode;
import lapr4.figurecatalogue.dto.FigureDTO;
import lapr4.figurecatalogue.repositories.Catalogue;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showproposalmanagement.domain.FigureInShow;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.dto.FigureInShowDTO;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UseCaseController
public class AddFigureProposalController {

    private final AuthorizationService authz;
    private final Catalogue catalogue;
    private final ListShowProposalService proposalSvc;
    private final ShowProposalRepository showProposalRepository;

    public AddFigureProposalController() {
        this.authz = AuthzRegistry.authorizationService();
        this.catalogue = PersistenceContext.repositories().catalogue();
        this.proposalSvc = new ListShowProposalService();
        this.showProposalRepository = PersistenceContext.repositories().showProposals();
    }

    public void addFigureProposal(final ShowProposalDTO selectedProposalDTO, final FigureDTO selectedFigureDTO, final double coordinateX, final double coordinateY, final double coordinateZ) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowProposal showProposal = proposalSvc.findShowProposalByID(selectedProposalDTO.id());
        Figure figure = new FigureDTOParser(catalogue).valueOf(selectedFigureDTO);
        showProposal.addFigure(new FigureInShow(figure.identity().toString(), coordinateX, coordinateY, coordinateZ));
        showProposalRepository.save(showProposal);
    }

    public void configureFiguresProposal(final ShowProposalDTO selectedProposalDTO, final List<FigureInShowDTO> figureInShowDTOList) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowProposal showProposal = proposalSvc.findShowProposalByID(selectedProposalDTO.id());

        Set<FigureInShow> figuresToAdd = new HashSet<>();

        for (FigureInShowDTO dto : figureInShowDTOList) {
            Figure figure = catalogue.ofIdentity(FigureCode.valueOf(dto.figureCode()))
                    .orElseThrow(() -> new IllegalArgumentException("Unknown figure: " + dto.figureCode()));

            figuresToAdd.add(new FigureInShow(figure.identity().toString(),dto.Xcoordinate(), dto.Ycoordinate(), dto.Zcoordinate()));
        }

        showProposal.configureFigures(figuresToAdd);
        showProposalRepository.save(showProposal);
    }

    public Iterable<ShowProposalDTO> listShowProposals() {
        return proposalSvc.allShowProposals();
    }

    public Iterable<FigureDTO> listFigureModels() {
        Iterable<Figure> figures = catalogue.findAllActive();
        return FigureDTOParser.transformToDTO(figures);
    }

}

