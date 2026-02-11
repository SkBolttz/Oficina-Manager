package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.AtualizarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.CadastrarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.FuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Endereco;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Funcionario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RegraNegocioException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Endereco.EnderecoMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Funcionario.FuncionarioMapper;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.FuncionarioRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioMapper funcionarioMapper;
    private final OficinaRepository oficinaRepository;
    private final EnderecoService enderecoService;
    private final EnderecoMapper enderecoMapper;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, FuncionarioMapper funcionarioMapper,
            OficinaRepository oficinaRepository, EnderecoService enderecoService, EnderecoMapper enderecoMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioMapper = funcionarioMapper;
        this.oficinaRepository = oficinaRepository;
        this.enderecoService = enderecoService;
        this.enderecoMapper = enderecoMapper;
    }

    /*
     * ======================
     * Cadastrar
     * ======================
     */

    public FuncionarioDTO cadastrarFuncionario(String cnpj, CadastrarFuncionarioDTO dto) {

        Oficina oficina = localizarOficina(cnpj);

        String cpfNormalizado = normalizarCpf(dto.cpf());

        validarDuplicidadeEmail(oficina, dto.email());
        validarDuplicidadeCPF(oficina, cpfNormalizado);

        Funcionario funcionario = funcionarioMapper.toEntity(dto);

        funcionario.setCpf(cpfNormalizado);
        funcionario.setAtivo(true);
        funcionario.setCargo(dto.cargo());
        funcionario.setDataAdmissao(LocalDate.now());
        funcionario.setOficina(oficina);

        Endereco endereco = enderecoMapper.toEntity(dto.endereco());
        funcionario.setEndereco(enderecoService.cadastrarEndereco(endereco));

        funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    /*
     * ======================
     * Atualizar
     * ======================
     */
    public FuncionarioDTO atualizarFuncionario(String cnpj, String cpf, AtualizarFuncionarioDTO dto) {

        Oficina oficina = localizarOficina(cnpj);

        Funcionario funcionario = localizarFuncionario(oficina, cpf);

        if (dto.nome() != null) {
            funcionario.setNome(dto.nome());
        }
        if (dto.email() != null) {
            funcionario.setEmail(dto.email());
        }
        if (dto.telefone() != null) {
            funcionario.setTelefone(dto.telefone());
        }
        if (dto.cargo() != null) {
            funcionario.setCargo(dto.cargo());
        }
        if (dto.endereco() != null) {
            enderecoService.atualizarEnderecoFuncionario(cnpj, funcionario, dto.endereco());
        }

        funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    /*
     * ======================
     * Ativar / Desativar
     * ======================
     */
    public FuncionarioDTO ativarFuncionario(String cnpj, String cpf) {
        Oficina oficina = localizarOficina(cnpj);
        Funcionario funcionario = localizarFuncionario(oficina, cpf);

        if (funcionario.getAtivo() == true) {
            throw new RegraNegocioException("Funcionario já está ativo");
        }

        funcionario.setAtivo(true);
        funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    public FuncionarioDTO inativarFuncionario(String cnpj, String cpf) {
        Oficina oficina = localizarOficina(cnpj);
        Funcionario funcionario = localizarFuncionario(oficina, cpf);

        if (funcionario.getAtivo() == false) {
            throw new RegraNegocioException("Funcionario já está inativo");
        }

        funcionario.setAtivo(false);
        funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    /*
     * ======================
     * Buscar
     * ======================
     */
    public FuncionarioDTO buscarFuncionarioPorCPF(String cnpj, String cpf) {
        Oficina oficina = localizarOficina(cnpj);
        Funcionario funcionario = localizarFuncionario(oficina, cpf);

        return funcionarioMapper.toResponseDTO(funcionario);
    }

    public Page<FuncionarioDTO> buscarFuncionarioPorNome(String cnpj, String nomeFuncionario, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        Page<Funcionario> funcionarios = funcionarioRepository.findByNomeContainingIgnoreCaseAndOficina(nomeFuncionario,
                oficina, pageable);

        if (funcionarios.isEmpty()) {
            throw new RegraNegocioException("Nenhum funcionário encontrado");
        }

        return funcionarios.map(funcionarioMapper::toResponseDTO);
    }

    public Page<FuncionarioDTO> buscarTodosFuncionarios(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        return funcionarioRepository.findByOficina(oficina, pageable)
                .map(funcionarioMapper::toResponseDTO);
    }

    public Page<FuncionarioDTO> buscarTodosFuncionariosAtivos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        return funcionarioRepository.findByAtivoAndOficina(true, oficina, pageable)
                .map(funcionarioMapper::toResponseDTO);
    }

    public Page<FuncionarioDTO> buscarTodosFuncionariosInativos(String cnpj, Pageable pageable) {
        Oficina oficina = localizarOficina(cnpj);
        return funcionarioRepository.findByAtivoAndOficina(false, oficina, pageable)
                .map(funcionarioMapper::toResponseDTO);
    }

    /*
     * ======================
     * Métodos Privados
     * ======================
     */
    private Oficina localizarOficina(String cnpj) {
        return oficinaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RegraNegocioException("Oficina não encontrada"));
    }

    private void validarDuplicidadeEmail(Oficina oficina, String email) {
        if (funcionarioRepository.findByEmailAndOficina(email, oficina) != null) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }
    }

    private void validarDuplicidadeCPF(Oficina oficina, String cpfNormalizado) {

        if (funcionarioRepository.existsByCpfAndOficina(cpfNormalizado, oficina)) {
            throw new RegraNegocioException("CPF já cadastrado");
        }
    }

    private Funcionario localizarFuncionario(Oficina oficina, String cpf) {

        String cpfNormalizado = normalizarCpf(cpf);

        return funcionarioRepository
                .findByCpfAndOficina(cpfNormalizado, oficina)
                .orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado"));
    }

    private String normalizarCpf(String cpf) {
        if (cpf == null) {
            throw new RegraNegocioException("CPF não pode ser nulo");
        }

        String cpfNormalizado = cpf.replaceAll("\\D", "");

        if (!cpfNormalizado.matches("\\d{11}")) {
            throw new RegraNegocioException("CPF inválido");
        }

        return cpfNormalizado;
    }

}
