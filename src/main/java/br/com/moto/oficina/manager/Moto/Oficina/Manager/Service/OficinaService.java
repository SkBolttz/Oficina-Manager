package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Api.ReceitaWS;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.AtualizarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.CadastrarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaBuscaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Endereco;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RecursoNaoEncontradoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RegraNegocioException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Endereco.EnderecoMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Oficina.OficinaMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OficinaService {

    private final OficinaRepository oficinaRepository;
    private final ReceitaWS receitaWS;
    private final OficinaMapper oficinaMapper;
    private final EnderecoMapper enderecoMapper;
    private final EnderecoService enderecoService;

    public OficinaService(
            OficinaRepository oficinaRepository,
            ReceitaWS receitaWS,
            OficinaMapper oficinaMapper,
            EnderecoMapper enderecoMapper,
            EnderecoService enderecoService) {

        this.oficinaRepository = oficinaRepository;
        this.receitaWS = receitaWS;
        this.oficinaMapper = oficinaMapper;
        this.enderecoMapper = enderecoMapper;
        this.enderecoService = enderecoService;
    }

    /*
     * ======================
     * Cadastro
     * ======================
     */
    public void cadastrarOficina(CadastrarOficinaDTO dto) {

        String cnpjNormalizado = normalizarCnpj(dto.cnpj());

        validarFormatoCnpj(cnpjNormalizado);
        validarCnpjNaoCadastrado(cnpjNormalizado);

        OficinaDTO dadosReceita = consultarCnpj(cnpjNormalizado);
        Oficina oficina = oficinaMapper.toEntity(dadosReceita);

        Endereco endereco = enderecoService
                .cadastrarEndereco(enderecoMapper.toEntity(dto.endereco()));

        oficina.setCnpj(cnpjNormalizado);
        oficina.setEndereco(endereco);
        oficina.setDataCadastro(LocalDate.now());
        oficina.setAtivo(true);

        oficinaRepository.save(oficina);
    }

    /*
     * ======================
     * Atualização
     * ======================
     */
    public void atualizarDadosOficina(String cnpj, AtualizarOficinaDTO dto) {

        System.out.println("Passou por aqui UM");

        String cnpjNormalizado = normalizarCnpj(cnpj);
        validarFormatoCnpj(cnpjNormalizado);

        System.out.println("Passou por aqui DOIS");

        Oficina oficina = oficinaRepository
                .findByCnpj(cnpjNormalizado)
                .orElseThrow(() -> new RegraNegocioException("Oficina não encontrada"));

        System.out.println("Passou por aqui TRES");

        if (dto.nome() != null) {
            oficina.setNome(dto.nome());
        }

        if (dto.situacao() != null) {
            oficina.setSituacao(dto.situacao());
        }

        if (dto.natureza_juridica() != null) {
            oficina.setNatureza_juridica(dto.natureza_juridica());
        }

        if (dto.porte() != null) {
            oficina.setPorte(dto.porte());
        }

        if (dto.email() != null) {
            oficina.setEmail(dto.email());
        }

        if (dto.telefone() != null) {
            oficina.setTelefone(dto.telefone());
        }

        if (dto.endereco() != null) {
            enderecoService.atualizarEnderecoOficina(oficina, dto);
        }

        System.out.println(oficina);
        oficinaRepository.save(oficina);
    }

    /*
     * ======================
     * Consulta
     * ======================
     */
    public OficinaBuscaDTO localizarOficina(String cnpj) {

        String cnpjNormalizado = normalizarCnpj(cnpj);
        validarFormatoCnpj(cnpjNormalizado);

        Oficina oficina = oficinaRepository
                .findByCnpj(cnpjNormalizado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oficina não encontrada"));

        return oficinaMapper.toDtoBusca(oficina);
    }

    /*
     * ======================
     * Métodos privados
     * ======================
     */

    private OficinaDTO consultarCnpj(String cnpjNormalizado) {
        try {
            OficinaDTO oficina = receitaWS.buscarCnpj(cnpjNormalizado);

            if (oficina == null) {
                throw new RegraNegocioException("CNPJ não encontrado na Receita Federal");
            }

            return oficina;
        } catch (Exception e) {
            throw new RegraNegocioException(
                    "Erro ao consultar a Receita Federal. Tente novamente mais tarde");
        }
    }

    private void validarCnpjNaoCadastrado(String cnpjNormalizado) {
        if (oficinaRepository.existsByCnpj(cnpjNormalizado)) {
            throw new RegraNegocioException(
                    "Já existe uma oficina cadastrada com este CNPJ");
        }
    }

    private void validarFormatoCnpj(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
            throw new RegraNegocioException(
                    "CNPJ deve conter exatamente 14 dígitos numéricos");
        }
    }

    private String normalizarCnpj(String cnpj) {
        return cnpj.replaceAll("\\D", "");
    }
}
