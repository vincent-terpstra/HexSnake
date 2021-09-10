package com.example.hex_a_snake.control;

public class HexControl { //implements InputProcessor {
    /*
    public HexControl(NextHexGame game){
        this.game = game;
    }
    float _x, _y;

    public  final HexXYZ hex = new HexXYZ();
    private final NextHexGame game;

    private int touches = 0;
    private float touchX(int screenX){
        return (float)screenX * 6.0f / Gdx.graphics.getWidth() - 3;
    }

    private float touchY(int screenY){
        return (Gdx.graphics.getHeight() * 0.5f -(float)screenY) * 6.0f / Gdx.graphics.getWidth();
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(++touches == 2 || button  == 1) {

            HexBatch.colors.switchColor();
            return true;
        } else if (touches != 1){
            return false;
        }
        float x = touchX(screenX);
        float y = touchY(screenY);

        if(game.newGame.touchDown(x,y, pointer) || game.undo.touchDown(x, y, pointer))
            return true;

        set(screenX, screenY);
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        float x = touchX(screenX);
        float y = touchY(screenY);

        if(game.newGame.touchUp(x,y, pointer) || game.undo.touchUp(x, y, pointer)){
        } else if(touches == 1) {
            game.move(hex);
        }
        hex.set(0,0);

        if(!Gdx.input.isTouched()){
            touches = 0;
        }
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        set(screenX, screenY);
        return false;
    }

    private final void set(int screenX, int screenY){
        float _height = Gdx.graphics.getHeight();
        float _width  = Gdx.graphics.getWidth();

        _x =  screenX;
        _y = _height - screenY;

        hex.setDirection(screenX / _width - .5f, (-screenY + _height / 2) / _width);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
     */
}
