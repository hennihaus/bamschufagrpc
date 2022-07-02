package de.hennihaus.services.supportservices

import io.grpc.StatusRuntimeException

interface ErrorService {
    fun toValidationException(message: String): StatusRuntimeException
    fun toNotFoundException(message: String): StatusRuntimeException
    fun toInternalServerException(message: String): StatusRuntimeException
}
