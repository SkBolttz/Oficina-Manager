package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Funcionario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.OrdemServico;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    Optional<OrdemServico> findByIdAndOficina(Long osId, Oficina oficina);

    Page<OrdemServico> findByOficina(Oficina oficina, Pageable pageable);

    Page<OrdemServico> findByOficinaAndStatus(Oficina oficina, Status status, Pageable pageable);

    Page<OrdemServico> findByOficinaAndFuncionarioResponsavel(Oficina oficina, Funcionario funcionario, Pageable pageable);

    Page<OrdemServico> findByOficinaAndFuncionarioResponsavelAndStatus(Oficina oficina, Funcionario funcionario, Status status, Pageable pageable);

    List<OrdemServico> findByStatusInAndPrazoEntregaBefore(List<Status> status, LocalDateTime localDateTime);

    List<OrdemServico> findByStatusAndDataAberturaBefore(Status status, LocalDateTime localDateTime);
}
