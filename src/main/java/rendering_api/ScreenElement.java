package rendering_api;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;

public abstract class ScreenElement {

    public final Input input;

    public ScreenElement(Vector2f sizeToParent, Vector2f offsetToParent, Input input) {
        this.sizeToParent = sizeToParent;
        this.offsetToParent = offsetToParent;
        this.input = input;
    }


    protected abstract void renderSelf(Vector2f offset);

    protected abstract void resizeSelfTo(int width, int height);


    public final void render(Vector2f parentOffset) {
        Vector2f thisOffset = new Vector2f(parentOffset).mul(offsetToParent);
        renderSelf(parentOffset);
        for (ScreenElement child : children) child.render(thisOffset);
    }

    public final void resize(Vector2i size, Vector2f parentSize) {
        Vector2f thisSize = new Vector2f(parentSize).mul(sizeToParent);
        resizeSelfTo((int) (size.x * thisSize.x), (int) (size.y * thisSize.y));
        for (ScreenElement child : children) child.resize(size, thisSize);
    }

    public void addRenderable(ScreenElement screenElement) {
        children.add(screenElement);
    }

    public void clearRenderables() {
        children.clear();
    }


    private final ArrayList<ScreenElement> children = new ArrayList<>();
    protected final Vector2f sizeToParent;
    protected final Vector2f offsetToParent;
}
