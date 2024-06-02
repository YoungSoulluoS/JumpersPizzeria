package io.github.JumperOnJava.lavajumper.mixin;

import io.github.JumperOnJava.lavajumper.gui.AskScreen;
import io.github.JumperOnJava.lavajumper.gui.widgets.SubScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(Screen.class)
public class ScreenMixins {
    //SnIgnoreRenderAfterOverlay
    @Inject(method = "render",at = @At(value = "INVOKE",shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/Drawable;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"),locals = LocalCapture.CAPTURE_FAILHARD)
    public void cancelRenderAfterOverlay(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, Iterator<Drawable> var5, Drawable drawable){
        if(drawable instanceof AskScreen.OverlayScreen)
        {
            while (var5.hasNext())var5.next();
        }
    }
//    @Inject(method = "applyBlur", at = @At("HEAD"),cancellable = true)
//    public void disableBlur(float delta, CallbackInfo ci){
//        if(SubScreen.blurDisabled){
//            ci.cancel();
//        }
//    }
}
