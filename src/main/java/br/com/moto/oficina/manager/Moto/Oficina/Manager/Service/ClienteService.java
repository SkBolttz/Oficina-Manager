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

    public ClienteDTO ativarCliente(String cnpj, String cpfCnpj) {
        Oficina oficina = localizarOficina(cnpj);
        Cliente cliente = localizarClienteCpfCnpj(oficina, cpfCnpj);
        cliente.setAtivo(true);
        return clienteMapper.toDto(cliente);
    }

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
    public ClienteDTO buscarClientePorCpfCnpj(String cnpj, String cpfCnpj) {
        Oficina oficina = localizarOficina(cnpj);
        return clienteMapper.toDto(localizarClienteCpfCnpj(oficina, cpfCnpj));
    }

    public Page<ClienteDTO> buscarClientePorNome(String cnpj, String nome, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);

        Page<Cliente> clientes = clienteRepository
                .findByNomeContainingIgnoreCaseAndOficina(nome, oficina, pageable);

        if (clientes.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum cliente encontrado");
        }

        return clientes.map(clienteMapper::toDto);
    }

    public Page<ClienteDTO> buscarTodosClientes(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);

        return clienteRepository.findAllByOficina(oficina, pageable)
                .map(clienteMapper::toDto);
    }

    public Page<ClienteDTO> buscarTodosAtivos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);

        return clienteRepository.findByAtivoAndOficina(true, oficina, pageable)
                .map(clienteMapper::toDto);
    }

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

    private void validarEmailDuplicado(Oficina oficina, String email) {
        if (clienteRepository.findByEmailAndOficina(email, oficina) != null) {
            throw new EmailDuplicadoException("E-mail já cadastrado");
        }
    }

    private void validarCpfCnpjDuplicado(Oficina oficina, String cpfCnpjNormalizado) {

        boolean existe = clienteRepository
                .existsByCpfCnpjAndOficina(cpfCnpjNormalizado, oficina);

        if (existe) {
            throw new CPFCNPJDuplicadoException("CPF/CNPJ já cadastrado");
        }
    }

    private Cliente localizarClienteCpfCnpj(Oficina oficina, String cpfCnpj) {

        String cpfCnpjNormalizado = normalizarCpfCnpj(cpfCnpj);

        return clienteRepository
                .findByCpfCnpjAndOficina(cpfCnpjNormalizado, oficina)
                .orElseThrow(() -> new ClienteNaoLocalizadoException("Cliente não encontrado"));
    }

    private Oficina localizarOficina(String cnpj) {

        String cnpjNormalizado = normalizarCpfCnpj(cnpj);

        Oficina oficina = oficinaRepository.findByCnpj(cnpjNormalizado)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina nao encontrada"));

        return oficina;
    }

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
