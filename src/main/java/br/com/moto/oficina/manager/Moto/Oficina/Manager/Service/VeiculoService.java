package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

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
    public VeiculoDTO cadastrarVeiculo(String cnpj, String cpfCnpjCliente, CadastrarVeiculoDTO dto) {

        Oficina oficina = localizarOficina(cnpj);

        validarPlacaVazia(dto.placa());
        validarPlacaDuplicada(oficina, dto.placa());

        Cliente cliente = clienteRepository
                .findByCpfCnpjAndOficina(cpfCnpjCliente, oficina)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente não encontrado"));

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
    public VeiculoDTO atualizarVeiculo(String cnpj, String placa, AtualizarVeiculoDTO dto) {

        Oficina oficina = localizarOficina(cnpj);
        Veiculo veiculo = buscarVeiculo(oficina, placa);

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
    public VeiculoDTO ativarVeiculo(String cnpj, String placa) {
        Oficina oficina = localizarOficina(cnpj);
        Veiculo veiculo = buscarVeiculo(oficina, placa);
        veiculo.setAtivo(true);
        return veiculoMapper.toDTO(veiculoRepository.save(veiculo));
    }

    public VeiculoDTO desativarVeiculo(String cnpj, String placa) {
        Oficina oficina = localizarOficina(cnpj);
        Veiculo veiculo = buscarVeiculo(oficina, placa);
        veiculo.setAtivo(false);
        return veiculoMapper.toDTO(veiculoRepository.save(veiculo));
    }

    /*
     * ======================
     * Buscar
     * ======================
     */
    public VeiculoDTO buscarVeiculoPorPlaca(String cnpj, String placa) {
        Oficina oficina = localizarOficina(cnpj);
        return veiculoMapper.toDTO(buscarVeiculo(oficina, placa));
    }

    public Page<VeiculoDTO> buscarVeiculosPorNomeCliente(
            String cnpj, String nome, Pageable pageable) {

        Oficina oficina = localizarOficina(cnpj);
        Page<Veiculo> veiculos = veiculoRepository
                .findByClienteNomeContainingIgnoreCaseAndOficina(nome, oficina, pageable);

        if (veiculos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum veículo encontrado");
        }

        return veiculos.map(veiculoMapper::toDTO);
    }

    public Page<VeiculoDTO> buscarTodos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        return veiculoRepository.findAllByOficina(oficina, pageable)
                .map(veiculoMapper::toDTO);
    }

    public Page<VeiculoDTO> buscarVeiculosAtivos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        return veiculoRepository.findByAtivoAndOficina(true, oficina, pageable)
                .map(veiculoMapper::toDTO);
    }

    public Page<VeiculoDTO> buscarVeiculosInativos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        return veiculoRepository.findByAtivoAndOficina(false, oficina, pageable)
                .map(veiculoMapper::toDTO);
    }

    /*
     * ======================
     * Métodos privados
     * ======================
     */

    private void validarPlacaVazia(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new RegraNegocioException("Placa não pode ser vazia");
        }
    }

    private void validarPlacaDuplicada(Oficina oficina, String placa) {
        if (veiculoRepository.existsByPlacaAndOficina(placa, oficina)) {
            throw new RegraNegocioException("Placa já cadastrada");
        }
    }

    private Veiculo buscarVeiculo(Oficina oficina, String placa) {
        return veiculoRepository.findByPlacaAndOficina(placa, oficina)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Veículo não encontrado"));
    }

    private Oficina localizarOficina(String cnpj) {
        return oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() ->
                        new RegraNegocioException("Oficina não encontrada"));
    }
}
