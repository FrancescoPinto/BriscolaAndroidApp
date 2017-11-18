package it.ma.polimi.briscola.forfullrelease.view;

/**
 * Created by utente on 03/11/17.
 */

//todo io farei così: https://developer.android.com/training/custom-views/custom-drawing.html
    //todo CREA DELLE CUSTOM VIEW SINGOLE (slot di carta, carta, contatore punteggio ecc. e queste le riusi in giro come vuoi
    //todo le carte di loro le puoi farre anche solo con una imageView, però deve avere la forma che vuoi tu ... quindi meglio disegnarle?
    //todo le riusi qui dentro

public class Briscola2PMatchView /* extends SurfaceView implements SurfaceHolder.Callback */{
/*
        private final SurfaceHolder holder;
        private MatchAnimatingThread thread;

        private float posX;
        private float posY;

        private Bitmap background;

        private Rect frameToDraw;
        private RectF whereToDraw;

        private DisplayMetrics metrics;

        public Briscola2PMatchView(Context context) {
            //todo: qui devi instanziare l'immagine di background, poi generare gli slot delle carte sul field, poi generare il deck
            super(context);
            // get the holder
            getHolder().addCallback(this);
            // init the SurfaceHolder
            holder = getHolder();
            // init bitmap background
            background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background); todo questa riga va rimessa
            // init display metrics
            metrics = context.getResources().getDisplayMetrics(); //prendi i dati del dispositivo!
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            // scale background to metrics
            background = Bitmap.createScaledBitmap(background, width, height, false);
            // init frames to draw adapting the screen
            frameToDraw = new Rect(0, 0, width, height);
            whereToDraw = new RectF(0, 0, width, height);

         //   monkey = BitmapFactory.decodeResource(context.getResources(), R.drawable.monkey); todo questa riga va rimessa
            monkey = getResizedBitmap(monkey, 270, 250);

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    monkey = getResizedBitmap(monkey, 470, 450);
                    break;
                case MotionEvent.ACTION_MOVE:
                    posX = event.getX() - Math.abs(monkey.getWidth() / 2);
                    posY = event.getY() - Math.abs(monkey.getHeight() / 2);
                    break;
                case MotionEvent.ACTION_UP:
                    monkey = getResizedBitmap(monkey, 270, 250);
                    break;

            }
            return true;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            thread = new MatchAnimatingThread(this, holder);
            thread.setGo(true);
            thread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            thread.setGo(false);
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void mDraw(Canvas canvas) {
            synchronized (holder) {

                // set background
                canvas.drawBitmap(background, frameToDraw, whereToDraw, null);
                // draw the monkey
                canvas.drawBitmap(monkey, posX, posY, null); //todo modifica questo
                // call this to update canvas changes
                invalidate();
            }
        }

        public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            bm.recycle();
            return resizedBitmap;
        }

        public MatchAnimatingThread getThread() {
            return thread;
        }*/
}
