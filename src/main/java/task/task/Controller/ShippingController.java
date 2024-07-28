package task.task.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import task.task.DTO.ShippingDTO;
import task.task.Entity.Shipping;
import task.task.Service.ShippingService;

import java.util.Optional;

@RestController
@Tag(name = "Shipping Management")
@RequestMapping("/shippings")
public class ShippingController {

    @Autowired
    ShippingService shippingService;

    @Operation(
            description = "Post endpoint for Shipping",
            summary = "This endpoint is used to Add a new Shipping"
    )

    @PostMapping
    public ShippingDTO addNewShipping(@RequestBody ShippingDTO shippingDTO) {
        return shippingService.addNewShipping(shippingDTO);
    }

    @Operation(
            description = "Get endpoint for Shipping By ID",
            summary = "This endpoint is used to get Shipping by ID"
    )

    @GetMapping("/{shippingId}")
    public Optional<ShippingDTO> getShippingById(@PathVariable("shippingId") int shippingId) {
        return shippingService.getShippingById(shippingId);
    }
}
