package reeve.demo.rentalApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reeve.demo.rentalApp.model.RentalAgreement;
import reeve.demo.rentalApp.model.Tool;
import reeve.demo.rentalApp.repository.ToolRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RentalServiceTest {

    @InjectMocks
    private RentalService rentalService;

    @Mock
    private ToolRepository toolRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test1() {
        assertThrows(IllegalArgumentException.class, () -> {
            rentalService.checkout("JAKR", 5, 101, LocalDate.of(2015, 9, 3));
        });
    }

    @Test
    void test2() {
        when(toolRepository.findById("LADW")).thenReturn(Optional.of(new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false)));

        RentalAgreement agreement = rentalService.checkout("LADW", 3, 10, LocalDate.of(2020, 7, 2));

        assertNotNull(agreement);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 4), agreement.getDueDate());
        assertEquals(1.99, agreement.getDailyRentalCharge());
        assertEquals(1, agreement.getChargeDays());
        assertEquals(1.99, agreement.getPreDiscountCharge());
        assertEquals(10, agreement.getDiscountPercent());
        assertEquals(0.2, agreement.getDiscountAmount());
        assertEquals(1.79, agreement.getFinalCharge());
    }

    @Test
    void test3() {
        when(toolRepository.findById("CHNS")).thenReturn(Optional.of(new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true)));

        RentalAgreement agreement = rentalService.checkout("CHNS", 5, 25, LocalDate.of(2015, 7, 2));

        assertNotNull(agreement);
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals("Chainsaw", agreement.getToolType());
        assertEquals("Stihl", agreement.getToolBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 6), agreement.getDueDate());
        assertEquals(1.49, agreement.getDailyRentalCharge());
        assertEquals(4, agreement.getChargeDays());
        assertEquals(5.96, agreement.getPreDiscountCharge());
        assertEquals(25, agreement.getDiscountPercent());
        assertEquals(1.49, agreement.getDiscountAmount());
        assertEquals(4.47, agreement.getFinalCharge());
    }

    @Test
    void test4() {
        when(toolRepository.findById("JAKD")).thenReturn(Optional.of(new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false)));

        RentalAgreement agreement = rentalService.checkout("JAKD", 6, 0, LocalDate.of(2015, 9, 3));

        assertNotNull(agreement);
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("DeWalt", agreement.getToolBrand());
        assertEquals(6, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 9, 8), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge());
        assertEquals(3, agreement.getChargeDays()); // Exclude weekends
        assertEquals(8.97, agreement.getFinalCharge());
    }

    @Test
    void test5() {
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false)));

        RentalAgreement agreement = rentalService.checkout("JAKR", 9, 0, LocalDate.of(2015, 7, 2));

        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());
        assertEquals(9, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 10), agreement.getDueDate());
        assertEquals(2.99, agreement.getDailyRentalCharge());
        assertEquals(6, agreement.getChargeDays()); // Exclude weekends
        assertEquals(17.94, agreement.getFinalCharge());
    }

    @Test
    void test6() {
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false)));

        RentalAgreement agreement = rentalService.checkout("JAKR", 4, 50, LocalDate.of(2020, 7, 2));

        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());
        assertEquals(4, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 5), agreement.getDueDate()); // includes holiday on July 3 and weekend
        assertEquals(2.99, agreement.getDailyRentalCharge());
        assertEquals(1, agreement.getChargeDays());
        assertEquals(1.49, agreement.getFinalCharge());
    }
}