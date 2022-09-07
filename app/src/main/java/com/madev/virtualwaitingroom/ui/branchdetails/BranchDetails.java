package com.madev.virtualwaitingroom.ui.branchdetails;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.madev.virtualwaitingroom.AppointmentActivity;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.BranchesModel;
import com.madev.virtualwaitingroom.Model.CommentModel;
import com.madev.virtualwaitingroom.Model.UserAppointment;
import com.madev.virtualwaitingroom.R;
import com.madev.virtualwaitingroom.ui.comments.CommentFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BranchDetails extends Fragment implements DatePickerDialog.OnDateSetListener{

    private BranchDetailsViewModel branchDetailsViewModel;
    private Unbinder unbinder;
    FirebaseDatabase db;
    private android.app.AlertDialog waitingDialog;
    DatabaseReference appointList,userAppointList;

    @BindView(R.id.img_branch)
    ImageView img_branch;
    @BindView(R.id.btnDate)
    CounterFab btnDate;
    @BindView(R.id.btn_rating)
    FloatingActionButton btn_rating;
    @BindView(R.id.branch_name)
    TextView branch_name;
    @BindView(R.id.branch_description)
    TextView branch_description;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.btnShowComment)
    Button btnShowComment;

    @OnClick(R.id.btn_rating)
    void onRatingButtonClick(){
       showDialogRating(); 
    }

    @OnClick(R.id.btnShowComment)
    void OnShowCommentButtonClick(){
        CommentFragment commentFragment = CommentFragment.getInstance();
        commentFragment.show(getActivity().getSupportFragmentManager(),"CommentFragment");
    }


    private void showDialogRating() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ocijenite objekat");
        builder.setMessage("Molimo unesite informacije");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_rating_comment,null);

        RatingBar ratingBar = (RatingBar)itemView.findViewById(R.id.rating_bar);
        EditText edt_comment = (EditText)itemView.findViewById(R.id.edt_comment);

        builder.setView(itemView);

        builder.setNegativeButton("PONIŠTI", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            CommentModel commentModel = new CommentModel();
            commentModel.setName(Common.currentUser.getFirstName() +" "+ Common.currentUser.getLastName());
            commentModel.setUid(Common.currentUser.getPhone());
            commentModel.setComment(edt_comment.getText().toString());
            commentModel.setRatingValue(ratingBar.getRating());
            Map<String,Object> serverTimeStamp = new HashMap<>();
            serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP);
            commentModel.setCommentTimeStamp(serverTimeStamp);
            commentModel.setPhotoUrl(Common.currentUser.getImage());

            branchDetailsViewModel.setCommentModel(commentModel);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        branchDetailsViewModel =
                new ViewModelProvider(this).get(BranchDetailsViewModel.class);
        View root = inflater.inflate(R.layout.branch_details_fragment, container, false);
        unbinder = ButterKnife.bind(this,root);
        initViews();
        branchDetailsViewModel.getMutableLiveDataBranches().observe(getViewLifecycleOwner(), branchesModel -> {
            displayInfo(branchesModel);
        });

        branchDetailsViewModel.getMutableLiveDataComment().observe(this,commentModel -> {
           submitRatingToFirebase(commentModel);
        });

        return root;
    }

    private void initViews() {
        waitingDialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
    }

    private void submitRatingToFirebase(CommentModel commentModel) {
        waitingDialog.show();
        FirebaseDatabase.getInstance()
                .getReference(Common.COMMENT_REF)
                .child(Common.selectedFood.getId())
                .push()
                .setValue(commentModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        addRatingToBranch(commentModel.getRatingValue());
                    }
                    waitingDialog.dismiss();
                });
    }

    private void addRatingToBranch(float ratingValue) {
        FirebaseDatabase.getInstance()
                .getReference(Common.CATEGORY_REF)
                .child(Common.categorySelected.getMenu_id())
                .child("branches")
                .child(Common.selectedFood.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            BranchesModel branchesModel = snapshot.getValue(BranchesModel.class);
                            branchesModel.setKey(Common.selectedFood.getKey());
                            if(branchesModel.getRatingValue() == null)
                                branchesModel.setRatingValue(0d);
                            if(branchesModel.getRatingCount() == null)
                                branchesModel.setRatingCount((long) 01);

                            double sumRating = branchesModel.getRatingValue() + ratingValue;
                            long ratingCount = 0;
                            if(branchesModel.getRatingCount() >0)
                               ratingCount = branchesModel.getRatingCount()+1;
                            else
                                ratingCount = branchesModel.getRatingCount();

                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("ratingValue",sumRating);
                            updateData.put("ratingCount",ratingCount);

                            branchesModel.setRatingValue(sumRating);
                            branchesModel.setRatingCount(ratingCount);

                            snapshot.getRef().updateChildren(updateData)
                                    .addOnCompleteListener(task -> {
                                        waitingDialog.dismiss();
                                        if(task.isSuccessful()){
                                            Toast.makeText(getContext(),"Hvala vam!",Toast.LENGTH_SHORT).show();
                                            Common.selectedFood = branchesModel;
                                            branchDetailsViewModel.setBranchModel(branchesModel);
                                        }
                                    });
                        }
                        else
                            waitingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        waitingDialog.dismiss();
                        Toast.makeText(getContext(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void displayInfo(BranchesModel branchesModel) {
        Glide.with(getContext()).load(branchesModel.getImage()).into(img_branch);
        branch_name.setText(new StringBuilder(branchesModel.getName()));
        branch_description.setText(new StringBuilder(branchesModel.getDescription()));
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(branchesModel.getId().equals("0") ||branchesModel.getId().equals("1") || branchesModel.getId().equals("2") )
                    showBookingDialog();
                else{
                    Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                intent.putExtra("BranchName",branchesModel.getName());
                startActivity(intent);
                }


            }
        });
        if(branchesModel.getRatingValue()!=null)
        ratingBar.setRating(branchesModel.getRatingValue().floatValue() / branchesModel.getRatingCount());

        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Common.selectedFood.getName());
    }

    private void showBookingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Rezervišite termin");
        builder.setMessage("Molimo unesite informacije");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_booking,null);

        TextView tv_booking_date = (TextView)itemView.findViewById(R.id.tv_booking_date);
        ImageButton ib_datepicker = (ImageButton)itemView.findViewById(R.id.ib_datepicker);
        EditText edt_notes = (EditText)itemView.findViewById(R.id.edt_notes);
        Spinner spinner_doctors = (Spinner)itemView.findViewById(R.id.spinner_doctors);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner_doctors.setAdapter(adapter);

        spinner_doctors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ib_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                AtomicBoolean i = new AtomicBoolean(false);
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.date_time_picker,null);

                builder.setView(itemView);
                AlertDialog dialog = builder.create();
                dialog.show();


                itemView.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                itemView.findViewById(R.id.date_time_set).setOnClickListener((View.OnClickListener) view -> {

                    DatePicker datePicker = (DatePicker)itemView.findViewById(R.id.date_picker);


                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth());

                    String outputPattern = "dd-MMM-yyyy";
                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                    String currentDateString = outputFormat.format(calendar.getTime());
                    tv_booking_date.setText(currentDateString);
                    dialog.dismiss();


                });






            }
        });


        builder.setView(itemView);

        builder.setNegativeButton("PONIŠTI", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.setPositiveButton("OK", (dialog, which) -> {

            db = FirebaseDatabase.getInstance();
            appointList = db.getReference("Appointment").child("Frizerski salon Mostar").child(tv_booking_date.getText().toString());
            UserAppointment userAppointment = new UserAppointment(branch_name.getText().toString(),tv_booking_date.getText().toString(),tv_booking_date.getText().toString(),"U obradi");
            userAppointList = db.getReference("UserAppoint");
            userAppointList.push().setValue(userAppointment);
            dialog.dismiss();

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}