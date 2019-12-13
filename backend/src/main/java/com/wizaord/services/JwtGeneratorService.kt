package com.wizaord.services

import io.smallrye.jwt.KeyUtils.decodePrivateKey
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.nio.charset.Charset
import java.security.PrivateKey
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class JwtGeneratorService {

    @ConfigProperty(name = "jwt-private-key-path")
    lateinit var privateKeyFilePath: String
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    lateinit var iss: String

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)


    fun generateToken(username: String, roles : List<String> ? = listOf()): String {
        logger.info("Generating new JWT Token for $username")

        val pk = this.readPrivateKey(privateKeyFilePath)

        val jsonWebSignature = JsonWebSignature()
        jsonWebSignature.key = pk
        jsonWebSignature.keyIdHeaderValue = privateKeyFilePath
        jsonWebSignature.setHeader("typ", "JWT")
        jsonWebSignature.algorithmHeaderValue = AlgorithmIdentifiers.RSA_USING_SHA256
        jsonWebSignature.payload = generateClaims(username, roles!!).toJson()
        return jsonWebSignature.compactSerialization;
    }

    private fun generateClaims(username: String, roles: List<String>): JwtClaims {
        val jwtClaims = JwtClaims()
        jwtClaims.issuedAt = NumericDate.now()
        jwtClaims.expirationTime = NumericDate.fromSeconds(NumericDate.now().value + 3000)
        jwtClaims.issuer = this.iss
        jwtClaims.subject = username
        jwtClaims.setClaim("upn", username)
        jwtClaims.setClaim("groups", roles)
        return jwtClaims
    }

    /**
     * Read a PEM encoded private key from the classpath
     *
     * @param pemResName - key file resource name
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    @Throws(Exception::class)
    private fun readPrivateKey(pemResName: String): PrivateKey {
        val contentIS: InputStream = JwtGeneratorService::class.java.getResourceAsStream(pemResName)
        val tmp = ByteArray(4096)
        val length: Int = contentIS.read(tmp)
        return decodePrivateKey(String(tmp, 0, length, Charset.forName("UTF-8")))
    }

}