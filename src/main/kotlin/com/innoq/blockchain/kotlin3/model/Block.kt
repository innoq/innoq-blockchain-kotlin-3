package com.innoq.blockchain.kotlin3.model

data class Block(
        val index: Long,
        val timestamp: Long,
        val proof: Long,
        val transactions: List<Transaction>,
        val previousBlockHash: String
)