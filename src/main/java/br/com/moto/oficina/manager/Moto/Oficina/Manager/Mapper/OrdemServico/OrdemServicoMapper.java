package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.OrdemServico;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico.CriarOrdemServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Cliente.ClienteMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Funcionario.FuncionarioMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.ItemEstoqueOS.ItemEstoqueOSMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.ItemServicoOS.ItemServicoOSMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Oficina.OficinaMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Servico.ServicoMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Veiculo.VeiculoMapper;
import org.mapstruct.Mapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico.OrdemServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.OrdemServico;

@Mapper(
        componentModel = "spring",
        uses = {
                ClienteMapper.class,
                VeiculoMapper.class,
                FuncionarioMapper.class,
                OficinaMapper.class,
                ItemServicoOSMapper.class,
                ItemEstoqueOSMapper.class
        }
)
public interface OrdemServicoMapper {
    
    OrdemServicoDTO toDTO(OrdemServico ordemServico);
    OrdemServico toEntity(CriarOrdemServicoDTO ordemServicoDTO);
}
