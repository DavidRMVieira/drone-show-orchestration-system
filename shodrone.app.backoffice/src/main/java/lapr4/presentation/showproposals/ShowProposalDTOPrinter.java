package lapr4.presentation.showproposals;

import eapli.framework.visitor.Visitor;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

public class ShowProposalDTOPrinter implements Visitor<ShowProposalDTO> {

    @Override
    public void visit(ShowProposalDTO visitee) {
        System.out.printf(
                "%-6s | %-20s | %-11s | %-9s | %-11s | %-6s | %-6s | %-20s | %-25s | %-30s%n",
                visitee.id(),
                formatDate(visitee.date()),
                visitee.duration(),
                visitee.numberOfDrones(),
                visitee.insuranceAmount(),
                visitee.latitudeLocation(),
                visitee.longitudeLocation(),
                safeString(visitee.state()),
                safeString(visitee.representativeEmail()),
                safeString(visitee.videoSimulation())
        );
    }

    public String header() {
        return String.format(
                "   %-6s | %-20s | %-11s | %-9s | %-11s | %-6s | %-6s | %-20s | %-25s | %-30s%n",
                "ID", "DATE", "DURATION", "DRONES", "INSURANCE", "LAT", "LNG",
                "STATE", "REP EMAIL", "VIDEO SIMULATION"
        );
    }

    private String safeString(String value) {
        return value == null ? "N/A" : value;
    }

    private String formatDate(java.util.Date date) {
        return date == null ? "N/A" : new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

}
