package de.hennihaus.objectmothers

import de.hennihaus.configurations.ConfigBackendConfiguration
import de.hennihaus.configurations.ServiceConfiguration

object ConfigurationObjectMother {

    const val DEFAULT_DOMAIN = "bambusinessintegration.wi.hs-furtwangen.de"
    const val DEFAULT_SERVICE = "schufa"

    const val DEFAULT_PROTOCOL = "http"
    const val DEFAULT_HOST = "0.0.0.0"
    const val DEFAULT_PORT = 8080
    const val DEFAULT_MAX_RETRIES = 2

    fun getServiceConfiguration(
        domain: String = DEFAULT_DOMAIN,
        service: String = DEFAULT_SERVICE,
    ) = ServiceConfiguration(
        domain = domain,
        service = service,
    )

    fun getConfigBackendConfiguration(
        protocol: String = DEFAULT_PROTOCOL,
        host: String = DEFAULT_HOST,
        port: Int = DEFAULT_PORT,
        maxRetries: Int = DEFAULT_MAX_RETRIES,
    ) = ConfigBackendConfiguration(
        protocol = protocol,
        host = host,
        port = port,
        maxRetries = maxRetries,
    )
}