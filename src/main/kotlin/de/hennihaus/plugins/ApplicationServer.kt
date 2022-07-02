package de.hennihaus.plugins

import de.hennihaus.Application
import de.hennihaus.services.grpcservices.RatingGrpcService
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.util.TransmitStatusRuntimeExceptionInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun Application.configureServer() = object : KoinComponent {

    private val server: Server

    init {
        val ratingGrpc by inject<RatingGrpcService>()

        server = ServerBuilder
            .forPort(8080)
            .intercept(TransmitStatusRuntimeExceptionInterceptor.instance())
            .addService(ratingGrpc)
            .build()

        server.start()
        server.awaitTermination()
    }
}