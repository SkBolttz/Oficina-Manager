package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.ClienteNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Veiculo.PlacaDuplicadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Veiculo.PlacaVaziaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Veiculo.VeiculoNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Util.ObterUsuarioLogado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.AtualizarVeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.CadastrarVeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.VeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Cliente;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Veiculo;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RecursoNaoEncontradoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RegraNegocioException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Veiculo.VeiculoMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.ClienteRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.VeiculoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;
    private final OficinaRepository oficinaRepository;
    private final VeiculoMapper veiculoMapper;

    /**
     * Construtor do serviço de veículos.
     *
     * @param veiculoRepository   Repositório de veículos
     * @param clienteRepository   Repositório de clientes
     * @param oficinaRepository   Repositório de oficinas
     * @param veiculoMapper       Mapper para conversão entre entidade e DTO
     */
    public VeiculoService(
            VeiculoRepository veiculoRepository,
            ClienteRepository clienteRepository,
            OficinaRepository oficinaRepository,
            VeiculoMapper veiculoMapper) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
        this.oficinaRepository = oficinaRepository;
        this.veiculoMapper = veiculoMapper;
    }

    /*
     * ======================
     * Cadastrar
     * ======================
     */

    /**
     * Cadastra um novo veículo para o cliente informado e vincula à oficina.
     *
     * @param dto               DTO com os dados do veículo
     * @return DTO do veículo cadastrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ClienteNaoLocalizadoException se o cliente não for encontrado
     * @throws PlacaVaziaException           se a placa for nula ou vazia
     * @throws PlacaDuplicadaException       se já existir veículo com a mesma placa na oficina
     */
    public VeiculoDTO cadastrarVeiculo(CadastrarVeiculoDTO dto) {

        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());

        validarPlacaVazia(dto.placa());
        validarPlacaDuplicada(oficina, dto.placa());

        Cliente cliente = clienteRepository
                .findByCpfCnpjAndOficina(dto.cnpjCpf(), oficina)
                .orElseThrow(() ->
                        new ClienteNaoLocalizadoException("Cliente não encontrado"));

        Veiculo veiculo = veiculoMapper.toEntity(dto);
        veiculo.setCliente(cliente);
        veiculo.setOficina(oficina);
        veiculo.setAtivo(true);

        return veiculoMapper.toDTO(veiculoRepository.save(veiculo));
    }

    /*
     * ======================
     * Atualizar
     * ======================
     */

    /**
     * Atualiza os dados de um veículo existente identificado pela placa na oficina.
     *
     * Campos nulos no DTO são ignorados.
     *
     * @param dto   DTO com os campos a serem atualizados
     * @return DTO do veículo atualizado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws VeiculoNaoLocalizadoException se o veículo não for encontrado
     */
    public VeiculoDTO atualizarVeiculo(AtualizarVeiculoDTO dto) {

        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Veiculo veiculo = buscarVeiculo(oficina, dto.placa());

        if (dto.marca() != null) {
            veiculo.setMarca(dto.marca());
        }
        if (dto.modelo() != null) {
            veiculo.setModelo(dto.modelo());
        }
        if (dto.cor() != null) {
            veiculo.setCor(dto.cor());
        }
        if (dto.tipo() != null) {
            veiculo.setTipo(dto.tipo());
        }
        if (dto.combustivel() != null) {
            veiculo.setCombustivel(dto.combustivel());
        }

        return veiculoMapper.toDTO(veiculoRepository.save(veiculo));
    }

    /*
     * ======================
     * Ativar / Desativar
     * ======================
     */

    /**
     * Ativa um veículo identificado pela placa na oficina.
     *
     * @param placa Placa do veículo a ser ativado
     * @return DTO do veículo ativado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws VeiculoNaoLocalizadoException se o veículo não for encontrado
     */
    public VeiculoDTO ativarVeiculo(String placa) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Veiculo veiculo = buscarVeiculo(oficina, placa);
        veiculo.setAtivo(true);
        return veiculoMapper.toDTO(veiculoRepository.save(veiculo));
    }

    /**
     * Desativa um veículo identificado pela placa na oficina.
     *
     * @param placa Placa do veículo a ser desativado
     * @return DTO do veículo desativado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws VeiculoNaoLocalizadoException se o veículo não for encontrado
     */
    public VeiculoDTO desativarVeiculo(String placa) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Veiculo veiculo = buscarVeiculo(oficina, placa);
        veiculo.setAtivo(false);
        return veiculoMapper.toDTO(veiculoRepository.save(veiculo));
    }

    /*
     * ======================
     * Buscar
     * ======================
     */

    /**
     * Busca um veículo pela placa na oficina.
     *
     * @param placa Placa do veículo
     * @return DTO do veículo encontrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws VeiculoNaoLocalizadoException se o veículo não for encontrado
     */
    public VeiculoDTO buscarVeiculoPorPlaca(String placa) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return veiculoMapper.toDTO(buscarVeiculo(oficina, placa));
    }

    /**
     * Busca veículos pelo nome do cliente (paginado).
     *
     * @param nome     Nome (ou parte) do cliente
     * @param pageable Informações de paginação
     * @return Página de DTOs dos veículos encontrados
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws VeiculoNaoLocalizadoException se nenhum veículo for encontrado
     */
    public Page<VeiculoDTO> buscarVeiculosPorNomeCliente(
            String nome, Pageable pageable) {

        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Page<Veiculo> veiculos = veiculoRepository
                .findByClienteNomeContainingIgnoreCaseAndOficina(nome, oficina, pageable);

        if (veiculos.isEmpty()) {
            throw new VeiculoNaoLocalizadoException("Nenhum veículo encontrado");
        }

        return veiculos.map(veiculoMapper::toDTO);
    }

    /**
     * Retorna todos os veículos da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos veículos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<VeiculoDTO> buscarTodos(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return veiculoRepository.findAllByOficina(oficina, pageable)
                .map(veiculoMapper::toDTO);
    }

    /**
     * Retorna veículos ativos da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos veículos ativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<VeiculoDTO> buscarVeiculosAtivos(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return veiculoRepository.findByAtivoAndOficina(true, oficina, pageable)
                .map(veiculoMapper::toDTO);
    }

    /**
     * Retorna veículos inativos da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos veículos inativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<VeiculoDTO> buscarVeiculosInativos(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return veiculoRepository.findByAtivoAndOficina(false, oficina, pageable)
                .map(veiculoMapper::toDTO);
    }

    /*
     * ======================
     * Métodos privados
     * ======================
     */

    /**
     * Valida se a placa informada não é nula ou vazia.
     *
     * @param placa Placa a ser validada
     * @throws PlacaVaziaException se a placa for nula ou vazia
     */
    private void validarPlacaVazia(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new PlacaVaziaException("Placa não pode ser vazia");
        }
    }

    /**
     * Valida se já existe veículo com a mesma placa na oficina.
     *
     * @param oficina Oficina onde validar
     * @param placa   Placa a ser validada
     * @throws PlacaDuplicadaException se a placa já estiver cadastrada na oficina
     */
    private void validarPlacaDuplicada(Oficina oficina, String placa) {
        if (veiculoRepository.existsByPlacaAndOficina(placa, oficina)) {
            throw new PlacaDuplicadaException("Placa já cadastrada");
        }
    }

    /**
     * Busca um veículo pela placa e oficina.
     *
     * @param oficina Oficina onde buscar
     * @param placa   Placa do veículo
     * @return Entidade Veiculo encontrada
     * @throws VeiculoNaoLocalizadoException se o veículo não for encontrado
     */
    private Veiculo buscarVeiculo(Oficina oficina, String placa) {
        return veiculoRepository.findByPlacaAndOficina(placa, oficina)
                .orElseThrow(() ->
                        new VeiculoNaoLocalizadoException("Veículo não encontrado"));
    }

    /**
     * Localiza uma oficina pelo CNPJ.
     *
     * @param cnpj CNPJ da oficina
     * @return Entidade Oficina encontrada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    private Oficina localizarOficina(String cnpj) {
        return oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() ->
                        new OficinaNaoLocalizadaException("Oficina não encontrada"));
    }
}