package net.fabricmc.example

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

// This logger is used to write text to the console and the log file.
// It is considered best practice to use your mod id as the logger's name.
// That way, it's clear which mod wrote info, warnings, and errors.
@JvmField
val LOGGER: Logger = LoggerFactory.getLogger("modid")

@Suppress("unused")
fun onInitialize() {
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.

    LOGGER.info("Hello Fabric world!")

}

fun id(path: String): Identifier{
    return Identifier("modid",path)
}

fun downloadFile(url: URL, outputFile: File) {
    LOGGER.info("rand")
    url.openStream().use {
        Channels.newChannel(it).use { rbc ->
            FileOutputStream(outputFile).use { output ->
                output.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
            }
        }
    }
}