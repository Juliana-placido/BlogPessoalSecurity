package com.generation.controller;

import com.generation.model.Postagem;
import com.generation.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postagens")
public class PostagemController {

    @Autowired
    private PostagemRepository postagemRepository;

    @GetMapping
    public ResponseEntity<List<Postagem>> getAll() {
        List<Postagem> postagens = postagemRepository.findAll();

        if (postagens.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(postagens);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Postagem> getById(@PathVariable Long id) {
        Optional<Postagem> postagem = postagemRepository.findById(id);

        if (postagem.isPresent()) {
            return ResponseEntity.ok(postagem.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
        List<Postagem> postagens = postagemRepository.findAllByTituloContainingIgnoreCase(titulo);

        if (postagens.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(postagens);
        }
    }

    @PostMapping
    public ResponseEntity<Postagem> post(@RequestBody Postagem postagem) {
        Postagem postagemSalva = postagemRepository.save(postagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(postagemSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Postagem> put(@PathVariable Long id, @RequestBody Postagem postagem) {
        Optional<Postagem> postagemExistente = postagemRepository.findById(id);

        if (postagemExistente.isPresent()) {
            Postagem postagemAtualizada = postagemExistente.get();
            postagemAtualizada.setTitulo(postagem.getTitulo());
            postagemAtualizada.setTexto(postagem.getTexto());
            postagemRepository.save(postagemAtualizada);
            return ResponseEntity.ok(postagemAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Postagem> postagemExistente = postagemRepository.findById(id);

        if (postagemExistente.isPresent()) {
            postagemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
