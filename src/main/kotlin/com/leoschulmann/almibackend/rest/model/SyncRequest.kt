package com.leoschulmann.almibackend.rest.model

data class SyncRequest(val s: List<List<Long>>) {

    fun extract(): List<SyncData> {
        return s.filter { list ->
            if (list.size != 2) {
                println("Sync request of unexpected size $list")
                false
            } else true
        }
            .map { list -> SyncData(list[0], (list[1].toInt())) }
    }
}
