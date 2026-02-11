package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import java.util.Optional;

import org.mapstruct.Mapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Estoque;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    @Mapping(source = "estoque.descricao", target = "descricao")
    Optional<Estoque> findByIdAndOficina(Long id, Oficina oficina);

  Page<Estoque> findByAtivoAndOficina(boolean b, Oficina oficina, Pageable pageable);

  Page<Estoque> findByDescricaoContainingIgnoreCaseAndOficina(String nomeItem, Oficina oficina, Pageable pageable);

  boolean existsByCodigoAndOficina(String nome, Oficina oficina);

  @Query("""
          SELECT e
          FROM Estoque e
          WHERE e.estoqueAtual < e.estoqueMinimo
            AND e.oficina = :oficina
            AND e.ativo = true
      """)
  Page<Estoque> buscarItensAbaixoDoMinimo(@Param("oficina") Oficina oficina, Pageable pageable);

  Page<Estoque> findByOficina(Oficina oficina, Pageable pageable);

  Optional<Estoque> findByCodigoAndOficina(String codigoItem, Oficina oficina);
}
