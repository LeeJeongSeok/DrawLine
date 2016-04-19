package cubewiz.kr.drawline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawView vw;
    ArrayList<Vertex> arVertex;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);        // 기존 뷰는 사용하지 않는다.

        // 새로운 뷰를 생성하여 액티비티에 연결
        vw = new DrawView(this);
        setContentView(vw);
        arVertex = new ArrayList<Vertex>();

    }

    public class Vertex {
        float x;
        float y;
        boolean draw;   // 그리기 여부

        public Vertex(float x, float y, boolean draw) {
            this.x = x;
            this.y = y;
            this.draw = draw;
        }
    }

    protected class DrawView extends View {
        Paint mPaint;       // 페인트 객체 선언

        public DrawView(Context context) {
            super(context);

            // 페인트 객체 생성 후 설정
            mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(7);
            mPaint.setAntiAlias(true);      // 안티얼라이싱

        }

        /**
         * 터치이벤트를 받는 함수
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    arVertex.add(new Vertex(event.getX(), event.getY(), false));
                    break;
                case MotionEvent.ACTION_MOVE:
                    arVertex.add(new Vertex(event.getX(), event.getY(), true));
                    break;
                case MotionEvent.ACTION_UP: // 터치때는 순간 Base64 변환
                    Bitmap bitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    String str = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    Log.d("Base64 : ", str);
                    break;
            }

            invalidate();       // onDraw() 호출
            return true;
        }

        /**
         * 화면을 계속 그려주는 함수
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawColor(Color.WHITE);      // 캔버스 배경색깔 설정


            // 그리기
            for (int i = 0; i < arVertex.size(); i++) {
                if (arVertex.get(i).draw) {       // 이어서 그리고 있는 중이라면
                    canvas.drawLine(arVertex.get(i - 1).x, arVertex.get(i - 1).y,
                            arVertex.get(i).x, arVertex.get(i).y, mPaint);
                    // 이전 좌표에서 다음좌표까지 그린다.
                } else {
                    canvas.drawPoint(arVertex.get(i).x, arVertex.get(i).y, mPaint);
                    // 점만 찍는다.
                }
            }


        }
    }
}
