package player;

import org.lwjgl.glfw.GLFW;
import settings.KeySetting;

import static utils.Constants.*;

public final class Hotbar {

    public static final int LENGTH = 9;

    public final byte[] contents = new byte[LENGTH];


    public Hotbar() {
        contents[0] = WATER;
        contents[1] = LAVA;
        contents[2] = GRASS;
        contents[3] = DIRT;
        contents[4] = STONE;
        contents[5] = COBBLESTONE;
        contents[6] = SAND;
        contents[7] = GLASS;
        contents[8] = GRAVEL;
    }


    public void handleInput(int button, int action) {
        if (action != GLFW.GLFW_PRESS) return;

        if (button == KeySetting.HOT_BAR_SLOT_1.value()) setSelectedSlot(0);
        if (button == KeySetting.HOT_BAR_SLOT_2.value()) setSelectedSlot(1);
        if (button == KeySetting.HOT_BAR_SLOT_3.value()) setSelectedSlot(2);
        if (button == KeySetting.HOT_BAR_SLOT_4.value()) setSelectedSlot(3);
        if (button == KeySetting.HOT_BAR_SLOT_5.value()) setSelectedSlot(4);
        if (button == KeySetting.HOT_BAR_SLOT_6.value()) setSelectedSlot(5);
        if (button == KeySetting.HOT_BAR_SLOT_7.value()) setSelectedSlot(6);
        if (button == KeySetting.HOT_BAR_SLOT_8.value()) setSelectedSlot(7);
        if (button == KeySetting.HOT_BAR_SLOT_9.value()) setSelectedSlot(8);

        if (button == KeySetting.DROP.value()) contents[selectedSlot] = AIR;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        selectedSlot %= LENGTH;
        if (selectedSlot < 0) selectedSlot += LENGTH;
        this.selectedSlot = selectedSlot;
    }


    private int selectedSlot = 0;
}
