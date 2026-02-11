package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import java.util.Optional;

import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Estoque;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long>{

    Page<Servico> findByOficina(Oficina oficina, Pageable pageable);

    Page<Servico> findByOficinaAndAtivo(Oficina oficina, boolean b, Pageable pageable);

    Page<Servico> findByDescricaoContainingIgnoreCaseAndOficina(String descricao, Oficina oficina, Pageable pageable);

    boolean existsByDescricaoIgnoreCaseAndOficinaId(String descricao, Long idOficina);

    @Mapping(source = "servico.descricao", target = "descricao")
    Optional<Servico> findByIdAndOficina(Long idServico, Oficina oficina);
    
}
