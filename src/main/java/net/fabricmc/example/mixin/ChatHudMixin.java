package net.fabricmc.example.mixin;

import net.fabricmc.example.EndersSpriteLoader;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

import static net.fabricmc.example.ExampleModKt.*;


@Mixin(ChatHud.class)
public class ChatHudMixin extends DrawableHelper {
    private Sprite img = null;

    @Inject(at = @At("TAIL"), method = "render")
    private void render(MatrixStack matrices, int currentTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (img != null) {
            DrawableHelper.drawSprite(matrices, 100, 10, 100, img.getX(), img.getY(), img);
        }

    }

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V")
    private void addMessage(Text message, @Nullable MessageSignatureData signature, @Nullable MessageIndicator indicator, CallbackInfo ci) {
        TextContent content = message.getContent();
        if (content instanceof TranslatableTextContent) {
            String key = ((TranslatableTextContent) content).getKey();
            MutableText value = (MutableText) ((TranslatableTextContent) content).getArgs()[1];
            String x = ((LiteralTextContent) value.getContent()).string();


            if (key.contains("chat.type")) {
                try {
                    URL url = new URL("https://cdn.discordapp.com/attachments/873102829852696576/1082431666687062016/image.png");
//                            "https://cdn.discordapp.com/attachments/873102829852696576/1082431666687062016/image.png"

                    downloadFile(url, new File("./imageCache/pain/.png"));
                    img = EndersSpriteLoader.INSTANCE.loadSpriteNoMeta(new File("./imageCache/pain/.png") , id("pain"), NativeImage.Format.RGB);


//                    LOGGER.info(c.toString());
                } catch (IOException e) {

                    LOGGER.warn(e.getMessage());

                }
            }
        }
    }

    private static ByteBuffer imageToBuffer(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4);
        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF));
            buffer.put((byte) ((pixel >> 8) & 0xFF));
            buffer.put((byte) (pixel & 0xFF));
            buffer.put((byte) ((pixel >> 24) & 0xFF));
        }
        buffer.flip();
        return buffer;
    }
}
