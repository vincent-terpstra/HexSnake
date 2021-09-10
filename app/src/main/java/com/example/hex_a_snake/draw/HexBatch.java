package com.example.hex_a_snake.draw;

import android.content.Context;

import com.example.hex_a_snake.R;
import com.example.hex_a_snake.control.Button;
import com.example.hex_a_snake.model.HexXYZ;
import com.example.hex_a_snake.model.PointXY;

public class HexBatch extends QuadBatch {
    private final ColorSource colors ;


    final float[] hexRegion;
    final float[][] numbers;


    public final float[] newGame;
    public final float[] undo;
    public HexBatch(Context context){
        super(500, new int[]{2, 2, 4});
        colors = new ColorSource(context);
        AtlasReader reader = new AtlasReader(context, R.raw.images);

        hexRegion = reader.CreateCoordinates("hex", .5f, .5f);

        numbers= new float[10][];
        for(int i = 0; i< 10; i++){
            numbers[i] = reader.CreateCoordinates("" + i, -.5f, .5f);
        }

        undo = reader.CreateCoordinates("undo", -.4f, .4f);
        newGame = reader.CreateCoordinates("newgame", -.4f, .4f);
    }

    static final float RATIO = (float)Math.sqrt(3) / 2f;

    private final float hexScreenX(float x){
        return -x * RATIO;
    }
    private final float hexScreenY(float x, float y){
        return (-x / 2 + y);
    }

    private void draw(float[] coords, float x, float y, float scale, float[] clr){
        draw(coords, x, y, scale, 0, clr);
    }

    private void draw(float[] coords, float x, float y, float cos, float sin, float[] clr){
        if(idx >= coords.length)
            end();


        for(int i = 0; i < coords.length;) {
            float dx = coords[i++];
            float dy = coords[i++];
        //XY
            vertexArray[idx++] = dx * cos + dy * sin + x;
            vertexArray[idx++] = dy * cos - dx * sin + y;
            //UV
             vertexArray[idx++] = coords[i++];
             vertexArray[idx++] = coords[i++];
        //RGBA
             vertexArray[idx++] = clr[0];
            vertexArray[idx++] = clr[1];
            vertexArray[idx++] = clr[2];
            vertexArray[idx++] = 1.0f;
        }
    }

    public void drawEndGame(){
        draw(hexRegion, 0,0, 0, 4, new float[]{.4f,0.1f,0.1f});
    }

    public void drawHexValue(float x, float y, int number) {
        drawHexValue(x, y, number, 1);
    }

    public void drawHexValue(float x, float y, int number, float width) {
        y = hexScreenY(x, y);
        x = hexScreenX(x);

        if(number == 0){
            return;
        }

        drawShadow(hexRegion, x, y, width, width, number);

        if(width > .3f) {
            draw(hexRegion, x, y, .9f * width, colors.getShadow(number));
            draw(hexRegion, x - .01f, y -.02f, .85f * width, 0, colors.background);
        }

        drawNumber(number, x, y, width);
    }

    public void drawShape(HexXYZ hex, int color, float width){
        drawShape(hex.getX(), hex.getY(), color, width);
    }

    public void drawShape(float x, float y, int color, float width) {
        y = hexScreenY(x, y);
        x = hexScreenX(x);
        drawShadow(hexRegion, x, y, width, width, color);
    }

    private void drawHex(float x, float y, float width, int clr){
        drawShadow(hexRegion, x, y, width, width, clr);

        if(width > .3f) {
            draw(hexRegion, x, y, .9f * width, colors.getShadow(clr));
            draw(hexRegion, x - .01f, y -.02f, .85f * width, 0, colors.background);
        }
    }

    private final void drawShadow(float[] region, float x, float y, float width,  float multi, int color){
        draw(region, x - .06f * multi, y - .05f * multi, width, colors.getShadow(color));
        draw(region, x, y, width, colors.getColor(color));
    }

    public void drawHexPoint(PointXY point, int depth, float width) {

        drawShadow(hexRegion, point.x(), point.y(), width, width,15-depth);
    }

