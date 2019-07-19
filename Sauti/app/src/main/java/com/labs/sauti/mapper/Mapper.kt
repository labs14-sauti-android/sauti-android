package com.labs.sauti.mapper

abstract class Mapper <T, U> {
    abstract fun mapFrom(from: T): U
}