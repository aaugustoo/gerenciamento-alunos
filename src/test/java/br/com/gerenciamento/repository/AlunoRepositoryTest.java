package br.com.gerenciamento.repository;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import java.util.List;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

     @Test
    public void salvarAluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");

        Aluno alunoSalvo = alunoRepository.save(aluno);
        Assert.assertNotNull(alunoSalvo.getId());
    }

    @Test
    public void encontrarAlunoAtivo() {
        Aluno aluno = new Aluno();
        aluno.setNome("Ativo");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.DIREITO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        alunoRepository.save(aluno);

        List<Aluno> ativos = alunoRepository.findByStatusAtivo();
        Assert.assertFalse(ativos.isEmpty());
    }

    @Test
    public void encontrarAlunoInativo() {
        Aluno aluno = new Aluno();
        aluno.setNome("Inativo");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.INATIVO);
        aluno.setMatricula("123456");
        alunoRepository.save(aluno);

        List<Aluno> inativos = alunoRepository.findByStatusInativo();
        Assert.assertFalse(inativos.isEmpty());
    }

    @Test
    public void encontrarPeloNome() {
        Aluno aluno = new Aluno();
        aluno.setNome("José Amaro");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.DIREITO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        alunoRepository.save(aluno);

        List<Aluno> busca = alunoRepository.findByNomeContainingIgnoreCase("Amaro");
        Assert.assertFalse(busca.isEmpty());
    }

}
