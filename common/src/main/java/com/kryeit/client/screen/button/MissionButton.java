package com.kryeit.client.screen.button;

import com.kryeit.Main;
import com.kryeit.Utils;
import com.kryeit.client.ClientsideActiveMission;
import com.kryeit.client.screen.MissionScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MissionButton extends Button {
    public static final ResourceLocation GUI_WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation ADVANCEMENT_WIDGETS = new ResourceLocation("textures/gui/advancements/widgets.png");
    private final boolean completed;
    private final ItemStack item;
    protected final OnTooltip onTooltip;

    protected final ClientsideActiveMission mission;
    private final MissionScreen screen;

    public MissionButton(MissionScreen screen, int x, int y, int width, int height, Component message, ClientsideActiveMission mission, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, width, height, message, onPress);
        this.completed = mission.isCompleted();
        this.item = mission.itemStack();
        this.onTooltip = onTooltip;
        this.screen = screen;
        this.mission = mission;
    }

    @Override
    public void renderButton(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderButtonTexture(matrices);
        drawText(matrices);
        tooltip(matrices, mouseX, mouseY);
    }

    public void drawText(PoseStack matrices) {
        int color = completed ? 0x00FF00 : Utils.getTitleColor(mission.difficulty());
        Font font = Minecraft.getInstance().font;
        AbstractWidget.drawCenteredString(matrices, font, this.getMessage(), this.x + this.width / 2 + 7, this.y + (this.height - 8) / 2, color);
    }

    public void tooltip(PoseStack matrices, int mouseX, int mouseY) {
        if (this.isMouseOver(mouseX, mouseY)) {
            screen.tooltipRunnables.add(() -> this.onTooltip.onTooltip(this, matrices, mouseX, mouseY));
        }
    }

    public void renderButtonTexture(PoseStack matrices) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(GUI_WIDGETS);

        int u = 0;
        int v = 66;

        if(isHovered) {
            v += 20;
        }

        int buttonWidth = 200;
        int buttonHeight = 20;

        int x = this.x;
        int y = this.y;

        RenderSystem.setShaderTexture(0, GUI_WIDGETS);
        blit(matrices, x, y, u, v, buttonWidth, buttonHeight, 256, 256);
        renderItem(matrices);
    }


    public void renderItem(PoseStack matrices) {
        renderBelowItem(matrices);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int textureX = x + width / 2 - 92;
        int textureY = y + height / 2 - 8; // Center of the button, minus half the size of the texture
        itemRenderer.renderGuiItem(item, textureX, textureY);
    }


    public void renderBelowItem(PoseStack matrices) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(ADVANCEMENT_WIDGETS);

        int u = 0;
        int v = 154;

        if(isHovered || mission.isCompleted()) {
            v -= 26;
        }

        if(Objects.equals(mission.difficulty(),"normal")) {
            u = 26 * 2;
        }

        if(Objects.equals(mission.difficulty(), "hard")) {
            u = 26;
        }

        int textureSize = 26;

        int x = this.x + 3;
        int y = this.y - 3;

        RenderSystem.setShaderTexture(0, ADVANCEMENT_WIDGETS);
        blit(matrices, x, y, u, v, textureSize, textureSize, 256, 256);
    }
}
