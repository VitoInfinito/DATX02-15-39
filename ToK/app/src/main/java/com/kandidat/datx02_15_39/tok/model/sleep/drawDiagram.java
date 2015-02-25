package com.kandidat.datx02_15_39.tok.model.sleep;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by tomashasselquist on 25/02/15.
 */
public class DrawDiagram extends View {
    Paint paint = new Paint();

    public DrawDiagram(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
        System.out.println("HEERERZSDZSADASDSADS");
        canvas.drawLine(0, 0, 20, 20, paint);
        canvas.drawLine(20, 0, 0, 20, paint);
    }

}

