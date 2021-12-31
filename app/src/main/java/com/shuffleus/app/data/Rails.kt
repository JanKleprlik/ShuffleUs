package com.shuffleus.app.data

sealed class RailsItem(val type:RailsViewType) {
    data class RailsGroupName(val groupName: String) : RailsItem(RailsViewType.GROUP_NAME)
    data class RailsPeople(val people: List<User>) : RailsItem(RailsViewType.PEOPLE)

    enum class RailsViewType { GROUP_NAME, PEOPLE }
}