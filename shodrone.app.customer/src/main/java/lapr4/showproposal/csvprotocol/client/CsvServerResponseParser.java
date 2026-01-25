package lapr4.showproposal.csvprotocol.client;

import lapr4.showproposalmanagement.dto.DroneInShowDTO;
import lapr4.showproposalmanagement.dto.FigureInShowDTO;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Set;

/**
 * Marshaler/ Unmarshal for the Customer App Server Csv Protocol. It is responsible for
 * marshaling the data to create a proper network message and unmarshal the
 * network message to the proper DTO class
 */
class CsvServerResponseParser {

    public Iterable<ShowProposalDTO> parseResponseMessageListCustomerShowProposals(final List<String> response)
            throws FailedRequestException {
        checkForErrorMessage(response);

        final List<ShowProposalDTO> ret = new ArrayList<>();

        response.remove(0); // removes header
        response.forEach(s -> ret.add(parseResponseMessageLineListCustomerShowProposals(s)));
        return ret;
    }

    private ShowProposalDTO parseResponseMessageLineListCustomerShowProposals(final String s) {
        // Split on triple comma
        final String[] tokens = s.split(",,,");

        // Robust: preenche campos em falta com "N/A"
        String[] fixedTokens = new String[15];
        for (int i = 0; i < fixedTokens.length; i++) {
            if (i < tokens.length) {
                fixedTokens[i] = tokens[i];
            } else {
                fixedTokens[i] = "\"N/A\"";
            }
        }

        try {
            long id = Long.parseLong(removeDoubleQuotes(fixedTokens[0]));
            Date date = parseDate(removeDoubleQuotes(fixedTokens[1]));
            int duration = Integer.parseInt(removeDoubleQuotes(fixedTokens[2]));
            int numberOfDrones = Integer.parseInt(removeDoubleQuotes(fixedTokens[3]));
            double insuranceAmount = Double.parseDouble(removeDoubleQuotes(fixedTokens[4]));
            double latitude = Double.parseDouble(removeDoubleQuotes(fixedTokens[5]));
            double longitude = Double.parseDouble(removeDoubleQuotes(fixedTokens[6]));
            String state = removeDoubleQuotes(fixedTokens[7]);
            long showRequestId = Long.parseLong(removeDoubleQuotes(fixedTokens[8]));
            String representativeEmail = removeDoubleQuotes(fixedTokens[9]);
            Set<DroneInShowDTO> drones = parseDrones(removeDoubleQuotes(fixedTokens[10]));
            Set<FigureInShowDTO> figures = parseFigures(removeDoubleQuotes(fixedTokens[11]));
            String videoSimulation = removeDoubleQuotes(fixedTokens[12]);
            String documentContent = removeDoubleQuotes(fixedTokens[13]).replace("[LNNN]", "\n").replace("[LRRR]", "\r");
            String documentLink = createDocumentFileAndGetLink(id, documentContent);
            String feedback = removeDoubleQuotes(fixedTokens[14]).replace("[LNNN]", "\n").replace("[LRRR]", "\r");

            return new ShowProposalDTO(
                    id,
                    date,
                    duration,
                    videoSimulation,
                    numberOfDrones,
                    insuranceAmount,
                    latitude,
                    longitude,
                    state,
                    showRequestId,
                    representativeEmail,
                    documentContent,
                    documentLink,
                    drones,
                    figures,
                    feedback
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse proposal line: " + s, e);
        }
    }

    /**
     * Cria um ficheiro localmente com o conteúdo do documento e retorna o path local.
     */
    private String createDocumentFileAndGetLink(long proposalId, String documentContent) {
        if (documentContent == null || documentContent.equals("N/A")) {
            return "N/A";
        }
        try {
            String folderPath = "files/generated_documents";
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(folderPath));
            String fileName = "document_" + proposalId + ".txt";
            java.nio.file.Path filePath = java.nio.file.Paths.get(folderPath, fileName);
            java.nio.file.Files.writeString(filePath, documentContent);
            return filePath.toString().replace("\\", "/");
        } catch (Exception e) {
            return "[ERROR_CREATING_DOCUMENT_FILE]";
        }
    }

    private Date parseDate(final String token) {
        if ("N/A".equals(token) || token.isEmpty()) {
            return null;
        }
        try {
            return new java.text.SimpleDateFormat("yyyy/MM/dd").parse(token);
        } catch (java.text.ParseException e) {
            throw new RuntimeException("Invalid date format: " + token, e);
        }
    }

    private Set<DroneInShowDTO> parseDrones(String token) {
        Set<DroneInShowDTO> drones = new java.util.HashSet<>();
        if (!token.equals("N/A") && !token.isEmpty()) {
            String[] droneTokens = token.split("\\|\\|");
            for (String droneStr : droneTokens) {
                String[] parts = droneStr.split("\\|");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    drones.add(new DroneInShowDTO(name, quantity));
                }
            }
        }
        return drones;
    }

    private Set<FigureInShowDTO> parseFigures(String token) {
        Set<FigureInShowDTO> figures = new java.util.HashSet<>();
        if (!token.equals("N/A") && !token.isEmpty()) {
            String[] figureTokens = token.split("\\|\\|");
            for (String figureStr : figureTokens) {
                String[] parts = figureStr.split("\\|");
                if (parts.length == 4) {
                    String code = parts[0].trim();
                    double x = Double.parseDouble(parts[1].trim());
                    double y = Double.parseDouble(parts[2].trim());
                    double z = Double.parseDouble(parts[3].trim());
                    figures.add(new FigureInShowDTO(code, x, y, z));
                }
            }
        }
        return figures;
    }


    public String parseResponseMessageAcceptProposal(final List<String> response)
            throws FailedRequestException {
        checkForErrorMessage(response);

        return removeDoubleQuotes(response.get(0));
    }

    public String parseResponseMessageRejectProposal(final List<String> response)
            throws FailedRequestException {
        checkForErrorMessage(response);

        return removeDoubleQuotes(response.get(0));
    }


    private String removeDoubleQuotes(final String token) {
        return token.replace("\"", "").trim();
    }

    private void checkForErrorMessage(final List<String> response) throws FailedRequestException {
        final String[] tokens = response.get(0).split(",");
        final String messageType = tokens[0];

        if (messageType.equals("SERVER_ERROR") || messageType.equals("BAD_REQUEST")
                || messageType.equals("UNKNOWN_REQUEST") || messageType.equals("ERROR_IN_REQUEST")) {
            throw new FailedRequestException(messageType + ":" + tokens[tokens.length - 1]);
        }
    }
}
