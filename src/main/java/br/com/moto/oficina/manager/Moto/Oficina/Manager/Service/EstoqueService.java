package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.time.LocalDate;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.CodigoItemDuplicadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.EstoqueNuloException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.ItemEstoqueNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Util.ObterUsuarioLogado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.AtualizarEstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.CadastroEstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.EstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Estoque;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Estoque.EstoqueMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.EstoqueRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;
    private final OficinaRepository oficinaRepository;

    /**
     * Construtor do serviço de estoque.
     *
     * @param estoqueRepository Repositório de estoque
     * @param estoqueMapper     Mapper para conversão entre entidade e DTO
     * @param oficinaRepository Repositório de oficina
     */
    public EstoqueService(EstoqueRepository estoqueRepository, EstoqueMapper estoqueMapper,
                          OficinaRepository oficinaRepository) {
        this.estoqueRepository = estoqueRepository;
        this.estoqueMapper = estoqueMapper;
        this.oficinaRepository = oficinaRepository;
    }

    /*
     * ======================
     * Cadastro
     * ======================
     */

    /**
     * Cadastra um novo item de estoque para a oficina informada.
     *
     * @param dto  DTO com os dados do item de estoque
     * @return DTO do item cadastrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws CodigoItemDuplicadoException  se já existir item com o mesmo código na oficina
     */
    public EstoqueDTO cadastrarItemEstoque(CadastroEstoqueDTO dto) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        validarDuplicidade(dto.codigo(), oficina);

        Estoque itemEstoque = estoqueMapper.toEntity(dto);
        itemEstoque.setOficina(oficina);
        itemEstoque.setAtivo(true);
        itemEstoque.setEstoqueAtual(itemEstoque.getEstoqueAtual() == null || itemEstoque.getEstoqueAtual() < 0 ? 0
                : itemEstoque.getEstoqueAtual());
        itemEstoque.setUltimaReposicao(LocalDate.now());

        estoqueRepository.save(itemEstoque);
        return estoqueMapper.toDTO(itemEstoque);
    }

    /*
     * ======================
     * Atualizar
     * ======================
     */

    /**
     * Atualiza um item de estoque existente.
     *
     * @param dto    DTO com os campos a serem atualizados (campos nulos são ignorados)
     * @return DTO do item atualizado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     * @throws EstoqueNuloException se o valor de estoque atual for negativo
     */
    public EstoqueDTO atualizarItemEstoque(AtualizarEstoqueDTO dto) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Estoque itemEstoque = localizarItemEstoque(dto.idItem(), oficina);

        itemEstoque.setDescricao(dto.descricao() == null ? itemEstoque.getDescricao() : dto.descricao());
        itemEstoque.setCodigo(dto.codigo() == null ? itemEstoque.getCodigo() : dto.codigo());
        itemEstoque.setPrecoVenda(dto.precoVenda() == null ? itemEstoque.getPrecoVenda() : dto.precoVenda());
        itemEstoque.setPrecoCompra(dto.precoCompra() == null ? itemEstoque.getPrecoCompra() : dto.precoCompra());
        itemEstoque
                .setEstoqueMinimo(dto.estoqueMinimo() == null ? itemEstoque.getEstoqueMinimo() : dto.estoqueMinimo());
        itemEstoque
                .setEstoqueMaximo(dto.estoqueMaximo() == null ? itemEstoque.getEstoqueMaximo() : dto.estoqueMaximo());
        itemEstoque
                .setUnidadeMedida(dto.unidadeMedida() == null ? itemEstoque.getUnidadeMedida() : dto.unidadeMedida());
        // Garantir que estoqueAtual não fique negativo
        if (dto.estoqueAtual() < 0) {
            throw new EstoqueNuloException("Estoque atual nao pode ser negativo");
        } else {
            itemEstoque
                    .setEstoqueAtual(dto.estoqueAtual() == 0 ? itemEstoque.getEstoqueAtual() : dto.estoqueAtual());
        }

        if (dto.estoqueAtual() > itemEstoque.getEstoqueAtual()) {
            itemEstoque.setUltimaReposicao(LocalDate.now());
        }

        estoqueRepository.save(itemEstoque);
        return estoqueMapper.toDTO(itemEstoque);
    }

    /*
     * ======================
     * Ativar / Desativar
     * ======================
     */

    /**
     * Ativa um item de estoque.
     *
     * @param idItem Identificador do item
     * @return DTO do item ativado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     */
    public EstoqueDTO ativarItemEstoque(Long idItem) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Estoque itemEstoque = localizarItemEstoque(idItem, oficina);
        itemEstoque.setAtivo(true);
        estoqueRepository.save(itemEstoque);
        return estoqueMapper.toDTO(itemEstoque);
    }

    /**
     * Desativa um item de estoque.
     *
     * @param idItem Identificador do item
     * @return DTO do item desativado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     */
    public EstoqueDTO desativarItemEstoque(Long idItem) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Estoque itemEstoque = localizarItemEstoque(idItem, oficina);
        itemEstoque.setAtivo(false);
        estoqueRepository.save(itemEstoque);
        return estoqueMapper.toDTO(itemEstoque);
    }

    /*
     * ======================
     * Buscar
     * ======================
     */

    /**
     * Busca um item de estoque pelo código.
     *
     * @param codigoItem Código do item
     * @return DTO do item encontrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     */
    public EstoqueDTO buscarItemEstoque(String codigoItem) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());

        Estoque itemEstoque = localizarItemCodigo(codigoItem, oficina);
        return estoqueMapper.toDTO(itemEstoque);
    }

    /**
     * Busca itens de estoque por nome (paginado).
     *
     * @param nomeItem Termo a ser buscado na descrição
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens que correspondem ao critério
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se nenhum item for encontrado
     */
    public Page<EstoqueDTO> buscarItemEstoquePorNome(String nomeItem, Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Page<Estoque> itensEstoque = estoqueRepository.findByDescricaoContainingIgnoreCaseAndOficina(nomeItem, oficina,
                pageable);

        if (itensEstoque.isEmpty()) {
            throw new ItemEstoqueNaoLocalizadoException("Item de estoque não encontrado");
        }

        return itensEstoque.map(estoqueMapper::toDTO);
    }

    /**
     * Retorna itens de estoque ativos (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens ativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<EstoqueDTO> buscarItensEstoqueAtivos(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Page<Estoque> itensEstoqueAtivos = estoqueRepository.findByAtivoAndOficina(true, oficina, pageable);
        return itensEstoqueAtivos.map(estoqueMapper::toDTO);
    }

    /**
     * Retorna itens de estoque inativos (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens inativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<EstoqueDTO> buscarItensEstoqueInativos(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Page<Estoque> itensEstoqueInativos = estoqueRepository.findByAtivoAndOficina(false, oficina, pageable);
        return itensEstoqueInativos.map(estoqueMapper::toDTO);
    }

    /**
     * Retorna todos os itens de estoque da oficina (paginado).
     *
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<EstoqueDTO> buscarTodosItens(Pageable pageable) {
        Oficina oficina = localizarOficina(ObterUsuarioLogado.obterCnpjUsuarioLogado());
        Page<Estoque> itensEstoque = estoqueRepository.findByOficina(oficina, pageable);
        return itensEstoque.map(estoqueMapper::toDTO);
    }

    /*
     * ======================
     * Regra para identificação de Estoque baixo
     * ======================
     */

    /*
     * ======================
     * Métodos privados
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
     * Localiza um item de estoque pelo id e oficina.
     *
     * @param id      Identificador do item
     * @param oficina Oficina onde buscar
     * @return Entidade Estoque encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for encontrado
     */
    private Estoque localizarItemEstoque(Long id, Oficina oficina) {
        return estoqueRepository.findByIdAndOficina(id, oficina)
                .orElseThrow(() -> new ItemEstoqueNaoLocalizadoException("Item de estoque não encontrado"));
    }

    /**
     * Valida duplicidade de código do item na oficina.
     *
     * @param nome    Código do item
     * @param oficina Oficina onde validar
     * @throws CodigoItemDuplicadoException se já existir item com o mesmo código
     */
    private void validarDuplicidade(String nome, Oficina oficina) {
        boolean existe = estoqueRepository.existsByCodigoAndOficina(nome, oficina);
        if (existe) {
            throw new CodigoItemDuplicadoException("Já existe um item de estoque com esse código na oficina");
        }
    }

    /**
     * Localiza um item de estoque pelo código na oficina.
     *
     * @param codigoItem Código do item
     * @param oficina    Oficina onde buscar
     * @return Entidade Estoque encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for encontrado
     */
    private Estoque localizarItemCodigo(String codigoItem, Oficina oficina ) {
        return estoqueRepository.findByCodigoAndOficina(codigoItem, oficina)
                .orElseThrow(() -> new ItemEstoqueNaoLocalizadoException("Item de estoque não encontrado"));
    }
}