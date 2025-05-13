package todolist.repository;

import todolist.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de integración para verificar que el método findAll() del repositorio
 * de usuarios devuelve correctamente todos los registros.
 */
@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class UserListTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Verifica que findAll() devuelve todos los usuarios guardados en la base de datos.
     */
    @Test
    public void testFindAllReturnsAllUsers() {
        Usuario usuario1 = new Usuario("repo-user1@umh.es");
        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario("repo-user2@umh.es");
        usuarioRepository.save(usuario2);

        Iterable<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(2);
    }
}