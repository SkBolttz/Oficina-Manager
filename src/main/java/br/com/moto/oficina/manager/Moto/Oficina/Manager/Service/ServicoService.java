package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.OS.ServicoNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Servico.ServicoDuplicadoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.AtualizarServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.CadastrarServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.ServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Servico;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RecursoNaoEncontradoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RegraNegocioException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Servico.ServicoMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.ServicoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ServicoMapper servicoMapper;
    private final OficinaRepository oficinaRepository;

    public ServicoService(
            ServicoRepository servicoRepository,
            ServicoMapper servicoMapper,
            OficinaRepository oficinaRepository) {
        this.servicoRepository = servicoRepository;
        this.servicoMapper = servicoMapper;
        this.oficinaRepository = oficinaRepository;
    }

    /*
     * ======================
     * Cadastro
     * ======================
     */
    public ServicoDTO cadastrarServico(String cnpj, CadastrarServicoDTO dto) {

        Oficina oficina = buscarOficina(cnpj);

        validarDescricaoDuplicada(dto.descricao(), oficina.getId());

        Servico servico = servicoMapper.toEntity(dto);
        servico.setOficina(oficina);
        servico.setAtivo(true);

        return servicoMapper.toDto(servicoRepository.save(servico));
    }

    /*
     * ======================
     * Atualizar
     * ======================
     */
    public ServicoDTO atualizarServico(String cnpj, Long idServico, AtualizarServicoDTO dto) {

        Servico servico = buscarServicoOficina(cnpj, idServico);

        if (dto.descricao() != null &&
                !dto.descricao().equalsIgnoreCase(servico.getDescricao())) {
            validarDescricaoDuplicada(dto.descricao(), servico.getOficina().getId());
        }

        if (dto.descricao() != null) {
            servico.setDescricao(dto.descricao());
        }
        if (dto.observacao() != null) {
            servico.setObservacao(dto.observacao());
        }
        if (dto.valorMaoDeObra() != null) {
            servico.setValorMaoDeObra(dto.valorMaoDeObra());
        }
        if (dto.tempoEstimado() != null) {
            servico.setTempoEstimado(dto.tempoEstimado());
        }

        servicoRepository.save(servico);

        return servicoMapper.toDto(servico);
    }

    /*
     * ======================
     * Ativar / Desativar
     * ======================
     */
    public ServicoDTO ativarServico(String cnpj, Long idServico) {
        Servico servico = buscarServicoOficina(cnpj, idServico);
        servico.setAtivo(true);
        return servicoMapper.toDto(servico);
    }

    public ServicoDTO desativarServico(String cnpj, Long idServico) {
        Servico servico = buscarServicoOficina(cnpj, idServico);
        servico.setAtivo(false);
        return servicoMapper.toDto(servico);
    }

    /*
     * ======================
     * Buscar
     * ======================
     */
    public Page<ServicoDTO> buscarPorDescricao(String cnpj, String descricao, Pageable pageable) {
        Oficina oficina = buscarOficina(cnpj);

        Page<Servico> servicos = servicoRepository.findByDescricaoContainingIgnoreCaseAndOficina(descricao, oficina,
                pageable);

        if (servicos.isEmpty()) {
            throw new ServicoNaoLocalizadoException("Nenhum serviço encontrado");
        }

        return servicos.map(servicoMapper::toDto);
    }

    public Page<ServicoDTO> buscarTodos(String cnpj, Pageable pageable) {
        Oficina oficina = buscarOficina(cnpj);
        return servicoRepository.findByOficina(oficina, pageable)
                .map(servicoMapper::toDto);
    }

    public Page<ServicoDTO> buscarAtivos(String cnpj, Pageable pageable) {
        Oficina oficina = buscarOficina(cnpj);
        return servicoRepository.findByOficinaAndAtivo(oficina, true, pageable)
                .map(servicoMapper::toDto);
    }

    public Page<ServicoDTO> buscarInativos(String cnpj, Pageable pageable) {
        Oficina oficina = buscarOficina(cnpj);
        return servicoRepository.findByOficinaAndAtivo(oficina, false, pageable)
                .map(servicoMapper::toDto);
    }

    /*
     * ======================
     * Métodos privados
     * ======================
     */

    private Oficina buscarOficina(String cnpj) {
        Oficina oficina = oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina nao encontrada"));
        if (oficina == null) {
            throw new OficinaNaoLocalizadaException("Oficina não encontrada");
        }
        return oficina;
    }

    private Servico buscarServicoOficina(String cnpj, Long idServico) {

        Oficina oficina = buscarOficina(cnpj);

        Servico servico = servicoRepository.findById(idServico)
                .orElseThrow(() -> new ServicoNaoLocalizadoException("Serviço não encontrado"));

        if (!servico.getOficina().getId().equals(oficina.getId())) {
            throw new ServicoNaoLocalizadoException("Serviço não pertence a esta oficina");
        }

        return servico;
    }

    private void validarDescricaoDuplicada(String descricao, Long idOficina) {
        if (servicoRepository.existsByDescricaoIgnoreCaseAndOficinaId(descricao, idOficina)) {
            throw new ServicoDuplicadoException("Já existe serviço cadastrado com esta descrição");
        }
    }
}
