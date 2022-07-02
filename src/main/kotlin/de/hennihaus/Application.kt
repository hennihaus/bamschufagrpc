package de.hennihaus

import de.hennihaus.plugins.configureDependencyInjection
import de.hennihaus.plugins.configureServer

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        configureDependencyInjection()
        configureServer()
    }
}
