package de.hennihaus.services.grpcservices.resourceservices

import com.google.common.base.CaseFormat
import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.GetRatingRequest
import de.hennihaus.models.generated.Rating
import de.hennihaus.services.supportservices.ErrorService
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import io.konform.validation.jsonschema.minLength
import org.koin.core.annotation.Single
import de.hennihaus.models.generated.RatingLevel as RequestRatingLevel

data class RatingResource(
    val socialSecurityNumber: String,
    val ratingLevel: String,
    val delayInMilliseconds: Long,
    val username: String,
    val password: String,
)

@Single
class RatingResourceService(
    override val error: ErrorService,
) : ResourceService<GetRatingRequest, RatingResource, Rating> {

    override fun GetRatingRequest.toResource() = RatingResource(
        socialSecurityNumber = socialSecurityNumber,
        ratingLevel = ratingLevel.toRatingLevel(),
        delayInMilliseconds = delayInMilliseconds,
        username = username,
        password = password,
    )

    override suspend fun RatingResource.validate(body: suspend (RatingResource) -> Rating): Rating {
        val validation = Validation<RatingResource> {
            RatingResource::socialSecurityNumber required {
                minLength(length = 1)
            }
            RatingResource::ratingLevel required {
                enum<RatingLevel>()
            }
            RatingResource::delayInMilliseconds required {}
            RatingResource::username required {
                minLength(length = 1)
            }
            RatingResource::password required {
                minLength(length = 1)
            }
        }
        return validation.validateAndThrowOnFailure(resource = this).let {
            body(this)
        }
    }

    private fun RequestRatingLevel.toRatingLevel(): String {
        val grpcRatingLevelPrefix = CaseFormat.UPPER_CAMEL.to(
            CaseFormat.UPPER_UNDERSCORE, "${this::class.simpleName}_",
        )
        return name.replace(
            oldValue = grpcRatingLevelPrefix,
            newValue = "",
        )
    }
}
