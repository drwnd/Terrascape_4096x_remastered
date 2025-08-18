package assets.identifiers;

public enum TextureIdentifier implements AssetIdentifier {

    MATERIALS("assets/textures/atlas256.png"),
    DAY_SKY("assets/textures/82984-skybox-blue-atmosphere-sky-space-hd-image-free-png.png"),
    NIGHT_SKY("assets/textures/706c5e1da58f47ad6e18145165caf55d.png"),
    CROSSHAIR("assets/textures/CrossHair.png"),
    HOT_BAR("assets/textures/HotBar.png"),
    HOT_BAR_SELECTION_INDICATOR("assets/textures/HotBarSelectionIndicator.png"),
    INVENTORY_OVERLAY("assets/textures/InventoryOverlay.png"),
    PROPERTIES("assets/textures/properties256.png"),
    TEXT("assets/textures/textAtlas.png"),
    GUI_ELEMENT_BACKGROUND("assets/textures/GuiElementBackground.png"),
    TOGGLE_ACTIVATED("assets/textures/ToggleActivated.png"),
    TOGGLE_DEACTIVATED("assets/textures/ToggleDeactivated.png");

    TextureIdentifier(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    private final String filepath;
}
