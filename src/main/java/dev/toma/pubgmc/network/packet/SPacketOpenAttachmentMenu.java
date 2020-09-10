package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.common.container.AttachmentContainer;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SPacketOpenAttachmentMenu implements NetworkPacket<SPacketOpenAttachmentMenu> {

    private ItemStack stack;

    public SPacketOpenAttachmentMenu() {

    }

    public SPacketOpenAttachmentMenu(ItemStack stack) {
        this.stack = stack;
    }
    
    @Override
    public void encode(SPacketOpenAttachmentMenu instance, PacketBuffer buf) {
        buf.writeItemStack(instance.stack);
    }

    @Override
    public SPacketOpenAttachmentMenu decode(PacketBuffer buf) {
        return new SPacketOpenAttachmentMenu(buf.readItemStack());
    }

    @Override
    public void handle(SPacketOpenAttachmentMenu instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(instance.stack.getItem() instanceof GunItem) {
                NetworkHooks.openGui(player, new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("container.attachments");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
                        return new AttachmentContainer(windowID, playerInventory, instance.stack);
                    }
                }, buf -> buf.writeItemStack(instance.stack));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
