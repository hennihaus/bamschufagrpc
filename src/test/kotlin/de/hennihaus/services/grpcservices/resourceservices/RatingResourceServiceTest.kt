package de.hennihaus.services.grpcservices.resourceservices

import de.hennihaus.models.RatingLevel
import de.hennihaus.models.generated.Rating
import de.hennihaus.models.generated.RatingLevel.RATING_LEVEL_A
import de.hennihaus.models.generated.RatingLevel.RATING_LEVEL_UNSPECIFIED
import de.hennihaus.objectmothers.RatingObjectMother.getBestRating
import de.hennihaus.objectmothers.RatingObjectMother.getMinValidRatingRequest
import de.hennihaus.objectmothers.RatingObjectMother.getMinValidRatingResource
import de.hennihaus.services.supportservices.ErrorServiceImpl
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.longs.exactly
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RatingResourceServiceTest {

    private val error = mockk<ErrorServiceImpl>()

    private val classUnderTest = RatingResourceService(
        error = error,
    )

    @BeforeEach
    fun init() = clearAllMocks()

    @Nested
    inner class ToResource {
        @Test
        fun `should return correct rating resource with rating level = A when when rating level of request = A`() {
            val request = getMinValidRatingRequest(
                ratingLevel = RATING_LEVEL_A,
            )

            val result: RatingResource = with(classUnderTest) {
                request.toResource()
            }

            result shouldBe getMinValidRatingResource(
                ratingLevel = "${RatingLevel.A}",
            )
        }

        @Test
        fun `should return rating resource with rating level = UNSPECIFIED when rating level of request = UNSPECIFIED`() {
            val request = getMinValidRatingRequest(
                ratingLevel = RATING_LEVEL_UNSPECIFIED,
            )

            val result: RatingResource = with(classUnderTest) {
                request.toResource()
            }

            result shouldBe getMinValidRatingResource(
                ratingLevel = "UNSPECIFIED",
            )
        }
    }

    @Nested
    inner class Validate {
        @BeforeEach
        fun init() {
            mockkObject(RatingResourceServiceTest)
        }

        @Test
        fun `should execute test operation when rating is valid`() = runBlocking {
            val resource = getMinValidRatingResource()
            every { testOperation(ratingResource = any()) } returns getBestRating()

            val result: Rating = with(classUnderTest) {
                resource.validate {
                    testOperation(ratingResource = it)
                }
            }

            result shouldBe getBestRating()
            verify(exactly = 1) { testOperation(ratingResource = getMinValidRatingResource()) }
            verify { error wasNot Called }
        }

        // @Test
        // fun `should throw an exception and not execute test operation when socialSecurityNumber = empty`() = runBlocking {
        //     val resource = getMinValidRatingResource()
        //
        //     val result = with(classUnderTest) {
        //         resource.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     verify {  }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when socialSecurityNumber = null`() {
        //     val classUnderTest = getMinValidRatingResource(socialSecurityNumber = null)
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage "[socialSecurityNumber is required]"
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when socialSecurityNumber = empty`() {
        //     val classUnderTest = getMinValidRatingResource(socialSecurityNumber = "")
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage "[socialSecurityNumber must have at least 1 characters]"
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when ratingLevel = null`() {
        //     val classUnderTest = getMinValidRatingResource(ratingLevel = null)
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage "[ratingLevel is required]"
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when ratingLevel = Z`() {
        //     val classUnderTest = getMinValidRatingResource(ratingLevel = "Z")
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage """
        //         [ratingLevel must be one of: 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'N', 'O', 'P']
        //     """.trimIndent()
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when delayInMilliseconds = null`() {
        //     val classUnderTest = getMinValidRatingResource(delayInMilliseconds = null)
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage "[delayInMilliseconds is required]"
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when username = null`() {
        //     val classUnderTest = getMinValidRatingResource(username = null)
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage "[username is required]"
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when username = empty`() {
        //     val classUnderTest = getMinValidRatingResource(username = "")
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage "[username must have at least 1 characters]"
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when password = null`() {
        //     val classUnderTest = getMinValidRatingResource(password = null)
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage "[password is required]"
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
        //
        // @Test
        // fun `should throw exception and not execute test operation when password = empty`() {
        //     val classUnderTest = getMinValidRatingResource(password = "")
        //
        //     val result: ValidationException = shouldThrowExactly {
        //         classUnderTest.validate {
        //             testOperation(ratingResource = it)
        //         }
        //     }
        //
        //     result shouldHaveMessage "[password must have at least 1 characters]"
        //     verify(exactly = 0) { testOperation(ratingResource = any()) }
        // }
    }

    companion object {
        fun testOperation(ratingResource: RatingResource): Rating {
            println(ratingResource)
            return mockk()
        }
    }
}