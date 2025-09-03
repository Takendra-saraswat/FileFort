package com.tks.filefort

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform