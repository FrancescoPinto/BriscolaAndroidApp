package it.ma.polimi.briscola.controller;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import it.ma.polimi.briscola.view.Briscola2PMatchView;

/**
 * Created by utente on 03/11/17.
 */

public class MatchAnimatingThread /*extends Thread*/{

      /*  private Briscola2PMatchView matchView;
        private final SurfaceHolder holder;
        private boolean go = false;

        public MatchAnimatingThread(Briscola2PMatchView view, SurfaceHolder holder) {
            this.matchView = view;
            this.holder = holder;
        }

        @Override
        public void run() {
            Canvas c;
            while (go) {
                c = null;
                try {
                    c = holder.lockCanvas();
                    synchronized (holder) {
                        matchView.mDraw(c);
                    }
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            }
        }


        public void setGo(boolean go) {
            this.go = go;
        }*/
 }

