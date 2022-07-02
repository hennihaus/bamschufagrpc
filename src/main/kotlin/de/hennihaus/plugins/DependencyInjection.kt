package de.hennihaus.plugins

import de.hennihaus.Application
import de.hennihaus.configurations.configBackendModule
import de.hennihaus.configurations.defaultModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.fileProperties
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() = startKoin {
    initKoin()
}

fun KoinApplication.initKoin(
    properties: Map<String, String> = emptyMap(),
    vararg modules: Module = arrayOf(defaultModule, configBackendModule),
) {
    slf4jLogger()
    fileProperties()
    properties(values = properties)
    modules(modules = modules)
}