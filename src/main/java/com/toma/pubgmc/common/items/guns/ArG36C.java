package com.toma.pubgmc.common.items.guns;

import java.util.ArrayList;
import java.util.List;

import com.toma.pubgmc.init.PMCItems;
import com.toma.pubgmc.init.PMCSounds;
import com.toma.pubgmc.util.handlers.ConfigHandler;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ArG36C extends GunBase
{
	public ArG36C(String name)
	{
		super(name);
		setMaxStackSize(1);
		
		setDamage(ConfigHandler.g36c);
		setVelocity(12f);
		setGravityModifier(0.005f);
		setGravityStartTime(5);
		setFireRate(2);
		setReloadTime(82);
		setVerticalRecoil(3.0f);
		setHorizontalRecoil(1.0f);
		
		canSwitchMode(true);
		setAutoFiremode(true);
		setBurstFire(false);
		setReloadType(ReloadType.MAGAZINE);
		setFiremode(Firemode.SINGLE);
		setAmmoType(AmmoType.AMMO556);
		setGunType(GunType.AR);
		
		setGunSound(PMCSounds.gun_g36c);
		setGunSilencedSound(PMCSounds.gun_g36c_silenced);
		setGunSoundVolume(10f);
		setGunSilencedSoundVolume(7f);
	}
	
	@Override
	public int getWeaponAmmoLimit(ItemStack stack)
	{
		return stack.hasTagCompound() && stack.getTagCompound().getInteger("magazine") > 1 ? 40 : 30;
	}
	
	@Override
	public SoundEvent getWeaponReloadSound()
	{
		return PMCSounds.reload_g36c;
	}
	
	@Override
	public List<ItemStack> getCraftingRecipe(Item item) 
	{
		List<ItemStack> r = new ArrayList<ItemStack>();
		r.add(new ItemStack(Items.IRON_INGOT, 50));
		r.add(new ItemStack(PMCItems.STEEL_INGOT, 40));
		return r;
	}
	
	@Override
	public List<Item> acceptedAttachments()
	{
		addARAttachments(true);
		return super.acceptedAttachments();
	}
}