package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long> {

    Optional<Oficina> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
    
}
