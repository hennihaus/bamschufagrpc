package de.hennihaus.objectmothers

import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.GetRatingRequest
import de.hennihaus.models.generated.Rating
import de.hennihaus.models.generated.getRatingRequest
import de.hennihaus.models.generated.rating
import de.hennihaus.objectmothers.GroupObjectMother.DEFAULT_PASSWORD
import de.hennihaus.objectmothers.GroupObjectMother.FIRST_GROUP_USERNAME
import de.hennihaus.services.grpcservices.resourceservices.RatingResource
import de.hennihaus.models.generated.RatingLevel as RequestRatingLevel

object RatingObjectMother {

    const val RATING_LEVEL_GRPC_ENUM_PREFIX = "RATING_LEVEL_"

    const val DEFAULT_RATING_LEVEL = "A"
    const val DEFAULT_SOCIAL_SECURITY_NUMBER = "123"
    const val DEFAULT_DELAY_IN_MILLISECONDS = 0L

    fun getBestRating(
        score: Int = RatingLevel.valueOf(value = DEFAULT_RATING_LEVEL).maxScore,
        failureRiskInPercent: Double = RatingLevel.valueOf(value = DEFAULT_RATING_LEVEL).failureRiskInPercent,
    ): Rating = rating {
        this.score = score
        this.failureRiskInPercent = failureRiskInPercent
    }

    fun getMinValidRatingRequest(
        socialSecurityNumber: String = DEFAULT_SOCIAL_SECURITY_NUMBER,
        ratingLevel: RequestRatingLevel = RequestRatingLevel.valueOf(value = "$RATING_LEVEL_GRPC_ENUM_PREFIX$DEFAULT_RATING_LEVEL"),
        delayInMilliseconds: Long = DEFAULT_DELAY_IN_MILLISECONDS,
        username: String = FIRST_GROUP_USERNAME,
        password: String = DEFAULT_PASSWORD,
    ): GetRatingRequest = getRatingRequest {
        this.socialSecurityNumber = socialSecurityNumber
        this.ratingLevel = ratingLevel
        this.delayInMilliseconds = delayInMilliseconds
        this.username = username
        this.password = password
    }

    fun getMinValidRatingResource(
        socialSecurityNumber: String = DEFAULT_SOCIAL_SECURITY_NUMBER,
        ratingLevel: String = DEFAULT_RATING_LEVEL,
        delayInMilliseconds: Long = DEFAULT_DELAY_IN_MILLISECONDS,
        username: String = FIRST_GROUP_USERNAME,
        password: String = DEFAULT_PASSWORD,
    ) = RatingResource(
        socialSecurityNumber = socialSecurityNumber,
        ratingLevel = ratingLevel,
        delayInMilliseconds = delayInMilliseconds,
        username = username,
        password = password,
    )
}
