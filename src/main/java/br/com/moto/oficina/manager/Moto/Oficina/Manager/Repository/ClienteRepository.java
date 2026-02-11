package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Cliente;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByEmail(String email);

    Cliente cpfCnpj(String cpfCnpj);

    Optional<Cliente> findByCpfCnpj(String cpfCnpj);

    Cliente findByNome(String nomeCliente);

    List<Cliente> findByAtivo(boolean b);

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    Optional<Cliente> findByCpfCnpjAndOficina(String cpfCnpj, Oficina oficina);

    Object findByEmailAndOficina(String email, Oficina oficina);

    Page<Cliente> findByNomeContainingIgnoreCaseAndOficina(String nome, Oficina oficina, Pageable pageable);

	Page<Cliente> findByAtivoAndOficina(boolean b, Oficina oficina, Pageable pageable);

    Page<Cliente> findAllByOficina(Oficina oficina, Pageable pageable);

    boolean existsByCpfCnpjAndOficina(String cpfCnpjNormalizado, Oficina oficina);
    
}
