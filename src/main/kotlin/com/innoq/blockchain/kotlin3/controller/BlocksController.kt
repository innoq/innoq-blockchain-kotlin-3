package com.innoq.blockchain.kotlin3.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.innoq.blockchain.kotlin3.genesisBlock
import com.innoq.blockchain.kotlin3.model.Block
import com.innoq.blockchain.kotlin3.model.Blocks
import com.innoq.blockchain.kotlin3.model.MineInfo
import com.innoq.blockchain.kotlin3.model.NodeInfo
import com.innoq.blockchain.kotlin3.nodeId
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import sun.security.krb5.Confounder.bytes
import kotlin.math.roundToInt


@RestController
class BlocksController(val objectMapper: ObjectMapper) {

    val digest = MessageDigest.getInstance("SHA-256")
    var blocks = listOf<Block>(genesisBlock)

    @GetMapping
    fun root(): Mono<NodeInfo> {
        return Mono.just(NodeInfo(nodeId.toString(), blocks.size.toLong()))
    }

    @GetMapping("/blocks")
    fun blocks(): Mono<Blocks> {
        return Mono.just(Blocks(blocks))
    }

    @GetMapping("/mine")
    fun mine(): Mono<MineInfo> {
        return Mono.fromCallable {
            val stopWatch = StopWatch()
            stopWatch.start()

            val previousBlock = blocks.last()
            val previousBlockHash = generateSHA256(previousBlock)

            var candidateBlock = Block(
                    previousBlock.index + 1,
                    (System.currentTimeMillis() / 1000L),
                    0,
                    emptyList(),
                    previousBlockHash)

            while(!generateSHA256(candidateBlock).startsWith("0000")) {
                candidateBlock = candidateBlock.copy(proof = candidateBlock.proof + 1)
            }

            blocks += candidateBlock

            stopWatch.stop()
            MineInfo(
                    "Mined a new block in %ds. Hashing power: %d hashes/s.".format(
                            stopWatch.totalTimeSeconds.roundToInt(),
                            (candidateBlock.proof / stopWatch.totalTimeSeconds).roundToInt()
                    ),
                    candidateBlock
            ) }
    }

    fun generateSHA256(value: Block): String {
        return generateSHA256(objectMapper.writeValueAsString(value))
    }

    fun generateSHA256(value: String): String {
        return digest.digest(value.toByteArray(StandardCharsets.UTF_8))
                .fold("", { str, it -> str + "%02x".format(it) })
    }
}