    public void drawNumber(int number, float x, float y, float multi){
        x-=.01f * multi;
        final float shift = .18f * multi;
        if(number > 9)
            x += shift;

        drawInt(number % 10, x, y, multi, number);
        if(number > 9) {
            x -= 2 * shift;
            drawInt(number / 10, x, y, multi, number);
        }
    }

    void drawInt(int num, float x, float y, float multi, int color){
        drawShadow(numbers[num], x, y, .35f * multi, .45f * multi, color);
    }

    public void drawScore(float x, float y, int score, int color){
        //setPackedColor(shadow);

        drawRow(score, x -.06f, y-.06f, 1, colors.getShadow(color));
        drawRow(score, x, y, 1, colors.getColor(color));
        drawRow(score, x, y,.9f,  colors.getShadow(color));
        drawRow(score, x -.01f, y - .02f, .85f, colors.background);

        do {
            drawInt(score % 10, x, y, 1f, color);
            x -= .4f;
        } while((score /= 10) > 0);

    }

    void drawRow(int s, float x, float y, float scale, float[] clr){
        do {
            draw(hexRegion, x, y, scale, clr);
            x-= .2f;
            if(s >= 10)
                draw(hexRegion, x, y, scale, clr);
            x-= .2f;

        } while((s/=10) > 0);
    }

    public void drawButton(Button button, float[] region, int clr){
        float x = button.x();
        float y = button.y();

        drawHex(x,y, 1, clr);

        if(x > 0){
            float rad = .225f;
            float multi = .2f;
            drawShadow(hexRegion, x, y + rad, multi, multi, clr);
            drawShadow(hexRegion, x + rad * RATIO, y - rad * .5f, multi, multi, clr);
            drawShadow(hexRegion, x - rad * RATIO, y - rad * .5f, multi, multi, clr);
            drawShadow(hexRegion, x, y - rad, multi, multi, clr);
            drawShadow(hexRegion, x + rad * RATIO, y + rad * .5f, multi, multi, clr);
            drawShadow(hexRegion, x - rad * RATIO, y + rad * .5f, multi, multi, clr);
            //drawNumber(1, x ,y+ rad, multi);
            //drawNumber(1, x +rad * RATIO ,y - rad * .5f , multi);
            //drawNumber(2, x - rad * RATIO,y - rad * .5f, multi);
        } else {
            drawShadow(region, x, y, .9f, .9f, clr);
        }
    }

    public void drawIndicator(HexXYZ direction, int clr) {
        if (direction.getX() == 0 && direction.getY() == 0)
            return;

        float dx = direction.getX();
        float dy = direction.getY();

        float multi = 2.9f;
        float x = multi * hexScreenX(dx);
        float y = multi * hexScreenY(dx, dy);

        drawHex(x + .5f * hexScreenX(-dy), y + .5f * hexScreenY(-dy, -dy + dx), .3f, clr);
        drawHex(x + .5f * hexScreenX(-dx + dy), y + .5f * hexScreenY(-dx + dy, -dx), .3f, clr);

        drawShadow(hexRegion, x, y, .6f,.8f,clr );
        draw(hexRegion, x, y, .45f, colors.background);
    }
/*
    public void drawHexValue(float x, float y, int colorIdx){
        drawHexValue(x, y, colorIdx, 1);
    }

    public void drawHexValue(float x, float y, int colorIdx, float width){
        if(colorIdx == 0) return;
        y = hexScreenY(x, y);
        x = hexScreenX(x);
        drawHex(x, y, colorIdx, width );
        drawNumber(colorIdx, x, y, width);
    }

    public void drawHex(PointXY point, int colorIdx, float width){
        setColor(colorIdx);
        drawShadow(hexRegion, point.x(), point.y(), width, width ,width);
    }

    public void drawHex(float x, float y, int colorIdx, float width){
        setColor(colorIdx);
        drawShadow(hexRegion, x , y , width, width, width);
        if(width > .3f) {
            setPackedColor(colors.background);
            draw(hexRegion, x, y, .9f * width, .9f * width);
        }
    }
 */
}
