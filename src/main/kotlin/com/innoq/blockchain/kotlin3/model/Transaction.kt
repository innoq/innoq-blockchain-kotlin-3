package com.innoq.blockchain.kotlin3.model

data class Transaction(
        val id: String,
        val timestamp: Long,
        val payload: String
)