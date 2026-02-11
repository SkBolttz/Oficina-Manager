package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteAtualizarDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteCadastroDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Cliente;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Cliente.ClienteMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Endereco.EnderecoMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.ClienteRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final EnderecoService enderecoService;
    private final OficinaRepository oficinaRepository;
    private final EnderecoMapper enderecoMapper;

    /**
     * Construtor do serviço.
     *
     * @param clienteRepository Repositório de cliente
     * @param clienteMapper     Mapper de cliente
     * @param enderecoService   Serviço de endereço
     * @param oficinaRepository Repositório de oficina
     * @param enderecoMapper    Mapper de endereço
     */
    public ClienteService(
            ClienteRepository clienteRepository,
            ClienteMapper clienteMapper,
            EnderecoService enderecoService,
            OficinaRepository oficinaRepository,
            EnderecoMapper enderecoMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.enderecoService = enderecoService;
        this.oficinaRepository = oficinaRepository;
        this.enderecoMapper = enderecoMapper;
    }

    /*
     * ======================
     * Cadastro
     * ======================
     */

    /**
     * Cadastra um novo cliente vinculado a uma oficina.
     *
     * @param cnpj CNPJ da oficina à qual o cliente será vinculado
     * @param dto  DTO com os dados de cadastro do cliente
     * @return DTO do cliente cadastrado
     * @throws CPFCNPJNuloException     se o CPF/CNPJ informado no DTO for null
     * @throws CPFCNPJInvalidoException se o CPF/CNPJ informado no DTO for inválido
     * @throws CPFCNPJDuplicadoException se já existir cliente com o mesmo CPF/CNPJ na oficina
     * @throws EmailDuplicadoException  se o e-mail já estiver cadastrado na oficina
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada pelo CNPJ
     */
    public ClienteDTO cadastrarCliente(String cnpj, ClienteCadastroDTO dto) {

        String cpfCnpjNormalizado = normalizarCpfCnpj(dto.cpfCnpj());
        Oficina oficina = localizarOficina(cnpj);

        validarCpfCnpjDuplicado(oficina, cpfCnpjNormalizado);
        validarEmailDuplicado(oficina, dto.email());

        Cliente cliente = clienteMapper.toEntity(dto);

        cliente.setDataCadastro(LocalDate.now());
        cliente.setCpfCnpj(cpfCnpjNormalizado);
        cliente.setOficina(oficina);
        cliente.setAtivo(true);
        cliente.setEndereco(enderecoService.cadastrarEndereco(
                enderecoMapper.toEntity(dto.endereco())));

        clienteRepository.save(cliente);

        return clienteMapper.toDto(cliente);
    }

    /*
     * ======================
     * Atualizar
     * ======================
     */

    /**
     * Atualiza um cliente existente.
     *
     * @param cnpj    CNPJ da oficina
     * @param cpfCnpj CPF/CNPJ do cliente a ser atualizado
     * @param dto     DTO contendo os campos a serem atualizados (campos nulos são ignorados)
     * @return DTO do cliente atualizado
     * @throws CPFCNPJNuloException se o cnpj passado for null
     * @throws CPFCNPJInvalidoException se o cnpj informado for inválido
     * @throws ClienteNaoLocalizadoException se o cliente não for encontrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public ClienteDTO editarCliente(String cnpj, String cpfCnpj, ClienteAtualizarDTO dto) {

        String cnpjNormalizado = normalizarCpfCnpj(cnpj);

        Oficina oficina = localizarOficina(cnpjNormalizado);
        Cliente cliente = localizarClienteCpfCnpj(oficina, cpfCnpj);

        if (dto.nome() != null) {
            cliente.setNome(dto.nome());
        }
        if (dto.email() != null) {
            cliente.setEmail(dto.email());
        }
        if (dto.telefone() != null) {
            cliente.setTelefone(dto.telefone());
        }
        if (dto.endereco() != null) {
            enderecoService.atualizarEnderecoCliente(cnpj, cliente, dto.endereco());
        }

        clienteRepository.save(cliente);

        return clienteMapper.toDto(cliente);
    }

    /*
     * ======================
     * Ativar / Desativar
     * ======================
     */

    /**
     * Ativa um cliente.
     *
     * @param cnpj    CNPJ da oficina
     * @param cpfCnpj CPF/CNPJ do cliente a ser ativado
     * @return DTO do cliente ativado
     * @throws ClienteNaoLocalizadoException se o cliente não for encontrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public ClienteDTO ativarCliente(String cnpj, String cpfCnpj) {
        Oficina oficina = localizarOficina(cnpj);
        Cliente cliente = localizarClienteCpfCnpj(oficina, cpfCnpj);
        cliente.setAtivo(true);
        return clienteMapper.toDto(cliente);
    }

    /**
     * Desativa um cliente.
     *
     * @param cnpj    CNPJ da oficina
     * @param cpfCnpj CPF/CNPJ do cliente a ser desativado
     * @return DTO do cliente desativado
     * @throws ClienteNaoLocalizadoException se o cliente não for encontrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public ClienteDTO desativarCliente(String cnpj, String cpfCnpj) {
        Oficina oficina = localizarOficina(cnpj);
        Cliente cliente = localizarClienteCpfCnpj(oficina, cpfCnpj);
        cliente.setAtivo(false);
        return clienteMapper.toDto(cliente);
    }

    /*
     * ======================
     * Buscar Clientes
     * ======================
     */

    /**
     * Busca um cliente por CPF/CNPJ.
     *
     * @param cnpj    CNPJ da oficina
     * @param cpfCnpj CPF/CNPJ do cliente
     * @return DTO do cliente encontrado
     * @throws ClienteNaoLocalizadoException se o cliente não for encontrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public ClienteDTO buscarClientePorCpfCnpj(String cnpj, String cpfCnpj) {
        Oficina oficina = localizarOficina(cnpj);
        return clienteMapper.toDto(localizarClienteCpfCnpj(oficina, cpfCnpj));
    }

    /**
     * Busca clientes por nome (consulta paginada).
     *
     * @param cnpj     CNPJ da oficina
     * @param nome     Termo a ser buscado no nome do cliente
     * @param pageable Informações de paginação
     * @return Página de DTOs de clientes que correspondem ao critério
     * @throws RecursoNaoEncontradoException se nenhum cliente for encontrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<ClienteDTO> buscarClientePorNome(String cnpj, String nome, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);

        Page<Cliente> clientes = clienteRepository
                .findByNomeContainingIgnoreCaseAndOficina(nome, oficina, pageable);

        if (clientes.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum cliente encontrado");
        }

        return clientes.map(clienteMapper::toDto);
    }

    /**
     * Retorna todos os clientes de uma oficina (paginado).
     *
     * @param cnpj     CNPJ da oficina
     * @param pageable Informações de paginação
     * @return Página de DTOs dos clientes da oficina
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<ClienteDTO> buscarTodosClientes(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);

        return clienteRepository.findAllByOficina(oficina, pageable)
                .map(clienteMapper::toDto);
    }

    /**
     * Retorna todos os clientes ativos de uma oficina (paginado).
     *
     * @param cnpj     CNPJ da oficina
     * @param pageable Informações de paginação
     * @return Página de DTOs dos clientes ativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<ClienteDTO> buscarTodosAtivos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);

        return clienteRepository.findByAtivoAndOficina(true, oficina, pageable)
                .map(clienteMapper::toDto);
    }

    /**
     * Retorna todos os clientes inativos de uma oficina (paginado).
     *
     * @param cnpj     CNPJ da oficina
     * @param pageable Informações de paginação
     * @return Página de DTOs dos clientes inativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<ClienteDTO> buscarTodosInativos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);

        return clienteRepository.findByAtivoAndOficina(false, oficina, pageable)
                .map(clienteMapper::toDto);
    }

    /*
     * ======================
     * Métodos privados
     * ======================
     */

    /**
     * Valida se o e-mail já está cadastrado para a oficina informada.
     *
     * @param oficina Oficina na qual validar o e-mail
     * @param email   E-mail a ser validado
     * @throws EmailDuplicadoException se existir registro com o mesmo e-mail
     */
    private void validarEmailDuplicado(Oficina oficina, String email) {
        if (clienteRepository.findByEmailAndOficina(email, oficina) != null) {
            throw new EmailDuplicadoException("E-mail já cadastrado");
        }
    }

    /**
     * Valida se o CPF/CNPJ já está cadastrado para a oficina informada.
     *
     * @param oficina               Oficina na qual validar
     * @param cpfCnpjNormalizado CPF/CNPJ normalizado (apenas números)
     * @throws CPFCNPJDuplicadoException se existir cliente com o mesmo CPF/CNPJ
     */
    private void validarCpfCnpjDuplicado(Oficina oficina, String cpfCnpjNormalizado) {

        boolean existe = clienteRepository
                .existsByCpfCnpjAndOficina(cpfCnpjNormalizado, oficina);

        if (existe) {
            throw new CPFCNPJDuplicadoException("CPF/CNPJ já cadastrado");
        }
    }

    /**
     * Localiza um cliente pelo CPF/CNPJ na oficina informada.
     *
     * @param oficina Oficina onde buscar o cliente
     * @param cpfCnpj CPF/CNPJ do cliente (pode conter formatação)
     * @return Entidade {@link Cliente} encontrada
     * @throws ClienteNaoLocalizadoException se não localizar o cliente
     */
    private Cliente localizarClienteCpfCnpj(Oficina oficina, String cpfCnpj) {

        String cpfCnpjNormalizado = normalizarCpfCnpj(cpfCnpj);

        return clienteRepository
                .findByCpfCnpjAndOficina(cpfCnpjNormalizado, oficina)
                .orElseThrow(() -> new ClienteNaoLocalizadoException("Cliente não encontrado"));
    }

    /**
     * Localiza uma oficina pelo CNPJ.
     *
     * @param cnpj CNPJ da oficina (pode conter formatação)
     * @return Entidade {@link Oficina} encontrada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    private Oficina localizarOficina(String cnpj) {

        String cnpjNormalizado = normalizarCpfCnpj(cnpj);

        Oficina oficina = oficinaRepository.findByCnpj(cnpjNormalizado)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina nao encontrada"));

        return oficina;
    }

    /**
     * Normaliza um CPF ou CNPJ removendo quaisquer caracteres não numéricos.
     *
     * @param cpfCnpj CPF ou CNPJ possivelmente formatado
     * @return String contendo apenas os dígitos do CPF/CNPJ
     * @throws CPFCNPJNuloException     se o parâmetro for null
     * @throws CPFCNPJInvalidoException se, após normalização, o comprimento não for 11 (CPF) ou 14 (CNPJ)
     */
    private String normalizarCpfCnpj(String cpfCnpj) {

        if (cpfCnpj == null) {
            throw new CPFCNPJNuloException("CPF/CNPJ não pode ser nulo");
        }

        String normalizado = cpfCnpj.replaceAll("\\D", "");

        if (!(normalizado.length() == 11 || normalizado.length() == 14)) {
            throw new CPFCNPJInvalidoException("CPF/CNPJ inválido");
        }

        return normalizado;
    }

}