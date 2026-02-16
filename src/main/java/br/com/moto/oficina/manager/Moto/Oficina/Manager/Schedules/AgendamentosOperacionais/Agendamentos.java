package br.com.moto.oficina.manager.Moto.Oficina.Manager.Schedules.AgendamentosOperacionais;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.OrdemServico;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico.Status;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OrdemServicoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class Agendamentos {

    private final OrdemServicoRepository ordemServicoRepository;

    public Agendamentos(OrdemServicoRepository ordemServicoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void verificarOrdemServicoAtrasada() {

        List<OrdemServico> ordensAtrasadas = ordemServicoRepository.findByStatusInAndPrazoEntregaBefore(
                statusOS(),
                LocalDateTime.now()
        );

        ordensAtrasadas.forEach(ordem -> {
            ordem.setStatus(Status.ATRASADA);
            ordemServicoRepository.save(ordem);
        });
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void limpezaOSAberta(){

        List<OrdemServico> osAbertas = ordemServicoRepository.findByStatusAndDataAberturaBefore(
                Status.ABERTA,
                LocalDateTime.now().minusDays(30)
        );
    }

    private List<Status> statusOS(){
        return List.of(
                Status.ABERTA,
                Status.EM_EXECUCAO,
                Status.AGUARDANDO_PECA
        );
    }
}
