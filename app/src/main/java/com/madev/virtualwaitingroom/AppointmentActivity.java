package com.madev.virtualwaitingroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.DatePicker.DatePickerFragment;
import com.madev.virtualwaitingroom.Interface.ItemClickListener;
import com.madev.virtualwaitingroom.Model.Appointment;
import com.madev.virtualwaitingroom.Model.FCMSendData;
import com.madev.virtualwaitingroom.Model.MyResponse;
import com.madev.virtualwaitingroom.Model.Notification;
import com.madev.virtualwaitingroom.Model.Token;
import com.madev.virtualwaitingroom.Model.UserAppointment;
import com.madev.virtualwaitingroom.Remote.IFCMService;
import com.madev.virtualwaitingroom.ViewHolder.AppointViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Button btn_selectDate;
    TextView tv_selectedDate;
    RecyclerView rv_appointment;
    LinearLayoutManager linearLayoutManager;
    FirebaseDatabase db;
    DatabaseReference appointList,userAppointList;
    IFCMService ifcmService;
    String branchName;
    FirebaseRecyclerAdapter<Appointment, AppointViewHolder> adapter;
    RelativeLayout rootLayout;
    io.reactivex.rxjava3.disposables.CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        btn_selectDate = (Button)findViewById(R.id.btn_selectDate);
        tv_selectedDate = (TextView)findViewById(R.id.tv_selectedDate);
        db = FirebaseDatabase.getInstance();
        branchName = getIntent().getStringExtra("BranchName");
        ifcmService = Common.getFCMClient();
        rv_appointment = (RecyclerView)findViewById(R.id.rv_appointment);
        rv_appointment.setHasFixedSize(true);
        rv_appointment.setLayoutManager(new GridLayoutManager(this, 3, GridLayout.VERTICAL, false));
        compositeDisposable =  new CompositeDisposable();


        btn_selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");



            }
        });


    }

    private void loadAppointList() {
        adapter = new FirebaseRecyclerAdapter<Appointment, AppointViewHolder>(Appointment.class, R.layout.appoint_item, AppointViewHolder.class, appointList) {
            @Override
            protected void populateViewHolder(AppointViewHolder appointViewHolder, Appointment appointment, int i) {
                appointViewHolder.tv_appoint_time.setText(appointment.getTimeframe());
                appointViewHolder.tv_appoint_avaliability.setText(appointment.getIsAvaliable());
                if(appointViewHolder.tv_appoint_avaliability.getText().equals("Zauzeto"))
                    appointViewHolder.tv_appoint_avaliability.setTextColor(Color.RED);
                else{
                    appointViewHolder.tv_appoint_avaliability.setTextColor(getResources().getColor(R.color.baseColor));
                }



                appointViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });


            }
        };
        adapter.notifyDataSetChanged();
        rv_appointment.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        view.setMinDate(System.currentTimeMillis() - 1000);
        Calendar c = Calendar.getInstance();
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());

        currentDateString = outputFormat.format(c.getTime());

        tv_selectedDate.setText(currentDateString);
        appointList = db.getReference("Appointment").child("Stomatoloska Ordinacija Maric").child(tv_selectedDate.getText().toString());
        loadAppointList();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getTitle().equals("Rezerviši termin")){
            addNewAppoint(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        return super.onContextItemSelected(item);
    }

    private void addNewAppoint(String key, Appointment item) {


        appointList = db.getReference("Appointment").child("Stomatoloska Ordinacija Maric").child(tv_selectedDate.getText().toString());
        UserAppointment userAppointment = new UserAppointment(branchName,tv_selectedDate.getText().toString(),item.getTimeframe(),"");
        userAppointList = db.getReference("UserAppoint");
        userAppointList.push().setValue(userAppointment);
        appointList.child(key).child("isAvaliable").setValue("Zauzeto");

       /* DatabaseReference tokens = db.getReference("Tokens");
        tokens.orderByKey().equalTo(Common.currentUser.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Token token = snapshot.getValue(Token.class);
                Notification notification = new Notification("Virtualna čekaonica","Nova rezervacija od : " + Common.currentUser.getFirstName() + " "+Common.currentUser.getLastName());
                FCMSendData content = new FCMSendData(token.getToken(),notification);
                ifcmService.sendNotification(content)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if(response.body().success == 1){
                                    Toast.makeText(AppointmentActivity.this,"Nova rezervacija",Toast.LENGTH_SHORT);
                                }
                                else{
                                    Toast.makeText(AppointmentActivity.this,"Status narudzbe je promijenjen ali notifikacija nije poslana",Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e("ERROR",t.getMessage());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        Toast.makeText(AppointmentActivity.this,"Termin uspješno rezervisan",Toast.LENGTH_LONG).show();
    }


}