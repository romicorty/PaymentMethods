package com.example.romina.payments.network;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.romina.payments.R;
import com.example.romina.payments.model.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodListActivity extends AppCompatActivity {
    private PaymentMethodAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_list);
        List<PaymentMethod> payments = new ArrayList<>();
        mAdapter = new PaymentMethodAdapter(this,R.layout.payment_method_item,payments);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        loadPaymentMethods();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment_method, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadPaymentMethods(){
        List<PaymentMethod> list = new ArrayList<>();
        PaymentMethod p1 = new PaymentMethod();
        p1.setName("Visa");
        list.add(p1);
        PaymentMethod p2 = new PaymentMethod();
        p2.setName("American Express");
        list.add(p2);
        mAdapter.updatePaymentMethods(list);
    }
}
