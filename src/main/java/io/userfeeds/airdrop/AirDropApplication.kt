package io.userfeeds.airdrop

import io.userfeeds.airdrop.collecting.AddressCollectingService
import io.userfeeds.airdrop.processing.AddressProcessingService
import io.userfeeds.airdrop.update.AlreadyProcessedService
import io.userfeeds.cache.util.logger
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@SpringBootApplication
@EnableScheduling
class AirDropApplication

fun main(args: Array<String>) {
    runApplication<AirDropApplication>(*args)
}

@Component
class CLR(
        private val alreadyProcessedService: AlreadyProcessedService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        alreadyProcessedService.updateProcessedAddressesList()
    }
}

@Component
class Worker(
        private val collector: AddressCollectingService,
        private val processor: AddressProcessingService
) {

    private val logger = logger()

    @Scheduled(fixedDelay = 10_000)
    fun run() {
        try {
            logger.info("updateOwnerList start")
            collector.updateOwnerList()
            processor.processOwners()
            logger.info("updateOwnerList end")
        } catch (e: Throwable) {
            logger.error("updateOwnerList error", e)
            throw e
        }
    }
}