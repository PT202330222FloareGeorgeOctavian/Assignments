package data;

import model.Client;
import model.Product;

/**
 * Class that extends {@link AbstractDAO<Product>}}
 */
public class ProductDAO extends AbstractDAO<Product> {
    public ProductDAO() {

    }

    /**
     * Decrease the amount of the product after the order has been placed
     * @param id the id of the product
     * @param x the amount that has been ordered
     */
    public void decreaseAmount(int id, int x) {
        update(id, new String[]{"amount"}, new String[]{String.valueOf(getAmount(id) - x)});
    }

    private int getAmount(int id) {
        return findById(id).getAmount();
    }

    /**
     * Check if the amount that is to be ordered is smaller or equal to the amount of the product
     * @param id the id of the product
     * @param amount the amount of the order
     * @return bool value which represents if there are enough products or not
     */
    public boolean checkAmount(int id, int amount) {
        if (findById(id) != null)
            return findById(id).getAmount() >= amount;
        return false;
    }

    /**
     *
     * Compute the price of the order after it has been placed
     * @param id the id of the product
     * @param amount the amount that has been ordered
     * @return the total price of the order
     */
    public int totalPrice(int id, int amount) {
        return findById(id).getPrice() * amount;
    }

}
