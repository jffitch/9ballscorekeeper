package com.mathgeniusguide.nineballscorekeeper.objects.stats

class SuccessStats{
    var success = 0
    var failure = 0
    var total = 0

    fun addSuccessStats(successStats: SuccessStats) {
        success += successStats.success
        failure += successStats.failure
        total += successStats.total
    }
}