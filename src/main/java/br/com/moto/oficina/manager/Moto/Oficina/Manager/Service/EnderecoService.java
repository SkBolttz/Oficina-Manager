package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Api.ViaCep;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.ViaCepDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.AtualizarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Cliente;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Endereco;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Funcionario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RegraNegocioException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.ClienteRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.EnderecoRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.FuncionarioRepository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.OficinaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnderecoService {

        private final EnderecoRepository enderecoRepository;
        private final ClienteRepository clienteRepository;
        private final FuncionarioRepository funcionarioRepository;
        private final OficinaRepository oficinaRepository;
        private final ViaCep viaCep;

        public EnderecoService(
                        EnderecoRepository enderecoRepository,
                        ClienteRepository clienteRepository,
                        FuncionarioRepository funcionarioRepository,
                        OficinaRepository oficinaRepository,
                        ViaCep viaCep) {

                this.enderecoRepository = enderecoRepository;
                this.clienteRepository = clienteRepository;
                this.funcionarioRepository = funcionarioRepository;
                this.oficinaRepository = oficinaRepository;
                this.viaCep = viaCep;
        }

        /*
         * ======================
         * Cadastro
         * ======================
         */

        public Endereco cadastrarEndereco(Endereco endereco) {
                return validarEnderecoDuplicado(endereco);
        }

        /*
         * ======================
         * Atualização
         * ======================
         */

        public void atualizarEnderecoCliente(String cnpj, Cliente cliente, EnderecoDTO dto) {

                Oficina oficina = oficinaRepository.findByCnpj(cnpj).orElseThrow();
                Cliente clienteLocalizado = clienteRepository
                                .findByCpfCnpjAndOficina(cliente.getCpfCnpj(), oficina).orElseThrow();

                Endereco endereco = clienteLocalizado.getEndereco();

                if (dto.cep() != null) {
                        endereco.setCep(dto.cep());
                }
                if (dto.logradouro() != null) {
                        endereco.setLogradouro(dto.logradouro());
                }
                if (dto.numero() != null) {
                        endereco.setNumero(dto.numero());
                }
                if (dto.complemento() != null) {
                        endereco.setComplemento(dto.complemento());
                }
                if (dto.bairro() != null) {
                        endereco.setBairro(dto.bairro());
                }
                if (dto.municipio() != null) {
                        endereco.setMunicipio(dto.municipio());
                }
                if (dto.uf() != null) {
                        endereco.setUf(dto.uf());
                }

                Endereco enderecoFinal = validarEnderecoDuplicado(endereco);

                cliente.setEndereco(enderecoFinal);
                clienteRepository.save(cliente);
        }

        public void atualizarEnderecoFuncionario(String cnpj, Funcionario funcionario, EnderecoDTO dto) {

                Oficina oficina = oficinaRepository.findByCnpj(cnpj).orElseThrow();
                Funcionario funcionarioLocalizado = funcionarioRepository
                                .findByCpfAndOficina(funcionario.getCpf(), oficina).orElseThrow();

                Endereco endereco = funcionarioLocalizado.getEndereco();

                if (dto.cep() != null) {
                        endereco.setCep(dto.cep());
                }
                if (dto.logradouro() != null) {
                        endereco.setLogradouro(dto.logradouro());
                }
                if (dto.numero() != null) {
                        endereco.setNumero(dto.numero());
                }
                if (dto.complemento() != null) {
                        endereco.setComplemento(dto.complemento());
                }
                if (dto.bairro() != null) {
                        endereco.setBairro(dto.bairro());
                }
                if (dto.municipio() != null) {
                        endereco.setMunicipio(dto.municipio());
                }
                if (dto.uf() != null) {
                        endereco.setUf(dto.uf());
                }

                Endereco enderecoFinal = validarEnderecoDuplicado(endereco);

                funcionario.setEndereco(enderecoFinal);
                funcionarioRepository.save(funcionario);
        }

        public void atualizarEnderecoOficina(Oficina oficina, AtualizarOficinaDTO dto) {

                Oficina oficinaLocalizada = oficinaRepository.findByCnpj(oficina.getCnpj()).orElseThrow();
                Endereco endereco = oficinaLocalizada.getEndereco();

                if (dto.endereco().cep() != null) {
                        endereco.setCep(dto.endereco().cep());
                }
                if (dto.endereco().logradouro() != null) {
                        endereco.setLogradouro(dto.endereco().logradouro());
                }
                if (dto.endereco().numero() != null) {
                        endereco.setNumero(dto.endereco().numero());
                }
                if (dto.endereco().complemento() != null) {
                        endereco.setComplemento(dto.endereco().complemento());
                }
                if (dto.endereco().bairro() != null) {
                        endereco.setBairro(dto.endereco().bairro());
                }
                if (dto.endereco().municipio() != null) {
                        endereco.setMunicipio(dto.endereco().municipio());
                }
                if (dto.endereco().uf() != null) {
                        endereco.setUf(dto.endereco().uf());
                }

                Endereco enderecoFinal = validarEnderecoDuplicado(endereco);
                oficina.setEndereco(enderecoFinal);
                oficinaRepository.save(oficina);
        }

        /*
         * ======================
         * Métodos Privados
         * ======================
         */

        private Endereco validarEnderecoDuplicado(Endereco endereco) {

                return enderecoRepository
                                .findByCepAndLogradouroAndNumeroAndBairroAndMunicipioAndUf(
                                                endereco.getCep(),
                                                endereco.getLogradouro(),
                                                endereco.getNumero(),
                                                endereco.getBairro(),
                                                endereco.getMunicipio(),
                                                endereco.getUf())
                                .orElseGet(() -> {
                                        if (endereco.getId() != null) {
                                                return endereco;
                                        }
                                        return enderecoRepository.save(endereco);
                                });
        }

        public EnderecoDTO buscarCep(String cep) {

                System.out.println("Entrou aqui UM");
                System.out.println(cep);
                
                ViaCepDTO cepAPI = viaCep.buscarCep(cep);

                if (viaCep == null || cepAPI.cep() == null) {
                        throw new RegraNegocioException("CEP não encontrado");
                }

                return new EnderecoDTO(
                                cepAPI.cep(),
                                cepAPI.logradouro(),
                                null,
                                cepAPI.complemento(),
                                cepAPI.bairro(),
                                cepAPI.municipio(),
                                cepAPI.uf());
        }
}
