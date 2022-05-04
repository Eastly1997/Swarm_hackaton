package com.lakbay.pamayanan.viewModels

class Donation {
    companion object {
        val FIELD_TOTAL = "total"
        val FIELD_COMMUNITY = "community"
        val FIELD_DEVELOPMENT = "development"
        val FIELD_GOAL_EDUCATION = "goal_education"
        val FIELD_GOAL_FOOD = "goal_food"
        val FIELD_GOAL_TREE = "goal_tree"
        val FIELD_GOAL_WATER = "goal_water"
    }


    var total: Double = 0.00
    var community: Double = 0.00
    var development: Double = 0.00
    var goal_education: Double = 0.00
    var goal_food: Double = 0.00
    var goal_tree: Double = 0.00
    var goal_water: Double = 0.00
}