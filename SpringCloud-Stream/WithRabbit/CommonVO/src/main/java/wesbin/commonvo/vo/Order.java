package wesbin.commonvo.vo;

import java.time.LocalDateTime;

public class Order {

    private Integer id;
    private OrderType type;
    private LocalDateTime createAt;
    private OrderStatus status;
    private Product product;
    private Shipment shipment;

    public Order() {
    }

    public Order(Integer id, OrderType type, LocalDateTime createAt, OrderStatus status, Product product, Shipment shipment) {
        this.id = id;
        this.type = type;
        this.createAt = createAt;
        this.status = status;
        this.product = product;
        this.shipment = shipment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    @Override
    public String toString() {
        return String.format("Order[id=%d,product=%d,shipment=%d]", id, product.getId(), shipment.getId());
    }
}