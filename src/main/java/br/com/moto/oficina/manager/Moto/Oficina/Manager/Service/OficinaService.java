// language: java
package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Usuario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.API.ErroReceitaFederalException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.CNPJInvalidoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.DuplicidadeCnpjException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.UsuarioRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Util.ObterUsuarioLogado;
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
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor do serviço de oficina.
     *
     * @param oficinaRepository Repositório de oficinas
     * @param receitaWS         Cliente para consulta de CNPJ (Receita Federal)
     * @param oficinaMapper     Mapper para conversão entre DTO e entidade Oficina
     * @param enderecoMapper    Mapper para conversão entre DTO e entidade Endereco
     * @param enderecoService   Serviço de endereço
     * @param usuarioRepository Repositório de usuários
     */
    public OficinaService(
            OficinaRepository oficinaRepository,
            ReceitaWS receitaWS,
            OficinaMapper oficinaMapper,
            EnderecoMapper enderecoMapper,
            EnderecoService enderecoService,
            UsuarioRepository usuarioRepository) {

        this.oficinaRepository = oficinaRepository;
        this.receitaWS = receitaWS;
        this.oficinaMapper = oficinaMapper;
        this.enderecoMapper = enderecoMapper;
        this.enderecoService = enderecoService;
        this.usuarioRepository = usuarioRepository;
    }

    /*
     * ======================
     * Cadastro
     * ======================
     */

    /**
     * Cadastra uma nova oficina.
     *
     * O método valida formato do CNPJ, verifica se já não existe uma oficina
     * cadastrada com o mesmo CNPJ, consulta os dados na Receita Federal, persiste
     * a oficina com endereço e altera o status do primeiro login.
     *
     * @param dto DTO com os dados para cadastro
     * @throws CNPJInvalidoException       se o CNPJ estiver em formato inválido
     * @throws DuplicidadeCnpjException    se já existir oficina com o mesmo CNPJ
     * @throws ErroReceitaFederalException se ocorrer erro ao consultar a Receita Federal
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

        Usuario user = buscarUsuario(dto.cnpj());
        user.setPrimeiroLogin(false);
        usuarioRepository.save(user);
    }

    /*
     * ======================
     * Atualização
     * ======================
     */

    /**
     * Atualiza os dados de uma oficina existente.
     *
     * Campos nulos no DTO são ignorados. Se o DTO contiver informação de endereço,
     * o serviço de endereço é utilizado para atualizar o endereço da oficina.
     *
     * @param cnpj CNPJ da oficina a ser atualizada
     * @param dto  DTO com os campos a serem atualizados
     * @throws CNPJInvalidoException         se o CNPJ informado estiver em formato inválido
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public void atualizarDadosOficina(String cnpj, AtualizarOficinaDTO dto) {

        String cnpjNormalizado = normalizarCnpj(cnpj);
        validarFormatoCnpj(cnpjNormalizado);

        Oficina oficina = oficinaRepository
                .findByCnpj(cnpjNormalizado)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina não encontrada"));

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

    /**
     * Localiza uma oficina pelo CNPJ.
     *
     * O CNPJ é normalizado e validado antes da busca.
     *
     * @param cnpj CNPJ da oficina (pode conter formatação)
     * @return DTO de busca da oficina localizada
     * @throws CNPJInvalidoException         se o CNPJ estiver em formato inválido
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    public OficinaBuscaDTO localizarOficina(String cnpj) {

        String cnpjNormalizado = normalizarCnpj(cnpj);
        validarFormatoCnpj(cnpjNormalizado);

        Oficina oficina = oficinaRepository
                .findByCnpj(cnpjNormalizado)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina não encontrada"));

        return oficinaMapper.toDtoBusca(oficina);
    }

    /*
     * ======================
     * Métodos privados
     * ======================
     */

    /**
     * Consulta os dados da Receita Federal para o CNPJ informado.
     *
     * @param cnpjNormalizado CNPJ já normalizado (apenas dígitos)
     * @return DTO com os dados retornados pela Receita Federal
     * @throws CNPJInvalidoException       se a Receita não retornar dados válidos para o CNPJ
     * @throws ErroReceitaFederalException se ocorrer erro na comunicação com a Receita Federal
     */
    private OficinaDTO consultarCnpj(String cnpjNormalizado) {
        try {
            OficinaDTO oficina = receitaWS.buscarCnpj(cnpjNormalizado);

            if (oficina == null) {
                throw new CNPJInvalidoException("CNPJ não encontrado na Receita Federal");
            }

            return oficina;
        } catch (Exception e) {
            throw new ErroReceitaFederalException(
                    "Erro ao consultar a Receita Federal. Tente novamente mais tarde");
        }
    }

    /**
     * Valida que não exista uma oficina cadastrada com o CNPJ informado.
     *
     * @param cnpjNormalizado CNPJ já normalizado (apenas dígitos)
     * @throws DuplicidadeCnpjException se já existir oficina com o mesmo CNPJ
     */
    private void validarCnpjNaoCadastrado(String cnpjNormalizado) {
        if (oficinaRepository.existsByCnpj(cnpjNormalizado)) {
            throw new DuplicidadeCnpjException(
                    "Já existe uma oficina cadastrada com este CNPJ");
        }
    }

    /**
     * Valida o formato do CNPJ.
     *
     * @param cnpj CNPJ já normalizado (apenas dígitos)
     * @throws CNPJInvalidoException se o CNPJ não contiver exatamente 14 dígitos numéricos
     */
    private void validarFormatoCnpj(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
            throw new CNPJInvalidoException(
                    "CNPJ deve conter exatamente 14 dígitos numéricos");
        }
    }

    /**
     * Normaliza o CNPJ removendo quaisquer caracteres não numéricos.
     *
     * @param cnpj CNPJ possivelmente formatado
     * @return String contendo apenas os dígitos do CNPJ
     */
    private String normalizarCnpj(String cnpj) {
        return cnpj.replaceAll("\\D", "");
    }

    /**
     * Busca um usuário pelo CNPJ.
     *
     * @param cnpj CNPJ do usuário a ser buscado
     * @return Usuario encontrado
     * @throws RecursoNaoEncontradoException se o usuário não for encontrado
     */
    private Usuario buscarUsuario(String cnpj){
        return usuarioRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }
}