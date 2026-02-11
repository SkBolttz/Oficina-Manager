package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.ClienteNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Endereco.CepNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Funcionario.FuncionarioNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Api.ViaCep;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.ViaCepDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.AtualizarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Cliente;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Endereco;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Funcionario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RegraNegocioException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.ClienteRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.EnderecoRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.FuncionarioRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.ClienteNaoLocalizadoException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final OficinaRepository oficinaRepository;
    private final ViaCep viaCep;

    /**
     * Construtor do serviço de endereço.
     *
     * @param enderecoRepository    Repositório de endereços
     * @param clienteRepository     Repositório de clientes
     * @param funcionarioRepository Repositório de funcionários
     * @param oficinaRepository     Repositório de oficinas
     * @param viaCep                Cliente para consulta de CEP (ViaCep)
     */
    public EnderecoService(
            EnderecoRepository enderecoRepository,
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            OficinaRepository oficinaRepository,
            ViaCep viaCep) {

        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.oficinaRepository = oficinaRepository;
        this.viaCep = viaCep;
    }

    /*
     * ======================
     * Cadastro
     * ======================
     */

    /**
     * Cadastra um endereço, evitando duplicação.
     *
     * @param endereco Entidade de endereço a ser cadastrada
     * @return Entidade de endereço persistida ou existente
     */
    public Endereco cadastrarEndereco(Endereco endereco) {
        return validarEnderecoDuplicado(endereco);
    }

    /*
     * ======================
     * Atualização
     * ======================
     */

    /**
     * Atualiza o endereço de um cliente vinculado a uma oficina.
     *
     * @param cnpj    CNPJ da oficina
     * @param cliente Entidade do cliente que terá o endereço atualizado
     * @param dto     DTO contendo os novos valores do endereço (valores nulos são ignorados)
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ClienteNaoLocalizadoException se o cliente não for localizado
     */
    public void atualizarEnderecoCliente(String cnpj, Cliente cliente, EnderecoDTO dto) {

        Oficina oficina = oficinaRepository.findByCnpj(cnpj).orElseThrow();
        Cliente clienteLocalizado = clienteRepository
                .findByCpfCnpjAndOficina(cliente.getCpfCnpj(), oficina).orElseThrow(() -> new ClienteNaoLocalizadoException("Cliente não localizado"));

        Endereco endereco = clienteLocalizado.getEndereco();

        if (dto.cep() != null) {
            endereco.setCep(dto.cep());
        }
        if (dto.logradouro() != null) {
            endereco.setLogradouro(dto.logradouro());
        }
        if (dto.numero() != null) {
            endereco.setNumero(dto.numero());
        }
        if (dto.complemento() != null) {
            endereco.setComplemento(dto.complemento());
        }
        if (dto.bairro() != null) {
            endereco.setBairro(dto.bairro());
        }
        if (dto.municipio() != null) {
            endereco.setMunicipio(dto.municipio());
        }
        if (dto.uf() != null) {
            endereco.setUf(dto.uf());
        }

        Endereco enderecoFinal = validarEnderecoDuplicado(endereco);

        cliente.setEndereco(enderecoFinal);
        clienteRepository.save(cliente);
    }

    /**
     * Atualiza o endereço de um funcionário vinculado a uma oficina.
     *
     * @param cnpj        CNPJ da oficina
     * @param funcionario Entidade do funcionário que terá o endereço atualizado
     * @param dto         DTO contendo os novos valores do endereço (valores nulos são ignorados)
     * @throws OficinaNaoLocalizadaException   se a oficina não for encontrada
     * @throws FuncionarioNaoLocalizadoException se o funcionário não for localizado
     */
    public void atualizarEnderecoFuncionario(String cnpj, Funcionario funcionario, EnderecoDTO dto) {

        Oficina oficina = oficinaRepository.findByCnpj(cnpj).orElseThrow();
        Funcionario funcionarioLocalizado = funcionarioRepository
                .findByCpfAndOficina(funcionario.getCpf(), oficina).orElseThrow(() -> new FuncionarioNaoLocalizadoException("Funcionário não localizado"));

        Endereco endereco = funcionarioLocalizado.getEndereco();

        if (dto.cep() != null) {
            endereco.setCep(dto.cep());
        }
        if (dto.logradouro() != null) {
            endereco.setLogradouro(dto.logradouro());
        }
        if (dto.numero() != null) {
            endereco.setNumero(dto.numero());
        }
        if (dto.complemento() != null) {
            endereco.setComplemento(dto.complemento());
        }
        if (dto.bairro() != null) {
            endereco.setBairro(dto.bairro());
        }
        if (dto.municipio() != null) {
            endereco.setMunicipio(dto.municipio());
        }
        if (dto.uf() != null) {
            endereco.setUf(dto.uf());
        }

        Endereco enderecoFinal = validarEnderecoDuplicado(endereco);

        funcionario.setEndereco(enderecoFinal);
        funcionarioRepository.save(funcionario);
    }

    /**
     * Atualiza o endereço de uma oficina.
     *
     * @param oficina Entidade da oficina que terá o endereço atualizado
     * @param dto     DTO contendo os novos valores da oficina (campos de endereço nulos são ignorados)
     * @throws OficinaNaoLocalizadaException se a oficina não for localizada
     */
    public void atualizarEnderecoOficina(Oficina oficina, AtualizarOficinaDTO dto) {

        Oficina oficinaLocalizada = oficinaRepository.findByCnpj(oficina.getCnpj()).orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina não localizada"));
        Endereco endereco = oficinaLocalizada.getEndereco();

        if (dto.endereco().cep() != null) {
            endereco.setCep(dto.endereco().cep());
        }
        if (dto.endereco().logradouro() != null) {
            endereco.setLogradouro(dto.endereco().logradouro());
        }
        if (dto.endereco().numero() != null) {
            endereco.setNumero(dto.endereco().numero());
        }
        if (dto.endereco().complemento() != null) {
            endereco.setComplemento(dto.endereco().complemento());
        }
        if (dto.endereco().bairro() != null) {
            endereco.setBairro(dto.endereco().bairro());
        }
        if (dto.endereco().municipio() != null) {
            endereco.setMunicipio(dto.endereco().municipio());
        }
        if (dto.endereco().uf() != null) {
            endereco.setUf(dto.endereco().uf());
        }

        Endereco enderecoFinal = validarEnderecoDuplicado(endereco);
        oficina.setEndereco(enderecoFinal);
        oficinaRepository.save(oficina);
    }

    /*
     * ======================
     * Métodos Privados
     * ======================
     */

    /**
     * Valida se um endereço equivalente já existe e evita duplicados.
     * Se o endereço informado já possuir id, retorna o próprio endereço.
     *
     * @param endereco Endereço a ser validado
     * @return Endereço existente ou persistido
     */
    private Endereco validarEnderecoDuplicado(Endereco endereco) {

        return enderecoRepository
                .findByCepAndLogradouroAndNumeroAndBairroAndMunicipioAndUf(
                        endereco.getCep(),
                        endereco.getLogradouro(),
                        endereco.getNumero(),
                        endereco.getBairro(),
                        endereco.getMunicipio(),
                        endereco.getUf())
                .orElseGet(() -> {
                    if (endereco.getId() != null) {
                        return endereco;
                    }
                    return enderecoRepository.save(endereco);
                });
    }

    /**
     * Consulta informações de endereço por CEP usando o serviço ViaCep.
     *
     * @param cep CEP a ser consultado
     * @return DTO com os dados de endereço retornados pela API
     * @throws CepNaoLocalizadoException se o CEP não for encontrado
     */
    public EnderecoDTO buscarCep(String cep) {

        ViaCepDTO cepAPI = viaCep.buscarCep(cep);

        if (viaCep == null || cepAPI.cep() == null) {
            throw new CepNaoLocalizadoException("CEP não encontrado");
        }

        return new EnderecoDTO(
                cepAPI.cep(),
                cepAPI.logradouro(),
                null,
                cepAPI.complemento(),
                cepAPI.bairro(),
                cepAPI.municipio(),
                cepAPI.uf());
    }
}