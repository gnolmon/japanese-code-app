package com.ana.DrawCharacter;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {

	List<StrokePath> strokesDrawn = new ArrayList<StrokePath>();
	StrokePath neoStroke;
	StrokePath strokesDrawing;
	OnCheckStrokeListener oncheck;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	public void setRemaintoOffsetStroke(int offset) {
		if (offset < 0)
			strokesDrawn.clear();
		for (int i = offset + 1; i < strokesDrawn.size(); i++) {
			strokesDrawn.remove(i);
		}
	}
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();

		//Draw canvas
		Paint canvasPaint = new Paint();
		canvasPaint.setAntiAlias(true);
		canvasPaint.setStyle(Style.FILL);
		canvasPaint.setColor(Color.WHITE);
		canvas.drawRect(new RectF(0, 0, width, height), canvasPaint);
		
		//Draw window component
		Paint windowPaint = new Paint();
		windowPaint.setARGB(255, 0, 0, 0);
		windowPaint.setStyle(Style.STROKE);
		windowPaint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
		canvas.drawRect(new RectF(0, 0, width-3, height-4), windowPaint);
		canvas.drawLine(0, height / 2, width, height / 2, windowPaint);
		canvas.drawLine(width / 2, 0, width / 2, height, windowPaint);

		//Draw strokes
		canvasPaint.setStyle(Style.STROKE);
		canvasPaint.setStrokeWidth(2.0f);
		canvasPaint.setStrokeWidth(4.0f);
		canvasPaint.setColor(Color.BLACK);
		drawAStroke(strokesDrawing, canvas, canvasPaint);
		for (int j = 0; j < strokesDrawn.size(); j++) {
			drawAStroke(strokesDrawn.get(j), canvas, canvasPaint);

		}
		// if (neoStroke != null){
		// paint.setColor(Color.YELLOW);
		// drawAStroke(neoStroke, canvas, paint);
		//
		// }
	}

	private void drawAStroke(StrokePath stroke, Canvas canvas, Paint paint) {
		if (stroke != null && stroke.size() != 0) {
			//Draw stroke continuous
			for (int i = 1; i < stroke.size(); i++) {
				canvas.drawLine(stroke.get(i - 1).x, stroke.get(i - 1).y,
						stroke.get(i).x, stroke.get(i).y, paint);
			}
			
			//Draw point ends
//			for (int i = 0; i < stroke.size(); i++) {
//				canvas.drawCircle(stroke.get(i).x, stroke.get(i).y, 3, paint);
//			}
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			strokesDrawing = new StrokePath();
			break;
		case MotionEvent.ACTION_MOVE:
			strokesDrawing.add(event.getX(), event.getY());

			break;
		case MotionEvent.ACTION_UP:
			strokesDrawn.add(strokesDrawing);
			strokesDrawing = null;
			if (oncheck!= null ){oncheck.onCheck();}
			
			// updateHiraganaViews();
			// neoStroke = strokesDrawing.getReduceStroke(0.05f);
			// invalidate();
			break;
		}
		invalidate();
		return true;
	}

	public void erase() {
		strokesDrawn = new ArrayList<StrokePath>();
		strokesDrawing = new StrokePath();
		neoStroke = null;
		invalidate();
	}
	public List<StrokePath> getCharStrokes() {
		return this.strokesDrawn;
	}

	public void setOnCheckListener(OnCheckStrokeListener listener) {
		this.oncheck = listener;
	}
	interface OnCheckStrokeListener {
		public void onCheck();
	}
}
