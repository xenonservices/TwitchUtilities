package services.xenon.twitchutilities.counter

import java.util.concurrent.CompletableFuture


fun blah()
{



    CompletableFuture.supplyAsync {
        "hello world"
    }.thenAccept {
        println("response is: $it")
    }


}