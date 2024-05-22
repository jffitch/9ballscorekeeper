package com.mathgeniusguide.nineballscorekeeper.enums

enum class SharedPreferencesTarget(val target: String) {
    GAME_STRING("game_string"),
    LOADED_GAME("loaded_game"),
    GAME_INFO("game_info"),
    ZERO_SCORE("zero_score"),
    END_AT_TOURNAMENT_WIN("end_at_tournament_win")
}