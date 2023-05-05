package com.generation.controller;

import com.generation.model.Usuario;
import com.generation.model.UsuarioLogin;
import com.generation.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario).orElseThrow(() -> new RuntimeException("Não foi possível cadastrar o usuário"));

            return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@RequestBody UsuarioLogin usuarioLogin) {
        try {
            Optional<UsuarioLogin> usuarioAutenticado = usuarioService.autenticarUsuario(usuarioLogin);
            if(usuarioAutenticado.isPresent()){
                return new ResponseEntity<>(usuarioAutenticado.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuário ou senha inválidos", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            Optional<Usuario> usuarioEncontrado = usuarioService.buscarUsuarioPorId(id);
            if (usuarioEncontrado.isPresent()) {
                return new ResponseEntity<>(usuarioEncontrado.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Optional<Usuario> usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);
            if (usuarioAtualizado.isPresent()) {
                return new ResponseEntity<>(usuarioAtualizado.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        try {
            usuarioService.excluirUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

