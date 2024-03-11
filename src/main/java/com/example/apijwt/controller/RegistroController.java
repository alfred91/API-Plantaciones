package com.example.apijwt.controller;

import com.example.apijwt.entity.Registro;
import com.example.apijwt.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registros")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @PostMapping
    public ResponseEntity<Registro> createRegistro(@RequestBody Registro registro) {
        Registro nuevoRegistro = registroService.save(registro);
        return ResponseEntity.ok(nuevoRegistro);
    }

    @GetMapping
    public ResponseEntity<List<Registro>> getAllRegistros() {
        List<Registro> registros = registroService.findAll();
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Registro> getRegistroById(@PathVariable Long id) {
        return registroService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Registro> updateRegistro(@PathVariable Long id, @RequestBody Registro registroDetails) {
        return registroService.update(id, registroDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistro(@PathVariable Long id) {
        if (registroService.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
