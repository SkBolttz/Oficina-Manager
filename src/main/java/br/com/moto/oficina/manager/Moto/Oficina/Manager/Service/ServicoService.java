package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.OS.ServicoNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Servico.ServicoDuplicadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Util.ObterUsuarioLogado;
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

    /**
     * Construtor do serviço de serviços.
     *
     * @param servicoRepository Repositório de serviços
     * @param servicoMapper     Mapper para conversão entre entidade e DTO
     * @param oficinaRepository Repositório de oficinas
     */
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

    /**
     * Cadastra um novo serviço para a oficina informada.
     *
     * @param dto  DTO com os dados do serviço
     * @return DTO do serviço cadastrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ServicoDuplicadoException     se já existir serviço com a mesma descrição na oficina
     */
    public ServicoDTO cadastrarServico(CadastrarServicoDTO dto) {

        Oficina oficina = buscarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());

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

    /**
     * Atualiza um serviço existente da oficina.
     *
     * Campos nulos no DTO são ignorados. Caso a descrição seja alterada, valida-se duplicidade.
     * @param dto        DTO com os campos a serem atualizados
     * @return DTO do serviço atualizado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ServicoNaoLocalizadoException se o serviço não for localizado
     * @throws ServicoDuplicadoException     se a nova descrição já existir para a oficina
     */
    public ServicoDTO atualizarServico(AtualizarServicoDTO dto) {

        Servico servico = buscarServicoOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado(), dto.idServico());

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

    /**
     * Ativa um serviço da oficina.
     *
     * @param idServico Identificador do serviço
     * @return DTO do serviço ativado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ServicoNaoLocalizadoException se o serviço não for localizado
     */
    public ServicoDTO ativarServico(Long idServico) {
        Servico servico = buscarServicoOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado(), idServico);
        servico.setAtivo(true);
        return servicoMapper.toDto(servico);
    }

    /**
     * Desativa um serviço da oficina.
     *
     * @param idServico Identificador do serviço
     * @return DTO do serviço desativado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ServicoNaoLocalizadoException se o serviço não for localizado
     */
    public ServicoDTO desativarServico(Long idServico) {
        Servico servico = buscarServicoOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado(), idServico);
        servico.setAtivo(false);
        return servicoMapper.toDto(servico);
    }

    /*
     * ======================
     * Buscar
     * ======================
     */

    /**
     * Busca serviços pela descrição (paginado).
     *
     * @param descricao Termo a ser buscado na descrição
     * @param pageable  Informações de paginação
     * @return Página de DTOs dos serviços encontrados
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ServicoNaoLocalizadoException se nenhum serviço for encontrado
     */
    public Page<ServicoDTO> buscarPorDescricao(String descricao, Pageable pageable) {
        Oficina oficina = buscarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());

        Page<Servico> servicos = servicoRepository.findByDescricaoContainingIgnoreCaseAndOficina(descricao, oficina,
                pageable);

        if (servicos.isEmpty()) {
            throw new ServicoNaoLocalizadoException("Nenhum serviço encontrado");
        }

        return servicos.map(servicoMapper::toDto);
    }

    /**
     * Retorna todos os serviços da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos serviços
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<ServicoDTO> buscarTodos(Pageable pageable) {
        Oficina oficina = buscarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return servicoRepository.findByOficina(oficina, pageable)
                .map(servicoMapper::toDto);
    }

    /**
     * Retorna serviços ativos da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos serviços ativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<ServicoDTO> buscarAtivos(Pageable pageable) {
        Oficina oficina = buscarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return servicoRepository.findByOficinaAndAtivo(oficina, true, pageable)
                .map(servicoMapper::toDto);
    }

    /**
     * Retorna serviços inativos da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos serviços inativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<ServicoDTO> buscarInativos(Pageable pageable) {
        Oficina oficina = buscarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        return servicoRepository.findByOficinaAndAtivo(oficina, false, pageable)
                .map(servicoMapper::toDto);
    }

    /*
     * ======================
     * Métodos privados
     * ======================
     */

    /**
     * Busca a oficina pelo CNPJ.
     *
     * @param cnpj CNPJ da oficina
     * @return Entidade Oficina encontrada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    private Oficina buscarOficina(String cnpj) {
        Oficina oficina = oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina nao encontrada"));
        if (oficina == null) {
            throw new OficinaNaoLocalizadaException("Oficina não encontrada");
        }
        return oficina;
    }

    /**
     * Busca um serviço pelo id e valida pertencimento à oficina.
     *
     * @param cnpj      CNPJ da oficina
     * @param idServico Identificador do serviço
     * @return Entidade Servico encontrada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ServicoNaoLocalizadoException se o serviço não for encontrado ou não pertencer à oficina
     */
    private Servico buscarServicoOficina(String cnpj, Long idServico) {

        Oficina oficina = buscarOficina(cnpj);

        Servico servico = servicoRepository.findById(idServico)
                .orElseThrow(() -> new ServicoNaoLocalizadoException("Serviço não encontrado"));

        if (!servico.getOficina().getId().equals(oficina.getId())) {
            throw new ServicoNaoLocalizadoException("Serviço não pertence a esta oficina");
        }

        return servico;
    }

    /**
     * Valida se já existe serviço com a mesma descrição na oficina.
     *
     * @param descricao  Descrição a validar
     * @param idOficina  Identificador da oficina
     * @throws ServicoDuplicadoException se já existir serviço com a mesma descrição
     */
    private void validarDescricaoDuplicada(String descricao, Long idOficina) {
        if (servicoRepository.existsByDescricaoIgnoreCaseAndOficinaId(descricao, idOficina)) {
            throw new ServicoDuplicadoException("Já existe serviço cadastrado com esta descrição");
        }
    }
}