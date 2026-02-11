package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.ClienteNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Funcionario.FuncionarioNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.OS.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Veiculo.VeiculoNaoLocalizadoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico.Status;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.OrdemServico.OrdemServicoMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.*;

@Service
@Transactional
public class OrdemServicoService {

    private final OrdemServicoRepository osRepository;
    private final OficinaRepository oficinaRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EstoqueRepository estoqueRepository;
    private final ServicoRepository servicoRepository;
    private final OrdemServicoMapper osMapper;

    /**
     * Construtor do serviço de ordens de serviço.
     *
     * @param osRepository        Repositório de ordens de serviço
     * @param oficinaRepository   Repositório de oficinas
     * @param clienteRepository   Repositório de clientes
     * @param veiculoRepository   Repositório de veículos
     * @param funcionarioRepository Repositório de funcionários
     * @param estoqueRepository   Repositório de estoque
     * @param servicoRepository   Repositório de serviços
     * @param osMapper            Mapper para conversão entre entidade e DTO
     */
    public OrdemServicoService(
            OrdemServicoRepository osRepository,
            OficinaRepository oficinaRepository,
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            FuncionarioRepository funcionarioRepository,
            EstoqueRepository estoqueRepository,
            ServicoRepository servicoRepository,
            OrdemServicoMapper osMapper) {

        this.osRepository = osRepository;
        this.oficinaRepository = oficinaRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.estoqueRepository = estoqueRepository;
        this.servicoRepository = servicoRepository;
        this.osMapper = osMapper;
    }

    // =========================================================
    // CRIAR ORDEM DE SERVIÇO
    // =========================================================

    /**
     * Cria uma nova ordem de serviço vinculada a uma oficina, cliente, veículo e funcionário responsável.
     *
     * @param cnpj                    CNPJ da oficina
     * @param cpfCnpj                 CPF ou CNPJ do cliente
     * @param cpfFuncionarioResponsavel CPF do funcionário responsável
     * @param dto                     DTO com os dados da ordem
     * @return DTO da ordem de serviço criada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws ClienteNaoLocalizadoException se o cliente não for encontrado
     * @throws VeiculoNaoLocalizadoException se o veículo não for encontrado
     * @throws FuncionarioNaoLocalizadoException se o funcionário não for encontrado
     */
    public OrdemServicoDTO criarOrdemServico(String cnpj, String cpfCnpj,String cpfFuncionarioResponsavel, CriarOrdemServicoDTO dto) {

        Oficina oficina = localizarOficina(cnpj);
        Cliente cliente = localizarCliente(oficina, cpfCnpj);
        Veiculo veiculo = localizarVeiculo(oficina, dto.placaVeiculo());
        Funcionario funcionario = localizarFuncionario(oficina, cpfFuncionarioResponsavel);

        OrdemServico os = osMapper.toEntity(dto);
        os.setNumero(gerarNumeroOS(oficina));
        os.setOficina(oficina);
        os.setCliente(cliente);
        os.setVeiculo(veiculo);
        os.setFuncionarioResponsavel(funcionario);
        os.setDataAbertura(LocalDateTime.now());
        os.setStatus(Status.ABERTA);
        os.setAtivo(true);
        os.setDescricaoProblema(dto.descricaoProblema());

        // LISTAS MUTÁVEIS
        os.setServicos(new ArrayList<>());
        os.setProdutos(new ArrayList<>());

        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    // =========================================================
    // ADICIONAR SERVIÇO
    // =========================================================

    /**
     * Adiciona um serviço à ordem de serviço.
     *
     * @param cnpj  CNPJ da oficina
     * @param osId  Identificador da ordem de serviço
     * @param dto   DTO contendo id do serviço a ser adicionado
     * @return DTO da ordem atualizada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     * @throws StatusOSException             se a OS não puder ser alterada no status atual
     * @throws ServicoNaoLocalizadoException se o serviço não for encontrado
     * @throws ServicoDuplicadoOSException   se o serviço já estiver adicionado à OS
     */
    public OrdemServicoDTO adicionarServicoOS(String cnpj, Long osId, AdicionarServicoOSDTO dto) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);
        validarOSAlteravel(os);

