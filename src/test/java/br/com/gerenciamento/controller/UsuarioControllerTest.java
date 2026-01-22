package br.com.gerenciamento.controller;

import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.service.ServiceUsuario;
import br.com.gerenciamento.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ServiceUsuario serviceUsuario;
    
    private Usuario usuarioTeste;
    
    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        usuarioTeste = new Usuario();
        usuarioTeste.setEmail("teste@email.com");
        usuarioTeste.setUser("usuario_teste");
        usuarioTeste.setSenha("senha123");

        serviceUsuario.salvarUsuario(usuarioTeste);
    }

    @Test
    public void testPaginaInicial() throws Exception {
        // verificação de carregamento da página index
        mockMvc.perform(MockMvcRequestBuilders.get("/index"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attributeExists("aluno"))
                .andExpect(model().attribute("aluno", hasProperty("id", nullValue())));
    }
    
    @Test
    public void testLoginComCredenciaisInvalidas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("user", "usuario_inexistente")
                .param("senha", "senha_errada"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("login/cadastro"))
                .andExpect(model().attributeExists("msg"))
                .andExpect(model().attribute("msg", "Usuario não encontrado. Tente novamente"));
    }
    
    @Test
    public void testCadastrarNovoUsuario() throws Exception {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail("novo@email.com");
        novoUsuario.setUser("novo_usuario");
        novoUsuario.setSenha("nova_senha");
        
        mockMvc.perform(MockMvcRequestBuilders.post("/salvarUsuario")
                .flashAttr("usuario", novoUsuario))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
    
    @Test
    public void testLoginComCredenciaisValidas() throws Exception {
        String senhaCriptografada = Util.md5("senha123");
        
        Usuario usuarioValido = new Usuario();
        usuarioValido.setEmail("valido@email.com");
        usuarioValido.setUser("usuario_valido");
        usuarioValido.setSenha("senha123");
        serviceUsuario.salvarUsuario(usuarioValido);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("user", "usuario_valido")
                .param("senha", "senha123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(request().sessionAttribute("usuarioLogado", notNullValue()));
    }

}
