package wesbin.shipmentservice;

import org.springframework.stereotype.Service;
import wesbin.commonvo.vo.Order;
import wesbin.commonvo.vo.Shipment;
import wesbin.commonvo.vo.ShipmentType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShipmentService {

    private List<Shipment> shipmentList;

    public ShipmentService() {
        shipmentList = new ArrayList<>();
        shipmentList.add(new Shipment(1, ShipmentType.CAR, LocalDate.now(), 50));
        shipmentList.add(new Shipment(2, ShipmentType.PLANE, LocalDate.now(), 200));
        shipmentList.add(new Shipment(3, ShipmentType.SHIP, LocalDate.now(), 100));
        shipmentList.add(new Shipment(4, ShipmentType.TRAIN, LocalDate.now(), 20));
    }

    public Shipment processOrder(Order order) {
        return shipmentList.stream()
                .filter(shipment -> shipment.getType().equals(order.getShipment().getType()))
                .findAny()
                .get();
    }
}
