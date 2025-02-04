package me.cominixo.betterf3.mixin;

import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.cominixo.betterf3.utils.Utils.START_X_POS;
import static me.cominixo.betterf3.utils.Utils.closingAnimation;
import static me.cominixo.betterf3.utils.Utils.xPos;

/**
 * Modifies the debug keys (f3 / f3 + m).
 */
@Mixin(Keyboard.class)
public class KeyboardMixin {
  @Shadow @Final private MinecraftClient client;

  /**
   * Adds the config menu by pressing f3 + m.
   *
   * @param key key pressed with f3
   * @param cir Callback info
   */
  @Inject(method = "processF3", at = @At("HEAD"))
  public void processF3(final int key, final CallbackInfoReturnable<Boolean> cir) {
    if (key == 77) { // Key m
      this.client.setScreen(new ModConfigScreen(null));
    }
  }

  /**
   * Plays the animation on f3 keypress.
   *
   * @param window window
   * @param key key
   * @param scancode scancode
   * @param i i
   * @param j j
   * @param ci Callback info
   */
  @Inject(method = "onKey", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "net/minecraft/client" +
  "/option/GameOptions.debugEnabled : Z"), cancellable = true)
  public void onDebugActivate(final long window, final int key, final int scancode, final int i, final int j, final CallbackInfo ci) {

    if (GeneralOptions.enableAnimations) {
      if (this.client.options.debugEnabled) {
        closingAnimation = true;
        ci.cancel();
      } else {
        closingAnimation = false;
        xPos = START_X_POS;
      }
    }
  }

}
