package com.labs.sauti.model.trade_info


data class RegulatedGoodData(
    val country: String,
    val language: String,
    val prohibiteds: List<Prohibited>,
    val restricteds: List<Restricted>,
    val sensitives: List<Sensitive>
)

data class Prohibited(
    val name: String
)

data class Sensitive(
    val name: String
)

data class Restricted(
    val name: String
)