package br.com.gerenciamento.service;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.util.Util;
import br.com.gerenciamento.exception.EmailExistsException;
import jakarta.validation.ConstraintViolationException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @Test(expected = EmailExistsException.class)
    public void salvarUsuarioEmailDuplicado() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("usuario@gmail.com");
        usuario1.setUser("usuario1");
        usuario1.setSenha("123");
        serviceUsuario.salvarUsuario(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("usuario@gmail.com");
        usuario2.setUser("usuario2");
        usuario2.setSenha("123");
        serviceUsuario.salvarUsuario(usuario2);
    }

    @Test
    public void salvarUsuarioCorretamente() throws Exception{
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@gmail.com");
        usuario.setUser("usuario");
        usuario.setSenha("123");
        serviceUsuario.salvarUsuario(usuario);

        Assert.assertNotNull(usuario.getId());
    }
    
    @Test
    public void loginComSucesso() throws Exception{
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@gmail.com");
        usuario.setUser("usuario");
        usuario.setSenha("123");
        serviceUsuario.salvarUsuario(usuario);

        Usuario logado = serviceUsuario.loginUser("usuario", Util.md5("123"));
        Assert.assertNotNull(logado);
    }

    @Test
    public void loginComFalha(){
        Usuario logado = serviceUsuario.loginUser("não", "existe");
        Assert.assertNull(logado);
    }
}
