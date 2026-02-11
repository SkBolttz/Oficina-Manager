package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.time.LocalDate;

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
            throw new RegraNegocioException("Estoque atual nao pode ser negativo");
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
    public EstoqueDTO ativarItemEstoque(String cnpj, Long idItem) {
        Oficina oficina = localizarOficina(cnpj);
        Estoque itemEstoque = localizarItemEstoque(idItem, oficina);
        itemEstoque.setAtivo(true);
        estoqueRepository.save(itemEstoque);
        return estoqueMapper.toDTO(itemEstoque);
    }

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
    public EstoqueDTO buscarItemEstoque(String cnpj, String codigoItem) {
        Oficina oficina = localizarOficina(cnpj);

        System.out.println("CNPJ da oficina: " + oficina.getCnpj() + ", Codigo do item: " + codigoItem);
        Estoque itemEstoque = localizarItemCodigo(codigoItem, oficina);
        return estoqueMapper.toDTO(itemEstoque);
    }

    public Page<EstoqueDTO> buscarItemEstoquePorNome(String cnpj, String nomeItem, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        Page<Estoque> itensEstoque = estoqueRepository.findByDescricaoContainingIgnoreCaseAndOficina(nomeItem, oficina,
                pageable);

        if (itensEstoque.isEmpty()) {
            throw new RegraNegocioException("Item de estoque não encontrado");
        }

        return itensEstoque.map(estoqueMapper::toDTO);
    }

    public Page<EstoqueDTO> buscarItensEstoqueAtivos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        Page<Estoque> itensEstoqueAtivos = estoqueRepository.findByAtivoAndOficina(true, oficina, pageable);
        return itensEstoqueAtivos.map(estoqueMapper::toDTO);
    }

    public Page<EstoqueDTO> buscarItensEstoqueInativos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        Page<Estoque> itensEstoqueInativos = estoqueRepository.findByAtivoAndOficina(false, oficina, pageable);
        return itensEstoqueInativos.map(estoqueMapper::toDTO);
    }

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

    // Ajustar quantidade de estoque (positivo para entrada, negativo para saída)
    public EstoqueDTO ajustarQuantidadeItemEstoque(String cnpj, Long idItem, int quantidade) {
        Oficina oficina = localizarOficina(cnpj);
        Estoque item = localizarItemEstoque(idItem, oficina);

        int novaQuantidade = item.getEstoqueAtual() + quantidade;
        if (novaQuantidade < 0) {
            throw new RegraNegocioException("Quantidade insuficiente em estoque");
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

    // Listar itens com estoque abaixo do mínimo
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

    private Oficina localizarOficina(String cnpj) {
        Oficina oficina = oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RegraNegocioException("Oficina não encontrada"));

        if (oficina == null) {
            throw new RegraNegocioException("Oficina não encontrada");
        }

        return oficina;
    }

    private Estoque localizarItemEstoque(Long id, Oficina oficina) {
        return estoqueRepository.findByIdAndOficina(id, oficina)
                .orElseThrow(() -> new RegraNegocioException("Item de estoque não encontrado"));
    }

    private void validarDuplicidade(String nome, Oficina oficina) {
        boolean existe = estoqueRepository.existsByCodigoAndOficina(nome, oficina);
        if (existe) {
            throw new RegraNegocioException("Já existe um item de estoque com esse código na oficina");
        }
    }

    private Estoque localizarItemCodigo(String codigoItem, Oficina oficina ) {
        Estoque itemEstoque = estoqueRepository.findByCodigoAndOficina(codigoItem, oficina)
                .orElseThrow(() -> new RegraNegocioException("Item de estoque não encontrado"));
        return itemEstoque;
    }
}
