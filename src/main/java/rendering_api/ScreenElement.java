package rendering_api;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;

public abstract class ScreenElement {

    public ScreenElement(Vector2f sizeToParent, Vector2f offsetToParent) {
        this.sizeToParent = sizeToParent;
        this.offsetToParent = offsetToParent;
    }


    protected abstract void renderSelf(Vector2f position, Vector2f size);

    protected abstract void resizeSelfTo(int width, int height);


    public final void render(Vector2f parentPosition, Vector2f parentSize) {
        Vector2f thisSize = new Vector2f(parentSize).mul(sizeToParent);
        Vector2f thisPosition = new Vector2f(parentPosition).add(new Vector2f(parentSize).mul(offsetToParent));

        renderSelf(thisPosition, thisSize);
        for (ScreenElement child : children) child.render(thisPosition, thisSize);
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


    protected final ArrayList<ScreenElement> children = new ArrayList<>();
    protected final Vector2f sizeToParent;
    protected final Vector2f offsetToParent;
}
