package com.jarawin.issuer.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jarawin.issuer.entity.Acquirer;
import com.jarawin.issuer.service.AcquirerService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/acquirers")
public class AcquirerController {

    @Autowired
    AcquirerService acquirerService;

    // Read all
    @GetMapping()
    public List<Acquirer> getAcquirers() {
        return acquirerService.findAllAcquirers();
    }

    // Read by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAcquirer(@PathVariable String id) {
        Acquirer acquirer = acquirerService.findAcquirerById(id);
        if (acquirer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(acquirer);
    }

    // Create
    @PostMapping()
    public ResponseEntity<?> postAcquirer(@Valid @RequestBody Acquirer body) {
        Acquirer acquirer = acquirerService.saveAcquirer(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(acquirer);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> putAcquirer(@PathVariable String id, @Valid @RequestBody Acquirer body) {
        Acquirer acquirer = acquirerService.findAcquirerById(id);
        if (acquirer == null) {
            return ResponseEntity.notFound().build();
        }

        acquirer = acquirerService.updateAcquirer(acquirer, body);
        return ResponseEntity.ok(acquirer);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAcquirer(@PathVariable String id) {
        Acquirer acquirer = acquirerService.findAcquirerById(id);

        if (acquirer == null) {
            return ResponseEntity.notFound().build();
        }

        acquirerService.deleteAcquirer(acquirer);
        return ResponseEntity.ok().build();
    }
}