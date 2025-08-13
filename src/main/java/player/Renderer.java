package player;

import org.joml.Vector2f;
import renderables.Renderable;

public final class Renderer extends Renderable {
    public Renderer() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));
        allowScaling(false);

    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {

    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }
}
