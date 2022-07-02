package de.hennihaus.services

import de.hennihaus.objectmothers.GroupObjectMother
import de.hennihaus.objectmothers.GroupObjectMother.getFirstGroup
import de.hennihaus.objectmothers.GroupObjectMother.getSecondGroup
import de.hennihaus.objectmothers.GroupObjectMother.getThirdGroup
import de.hennihaus.services.TrackingServiceImpl.Companion.GROUP_NOT_FOUND_MESSAGE
import de.hennihaus.services.callservices.GroupCallService
import de.hennihaus.services.callservices.GroupCallServiceImpl
import de.hennihaus.services.supportservices.ErrorService
import de.hennihaus.services.supportservices.ErrorServiceImpl
import io.grpc.StatusRuntimeException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TrackingServiceTest {

    // private lateinit var groupCall: GroupCallService
    // private lateinit var error: ErrorService
    // private lateinit var bankName: String
    //
    // private lateinit var classUnderTest: TrackingService
    //
    // @BeforeEach
    // fun init() = clearAllMocks()
    //
    // @Nested
    // inner class TrackRequest {
    //
    //     @BeforeEach
    //     fun init() {
    //         groupCall = mockk<GroupCallServiceImpl>()
    //         error = mockk<ErrorServiceImpl>()
    //         bankName = GroupObjectMother.DEFAULT_JMS_BANK_A_NAME
    //         classUnderTest = TrackingServiceImpl(
    //             groupCall = groupCall,
    //             error = error,
    //             bankName = bankName,
    //         )
    //
    //         // default behavior
    //         coEvery { groupCall.getAllGroups() } returns listOf(
    //             getFirstGroup(),
    //             getSecondGroup(),
    //             getThirdGroup(),
    //         )
    //         coEvery { groupCall.updateGroup(id = any(), group = any()) } returns mockk()
    //     }
    //
    //     @Test
    //     fun `should pass raised stats correctly when username, password and bank available`() = runBlocking {
    //         val (id, username, password, _, _, stats) = getThirdGroup()
    //
    //         classUnderTest.trackRequest(username = username, password = password)
    //
    //         coVerifySequence {
    //             groupCall.getAllGroups()
    //             groupCall.updateGroup(
    //                 id = id,
    //                 group = getThirdGroup().copy(
    //                     stats = stats + Pair(
    //                         first = bankName,
    //                         second = stats[bankName]!! + 1,
    //                     ),
    //                 ),
    //             )
    //         }
    //         verify { error wasNot Called }
    //     }
    //
    //     @Test
    //     fun `should throw an exception when zero groups are available`() = runBlocking {
    //         val (_, username, password) = getThirdGroup()
    //         coEvery { groupCall.getAllGroups() } returns emptyList()
    //
    //         val result: StatusRuntimeException = shouldThrowExactly {
    //             classUnderTest.trackRequest(
    //                 username = username,
    //                 password = password,
    //             )
    //         }
    //
    //         result shouldHaveMessage GROUP_NOT_FOUND_MESSAGE
    //         coVerifySequence {
    //             groupCall.getAllGroups()
    //             error.toValidationException(message = GROUP_NOT_FOUND_MESSAGE)
    //         }
    //         coVerify(exactly = 0) { groupCall.updateGroup(id = any(), group = any()) }
    //     }
    //
    //     @Test
    //     fun `should throw an exception and not update stats when username is unknown`() = runBlocking {
    //         val username = "unknown"
    //         val password = getThirdGroup().password
    //
    //         val result: NotFoundException = shouldThrowExactly {
    //             classUnderTest.trackRequest(
    //                 username = username,
    //                 password = password,
    //             )
    //         }
    //
    //         result shouldHaveMessage GROUP_NOT_FOUND_MESSAGE
    //         coVerify(exactly = 1) { groupCall.getAllGroups() }
    //         coVerify(exactly = 0) { groupCall.updateGroup(id = any(), group = any()) }
    //     }
    //
    //     @Test
    //     fun `should throw an exception and not update stats when password is unknown`() = runBlocking {
    //         val username = getThirdGroup().username
    //         val password = "unknown"
    //
    //         val result: NotFoundException = shouldThrowExactly {
    //             classUnderTest.trackRequest(
    //                 username = username,
    //                 password = password,
    //             )
    //         }
    //
    //         result shouldHaveMessage GROUP_NOT_FOUND_MESSAGE
    //         coVerify(exactly = 1) { groupCall.getAllGroups() }
    //         coVerify(exactly = 0) { groupCall.updateGroup(id = any(), group = any()) }
    //     }
    //
    //     @Test
    //     fun `should throw an exception and not update stats when bankName is unknown`() = runBlocking {
    //         val (_, username, password) = getThirdGroup()
    //         bankName = "unknown"
    //         classUnderTest = TrackingServiceImpl(
    //             groupCall = groupCall,
    //             bankName = bankName,
    //         )
    //
    //         val result: IllegalStateException = shouldThrowExactly {
    //             classUnderTest.trackRequest(
    //                 username = username,
    //                 password = password,
    //             )
    //         }
    //
    //         result shouldHaveMessage TrackingServiceImpl.BANK_NOT_FOUND_MESSAGE
    //         coVerify(exactly = 1) { groupCall.getAllGroups() }
    //         coVerify(exactly = 0) { groupCall.updateGroup(id = any(), group = any()) }
    //     }
    //
    //     @Test
    //     fun `should throw an exception when error in groupCall occurs`() = runBlocking {
    //         val (_, username, password, _, _, _) = getThirdGroup()
    //         coEvery { groupCall.updateGroup(id = any(), group = any()) } throws Exception("")
    //
    //         val result: Exception = shouldThrowExactly {
    //             classUnderTest.trackRequest(
    //                 username = username,
    //                 password = password,
    //             )
    //         }
    //
    //         result shouldHaveMessage ""
    //         coVerifySequence {
    //             groupCall.getAllGroups()
    //             groupCall.updateGroup(id = any(), group = any())
    //         }
    //     }
    // }
}