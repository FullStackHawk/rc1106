package reeve.demo.rentalApp.model;

import lombok.Data;

@Data
public class Checkout {
    private String toolCode;
    private int rentalDayCount;
    private int discountPercent;
    private String checkoutDate;
}