package core.assets.identifiers;

import core.assets.AssetLoader;
import core.assets.GuiElement;
import core.assets.GuiElementData;

public interface GuiElementIdentifier extends AssetIdentifier<GuiElement> {

    GuiElementData getData();

    @Override
    default GuiElement generateAsset() {
        return AssetLoader.loadGuiElement(getData());
    }
}
