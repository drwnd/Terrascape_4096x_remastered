package rendering_api.screen_elements;

import org.joml.Vector2f;

public final class DummyScreenElement extends ScreenElement {

    static final DummyScreenElement dummy = new DummyScreenElement();

    public DummyScreenElement() {
        super(new Vector2f(), new Vector2f());
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {

    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }

    @Override
    public Vector2f getPosition() {
        return new Vector2f();
    }

    @Override
    public Vector2f getSize() {
        return new Vector2f(1.0f, 1.0f);
    }
}
