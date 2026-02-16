package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Funcionario.FuncionarioNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Funcionario.FuncionarioStatusException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Util.ObterUsuarioLogado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.AtualizarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.CadastrarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.FuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Endereco;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Funcionario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Endereco.EnderecoMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Funcionario.FuncionarioMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.FuncionarioRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioMapper funcionarioMapper;
    private final OficinaRepository oficinaRepository;
    private final EnderecoService enderecoService;
    private final EnderecoMapper enderecoMapper;

    /**
     * Construtor do serviço de funcionário.
     *
     * @param funcionarioRepository Repositório de funcionários
     * @param funcionarioMapper     Mapper para conversão entre entidade e DTO
     * @param oficinaRepository     Repositório de oficinas
     * @param enderecoService       Serviço de endereço
     * @param enderecoMapper        Mapper de endereço
     */
    public FuncionarioService(FuncionarioRepository funcionarioRepository, FuncionarioMapper funcionarioMapper,
                              OficinaRepository oficinaRepository, EnderecoService enderecoService, EnderecoMapper enderecoMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioMapper = funcionarioMapper;
        this.oficinaRepository = oficinaRepository;
        this.enderecoService = enderecoService;
        this.enderecoMapper = enderecoMapper;
    }

    /*
     * ======================
     * Cadastrar
     * ======================
     */

    /**
     * Cadastra um novo funcionário vinculado a uma oficina.
     *
     * @param dto  DTO com os dados do funcionário
     * @return DTO do funcionário cadastrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws EmailDuplicadoException       se o e-mail já estiver cadastrado na oficina
     * @throws CPFCNPJDuplicadoException     se o CPF já estiver cadastrado na oficina
     * @throws CPFCNPJNuloException          se o CPF informado for nulo
     * @throws CPFCNPJInvalidoException      se o CPF informado for inválido
     */
    public FuncionarioDTO cadastrarFuncionario(CadastrarFuncionarioDTO dto) {

        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());

        String cpfNormalizado = normalizarCpf(dto.cpf());

        validarDuplicidadeEmail(oficina, dto.email());
        validarDuplicidadeCPF(oficina, cpfNormalizado);

        Funcionario funcionario = funcionarioMapper.toEntity(dto);

        funcionario.setCpf(cpfNormalizado);
        funcionario.setAtivo(true);
        funcionario.setCargo(dto.cargo());
        funcionario.setDataAdmissao(LocalDate.now());
        funcionario.setOficina(oficina);

        Endereco endereco = enderecoMapper.toEntity(dto.endereco());
        funcionario.setEndereco(enderecoService.cadastrarEndereco(endereco));

        funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    /*
     * ======================
     * Atualizar
     * ======================
     */

    /**
     * Atualiza um funcionário existente.
     * @param dto  DTO com os campos a serem atualizados (campos nulos são ignorados)
     * @return DTO do funcionário atualizado
     * @throws OficinaNaoLocalizadaException   se a oficina não for encontrada
     * @throws FuncionarioNaoLocalizadoException se o funcionário não for localizado
     */
    public FuncionarioDTO atualizarFuncionario(AtualizarFuncionarioDTO dto) {

        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());

        Funcionario funcionario = localizarFuncionario(oficina, dto.cpfFuncionario());

        if (dto.nome() != null) {
            funcionario.setNome(dto.nome());
        }
        if (dto.email() != null) {
            funcionario.setEmail(dto.email());
        }
        if (dto.telefone() != null) {
            funcionario.setTelefone(dto.telefone());
        }
        if (dto.cargo() != null) {
            funcionario.setCargo(dto.cargo());
        }
        if (dto.endereco() != null) {
            enderecoService.atualizarEnderecoFuncionario(ObterUsuarioLogado.obterCnpjUsuarioLogado(), funcionario, dto.endereco());
        }

        funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    /*
     * ======================
     * Ativar / Desativar
     * ======================
     */

    /**
     * Ativa um funcionário.
     *
     * @param cpf  CPF do funcionário a ser ativado
     * @return DTO do funcionário ativado
     * @throws OficinaNaoLocalizadaException   se a oficina não for encontrada
     * @throws FuncionarioNaoLocalizadoException se o funcionário não for localizado
     * @throws FuncionarioStatusException      se o funcionário já estiver ativo
     */
    public FuncionarioDTO ativarFuncionario(String cpf) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Funcionario funcionario = localizarFuncionario(oficina, cpf);

        if (funcionario.getAtivo() == true) {
            throw new FuncionarioStatusException("Funcionario já está ativo");
        }

        funcionario.setAtivo(true);
        funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    /**
     * Inativa um funcionário.
     *
     * @param cpf  CPF do funcionário a ser inativado
     * @return DTO do funcionário inativado
     * @throws OficinaNaoLocalizadaException   se a oficina não for encontrada
     * @throws FuncionarioNaoLocalizadoException se o funcionário não for localizado
     * @throws FuncionarioStatusException      se o funcionário já estiver inativo
     */
    public FuncionarioDTO inativarFuncionario(String cpf) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Funcionario funcionario = localizarFuncionario(oficina, cpf);

        if (funcionario.getAtivo() == false) {
            throw new FuncionarioStatusException("Funcionario já está inativo");
        }

        funcionario.setAtivo(false);
        funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    /*
     * ======================
     * Buscar
     * ======================
     */

    /**
     * Busca um funcionário por CPF.
     *
     * @param cpf  CPF do funcionário
     * @return DTO do funcionário encontrado
     * @throws OficinaNaoLocalizadaException   se a oficina não for encontrada
     * @throws FuncionarioNaoLocalizadoException se o funcionário não for localizado
     */
    public FuncionarioDTO buscarFuncionarioPorCPF(String cpf) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Funcionario funcionario = localizarFuncionario(oficina, cpf);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    /**
     * Busca funcionários por nome (paginado).
     *
     * @param nomeFuncionario Termo a ser buscado no nome do funcionário
     * @param pageable        Informações de paginação
     * @return Página de DTOs dos funcionários que correspondem ao critério
     * @throws OficinaNaoLocalizadaException   se a oficina não for encontrada
     * @throws FuncionarioNaoLocalizadoException se nenhum funcionário for encontrado
     */
    public Page<FuncionarioDTO> buscarFuncionarioPorNome(String nomeFuncionario, Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Page<Funcionario> funcionarios = funcionarioRepository.findByNomeContainingIgnoreCaseAndOficina(nomeFuncionario,
                oficina, pageable);

        if (funcionarios.isEmpty()) {
            throw new FuncionarioNaoLocalizadoException("Nenhum funcionário encontrado");
        }

        return funcionarios.map(funcionarioMapper::toResponseDTO);
    }

    /**
     * Retorna todos os funcionários da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos funcionários
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<FuncionarioDTO> buscarTodosFuncionarios(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return funcionarioRepository.findByOficina(oficina, pageable)
                .map(funcionarioMapper::toResponseDTO);
    }

    /**
     * Retorna todos os funcionários ativos da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos funcionários ativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<FuncionarioDTO> buscarTodosFuncionariosAtivos(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return funcionarioRepository.findByAtivoAndOficina(true, oficina, pageable)
                .map(funcionarioMapper::toResponseDTO);
    }

    /**
     * Retorna todos os funcionários inativos da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos funcionários inativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<FuncionarioDTO> buscarTodosFuncionariosInativos(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return funcionarioRepository.findByAtivoAndOficina(false, oficina, pageable)
                .map(funcionarioMapper::toResponseDTO);
    }

    /*
     * ======================
     * Métodos Privados
     * ======================
     */

    /**
     * Localiza uma oficina pelo CNPJ.
     *
     * @param cnpj CNPJ da oficina
     * @return Entidade Oficina encontrada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    private Oficina localizarOficina(String cnpj) {
        return oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina não encontrada"));
    }

    /**
     * Valida se o e-mail já está cadastrado para a oficina informada.
     *
     * @param oficina Oficina na qual validar o e-mail
     * @param email   E-mail a ser validado
     * @throws EmailDuplicadoException se existir registro com o mesmo e-mail
     */
    private void validarDuplicidadeEmail(Oficina oficina, String email) {
        if (funcionarioRepository.findByEmailAndOficina(email, oficina) != null) {
            throw new EmailDuplicadoException("E-mail já cadastrado");
        }
    }

    /**
     * Valida se o CPF já está cadastrado para a oficina informada.
     *
     * @param oficina       Oficina na qual validar
     * @param cpfNormalizado CPF normalizado (apenas números)
     * @throws CPFCNPJDuplicadoException se existir funcionário com o mesmo CPF
     */
    private void validarDuplicidadeCPF(Oficina oficina, String cpfNormalizado) {

        if (funcionarioRepository.existsByCpfAndOficina(cpfNormalizado, oficina)) {
            throw new CPFCNPJDuplicadoException("CPF já cadastrado");
        }
    }

    /**
     * Localiza um funcionário pelo CPF na oficina informada.
     *
     * @param oficina Oficina onde buscar o funcionário
     * @param cpf     CPF do funcionário (pode conter formatação)
     * @return Entidade {@link Funcionario} encontrada
     * @throws FuncionarioNaoLocalizadoException se não localizar o funcionário
     */
    private Funcionario localizarFuncionario(Oficina oficina, String cpf) {

        String cpfNormalizado = normalizarCpf(cpf);

        return funcionarioRepository
                .findByCpfAndOficina(cpfNormalizado, oficina)
                .orElseThrow(() -> new FuncionarioNaoLocalizadoException("Funcionário não encontrado"));
    }

    /**
     * Normaliza um CPF removendo quaisquer caracteres não numéricos.
     *
     * @param cpf CPF possivelmente formatado
     * @return String contendo apenas os dígitos do CPF
     * @throws CPFCNPJNuloException     se o parâmetro for null
     * @throws CPFCNPJInvalidoException se, após normalização, o CPF não tiver 11 dígitos
     */
    private String normalizarCpf(String cpf) {
        if (cpf == null) {
            throw new CPFCNPJNuloException("CPF não pode ser nulo");
        }

        String cpfNormalizado = cpf.replaceAll("\\D", "");

        if (!cpfNormalizado.matches("\\d{11}")) {
            throw new CPFCNPJInvalidoException("CPF inválido");
        }

        return cpfNormalizado;
    }

}