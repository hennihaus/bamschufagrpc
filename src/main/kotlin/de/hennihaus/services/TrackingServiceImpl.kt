package de.hennihaus.services

import de.hennihaus.configurations.ConfigBackendConfiguration.Companion.BANK_NAME
import de.hennihaus.models.Group
import de.hennihaus.services.callservices.GroupCallService
import de.hennihaus.services.supportservices.ErrorService
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single

@Single
class TrackingServiceImpl(
    private val groupCall: GroupCallService,
    private val error: ErrorService,
    @Property(BANK_NAME) private val bankName: String,
) : TrackingService {

    override suspend fun trackRequest(username: String, password: String) {
        val group = groupCall.getAllGroups().find {
            (it.username == username) and (it.password == password)
        }
        with(group ?: throw error.toNotFoundException(message = GROUP_NOT_FOUND_MESSAGE)) {
            group.takeIf { it.hasBankName() }
                ?.also {
                    groupCall.updateGroup(
                        id = id,
                        group = it.updateStats(),
                    )
                }
                ?: throw error.toInternalServerException(
                    message = BANK_NOT_FOUND_MESSAGE,
                )
        }
    }

    private fun Group.hasBankName(): Boolean = stats.containsKey(key = bankName)

    private fun Group.updateStats(): Group = copy(
        stats = stats.map { (bank, request) ->
            if (bank == bankName) bank to request.plus(ONE_REQUEST)
            else bank to request
        }.toMap()
    )

    companion object {
        const val ONE_REQUEST = 1
        const val GROUP_NOT_FOUND_MESSAGE = "Group not found by username and password."
        const val BANK_NOT_FOUND_MESSAGE = "Internal bank name configuration is invalid."
    }
}