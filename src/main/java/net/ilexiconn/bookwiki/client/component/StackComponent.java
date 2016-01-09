package net.ilexiconn.bookwiki.client.component;

import net.ilexiconn.bookwiki.BookWiki;
import net.ilexiconn.bookwiki.api.IComponent;
import net.ilexiconn.bookwiki.client.gui.BookWikiGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public class StackComponent extends Gui implements IComponent {
    @Override
    public char getID() {
        return 's';
    }

    @Override
    public String init(String string, String arg, String group) {
        return string.replace(group, group + "\n\n");
    }

    @Override
    public void render(Minecraft mc, BookWiki bookWiki, String arg, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        int stackSize = 1;
        if (arg.contains("*")) {
            String[] s = arg.split("\\*");
            arg = s[0];
            stackSize = Integer.parseInt(s[1]);
        }
        ItemStack stack = new ItemStack(Item.getByNameOrId(arg), stackSize);
        mc.getTextureManager().bindTexture(gui.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(x + 50, y, 292, 127, 18, 18, 512.0F, 512.0F);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().zLevel = 100.0F;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x + 51, y + 1);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x + 51, y + 1, null);
        mc.getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public void drawTooltip(Minecraft mc, BookWiki bookWiki, String arg, int x, int y, int mouseX, int mouseY) {
        int stackSize = 1;
        if (arg.contains("*")) {
            String[] s = arg.split("\\*");
            arg = s[0];
            stackSize = Integer.parseInt(s[1]);
        }
        ItemStack stack = new ItemStack(Item.getByNameOrId(arg), stackSize);
        if (mouseX + 1 >= x + 51 && mouseY + 1 >= y + 1 && mouseX + 1 < x + 51 + 18 && mouseY + 1 < y + 1 + 18) {
            renderToolTip(mc, stack, mouseX, mouseY);
        }
    }

    public void renderToolTip(Minecraft mc, ItemStack stack, int x, int y) {
        List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + list.get(i));
            } else {
                list.set(i, EnumChatFormatting.GRAY + list.get(i));
            }
        }

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int i = 0;

        for (String s : list) {
            int j = mc.fontRendererObj.getStringWidth(s);
            if (j > i) {
                i = j;
            }
        }

        int l1 = x + 12;
        int i2 = y - 12;
        int k = 8;

        if (list.size() > 1) {
            k += 2 + (list.size() - 1) * 10;
        }

        zLevel = 300.0F;
        int l = -267386864;
        drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
        drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
        drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
        drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
        drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
        int i1 = 1347420415;
        int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
        drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
        drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
        drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
        drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);
        zLevel = 0.0F;

        for (int k1 = 0; k1 < list.size(); ++k1) {
            String s1 = list.get(k1);
            mc.fontRendererObj.drawStringWithShadow(s1, (float) l1, (float) i2, -1);
            if (k1 == 0) {
                i2 += 2;
            }
            i2 += 10;
        }

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
}
