package wesbin.productservice;

import org.springframework.stereotype.Service;
import wesbin.commonvo.vo.Order;
import wesbin.commonvo.vo.Product;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> productList;

    public ProductService() {
        productList = new ArrayList<>();
        productList.add(new Product(1, "Example#1", 500));
        productList.add(new Product(2, "Example#2", 100));
        productList.add(new Product(3, "Example#3", 1000));
        productList.add(new Product(4, "Example#4", 200));
    }

    public Product processOrder(Order order) {
        return productList.stream().filter(product -> product.getName().equals(order.getProduct().getName())).findAny().get();
    }
}
