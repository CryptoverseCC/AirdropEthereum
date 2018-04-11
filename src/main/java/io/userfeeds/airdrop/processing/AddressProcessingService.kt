package io.userfeeds.airdrop.processing;

import io.reactivex.Completable
import io.reactivex.Observable
import org.springframework.stereotype.Component

@Component
class AddressProcessingService(
        private val mongoDB: AddressProcessing.MongoDB,
        private val processor: AddressProcessing.Processor
) {

    fun processOwners() {
        Observable.fromIterable(mongoDB.getNotProcessedOwnersAddresses())
                .buffer(300)
                .flatMapCompletable {
                    processBatch(it)
                            .doOnComplete { mongoDB.saveProcessedOwnersAddresses(it) }
                }
                .blockingGet()
    }

    private fun processBatch(addresses: List<String>): Completable {
        return Completable.fromCallable {
            processor.processAddresses(addresses)
        }
    }
}
