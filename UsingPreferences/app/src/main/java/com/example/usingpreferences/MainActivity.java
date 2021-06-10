package com.example.usingpreferences;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static  MainActivity ma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Deklarasi dan Menginisialisasi variable nama dengan Label Nama dari Layout MainActivity */
        TextView nama = findViewById(R.id.tv_namaMain);

        /* Men-set Label Nama dengan data User sedang login dari Preferences */
        nama.setText(Preferences.getLoggedInUser(getBaseContext()));

        /* Men-set Status dan User yang sedang login menjadi default atau kosong di
         * Data Preferences. Kemudian menuju ke LoginActivity*/
        findViewById(R.id.button_logoutMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Menghapus Status login dan kembali ke Login Activity
                Preferences.clearLoggedInUser(getBaseContext());
                startActivity(new Intent(getBaseContext(),login.class));
                finish();
            }
        });
        Button btn = findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, BuatBiodata.class);
                startActivity(intent);
            }
        });
        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }
    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("select * from biodata", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        ListView01 = findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                final CharSequence[] dialogitem = {"Lihat Biodata", "Update Biodata", "Hapus Biodata"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0 :
                                Intent i = new Intent(getApplicationContext(), LihatBiodata.class);
                                i.putExtra("nama", selection);
                                startActivity(i);
                                break;
                            case 1 :
                                Intent in = new Intent(getApplicationContext(), UpdateBiodata.class);
                                in.putExtra("nama", selection);
                                startActivity(in);
                                break;
                            case 02:
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("delete from biodata where nama = '"+selection+"'");
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();
    }
}