package com.example.puzzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.puzzle.Model.Board;
import com.example.puzzle.Model.Place;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainView;
    private Board board;
    private BoardView boardView;
    private int boardSize = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView    = (ViewGroup) findViewById(R.id.mainLayout);
        getSupportActionBar().setTitle("Bermain Puzzle Huruf");
        this.newGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void newGame() {
        this.board = new Board(this.boardSize);
        this.board.addBoardChangeListener(boardChangeListener);
        this.board.rearrange();
        this.mainView.removeView(boardView);
        this.boardView = new BoardView(this, board);
        this.mainView.addView(boardView);
//        this.moves.setText("Number of movements: 0");
    }

    public void changeSize(int newSize) {
        if (newSize != this.boardSize) {
            this.boardSize = newSize;
            this.newGame();
            boardView.invalidate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                FragmentManager fm = getSupportFragmentManager();
                SettingsDialogFragment settings = new SettingsDialogFragment(this.boardSize);
                settings.show(fm, "fragment_settings");
                break;
            case R.id.action_new_game:
                new AlertDialog.Builder(this)
                        .setTitle("Ulangi")
                        .setMessage("Apakah anda yakin untuk mengulangi game ?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        board.rearrange();
//                                        moves.setText("Number of movements: 0");
                                        boardView.invalidate();
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                })
                        .show();
                break;
            case R.id.action_exit:
                new AlertDialog.Builder(this)
                        .setTitle("Keluar")
                        .setMessage("Apakah anda yakin untuk keluar dari game ?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Board.BoardChangeListener boardChangeListener = new Board.BoardChangeListener() {
        public void tileSlid(Place from, Place to, int numOfMoves) {
//            moves.setText("Number of movements: " + Integer.toString(numOfMoves));
        }

        public void solved(int numOfMoves) {
//            moves.setText("Solved in " + Integer.toString(numOfMoves) + " moves!");
            Toast.makeText(getApplicationContext(), "You won it!",
                    Toast.LENGTH_LONG).show();
        }
    };

    public static class SettingsDialogFragment extends DialogFragment {

        private int size;
        public SettingsDialogFragment(int size) {
            this.size = size;
        }
        void setSize(int size) {
            this.size = size;
        }
        int getSize() {
            return this.size;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Pilih Ukuran Puzzle")
                    .setSingleChoiceItems(R.array.size_options, this.size - 2,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    setSize(which + 2);

                                }

                            })
                    .setPositiveButton("Pilih",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    ((MainActivity) getActivity())
                                            .changeSize(getSize());
                                }
                            })
                    .setNegativeButton("Batal",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });

            return builder.create();
        }
    }
}