        Servico servico = servicoRepository.findByIdAndOficina(dto.idServico(), oficina)
                .orElseThrow(() -> new ServicoNaoLocalizadoException("Serviço não encontrado"));

        boolean jaExiste = os.getServicos().stream()
                .anyMatch(s -> s.getServico().getId().equals(servico.getId()));

        if (jaExiste) {
            throw new ServicoDuplicadoOSException("Serviço já adicionado a esta OS");
        }

        ItemServicoOS item = new ItemServicoOS();
        item.setServico(servico);
        item.setQuantidade(1);
        item.setValorUnitario(servico.getValorMaoDeObra());
        item.setOrdemServico(os);

        os.getServicos().add(item);
        atualizarValoresOS(os);

        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    // =========================================================
    // ADICIONAR PRODUTO / ESTOQUE
    // =========================================================

    /**
     * Adiciona um produto (item de estoque) à ordem de serviço e atualiza o estoque.
     *
     * @param cnpj  CNPJ da oficina
     * @param osId  Identificador da ordem de serviço
     * @param dto   DTO contendo id do produto e quantidade
     * @return DTO da ordem atualizada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     * @throws StatusOSException             se a OS não puder ser alterada no status atual
     * @throws ProdutoNaoLocalizadoException se o produto não for encontrado
     * @throws StatusProdutoException        se o produto estiver inativo
     * @throws QuantidadeEstoqueException    se a quantidade solicitada for maior que o estoque disponível
     */
    public OrdemServicoDTO adicionarProdutoOS(String cnpj, Long osId, AdicionarProdutoOSDTO dto) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);
        validarOSAlteravel(os);

        Estoque produto = estoqueRepository.findByIdAndOficina(dto.idProduto(), oficina)
                .orElseThrow(() -> new ProdutoNaoLocalizadoException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new StatusProdutoException("Produto inativo");
        }

        if (produto.getEstoqueAtual() < dto.quantidade()) {
            throw new QuantidadeEstoqueException("Quantidade solicitada maior que o estoque disponível");
        }

        ItemEstoqueOS item = new ItemEstoqueOS();
        item.setProduto(produto);
        item.setQuantidade(dto.quantidade());
        item.setValorUnitario(produto.getPrecoVenda());
        item.setValorTotal(produto.getPrecoVenda().multiply(BigDecimal.valueOf(dto.quantidade())));
        item.setOrdemServico(os);

        produto.setEstoqueAtual(produto.getEstoqueAtual() - dto.quantidade());

        os.getProdutos().add(item);
        atualizarValoresOS(os);

        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    // =========================================================
    // REMOVER SERVIÇO / PRODUTO
    // =========================================================

    /**
     * Remove um item de serviço da ordem.
     *
     * @param cnpj          CNPJ da oficina
     * @param osId          Identificador da ordem de serviço
     * @param itemServicoId Identificador do item de serviço na OS
     * @return DTO da ordem atualizada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     * @throws StatusOSException             se a OS não puder ser alterada no status atual
     */
    public OrdemServicoDTO removerServicoOS(String cnpj, Long osId, Long itemServicoId) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);
        validarOSAlteravel(os);

        os.getServicos().removeIf(s -> s.getId().equals(itemServicoId));
        atualizarValoresOS(os);

        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    /**
     * Remove um item de produto da ordem e restaura o estoque.
     *
     * @param cnpj         CNPJ da oficina
     * @param osId         Identificador da ordem de serviço
     * @param itemEstoqueId Identificador do item de estoque na OS
     * @return DTO da ordem atualizada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     * @throws StatusOSException             se a OS não puder ser alterada no status atual
     * @throws ProdutoNaoLocalizadoException se o item não for encontrado na OS
     */
    public OrdemServicoDTO removerProdutoOS(String cnpj, Long osId, Long itemEstoqueId) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);
        validarOSAlteravel(os);

        ItemEstoqueOS item = os.getProdutos().stream()
                .filter(p -> p.getId().equals(itemEstoqueId))
                .findFirst()
                .orElseThrow(() -> new ProdutoNaoLocalizadoException("Produto não encontrado na OS"));

        Estoque produto = item.getProduto();
        produto.setEstoqueAtual(produto.getEstoqueAtual() + item.getQuantidade());

        os.getProdutos().remove(item);
        atualizarValoresOS(os);

        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    // =========================================================
    // STATUS DA OS
    // =========================================================

    /**
     * Inicia a execução da OS (altera status para EM_EXECUCAO).
     *
     * @param cnpj CNPJ da oficina
     * @param osId Identificador da ordem de serviço
     * @return DTO da ordem atualizada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     * @throws StatusOSException             se a OS não estiver em ABERTA
     */
    public OrdemServicoDTO iniciarOS(String cnpj, Long osId) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);

        if (os.getStatus() != Status.ABERTA) {
            throw new StatusOSException("Somente OS em ABERTA podem ser iniciadas");
        }

        os.setStatus(Status.EM_EXECUCAO);
        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    /**
     * Coloca a OS no status AGUARDANDO_PECA.
     *
     * @param cnpj CNPJ da oficina
     * @param osId Identificador da ordem de serviço
     * @return DTO da ordem atualizada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     * @throws StatusOSException             se a OS não estiver em EM_EXECUCAO
     */
    public OrdemServicoDTO aguardarPecaOS(String cnpj, Long osId) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);

        if (os.getStatus() != Status.EM_EXECUCAO) {
            throw new StatusOSException("Somente OS em execução podem aguardar peça");
        }

        os.setStatus(Status.AGUARDANDO_PECA);
        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    /**
     * Finaliza a OS, aplicando dados de conclusão, desconto e valores finais.
     *
     * @param cnpj           CNPJ da oficina
     * @param osId           Identificador da ordem de serviço
     * @param finalizarOsDTO DTO com dados finais (quilometragem, diagnóstico, pagamento, parcelas, garantia, desconto)
     * @return DTO da ordem finalizada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     * @throws StatusOSException             se a OS já estiver finalizada
     */
    public OrdemServicoDTO finalizarOS(String cnpj, Long osId, FinalizarOsDTO finalizarOsDTO) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);

        if (os.getStatus() == Status.FINALIZADA) {
            throw new StatusOSException("OS já finalizada");
        }

        os.setQuilometragemSaida(finalizarOsDTO.quilometragemSaida());
        os.setDiagnostico(finalizarOsDTO.diagnostico());
        os.setFormaPagamento(finalizarOsDTO.formaPagamento());
        os.setParcelas(finalizarOsDTO.parcelas());
        os.setGarantiaDias(finalizarOsDTO.garantiaDias());

        if(finalizarOsDTO.desconto() != null) {
            os.setDesconto(finalizarOsDTO.desconto());
        }

        os.setStatus(Status.FINALIZADA);
        os.setDataConclusao(LocalDateTime.now());

        atualizarValoresOS(os);
        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    /**
     * Cancela a OS e restaura os estoques dos produtos utilizados.
     *
     * @param cnpj CNPJ da oficina
     * @param osId Identificador da ordem de serviço
     * @return DTO da ordem cancelada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     * @throws StatusOSException             se a OS já estiver finalizada
     */
    public OrdemServicoDTO cancelarOS(String cnpj, Long osId) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);

        if (os.getStatus() == Status.FINALIZADA) {
            throw new StatusOSException("Não é possível cancelar OS finalizada");
        }

        os.setStatus(Status.CANCELADA);

        os.getProdutos().forEach(item -> {
            Estoque produto = item.getProduto();
            produto.setEstoqueAtual(produto.getEstoqueAtual() + item.getQuantidade());
        });

        osRepository.save(os);
        return osMapper.toDTO(os);
    }

    // =========================================================
    // CONSULTAS
    // =========================================================

    /**
     * Lista todas as ordens de serviço da oficina (paginado).
     *
     * @param cnpj     CNPJ da oficina
     * @param pageable Informações de paginação
     * @return Página de DTOs das ordens encontradas
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se nenhuma ordem for encontrada
     */
    public Page<OrdemServicoDTO> listarOrdensDeServico(String cnpj, Pageable pageable) {

        Oficina oficina = localizarOficina(cnpj);
        Page<OrdemServico> ordens = osRepository.findByOficina(oficina, pageable);

        if (ordens.isEmpty()) {
            throw new OSNaoLocalizadaException("Nenhuma ordem de serviço encontrada");
        }

        return ordens.map(osMapper::toDTO);
    }

    /**
     * Lista ordens de serviço filtrando por status.
     *
     * @param cnpj     CNPJ da oficina
     * @param status   Status desejado (pode ser null para todos)
     * @param pageable Informações de paginação
     * @return Página de DTOs das ordens encontradas
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se nenhuma ordem for encontrada com o status informado
     */
    public Page<OrdemServicoDTO> listarOrdensDeServicoPorStatus(String cnpj, Status status, Pageable pageable) {

        Oficina oficina = localizarOficina(cnpj);
        Page<OrdemServico> ordens = osRepository.findByOficinaAndStatus(oficina, status, pageable);

        if (ordens.isEmpty()) {
            throw new OSNaoLocalizadaException("Nenhuma OS encontrada com o status informado");
        }

        return ordens.map(osMapper::toDTO);
    }

    /**
     * Recupera uma OS por id.
     *
     * @param cnpj CNPJ da oficina
     * @param osId Identificador da ordem de serviço
     * @return DTO da ordem encontrada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     * @throws OSNaoLocalizadaException      se a ordem não for encontrada
     */
    public OrdemServicoDTO listarOrdensDeServicoPorId(String cnpj, Long osId) {

        Oficina oficina = localizarOficina(cnpj);
        OrdemServico os = localizarOS(osId, oficina);

        return osMapper.toDTO(os);
    }

    /**
     * Lista ordens de serviço de um funcionário, opcionalmente filtrando por status.
     *
     * @param cnpj           CNPJ da oficina
     * @param cpfFuncionario CPF do funcionário
     * @param status         Status desejado (pode ser null)
     * @param pageable       Informações de paginação
     * @return Página de DTOs das ordens encontradas
     * @throws OficinaNaoLocalizadaException   se a oficina não for encontrada
     * @throws FuncionarioNaoLocalizadoException se o funcionário não for encontrado
     * @throws OSNaoLocalizadaException        se nenhuma ordem for encontrada para o critério
     */
    public Page<OrdemServicoDTO> listarOsDoFuncionario(
            String cnpj,
            String cpfFuncionario,
            Status status,
            Pageable pageable
    ) {
        Oficina oficina = localizarOficina(cnpj);
        Funcionario funcionario = localizarFuncionario(oficina, cpfFuncionario);

        Page<OrdemServico> os;

        if (status == null) {
            os = osRepository.findByOficinaAndFuncionarioResponsavel(oficina, funcionario, pageable);
        } else {
            os = osRepository.findByOficinaAndFuncionarioResponsavelAndStatus(
                    oficina, funcionario, status, pageable
            );
        }

        if (os.isEmpty()) {
            String msg = (status == null)
                    ? "Nenhuma ordem de serviço encontrada para o funcionário informado"
                    : "Nenhuma ordem de serviço com status " + status + " encontrada para o funcionário informado";

            throw new OSNaoLocalizadaException(msg);
        }

        return os.map(osMapper::toDTO);
    }


    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    /**
     * Localiza uma ordem de serviço pelo id e oficina.
     *
     * @param osId   Identificador da ordem
     * @param oficina Oficina onde buscar
     * @return Entidade OrdemServico encontrada
     * @throws OSNaoLocalizadaException se a OS não for encontrada
     */
    private OrdemServico localizarOS(Long osId, Oficina oficina) {
        return osRepository.findByIdAndOficina(osId, oficina)
                .orElseThrow(() -> new OSNaoLocalizadaException("Ordem de serviço não encontrada"));
    }

    /**
     * Localiza uma oficina pelo CNPJ.
     *
     * @param cnpj CNPJ da oficina
     * @return Entidade Oficina encontrada
     * @throws OficinaNaoLocalizadaException se a oficina não for encontrada
     */
    private Oficina localizarOficina(String cnpj) {
        return oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new OficinaNaoLocalizadaException("Oficina não encontrada"));
    }

    /**
     * Localiza um cliente pela identificação (CPF/CNPJ) e oficina.
     *
     * @param oficina   Oficina onde buscar
     * @param cpfCnpj   CPF ou CNPJ do cliente
     * @return Entidade Cliente encontrada
     * @throws ClienteNaoLocalizadoException se o cliente não for encontrado
     */
    private Cliente localizarCliente(Oficina oficina, String cpfCnpj) {
        return clienteRepository.findByCpfCnpjAndOficina(cpfCnpj, oficina)
                .orElseThrow(() -> new ClienteNaoLocalizadoException("Cliente não encontrado"));
    }

    /**
     * Localiza um veículo pela placa e oficina.
     *
     * @param oficina Oficina onde buscar
     * @param placa   Placa do veículo
     * @return Entidade Veiculo encontrada
     * @throws VeiculoNaoLocalizadoException se o veículo não for encontrado
     */
    private Veiculo localizarVeiculo(Oficina oficina, String placa) {
        return veiculoRepository.findByPlacaAndOficina(placa, oficina)
                .orElseThrow(() -> new VeiculoNaoLocalizadoException("Veículo não encontrado"));
    }

    /**
     * Localiza um funcionário pelo CPF na oficina.
     *
     * @param oficina Oficina onde buscar
     * @param cpf     CPF do funcionário
     * @return Entidade Funcionario encontrada
     * @throws FuncionarioNaoLocalizadoException se o funcionário não for encontrado
     */
    private Funcionario localizarFuncionario(Oficina oficina, String cpf) {
        return funcionarioRepository.findByCpfAndOficina(cpf, oficina)
                .orElseThrow(() -> new FuncionarioNaoLocalizadoException("Funcionário não encontrado"));
    }

    /**
     * Valida se a OS pode ser alterada (não finalizada nem cancelada).
     *
     * @param os Ordem de serviço a validar
     * @throws StatusOSException se a OS estiver finalizada ou cancelada
     */
    private void validarOSAlteravel(OrdemServico os) {
        if (os.getStatus() == Status.FINALIZADA || os.getStatus() == Status.CANCELADA) {
            throw new StatusOSException("Não é possível alterar OS finalizada ou cancelada");
        }
    }

    /**
     * Recalcula os valores totais da OS (serviços, produtos e desconto).
     *
     * @param os Ordem de serviço cujos valores serão atualizados
     */
    private void atualizarValoresOS(OrdemServico os) {

        BigDecimal valorServicos = os.getServicos().stream()
                .map(s -> s.getValorUnitario().multiply(BigDecimal.valueOf(s.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorProdutos = os.getProdutos().stream()
                .map(p -> p.getValorUnitario().multiply(BigDecimal.valueOf(p.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal desconto = os.getDesconto() != null ? os.getDesconto() : BigDecimal.ZERO;

        os.setValorServicos(valorServicos);
        os.setValorProdutos(valorProdutos);
        os.setValorTotal(valorServicos.add(valorProdutos).subtract(desconto));
    }

    /**
     * Gera um número único para a OS com base no id da oficina e contagem atual.
     *
     * @param oficina Oficina para a qual gerar o número
     * @return String com o número gerado (ex: OS-<oficinaId>-00001)
     */
    private String gerarNumeroOS(Oficina oficina) {
        long proximoNumero = osRepository.count() + 1;
        return "OS-" + oficina.getId() + "-" + String.format("%05d", proximoNumero);
    }
}