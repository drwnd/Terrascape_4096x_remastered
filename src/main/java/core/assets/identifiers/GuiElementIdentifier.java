package core.assets.identifiers;

import core.assets.AssetLoader;
import core.assets.GuiElement;

public interface GuiElementIdentifier extends AssetIdentifier<GuiElement> {

    Object[] attributes();

    int[] attributeSizes();

    @Override
    default GuiElement generateAsset() {
        return AssetLoader.loadGuiElement(this);
    }
}
