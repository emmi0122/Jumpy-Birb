package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.*;

/**
 * @author of this class is Hampus Ram
 */
public class ParallaxBackground {
    private TextureRegion[] bgLayers;
    private float[] parallaxSpeeds;
    private float[] scrolls;

    private float viewportWidth;
    private float cameraSpeed;

    /**
     * Creates a parallax background effect.
     *
     * Supports any number of background layers, two and over. Supply them from
     * the furthest back and forward.
     *
     * @param viewportWidth the width of the viewport
     * @param cameraSpeed the speed in pixels/s
     * @param bgLayers the different background layers
     *
     * @throws IllegalArgumentException if the number of backgrounds are less
     * than two.
     */
    public ParallaxBackground(float viewportWidth, float cameraSpeed, TextureRegion ... bgLayers) {

        this.viewportWidth = viewportWidth;
        this.cameraSpeed = cameraSpeed;
        this.bgLayers = bgLayers;
        this.parallaxSpeeds = intervals(0.2f, 0.5f, bgLayers.length);
        this.scrolls = new float[bgLayers.length];
    }

    public void update(float delta) {
        for (int i = 0; i < scrolls.length; ++i) {
            scrolls[i] += cameraSpeed * parallaxSpeeds[i] * delta;
            scrolls[i] %= bgLayers[i].getRegionWidth();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < scrolls.length; ++i) {
            drawLayer(batch, bgLayers[i], scrolls[i]);
        }
    }

    private void drawLayer(SpriteBatch batch, TextureRegion layer, float scrollX) {
        float x = -scrollX;

        // Draw enough tiles to cover the screen
        while (x < viewportWidth) {
            batch.draw(layer, x, 0);
            x += layer.getRegionWidth();
        }
    }

    private static float[] intervals(float min, float max, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("Count must be at least 2.");
        }

        float[] result = new float[count];
        float step = (max - min) / (count - 1);

        for (int i = 0; i < count; i++) {
            result[i] = min + i * step;
        }

        return result;
    }
}
