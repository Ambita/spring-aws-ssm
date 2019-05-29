package com.ambita.spring.aws.ssm

import org.slf4j.LoggerFactory
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.GetParameterRequest
import java.net.URI
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets.UTF_8

class ParamStoreResourceLoader(configurableEnvironment: ConfigurableEnvironment) : DefaultResourceLoader() {
    private val firstProfile: String

    private var ssmClient: SsmClient? = null

    init {

        firstProfile = findFirstProfile(configurableEnvironment)
        LOG.info("Initalizing {} for ssm:// urls. firstProfile: {}",
                ParamStoreResourceLoader::class.java,
                firstProfile)
    }

    private fun findFirstProfile(configurableEnvironment: ConfigurableEnvironment): String {
        val profiles = configurableEnvironment.activeProfiles
        LOG.warn("Profiles in order: {}", *profiles)
        return if (profiles.size > 0 && !profiles[0].isEmpty())
            profiles[0]
        else
            "localdev"
    }

    override fun getResource(location: String): Resource {
        return loadFromSSM(location)
    }

    private fun loadFromSSM(location: String): Resource {

        try {
            val ssmUri = URI(location)

            val ssmPath = getPath(ssmUri)
            LOG.info("Loading properties from: {}", ssmPath)

            val client = buildClient(ssmUri)

            val request = GetParameterRequest
                    .builder()
                    .name(ssmPath)
                    .withDecryption(true)
                    .build()

            val resp = client.getParameter(request)
            val message = resp.parameter().value()
            LOG.debug("ssm message for {}: {} chars", location, message.length)

            return ByteArrayResource(
                    message.toByteArray(UTF_8),
                    "Yaml from $location"
            )
        } catch (e: URISyntaxException) {
            throw IllegalArgumentException("ssm url is not valid: $location", e)
        }

    }

    private fun getPath(ssmUri: URI): String {
        try {
            return ssmUri.path
                    .replace("PROFILE".toRegex(), firstProfile)
        } catch (e: RuntimeException) {
            LOG.error("Error when replacing PROFILE", e)
            throw e
        }

    }

    private fun buildClient(ssmUri: URI): SsmClient {
        if (ssmClient == null) {
            val region = findRegion(ssmUri)

            ssmClient = SsmClient
                    .builder()
                    .region(region)
                    .build()
        }

        return ssmClient as SsmClient
    }

    private fun findRegion(ssmUri: URI): Region {
        val uriRegionId = ssmUri.host
        return Region
                .regions()
                .stream()
                .filter { region -> uriRegionId.equals(region.id(), ignoreCase = true) }
                .findFirst()
                .orElseThrow<Throwable> { IllegalArgumentException("No region found for $uriRegionId") }
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(ParamStoreResourceLoader::class.java)
    }
}
