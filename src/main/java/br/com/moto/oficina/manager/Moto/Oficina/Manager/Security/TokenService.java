package br.com.moto.oficina.manager.Moto.Oficina.Manager.Security;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private long expirationMillis;

    public String gerarToken(Usuario usuario){
        return JWT.create()
                .withIssuer("Geficina")
                .withSubject(usuario.getCnpj())
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + expirationMillis))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validarToken(String token) {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("Geficina")
                    .build()
                    .verify(token)
                    .getSubject();
    }
}
