package com.example.hex_a_gone.board;

import com.example.hex_a_gone.draw.HexBatch;

import java.util.Vector;

public class EffectManager {
    public EffectManager(){
        reset();
    }

    final float duration = .3f;
    private float timer = 0;
    private Vector<Effect> effects;

    public boolean active(){
        return timer > 0f;
    }

    public void update(float delta){
        timer = Math.max(timer - delta, 0);
    }

    public void draw(HexBatch batch){
        if(timer > 0)
        for(Effect e : effects){
            e.draw(batch);
        }
    }

    public void reset(){
        timer = duration; //duration of the effect
        effects = new Vector<>();
    }

    public void zeroTime(){
        timer = 0;
    }

    void addStationary(final float x, final float y, final int number){
        effects.add(batch -> batch.drawHexValue(x, y, number));
    }

    void addRandom(final float x, final float y, final int number){
        effects.add(0, batch -> batch.drawHexValue(x, y, number, 1 - timer / duration));
    }

    void addShift(final float x0, final float y0, final float x1, final float y1, final int number){
        effects.add(batch->{
                float dx = x1 - x0;
                float dy = y1 - y0;

                float shift = 1 - timer / duration;

                batch.drawHexValue(x0 + dx * shift, y0 + dy * shift, number);
            }
        );
    }
    void addIncrease(final float x0, final float y0, final float x1, final float y1, final int number){
        effects.add(batch -> {
            float dx = x1 - x0;
            float dy = y1 - y0;

            float shift = 1 - timer / duration;
            batch.drawHexValue(x0 + dx * shift, y0 + dy * shift, number);
            batch.drawHexValue(x0 + dx * shift, y0 + dy * shift, number + 1, shift);
        });
    }

    interface Effect {
        void draw(HexBatch batch);
    }
}
