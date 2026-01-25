package lapr4.integrations.plugins.proposaltemplate.version_1_0;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProposalTemplateImplListener extends ProposalTemplateBaseListener {

    private String language;
    private String representativeName;
    private String companyName;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private String vatNumber;
    private String showProposalNumber;
    private String date;
    private String videoLink;
    private String insuranceAmount;
    private String crmManagerName;
    private String gpsLocation;
    private String dateEvent;
    private String timeEvent;
    private int durationEvent;
    private final Map<String, Integer> drones = new HashMap<>();
    private final Map<String, String> figures = new HashMap<>();

    @Override
    public void exitPortugueseVersion(ProposalTemplateParser.PortugueseVersionContext ctx) {
        language = "PT";
    }

    @Override
    public void exitEnglishVersion(ProposalTemplateParser.EnglishVersionContext ctx) {
        language = "EN";
    }

    @Override
    public void exitRepresentativeName(ProposalTemplateParser.RepresentativeNameContext ctx) {
        StringBuilder sb = new StringBuilder();

        List<TerminalNode> names = ctx.NAME();
        for (int i = 0; i < names.size(); i++) {
            sb.append(names.get(i).getText());
            if (i < names.size() - 1) {
                sb.append(" ");
            }
        }

        representativeName = sb.toString();
    }

    @Override public void exitCompanyName(ProposalTemplateParser.CompanyNameContext ctx) {
        StringBuilder sb = new StringBuilder();

        List<TerminalNode> names = ctx.NAME();
        for (int i = 0; i < names.size(); i++) {
            sb.append(names.get(i).getText());
            if (i < names.size() - 1) {
                sb.append(" ");
            }
        }

        companyName = sb.toString();
    }

    @Override public void exitStreet(ProposalTemplateParser.StreetContext ctx) {
        StringBuilder sb = new StringBuilder();

        List<TerminalNode> names = ctx.NAME();
        for (int i = 0; i < names.size(); i++) {
            sb.append(names.get(i).getText());
            if (i < names.size() - 1) {
                sb.append(" ");
            }
        }

        street = sb.toString();
    }

    @Override public void exitPostalCode(ProposalTemplateParser.PostalCodeContext ctx) {
        postalCode = ctx.POSTALCODE().getText();
    }

    @Override public void exitCity(ProposalTemplateParser.CityContext ctx) {
        StringBuilder sb = new StringBuilder();

        List<TerminalNode> names = ctx.NAME();
        for (int i = 0; i < names.size(); i++) {
            sb.append(names.get(i).getText());
            if (i < names.size() - 1) {
                sb.append(" ");
            }
        }

        city = sb.toString();
    }

    @Override public void exitCountry(ProposalTemplateParser.CountryContext ctx) {
        StringBuilder sb = new StringBuilder();

        List<TerminalNode> names = ctx.NAME();
        for (int i = 0; i < names.size(); i++) {
            sb.append(names.get(i).getText());
            if (i < names.size() - 1) {
                sb.append(" ");
            }
        }

        country = sb.toString();
    }

    @Override public void exitVatNumber(ProposalTemplateParser.VatNumberContext ctx) {
        vatNumber = ctx.VATNUMBER().getText();
    }

    @Override public void exitShowProposalNumber(ProposalTemplateParser.ShowProposalNumberContext ctx) {
        showProposalNumber = ctx.NUMBER().getText();
    }

    @Override public void exitDate(ProposalTemplateParser.DateContext ctx) {
        date = ctx.DATE().getText();
    }

    @Override public void exitVideoLink(ProposalTemplateParser.VideoLinkContext ctx) {
        videoLink = ctx.URL().getText();
    }

    @Override public void exitInsuranceAmount(ProposalTemplateParser.InsuranceAmountContext ctx) {
        insuranceAmount = ctx.NUMBER().getText();
        if (ctx.CURRENCY() != null) {
            insuranceAmount += " " + ctx.CURRENCY().getText();
        }
    }

    @Override public void exitCrmManagerName(ProposalTemplateParser.CrmManagerNameContext ctx) {
        StringBuilder sb = new StringBuilder();

        List<TerminalNode> names = ctx.NAME();
        for (int i = 0; i < names.size(); i++) {
            sb.append(names.get(i).getText());
            if (i < names.size() - 1) {
                sb.append(" ");
            }
        }

        crmManagerName = sb.toString();
    }

    @Override public void exitCoordinates(ProposalTemplateParser.CoordinatesContext ctx) {
        gpsLocation = ctx.getText();
    }

    @Override public void exitDateEvent(ProposalTemplateParser.DateEventContext ctx) {
        dateEvent = ctx.DATE().getText();
    }

    @Override public void exitTimeEvent(ProposalTemplateParser.TimeEventContext ctx) {
        timeEvent = ctx.TIME().getText();
    }

    @Override public void exitDurationEvent(ProposalTemplateParser.DurationEventContext ctx) {
        durationEvent = Integer.parseInt(ctx.NUMBER().getText());
    }

    @Override public void exitDroneItem(ProposalTemplateParser.DroneItemContext ctx) {
        String model = ctx.droneModel().getText();
        int quantity = Integer.parseInt(ctx.droneQuantity().getText());
        drones.put(model, quantity);
    }

    @Override public void exitFigureItem(ProposalTemplateParser.FigureItemContext ctx) {
        String position = ctx.figurePosition().getText();
        String code = ctx.figureCode().getText();
        figures.put(code, position);
    }

    public String generateDocument() {
        StringBuilder builder = new StringBuilder();
        boolean isPortuguese = "PT".equalsIgnoreCase(language);
        boolean hasRepName = representativeName != null && !representativeName.trim().isEmpty();

        if (isPortuguese) {
            appendHeaderPortuguese(builder, hasRepName);
            appendBodyPortuguese(builder);
            appendClosingPortuguese(builder, hasRepName);
            appendDetailsPortuguese(builder);
        } else {
            appendHeaderEnglish(builder, hasRepName);
            appendBodyEnglish(builder);
            appendClosingEnglish(builder, hasRepName);
            appendDetailsEnglish(builder);
        }

        appendDroneList(builder, isPortuguese);
        appendFigureList(builder, isPortuguese);

        return builder.toString();
    }

    private void appendHeaderPortuguese(StringBuilder builder, boolean hasRepName) {
        if (hasRepName) {
            builder.append("Exmo(a). Senhor(a),\n")
                    .append(representativeName).append("\n");
        } else {
            builder.append("Exmos. Senhores\n");
        }
        appendAddressBlock(builder, true);
        builder.append("Referência ").append(showProposalNumber).append(" / ").append(date).append("\n")
                .append("Proposta de Show\n\n");

        if (hasRepName) {
            builder.append(companyName).append(" é um cliente VIP e ");
        }
    }

    private void appendBodyPortuguese(StringBuilder builder) {
        builder.append("Shodrone tem o prazer de submeter à sua apreciação uma proposta para execução de um show aéreo com drones, conforme descrição abaixo.\n")
                .append("Shodrone é uma empresa que dá prioridade à segurança, pelo que usa a mais avançada tecnologia de IA para apoiar o desenvolvimento dos seus shows, ")
                .append("sendo que todos os shows são prévia e cuidadosamente testados/simulados com a tecnologia AI-Test© antes de serem apresentados ao cliente. ")
                .append("No link ").append(videoLink).append(" encontra-se um vídeo com a simulação do show proposto.\n\n")
                .append("Com a aplicação do AI-Test©, um exclusivo da Shodrone, temos a confiança de oferecer um seguro de responsabilidade civil no valor de ")
                .append(insuranceAmount).append(" para o show. ")
                .append("Os dados detalhados do show são apresentados em anexo.\n\n");
    }

    private void appendClosingPortuguese(StringBuilder builder, boolean hasRepName) {
        if (hasRepName) {
            builder.append("Esperamos ter notícias suas em breve.\n\n");
        } else {
            builder.append("Estando certos que seremos alvo da sua preferência.\n\n")
                    .append("Subscrevemo-nos ao dispor.\n\n");
        }

        builder.append("Melhores cumprimentos,\n\n")
                .append(crmManagerName).append("\n")
                .append("CRM Manager\n\n");
    }

    private void appendDetailsPortuguese(StringBuilder builder) {
        builder.append("Anexo – Detalhes do Show ").append(showProposalNumber).append("\n\n")
                .append("Local de realização – ").append(gpsLocation).append("\n")
                .append("Data – ").append(dateEvent).append("\n")
                .append("Hora – ").append(timeEvent).append("\n")
                .append("Duração – ").append(durationEvent).append(" minutos\n\n");
    }

    private void appendHeaderEnglish(StringBuilder builder, boolean hasRepName) {
        if (hasRepName) {
            builder.append("Dear,\n")
                    .append(representativeName).append("\n");
        } else {
            builder.append("Dear Sirs,\n");
        }
        appendAddressBlock(builder, false);
        builder.append("Reference ").append(showProposalNumber).append(" / ").append(date).append("\n")
                .append("Show Proposal\n\n");

        if (hasRepName) {
            builder.append(companyName).append(" is a VIP client and ");
        }
    }

    private void appendBodyEnglish(StringBuilder builder) {
        builder.append("Shodrone is pleased to submit for your consideration a proposal for the execution of an aerial show with drones, as described below.\n")
                .append("Shodrone is a company that prioritizes safety, which is why it uses the most advanced AI technology to support the development of its shows, ")
                .append("with all shows being previously and carefully tested/simulated with AI-Test© technology before being presented to the client. ")
                .append("In the link ").append(videoLink).append(" there is a video with a simulation of the proposed show.\n\n")
                .append("With the application of AI-Test©, a Shodrone exclusive, we are confident in offering a liability insurance of ")
                .append(insuranceAmount).append(" for the show. ")
                .append("The detailed show data is attached.\n\n");
    }

    private void appendClosingEnglish(StringBuilder builder, boolean hasRepName) {
        if (hasRepName) {
            builder.append("We look forward to hearing from you soon.\n\n");
        } else {
            builder.append("We are confident you will choose us.\n\n")
                    .append("Sincerely at your disposal.\n\n");
        }

        builder.append("Best regards,\n\n")
                .append(crmManagerName).append("\n")
                .append("CRM Manager\n\n");
    }

    private void appendDetailsEnglish(StringBuilder builder) {
        builder.append("Attachment – Show Details ").append(showProposalNumber).append("\n\n")
                .append("Location – ").append(gpsLocation).append("\n")
                .append("Date – ").append(dateEvent).append("\n")
                .append("Time – ").append(timeEvent).append("\n")
                .append("Duration – ").append(durationEvent).append(" minutes\n\n");
    }

    private void appendAddressBlock(StringBuilder builder, boolean isPortuguese) {
        builder.append(companyName).append("\n")
                .append(street).append(", ").append(postalCode).append(", ").append(city).append(", ").append(country).append("\n")
                .append(isPortuguese ? "NIF " : "VAT Number ").append(vatNumber).append("\n\n");
    }

    private void appendDroneList(StringBuilder builder, boolean isPortuguese) {
        builder.append(isPortuguese ? "#Lista de drones utilizados\n" : "#List of used drones\n");

        for (Map.Entry<String, Integer> entry : drones.entrySet()) {
            builder.append(entry.getKey()).append(" - ").append(entry.getValue());
            builder.append(isPortuguese ? " unidades\n" : " units\n");
        }
        builder.append("\n");
    }

    private void appendFigureList(StringBuilder builder, boolean isPortuguese) {
        builder.append(isPortuguese ? "#Lista de figuras\n" : "#List of figures\n");

        for (Map.Entry<String, String> entry : figures.entrySet()) {
            builder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
    }

}
