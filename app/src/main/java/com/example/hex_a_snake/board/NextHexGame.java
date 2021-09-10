package com.example.hex_a_snake.board;

public class NextHexGame  {
//	/**extends ApplicationAdapter {
//	private HexBatch batch;
//	private HexControl control;
//	private Dragon dragon ;
//	private EffectManager effectManager;
//
//	private Board[]    boards = new Board[3];
//	int boardIdx = 0;
//	int backtrack = 0;
//
//	public Button newGame, undo;
//
//	@Override
//	public void create () {
//	    effectManager = new EffectManager();
//
//	    batch = new HexBatch();
//		control = new HexControl(this);
//		dragon = new Dragon(-2.5f, 3.0f * (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth() - .5f);
//
//		newGame = new Button(){
//			@Override
//			public void run(){
//				boards[0] = new Board();
//				effectManager.reset();
//				boards[0].addRandom(4, effectManager);
//                endgame = false;
//				boardIdx = backtrack = 0;
//			}
//		};
//
//		newGame.run();
//
//		undo = new Button(){
//			@Override
//			public void run(){
//				if(backtrack > 0){
//					backtrack--;
//					boardIdx = (boardIdx + boards.length - 1) % boards.length;
//					endgame = false;
//				}
//			}
//		};
//		resume();
//	}
//
//	@Override
//	public void pause (){
//		Gdx.input.setInputProcessor(null);
//	}
//
//	@Override
//	public void resume(){
//		Gdx.input.setInputProcessor(control);
//	}
//
//	@Override
//    public void resize(int screenX, int screenY){
//	    batch.resize(screenX, screenY);
//		float y = .7f - 3f * screenY / screenX;
//		float x = 2.2f;
//
//		newGame.set(x, y);
//		undo.set(-x, y);
//    }
//
//	@Override
//	public void render () {
//		//ScreenCaptureUtils.capture();
//		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
//			batch.colors.switchColor();
//
//	    effectManager.update(Gdx.graphics.getDeltaTime());
//
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		dragon.drawDragon(batch);
//		/**/
//       if(endgame){
//           batch.setPackedColor(1000);
//           batch.draw(batch.hex, 0,0, 4f, 4f, 0, 1);
//       }
//
//
//        Board board = boards[boardIdx];
//
//		if(effectManager.active()){
//		    effectManager.draw(batch);
//        } else {
//            for(int x = -2; x <= 2; x++) {
//                for(int y = -2; y<=2; y++) {
//                    if(Board.inHex( x, y))
//                        batch.drawHexValue( x, y, board.get( x, y));
//                }
//            }
//        }
//
//		batch.setColor(board.max);
//		batch.drawButton(newGame, batch.newgame);
//		batch.drawButton(undo, batch.undo);
//
//		batch.drawScore(board.score);
//		batch.drawIndicator(control.hex);
//		/**/
//
//		batch.end();
//	}
//
//
//	@Override
//	public void dispose () {
//		batch.dispose();
//	}
//
//
//	static boolean endgame = false;
//	public void move(HexXYZ hex){
//		Board board = boards[boardIdx];
//		Board board2 = board.move(hex, effectManager);
//		if(!board.isEqual(board2)){
//			board2.addRandom(2, effectManager);
//			boardIdx = (boardIdx + 1) % boards.length;
//			boards[boardIdx] = board2;
//			backtrack = Math.min(backtrack + 1, boards.length - 1);
//			endgame = board2.checkEndgame();
//		} else {
//		    effectManager.zeroTime();
//        }
//	}
//
}
