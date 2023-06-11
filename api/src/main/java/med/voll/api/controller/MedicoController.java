package med.voll.api.controller;


import jakarta.validation.Valid;
import med.voll.api.endereco.Endereco;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){
        repository.save(new Medico(dados));
    }

    /*
    * Devolve um list dos elementos
    * tambem devolve informações sobre a paginação
    * http://localhost:8080/medicos?size=1 retorna apenas um registro
    * http://localhost:8080/medicos?size=1&page=1 retornas um registro na proxima pagina(começa de 0)
    * http://localhost:8080/medicos?sort=nome retorna os registros ordenados de forma crescente pelo atributo nome
    * http://localhost:8080/medicos?sort=nome,desc retorna os registros ordenados de forma decrecente pelo atributo nome
    *
    *
    * Assim ele usa os parametros defaault de paginação
    *   @GetMapping
    *   public Page<DadosListagemMedico> listar(Pageable paginacao){
    *   return repository.findAll(paginacao).map(DadosListagemMedico::new);//Tive que criar um novo repository chamado DadosListagemMedico e construtor no repository
    *   }
    *
    * No metodo abaixo eu passo os parametros default, eles podem ser sobrescritos pelos parametros da url
    */
    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);//Tive que criar um novo repository chamado DadosListagemMedico e construtor no repository
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }

    /*
    Delete fisico
    @DeleteMapping("/{id}")//O @PathVariable diz ao spring que o id é uma variavel do caminho da URL
    @Transactional
    public void excluir(@PathVariable Long id){
        repository.deleteById(id);
    }
    */

    @DeleteMapping("/{id}")//O @PathVariable diz ao spring que o id é uma variavel do caminho da URL
    @Transactional
    public void excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
    }

}
