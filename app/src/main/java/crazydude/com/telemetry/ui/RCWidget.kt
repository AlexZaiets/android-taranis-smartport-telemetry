package crazydude.com.telemetry.ui

import android.R.attr
import android.app.PendingIntent
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import crazydude.com.telemetry.R


class RCWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var height: Float = 0f

    private var rcChannels : IntArray = IntArray(0);

    private var showExclamationMark = true;

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int

        val desiredWidth =
            Math.ceil(0.1 * heightSize + 0.3 * heightSize * 15 + 0.2 * heightSize).toInt();
        val desiredHeight = heightSize

        //Measure Width
        width = if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            Math.min(desiredWidth, widthSize)
        } else {
            //Be whatever you want
            desiredWidth
        }

        //Measure Height
        height = if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            Math.min(desiredHeight, heightSize)
        } else {
            //Be whatever you want
            desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        height = h.toFloat()
    }

    //1000...2000 -> 0...1
    private fun getRCChannelFloat( channelIndex : Int) : Float
    {
        if ( this.rcChannels.size > channelIndex ) {
            return (this.rcChannels[channelIndex]- 1000) / 1000.0f;
        }
        return 1.0f;
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {

/*
            val paint2 = Paint()
            paint2.color = Color.parseColor("#909090")
            paint2.style = Paint.Style.FILL
            canvas.drawPaint(paint2)

 */

            for (ch in 0..15) {
                var x = height * 0.1f + ch * height * 0.3f;
                var x2 = x + height * 0.15f
                var top = height * 0.1f;
                var bottom = height * 0.75f;
                var middleY = (top + bottom) / 2.0f
                var h = (bottom - top);
                var strokeWidth = this.height / 25.0f;

                //paint.color = Color.rgb(34, 177, 76)
                paint.color = Color.rgb(255, 255, 255)
                paint.style = Paint.Style.FILL;

                var v = this.getRCChannelFloat(ch);
                it.drawRect(
                    x,
                    bottom - strokeWidth / 2.0f,
                    x2,
                    bottom - strokeWidth / 2.0f - (h - strokeWidth) * v,
                    paint
                )

                //paint.color = Color.rgb(255, 255, 255)
                paint.color = Color.rgb(180, 180, 180)
                paint.style = Paint.Style.STROKE;
                paint.strokeWidth = strokeWidth;

                it.drawRect(x, top, x2, height * 0.75f, paint)
                it.drawLine(x, middleY, x2, middleY, paint)
            }

            paint.color = Color.rgb(180, 180, 180)
            paint.style = Paint.Style.FILL;
            paint.textSize = this.height / 5.0f
            paint.textAlign = Paint.Align.CENTER
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            for (ch in 0..15) {
                var x = height * 0.1f + ch * height * 0.3f + height * 0.15f / 2.0f;
                canvas.drawText((ch + 1).toString(), x, height * 0.95f, paint);
            }

            if (showExclamationMark)
            {
                var x : Int =  Math.round(0.1 * height + 0.3 * height * 15 + 0.2 * height).toInt() / 2
                var y = Math.round(height*0.06f);
                var w = Math.round(height);
                var h = Math.round(height*0.9f);
                val d = context.resources.getDrawable( R.drawable.ic_exclamation_triangle)
                d?.bounds = Rect(x-w/2, y, x+w/2, y + h )
                d?.draw(canvas)
            }
        }
    }

    public fun setChannels(channels: IntArray)
    {
        this.rcChannels = channels.clone();
        invalidate()
    }

    public fun setShowExclamationMark(value : Boolean)
    {
        if ( this.showExclamationMark != value ) {
            this.showExclamationMark = value;
            invalidate()
        }
    }

}