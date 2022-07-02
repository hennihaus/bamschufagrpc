package de.hennihaus.services

import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.Rating
import de.hennihaus.models.generated.rating
import kotlinx.coroutines.delay
import org.koin.core.annotation.Single

@Single
class RatingServiceImpl : RatingService {

    override suspend fun calculateRating(ratingLevel: String, delayInMilliseconds: Long): Rating {
        delay(timeMillis = delayInMilliseconds)

        return RatingLevel.valueOf(value = ratingLevel.uppercase()).let {
            rating {
                score = (it.minScore..it.maxScore).random()
                failureRiskInPercent = it.failureRiskInPercent
            }
        }
    }
}
