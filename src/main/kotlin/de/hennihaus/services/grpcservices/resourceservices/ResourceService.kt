package de.hennihaus.services.grpcservices.resourceservices

import de.hennihaus.services.supportservices.ErrorService
import io.konform.validation.Invalid
import io.konform.validation.Validation

interface ResourceService<Request, Resource, R> {

    val error: ErrorService

    fun Request.toResource(): Resource

    suspend fun Resource.validate(body: suspend (Resource) -> R): R

    fun Validation<Resource>.validateAndThrowOnFailure(resource: Resource) {
        val result = validate(value = resource)
        if (result is Invalid<Resource>) {
            val messages = result.errors.map {
                "${it.dataPath.substringAfterLast(".")} ${it.message}"
            }
            throw error.toValidationException(
                message = "$messages",
            )
        }
    }
}