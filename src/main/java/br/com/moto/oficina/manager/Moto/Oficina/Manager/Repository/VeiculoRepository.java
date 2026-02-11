package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Veiculo;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Optional<Veiculo> findByPlacaAndOficina(String placa, Oficina oficina);

    Page<Veiculo> findByClienteNomeContainingIgnoreCaseAndOficina(String nome, Oficina oficina, Pageable pageable);

    Page<Veiculo> findAllByOficina(Oficina oficina, Pageable pageable);

    Page<Veiculo> findByAtivoAndOficina(boolean b, Oficina oficina, Pageable pageable);

    boolean existsByPlacaAndOficina(String placa, Oficina oficina);

}
