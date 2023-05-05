package com.generation.service;

import java.util.Optional;

import com.generation.model.Usuario;
import com.generation.model.UsuarioLogin;
import com.generation.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            return Optional.empty();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senha = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senha);
        return Optional.of(usuarioRepository.save(usuario));
    }

    public Optional<Usuario> atualizarUsuario(Long id, Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            return Optional.of(usuarioRepository.save(usuario));
        }
        return Optional.empty();
    }


    public Optional<UsuarioLogin> autenticarUsuario(UsuarioLogin usuarioLogin) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.getUsuario());
        if (usuario.isPresent()) {
            if (passwordEncoder.matches(usuarioLogin.getSenha(), usuario.get().getSenha())) {
                JwtService jwtService = new JwtService();
                String token = jwtService.generateToken(usuario.get());
                UsuarioLogin usuarioLoginResponse = new UsuarioLogin();
                usuarioLoginResponse.setSenha(token);
                usuarioLoginResponse.setUsuario(usuario.get().getUsuario());
                return Optional.of(usuarioLoginResponse);
            }
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void excluirUsuario(Long id) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.deleteById(id);
        } else {
            throw new Exception("Usuário não encontrado");
        }
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);
        if (usuario.isPresent()) {
            return new org.springframework.security.core.userdetails.User(usuario.get().getUsuario(),
                    usuario.get().getSenha(), usuario.get().getAuthorities());
        }
        throw new UsernameNotFoundException("Usuário não encontrado!");
    }

}
