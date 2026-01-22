package br.com.gerenciamento.controller;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import java.util.List;
import org.junit.*;
import br.com.gerenciamento.service.ServiceAluno;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AlunoControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ServiceAluno serviceAluno;
    
    private Aluno alunoTeste;
    
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        alunoTeste = new Aluno();
        alunoTeste.setNome("João da Silva");
        alunoTeste.setMatricula("MAT123");
        alunoTeste.setCurso(Curso.INFORMATICA);
        alunoTeste.setStatus(Status.ATIVO);
        alunoTeste.setTurno(Turno.MATUTINO);
        
        serviceAluno.save(alunoTeste);
    }
    
    @After
    public void tearDown() {
        if (alunoTeste.getId() != null) {
            serviceAluno.deleteById(alunoTeste.getId());
        }
    }

    @Test
    public void testPesquisarAlunoPorNome() throws Exception {
        String nomePesquisa = "João";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/pesquisar-aluno")
                .param("nome", nomePesquisa))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("Aluno/pesquisa-resultado"))
                .andExpect(model().attributeExists("ListaDeAlunos"))
                .andExpect(model().attribute("ListaDeAlunos", 
                        hasItem(hasProperty("nome", containsString(nomePesquisa)))));
    }
    
    @Test
    public void testAcessarFormularioInserirAluno() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/inserirAlunos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("Aluno/formAluno"))
                .andExpect(model().attributeExists("aluno"))
                .andExpect(model().attribute("aluno", hasProperty("id", nullValue())));
    }
    
    @Test
    public void testAcessarAlunosAtivos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alunos-ativos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("Aluno/alunos-ativos"))
                .andExpect(model().attributeExists("alunosAtivos"))
                .andExpect(model().attribute("alunosAtivos", 
                        hasItem(hasProperty("status", equalTo(Status.ATIVO)))));
    }
    
    @Test
    public void testAcessarAlunosInativos() throws Exception {
        Aluno alunoInativo = new Aluno();
        alunoInativo.setNome("Maria Inativa");
        alunoInativo.setMatricula("MAT456");
        alunoInativo.setCurso(Curso.ADMINISTRACAO);
        alunoInativo.setStatus(Status.INATIVO);
        alunoInativo.setTurno(Turno.NOTURNO);
        serviceAluno.save(alunoInativo);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/alunos-inativos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("Aluno/alunos-inativos"))
                .andExpect(model().attributeExists("alunosInativos"))
                .andExpect(model().attribute("alunosInativos", 
                        hasItem(hasProperty("status", equalTo(Status.INATIVO)))));

        serviceAluno.deleteById(alunoInativo.getId());
    }
    
}
