package net.fabricmc.example

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient
import net.minecraft.client.resource.metadata.AnimationResourceMetadata
import net.minecraft.client.texture.*
import net.minecraft.util.Identifier
import java.io.File
import java.io.FileInputStream
import java.io.FileReader

object EndersSpriteLoader {
    private var isFirstLoad = true
    private var loader: SpriteLoader? = null
    private var atlas: SpriteAtlasTexture? = null
    private val gson = Gson()
    val atlasId = id("textures/atlas/pain.png")

    fun loadSpriteNoMeta(imageFile: File, id: Identifier, imageFormat: NativeImage.Format): Sprite {
        return loadWithCustomMeta(imageFile, AnimationResourceMetadata.EMPTY, id, imageFormat)
    }

    fun loadSprite(imageFile: File, id: Identifier, imageFormat: NativeImage.Format): Sprite {
        return loadWithCustomMeta(imageFile, File("${imageFile.absoluteFile.path}.mcmeta"), id, imageFormat)
    }

    fun loadWithCustomMeta(imageFile: File, metaFile: File, id: Identifier, imageFormat: NativeImage.Format): Sprite {
        return loadWithCustomMeta(imageFile, gson.fromJson(FileReader(metaFile), JsonObject::class.java), id, imageFormat)
    }

    fun loadWithCustomMeta(imageFile: File, meta: JsonObject, id: Identifier, imageFormat: NativeImage.Format): Sprite {
        return loadWithCustomMeta(imageFile, AnimationResourceMetadata.READER.fromJson(meta), id, imageFormat)
    }

    fun loadWithCustomMeta(imageFile: File, meta: AnimationResourceMetadata, id: Identifier, imageFormat: NativeImage.Format): Sprite {
        if (atlas == null)
            atlas = SpriteAtlasTexture(atlasId)

        if (loader == null)
            loader = SpriteLoader.fromAtlas(atlas!!)

        if (isFirstLoad) {
            MinecraftClient.getInstance().textureManager.registerTexture(atlasId, atlas!!)
            isFirstLoad = false
        }

        val native = NativeImage.read(imageFormat, FileInputStream(imageFile))
        val contents = SpriteContents(id, SpriteDimensions(native.width, native.height), native, meta)
        val result = loader!!.method_47663(listOf(contents), 0) {}
        atlas!!.upload(result)

        return atlas!!.getSprite(id)!!
    }

}
