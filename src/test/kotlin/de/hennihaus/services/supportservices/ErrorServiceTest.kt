package de.hennihaus.services.supportservices

import de.hennihaus.objectmothers.ConfigurationObjectMother.getServiceConfiguration
// import de.hennihaus.objectmothers.ExceptionObjectMother.DEFAULT_MESSAGE
import io.grpc.StatusRuntimeException
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ErrorServiceTest {

    // private val config = getServiceConfiguration()
    //
    // private val classUnderTest = ErrorServiceImpl(
    //     config = config,
    // )
    //
    // @Nested
    // inner class ToValidationException {
    //     @Test
    //     fun `should map a message to RuntimeException with 400er status code`() {
    //         val message = DEFAULT_MESSAGE
    //
    //         val result: StatusRuntimeException = classUnderTest.toValidationException(
    //             message = message,
    //         )
    //
    //         result shouldBe nSta
    //     }
    // }
}