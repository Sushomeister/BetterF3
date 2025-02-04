package me.cominixo.betterf3.modules;

import com.mojang.blaze3d.platform.GlDebugInfo;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The System module.
 */
public class SystemModule extends BaseModule {

  /**
   * Instantiates a new System module.
   */
  public SystemModule() {
    this.defaultNameColor = TextColor.fromFormatting(Formatting.GOLD);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;

    lines.add(new DebugLine("java_version"));
    lines.add(new DebugLine("memory_usage"));
    lines.add(new DebugLine("allocated_memory"));
    lines.add(new DebugLine("cpu"));
    lines.add(new DebugLine("display"));
    lines.add(new DebugLine("gpu"));
    lines.add(new DebugLine("opengl_version"));
    lines.add(new DebugLine("gpu_driver"));

    for (final DebugLine line : lines) {
      line.inReducedDebug = true;
    }
  }

  /**
   * Updates the System module.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {
    final long maxMemory = Runtime.getRuntime().maxMemory();
    final long totalMemory = Runtime.getRuntime().totalMemory();
    final long freeMemory = Runtime.getRuntime().freeMemory();
    final long usedMemory = totalMemory - freeMemory;

    final Window window = client.getWindow();

    final String javaVersion = String.format("%s %dbit", System.getProperty("java.version"), client.is64Bit() ? 64 : 32);
    final String memoryUsage = String.format("% 2d%% %03d/%03d MB", usedMemory * 100 / maxMemory, usedMemory / 1024 / 1024, maxMemory / 1024 / 1024);
    final String allocatedMemory = String.format("% 2d%% %03dMB", totalMemory * 100 / maxMemory, totalMemory / 1024 / 1024);
    final String displayInfo = String.format("%d x %d (%s)", window.getFramebufferWidth(), window.getFramebufferHeight(), GlDebugInfo.getVendor());

    final String[] versionSplit = GlDebugInfo.getVersion().split(" ");

    final String openGlVersion = versionSplit[0];
    final String gpuDriverVersion = String.join(" ", ArrayUtils.remove(versionSplit, 0));

    lines.get(0).value(javaVersion);
    lines.get(1).value(Utils.percentColor((int) (usedMemory * 100 / maxMemory)) + memoryUsage);
    lines.get(2).value(allocatedMemory);
    lines.get(3).value(GlDebugInfo.getCpuInfo());
    lines.get(4).value(displayInfo);
    lines.get(5).value(GlDebugInfo.getRenderer());
    lines.get(6).value(openGlVersion);
    lines.get(7).value(gpuDriverVersion);
  }
}
