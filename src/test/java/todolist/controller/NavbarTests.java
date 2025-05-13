package todolist.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import todolist.authentication.ManagerUserSession;
import todolist.dto.UsuarioData;
import todolist.service.TareaService;
import todolist.service.UsuarioService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Test de la barra de navegación.
 * Verifica que se muestren los enlaces correctos según el estado de autenticación del usuario.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NavbarTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerUserSession sessionManager;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TareaService tareaService;


    /**
     * Verifica que si el usuario está logueado, la barra de navegación
     * contiene su nombre y un botón para cerrar sesión.
     */
    @Test
    void navbarMuestraOpcionesUsuarioLogueado() throws Exception {
        Long userId = 1L;
        configurarMocksUsuarioLogueado(userId);
        mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Richard Stallman")))
                .andExpect(content().string(containsString("Log out")));
    }

    /**
     * Si el usuario NO está logueado, en la página "About" deben aparecer
     * enlaces a login y registro, en lugar del menú del usuario.
     */
    @Test
    void navbarMuestraLoginYRegisterCuandoNoLogeado() throws Exception {
        when(sessionManager.usuarioLogeado()).thenReturn(null);
        mockMvc.perform(get("/about"))
                .andExpect(content().string(containsString("href=\"/login\"")))
                .andExpect(content().string(containsString("href=\"/registro\"")));
    }


    /**
     * Verifica que si el usuario está logueado, la barra de navegación
     * muestra su nombre correctamente.
     */
    @Test
    void navbarMuestraNombreUsuarioCorrectamente() throws Exception {
        Long userId = 1L;
        configurarMocksUsuarioLogueado(userId);
        mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect(content().string(containsString("Richard Stallman")));
    }



    @Test
    void menuDesplegableContieneOpcionLogout() throws Exception {
        Long userId = 1L;
        configurarMocksUsuarioLogueado(userId);
        mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect(content().string(containsString("dropdown-menu")))
                .andExpect(content().string(containsString("Log out")));
    }

    /**
     * Método auxiliar para configurar el estado simulado de un usuario logueado.
     */
    @Test
    void navbarNoContieneEnlacesCriticosEnPaginasDeError() throws Exception {
        mockMvc.perform(get("/ruta-inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(not(containsString("href=\"/usuarios/1/tareas\""))));
    }

    private void configurarMocksUsuarioLogueado(Long userId) {
        UsuarioData usuarioMock = new UsuarioData();
        usuarioMock.setId(userId);
        usuarioMock.setNombre("Richard Stallman");
        usuarioMock.setEmail("richard@umh.es");

        when(sessionManager.usuarioLogeado()).thenReturn(userId);
        when(usuarioService.findById(userId)).thenReturn(usuarioMock);
        when(tareaService.allTareasUsuario(userId)).thenReturn(Collections.emptyList());
    }
}