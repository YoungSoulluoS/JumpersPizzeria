package io.github.JumperOnJava.lavajumper.mixin;

import io.github.JumperOnJava.lavajumper.gui.GuiHelper;
import io.github.JumperOnJava.lavajumper.gui.widgets.ScrollListWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * fix for scissors so they work fine with Subscreen.
 */
@Mixin(DrawContext.class)
public abstract class ScissorMixin {
    @Shadow public abstract MatrixStack getMatrices();

    @ModifyArg(method = "enableScissor",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext$ScissorStack;push(Lnet/minecraft/client/gui/ScreenRect;)Lnet/minecraft/client/gui/ScreenRect;"),index = 0)
    private ScreenRect enableScissor(ScreenRect rect){
        var ul = GuiHelper.transformCoords(getMatrices(),rect.getLeft(),rect.getTop());
        var dr = GuiHelper.transformCoords(getMatrices(),rect.getRight(),rect.getBottom());
        return new ScreenRect((int) ul.x, (int) ul.y,(int) (dr.x-ul.x), (int) (dr.y-ul.y));
    }
    @Inject(method = "scissorContains", at = @At("HEAD"),cancellable = true)
    private void ignoreScissorContains(int x, int y, CallbackInfoReturnable<Boolean> cir){
        if(ScrollListWidget.renderingEntries){
            cir.setReturnValue(true);
        }
    }
}
