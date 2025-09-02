
package service;

import model.Aluno;
import model.Usuario;
import model.enums.TipoPlano;

import java.util.*;
import java.util.stream.Collectors;

public class UsuarioService {

    private Map<String, Usuario> usuarios = new HashMap<>();

    public void addUser(Usuario usuario) {
        usuarios.put(usuario.getEmail().toLowerCase(), usuario);
    }

    public Optional<Usuario> findByEmail(String email) {
        return Optional.ofNullable(usuarios.get(email.toLowerCase()));
    }

    public List<Aluno> getAllStudents() {
        return usuarios.values().stream()
                .filter(u -> u instanceof Aluno)
                .map(u -> (Aluno) u)
                .collect(Collectors.toList());
    }

    public Map<TipoPlano, List<Aluno>> groupStudentsBySubscriptionPlan() {
        return getAllStudents().stream()
                .collect(Collectors.groupingBy(Aluno::getPlano));
    }
}
