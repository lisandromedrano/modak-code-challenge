package com.modak.challenge.model

class NotificationType(val name: String){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NotificationType) return false
        return name == other.name
    }
    override fun hashCode() = name.hashCode()
}
