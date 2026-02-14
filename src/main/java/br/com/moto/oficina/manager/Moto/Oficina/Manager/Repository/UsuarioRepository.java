package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCnpj(String cnpj);

    Optional<Boolean> existsByCnpj(String cnpj);
}
