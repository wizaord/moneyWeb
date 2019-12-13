package com.wizaord.Helpers

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network


private lateinit var MONGO: MongodExecutable

fun startMongoDatabase() {
    val version: Version.Main = Version.Main.V4_0
    val port = 27018
    val config: IMongodConfig = MongodConfigBuilder()
            .version(version)
            .net(Net(port, Network.localhostIsIPv6()))
            .build()
    MONGO = MongodStarter.getDefaultInstance().prepare(config)
    MONGO.start()
}

fun stopMongoDatabase() {
    MONGO.stop()
}