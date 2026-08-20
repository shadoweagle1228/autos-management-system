package com.autos.autos_service.infrastructure.adapter.in.web;

import com.autos.autos_service.domain.model.Auto;
import com.autos.autos_service.domain.port.in.ManageAutosUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/autos")
public class AutoController {

    private final ManageAutosUseCase manageAutosUseCase;

    public AutoController(ManageAutosUseCase manageAutosUseCase) {
        this.manageAutosUseCase = manageAutosUseCase;
    }

    @PostMapping
    public ResponseEntity<Auto> createAuto(@RequestBody Auto auto, Principal principal) {
        return new ResponseEntity<>(manageAutosUseCase.createAuto(auto, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Auto>> getAllAutos(Principal principal) {
        return ResponseEntity.ok(manageAutosUseCase.getAllAutos(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auto> getAutoById(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(manageAutosUseCase.getAutoById(id, principal.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Auto> updateAuto(@PathVariable Long id, @RequestBody Auto auto, Principal principal) {
        return ResponseEntity.ok(manageAutosUseCase.updateAuto(id, auto, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuto(@PathVariable Long id, Principal principal) {
        manageAutosUseCase.deleteAuto(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Auto>> searchOrFilterAutos(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String brand,
            Principal principal) {
        
        String userId = principal.getName();

        if (query != null && !query.isEmpty()) {
            return ResponseEntity.ok(manageAutosUseCase.searchAutos(userId, query));
        } else if (year != null) {
            return ResponseEntity.ok(manageAutosUseCase.filterByYear(userId, year));
        } else if (brand != null && !brand.isEmpty()) {
            return ResponseEntity.ok(manageAutosUseCase.filterByBrand(userId, brand));
        } 
        
        return ResponseEntity.ok(manageAutosUseCase.getAllAutos(userId));
    }
}