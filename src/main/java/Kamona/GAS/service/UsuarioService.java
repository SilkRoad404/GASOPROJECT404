package Kamona.GAS.service;

import Kamona.GAS.model.Usuario;
import Kamona.GAS.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired private UsuarioRepository repo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.withUsername(u.getUsername())
            .password(u.getPassword())
            .roles(u.getRol())
            .build();
    }

    public void guardar(Usuario u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        repo.save(u);
    }

    public void guardarDirecto(Usuario u) {
        repo.save(u);
    }

    public Usuario buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario buscarPorUsername(String username) {
        return repo.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }
}