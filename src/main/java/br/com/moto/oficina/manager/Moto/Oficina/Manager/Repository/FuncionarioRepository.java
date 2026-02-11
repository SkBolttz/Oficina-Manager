package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Funcionario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

    Funcionario findByEmail(String email);

    Funcionario findByEmailAndOficina(String email, Oficina oficina);

    Optional<Funcionario> findByCpfAndOficina(String cpf, Oficina oficina);

    Page<Funcionario> findByNomeContainingIgnoreCaseAndOficina(String nomeFuncionario, Oficina oficina, Pageable pageable);

    Page<Funcionario> findByOficina(Oficina oficina, Pageable pageable);

    Page<Funcionario> findByAtivoAndOficina(boolean b, Oficina oficina, Pageable pageable);

    boolean existsByCpfAndOficina(String cpfNormalizado, Oficina oficina);
    
}
