package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.common.container.PMCPlayerContainer;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SPacketOpenPlayerInventory implements NetworkPacket<SPacketOpenPlayerInventory> {

    @Override
    public void encode(SPacketOpenPlayerInventory instance, PacketBuffer buf) {

    }

    @Override
    public SPacketOpenPlayerInventory decode(PacketBuffer buf) {
        return new SPacketOpenPlayerInventory();
    }

    @Override
    public void handle(SPacketOpenPlayerInventory instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            NetworkHooks.openGui(ctx.get().getSender(), new INamedContainerProvider() {
                final ITextComponent DISPLAY = new TranslationTextComponent("");
                @Override
                public ITextComponent getDisplayName() {
                    return DISPLAY;
                }

                @Nullable
                @Override
                public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                    return new PMCPlayerContainer(p_createMenu_1_, p_createMenu_2_, p_createMenu_3_);
                }
            });
        });
    }
}
