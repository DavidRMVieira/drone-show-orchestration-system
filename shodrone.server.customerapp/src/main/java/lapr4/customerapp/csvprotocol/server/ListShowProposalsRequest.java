package lapr4.customerapp.csvprotocol.server;

import lapr4.showproposalmanagement.application.AnalyzeProposalController;
import lapr4.showproposalmanagement.application.GetShowInfoController;
import lapr4.showproposalmanagement.application.ListScheduledShowsController;
import lapr4.showproposalmanagement.dto.DroneInShowDTO;
import lapr4.showproposalmanagement.dto.FigureInShowDTO;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.util.Set;

/**
 * Abstract class for handling requests to list customer proposals.
 */
public abstract class ListShowProposalsRequest extends CustomerAppProtocolRequest {


    protected ListShowProposalsRequest(AnalyzeProposalController controller, String inputRequest) {
        super(controller, inputRequest);
    }

    protected ListShowProposalsRequest(GetShowInfoController controller, String inputRequest) {
        super(controller, inputRequest);
    }

    protected ListShowProposalsRequest(ListScheduledShowsController controller, String inputRequest) {
        super(controller, inputRequest);
    }


    protected String buildResponse(final Iterable<ShowProposalDTO> proposalsDTO) {
        final var sb = new StringBuilder();

        // header
        sb.append("\"ID\",,,\"DATE\",,,\"DURATION\",,,\"NUMBER OF DRONES\",,,\"INSURANCE AMOUNT\",," +
                "\"LATITUDE\",,,\"LONGITUDE\",,,\"STATE\",,,\"SHOW REQUEST ID\",,,\"REPRESENTATIVE EMAIL\",," +
                "\"DRONES IN SHOW\",,,\"FIGURES IN SHOW\",,,\"VIDEO SIMULATION\",,,\"DOCUMENT\",,,\"FEEDBACK\"\n");

        // result rows
        for (final ShowProposalDTO each : proposalsDTO) {
            sb.append("\"").append(each.id()).append("\",,,")
                    .append("\"").append(formatDate(each.date())).append("\",,,")
                    .append("\"").append(each.duration()).append("\",,,")
                    .append("\"").append(each.numberOfDrones()).append("\",,,")
                    .append("\"").append(each.insuranceAmount()).append("\",,,")
                    .append("\"").append(each.latitudeLocation()).append("\",,,")
                    .append("\"").append(each.longitudeLocation()).append("\",,,")
                    .append("\"").append(safeString(each.state())).append("\",,,")
                    .append("\"").append(each.showRequestId()).append("\",,,")
                    .append("\"").append(safeString(each.representativeEmail())).append("\",,,")
                    .append("\"").append(dronesToString(each.drones())).append("\",,,")
                    .append("\"").append(figuresToString(each.figures())).append("\",,,")
                    .append("\"").append(safeString(each.videoSimulation())).append("\",,,")
                    .append("\"").append(safeString(each.document())).append("\",,,")
                    .append("\"").append(safeString(each.feedback())).append("\"")
                    .append("\n");
        }

        // end of a message
        sb.append("\n");

        return sb.toString();
    }

    protected String dronesToString(Set<DroneInShowDTO> drones) {
        if (drones == null || drones.isEmpty()) return "N/A";
        return drones.stream()
                .map(d -> d.droneModelName() + "|" + d.quantity())
                .collect(java.util.stream.Collectors.joining("||"));
    }

    protected String figuresToString(Set<FigureInShowDTO> figures) {
        if (figures == null || figures.isEmpty()) return "N/A";
        return figures.stream()
                .map(f -> f.figureCode() + "|" + f.Xcoordinate() + "|" + f.Ycoordinate() + "|" + f.Zcoordinate())
                .collect(java.util.stream.Collectors.joining("||"));
    }

    protected String formatDate(java.util.Date date) {
        return date == null ? "N/A" : new java.text.SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    protected String safeString(String value) {
        if (value == null) {
            return "N/A";
        }
        // Replace newlines and carriage returns with placeholders to avoid CSV format issues
        return value.replace("\n", "[LNNN]").replace("\r", "[LRRR]");
    }
}
