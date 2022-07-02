package de.hennihaus.services.grpcservices

import de.hennihaus.models.generated.GetRatingRequest
import de.hennihaus.models.generated.Rating
import de.hennihaus.models.generated.RatingServiceGrpcKt.RatingServiceCoroutineImplBase
import de.hennihaus.services.RatingService
import de.hennihaus.services.TrackingService
import de.hennihaus.services.grpcservices.resourceservices.RatingResourceService
import org.koin.core.annotation.Single

@Single
class RatingGrpcService(
    private val ratingResource: RatingResourceService,
    private val rating: RatingService,
    private val tracking: TrackingService,
) : RatingServiceCoroutineImplBase() {

    override suspend fun getRating(request: GetRatingRequest): Rating = with(ratingResource) {
        request.toResource().validate {
            val score = rating.calculateRating(
                ratingLevel = it.ratingLevel,
                delayInMilliseconds = it.delayInMilliseconds,
            )
            score.also { _ ->
                tracking.trackRequest(
                    username = it.username,
                    password = it.password,
                )
            }
        }
    }
}

// TODO: 25.06.22 Saubere Application Structure
// TODO: 25.06.22 Proto in library veröffentlichen 
// TODO: 25.06.22 protobuf vollständig konfiguerieren 
// TODO: 25.06.22 Request Logging 
// TODO: 25.06.22 Global error handling 
// TODO: 25.06.22 Testsoftware
// TODO: 25.06.22 Research are there any CORS necessary
// TODO: 26.06.22 Linter for protobuf (github action check with google AIP linter)
// TODO: 28.06.22 Generate Kotlin proto for existing Java protos
// TODO: 29.06.22 RAtingGrpcService und RatingService mit KoinAnnotation anstatt selber deklarieren

// TODO: 26.06.22 Automatische Dokumentation des Proto erstellen
// TODO: 26.06.22 Package Upload Java
// TODO: 26.06.22 Package Upload Kotlin
// TODO: 26.06.22 Package Upload JavaScript
// TODO: 26.06.22 Package Upload TypeScript
// TODO: 26.06.22 Package Upload .NET
// TODO: 26.06.22 Package Upload Ruby
// TODO: 26.06.22 Hinweis für Golang machen wie proto eingebunden werden kann

// TODO: 26.06.22 No package upload for Python-, Scala and C++ Fanboys. You should not use theese language for application development!
