package reeve.demo.rentalApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reeve.demo.rentalApp.model.Checkout;
import reeve.demo.rentalApp.model.RentalAgreement;
import reeve.demo.rentalApp.service.RentalService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Operation(summary = "Checkout a tool", description = "Generate a rental agreement for a tool.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/checkout")
    public ResponseEntity<RentalAgreement> checkout(@RequestBody Checkout checkout) {
        RentalAgreement rentalAgreement = rentalService.checkout(
                checkout.getToolCode(),
                checkout.getRentalDayCount(),
                checkout.getDiscountPercent(),
                LocalDate.parse(checkout.getCheckoutDate(), DateTimeFormatter.ofPattern("MM/dd/yy"))
        );
        return ResponseEntity.ok(rentalAgreement);
    }
}
