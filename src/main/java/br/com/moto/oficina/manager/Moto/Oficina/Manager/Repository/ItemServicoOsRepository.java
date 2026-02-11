package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.ItemServicoOS;

@Repository
public interface ItemServicoOsRepository extends JpaRepository<ItemServicoOS, Long> {
    
}
