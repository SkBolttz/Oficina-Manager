package br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<Endereco> findByCepAndLogradouroAndNumeroAndBairroAndMunicipioAndUf(
            String cep,
            String logradouro,
            String numero,
            String bairro,
            String municipio,
            String uf);

}
