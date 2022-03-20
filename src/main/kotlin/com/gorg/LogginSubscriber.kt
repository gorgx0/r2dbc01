package com.gorg

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import reactor.core.publisher.BaseSubscriber

class LogginSubscriber<T>: BaseSubscriber<T>() {
    override fun hookOnNext(value: T) {
        println("onNext: ${value.toString()}")
        println(value.toString())
    }

    override fun hookOnSubscribe(subscription: Subscription) {
        println("Subscribed - $subscription")
        subscription.request(10)
    }

    override fun hookOnError(throwable: Throwable) {
        super.hookOnError(throwable)
    }

    override fun hookOnComplete() {
        println("Completed!")
    }
}