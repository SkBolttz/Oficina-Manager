package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.ItemEstoqueOS;

@Repository
public interface ItemEstoqueOsRepository extends JpaRepository<ItemEstoqueOS, Long>{
    
}
