package com.generation.controller;

import com.generation.model.Tema;
import com.generation.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/temas")
@CrossOrigin("*")
public class TemaController {

    @Autowired
    private TemaRepository temaRepository;

    @GetMapping
    public List<Tema> getAll() {
        return temaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tema> getById(@PathVariable Long id) {
        Optional<Tema> tema = temaRepository.findById(id);

        if (tema.isPresent()) {
            return ResponseEntity.ok(tema.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/descricao/{descricao}")
    public List<Tema> getByDescricao(@PathVariable String descricao) {
        return temaRepository.findAllByDescricaoContainingIgnoreCase(descricao);
    }

    @PostMapping
    public ResponseEntity<Tema> post(@RequestBody Tema tema) {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(tema));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tema> put(@PathVariable Long id, @RequestBody Tema tema) {
        tema.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(tema));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        temaRepository.deleteById(id);
    }
}

