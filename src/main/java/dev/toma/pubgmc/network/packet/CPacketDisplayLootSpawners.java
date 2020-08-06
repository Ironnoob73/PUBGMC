package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.client.render.block.LootSpawnerRenderer;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketDisplayLootSpawners implements NetworkPacket<CPacketDisplayLootSpawners> {

    public CPacketDisplayLootSpawners() {

    }

    @Override
    public void encode(CPacketDisplayLootSpawners instance, PacketBuffer buf) {
    }

    @Override
    public CPacketDisplayLootSpawners decode(PacketBuffer buf) {
        return new CPacketDisplayLootSpawners();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(CPacketDisplayLootSpawners instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            boolean b = !LootSpawnerRenderer.debugRender;
            LootSpawnerRenderer.debugRender = b;
            TextFormatting color = b ? TextFormatting.GREEN : TextFormatting.RED;
            String message = b ? "Marking all loot spawners" : "Stopping loot spawner render";
            Minecraft.getInstance().player.sendStatusMessage(new StringTextComponent(color.toString() + message), true);
        });
        ctx.get().setPacketHandled(true);
    }
}
