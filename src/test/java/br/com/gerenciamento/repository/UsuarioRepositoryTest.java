package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Usuario;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void salvarUsuarioCorretamente(){
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@gmail.com");
        usuario.setUser("usuario");
        usuario.setSenha("123");

        Usuario salvo = usuarioRepository.save(usuario);

        Assert.assertNotNull(salvo.getId());
    }

    @Test
    public void acharPorEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@gmail.com");
        usuario.setUser("usuario");
        usuario.setSenha("123");
        usuarioRepository.save(usuario);

        Usuario encontrado = usuarioRepository.findByEmail("usuario@gmail.com");
        Assert.assertEquals("usuario", encontrado.getUser());
    }

     @Test
    public void buscarLoginComExito() {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@gmail.com");
        usuario.setUser("usuario");
        usuario.setSenha("123");
        usuarioRepository.save(usuario);

        Usuario logado = usuarioRepository.buscarLogin("usuario", "123");
        Assert.assertNotNull(logado);
    }

    @Test
    public void buscarLoginComFalha() {
        Usuario tentativa = usuarioRepository.buscarLogin("não", "existe");
        Assert.assertNull(tentativa);
    }
}
