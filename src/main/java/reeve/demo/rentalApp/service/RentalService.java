package reeve.demo.rentalApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reeve.demo.rentalApp.model.RentalAgreement;
import reeve.demo.rentalApp.model.Tool;
import reeve.demo.rentalApp.repository.ToolRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private ToolRepository toolRepository;

    public RentalAgreement checkout(String toolCode, int rentalDayCount, int discountPercent, LocalDate checkoutDate) {
        if (rentalDayCount < 1) {
            throw new IllegalArgumentException("Rental day count must be at least 1");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }

        Optional<Tool> toolOptional = toolRepository.findById(toolCode);
        if (toolOptional.isEmpty()) {
            throw new IllegalArgumentException("Tool not found");
        }

        Tool tool = toolOptional.get();

        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setToolCode(toolCode);
        rentalAgreement.setToolType(tool.getType());
        rentalAgreement.setToolBrand(tool.getBrand());
        rentalAgreement.setRentalDays(rentalDayCount);
        rentalAgreement.setCheckoutDate(checkoutDate);
        rentalAgreement.setDiscountPercent(discountPercent);

        LocalDate dueDate = checkoutDate.plusDays(rentalDayCount - 1);
        rentalAgreement.setDueDate(dueDate);

        rentalAgreement.setDailyRentalCharge(tool.getDailyCharge());

        int chargeDays = calculateChargeDays(checkoutDate, dueDate, tool);
        rentalAgreement.setChargeDays(chargeDays);

        double preDiscountCharge = round(chargeDays * tool.getDailyCharge(), 2);
        rentalAgreement.setPreDiscountCharge(preDiscountCharge);

        double discountAmount = round(preDiscountCharge * discountPercent / 100, 2);
        rentalAgreement.setDiscountAmount(discountAmount);

        double finalCharge = round(preDiscountCharge - discountAmount, 2);
        rentalAgreement.setFinalCharge(finalCharge);

        return rentalAgreement;
    }

    private double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private int calculateChargeDays(LocalDate checkoutDate, LocalDate dueDate, Tool tool) {
        int chargeDays = 0;
        LocalDate currentDate = checkoutDate;

        while (!currentDate.isAfter(dueDate)) {
            boolean isWeekend = currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isHoliday = isHoliday(currentDate);

            if ((tool.isWeekdayCharge() && !isWeekend && !isHoliday) ||
                    (tool.isWeekendCharge() && isWeekend && !isHoliday) ||
                    (tool.isHolidayCharge() && isHoliday)) {
                chargeDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return chargeDays;
    }

    public boolean isHoliday(LocalDate date) {
        if (date.getMonth() == Month.JULY) {
            // Independence Day
            if (date.getDayOfMonth() == 4) return true;
            if (date.getDayOfMonth() == 3 && date.getDayOfWeek() == DayOfWeek.FRIDAY) return true;
            if (date.getDayOfMonth() == 5 && date.getDayOfWeek() == DayOfWeek.MONDAY) return true;
        }
        // Labor Day
        if (date.getMonth() == Month.SEPTEMBER && date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() <= 7) {
            return true;
        }
        return false;
    }
}