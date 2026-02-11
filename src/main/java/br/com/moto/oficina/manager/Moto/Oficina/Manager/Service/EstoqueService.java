java
        package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.CodigoItemDuplicadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.EstoqueInsuficienteException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.EstoqueNuloException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.ItemEstoqueNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.AtualizarEstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.CadastroEstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.EstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Estoque;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RegraNegocioException;
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
     * @param cnpj CNPJ da oficina
     * @param dto  DTO com os dados do item de estoque
     * @return DTO do item cadastrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws CodigoItemDuplicadoException  se já existir item com o mesmo código na oficina
     */
    public EstoqueDTO cadastrarItemEstoque(String cnpj, CadastroEstoqueDTO dto) {
        Oficina oficina = localizarOficina(cnpj);
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
     * @param cnpj   CNPJ da oficina
     * @param idItem Identificador do item de estoque
     * @param dto    DTO com os campos a serem atualizados (campos nulos são ignorados)
     * @return DTO do item atualizado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     * @throws EstoqueNuloException se o valor de estoque atual for negativo
     */
    public EstoqueDTO atualizarItemEstoque(String cnpj, Long idItem, AtualizarEstoqueDTO dto) {
        Oficina oficina = localizarOficina(cnpj);
        Estoque itemEstoque = localizarItemEstoque(idItem, oficina);

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
                    .setEstoqueAtual(dto.estoqueAtual() == null ? itemEstoque.getEstoqueAtual() : dto.estoqueAtual());
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
     * @param cnpj   CNPJ da oficina
     * @param idItem Identificador do item
     * @return DTO do item ativado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     */
    public EstoqueDTO ativarItemEstoque(String cnpj, Long idItem) {
        Oficina oficina = localizarOficina(cnpj);
        Estoque itemEstoque = localizarItemEstoque(idItem, oficina);
        itemEstoque.setAtivo(true);
        estoqueRepository.save(itemEstoque);
        return estoqueMapper.toDTO(itemEstoque);
    }

    /**
     * Desativa um item de estoque.
     *
     * @param cnpj   CNPJ da oficina
     * @param idItem Identificador do item
     * @return DTO do item desativado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     */
    public EstoqueDTO desativarItemEstoque(String cnpj, Long idItem) {
        Oficina oficina = localizarOficina(cnpj);
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
     * @param cnpj       CNPJ da oficina
     * @param codigoItem Código do item
     * @return DTO do item encontrado
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     */
    public EstoqueDTO buscarItemEstoque(String cnpj, String codigoItem) {
        Oficina oficina = localizarOficina(cnpj);

        Estoque itemEstoque = localizarItemCodigo(codigoItem, oficina);
        return estoqueMapper.toDTO(itemEstoque);
    }

    /**
     * Busca itens de estoque por nome (paginado).
     *
     * @param cnpj     CNPJ da oficina
     * @param nomeItem Termo a ser buscado na descrição
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens que correspondem ao critério
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se nenhum item for encontrado
     */
    public Page<EstoqueDTO> buscarItemEstoquePorNome(String cnpj, String nomeItem, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
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
     * @param cnpj     CNPJ da oficina
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens ativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<EstoqueDTO> buscarItensEstoqueAtivos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        Page<Estoque> itensEstoqueAtivos = estoqueRepository.findByAtivoAndOficina(true, oficina, pageable);
        return itensEstoqueAtivos.map(estoqueMapper::toDTO);
    }

    /**
     * Retorna itens de estoque inativos (paginado).
     *
     * @param cnpj     CNPJ da oficina
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens inativos
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<EstoqueDTO> buscarItensEstoqueInativos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        Page<Estoque> itensEstoqueInativos = estoqueRepository.findByAtivoAndOficina(false, oficina, pageable);
        return itensEstoqueInativos.map(estoqueMapper::toDTO);
    }

    /**
     * Retorna todos os itens de estoque da oficina (paginado).
     *
     * @param cnpj     CNPJ da oficina
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<EstoqueDTO> buscarTodosItens(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        Page<Estoque> itensEstoque = estoqueRepository.findByOficina(oficina, pageable);
        return itensEstoque.map(estoqueMapper::toDTO);
    }

    /*
     * ======================
     * Regra para Ajustes em Quantidade
     * ======================
     */

    /**
     * Ajusta a quantidade de um item de estoque (positivo para entrada, negativo para saída).
     *
     * @param cnpj    CNPJ da oficina
     * @param idItem  Identificador do item
     * @param quantidade Quantidade a ajustar (pode ser negativa)
     * @return DTO do item após ajuste
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ItemEstoqueNaoLocalizadoException se o item não for localizado
     * @throws EstoqueInsuficienteException se o ajuste resultar em quantidade negativa
     */
    public EstoqueDTO ajustarQuantidadeItemEstoque(String cnpj, Long idItem, int quantidade) {
        Oficina oficina = localizarOficina(cnpj);
        Estoque item = localizarItemEstoque(idItem, oficina);

        int novaQuantidade = item.getEstoqueAtual() + quantidade;
        if (novaQuantidade < 0) {
            throw new EstoqueInsuficienteException("Quantidade insuficiente em estoque");
        }

        item.setEstoqueAtual(novaQuantidade);
        estoqueRepository.save(item);
        return estoqueMapper.toDTO(item);
    }

    /*
     * ======================
     * Regra para identificação de Estoque baixo
     * ======================
     */

    /**
     * Lista itens com estoque abaixo do mínimo (paginado).
     *
     * @param cnpj     CNPJ da oficina
     * @param pageable Informações de paginação
     * @return Página de DTOs dos itens com estoque baixo
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public Page<EstoqueDTO> itensEstoqueBaixo(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        Page<Estoque> itens = estoqueRepository.buscarItensAbaixoDoMinimo(oficina, pageable);
        return itens.map(estoqueMapper::toDTO);
    }

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
        Oficina oficina = oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina não encontrada"));

        return oficina;
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
        Estoque itemEstoque = estoqueRepository.findByCodigoAndOficina(codigoItem, oficina)
                .orElseThrow(() -> new ItemEstoqueNaoLocalizadoException("Item de estoque não encontrado"));
        return itemEstoque;
    }
}