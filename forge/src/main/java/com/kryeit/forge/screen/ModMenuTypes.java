package com.kryeit.forge.screen;

import com.kryeit.Main;
import com.kryeit.forge.MainForge;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

public class ModMenuTypes {

    public static final MenuEntry<ExchangeATMMenu> EXCHANGE_ATM_MENU =
            register("exchange_atm_menu", ExchangeATMMenu::new, () -> ExchangeATMScreen::new);



    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
            String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return MainForge.REGISTRATE
                .menu(name, factory, screenFactory)
                .register();
    }

    public static void register() {

    }
}
