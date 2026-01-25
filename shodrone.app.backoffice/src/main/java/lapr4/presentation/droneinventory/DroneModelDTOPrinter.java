package lapr4.presentation.droneinventory;

import eapli.framework.visitor.Visitor;
import lapr4.droneinventory.dto.DroneModelDTO;

public class DroneModelDTOPrinter implements Visitor<DroneModelDTO> {

    @Override
    public void visit(DroneModelDTO visitee) {
        System.out.printf(
                "%-30s%-30s%n",
                visitee.getName(),
                visitee.getManufacturerName()
        );
    }

    public String header() {
        return String.format(
                "%-30s%-30s%n",
                "MODEL NAME", "MANUFACTURER"
        );
    }
}
