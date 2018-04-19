package com.innoq.blockchain.kotlin3

import com.innoq.blockchain.kotlin3.model.Block
import com.innoq.blockchain.kotlin3.model.Transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

val nodeId = UUID.randomUUID();

val genesisBlock = Block(
        1,
        0,
        1917336,
        listOf(
                Transaction(
                        "b3c973e2-db05-4eb5-9668-3e81c7389a6d",
                        0,
                        "I am Heribert Innoq"
                )
        ),
        "0"
)

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
