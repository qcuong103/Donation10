package ie.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ie.app.R;
import ie.app.api.DonationApi;
import ie.app.database.DBManager;
import ie.app.main.DonationApp;
import ie.app.models.Donation;

public class Base  extends AppCompatActivity {
    public DonationApp app;
    public DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (DonationApp) getApplication();
        app.dbManager.open();
        app.dbManager.setTotalDonated(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.dbManager.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_donate, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        super.onPrepareOptionsMenu(menu);
        MenuItem report = menu.findItem(R.id.menuReport);
        MenuItem donate = menu.findItem(R.id.menuDonate);
        MenuItem reset = menu.findItem(R.id.menuReset);

        if(app.dbManager.getAll().isEmpty())
        {
            report.setEnabled(false);
            reset.setEnabled(false);
        }
        else {
            report.setEnabled(true);
            reset.setEnabled(true);
        }

        if(this instanceof Donate){
            donate.setVisible(false);
            if(!app.dbManager.getAll().isEmpty())
            {
                report.setVisible(true);
                reset.setEnabled(true);
            }
        }
        else {
            report.setVisible(false);
            donate.setVisible(true);
            reset.setVisible(false);
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllTask(this).execute("/getalldonation");
    }

    public void report(MenuItem item)
    {
        startActivity (new Intent(this, Report.class));
    }
    public void donate(MenuItem item)
    {
        startActivity (new Intent(this, Donate.class));
    }
    public void reset(MenuItem item) {}
}

class GetAllTask extends AsyncTask<String, Void, List<Donation>> {
    protected ProgressDialog dialog;
    protected Context context;
//    public Donate donate;
//    public DonationApp app;

    public GetAllTask(Context context)
    {
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(context, 1);
        this.dialog.setMessage("Retrieving Donations List");
        this.dialog.show();
    }
    @Override
    protected List<Donation> doInBackground(String... params) {
        try {
            Log.v("donate", "Donation App Getting All Donations");
            return (List<Donation>) DonationApi.getAll((String) params[0]);
        }
        catch (Exception e) {
            Log.v("donate", "ERROR : " + e);
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(List<Donation> result) {
        super.onPostExecute(result);
        //use result to calculate the totalDonated amount here
//        donate.progressBar.setProgress(app.totalDonated);
//        donate.amountTotal.setText("$" + app.totalDonated);
        if (dialog.isShowing())
            dialog.dismiss();
    }
}

