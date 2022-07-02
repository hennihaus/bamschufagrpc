package de.hennihaus.configurations

import de.hennihaus.configurations.ServiceConfiguration.Companion.DOMAIN_NAME
import de.hennihaus.configurations.ServiceConfiguration.Companion.SERVICE_NAME
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule as generatedModule

val defaultModule = module {
    includes(generatedModule)

    single {
        CIO.create()
    }

    single {
        val domain = getProperty<String>(key = DOMAIN_NAME)
        val service = getProperty<String>(key = SERVICE_NAME)

        ServiceConfiguration(
            domain = domain,
            service = service,
        )
    }
}

data class ServiceConfiguration(
    val domain: String,
    val service: String,
) {
    companion object {
        const val DOMAIN_NAME = "grpc.application.domain"
        const val SERVICE_NAME = "grpc.application.bankName"
    }
}