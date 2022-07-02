package de.hennihaus.services.supportservices

import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.ErrorInfo
import com.google.rpc.Status
import de.hennihaus.configurations.ServiceConfiguration
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import org.koin.core.annotation.Single

@Single
class ErrorServiceImpl(private val config: ServiceConfiguration) : ErrorService {

    override fun toValidationException(message: String): StatusRuntimeException = buildException(
        code = Code.INVALID_ARGUMENT,
        message = message,
        reason = ENTITY_INVALID_REASON,
    )

    override fun toNotFoundException(message: String): StatusRuntimeException = buildException(
        code = Code.NOT_FOUND,
        message = message,
        reason = ENTITY_NOT_FOUND_REASON,
    )

    override fun toInternalServerException(message: String): StatusRuntimeException = buildException(
        code = Code.INTERNAL,
        message = message,
        reason = INTERNAL_SERVER_ERROR_REASON,
    )

    private fun buildException(code: Code, message: String, reason: String): StatusRuntimeException {
        val errorInfo = ErrorInfo.newBuilder().apply {
            this.reason = reason
            this.domain = config.domain
            this.metadataMap + (SERVICE_METADATA_KEY to "${config.service}.${config.domain}")
        }
        val status = Status.newBuilder().apply {
            this.code = code.number
            this.message = message
            this.detailsList + Any.pack(errorInfo.build())
        }
        return StatusProto.toStatusRuntimeException(
            status.build()
        )
    }

    companion object {
        const val ENTITY_INVALID_REASON = "ENTITY_INVALID"
        const val ENTITY_NOT_FOUND_REASON = "ENTITY_NOT_FOUND"
        const val INTERNAL_SERVER_ERROR_REASON = "INTERNAL_SERVER_ERROR"

        const val SERVICE_METADATA_KEY = "service"
    }
}