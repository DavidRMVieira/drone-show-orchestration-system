package lapr4.presentation.droneinventory;

import eapli.framework.visitor.Visitor;
import lapr4.droneinventory.dto.DroneDTO;

public class DroneDTOPrinter implements Visitor<DroneDTO> {

    @Override
    public void visit(DroneDTO visitee) {
        System.out.printf(
                "%-20s%-15s%-30s%n",
                visitee.getSerialNumber(),
                visitee.getState(),
                visitee.getModel()
        );
    }

    public String header() {
        return String.format(
                "%-20s%-15s%-30s%n",
                "SERIAL NUMBER", "STATE", "MODEL"
        );
    }
}
