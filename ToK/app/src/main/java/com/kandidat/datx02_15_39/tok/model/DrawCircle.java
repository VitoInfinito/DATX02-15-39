package com.kandidat.datx02_15_39.tok.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Adam on 17/02/15.
 */
public class DrawCircle extends View {
    public DrawCircle(Context context){
        super(context);
    }

    public void onDraw(Canvas canvas){



        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setARGB(255, 255,255,255);

        canvas.drawColor(Color.YELLOW);


        canvas.drawCircle(10,10,5, p);
    }
}
